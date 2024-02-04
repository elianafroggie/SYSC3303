import java.util.concurrent.ArrayBlockingQueue;

public class Scheduler implements Runnable {
    private ArrayBlockingQueue<Inform> floorRequests; // Queue for floor requests
    private ArrayBlockingQueue<Inform> elevatorRequests; // Queue for elevator requests

    public Scheduler() {
        floorRequests = new ArrayBlockingQueue<>(10); // Assuming a capacity of 10 for demonstration purposes
        elevatorRequests = new ArrayBlockingQueue<>(10);
    }

    public void send(Inform input) {
        if (input.getMessageSender() == 0) {
            // Message from Floor subsystem
            floorRequests.add(input);
        } else if (input.getMessageSender() == 1) {
            // Message from Elevator subsystem
            elevatorRequests.add(input);
        }
    }

    public Inform receive(int toWho) {
        if (toWho == 0 && !floorRequests.isEmpty()) {
            // Request for Floor subsystem
            return floorRequests.poll();
        } else if (toWho == 1 && !elevatorRequests.isEmpty()) {
            // Request for Elevator subsystem
            return elevatorRequests.poll();
        }

        return null;
    }

    @Override
    public void run() {
        System.out.println("Scheduler starts..");

        // For demonstration purposes, you can add further logic here

        // For example, handling requests and sending them back to the respective subsystems
        while (true) {
            Inform floorRequest = receive(0);
            if (floorRequest != null) {
                System.out.println("Scheduler received a message from Floor");
                // Further processing for Floor request
                // For iteration 1, you may just print the received information
                System.out.println("Received Floor Request: " + floorRequest.toString());
                // Sending it back to Elevator
                send(floorRequest);
            }

            Inform elevatorRequest = receive(1);
            if (elevatorRequest != null) {
                System.out.println("Scheduler received a message from Elevator");
                // Further processing for Elevator request
                // For iteration 1, you may just print the received information
                System.out.println("Received Elevator Request: " + elevatorRequest.toString());
                // Sending it back to Floor
                send(elevatorRequest);
            }

            // Add a delay or additional logic here for better simulation
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
