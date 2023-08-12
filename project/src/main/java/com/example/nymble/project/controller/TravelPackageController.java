package com.example.nymble.project.controller;

import com.example.nymble.project.model.Activity;
import com.example.nymble.project.model.TravelPackage;
import com.example.nymble.project.model.output.PassengerOutput;
import com.example.nymble.project.model.output.PrintItinerary;
import com.example.nymble.project.model.output.SinglePassenger;
import com.example.nymble.project.service.TravelPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TravelPackageController {

    private final TravelPackageService travelPackageService;

    @Autowired
    public TravelPackageController(TravelPackageService travelPackageService) {
        this.travelPackageService = travelPackageService;
    }

    /**
     * This will lets us to new add travel package in DB
     *
     * @param travelPackage
     * @return
     */
    @PostMapping("/addTravelPackage")
    public ResponseEntity<TravelPackage> addTravelPackage(@RequestBody TravelPackage travelPackage){
        TravelPackage travelPkg = travelPackageService.addTravelPackage(travelPackage);
        if(travelPkg == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(travelPackage, HttpStatus.OK);
    }

    /**
     * This Api will help us to fetch a particular travel Package using travel package Id
     *
     * Requirement - 1
     *
     * @param packageId
     * @return
     */
    @GetMapping("/travelPackages/{id}")
    public ResponseEntity<PrintItinerary> getItinerary(@PathVariable(value = "id") String packageId){
            PrintItinerary printItinerary = travelPackageService.getItinerary(packageId);
            if(printItinerary == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return new ResponseEntity<>(printItinerary, HttpStatus.OK);
    }

    /**
     * This Api will gives us the List of passengers
     *
     * Requirement - 2
     *
     * @param packageId
     * @return
     */
    @GetMapping("/travelPackages/{packageId}/passengers")
    public ResponseEntity<List<PassengerOutput>> getAllPassengers(@PathVariable("packageId") String packageId){
        List<PassengerOutput> passengerOutputList = travelPackageService.getPassengerDetails(packageId);
        if(passengerOutputList.isEmpty()){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(passengerOutputList, HttpStatus.OK);
    }

    /**
     * This Api will give individual passenger details
     *
     * Requirement - 3
     *
     * @param packageId
     * @param passengerId
     * @return
     */
    @GetMapping("/travelPackages/{packageId}/passengers/{passengerId}")
    public ResponseEntity<SinglePassenger> getSinglePassenger(@PathVariable("packageId") String packageId, @PathVariable("passengerId") String passengerId){
        SinglePassenger passenger = travelPackageService.getSinglePassengerDetails(packageId, passengerId);
        if(passenger == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(passenger, HttpStatus.OK);
    }

    /**
     * This Api will get All the Activities in which we have empty spaces to fill people
     *
     * Requirement - 4
     *
     * @param packageId
     * @return
     */
    @GetMapping("/travelPackages/{packageId}/activities")
    public ResponseEntity<List<Activity>> getAllActivities(@PathVariable("packageId") String packageId){
        List<Activity> activities = travelPackageService.getAllActivities(packageId);
        if(activities.isEmpty()){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(activities, HttpStatus.OK);
    }

    /**
     * This Api will update the users chosen activity name and users remaining balance and save it in DB
     *
     * @param packageId
     * @param passengerId
     * @param activity
     */
    @PostMapping("/travelPackages/{packageId}/passengers/{passengerId}")
    public ResponseEntity<String> userSignsUpForAnActivity(@PathVariable("packageId") String packageId,@PathVariable("passengerId") String passengerId, @RequestBody Activity activity){
        String output = travelPackageService.userSignsUpForAnActivity(packageId, passengerId, activity);
        return new ResponseEntity<>(output, HttpStatus.OK);
    }


    /**
     * This Api will fetch all the travel packages
     *
     * @return
     */
    @GetMapping("/travelPackages")
    public List<TravelPackage> getAllPackages(){
        return travelPackageService.getAllTravelPackages();
    }
}
