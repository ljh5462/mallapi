package com.spring.mallapi.util;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import com.google.api.client.util.DateTime;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
@RequiredArgsConstructor
public class CustomTimeConvert {
	
	public String convertTimestampToCurrentDate(DateTime time) {
		
		String currentTimestamp = String.valueOf(time);
		// Parse the timestamp to ZonedDateTime
		ZonedDateTime utcDateTime = ZonedDateTime.parse(currentTimestamp);
		// Convert to JST (Japan Standard Time)
		ZonedDateTime jstDateTime = utcDateTime.withZoneSameInstant(ZoneId.of("Asia/Tokyo"));

		// Format the JST datetime string as "YYYY/MM/DD HH24:MI:SS"
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm:ss");
		String formattedDateTime = jstDateTime.format(formatter);;
        
        return formattedDateTime;
	}
	
	public String convertDurationToTime(String duration) {
		
		String formattedDuration = convertDuration(duration);
		
		return formattedDuration;
	}
	
	public static String convertDuration(String duration) {
        // Parse the duration string
        Duration dur = Duration.parse(duration);

        // Extract hours, minutes, and seconds
        long hours = dur.toHours();
        long minutes = dur.toMinutes() % 60;
        long seconds = dur.getSeconds() % 60;

        // Format the duration string as "HH:MM:SS"
        String formattedDuration = String.format("%02d:%02d:%02d", hours, minutes, seconds);

        return formattedDuration;
    }
}
