package com.example.nymble.project.model.output;

import lombok.Data;

@Data
public class UserOptedForActivity {
    private double remainingBalance;
    private boolean userOptedForActivity; // this for STANDARD, PREMIUM and GOLD users
}
