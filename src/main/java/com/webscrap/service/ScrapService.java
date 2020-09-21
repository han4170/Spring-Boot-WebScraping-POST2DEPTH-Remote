package com.webscrap.service;

import com.webscrap.dto.WebscrapingDto;
import com.webscrap.dto.WebscrapingInputDto;

public interface ScrapService {

	WebscrapingDto selectWebListPost(WebscrapingInputDto WebscrapingInput);
}
