package com.example.nymble.project.model.output;

import lombok.Data;

import java.util.List;

@Data
public class SinglePassenger {
    private String passengerName;
    private String passengerNumber;
    private double balance;
    private List<UserOptedActivity> userOptedActivities;

}
