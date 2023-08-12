package com.example.nymble.project;

import com.example.nymble.project.controller.TravelPackageController;
import com.example.nymble.project.model.Activity;
import com.example.nymble.project.model.Destination;
import com.example.nymble.project.model.output.PassengerOutput;
import com.example.nymble.project.model.output.PrintItinerary;
import com.example.nymble.project.model.output.SinglePassenger;
import com.example.nymble.project.service.TravelPackageService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;


@SpringJUnitConfig
@WebMvcTest(controllers = TravelPackageController.class)
@Import({TravelPackageTest.MongoConfig.class})
public class TravelPackageTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TravelPackageService travelPackageService;

    @TestConfiguration
    public static class MongoConfig {
        @Bean
        public MongoTemplate mongoTemplate() {
            return Mockito.mock(MongoTemplate.class);
        }
    }

    /**
     * Requirement - 1 Success Case
     *
     * @throws Exception
     */
    @Test
    public void testGetItinerary_Success() throws Exception {
        PrintItinerary expectedItinerary = new PrintItinerary();
        expectedItinerary.setTravelPackageName("Explore Bangkok");
        List<Destination> destinations = new ArrayList<>();

        List<Activity> activityList = new ArrayList<>();
        Activity activity1 = new Activity();
        activity1.setActivityName("para sailing");
        activity1.setActivityDescription("Enjoy 5 minute ride");
        activity1.setActivityCapacity(0);
        activity1.setActivityCost(5000);
        activityList.add(activity1);
        Activity activity2 = new Activity();
        activity2.setActivityName("banana boat ride");
        activity2.setActivityDescription("Enjoy 5 minute ride");
        activity2.setActivityCapacity(14);
        activity2.setActivityCost(2000);
        activityList.add(activity2);
        destinations.add(new Destination("pattaya", activityList));

        List<Activity> activityList1 = new ArrayList<>();
        Activity activity3 = new Activity();
        activity3.setActivityName("boat ride");
        activity3.setActivityDescription("Enjoy 5 minute ride");
        activity3.setActivityCapacity(9);
        activity3.setActivityCost(3000);
        activityList1.add(activity3);
        destinations.add(new Destination("Krabi Island", activityList1));

        expectedItinerary.setDestinationList(destinations);

        when(travelPackageService.getItinerary("64d776bcd33ac02eec3b87db")).thenReturn(expectedItinerary);

        mockMvc.perform(MockMvcRequestBuilders.get("/travelPackages/64d776bcd33ac02eec3b87db")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.travelPackageName").value("Explore Bangkok"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.destinationList[0].destinationName").value("pattaya"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.destinationList[1].destinationName").value("Krabi Island"));
    }

    /**
     * Requirement -1 failure case
     *
     * @throws Exception
     */
    @Test
    public void testGetItinerary_NotFound() throws Exception {
        when(travelPackageService.getItinerary("nonExistentId")).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/travelPackages/nonExistentId")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.travelPackageName").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.destinationList").doesNotExist());
    }

    /**
     * Requirement - 2 Success Case
     *
     * @throws Exception
     */
    @Test
    public void testGetAllPassengers_Success() throws Exception {
        List<PassengerOutput> passengerOutputList = new ArrayList<>();

        PassengerOutput passengerOutput1 = new PassengerOutput();
        passengerOutput1.setPackageName("Explore Bangkok");
        passengerOutput1.setPassengerCapacity(10);
        passengerOutput1.setPassengerCount(3);
        passengerOutput1.setPassengerName("sharuk");
        passengerOutput1.setPassengerNumber("9999999999");
        passengerOutputList.add(passengerOutput1);

        PassengerOutput passengerOutput2 = new PassengerOutput();
        passengerOutput2.setPackageName("Explore Bangkok");
        passengerOutput2.setPassengerCapacity(10);
        passengerOutput2.setPassengerCount(3);
        passengerOutput2.setPassengerName("Ahmed");
        passengerOutput2.setPassengerNumber("8888888888");
        passengerOutputList.add(passengerOutput2);

        PassengerOutput passengerOutput3 = new PassengerOutput();
        passengerOutput3.setPackageName("Explore Bangkok");
        passengerOutput3.setPassengerCapacity(10);
        passengerOutput3.setPassengerCount(3);
        passengerOutput3.setPassengerName("shaik");
        passengerOutput3.setPassengerNumber("7777777777");
        passengerOutputList.add(passengerOutput3);

        when(travelPackageService.getPassengerDetails("64d776bcd33ac02eec3b87db")).thenReturn(passengerOutputList);

        mockMvc.perform(MockMvcRequestBuilders.get("/travelPackages/64d776bcd33ac02eec3b87db/passengers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].packageName").value("Explore Bangkok"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].passengerCount").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].passengerName").value("sharuk"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].passengerName").value("Ahmed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].passengerName").value("shaik"));
    }

    /**
     * Requirement - 2 Failure Case
     *
     * @throws Exception
     */
    @Test
    public void testGetAllPassengers_NoPassengers() throws Exception {
        List<PassengerOutput> passengerOutputList = new ArrayList<>(); // Empty list

        when(travelPackageService.getPassengerDetails("emptyPackageId")).thenReturn(passengerOutputList);

        mockMvc.perform(MockMvcRequestBuilders.get("/travelPackages/emptyPackageId/passengers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist()); // Check that response body is empty
    }

    /**
     * Requirement - 3 success case
     *
     * @throws Exception
     */
    @Test
    public void testGetSinglePassenger_Success() throws Exception {
        SinglePassenger singlePassenger = new SinglePassenger();
        singlePassenger.setPassengerName("shaik");
        singlePassenger.setPassengerNumber("9999999999");
        singlePassenger.setBalance(5000);
        singlePassenger.setUserOptedActivities(new ArrayList<>());

        when(travelPackageService.getSinglePassengerDetails("64d776bcd33ac02eec3b87db","1")).thenReturn(singlePassenger);

        mockMvc.perform(MockMvcRequestBuilders.get("/travelPackages/64d776bcd33ac02eec3b87db/passengers/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.passengerName").value("shaik"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.passengerNumber").value("9999999999"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value(5000))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userOptedActivities").value(new ArrayList<>()));
    }

    /**
     * Requirement - 3 Failure case
     *
     * @throws Exception
     */
    @Test
    public void testGetSinglePassenger_NotFound() throws Exception {
        when(travelPackageService.getSinglePassengerDetails("64d776bcd33ac02eec3b87db","5")).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/travelPackages/64d776bcd33ac02eec3b87db/5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.passengerName").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.passengerNumber").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userOptedActivities").doesNotExist());
    }

    /**
     * Requirement - 4 Success case
     *
     * @throws Exception
     */
    @Test
    public void testGetAllActivities_Success() throws Exception {
        List<Activity> activityList = new ArrayList<>();

        Activity activity2 = new Activity();
        activity2.setActivityName("banana boat ride");
        activity2.setActivityDescription("enjoy 5 minute ride");
        activity2.setActivityCapacity(14);
        activity2.setActivityCost(2000);
        activityList.add(activity2);

        Activity activity3 = new Activity();
        activity3.setActivityName("boat ride");
        activity3.setActivityDescription("enjoy 5 minute ride");
        activity3.setActivityCapacity(9);
        activity3.setActivityCost(3000);
        activityList.add(activity3);

        when(travelPackageService.getAllActivities("samplePackageId")).thenReturn(activityList);

        mockMvc.perform(MockMvcRequestBuilders.get("/travelPackages/samplePackageId/activities")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].activityName").value("banana boat ride"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].activityCapacity").value(14))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].activityName").value("boat ride"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].activityDescription").value("enjoy 5 minute ride"));
    }

    /**
     * Requirement -4 Failure case
     *
     * @throws Exception
     */
    @Test
    public void testGetAllActivities_NoActivities() throws Exception {
        List<Activity> activityList = new ArrayList<>(); // Empty list

        when(travelPackageService.getAllActivities("emptyPackageId")).thenReturn(activityList);

        mockMvc.perform(MockMvcRequestBuilders.get("/travelPackages/emptyPackageId/activities")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist()); // Check that response body is empty
    }




}

