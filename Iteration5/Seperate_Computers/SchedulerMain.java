//package org.example;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        // Instantiate objects of the floor, scheduler and elevatorSubsystem
        Scheduler host = new Scheduler();

        // Start the processes or threads (the functions for each class)
        Thread hostSchedulerThread = new Thread(host::scheduleRequests);

        // Start the threads
        hostSchedulerThread.start();


       
    }
}
