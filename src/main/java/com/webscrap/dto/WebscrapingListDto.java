package com.webscrap.dto;

import java.util.List;

import lombok.Data;

@Data
public class WebscrapingListDto {
	
	private String Used_Year; //사용년월
	
	private String Used_Count; //사용건수
	
	private String Used_Amount; //사용금액
	
	private String Deduction_SubjectAmount; //공제제외대상금액
	
	private String Deduction_Amount; //공제대상금액

}
