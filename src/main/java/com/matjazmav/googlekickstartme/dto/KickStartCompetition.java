package com.matjazmav.googlekickstartme.dto;

import lombok.*;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class KickStartCompetition {
    private final String id;
    private final long timestamp;
    private final List<KickStartChallenge> challenges;
}
