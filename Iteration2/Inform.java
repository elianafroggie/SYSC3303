// Immutable date-time object that represents a time, as in hour-minute-second
import java.time.LocalTime;
public class Inform {
    // Represents the car button associated with the floor
    private int button;
    // Represents the current floor of the floor/elevator in request
    private int currentFloor;
    // Represents the direction that the elevator is going in
    private String travelDirection;
    // Represents who is sending the message for the elevator or the floor
    private int messageSender;
    // Represents the time corresponding with the message
    private LocalTime currentTime;

    // Used logical constructors which take into account different scenarios when creating instances for Floor or Elevator
    // Constructor to be used for creating an instance when Floor is passing information to the Scheduler
    public Inform(int button, int currentFloor, String travelDirection, int messageSender, LocalTime currentTime){
        this.button = button; // during an event, set the button value based on the pressed button only
        this.currentFloor = currentFloor; // sets the current floor number
        this.travelDirection = travelDirection; // based on the event, set the direction of travel so either Up or Down
        this.messageSender = 0; // setting the send message to 0, means the transfer of information is from the floor component
        this.currentTime = currentTime;
    }
    // Constructor to be used when creating an instance when Elevator sends messages to the Scheduler
    public Inform(int currentFloor, LocalTime currentTime){
        this.button = 0; // button not applicable for this
        this.currentFloor = currentFloor;
        this.travelDirection = ""; // direction of travel set to an empty string, since it is not applicable
        this.messageSender = 1; // setting the send message to 1, means the information is being sent from the Elevator component
        this.currentTime = currentTime; // set the current time to null,
    }

    // The getter methods will allow us to access the internal state of the Inform object externally outside of the class to obtain information for example by the Scheduler system
    // Getter method to retrieve the car button values related to the event
    public int getButton() {
        return this.button;
    }
    // Getter method to retrieve the current elevator or floor number
    public int getCurrentFloor() {
        return this.currentFloor;
    }
    // Getter method to retrieve which subsystem between Floor or Elevator send the messages that created the event
    public int getMessageSender() {
        return this.messageSender;
    }
    // Getter method to retrieve the direction travelled (either Up or Down movements)
    public String getTravelDirection() {
        return this.travelDirection;
    }
    // Getter method to get the current time for the event process
    // Method will return an object of LocalTime class, which represents a time without a time zone or date
    public LocalTime getCurrentTime() {
        return this.currentTime;
    }
}
