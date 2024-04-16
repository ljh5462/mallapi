package com.spring.mallapi.service;

import java.io.IOException;
import java.util.List;

import org.mortbay.log.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import com.google.api.services.youtube.model.VideoLiveStreamingDetails;
import com.google.api.services.youtube.model.VideoSnippet;
import com.spring.mallapi.dto.YoutubeDTO;
import com.spring.mallapi.util.CustomTimeConvert;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class YoutubeServiceImpl implements YoutubeService{
	
	private final CustomTimeConvert customTimeConvert;

	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();
	
	private static YouTube youtube;
	
	@Value("${youtube.api.key}")
	private String apiKey;
	
	// id로 동영상 정보 조회
	@Override
	public YoutubeDTO getVideoInfo(String id){
		try{
            youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
                public void initialize(HttpRequest request) throws IOException {}
            }).setApplicationName("youtube-cmdline-search-sample").build();

            YouTube.Videos.List videos = youtube.videos().list("id,snippet");

            videos.setKey(apiKey);
            videos.setPart("snippet,liveStreamingDetails,contentDetails");
            videos.setRegionCode("kr");
            videos.setId(id);

            VideoListResponse videoResponse = videos.execute();
            List<Video> videoList = videoResponse.getItems();
            
            if(!videoList.isEmpty()) {
            	String kind = "video";
            	
            	Video v = videoList.get(0);
            	VideoSnippet vs = v.getSnippet();
            	VideoLiveStreamingDetails vl = v.getLiveStreamingDetails();
            	
            	DateTime actualStartTime = null;
        		DateTime actualEndTime = null;
        		DateTime scheduledStartTime = null;
            	
            	if(vl != null) {
            		kind = "live";
            		actualStartTime = vl.getActualStartTime();
            		actualEndTime = vl.getActualEndTime();
            		scheduledStartTime = vl.getScheduledStartTime();
            	}
            	
            	YoutubeDTO youtubeDTO = YoutubeDTO.builder()
	            	.id(v.getId())
	            	.title(vs.getTitle())
	            	.publishedAt(customTimeConvert.convertTimestampToCurrentDate(vs.getPublishedAt()))
	            	.description(vs.getDescription())
	            	.channelId(vs.getChannelId())
	            	.channelTitle(vs.getChannelTitle())
	            	.duration(customTimeConvert.convertDurationToTime(v.getContentDetails().getDuration()))
	            	.liveBroadcastContent(vs.getLiveBroadcastContent())
	            	.kind(kind)
	            	.actualStartTime(actualStartTime != null ? customTimeConvert.convertTimestampToCurrentDate(actualStartTime) : "")
	            	.actualEndTime(actualEndTime != null ? customTimeConvert.convertTimestampToCurrentDate(actualEndTime) : "")
	            	.scheduledStartTime(scheduledStartTime != null ? customTimeConvert.convertTimestampToCurrentDate(scheduledStartTime) : "")
	            	.thumbnail(vs.getThumbnails().getMaxres())
	            	.build();
            	return youtubeDTO;
            }
            
            else {
            	YoutubeDTO youtubeDTO = new YoutubeDTO();
            	return youtubeDTO;
            }

        } catch (GoogleJsonResponseException e) {
            System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
                    + e.getDetails().getMessage());
        } catch (IOException e) {
            System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }

        return null;
	}
}
