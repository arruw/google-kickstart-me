package com.matjazmav.googlekickstartme.service;

import lombok.val;
import org.openqa.selenium.*;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Service
public class ThumbnailService {

    private final WebDriver webDriver;

    public ThumbnailService(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public BufferedImage getThumbnail(String url) throws IOException {
        webDriver.get(url);

        val flierEl = webDriver.findElement(By.className("flier"));

        val fullImage = ImageIO.read(((TakesScreenshot)webDriver).getScreenshotAs(OutputType.FILE));

        return cropElement(fullImage, flierEl);
    }

    private static BufferedImage cropElement(final BufferedImage fullImage, final WebElement el) {
        val point = el.getLocation();
        val elWidth = el.getSize().getWidth();
        val elHeight = el.getSize().getHeight();

        return fullImage.getSubimage(point.getX(), point.getY(), elWidth, elHeight);
    }
}
