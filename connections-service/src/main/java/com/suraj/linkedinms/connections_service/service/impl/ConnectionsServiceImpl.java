package com.suraj.linkedinms.connections_service.service.impl;

import com.suraj.linkedinms.connections_service.auth.UserContextHolder;
import com.suraj.linkedinms.connections_service.entity.Person;
import com.suraj.linkedinms.connections_service.repository.PersonRepository;
import com.suraj.linkedinms.connections_service.service.ConnectionsService;
import com.suraj.linkedinms.event.AcceptConnectionRequestEvent;
import com.suraj.linkedinms.event.SendConnectionRequestEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ConnectionsServiceImpl implements ConnectionsService {

	private final PersonRepository personRepository;
	private final KafkaTemplate<Long, SendConnectionRequestEvent> sendConnectionRequestEventKafkaTemplate;
	private final KafkaTemplate<Long, AcceptConnectionRequestEvent> acceptConnectionRequestEventKafkaTemplate;

	@Override
	public List<Person> getFirstDegreeConnections() {
		Long userId = UserContextHolder.getCurrentUserId();
		log.info("Fetching first degree connections for userId: {}", userId);
		return personRepository.getFirstDegreeConnections(userId);
	}

	@Override
	public Boolean sendConnectionRequest(Long toReceiverUserId) {
		Long senderUserId = UserContextHolder.getCurrentUserId();
		log.info("User with ID: {} is sending a connection request to user with ID: {}", senderUserId, toReceiverUserId);
		if (senderUserId.equals(toReceiverUserId)) {
			throw new IllegalArgumentException("Cannot send a connection request to oneself.");
		}
		if (toReceiverUserId == null) {
			throw new IllegalArgumentException("Receiver user ID cannot be null.");
		}

		boolean requestAlreadySent = personRepository.connectionRequestExists(senderUserId, toReceiverUserId);
		if (requestAlreadySent) {
			log.warn("Connection request already exists between user {} and user {}", senderUserId, toReceiverUserId);
			throw new IllegalStateException("Connection request already exists between user " + senderUserId + " and user " + toReceiverUserId);
		}

		boolean alreadyConnected = personRepository.alreadyConnected(senderUserId, toReceiverUserId);
		if (alreadyConnected) {
			log.warn("Users {} and {} are already connected.", senderUserId, toReceiverUserId);
			throw new IllegalStateException("Users " + senderUserId + " and " + toReceiverUserId + " are already connected.");
		}

		log.info("Adding connection request from user {} to user {}", senderUserId, toReceiverUserId);
		personRepository.addConnectionRequest(senderUserId, toReceiverUserId);
		log.info("Connection request added from user {} to user {}", senderUserId, toReceiverUserId);

		SendConnectionRequestEvent sendConnectionRequestEvent = SendConnectionRequestEvent.builder()
				.senderUserId(senderUserId)
				.receiverUserId(toReceiverUserId)
				.build();

		log.info("Publishing SendConnectionRequestEvent to Kafka: {}", sendConnectionRequestEvent);
		sendConnectionRequestEventKafkaTemplate.send("send-connection-request-topic", sendConnectionRequestEvent);
		log.info("SendConnectionRequestEvent published to Kafka successfully");
		return true;
	}

	@Override
	public Boolean acceptConnectionRequest(Long fromSenderUserId) {
		Long toReceiverUserId = UserContextHolder.getCurrentUserId();
		boolean connectionRequestExists = personRepository.connectionRequestExists(toReceiverUserId, fromSenderUserId);
		if (!connectionRequestExists) {
			log.warn("AcceptConnectionRequest: No connection request exists from user {} to user {}", toReceiverUserId, fromSenderUserId);
			throw new IllegalStateException("No connection request exists from user " + fromSenderUserId + " to user " + toReceiverUserId);
		}

		log.info("Accepting connection request from user {} to user {}", fromSenderUserId, toReceiverUserId);
		personRepository.acceptConnectionRequest(fromSenderUserId, toReceiverUserId);

		log.info("Connection request accepted from user {} to user {}", fromSenderUserId, toReceiverUserId);
		AcceptConnectionRequestEvent acceptConnectionRequestEvent = AcceptConnectionRequestEvent.builder()
				.senderUserId(fromSenderUserId)
				.receiverUserId(toReceiverUserId)
				.build();

		log.info("Publishing AcceptConnectionRequestEvent to Kafka: {}", acceptConnectionRequestEvent);
		acceptConnectionRequestEventKafkaTemplate.send("accept-connection-request-topic", acceptConnectionRequestEvent);
		log.info("AcceptConnectionRequestEvent published to Kafka successfully.");
		return true;
	}

	@Override
	public Boolean rejectConnectionRequest(Long fromSenderUserId) {
		Long toReceiverUserId = UserContextHolder.getCurrentUserId();
		boolean connectionRequestExists = personRepository.connectionRequestExists(toReceiverUserId, fromSenderUserId);
		if (!connectionRequestExists) {
			log.warn("RejectConnectionRequest: No connection request exists from user {} to user {}", toReceiverUserId, fromSenderUserId);
			throw new IllegalStateException("No connection request exists from user " + fromSenderUserId + " to user " + toReceiverUserId);
		}
		log.info("Rejecting connection request from user {} to user {}", fromSenderUserId, toReceiverUserId);
		personRepository.rejectConnectionRequest(fromSenderUserId, toReceiverUserId);
		return true;
	}

}
