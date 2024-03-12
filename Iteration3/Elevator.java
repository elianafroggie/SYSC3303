import java.util.ArrayList;
import java.util.List;

public class Elevator implements Runnable {
    protected enum elevatorState {
        DOORS_OPENING,
        DOORS_CLOSING,
        MOVING_UP,
        MOVING_DOWN,
        WAITING;
    }
    private final int elevatorId;
    private elevatorState currentState;
    int currentFloor = 1;
    private final ElevatorSubsystem elevatorSubsystem;
    private List<Integer> stopList = new ArrayList<>();
    private List<Integer> nextDownList = new ArrayList<>();
    private List<Integer> nextUpList = new ArrayList<>();
    private String direction = "";


    public Elevator(int elevatorId, ElevatorSubsystem elevatorSubsystem) {
        this.elevatorId = elevatorId;
        this.elevatorSubsystem = elevatorSubsystem;
        currentState = elevatorState.WAITING;
    }

    @Override
    public void run() {
        // Simulate elevator operations
        while (true) {
            if (currentState == elevatorState.WAITING) {
                if (stopList.size() >= 2) {
                    if (stopList.get(0) > stopList.get(1)) {
                        if (currentFloor == stopList.getFirst()) {
                            currentState = elevatorState.DOORS_OPENING;
                            //System.out.println("Doors opening on floor: " + currentFloor);
                        }
                        if (currentFloor > stopList.getFirst()) {
                            currentState = elevatorState.MOVING_DOWN;
                        }
                        if (currentFloor < stopList.getFirst()) {
                            currentState = elevatorState.MOVING_UP;
                        }
                        direction = "Down";
                    }
                    if (stopList.get(0) < stopList.get(1)) {
                        if (currentFloor == stopList.getFirst()) {
                            currentState = elevatorState.DOORS_OPENING;
                            //System.out.println("Doors opening on floor: " + currentFloor);
                        }
                        if (currentFloor < stopList.getFirst()) {
                            currentState = elevatorState.MOVING_DOWN;
                        }
                        if (currentFloor > stopList.getFirst()) {
                            currentState = elevatorState.MOVING_UP;
                        }
                        direction = "Up";
                    }
                } else {
                    if (nextUpList.isEmpty() && nextDownList.isEmpty()) {
                        currentState = elevatorState.WAITING;
                        direction = "";
                    }
                    if (nextUpList.size() >= nextDownList.size()) {
                        stopList = new ArrayList<>(nextUpList);
                        nextUpList.clear();
                        direction = "Up";
                    } else {
                        stopList = new ArrayList<>(nextDownList);
                        nextDownList.clear();
                        direction = "Down";
                    }
                }
            }
            if (currentState == elevatorState.DOORS_OPENING) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Elevator " + this.elevatorId + " : Doors open on floor: " + currentFloor);
                currentState = elevatorState.DOORS_CLOSING;
            }
            if (currentState == elevatorState.DOORS_CLOSING) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Elevator " + this.elevatorId + " : Doors closed on floor: " + currentFloor);
                stopList.removeFirst();

                if(stopList.size()>1) {
                    if (stopList.get(0) < stopList.get(1) || currentFloor < stopList.getFirst()) {
                        currentState = elevatorState.MOVING_UP;
                        direction = "Up";
                    }
                    if (stopList.get(0) > stopList.get(1)) {
                        currentState = elevatorState.MOVING_DOWN;
                        direction = "Down";
                    }
                }
                if(stopList.size() == 1){
                    //System.out.println("Size is 1");
                    if(stopList.getFirst() > currentFloor){
                        currentState = elevatorState.MOVING_UP;
                        direction = "Up";
                    }
                    if(stopList.getFirst() < currentFloor){
                        currentState = elevatorState.MOVING_DOWN;
                        direction = "Down";
                    }
                }
                if(stopList.isEmpty()){
                    currentState = elevatorState.WAITING;
                    direction = "";
                }
            }
            if (currentState == elevatorState.MOVING_UP) {
                System.out.println("Elevator " + this.elevatorId + " : Moving up");
                while (currentFloor < stopList.getFirst()) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    currentFloor += 1;
                }
                //stopList.removeFirst();
                currentState = elevatorState.DOORS_OPENING;
            }
            if (currentState == elevatorState.MOVING_DOWN) {
                System.out.println("Elevator " + this.elevatorId + " : Moving Down");
                while (currentFloor > stopList.getFirst()) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    currentFloor -= 1;
                }
                //stopList.removeFirst();
                currentState = elevatorState.DOORS_OPENING;
            }

        }

    }

    public void addStops(int pickupFloor, int destinationFloor, String direction){
        int index = 0;
        // This is the logic for adding floors to the floor list of stops for the elevator
        // Currently I have decided that starvation is a myth from big tech
        if(currentState == elevatorState.WAITING){
            stopList.add(pickupFloor);
            stopList.add(destinationFloor);
        }
        else if (direction.equals("Up") && this.direction.equals("Up") && currentFloor <= pickupFloor) {
            while (index < stopList.size() && stopList.get(index) < pickupFloor) {
                index++;
            }
            stopList.add(index, pickupFloor);

            while (index < stopList.size() && stopList.get(index) < destinationFloor) {
                index++;
            }
            stopList.add(index, destinationFloor);
        }
        else if (direction.equals("Down") && (this.direction.equals("Down") && currentFloor >= pickupFloor)){
            while (index < stopList.size() && stopList.get(index) > pickupFloor) {
                index++;
            }
            stopList.add(index, pickupFloor);

            while (index < stopList.size() && stopList.get(index) < destinationFloor) {
                index++;
            }
            stopList.add(index, destinationFloor);
        } else{
            if (direction.equals("Up")) {
                while (index < nextUpList.size() && nextUpList.get(index) < pickupFloor) {
                    index++;
                }
                nextUpList.add(index, pickupFloor);
                while (index < nextUpList.size() && nextUpList.get(index) < destinationFloor) {
                    index++;
                }
                nextUpList.add(index, destinationFloor);
            }
            if (direction.equals("Down")) {
                while (index < nextDownList.size() && nextDownList.get(index) > pickupFloor) {
                    index++;
                }
                nextDownList.add(index, pickupFloor);

                while (index < nextDownList.size() && nextDownList.get(index) < destinationFloor) {
                    index++;
                }
                nextDownList.add(index, destinationFloor);
            } else {
                System.out.println("Unable to add request to queue");
            }
        }
        System.out.println("Request for elevator added to elevator queue");
    }
    public elevatorState getState() {
        return currentState;
    }

    public List<Integer> getStopList(){
        return stopList;
    }
    public String getDirection(){
        return direction;
    }
}
