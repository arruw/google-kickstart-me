package com.matjazmav.googlekickstartme;

import com.matjazmav.googlekickstartme.service.GoogleKickstartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;

@EnableCaching
@EnableScheduling
@SpringBootApplication
public class GoogleKickstartMeApplication {

	private final GoogleKickstartService gkss;
	private final CacheManager cacheManager;

	public GoogleKickstartMeApplication(CacheManager cacheManager, GoogleKickstartService gkss) {
		this.gkss = gkss;
		this.cacheManager = cacheManager;
	}

	public static void main(String[] args) {
		SpringApplication.run(GoogleKickstartMeApplication.class, args);
	}

	@Scheduled(fixedDelay = 1000*60*60)
	public void refreshCaches() {
		try {
			cacheManager.getCache("GoogleKickstartService.getCompetitions").clear();
			gkss.getCompetitions();
		} catch (IOException e) {}
	}
}
