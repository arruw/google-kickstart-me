package com.matjazmav.googlekickstartme.service;

import com.matjazmav.googlekickstartme.util.*;
import lombok.*;
import org.openqa.selenium.*;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

@Service
@RequiredArgsConstructor
public class ThumbnailService {

    private final WebDriverPool<WebDriver> webDriverPool;

    @Cacheable("ThumbnailService.getThumbnail")
    public BufferedImage getThumbnail(String url, By selector) throws Exception {
        return webDriverPool.execute(webDriver -> {
            webDriver.get(url);
            val flierEl = webDriver.findElement(selector);
            val fullImage = ImageIO.read(((TakesScreenshot)webDriver).getScreenshotAs(OutputType.FILE));

            return cropElement(fullImage, flierEl);
        });
    }

    private static BufferedImage cropElement(final BufferedImage fullImage, final WebElement el) {
        val point = el.getLocation();
        val elWidth = el.getSize().getWidth();
        val elHeight = el.getSize().getHeight();

        return fullImage.getSubimage(point.getX(), point.getY(), elWidth, elHeight);
    }
}
