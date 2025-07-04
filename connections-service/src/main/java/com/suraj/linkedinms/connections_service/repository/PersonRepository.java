package com.suraj.linkedinms.connections_service.repository;

import com.suraj.linkedinms.connections_service.entity.Person;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends Neo4jRepository<Person, Long> {

	Optional<Person> findByName(String name);

	@Query("MATCH (personA:Person)-[:CONNECTED_TO]-(personB:Person) WHERE personA.userId = $userId RETURN personB")
	List<Person> getFirstDegreeConnections(Long userId);

	@Query("MATCH (sender:Person)-[r:REQUESTED_TO]->(receiver:Person) " +
			"WHERE sender.userId = $senderUserId AND receiver.userId = $receiverUserId " +
			"RETURN COUNT(r) > 0")
	boolean connectionRequestExists(Long senderUserId, Long receiverUserId);

	@Query("MATCH (sender:Person)-[r:CONNECTED_TO]-(receiver:Person) " +
			"WHERE sender.userId = $senderUserId AND receiver.userId = $receiverUserId " +
			"RETURN COUNT(r) > 0")
	boolean alreadyConnected(Long senderUserId, Long receiverUserId);

	@Query("MATCH (sender:Person), (receiver:Person) " +
			"WHERE sender.userId = $senderUserId AND receiver.userId = $receiverUserId " +
			"CREATE (sender)-[:REQUESTED_TO]->(receiver)")
	void addConnectionRequest(Long senderUserId, Long receiverUserId);

	@Query("MATCH (sender:Person)-[r:REQUESTED_TO]->(receiver:Person) " +
			"WHERE sender.userId = $fromSenderUserId AND receiver.userId = $toReceiverUserId " +
			"DELETE r " +
			"CREATE (sender)-[:CONNECTED_TO]-(receiver)")
	void acceptConnectionRequest(Long fromSenderUserId, Long toReceiverUserId);

	@Query("MATCH (sender:Person)-[r:REQUESTED_TO]->(receiver:Person) " +
			"WHERE sender.userId = $fromSenderUserId AND receiver.userId = $toReceiverUserId " +
			"DELETE r")
	void rejectConnectionRequest(Long fromSenderUserId, Long toReceiverUserId);
}
