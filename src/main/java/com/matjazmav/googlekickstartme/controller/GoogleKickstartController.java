package com.matjazmav.googlekickstartme.controller;

import com.google.common.util.concurrent.RateLimiter;
import com.matjazmav.googlekickstartme.dto.*;
import com.matjazmav.googlekickstartme.service.*;
import lombok.val;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class GoogleKickstartController {

    private final GoogleKickstartService gkss;

    public GoogleKickstartController(GoogleKickstartService gkss) {
        this.gkss = gkss;
    }

    @GetMapping("/ranks/{nickname}")
    public List<ChallengeResult> getRanks(@PathVariable String nickname) throws IOException {
        val now = Instant.now().getEpochSecond() * 1000;

        // get all Kick Start challenges as empty ChallengeResult
        Map<String, ChallengeResult> emptyChallengeResults = gkss.getCompetitions().stream()
                .flatMap(a -> a.getChallenges().stream().map(c -> new ChallengeResult(){{
                    setChallengeId(c.getId());
                    setTimestamp(c.getTimestamp());
                }}))
                .collect(Collectors.toMap(c -> c.getChallengeId(), c -> c));

        // populate empty challenge results
        for (final String challengeId: gkss.getChallenges(nickname)) {
            val cr = gkss.getResults(challengeId, nickname);
            emptyChallengeResults.replace(cr.getChallengeId(), cr);
        }

        return emptyChallengeResults.values().stream()
                .filter(cr -> cr.getTimestamp() <= now)
                .sorted(Comparator.comparingLong(ChallengeResult::getTimestamp))
                .collect(Collectors.toList());
    }
}
