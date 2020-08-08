package com.matjazmav.googlekickstartme.dto;

import lombok.*;

@Getter
@RequiredArgsConstructor
public class Badge {
    private final String nickname;
    private final String country;
    private final String language;
    private final Integer[] scores;
    private final Double[] relativeCumulativeScores;
    private final int totalScore;
    private final String countryFlagUrl;
    private final String languageUrl;
}