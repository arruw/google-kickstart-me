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
import java.util.stream.*;

@Service
@Slf4j
public class GoogleKickstartService {

    private static final String BASE_URL = "https://codejam.googleapis.com";

    private final ObjectMapper objectMapper;
    private final WebClient  webClient;
    private final CacheManager cacheManager;

    public GoogleKickstartService(ObjectMapper objectMapper, RestTemplateBuilder restTemplateBuilder, CacheManager cacheManager) {
        this.objectMapper = objectMapper;
        this.cacheManager = cacheManager;
        this.webClient = WebClient
                .builder()
                .baseUrl(BASE_URL)
                .build();
    }

    /**
     * Get challenge ids where user participated
     * @param nickname
     * @return Set of challenge ids where user participated
     * @throws IOException
     */
    @Cacheable("GoogleKickstartService.getChallenges")
    public Set<String> getChallenges(final String nickname) throws IOException {
        val now = Instant.now().getEpochSecond() * 1000;

        var requestSpec = webClient.get().uri(uriBuilder -> uriBuilder
                .path("/ranks")
                .queryParam("p", Base64Utils.encodeToString(String.format("{\"nickname\":\"%s\"}", nickname).getBytes()))
                .build());

        var body = objectMapper.readTree(Base64Utils.decodeFromString(requestSpec.retrieve().bodyToMono(String.class).block()));

        val kickstartChallengeIds = IteratorToStream(body.get("adventures").elements())
                .filter(a -> a.get("title").asText().startsWith("Kick Start"))
                .flatMap(a -> IteratorToStream(a.get("challenges").elements())
                        .filter(c -> c.get("end_ms").asLong() <= now)
                        .map(c -> c.get("id").asText()))
                .collect(Collectors.toSet());

        return IteratorToStream(body.get("scores").elements())
                .map(s -> s.get("challenge_id").asText())
                .filter(cid -> kickstartChallengeIds.contains(cid))
                .collect(Collectors.toSet());
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
                .queryParam("p", Base64Utils.encodeToString(String.format("{\"nickname\":\"%s\",\"scoreboard_page_size\":1}", nickname).getBytes()))
                .build(challengeId));

        var body = objectMapper.readTree(Base64Utils.decodeFromString(requestSpec.retrieve().bodyToMono(String.class).block()));

        val rank = body.get("scoreboard_page_number").asInt();
        val rankRelative = (double)rank / body.get("full_scoreboard_size").asDouble() * 100.0;
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
                .queryParam("p", "e30")
                .build());

        var body = objectMapper.readTree(Base64Utils.decodeFromString(requestSpec.retrieve().bodyToMono(String.class).block()));

        return IteratorToStream(body.get("adventures").elements())
                .filter(a -> a.get("competition__str").asText().equals("KICK_START") && !a.get("challenges").isEmpty())
                .map(a -> new Competition(){{
                    setId(a.get("id").asText());
                    setTitle(a.get("title").asText());
                    setTimestamp(a.get("reg_end_ms").asLong());
                    setChallenges(IteratorToStream(a.get("challenges").elements())
                            .map(c -> new Challenge(){{
                                setId(c.get("id").asText());
                                setTitle(c.get("title").asText());
                                setTimestamp(c.get("end_ms").asLong());
                            }})
                            .collect(Collectors.toList()));
                }})
                .collect(Collectors.toList());
    }

    private static <T extends Object> Stream<T> IteratorToStream(Iterator<T> it) {
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(it, Spliterator.ORDERED), false);
    }
}
