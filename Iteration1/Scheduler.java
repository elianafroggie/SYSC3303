import java.util.concurrent.BlockingQueue; // blocking queue is useful where one thread produces data that another thread consumes, so this mechanism is enabling synchronized communication
import java.util.concurrent.LinkedBlockingQueue; // the LinkedBlockingQueue class is an implementation of the BlockingQueue interface, and acts as a linked data structure

// The Scheduler subsystem is one of the three threads in this system, and will act as a server in the system
public class Scheduler implements Runnable {
    // Queues are mainly going to be used for asynchronous communication so subsystems can add requests individually, and
    // first BlockingQueue instance to handle requests from the Floor subsystem
    private final BlockingQueue<Inform> floorRequests;
    // second BlockingQueue instance to handle requests from the Elevator subsystem, with no other in-depth purpose
    private final BlockingQueue<Inform> elevatorRequests;
    // Variable named elevatorQueue for the declaration of the third private final BlockingQueue instance
    // Queue is mainly used to receive and store the requests that are only for the Elevator subsystem
    private final BlockingQueue<Inform> elevatorQueue;
    // The Scheduler() constructor initializes the queues with a LinkedBlockingQueue class
    public Scheduler() {
        // No fixed capacity allowing the queues to adjust dynamically to the number of requests received, and providing flexibility
        // LinkedBlockingQueue is a thread-safe design for the BlockingQueue interface, and used where multiple threads are involved
        floorRequests = new LinkedBlockingQueue<>();
        elevatorRequests = new LinkedBlockingQueue<>();
        // Added an initialization of the elevatorQueue using a new LinkedBlockingQueue instance where it is an empty queue (array) with a dynamic initial capacity
        elevatorQueue = new LinkedBlockingQueue<>();
    }

    // The Run method for the Scheduler thread
    @Override
    // This Run method would identify as the primary execution logic for this thread
    public void run() {
        System.out.println("The Scheduler is Starting Now...");

        // The Main loop for processing requests
        // Continuously processes requests from both the Floor and Elevator subsystems
        while (!Thread.interrupted()) {
            processRequests(0, "Floor"); // transfer of info is from floor component due to send message value is 0
            processRequests(1, "Elevator"); // transfer of info is from elevator component due to send message value is 1

            // delay of 1500 milliseconds
            try {
                Thread.sleep(1500);
                // the thread will remain active and running until it gets interrupted
            } catch (InterruptedException e) {
                // Handle interruption by setting the interrupt flag of the current thread
                Thread.currentThread().interrupt();

            }
        }
    }

    /**
     * Returns the BlockingQueue used to contain the Elevator requests
     * Method grants full access to the elevator queue list, so basically the method is allowing controlled access to the elevator queue from outside the Scheduler class
     * Queue is encapsulated so other classes can retrieve it without giving any internal information of the Scheduler class
     * @return BlockingQueue keeps the elevator requests
     */
    public BlockingQueue<Inform> getElevatorQueue() {
        return elevatorQueue;
    }

    // Method for processing requests of a specific subsystem
    private void processRequests(int componentId, String componentName) {
        // calls receive method to get the request from componentId
        // the request received will be stored in the request variable
        Inform request = receive(componentId);

        // must have a valid request to go on with the process
        if (request != null) {
            System.out.println("The Scheduler Thread is Receiving a Message from the following Subsystem:" + componentName);
            // Log the details of the received request, including the subsystem name and the info obtained in string representation
            System.out.println("Received the Request from" + componentName + " and the Request Contained Following Details: " + request.toString());

            // Using the send method, send the request back to the other subsystems in the project
            send(request);
        }
    }

    // Method for sending requests to the Scheduler component
    // uses the put method of the BlockingQueue so it has the ability to block the request if the queue is full in a situation
    public void send(Inform input) {
        try {
            // Check the message sender and put the input into the corresponding queue
            if (input.getMessageSender() == 0) {
                // Put (enqueue) the request in the floorRequests queue
                floorRequests.put(input);
            } else if (input.getMessageSender() == 1) {
                // Put the request in the elevatorRequests queue
                elevatorRequests.put(input);
            }
        } catch (InterruptedException e) {
            // Handle interruption by setting the interrupt flag of the current thread
            Thread.currentThread().interrupt();

        }
    }

    // Method for receiving requests from a particular subsystem
    // uses the take method basically where the method waits for a request to be available, and blocks if queue is empty
    public Inform receive(int fromWho) {
        // fromWho is the method parameter of the receive method which is used to figure out from which subsystem the request is arriving from
        try {
            // Check which subsystem it is, and take (dequeue) the request from the corresponding queue
            if (fromWho == 0) { // floor subsystem
                return floorRequests.take(); // take a request from floorRequests queue
            } else if (fromWho == 1) { // elevator subsystem
                return elevatorRequests.take(); // take a request from elevatorRequests queue
            }
        } catch (InterruptedException e) {
            // Handle interruption by setting the interrupt flag of the current thread
            Thread.currentThread().interrupt();
        }
        // it should return a null value if the queue is empty or an interruption state occurs
        return null;
    }

}

