package com.infy.org.kafka.service;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infy.org.kafka.dto.iStore;
import com.infy.org.kafka.mail.Processor;

@Service
public class Consumer {
	private final Logger logger = LoggerFactory.getLogger(Consumer.class);
	@Autowired
	Processor ps;
	
	@KafkaListener(topics = "users", groupId = "group_id")
	public void consume(String message) {	
		
		boolean hasProcessed = ps.parsePayload(message);
		logger.info(String.format("$$ -> Message Consumed and email sent successful for this message -> %s", message));
	}
}