package com.suraj.linkedinms.notification_service.consumer;

import com.suraj.linkedinms.notification_service.client.ConnectionsClient;
import com.suraj.linkedinms.notification_service.dto.PersonDto;
import com.suraj.linkedinms.notification_service.entity.Notification;
import com.suraj.linkedinms.notification_service.service.SendNotification;
import com.suraj.linkedinms.post_service.event.PostCreatedEvent;
import com.suraj.linkedinms.post_service.event.PostLikedEvent;
import com.suraj.linkedinms.notification_service.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostsServiceConsumer {

	private final ConnectionsClient connectionsClient;
	private final SendNotification sendNotificationService;

	@KafkaListener(topics = "post-created-topic")
	public void handlePostCreatedEvent(PostCreatedEvent postCreatedEvent) {
		log.info("Handle Post Created Event: {}", postCreatedEvent);
		List<PersonDto> firstConnections = connectionsClient.getFirstDegreeConnections(postCreatedEvent.getCreatorId());

		for (PersonDto connection : firstConnections) {
			log.info("Notifying connection: {} about new post with ID: {}", connection.getId(), postCreatedEvent.getPostId());
			sendNotificationService.sendNotification(connection.getUserId(), "Your connection: " + postCreatedEvent.getCreatorId() + " has created a new post with content: " + postCreatedEvent.getContent());
		}

	}

	@KafkaListener(topics = "post-liked-topic")
	public void handlePostLikedEvent(PostLikedEvent postLikedEvent) {
		log.info("Handle Post Liked Event: {}", postLikedEvent);
		String message = "Your post with ID: " + postLikedEvent.getPostId() + " has been liked by user with ID: " + postLikedEvent.getLikedByUserId();
		sendNotificationService.sendNotification(postLikedEvent.getCreatorId(), message);
	}

}
