package com.spring.mallapi.service;

import java.util.List;

import com.spring.mallapi.dto.YoutubeDTO;

public interface YoutubeService {
	
	public YoutubeDTO getVideoOne(String url);
	
	public List<YoutubeDTO> getUpcomingLiveList(String channelId);
}
