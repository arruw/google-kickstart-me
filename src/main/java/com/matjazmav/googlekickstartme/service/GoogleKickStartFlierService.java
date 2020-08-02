package com.matjazmav.googlekickstartme.service;

import com.matjazmav.googlekickstartme.constant.ProgrammingLanguage;
import com.matjazmav.googlekickstartme.dto.ChallengeResult;
import com.matjazmav.googlekickstartme.dto.Flier;
import com.matjazmav.googlekickstartme.dto.Score;
import lombok.val;
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

    public Flier getFlier(String nickname, ProgrammingLanguage primaryLanguage) throws IOException {
        val now = Instant.now().getEpochSecond() * 1000;

        // get all Kick Start challenges as empty results
        final Map<String, ChallengeResult> resultMap = gkss.getCompetitions().stream()
                .flatMap(a -> a.getChallenges().stream().map(c -> new ChallengeResult(){{
                    setChallengeId(c.getId());
                    setTimestamp(c.getTimestamp());
                }}))
                .collect(Collectors.toMap(ChallengeResult::getChallengeId, Function.identity()));

        val profile = gkss.getProfile(nickname);

        // inject actual results where user participated
        for (val challengeId : profile.getScores().stream().map(Score::getChallengeId).collect(Collectors.toList())) {
            resultMap.put(challengeId, gkss.getResults(challengeId, nickname));
        }

        // get last 14 relative ranking
        val rankings = resultMap.values().stream()
                .filter(cr -> cr.getTimestamp() <= now)
                .sorted((p1, p2) -> -Long.compare(p1.getTimestamp(), p2.getTimestamp()))
                .limit(14)
                .map(cr -> cr.getRankRelative())
                .collect(Collectors.toList());

        val totalScore = profile.getScores().stream().mapToInt(Score::getScore).sum();

        return new Flier(){{
            setNickname(nickname);
            setCountry(profile.getCountry());
            setPrimaryProgrammingLanguage(primaryLanguage);
            setLastRankings(rankings);
            setTotalScore(totalScore);
        }};
    }

}
