package com.matjazmav.googlekickstartme.controller;

import com.matjazmav.googlekickstartme.constant.ProgrammingLanguage;
import com.matjazmav.googlekickstartme.dto.*;
import com.matjazmav.googlekickstartme.service.*;
import lombok.val;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@Controller
public class GoogleKickstartController {

    private final GoogleKickStartFlierService flierService;

    public GoogleKickstartController(GoogleKickStartFlierService flierService) {
        this.flierService = flierService;
    }

    @GetMapping("/flier/{nickname}")
    public String getFlier(@PathVariable String nickname, @RequestParam(name="ppl") ProgrammingLanguage ppl, @RequestParam(name="link", required = false) String link, Model model) throws IOException {
        val flierData = flierService.getFlier(nickname, ppl);
        model.addAttribute("model", flierData);
        model.addAttribute("link", link != null ? link : "#");
        return "flier";
    }

    @ResponseBody
    @GetMapping(value = "/flier/{nickname}/data")
    public Flier getFlierData(@PathVariable String nickname, @RequestParam(name="ppl") ProgrammingLanguage primaryProgrammingLang) throws IOException {
        return flierService.getFlier(nickname, primaryProgrammingLang);
    }
}
