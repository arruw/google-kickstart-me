package com.matjazmav.googlekickstartme.controller;

import com.matjazmav.googlekickstartme.service.*;
import lombok.*;
import org.openqa.selenium.*;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.awt.image.BufferedImage;
import java.util.concurrent.*;


@Controller
@RequiredArgsConstructor
public class GoogleKickStartBadgeController {

    private final BadgeService badgeService;
    private final ThumbnailService thumbnailService;

    private static final CacheControl CACHE_CONTROL = CacheControl
            .maxAge(1, TimeUnit.HOURS)
            .cachePublic();

    @ResponseBody
    @RequestMapping("/")
    public ResponseEntity<Void> index() {
        return ResponseEntity
                .status(HttpStatus.MOVED_PERMANENTLY)
                .header(HttpHeaders.LOCATION, "https://github.com/matjazmav/google-kickstart-me/blob/master/README.md")
                .build();
    }

    @GetMapping("/flier/{nickname}/{language}")
    public String getFlier(@PathVariable String nickname, @PathVariable String language, @RequestParam(name="link", required = false) String link, Model model) throws Exception {
        val flierData = badgeService.getFlier(nickname, language);
        model.addAttribute("model", flierData);
        model.addAttribute("link", link != null ? link : "#");
        return "flier";
    }

    @ResponseBody
    @GetMapping(value = "/flier/{nickname}/{language}/thumbnail", produces = "image/png")
    public ResponseEntity<BufferedImage> getThumbnail(@PathVariable String nickname, @PathVariable String language) throws Exception {
        val sourceUrl = MvcUriComponentsBuilder.fromController(this.getClass())
                .path("/flier/{nickname}/{language}")
                .build(nickname, language);

        return ResponseEntity
                .ok()
                .cacheControl(CACHE_CONTROL)
                .body(thumbnailService.getThumbnail(sourceUrl.toString(), By.className("flier")));
    }
}
