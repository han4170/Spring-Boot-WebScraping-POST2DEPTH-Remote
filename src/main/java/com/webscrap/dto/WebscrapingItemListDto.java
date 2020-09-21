package com.webscrap.dto;

import java.util.List;

import lombok.Data;

@Data
public class WebscrapingItemListDto {
	
	private String Used_Year; //사용년월
	
	private String Trading_Date; //거래일시
	
	private String Store_Name; //가맹점명
	
	private String Amount_Used; //사용금액

	private String Issued_Method; //발급수단
	
	private String Trading_Method; //거래구분
	
	private String Deduction_Status; //공제여부
	
	private String Issued_classifi; //발행구분
	
}
