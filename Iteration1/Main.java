import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        // Create shared buffer (scheduler)
        Scheduler scheduler = new Scheduler();

        // Create and start the floor system thread
        FloorSystem floorSystem = new FloorSystem(scheduler, new ArrayList<>(), 5);
        Thread floorSystemThread = new Thread(floorSystem, "FloorSystem");
        floorSystemThread.start();

        // Create and start the scheduler thread
        Thread schedulerThread = new Thread(scheduler, "Scheduler");
        schedulerThread.start();

        // Create and start one elevator thread
        Elevator elevator = new Elevator(scheduler);
        Thread elevatorThread = new Thread(elevator, "Elevator");
        elevatorThread.start();
    }
}
