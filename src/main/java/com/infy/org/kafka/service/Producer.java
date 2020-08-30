package com.infy.org.kafka.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class Producer {
	private static final Logger logger = LoggerFactory.getLogger(Producer.class);
	@Value("${topic.name.appreciation}")
	private String topicNameAppreciation;
	
	@Value("${topic.name.feedback}")
	private String topicNameFeedback;
	
	@Value("${topic.name.course}")
	private String topicNameCourse;
	
	@Value("${topic.name.task}")
	private String topicNameTask;
	
	@Value("${topic.name.jiratask}")
	private String topicNameJiraTask;
	
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	public void sendAppreciationMessage(String message) {
		logger.info(String.format("$$ -> Producing message --> %s", message));
		logger.info("topic name :"+topicNameAppreciation);
		this.kafkaTemplate.send(topicNameAppreciation, message);
	}
	
	public void sendFeedbackMessage(String message) {
		logger.info(String.format("$$ -> Producing message --> %s", message));
		logger.info("topic name :"+topicNameFeedback);
		this.kafkaTemplate.send(topicNameFeedback, message);
	}
	
	public void sendCourseMessage(String message) {
		logger.info(String.format("$$ -> Producing message --> %s", message));
		logger.info("topic name :"+topicNameCourse);
		this.kafkaTemplate.send(topicNameCourse, message);
	}
	
	public void sendTaskMessage(String message) {
		logger.info(String.format("$$ -> Producing message --> %s", message));
		logger.info("topic name :"+topicNameTask);
		this.kafkaTemplate.send(topicNameTask, message);
	}
	
	public void sendJiraTaskMessage(String message) {
		logger.info(String.format("$$ -> Producing message --> %s", message));
		logger.info("topic name :"+topicNameJiraTask);
		this.kafkaTemplate.send(topicNameJiraTask, message);
	}
}