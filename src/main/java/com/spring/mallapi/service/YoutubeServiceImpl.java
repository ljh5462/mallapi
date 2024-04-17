package com.spring.mallapi.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.SearchResultSnippet;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import com.google.api.services.youtube.model.VideoLiveStreamingDetails;
import com.google.api.services.youtube.model.VideoSnippet;
import com.spring.mallapi.dto.YoutubeDTO;
import com.spring.mallapi.util.CustomTimeConvert;

import lombok.AllArgsConstructor;
import lombok.Getter;
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
	public YoutubeDTO getVideoInfo(String url){
		
		try{
			
			VideoInfo videoInfo = extractVideoId(url); // url 형식인 경우 id 추출, url로 쇼츠여부 확인
			String vid = videoInfo.getVideoId();
			boolean isShorts = videoInfo.isShorts();
			
            youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
                public void initialize(HttpRequest request) throws IOException {}
            }).setApplicationName("youtube-cmdline-search-sample").build();

            YouTube.Videos.List videos = youtube.videos().list("id,snippet");

            videos.setKey(apiKey);
            videos.setPart("snippet,liveStreamingDetails,contentDetails");
            videos.setRegionCode("kr");
            videos.setId(vid);

            VideoListResponse videoResponse = videos.execute();
            List<Video> videoList = videoResponse.getItems();
            
            if(!videoList.isEmpty()) {
            	String kind = "video";
            	
            	if(isShorts) {
            		kind = "shorts";
            	}
            	
            	Video v = videoList.get(0);
            	VideoSnippet vs = v.getSnippet();
            	VideoLiveStreamingDetails vl = v.getLiveStreamingDetails();
            	
            	DateTime actualStartTime = null;
        		DateTime actualEndTime = null;
        		DateTime scheduledStartTime = null;
            	
            	if(vl != null) {
            		if(isShorts) {
            			kind += " live";
            		}
            		else {
            			kind = "live";            			
            		}
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
	            	.tags(vs.getTags())
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
	
	//채널id로 실시간, 대기중인 동영상 조회
	public List<YoutubeDTO> getLiveList(String channelId){
		
		try{
			
	        youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
	            public void initialize(HttpRequest request) throws IOException {}
	        }).setApplicationName("youtube-cmdline-search-sample").build();
	
	        YouTube.Search.List search = youtube.search().list("snippet");
	
	        search.setKey(apiKey);
	        search.setChannelId(channelId);
	        search.setEventType("live");
	        search.setPart("id,snippet");
	        search.setType("video");
	
	        SearchListResponse searchListResponse = search.execute();
	        
	        search.setEventType("upcoming");
	        
	        SearchListResponse searchListResponse2 = search.execute();
	        
	        List<SearchResult> searchList = searchListResponse.getItems();
	        List<SearchResult> searchList2 = searchListResponse2.getItems();
	        
	        List<SearchResult> combinedList = new ArrayList<>(searchList);
	        combinedList.addAll(searchList2);
	        
	        List<YoutubeDTO> result = new ArrayList<>();
	        
	        for(SearchResult v : combinedList) {
	        	
	        	ResourceId rs = v.getId();
	        	SearchResultSnippet srs = v.getSnippet();
	        	
	        	YoutubeDTO youtubeDTO = YoutubeDTO.builder()
	        			.id(rs.getVideoId())
	        			.title(srs.getTitle())
	        			.publishedAt(customTimeConvert.convertTimestampToCurrentDate(srs.getPublishedAt()))
	        			.description(srs.getDescription())
	        			.channelId(srs.getChannelId())
	        			.channelTitle(srs.getChannelTitle())
	        			.liveBroadcastContent(srs.getLiveBroadcastContent())
	        			.thumbnail(srs.getThumbnails().getMaxres())
	        			.build();
	        	
	        	result.add(youtubeDTO);
	        }
	        
	        return result;

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
	
	//url로 동영상 id 추출, 쇼츠여부 확인(url로 받을경우만)
	public static VideoInfo extractVideoId(String videoUrl) {
		
		String videoId = null;
        boolean isShorts = false;
		
		// Split the URL by "/", and get the last part
        String[] parts = videoUrl.split("/");
        String lastPart = parts[parts.length - 1];

        // If the last part contains a "?" (indicating query parameters), split it by "?" and get the first part
        if (lastPart.contains("?")) {
            String[] subParts = lastPart.split("\\?");
            videoId = subParts[0];
        } else {
            videoId = lastPart;
        }
        
        // Check if the URL contains "shorts"
        if (videoUrl.contains("shorts")) {
            isShorts = true;
        }

        return new VideoInfo(videoId, isShorts);
	}
	
	@AllArgsConstructor
	@Getter
	public static class VideoInfo {
        private String videoId;
        private boolean isShorts;
    }
}
