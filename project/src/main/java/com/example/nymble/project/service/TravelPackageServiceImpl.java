package com.example.nymble.project.service;

import com.example.nymble.project.enums.PassengerType;
import com.example.nymble.project.model.Activity;
import com.example.nymble.project.model.Destination;
import com.example.nymble.project.model.Passenger;
import com.example.nymble.project.model.TravelPackage;
import com.example.nymble.project.model.output.*;
import com.example.nymble.project.repository.TravelPackageRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TravelPackageServiceImpl implements TravelPackageService{

    private final TravelPackageRepo travelPackageRepo;

    @Autowired
    public TravelPackageServiceImpl(TravelPackageRepo travelPackageRepo) {
        this.travelPackageRepo = travelPackageRepo;
    }

    /**
     * This will give the Itinerary which has package name and list of destinations
     *
     * Requirement - 1
     *
     * @param packageId
     * @return
     */
    @Override
    public PrintItinerary getItinerary(String packageId){
        Optional<TravelPackage> travelPackageOptional = travelPackageRepo.findById(packageId);
        TravelPackage travelPackage = travelPackageOptional.orElse(null);
        if(travelPackage == null) return null;
        PrintItinerary printItinerary = new PrintItinerary();
        if(travelPackage != null && travelPackage.getPackageName() != null){
            printItinerary.setTravelPackageName(travelPackage.getPackageName());
        }
        if(travelPackage != null && travelPackage.getDestinationList() != null){
            printItinerary.setDestinationList(travelPackage.getDestinationList());
        }
        return printItinerary;
    }

    /**
     * This function will return List of passengers. Each passenger will have below fields
     *  passenger -> {packageName, passengerCapacity, passengerCount, passengerName, passengerNumber}
     *
     *  Requirement - 2
     *
     * @param packageId
     * @return
     */
    @Override
    public List<PassengerOutput> getPassengerDetails(String packageId){
        List<PassengerOutput> resultantPassengerList = new ArrayList<>();
        Optional<TravelPackage> travelPackageOptional = travelPackageRepo.findById(packageId);
        TravelPackage travelPackage = travelPackageOptional.orElse(null);
        if(travelPackage != null && travelPackage.getPassengerList().size() != 0) {
            for (Passenger passenger : travelPackage.getPassengerList()) {
                PassengerOutput passengerOutput = new PassengerOutput();
                if (travelPackage.getPackageName() != null) {
                    passengerOutput.setPackageName(travelPackage.getPackageName());
                }
                if (travelPackage.getPassengerCapacity() != 0) {
                    passengerOutput.setPassengerCapacity(travelPackage.getPassengerCapacity());
                }
                if (travelPackage.getPassengerList().size() != 0) {
                    passengerOutput.setPassengerCount(travelPackage.getPassengerList().size());
                }
                passengerOutput.setPassengerName(passenger.getPassengerName());
                passengerOutput.setPassengerNumber(passenger.getPassengerNumber());
                resultantPassengerList.add(passengerOutput);
            }
        }
        return  resultantPassengerList;
    }

    /**
     * This function is responsible for getting single passenger details
     *
     * Requirement - 3
     *
     * @param packageId
     * @param passengerId
     * @return
     */
    @Override
    public SinglePassenger getSinglePassengerDetails(String packageId, String passengerId){
        Optional<TravelPackage> travelPackageOptional = travelPackageRepo.findById(packageId);
        TravelPackage travelPackage = travelPackageOptional.orElse(null);
        SinglePassenger singlePassenger = new SinglePassenger();
        if(travelPackage != null  && travelPackage.getPassengerList().size() > 0){
            for(Passenger passenger : travelPackage.getPassengerList()){
                if(passenger.getPassengerId().equals(passengerId)){
                    singlePassenger.setPassengerName(passenger.getPassengerName());
                    singlePassenger.setPassengerNumber(passenger.getPassengerNumber());
                    singlePassenger.setBalance(passenger.getBalance());
                    singlePassenger.setUserOptedActivities(userOptedActivities(travelPackage, passenger.getActivityList()));
                }
            }
        }
        return singlePassenger;
    }


    /**
     * This function will gives us the user opted activities in the specific output form
     *
     * @param travelPackage
     * @param activityList
     * @return
     */
    public List<UserOptedActivity> userOptedActivities(TravelPackage travelPackage, List<String> activityList){
        List<UserOptedActivity> activityOutputList = new ArrayList<>();
        for(Destination destination : travelPackage.getDestinationList()){
            helperForUserOptedActivities(activityOutputList,destination,activityList);
        }
        return activityOutputList;
    }

    /**
     * This is a helper function, which gives us the list of activities opted by the user
     *
     * @param activityOutputList
     * @param destination
     * @param activityNameList
     */
    public void helperForUserOptedActivities(List<UserOptedActivity> activityOutputList, Destination destination, List<String> activityNameList){
        for(Activity activity : destination.getActivityList()) {
            for(String activityName : activityNameList) {
                if (activity.getActivityName().equals(activityName)) {
                    UserOptedActivity userOptedActivity = new UserOptedActivity();
                    userOptedActivity.setActivityName(activity.getActivityName());
                    userOptedActivity.setPrice(activity.getActivityCost());
                    userOptedActivity.setDestinationName(destination.getDestinationName());
                    activityOutputList.add(userOptedActivity);
                }
            }
        }
    }

    /**
     * This will fetch all activities which still have spaces in a particular package
     *
     * requirement - 4
     *
     * @param packageId
     * @return
     */
    @Override
    public List<Activity> getAllActivities(String packageId){
        List<Activity> activitiesWithSpaces = new ArrayList<>();
        Optional<TravelPackage> travelPackageOptional = travelPackageRepo.findById(packageId);
        TravelPackage travelPackage = travelPackageOptional.orElse(null);
        if(travelPackage != null && travelPackage.getDestinationList().size() > 0) {
            for (Destination destination : travelPackage.getDestinationList()){
                for(Activity activity : destination.getActivityList()){
                    if(activity.getActivityCapacity() > 0){
                        activitiesWithSpaces.add(activity);
                    }
                }
            }
        }
        return activitiesWithSpaces;
    }

    /**
     * This function updates the necessary details in passenger and then save it in travel repo DB
     *
     * @param packageId
     * @param passengerId
     * @param activity
     */
    @Override
    public String userSignsUpForAnActivity(String packageId, String passengerId, Activity activity){
        Optional<TravelPackage> travelPackageOptional = travelPackageRepo.findById(packageId);
        TravelPackage travelPackage = travelPackageOptional.orElse(null);
        String output = "";
        if(travelPackage != null && travelPackage.getPassengerList().size() > 0) {
            // find how many spots are still empty
            int activityCapacity = findEmptySpots(travelPackage, activity);
            if(activityCapacity > 0){
                // reduce the available slots in activity if user signs up for a new activity
                reduceAvailableActivitySlotsAndSaveToDB(travelPackage,activity);

                // update amount only if passenger finds a spot, when he signs up for a new activity
                updateAmountOfPassengerAndSaveToDB(travelPackage,passengerId,activity);
                output = "User has SuccessFully Signed Up For the Activity";
            } else {
                output = "There are No spots Left for this activity";
            }
        }
        return output;
    }

    /**
     * This function helps us to update Amount in users wallet and Add new Activity to the
     * users activity list when he signs up for a new activity
     *
     * @param travelPackage
     * @param passengerId
     * @param activity
     */
    public void updateAmountOfPassengerAndSaveToDB(TravelPackage travelPackage, String passengerId, Activity activity){
        List<Passenger> passengerList = travelPackage.getPassengerList();
        for (int i=0;i<passengerList.size();i++){
            Passenger passenger = travelPackage.getPassengerList().get(i);
            if(passenger.getPassengerId().equals(passengerId)){
                // call the below function to get the discounted price
                UserOptedForActivity userOptedForActivity = calculateDiscountedPriceWhenUserSignsUpForActivity(passenger, activity);
                // compute the logic if user has debited his amount for activity
                if(userOptedForActivity.isUserOptedForActivity()){
                    passenger.setBalance(userOptedForActivity.getRemainingBalance()); // set updated balance
                    List<String> existingActivities = passenger.getActivityList();
                    existingActivities.add(activity.getActivityName()); // add new activity
                    passenger.setActivityList(existingActivities);
                    travelPackage.getPassengerList().set(i, passenger);
                    travelPackageRepo.save(travelPackage);
                    break;
                }
            }
        }
    }

    /**
     * This Function reduce the available slots by decrementing by 1, when a user signs up
     * for an activity
     *
     * @param travelPackage
     * @param activity
     */
    public void reduceAvailableActivitySlotsAndSaveToDB(TravelPackage travelPackage, Activity activity){
        List<Destination> destinationList = travelPackage.getDestinationList();
        for(int i=0; i < destinationList.size(); i++){
            Destination destination = destinationList.get(i);
            for(int j=0;j<destination.getActivityList().size();j++){
                Activity act = destination.getActivityList().get(j);
                if(activity.getActivityName().equals(act.getActivityName())){
                    int  updatedActivitySize = act.getActivityCapacity()>0 ? act.getActivityCapacity()-1 : 0;
                    // if we keep on decrementing the activity count, it would become zero at some point
                    // then make sure to mark updatedActivitySize to 0 -> It says there are no empty spots fo users
                    act.setActivityCapacity(updatedActivitySize);
                    destination.getActivityList().set(j,act);
                    travelPackage.getDestinationList().set(i,destination);
                    travelPackageRepo.save(travelPackage);
                    break;
                }
            }
        }
    }

    /**
     * This Function gives us how many empty spots are present in a particular activity
     *
     * @param travelPackage
     * @param activity
     * @return
     */
    public int findEmptySpots(TravelPackage travelPackage, Activity activity){
        List<Destination> destinationList = travelPackage.getDestinationList();
        int capacity = 0;
        for(int i=0; i < destinationList.size(); i++){
            Destination destination = destinationList.get(i);
            for(int j=0;j<destination.getActivityList().size();j++){
                Activity act = destination.getActivityList().get(j);
                if(activity.getActivityName().equals(act.getActivityName())){
                    capacity = act.getActivityCapacity();
                    break;
                }
            }
        }
        return capacity;
    }

    /**
     * When a particular passenger signs up for an activity this function helps us find
     * the discounted price depending on the type of passenger (STANDARD, GOLD, PREMIUM)
     *
     * @param passenger
     * @param activity
     * @return
     */
    public UserOptedForActivity calculateDiscountedPriceWhenUserSignsUpForActivity(Passenger passenger, Activity activity){
        UserOptedForActivity userOptedForActivity = new UserOptedForActivity();
        if(passenger.getPassengerType() == PassengerType.STANDARD){
            if(passenger.getBalance() >= activity.getActivityCost()){
                userOptedForActivity.setRemainingBalance(passenger.getBalance() - activity.getActivityCost());
                userOptedForActivity.setUserOptedForActivity(true);
            } else {
                userOptedForActivity.setUserOptedForActivity(false);
                userOptedForActivity.setRemainingBalance(passenger.getBalance());
            }
        } else if (passenger.getPassengerType() == PassengerType.GOLD) {
            // give 10 percentage discount if user is GOLD member
            double discountedPrice = activity.getActivityCost() - (activity.getActivityCost()/10.0);
            if(passenger.getBalance() >= discountedPrice) {
                userOptedForActivity.setRemainingBalance(passenger.getBalance() - discountedPrice);
                userOptedForActivity.setUserOptedForActivity(true);
            }
            else {
                userOptedForActivity.setRemainingBalance(passenger.getBalance());
                userOptedForActivity.setUserOptedForActivity(false);
            }
        } else {
            // dont debit any balance from user if he is PREMIUM user
            userOptedForActivity.setRemainingBalance(passenger.getBalance());
            userOptedForActivity.setUserOptedForActivity(true);
        }
        return userOptedForActivity;
    }

    /**
     * This will help us to add a new Travel package
     *
     * @param travelPackage
     * @return
     */
    @Override
    public TravelPackage addTravelPackage(TravelPackage travelPackage) {
        return travelPackageRepo.save(travelPackage);
    }

    /**
     * This will get All Travel Packages from DB
     *
     * @return
     */
    @Override
    public List<TravelPackage> getAllTravelPackages() {
        return travelPackageRepo.findAll();
    }
}

