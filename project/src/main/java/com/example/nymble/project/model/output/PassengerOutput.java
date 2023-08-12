package com.example.nymble.project.model.output;

import lombok.Data;

@Data
public class PassengerOutput {
    private String packageName;
    private int passengerCapacity;
    private int passengerCount;
    private String passengerName;
    private String passengerNumber;
}
