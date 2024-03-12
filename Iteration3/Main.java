public class Main {
    public static void main(String[] args) {
        // Instantiate objects of the client, host, and server classes
        Floor client = new Floor();
        Scheduler host = new Scheduler();

        // Start the processes or threads (the functions for each class)
        Thread clientFloorThread = new Thread(() -> client.readCSV("data.csv", ",", 1026));
        Thread hostSchedulerThread = new Thread(host::schedulerRequests);

        // Start the threads
        clientFloorThread.start();
        hostSchedulerThread.start();
    }
}
