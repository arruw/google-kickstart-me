package com.matjazmav.googlekickstartme.dto;

import lombok.Data;

import java.util.List;

@Data
public class Competition {
    private String id;
    private String title;
    private long timestamp;
    private List<Challenge> challenges;
}
