package com.matjazmav.googlekickstartme.dto;

import com.matjazmav.googlekickstartme.constant.ProgrammingLanguage;
import lombok.Data;

import java.util.List;

@Data
public class Flier {
    private String nickname;
    private String country;
    private ProgrammingLanguage primaryProgrammingLanguage;
    private List<Integer> lastRankings;
    private int totalScore;
}