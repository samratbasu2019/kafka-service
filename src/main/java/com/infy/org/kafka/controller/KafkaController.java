package com.infy.org.kafka.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.infy.org.kafka.service.Producer;
import com.infy.org.kafka.utility.Constant;

@RestController
@RequestMapping(value = "/kafka")
public class KafkaController {
	
	private static final Logger log = LoggerFactory.getLogger(KafkaController.class);
	private final Producer producer;
	
	@Autowired
	public KafkaController(Producer producer) {
		this.producer = producer;
	}

	@PostMapping(value = "/publish")
	public ResponseEntity<?> sendMessageToKafkaTopic(@RequestBody String message, @RequestParam("type") String type) {
		log.info("Message came for publish is :"+message);
		if (type.equalsIgnoreCase(Constant.APPRECIATION))
			this.producer.sendAppreciationMessage(message);
		else if(type.equalsIgnoreCase(Constant.FEEDBACK))
			this.producer.sendFeedbackMessage(message);
		else if(type.equalsIgnoreCase(Constant.COURSE))
			this.producer.sendCourseMessage(message);
		return new ResponseEntity<>("Message published Successfully", HttpStatus.OK);
	}
}