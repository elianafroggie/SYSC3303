package org.example;

public class Main {
    public static void main(String[] args) {
        // Instantiate objects of the floor, scheduler and elevatorSubsystem
        Floor floor = new Floor();
        Scheduler host = new Scheduler();
        ElevatorSubsystem subsystem = new ElevatorSubsystem(2);

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
        for (int index = 0; index < 22; index++) {

            // Updating the specific position of each elevator at the current timestamp
            // Use the modulo operation in order to loop through the elevators, and obtaining the index as a result
            elevatorGUI.updateElevatorLocation(index % 4, index);

            try {
                // Pause the execution for about 1.5 seconds
                Thread.sleep(1500);

            } catch (InterruptedException interruptedException) {
                // Print any interrupted exceptions that can occur during the thread sleep time of 1500
                interruptedException.printStackTrace();
            }
        }
    }
}