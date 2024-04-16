package com.spring.mallapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.spring.mallapi.dto.YoutubeDTO;
import com.spring.mallapi.service.YoutubeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/youtube")
public class YoutubeController {
	
	@Autowired
	public YoutubeService youtubeService;
	
	@ResponseBody
	@PostMapping("/getInfo")
	public YoutubeDTO getInfo(@RequestParam("id") String id ){
		return youtubeService.getVideoInfo(id);
	}
	

}
