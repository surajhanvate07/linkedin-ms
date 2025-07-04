package com.suraj.linkedinms.notification_service.client;

import com.suraj.linkedinms.notification_service.dto.PersonDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "connections-service", path = "/connections")
public interface ConnectionsClient {

	@GetMapping("/core/first-degree")
	List<PersonDto> getFirstDegreeConnections(@RequestHeader("X-User-Id") Long userId);
}
