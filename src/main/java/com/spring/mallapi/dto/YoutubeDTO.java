package com.spring.mallapi.dto;

import java.util.List;

import com.google.api.services.youtube.model.Thumbnail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class YoutubeDTO {

	private String id; // 동영상 id
	private String title; // 제목
	private String publishedAt; // 동영상 투고 시간
	private String description; // 설명
	private String channelId; // 채널 id
	private String channelTitle; // 채널명
	private String duration; // 영상 길이
	private String kind; // 영상 종류
	private String liveBroadcastContent; // 현재 방송 상태
	private Thumbnail thumbnail; // 썸네일 정보(maxres)
	private String actualStartTime; // 방송시작시간
	private String actualEndTime; // 방송종료시간
	private String scheduledStartTime; // 방송예정시간
	private List<String> tags; // 태그
	
}
