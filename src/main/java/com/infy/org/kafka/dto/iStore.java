package com.infy.org.kafka.dto;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class iStore {
	private Map<String, Object> parentNode;
	private List<Map<String, Object>> childNode;
	
	
}
