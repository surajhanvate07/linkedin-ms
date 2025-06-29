package com.suraj.linkedinms.connections_service.contoller;

import com.suraj.linkedinms.connections_service.entity.Person;
import com.suraj.linkedinms.connections_service.service.ConnectionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/core")
public class ConnectionsController {

	private final ConnectionsService connectionsService;

	@GetMapping("/{userId}/first-degree")
	public ResponseEntity<List<Person>> getFirstDegreeConnections(@PathVariable("userId") Long userId) {
		List<Person> connections = connectionsService.getFirstDegreeConnections(userId);
		return ResponseEntity.ok(connections);
	}
}
