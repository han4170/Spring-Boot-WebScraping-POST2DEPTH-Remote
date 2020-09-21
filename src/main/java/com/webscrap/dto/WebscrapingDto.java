package com.webscrap.dto;

import java.util.List;

import lombok.Data;


@Data
public class WebscrapingDto {
	
	private String errMsg; // 에러 메세지
	
	private String errYn; // 에러유무 Y,N
	
	private List<WebscrapingListDto> list; // 월별 데이터
	
	private List<WebscrapingItemListDto> itemList; // 월 세부 일자별 데이터
	
	// 총합계
	private String total_count; // 사용건수
	
	private String total_amount; // 사용금액
	
	private String total_deduction_subjectamount; // 공제제외대상금액
	
	private String total_deduction_amount; // 공제대상금액
}
