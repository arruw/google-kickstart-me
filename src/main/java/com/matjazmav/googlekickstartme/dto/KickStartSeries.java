package com.matjazmav.googlekickstartme.dto;

import lombok.*;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class KickStartSeries {
    final private List<KickStartCompetition> competitions;
}
