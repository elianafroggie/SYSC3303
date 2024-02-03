// Thread for Elevator that is a client in the system
public class Elevator implements Runnable {

    // Shared buffer between the multiple threads
    private Scheduler sharedBuffer;
    private int currentFloor;
    private int targetFloor;

    // Elevator thread constructor
    public Elevator(Scheduler sharedBuffer) {
        this.sharedBuffer = sharedBuffer;
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
    public void run() {
        while (isAtRest()) {

            printStatus(); // Method prints the current status of the elevator as it should indicate that it is active to use

            Inform elevatorRequest = receiveElevatorInfo(); // Gets the elevator requests from the shared buffer

            if (elevatorRequest.getCurrentFloor() == currentFloor) {
                targetFloor = elevatorRequest.getButton(); // Updates target floor if request made for the current floor
            } else {
                moveFloors(elevatorRequest.getCurrentFloor(), elevatorRequest.getButton()); // Updates target floor and moves to destination floor, if the request was made for a different floor
            }

            printArrivalStatus();

            sharedBuffer.send(new Inform(currentFloor)); // Current floor gets informed to the shared buffer

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
    private void moveFloors(int sourceFloor, int destinationFloor) {
        updateCurrentFloor(sourceFloor);

        System.out.printf("The %s on Floor %d will go to Floor %d%n",
                Thread.currentThread().getName(), currentFloor, destinationFloor);

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
