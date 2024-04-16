package com.spring.mallapi.dto;

import com.google.api.services.youtube.model.Thumbnail;
import com.google.api.services.youtube.model.ThumbnailDetails;
import com.google.api.services.youtube.model.VideoLiveStreamingDetails;

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
	private String publishedAt;
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
	
	//private Snippet snippet;
	//private LiveStreamingDetails liveStreamingDetails;
	
//	@Data
//	private class Snippet {
//		
//		private String publishedAt;
//		private String channelId;
//		private String channelTitle;
//		private String title;
//		private String description;
//		private String liveBroadcastContent;
//		private ThumnailInfo thumnailInfo;
//	}
	
//	@Data
//	private class Thumbnails{
//		private ThumnailInfo stardard;
//		private ThumnailInfo maxres;
//	}
	
//	@Data
//	private class ThumnailInfo{
//		private String url;
//		private Integer width;
//		private Integer height;
//	}
	
//	@Data
//	private class LiveStreamingDetails{
//		
//		private String actualStartTime; // 방송시작시간
//		private String actualEndTime; // 방송종료시간
//		private String scheduledStartTime; // 방송예정시간
//	}
}
