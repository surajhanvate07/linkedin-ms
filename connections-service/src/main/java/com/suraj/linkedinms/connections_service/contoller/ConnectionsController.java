package com.suraj.linkedinms.connections_service.contoller;

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
}
