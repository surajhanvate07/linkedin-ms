package com.suraj.linkedinms.connections_service.service.impl;

import com.suraj.linkedinms.connections_service.auth.UserContextHolder;
import com.suraj.linkedinms.connections_service.entity.Person;
import com.suraj.linkedinms.connections_service.repository.PersonRepository;
import com.suraj.linkedinms.connections_service.service.ConnectionsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ConnectionsServiceImpl implements ConnectionsService {

	private final PersonRepository personRepository;

	@Override
	public List<Person> getFirstDegreeConnections() {
		Long userId = UserContextHolder.getCurrentUserId();
		log.info("Fetching first degree connections for userId: {}", userId);
		return personRepository.getFirstDegreeConnections(userId);
	}
}
