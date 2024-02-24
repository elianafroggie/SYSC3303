import java.util.concurrent.BlockingQueue; // Imports the BlockingQueue Interface

// Thread for Elevator that is a client in the system
public class Elevator implements Runnable {

    // Shared buffer between the multiple threads
    private Scheduler sharedBuffer;
    private int currentFloor;
    private int targetFloor;
    private BlockingQueue<Inform> queuesRequested; // Allows the Elevator to Receive Requests from the Scheduler subsystem

    // Elevator thread constructor
    public Elevator(Scheduler sharedBuffer) {
        this.sharedBuffer = sharedBuffer;
        this.queuesRequested = sharedBuffer.getElevatorQueue(); // Obtain the elevator requests queue from the subsystem of Scheduler
        initializeElevator(); // Calls method for additional initialization
    }

    // Initializing the elevator with default values
    public void initializeElevator() {
        this.currentFloor = 1; // Initial state of the elevator as it starts at the first floor
        this.targetFloor = currentFloor; // Initially the elevator's initial target floor is the current floor
    }

    // Updates the current floor of the elevator
    public void updateCurrentFloor(int floor) {
        currentFloor = floor;
    }

    // Run method for the elevator thread
    // Continuously listens for Elevator requests from the added queue in Iteration 2 (queuesRequested), and processes the requested in the order received
    // Communicates the information with the shared buffer for the system
    public void run() {
        while (isAtRest()) {

            printStatus(); // Method prints the current status of the elevator as it should indicate that it is active to use

            // used to invoke method that may throw checked exceptions
            try {
                Inform elevatorRequest = queuesRequested.take(); // Gives the ability for the elevator to receive the requests from the queuesRequested
                // take() method is part of the Blocking Queue Interface where it is used to remove the first or the head element from the given queue
                // so once an element is actually taken from the queue, then it shouldn't be in the queue list

                if (elevatorRequest.getCurrentFloor() == currentFloor) {
                    targetFloor = elevatorRequest.getButton(); // Updates target floor if request made for the current floor
                } else {
                    moveFloors(elevatorRequest.getCurrentFloor(), elevatorRequest.getButton()); // Updates target floor and moves to destination floor, if the request was made for a different floor
                }
                printArrivalStatus();

                sharedBuffer.send(new Inform(currentFloor)); // Current floor gets informed to the shared buffer
            } catch(InterruptedException e) {
                Thread.currentThread().interrupt(); // Interrupt status of the current thread would be set if any methods will throws an InterruptedException
            }
            sleepModerately(); // Pauses the execution of program
        }
    }

    // Prints the initial status of the elevator, to detect if the elevator is available or not
    private void printStatus() {
        System.out.println(Thread.currentThread().getName() + " is available.");
    }

    // Prints the arrival status of the floor that it has arrived onto for the elevator
    private void printArrivalStatus() {
        System.out.println(Thread.currentThread().getName() + " arrives on the Floor " + currentFloor);
    }

    // Checks if the elevator is at rest, meaning the target floor hasn't changed yet from the current floor
    private boolean isAtRest() {
        return currentFloor == targetFloor;
    }

    // Shared buffer which is the Scheduler gives an elevator request, whereas receiver ID is given as parameter of value 1
    private Inform receiveElevatorInfo() {
        return sharedBuffer.receive(1);
    }

    // Method to used to explain the logic of moving between floors in the elevator car
    // Updated the moveFloors() method so that the logic can print the outcome of the elevator moving up or down according to the destination floor compared to the source floor
    private void moveFloors(int sourceFloor, int destinationFloor) {
        updateCurrentFloor(sourceFloor);

        if(destinationFloor > sourceFloor) {
            System.out.printf("The %s on Floor %d will go upwards to the Floor %d%n",
                    Thread.currentThread().getName(), currentFloor, destinationFloor);
        } else {
            System.out.printf("The %s on Floor %d will go downwards to the Floor %d%n",
                    Thread.currentThread().getName(), currentFloor, destinationFloor);
        }

        updateCurrentFloor(destinationFloor);
    }

    // Simulate delays in the running of the program, as the current thread sleeps for about 1.5 seconds
    private void sleepModerately() {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            // Sets the interrupt flag for the current thread meaning that the thread has been interrupted
            Thread.currentThread().interrupt();
        }
    }
}
