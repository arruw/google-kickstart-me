package com.matjazmav.googlekickstartme.dto;

import lombok.Data;

@Data
public class Score {
    private String challengeId;
    private int rank;

    private int score;
    private long timestamp;
}
