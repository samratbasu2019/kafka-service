package com.infy.org.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.infy.org.kafka")
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class KafkaServiceStarter {

	public static void main(String[] args) {
		SpringApplication.run(KafkaServiceStarter.class, args);
	}

}