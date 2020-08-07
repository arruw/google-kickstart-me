package com.matjazmav.googlekickstartme.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.matjazmav.googlekickstartme.util.*;
import lombok.val;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.remote.*;
import org.openqa.selenium.remote.service.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.annotation.RequestScope;

import java.net.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Configuration
public class WebDriverConfig {

    @Value("${selenium.chrome.path}")
    private String chromePath;

    @Value("${selenium.chrome.driver.path}")
    private String chromeDriverPath;

    @Bean()
    public Supplier<WebDriver> webDriver() {
        System.setProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY, chromeDriverPath);

        val chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        chromeOptions.setBinary(chromePath);

        return () -> new ChromeDriver(chromeOptions);
    }

    @Bean()
    public WebDriverPool<WebDriver> webDriverPool() {
        return new WebDriverPool<>(webDriver(), 20);
    }

//    private static final String USERNAME = "matjamav1";
//    private static final String AUTOMATE_KEY = "oDpYez1yhD6mTfstmPtq";
//
//    @Bean()
//    public Supplier<WebDriver> webDriver() throws MalformedURLException {
//        DesiredCapabilities caps = new DesiredCapabilities();
//
//        caps.setCapability("os", "Windows");
//        caps.setCapability("os_version", "10");
//        caps.setCapability("browser", "Chrome");
//        caps.setCapability("browser_version", "80");
//        caps.setCapability("name", "matjamav1's First Test");
//
//        val USERNAME = "matjamav1";
//        val AUTOMATE_KEY = "oDpYez1yhD6mTfstmPtq";
//        val url = new URL("https://" + USERNAME + ":" + AUTOMATE_KEY + "@hub-cloud.browserstack.com/wd/hub");
//
//        return () ->  new RemoteWebDriver(url, caps);
//    }

}
