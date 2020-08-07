package com.matjazmav.googlekickstartme.dto;

import lombok.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
public class KickStartChallenge {
    private final String id;
    private final long timestamp;
    private @Setter Integer scoreboardSize;
}
