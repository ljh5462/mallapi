package com.spring.mallapi.service;

import java.util.List;

import com.spring.mallapi.dto.YoutubeDTO;

public interface YoutubeService {
	
	public YoutubeDTO getVideoInfo(String url);
	
	public List<YoutubeDTO> getLiveList(String channelId);
}
