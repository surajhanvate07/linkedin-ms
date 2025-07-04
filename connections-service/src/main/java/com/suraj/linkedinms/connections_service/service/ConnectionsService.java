package com.suraj.linkedinms.connections_service.service;

import com.suraj.linkedinms.connections_service.entity.Person;

import java.util.List;

public interface ConnectionsService {
	List<Person> getFirstDegreeConnections();

	Boolean sendConnectionRequest(Long toReceiverUserId);

	Boolean acceptConnectionRequest(Long fromSenderUserId);

	Boolean rejectConnectionRequest(Long fromSenderUserId);
}
