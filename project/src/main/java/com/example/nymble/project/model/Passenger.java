package com.example.nymble.project.model;

import com.example.nymble.project.enums.PassengerType;
import lombok.Data;

import java.util.List;

@Data
public class Passenger {
    private String passengerId;
    private String passengerName;
    private String passengerNumber;
    private PassengerType passengerType;
    private double balance;
    private List<String> activityList;
}
