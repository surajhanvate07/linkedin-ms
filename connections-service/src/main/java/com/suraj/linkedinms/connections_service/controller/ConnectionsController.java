package com.suraj.linkedinms.connections_service.controller;

import com.suraj.linkedinms.connections_service.auth.UserContextHolder;
import com.suraj.linkedinms.connections_service.entity.Person;
import com.suraj.linkedinms.connections_service.service.ConnectionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/core")
public class ConnectionsController {

	private final ConnectionsService connectionsService;

	@GetMapping("/first-degree")
	public ResponseEntity<List<Person>> getFirstDegreeConnections() {
		List<Person> connections = connectionsService.getFirstDegreeConnections();
		return ResponseEntity.ok(connections);
	}

	@PostMapping("/send-connection-request/{toReceiverUserId}")
	public ResponseEntity<Boolean> sendConnectionRequest(@PathVariable("toReceiverUserId") Long toReceiverUserId) {
		boolean isRequestSent = connectionsService.sendConnectionRequest(toReceiverUserId);
		return ResponseEntity.ok(isRequestSent);
	}

	@PostMapping("/accept-connection-request/{fromSenderUserId}")
	public ResponseEntity<Boolean> acceptConnectionRequest(@PathVariable("fromSenderUserId") Long fromSenderUserId) {
		boolean isAccepted = connectionsService.acceptConnectionRequest(fromSenderUserId);
		return ResponseEntity.ok(isAccepted);
	}

	@PostMapping("/reject-connection-request/{fromSenderUserId}")
	public ResponseEntity<Boolean> rejectConnectionRequest(@PathVariable("fromSenderUserId") Long fromSenderUserId) {
		boolean isRejected = connectionsService.rejectConnectionRequest(fromSenderUserId);
		return ResponseEntity.ok(isRejected);
	}
}
