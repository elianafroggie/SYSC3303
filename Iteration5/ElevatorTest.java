import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ElevatorTest {
    // Testing the starting state of the elevator
    @Test
    public void testElevatorStartingState() {
        // creating a new instance of the Elevator class, and passing the value of 1 as the elevator ID and null as the elevatorSubsystem
        // null would mean that no subsystem is associated with this elevator in the initial state of the elevator
        Elevator elevator = new Elevator(1, null);

        System.out.println("Assertion for the Elevator State as it is WAITING...");
        // Initially, we need to verify using assertEquals assertion that the elevator's state is WAITING
        assertEquals(Elevator.elevatorState.WAITING, elevator.getState());
        System.out.println("Elevator State is in WAITING State: " + (elevator.getState() == Elevator.elevatorState.WAITING));

        System.out.println("Assertion for Current Floor being at Level 1...");
        // Verify the current floor of the elevator is at 1 initially
        assertEquals(1, elevator.currentFloor);
        System.out.println("Current Floor at Level 1: " + (elevator.currentFloor == 1));

        System.out.println("Assertion for the Elevator Direction being an Empty String...");
        // Verify the elevator's direction is an empty string initially as you aren't in the moving state
        assertEquals("", elevator.getDirection());
        System.out.println("Elevator Direction ia an Empty String: " + (elevator.getDirection().isEmpty()));

        System.out.println("Assertion for the Stop List being Empty...");
        // Using the isEmpty() method, verify if the elevator's stop list is empty when it is initialized
        assertTrue(elevator.getStopList().isEmpty());
        System.out.println("Elevator Stop List is Empty: " + (elevator.getStopList().isEmpty()));
    }

    @Test
    public void testingElevatorAddStops() {
        // Created the 1st Elevator Instance with the elevatorID = 1, and a null ElevatorSubsystem
        Elevator elevator1 = new Elevator(1, null);

        // Stops added at Floors 4 and 7, with Upwards Direction for 1st Elevator
        elevator1.addStops(4, 7, "Up", 1);
        // assertEquals Assertion Statement that the Stop List Contains Floors 4 and 7
        assertEquals(Arrays.asList(4, 7), elevator1.getStopList());
        // assertEquals Assertion Statement that the Direction is Upwards
        assertEquals("Up", elevator1.getDirection());

        // Created the 2nd Elevator Instance with the elevatorID = 2, and a null ElevatorSubsystem
        Elevator elevator2 = new Elevator(2, null);

        // Stops added at Floors 3 and 6, with Upwards Direction for 1st Elevator
        elevator2.addStops(3, 6, "Down", 2);
        // assertEquals Assertion Statement that the Stop List Contains Floors 3 and 6
        assertEquals(Arrays.asList(3, 6), elevator2.getStopList());
        // assertEquals Assertion Statement that the Direction is Upwards
        assertEquals("Down", elevator2.getDirection());

        // Created the 3rd Elevator Instance with the elevatorID = 3, and a null ElevatorSubsystem
        Elevator elevator3 = new Elevator(3, null);

        // Stops added at Floors 2 and 4, with Upwards Direction for 1st Elevator
        elevator3.addStops(2, 4, "Up", 4);
        // assertEquals Assertion Statement that the Stop List Contains Floors 2 and 4
        assertEquals(Arrays.asList(2, 4), elevator3.getStopList());
        // assertEquals Assertion Statement that the Direction is Upwards
        assertEquals("Up", elevator3.getDirection());
    }

    @Test
    public void testingElevatorMovingUp() {
        // Creating a New Instance of the Elevator Class, and Passing the Value of 1 as the elevatorID and null as the ElevatorSubsystem
        // Null would mean that No Subsystem is Associated with this Elevator in the Initial State of the Elevator
        Elevator elevator = new Elevator(1, null);

        // Added Stops at pickupFloor 3 and destinationFloor 5 with Elevator Moving Upwards Direction
        elevator.addStops(3, 5, "Up", 2);

        // Executing the Elevator Movement
        elevator.run();

        // Assertion Statement Indicating the Elevator is on Floor 3 which is the pickupFloor
        assertEquals(3, elevator.currentFloor);

        // Assertion Statement Indicating the Doors are Opening for the Elevator on Floor 3
        assertEquals(Elevator.elevatorState.DOORS_OPENING, elevator.getState());
        elevator.run();

        // Assertion Statement Indicating the Doors are CLOSING for the Elevator on Floor 3
        assertEquals(Elevator.elevatorState.DOORS_CLOSING, elevator.getState());
        elevator.run();

        // Assertion Statement Indicating the Elevator is on Floor 5 which is the destinationFloor
        assertEquals(5, elevator.currentFloor);

        // Assertion Statement Indicating the Doors are CLOSING for the Elevator on Floor 5
        assertEquals(Elevator.elevatorState.DOORS_OPENING, elevator.getState());
    }

    @Test
    public void testingElevatorMovingDown() {
        // Creating a New Instance of the Elevator Class, and Passing the Value of 1 as the elevatorID and null as the ElevatorSubsystem
        // Null would mean that No Subsystem is Associated with this Elevator in the Initial state of the Elevator
        Elevator elevator = new Elevator(1, null);

        // Added Stops at pickupFloor 9 and destinationFloor 5 with Elevator Moving Downwards Direction
        elevator.addStops(9, 5, "Down", 1);

        // Executing the Elevator Movement
        elevator.run();

        // Assertion Statement Indicating the Elevator is on Floor 9 which is the pickupFloor
        assertEquals(9, elevator.currentFloor);

        // Assertion Statement Indicating the Doors are Opening for the Elevator on Floor 9
        assertEquals(Elevator.elevatorState.DOORS_OPENING, elevator.getState());
        elevator.run();

        // Assertion Statement Indicating the Doors are CLOSING for the Elevator on Floor 9
        assertEquals(Elevator.elevatorState.DOORS_CLOSING, elevator.getState());
        elevator.run();

        // Assertion Statement Indicating the Elevator is on Floor 5 which is the destinationFloor
        assertEquals(5, elevator.currentFloor);

        // Assertion Statement Indicating the Doors are CLOSING for the Elevator on Floor 5
        assertEquals(Elevator.elevatorState.DOORS_OPENING, elevator.getState());
    }

    // Situation 1: Testing Adding Stops While the Elevator is in Motion
    @Test
    public void testingAddStopsSituation1() {

        // Creating a New Instance of the Elevator Class, and Passing the Value of 1 as the elevatorID and null as the ElevatorSubsystem
        Elevator elevator = new Elevator(1, null);

        // Adding Stops in Elevator Initially at Floors 4 and 5 with an Upwards Direction Motion
        elevator.addStops(4, 5, "Up", 1);

        // Executing the Elevator Movement
        elevator.run();

        // Adding Stops in Elevator at Floors 7 and 9 with an Upwards Direction while in Motion
        elevator.addStops(7, 9, "Up", 3);

        // Executing the Elevator Movement
        elevator.run();

        // assertEquals Assertion Statement where the Stop List Consists of Floors 4, 5, 7, and 9
        assertEquals(Arrays.asList(4, 5, 7, 9), elevator.getStopList());
    }

    // Situation 2: Testing Adding Stops for the Elevator in Different Directions
    @Test
    public void testingAddStopsSituation2() {

        // Creating a New Instance of the Elevator Class, and Passing the Value of 1 as the elevatorID and null as the ElevatorSubsystem
        Elevator elevator = new Elevator(1, null);

        // Adding Stops in Elevator Initially at Floors 4 and 7 with an Upwards Direction Motion
        elevator.addStops(4, 7, "Up", 1);

        // Executing the Elevator Movement
        elevator.run();

        // Adding Stops in Elevator at Floors 3 and 1 with a Downwards Direction while in Motion
        elevator.addStops(3, 1, "Down", 1);

        // Executing the Elevator Movement
        elevator.run();

        // assertEquals Assertion Statement where the Stop List Consists of Floors 4, 7, 3, and 1
        assertEquals(Arrays.asList(4, 7, 3, 1), elevator.getStopList());
    }
}