package com.webscrap.dto;

import java.util.List;

import lombok.Data;

@Data
public class WebscrapingHistDto {
	
	private String Period_from;
	
	private String Period_to;
	
	private String Start_time;
	
	private String End_time;
	
	private String Elapsed_time;
}