/**
 * This is the sample Input of Each Package
 *
 * {
 *     "packageName" : "Explore Bangkok",
 *     "passengerCapacity" : 10,
 *     "destinationList" : [
 *         {
 *             "destinationName" : "pataya",
 *             "activityList" : [
 *                 {
 *                     "activityName" : "para sailing",
 *                     "activityDescription" : "enjoy 5 minute ride",
 *                     "activityCost" : 5000,
 *                     "activityCapacity" : 10
 *                 },
 *                 {
 *                     "activityName" : "banana boat ride",
 *                     "activityDescription" : "enjoy 5 minute ride",
 *                     "activityCost" : 2000,
 *                     "activityCapacity" : 15
 *                 }
 *             ]
 *         },
 *         {
 *             "destinationName" : "Krabi Island",
 *             "activityList" : [
 *                 {
 *                     "activityName" : "boat ride",
 *                     "activityDescription" : "enjoy 5 minute ride",
 *                     "activityCost" : 3000,
 *                     "activityCapacity" : 10
 *                 }
 *             ]
 *         }
 *     ],
 *     "passengerList" : [
 *         {
 *             "passengerId" : 1,
 *             "passengerName" : "sharuk",
 *             "passengerNumber" : "9999999999",
 *             "passengerType" : "GOLD",
 *             "balance" : 50000,
 *             "activityList" : []
 *         },
 *         {
 *             "passengerId" : 2,
 *             "passengerName" : "Ahmed",
 *             "passengerNumber" : "8888888888",
 *             "passengerType" : "STANDARD",
 *             "balance" : 50000,
 *             "activityList" : []
 *         }
 *     ]
 * }
 *
 *
 *
 */