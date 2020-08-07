package com.matjazmav.googlekickstartme.dto;

import lombok.*;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class KickStartProfile {
    private final String nickname;
    private final String country;
    private final List<KickStartScore> scores;
}
