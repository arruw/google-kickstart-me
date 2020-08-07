package com.matjazmav.googlekickstartme;

import com.matjazmav.googlekickstartme.service.GoogleKickStartService;
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

	private final GoogleKickStartService gkss;
	private final CacheManager cacheManager;

	public GoogleKickstartMeApplication(CacheManager cacheManager, GoogleKickStartService gkss) {
		this.gkss = gkss;
		this.cacheManager = cacheManager;
	}

	public static void main(String[] args) {
		SpringApplication.run(GoogleKickstartMeApplication.class, args);
	}

	@Scheduled(fixedDelay = 1000*60*60)
	public void refreshCaches() {
		try {
			cacheManager.getCache("GoogleKickstartService.getKickStartSeries").clear();
			gkss.getKickStartSeries();
		} catch (Exception e) {}
	}

	@Scheduled(fixedDelay = 1000*60*60)
	public void clearCaches() {
		try {
			cacheManager.getCache("ThumbnailService.getThumbnail").clear();
			cacheManager.getCache("GoogleKickStartFlierService.getFlier").clear();
		} catch (Exception e) {}
	}
}
