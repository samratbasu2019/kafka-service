package com.infy.org.kafka.mail;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.infy.org.kafka.utility.Constant;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;

//import org.springframework.ui.velocity.VelocityEngineUtils;

@Service
public class Processor {
	private static final Logger log = LoggerFactory.getLogger(Processor.class);
	@Autowired
	private JavaMailSender mailSender;

	@Value("${app.appreciation.receiver.email.body}")
	private String appreciationReceiverEmailBody;
	@Value("${app.appreciation.receiver.email.subject}")
	private String appreciationReceiverEmailSubject;

	@Value("${app.appreciation.provider.email.body}")
	private String appreciationProviderEmailBody;
	@Value("${app.appreciation.provider.email.subject}")
	private String appreciationProviderEmailSubject;

	@Value("${app.feedback.receiver.email.body}")
	private String feedbackReceiverEmailBody;
	@Value("${app.feedback.receiver.email.subject}")
	private String feedbackReceiverEmailSubject;

	@Value("${app.feedback.provider.email.body}")
	private String feedbackProviderEmailBody;
	@Value("${app.feedback.provider.email.subject}")
	private String feedbackProviderEmailSubject;

	@Value("${app.task.complete.email.body}")
	private String taskCompleteEmailBody;
	@Value("${app.task.complete.email.subject}")
	private String taskCompleteEmailSubject;
	
	@Value("${app.task.complete.ack.email.body}")
	private String taskCompleteAckEmailBody;
	@Value("${app.task.complete.ack.email.subject}")
	private String taskCompleteAckEmailSubject;

	@Value("${app.task.assign.email.body}")
	private String taskAssignedEmailBody;
	@Value("${app.task.assign.email.subject}")
	private String taskAssignedEmailSubject;

	@Value("${app.task.assign.ack.email.body}")
	private String taskAssignedAckEmailBody;
	@Value("${app.task.assign.ack.email.subject}")
	private String taskAssignedAckEmailSubject;

	@Value("${app.course.complete.email.body}")
	private String courseCompleteEmailBody;
	@Value("${app.course.complete.email.subject}")
	private String courseCompleteEmailSubject;
	
	@Value("${app.jira.task.assign.email.body}")
	private String jiraTaskAssignedEmailBody;
	@Value("${app.jira.task.assign.email.subject}")
	private String jiraTaskAssignedEmailSubject;

	@Autowired
	private Configuration freemarkerConfig;

