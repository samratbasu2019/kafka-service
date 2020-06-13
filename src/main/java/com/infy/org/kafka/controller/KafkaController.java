package com.infy.org.kafka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.infy.org.kafka.service.Producer;
import com.infy.org.kafka.utility.Constant;

@RestController
@RequestMapping(value = "/kafka")
public class KafkaController {
	private final Producer producer;

	@Autowired
	public KafkaController(Producer producer) {
		this.producer = producer;
	}

	@PostMapping(value = "/publish")
	public ResponseEntity<?> sendMessageToKafkaTopic(@RequestParam("message") String message, @RequestParam("type") String type) {
		if (type.equalsIgnoreCase(Constant.APPRECIATION))
			this.producer.sendAppreciationMessage(message);
		else if(type.equalsIgnoreCase(Constant.FEEDBACK))
			this.producer.sendFeedbackMessage(message);
		else if(type.equalsIgnoreCase(Constant.COURSE))
			this.producer.sendCourseMessage(message);
		return new ResponseEntity<>("Message published Successfully", HttpStatus.OK);
	}
}