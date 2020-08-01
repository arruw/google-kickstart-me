package com.matjazmav.googlekickstartme.dto;

import lombok.Data;

@Data
public class ChallengeResult {
    private String challengeId;
    private int rank;
    private int rankRelative;
    private long timestamp;
}
