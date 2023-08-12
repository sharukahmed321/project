package com.example.nymble.project.service;

import com.example.nymble.project.model.Activity;
import com.example.nymble.project.model.TravelPackage;
import com.example.nymble.project.model.output.PassengerOutput;
import com.example.nymble.project.model.output.PrintItinerary;
import com.example.nymble.project.model.output.SinglePassenger;

import java.util.List;

public interface TravelPackageService {
    PrintItinerary getItinerary(String packageId);

    List<PassengerOutput> getPassengerDetails(String packageId);

    SinglePassenger getSinglePassengerDetails(String packageId, String passengerId);

    List<Activity> getAllActivities(String packageId);

    String userSignsUpForAnActivity(String packageId, String passengerId, Activity activity);

    TravelPackage addTravelPackage(TravelPackage travelPackage);

    List<TravelPackage> getAllTravelPackages();
}
