package com.matjazmav.googlekickstartme.config;

import com.matjazmav.googlekickstartme.util.*;
import lombok.val;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

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
}
