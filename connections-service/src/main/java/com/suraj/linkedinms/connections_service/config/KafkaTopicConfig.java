package com.suraj.linkedinms.connections_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

	public NewTopic sendConnectionRequestTopic() {
		return new NewTopic("send-connection-request-topic", 3, (short) 1);
	}

	public NewTopic acceptConnectionRequestTopic() {
		return new NewTopic("accept-connection-request-topic", 3, (short) 1);
	}

//	public NewTopic rejectConnectionRequestTopic() {
//		return new NewTopic("reject-connection-request-topic", 3, (short) 1);
//	}

}
