//package org.example;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        // Instantiate objects of the floor, scheduler and elevatorSubsystem
        Floor floor = new Floor();
        Scheduler host = new Scheduler();
        ElevatorSubsystem subsystem = new ElevatorSubsystem(4);

        // Instantiates the GUIForElevator object with 4 elevators, which sets up the GUI for displaying the real-time info about each elevator present in the control system
        GUIForElevator elevatorGUI = new GUIForElevator(4);

        // Start the processes or threads (the functions for each class)
        Thread clientFloorThread = new Thread(() -> floor.readCSV("data.csv", ","));
        Thread hostSchedulerThread = new Thread(host::scheduleRequests);
        Thread subsystemThread = new Thread(subsystem::sendReceiveRequests);

        // Start the threads
        clientFloorThread.start();
        hostSchedulerThread.start();
        subsystemThread.start();


        // Simulation of the movement of the elevators over time, and performing an iteration through the time in order to update the elevator positions accurately
        while(true) {
            String elevatorDataString = subsystem.getInfo();
            String[] elevatorSubsystemParts = elevatorDataString.split(",");

            for(int i = 0; i < elevatorSubsystemParts.length - 1; i += 6){
                int elevatorId = Integer.parseInt(elevatorSubsystemParts[i].trim());
                String state = elevatorSubsystemParts[1+i].trim();
                int currentFloor = Integer.parseInt(elevatorSubsystemParts[3+i].trim());
                int passengers = Integer.parseInt(elevatorSubsystemParts[5+i].trim());
                elevatorGUI.updateElevatorLocation(elevatorId, currentFloor, state, passengers);
            }

            try {Thread.sleep(1000);

            } catch (InterruptedException interruptedException) {
                // Print any interrupted exceptions that can occur during the thread sleep time of 1500
                interruptedException.printStackTrace();
            }
        }
    }
}
