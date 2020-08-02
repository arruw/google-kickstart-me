package com.matjazmav.googlekickstartme.dto;

import lombok.Data;

import java.util.List;

@Data
public class Profile {
    private String nickname;
    private String country;
    private List<Score> scores;
}
