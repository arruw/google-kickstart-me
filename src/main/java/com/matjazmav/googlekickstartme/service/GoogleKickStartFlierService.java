package com.matjazmav.googlekickstartme.service;

import com.matjazmav.googlekickstartme.dto.*;
import com.matjazmav.googlekickstartme.util.*;
import lombok.val;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.*;

@Service
public class GoogleKickStartFlierService {

    private final GoogleKickStartService gkss;

    public GoogleKickStartFlierService(GoogleKickStartService gkss) {
        this.gkss = gkss;
    }

    @Cacheable("GoogleKickStartFlierService.getFlier")
    public Flier getFlier(String nickname, String language) throws IOException {

        val now = Instant.now().getEpochSecond() * 1000;
        val challengeIds = gkss.getKickStartSeries().getCompetitions().stream()
                .flatMap(c -> c.getChallenges().stream())
                .filter(c -> c.getTimestamp() <= now)
                .sorted(Comparator.comparingLong(KickStartChallenge::getTimestamp))
                .map(KickStartChallenge::getId)
                .collect(Collectors.toList());

        val profile = gkss.getProfile(nickname);
        val scoreMap = profile.getScores().stream()
                .collect(Collectors.toMap(KickStartScore::getChallengeId, Function.identity()));

        val scores = challengeIds.stream()
                .map(cId -> scoreMap.containsKey(cId) ? scoreMap.get(cId).getScore() : 0)
                .collect(Collectors.toList())
                .toArray(new Integer[]{});

        val cumulativeScores =  scores.clone();
        Arrays.parallelPrefix(cumulativeScores, Integer::sum);

        val totalScore = profile.getScores().stream().mapToInt(KickStartScore::getScore).sum();
        val relativeCumulativeScores = Arrays.stream(cumulativeScores)
                .map(s -> s == 0 ? 1 : s/(double)totalScore*100)
                .collect(Collectors.toList())
                .toArray(new Double[]{});

        return new Flier(
                nickname,
                profile.getCountry(),
                language,
                scores,
                relativeCumulativeScores,
                totalScore,
                CountryFlagImageMapper.get(profile.getCountry()),
                LanguageImageMapper.get(language)
        );
    }

}
