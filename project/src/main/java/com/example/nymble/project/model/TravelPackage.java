package com.example.nymble.project.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
public class TravelPackage {
    @Id
    private String packageId;
    private String packageName;
    private int passengerCapacity;
    private List<Destination> destinationList; // itinerary
    private List<Passenger> passengerList;
}
