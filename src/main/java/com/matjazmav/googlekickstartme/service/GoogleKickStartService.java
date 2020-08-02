package com.matjazmav.googlekickstartme.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matjazmav.googlekickstartme.dto.*;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.web.client.*;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.*;
import org.springframework.web.reactive.function.client.WebClient;
import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.*;

@Service
@Slf4j
public class GoogleKickStartService {

    private static final String BASE_URL = "https://codejam.googleapis.com";

    private final ObjectMapper objectMapper;
    private final WebClient  webClient;
    private final CacheManager cacheManager;

    public GoogleKickStartService(ObjectMapper objectMapper, RestTemplateBuilder restTemplateBuilder, CacheManager cacheManager) {
        this.objectMapper = objectMapper;
        this.cacheManager = cacheManager;
        this.webClient = WebClient
                .builder()
                .baseUrl(BASE_URL)
                .build();
    }

    /**
     * Get profile data with scores
     * @param nickname
     * @return profile
     * @throws IOException
     */
    @Cacheable("GoogleKickstartService.getProfile")
    public Profile getProfile(final String nickname) throws IOException {
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
                .map(s -> new Score(){{
                    setChallengeId(s.get("challenge_id").asText());
                    setScore(s.get("score").get("score_1").asInt());
                    setTimestamp(timestampMap.get(s.get("challenge_id").asText()));
                }})
                .collect(Collectors.toList());

        return new Profile(){{
            setNickname(nickname);
            setCountry(country);
            setScores(scores);
        }};
    }

    /**
     * Get users challenge result
     * @param challengeId
     * @param nickname
     * @return users challenge result
     * @throws IOException
     */
    @Cacheable("GoogleKickstartService.getResults")
    public ChallengeResult getResults(final String challengeId, final String nickname) throws IOException {
        var requestSpec = webClient.get().uri(uriBuilder -> uriBuilder
                .path("/scoreboard/{challengeId}/find")
                .queryParam("p", encodeJsonParameter("{\"nickname\":\"%s\",\"scoreboard_page_size\":1}", nickname))
                .build(challengeId));

        val body = objectMapper.readTree(Base64Utils.decodeFromString(requestSpec.retrieve().bodyToMono(String.class).block()));

        val rank = body.get("scoreboard_page_number").asInt();
        val rankRelative = (1.0 - (double)rank / body.get("full_scoreboard_size").asDouble()) * 100.0;
        val timestamp = body.get("challenge").get("end_ms").asLong();

        return new ChallengeResult(){{
            setChallengeId(challengeId);
            setRank(rank);
            setRankRelative((int)rankRelative);
            setTimestamp(timestamp);
        }};
    }

    /**
     * Get list of all Kick Start competitions and challenges
     * @return list of all Kick Start competitions and challenges
     * @throws IOException
     */
    @Cacheable("GoogleKickstartService.getCompetitions")
    public List<Competition> getCompetitions() throws IOException {
        var requestSpec = webClient.get().uri(uriBuilder -> uriBuilder
                .path("/poll")
                .queryParam("p", encodeJsonParameter("{}"))
                .build());

        val body = objectMapper.readTree(Base64Utils.decodeFromString(requestSpec.retrieve().bodyToMono(String.class).block()));

        return iteratorToStream(body.get("adventures").elements())
                .filter(a -> a.get("competition__str").asText().equals("KICK_START") && !a.get("challenges").isEmpty())
                .map(a -> new Competition(){{
                    setId(a.get("id").asText());
                    setTitle(a.get("title").asText());
                    setTimestamp(a.get("reg_end_ms").asLong());
                    setChallenges(iteratorToStream(a.get("challenges").elements())
                            .map(c -> new Challenge(){{
                                setId(c.get("id").asText());
                                setTitle(c.get("title").asText());
                                setTimestamp(c.get("end_ms").asLong());
                            }})
                            .collect(Collectors.toList()));
                }})
                .collect(Collectors.toList());
    }

    private static <T extends Object> Stream<T> iteratorToStream(Iterator<T> it) {
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(it, Spliterator.ORDERED), false);
    }

    private static String encodeJsonParameter(String json, Object... params) {
        return Base64Utils.encodeToString(String.format(json, params).getBytes());
    }
}
