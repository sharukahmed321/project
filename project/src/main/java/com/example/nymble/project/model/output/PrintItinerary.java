package com.example.nymble.project.model.output;

import com.example.nymble.project.model.Destination;
import lombok.Data;

import java.util.List;

@Data
public class PrintItinerary {
    private String travelPackageName;
    private List<Destination> destinationList;
}
