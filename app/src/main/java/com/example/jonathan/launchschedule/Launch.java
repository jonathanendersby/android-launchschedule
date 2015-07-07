package com.example.jonathan.launchschedule;

import java.util.Date;

public class Launch {
    // Very basic class to hold data for the individual launches.

    String description;
    String launchSite;
    String gmtDateString;
    Date date;
    String mission;

    @Override
    public String toString() {
        return this.description + ". " + this.gmtDateString;
    }
}