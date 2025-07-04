package com.suraj.linkedinms.notification_service;

import com.suraj.linkedinms.notification_service.entity.Notification;
import com.suraj.linkedinms.notification_service.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/core")
@RequiredArgsConstructor
public class NotificationController {

	private final NotificationRepository notificationRepository;

	@GetMapping("/all")
	public ResponseEntity<List<Notification>> getAllNotifications() {
		return ResponseEntity.ok(notificationRepository.findAll());
	}
}
