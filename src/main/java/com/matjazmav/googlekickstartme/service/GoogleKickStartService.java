package com.matjazmav.googlekickstartme.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matjazmav.googlekickstartme.dto.*;
import lombok.val;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.*;
import org.springframework.web.reactive.function.client.WebClient;
import java.io.IOException;
import java.util.*;
import java.util.stream.*;

@Service
public class GoogleKickStartService {

    private final String baseUrl = "https://codejam.googleapis.com";
    private final ObjectMapper objectMapper;
    private final WebClient  webClient;

    public GoogleKickStartService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.webClient = WebClient
                .builder()
                .baseUrl(baseUrl)
                .build();
    }

    public KickStartProfile getProfile(final String nickname) throws IOException {
        var requestSpec = webClient.get().uri(uriBuilder -> uriBuilder
                .path("/ranks")
                .queryParam("p", encodeJsonParameter("{\"nickname\":\"%s\"}", nickname))
                .build());

        val body = objectMapper.readTree(Base64Utils.decodeFromString(requestSpec.retrieve().bodyToMono(String.class).block()));

        val country = (!body.get("scores").isEmpty()) ? body.get("scores").get(0).get("score").get("country").asText() : null;

        final Map<String, Long> timestampMap = iteratorToStream(body.get("adventures").elements())
                .filter(a -> a.get("title").asText().startsWith("Kick Start"))
                .flatMap(a -> iteratorToStream(a.get("challenges").elements()))
                .collect(Collectors.toMap(c -> c.get("id").asText(), c -> c.get("end_ms").asLong()));

        val scores = iteratorToStream(body.get("scores").elements())
                .filter(s -> timestampMap.containsKey(s.get("challenge_id").asText()))
                .map(s -> new KickStartScore(
                        s.get("challenge_id").asText(),
                        s.get("score").get("rank").asInt(),
                        s.get("score").get("score_1").asInt(),
                        timestampMap.get(s.get("challenge_id").asText())
                ))
                .collect(Collectors.toList());

        return new KickStartProfile(nickname, country, scores);
    }

    @Cacheable("GoogleKickStartService.getKickStartSeries")
    public KickStartSeries getKickStartSeries() throws IOException {
        var requestSpec = webClient.get().uri(uriBuilder -> uriBuilder
                .path("/poll")
                .queryParam("p", encodeJsonParameter("{}"))
                .build());

        val body = objectMapper.readTree(Base64Utils.decodeFromString(requestSpec.retrieve().bodyToMono(String.class).block()));

        val competitions = iteratorToStream(body.get("adventures").elements())
                .filter(a -> a.get("competition__str").asText().equals("KICK_START") && !a.get("challenges").isEmpty())
                .map(a -> new KickStartCompetition(
                        a.get("id").asText(),
                        a.get("reg_end_ms").asLong(),
                        iteratorToStream(a.get("challenges").elements())
                                .map(c -> new KickStartChallenge(c.get("id").asText(), c.get("end_ms").asLong(), null))
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());

        return new KickStartSeries(competitions);
    }

    private static <T extends Object> Stream<T> iteratorToStream(Iterator<T> it) {
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(it, Spliterator.ORDERED), false);
    }

    private static String encodeJsonParameter(String json, Object... params) {
        return Base64Utils.encodeToString(String.format(json, params).getBytes());
    }
}
