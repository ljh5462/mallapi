package com.spring.mallapi.service;

import com.spring.mallapi.dto.YoutubeDTO;

public interface YoutubeService {
	
	public YoutubeDTO getVideoInfo(String id);
}
