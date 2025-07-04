package com.suraj.linkedinms.notification_service.consumer;

import com.suraj.linkedinms.connections_service.event.AcceptConnectionRequestEvent;
import com.suraj.linkedinms.connections_service.event.SendConnectionRequestEvent;
import com.suraj.linkedinms.notification_service.service.SendNotification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConnectionsServiceConsumer {

	private final SendNotification sendNotificationService;

	@KafkaListener(topics = "send-connection-request-topic")
	public void handleSendConnectionRequestEvent(SendConnectionRequestEvent sendConnectionRequestEvent) {
		log.info("Received Send Connection Request Event: {}", sendConnectionRequestEvent);
		Long senderUserId = sendConnectionRequestEvent.getSenderUserId();
		Long receiverUserId = sendConnectionRequestEvent.getReceiverUserId();

		log.info("Sending notification to user with ID: {} about connection request from user with ID: {}", receiverUserId, senderUserId);
		String message = "User with ID: " + senderUserId + " has sent you a connection request.";
		sendNotificationService.sendNotification(receiverUserId, message);
	}

	@KafkaListener(topics = "accept-connection-request-topic")
	public void handleAcceptConnectionRequestEvent(AcceptConnectionRequestEvent acceptConnectionRequestEvent) {
		log.info("Received Accept Connection Request Event: {}", acceptConnectionRequestEvent);
		Long senderUserId = acceptConnectionRequestEvent.getSenderUserId();
		Long receiverUserId = acceptConnectionRequestEvent.getReceiverUserId();

		log.info("Sending notification to user with ID: {} about accepted connection request from user with ID: {}", receiverUserId, senderUserId);
		String message = "User with ID: " + senderUserId + " has accepted your connection request.";
		sendNotificationService.sendNotification(receiverUserId, message);
	}
}
