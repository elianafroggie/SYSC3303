public class FloorMain {
    public static void main(String[] args) {
        // Instantiate objects of the floor, scheduler and elevatorSubsystem
        Floor floor = new Floor();

        // Start the processes or threads (the functions for each class)
        Thread clientFloorThread = new Thread(() -> floor.readCSV("data.csv", ","));

        // Start the threads
        clientFloorThread.start();
    }
}
