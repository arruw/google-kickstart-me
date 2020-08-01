package com.matjazmav.googlekickstartme.dto;

import lombok.Data;

import java.util.List;

@Data
public class Challenge {
    private String id;
    private String title;
    private long timestamp;
    private List<Task> tasks;
}
