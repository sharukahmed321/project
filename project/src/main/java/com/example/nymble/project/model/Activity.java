package com.example.nymble.project.model;

import lombok.Data;

@Data
public class Activity {
    private String activityName;
    private String activityDescription;
    private double activityCost;
    private int activityCapacity;
}