	public boolean parsePayload(String message, String type) throws Exception {
		log.info("Inside parser payload");
		String parentName = null, patentEmail = null, childName = null, childEmail = null, taskStatus = null;
		Iterator<JsonNode> childIterator;
		ObjectMapper obm = new ObjectMapper();
		boolean hasProcessed = false;
		JsonNode parentNode = obm.readValue(message, JsonNode.class);

		switch (type) {
		case Constant.APPRECIATION:
			ArrayNode childNode = (ArrayNode) parentNode.get("appreciation");
			parentName = parentNode.get("name").asText().trim();
			patentEmail = parentNode.get("email").asText().trim();
			log.info("parent appreciation receiver emails :" + parentNode.get("email").asText());
			childIterator = childNode.elements();
			while (childIterator.hasNext()) {
				JsonNode clildrenNode = childIterator.next();
				childName = clildrenNode.get("appreciatorName").asText().trim();
				childEmail = clildrenNode.get("appreciatorEmail").asText().trim();
				log.info("Child  appreciation giver email :" + clildrenNode.get("appreciatorEmail"));
			}
			hasProcessed = sendEmail(patentEmail, parentName, childName, appreciationReceiverEmailBody,
					appreciationReceiverEmailSubject);
			hasProcessed = sendEmail(childEmail, childName, parentName, appreciationProviderEmailBody,
					appreciationProviderEmailSubject);
			break;
		case Constant.FEEDBACK:
			ArrayNode childFeedbackNode = (ArrayNode) parentNode.get("feedback");

			parentName = parentNode.get("name").asText().trim();
			patentEmail = parentNode.get("email").asText().trim();
			log.info("parent feedback receiver emails :" + parentNode.get("email").asText());
			childIterator = childFeedbackNode.elements();
			while (childIterator.hasNext()) {
				JsonNode clildrenNode = childIterator.next();
				childName = clildrenNode.get("feedbackerName").asText().trim();
				childEmail = clildrenNode.get("feedbackerEmail").asText().trim();
				log.info("Child  feedback giver email :" + clildrenNode.get("feedbackerEmail"));
			}
			hasProcessed = sendEmail(patentEmail, parentName, childName, feedbackReceiverEmailBody,
					feedbackReceiverEmailSubject);
			hasProcessed = sendEmail(childEmail, childName, parentName, feedbackProviderEmailBody,
					feedbackProviderEmailSubject);
			break;
		case Constant.COURSE:
			ArrayNode childCourseNode = (ArrayNode) parentNode.get("course");

			parentName = parentNode.get("name").asText().trim();
			patentEmail = parentNode.get("email").asText().trim();
			log.info("parent course receiver emails :" + parentNode.get("email").asText());
			childIterator = childCourseNode.elements();
			while (childIterator.hasNext()) {
				JsonNode clildrenNode = childIterator.next();
				childName = clildrenNode.get("courseName").asText().trim();
				log.info("Child  appreciation giver email :" + clildrenNode.get("courseName"));
			}
			hasProcessed = sendEmail(patentEmail, parentName, childName, courseCompleteEmailBody,
					courseCompleteEmailSubject);
			break;
		case Constant.TASK:
			ArrayNode childTaskNode = (ArrayNode) parentNode.get("task");

			parentName = parentNode.get("name").asText().trim();
			patentEmail = parentNode.get("email").asText().trim();
			log.info("parent course receiver emails :" + parentNode.get("email").asText());
			childIterator = childTaskNode.elements();
			while (childIterator.hasNext()) {
				JsonNode clildrenNode = childIterator.next();
				childName = clildrenNode.get("taskCreatorName").asText().trim();
				childEmail = clildrenNode.get("taskCreatorEmail").asText().trim();
				taskStatus = clildrenNode.get("taskStatus").asText().trim();
				log.info("Child  Task giver email :" + clildrenNode.get("taskName"));
			}
			if (!taskStatus.equalsIgnoreCase(Constant.APPROVED)) {
				hasProcessed = sendEmail(patentEmail, parentName, childName, taskAssignedAckEmailBody,
						taskAssignedAckEmailSubject);
				hasProcessed = sendEmail(childEmail, childName, parentName, taskAssignedEmailBody,
						taskAssignedEmailSubject);

			}else {
				hasProcessed = sendEmail(patentEmail, parentName, childName, taskCompleteEmailBody,
						taskCompleteEmailSubject);
				hasProcessed = sendEmail(childEmail, childName, parentName, taskCompleteAckEmailBody,
						taskCompleteAckEmailSubject);
			}
			break;
		case Constant.JIRATASK:
			//ArrayNode childJiraTaskNode = (ArrayNode) parentNode.get("task");

			parentName = parentNode.get("name").asText().trim();
			patentEmail = parentNode.get("email").asText().trim();
			log.info("parent course receiver emails :" + parentNode.get("email").asText());
			//childIterator = childJiraTaskNode.elements();
			hasProcessed = sendEmail(patentEmail, parentName, "", jiraTaskAssignedEmailBody,
						jiraTaskAssignedEmailSubject);
	
			break;

		}

		return hasProcessed;

	}

	private boolean sendEmail(String toEmailId, String toName, String fromName, String emailBodyTemplate,
			String emailSubjectTemplate) throws Exception {
		boolean hasProcessed = false;
		log.info("Sending emails to : " + toEmailId + " name :" + toName + " Sent from :" + fromName);
		MimeMessage message = mailSender.createMimeMessage();

		MimeMessageHelper helper = new MimeMessageHelper(message);

		Map<String, Object> model = new HashMap<>();
		model.put("user", toName);
		model.put("appreciator", fromName);
		freemarkerConfig.setClassForTemplateLoading(this.getClass(), "/");

		Template t = freemarkerConfig.getTemplate(emailBodyTemplate);
		String text = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);

		helper.setTo(toEmailId);
		helper.setText(text, true); // set to html

		Map<String, Object> modelSub = new HashMap<>();
		modelSub.put("appreciator", fromName);
		Template sub = freemarkerConfig.getTemplate(emailSubjectTemplate);
		String subject = FreeMarkerTemplateUtils.processTemplateIntoString(sub, modelSub);

		helper.setSubject(subject);

		mailSender.send(message);
		hasProcessed = true;
		return hasProcessed;
	}

}
