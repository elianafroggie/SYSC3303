public class Main {
    public static void main(String[] args) {
        // Instantiate objects of the client, host, and server classes
        ClientFloor client = new ClientFloor();
        HostScheduler host = new HostScheduler();
        ServerElevator[] servers = new ServerElevator[3];
        for (int i = 0; i < servers.length; i++) {
            servers[i] = new ServerElevator(i);
            servers[i].startThreads();
        }

        // Start the processes or threads (the functions for each class)
        Thread clientFloorThread = new Thread(() -> client.readCSV("", "", 23));
        Thread hostSchedulerThread = new Thread(host::schedulerRequests);

        // Start the threads
        clientFloorThread.start();
        hostSchedulerThread.start();
    }
}
