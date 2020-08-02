package com.matjazmav.googlekickstartme.controller;

import com.matjazmav.googlekickstartme.constant.ProgrammingLanguage;
import com.matjazmav.googlekickstartme.dto.*;
import com.matjazmav.googlekickstartme.service.*;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;


@Controller
public class GoogleKickStartController {

    private final GoogleKickStartFlierService flierService;
    private final ThumbnailService thumbnailService;

    public GoogleKickStartController(GoogleKickStartFlierService flierService, ThumbnailService thumbnailService) {
        this.flierService = flierService;
        this.thumbnailService = thumbnailService;
    }

    @GetMapping("/flier/{nickname}")
    public String getFlier(@PathVariable String nickname, @RequestParam(name="ppl") ProgrammingLanguage ppl, @RequestParam(name="link", required = false) String link, @RequestParam(name="about", required = false, defaultValue = "true") boolean about, Model model) throws IOException {
        val flierData = flierService.getFlier(nickname, ppl);
        model.addAttribute("model", flierData);
        model.addAttribute("link", link != null ? link : "#");
        model.addAttribute("about", about);
        return "flier";
    }

    @ResponseBody
    @GetMapping(value = "/flier/{nickname}/data")
    public Flier getFlierData(@PathVariable String nickname, @RequestParam(name="ppl") ProgrammingLanguage primaryProgrammingLang) throws IOException {
        return flierService.getFlier(nickname, primaryProgrammingLang);
    }

    @ResponseBody
    @GetMapping(value = "/flier/{nickname}/thumbnail", produces = "image/png")
    public BufferedImage getThumbnail(@PathVariable String nickname, @RequestParam(name="ppl") ProgrammingLanguage ppl) throws IOException {
        val sourceUrl = MvcUriComponentsBuilder.fromController(this.getClass())
                .path("/flier/{nickname}")
                .queryParam("ppl", ppl)
                .queryParam("about", false)
                .build(nickname);
        return thumbnailService.getThumbnail(sourceUrl.toString());
    }
}
