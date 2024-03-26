public class Main {
    public static void main(String[] args) {
        // Instantiate objects of the floor, scheduler and elevatorSubsystem
        Floor floor = new Floor();
        Scheduler host = new Scheduler();
        ElevatorSubsystem subsystem = new ElevatorSubsystem(2);

        // Start the processes or threads (the functions for each class)
        Thread clientFloorThread = new Thread(() -> floor.readCSV("data.csv", ","));
        Thread hostSchedulerThread = new Thread(host::scheduleRequests);
        Thread subsystemThread = new Thread(subsystem::sendReceiveRequests);

        // Start the threads
        clientFloorThread.start();
        hostSchedulerThread.start();
        subsystemThread.start();
    }
}