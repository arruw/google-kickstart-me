package com.matjazmav.googlekickstartme.dto;

import lombok.*;

@Getter
@RequiredArgsConstructor
public class KickStartScore {
    private final String challengeId;
    private final int rank;
    private final int score;
    private final long timestamp;
}
