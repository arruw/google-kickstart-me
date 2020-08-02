package com.matjazmav.googlekickstartme.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.val;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.TimeUnit;

@Configuration
public class SeleniumWebDriverConfig {

    @Value("${chromePath}")
    private String chromePath;

    @Value("${chromeDriverPath}")
    private String chromeDriverPath;

    @Bean
    @Primary
    public WebDriver webDriver() {
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);

        val chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--window-size=300,300");
        chromeOptions.setBinary(chromePath);

        val webDriver = new ChromeDriver(chromeOptions);

        return webDriver;
    }

}
