package com.infy.org.kafka.mail;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;

//import org.springframework.ui.velocity.VelocityEngineUtils;

@Service
public class Processor{
	private static final Logger log = LoggerFactory.getLogger(Processor.class);
	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private Configuration freemarkerConfig;

	public boolean parsePayload(String message) {
		ObjectMapper obm = new ObjectMapper();
		boolean hasProcessed = false;
		try {
			JsonNode parentNode = obm.readValue(message, JsonNode.class);
			ArrayNode childNode = (ArrayNode) parentNode.get("appreciation");
			String appreciatorName=null,appreciatorEmail=null;
			log.info("parent appreciation receiver emails :" + parentNode.get("email").asText());

			Iterator<JsonNode> childIterator = childNode.elements();
			while (childIterator.hasNext()) {
				JsonNode clildrenNode = childIterator.next();
				appreciatorName=clildrenNode.get("appreciatorName").asText();
				appreciatorEmail=clildrenNode.get("appreciatorEmail").asText();
				log.info("Child  appreciation giver email :" + clildrenNode.get("appreciatorEmail"));
			}
			hasProcessed = sendEmail(parentNode.get("email").asText(), parentNode.get("name").asText(), appreciatorName);
			hasProcessed=true;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return hasProcessed;
	}

	private boolean sendEmail(String toEmailId, String toName, String fromName) throws Exception {
		boolean hasProcessed = false;
		log.info("Sending emails to : "+toEmailId + " name :"+toName+" Sent from :"+fromName);
		MimeMessage message = mailSender.createMimeMessage();

		MimeMessageHelper helper = new MimeMessageHelper(message);

		Map<String, Object> model = new HashMap<>();
		model.put("user", toName);
		model.put("appreciator", fromName);
		freemarkerConfig.setClassForTemplateLoading(this.getClass(), "/");

		Template t = freemarkerConfig.getTemplate("email-appreciation-body.ftl");
		String text = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);

		helper.setTo(toEmailId);
		helper.setText(text, true); // set to html
		
		Map<String, Object> modelSub = new HashMap<>();
		modelSub.put("appreciator", fromName);
		Template sub = freemarkerConfig.getTemplate("email-appreciation-subject.ftl");
		String subject = FreeMarkerTemplateUtils.processTemplateIntoString(sub, modelSub);
		
		helper.setSubject(subject);

		mailSender.send(message);
		hasProcessed = true;
		return hasProcessed;
	}

}
