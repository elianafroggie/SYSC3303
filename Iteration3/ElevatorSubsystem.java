import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.List;

public class ElevatorSubsystem {
    private final Elevator[] elevators;
    DatagramSocket sendSocket, receiveSocket;

    public ElevatorSubsystem(int numElevators) {
        elevators = new Elevator[numElevators];
        for (int i = 0; i < numElevators; i++) {
            elevators[i] = new Elevator(i, this);
            Thread elevatorThread = new Thread(elevators[i]);
            elevatorThread.start();
        }
    }

    public void addStops(int pickup, int destination, String direction, int elevatorId){
        if (elevatorId >= 0 && elevatorId < elevators.length) {
            Elevator elevator = elevators[elevatorId];
            try{
                elevator.addStops(pickup, destination, direction);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Elevator.elevatorState getState(int elevatorId) {
        if (elevatorId >= 0 && elevatorId < elevators.length) {
            Elevator elevator = elevators[elevatorId];
            System.out.println(elevator.getState() + " on floor " + elevator.currentFloor + " going " + elevator.getDirection());
            return elevator.getState();
        } else {
            // Handle invalid elevator ID, such as returning a default status or throwing an exception
            return null; // Example: returning null for an invalid ID
        }
    }

    public List<Integer> getStopList(int elevatorId){
        if (elevatorId >= 0 && elevatorId < elevators.length) {
            Elevator elevator = elevators[elevatorId];
            System.out.println(elevator.getStopList());
            return elevator.getStopList();
        } else {
            // Handle invalid elevator ID, such as returning a default status or throwing an exception
            return null; // Example: returning null for an invalid ID
        }

    }
    public List<Integer> getNextUpList(int elevatorId) {
        if (elevatorId >= 0 && elevatorId < elevators.length) {
            Elevator elevator = elevators[elevatorId];
            System.out.println(elevator.getNextUpList());
            return elevator.getNextUpList();
        } else {
            // Handle invalid elevator ID, such as returning a default status or throwing an exception
            return null; // Example: returning null for an invalid ID
        }
    }
    public String getDirection(int elevatorId){
        if (elevatorId >= 0 && elevatorId < elevators.length) {
            Elevator elevator = elevators[elevatorId];
            System.out.println(elevator.getDirection());
            return elevator.getDirection();
        } else {
            // Handle invalid elevator ID, such as returning a default status or throwing an exception
            return null; // Example: returning null for an invalid ID
        }

    }
    public void receiveAndScheduleRequests() {
    }

    public void run(){
    }


    public static void main(String[] args) {
        int numElevators = 2; // Number of elevators
        ElevatorSubsystem elevatorSubsystem = new ElevatorSubsystem(numElevators);
        //elevatorSubsystem.receiveAndScheduleRequests();


        // Example usage: control elevator 0
        elevatorSubsystem.addStops(1,3,"Up",0);
        elevatorSubsystem.addStops(4,1,"Down",0);
        elevatorSubsystem.addStops(2,4,"Up", 0);
        //elevatorSubsystem.getStopList(0);
        //elevatorSubsystem.getState(0);

        elevatorSubsystem.addStops(5,4,"Down",1);
        elevatorSubsystem.addStops(4,2, "Down", 1);
        elevatorSubsystem.addStops(1,3,"Up", 1);
        elevatorSubsystem.getNextUpList(1);
        //elevatorSubsystem.addStops(2,4,"Up", 1);
        //elevatorSubsystem.getStopList(1);
        //elevatorSubsystem.getState(1);
    }
}
