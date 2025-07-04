package com.suraj.linkedinms.connections_service.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SendConnectionRequestEvent {

	private Long senderUserId;
	private Long receiverUserId;
}
