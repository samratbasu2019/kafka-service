package com.infy.org.kafka.service;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infy.org.kafka.dto.iStore;
import com.infy.org.kafka.mail.Processor;
import com.infy.org.kafka.utility.Constant;

@Service
public class Consumer {
	private final Logger logger = LoggerFactory.getLogger(Consumer.class);
	@Autowired
	Processor ps;
	
	@KafkaListener(topics = "${topic.name.appreciation}", groupId = "group_id")
	public void consumeAppreciation(String message) {
		logger.info("Inside consumer service appreciation.");
		boolean hasProcessed = false;
		try {
			hasProcessed = ps.parsePayload(message, Constant.APPRECIATION);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		logger.info(String.format("$$ -> Message Consumed and email sent successful for this message -> %s", message));
	}
	
	@KafkaListener(topics = "${topic.name.feedback}", groupId = "group_id")
	public void consumeFeedback(String message) {
		logger.info("Inside consumer service Feedback.");
		boolean hasProcessed = false;
		try {
			hasProcessed = ps.parsePayload(message, Constant.FEEDBACK);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		logger.info(String.format("$$ -> Message Consumed and email sent successful for this message -> %s", message));
	}
	
	@KafkaListener(topics = "${topic.name.course}", groupId = "group_id")
	public void consumeCourse(String message) {
		logger.info("Inside consumer service Course.");
		boolean hasProcessed = false;
		try {
			hasProcessed = ps.parsePayload(message, Constant.COURSE);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		logger.info(String.format("$$ -> Message Consumed and email sent successful for this message -> %s", message));
	}
	
	@KafkaListener(topics = "${topic.name.task}", groupId = "group_id")
	public void consumeTask(String message) {
		logger.info("Inside consumer service Task.");
		boolean hasProcessed = false;
		try {
			hasProcessed = ps.parsePayload(message, Constant.TASK);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		logger.info(String.format("$$ -> Message Consumed and email sent successful for this message -> %s", message));
	}
	
	@KafkaListener(topics = "${topic.name.jiratask}", groupId = "group_id")
	public void consumeJiraTask(String message) {
		logger.info("Inside consumer service Jira Task.");
		boolean hasProcessed = false;
		try {
			hasProcessed = ps.parsePayload(message, Constant.JIRATASK);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		logger.info(String.format("$$ -> Message Consumed and email sent successful for this message -> %s", message));
	}
}