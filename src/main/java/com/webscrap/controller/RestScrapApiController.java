package com.webscrap.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.webscrap.service.ScrapService;
import com.webscrap.dto.WebscrapingDto;
import com.webscrap.dto.WebscrapingInputDto;

@RestController
public class RestScrapApiController {

	@Autowired
	private ScrapService scrapService;
	
	@RequestMapping(value="/api/webScapingPost", method=RequestMethod.POST)
	public WebscrapingDto webScapingPost( WebscrapingInputDto WebscrapingInput){
		return scrapService.selectWebListPost(WebscrapingInput);
	}

	
}
