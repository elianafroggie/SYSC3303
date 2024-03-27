import java.util.*;

public class Elevator implements Runnable {
    public enum elevatorState {
        DOORS_OPENING,
        DOORS_CLOSING,
        MOVING_UP,
        MOVING_DOWN,
        WAITING
    }
    private final int elevatorId;
    private elevatorState currentState;
    int currentFloor;
    private final ElevatorSubsystem elevatorSubsystem;
    private List<Integer> stopList;
    private List<Integer> nextDownList;
    private List<Integer> nextUpList;
    private String direction = "Null";


    public Elevator(int elevatorId, ElevatorSubsystem elevatorSubsystem) {
        this.elevatorId = elevatorId;
        this.elevatorSubsystem = elevatorSubsystem;
        this.stopList = new ArrayList<>();
        this.nextDownList = new ArrayList<>();
        this.nextUpList = new ArrayList<>();
        currentState = elevatorState.WAITING;
        currentFloor = 1;
        System.out.println("Elevator created: " + this.elevatorId);
    }

    @Override
    public void run() {
        System.out.println("Running");
        // Simulate elevator operations im not explaining it just read it
        while (true) {
            stopList = getStopList();
            nextDownList = getNextDownList();
            nextUpList = getNextUpList();
            if (currentState == elevatorState.WAITING) {
                setState(elevatorState.WAITING);
                if(stopList.isEmpty()){
                    if (!nextUpList.isEmpty() && nextUpList.size() >= nextDownList.size()) {
                        stopList.addAll(nextUpList);
                        nextUpList.clear();
                        System.out.println(stopList);
                    } else if(!nextDownList.isEmpty()){
                        stopList.addAll(nextDownList);
                        nextDownList.clear();
                    }
                }

                if (stopList.size() >= 2) {
                    if (stopList.get(0) > stopList.get(1)) {
                        if (currentFloor == stopList.getFirst()) {
                            currentState = elevatorState.DOORS_OPENING;
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
                        if (currentFloor > stopList.getFirst()) {
                            currentState = elevatorState.MOVING_DOWN;
                        }
                        if (currentFloor < stopList.getFirst()) {
                            currentState = elevatorState.MOVING_UP;
                        }
                        direction = "Up";
                    }
                }
            }

            if (currentState == elevatorState.DOORS_OPENING) {
                setState(elevatorState.DOORS_OPENING);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Elevator " + this.elevatorId + " : Doors open on floor: " + currentFloor);
                currentState = elevatorState.DOORS_CLOSING;
            }
            if (currentState == elevatorState.DOORS_CLOSING) {
                setState(elevatorState.DOORS_CLOSING);
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
                    }
                    if (stopList.get(0) > stopList.get(1)) {
                        currentState = elevatorState.MOVING_DOWN;
                    }
                }
                if(stopList.size() == 1){
                    //System.out.println("Size is 1");
                    if(stopList.getFirst() > currentFloor){
                        currentState = elevatorState.MOVING_UP;
                    }
                    if(stopList.getFirst() < currentFloor){
                        currentState = elevatorState.MOVING_DOWN;
                    }
                }
                if(stopList.isEmpty()){
                    currentState = elevatorState.WAITING;
                    direction = "Null";
                }
            }
            if (currentState == elevatorState.MOVING_UP) {
                setState(elevatorState.MOVING_UP);
                System.out.println("Elevator " + this.elevatorId + " : Moving up");
                while (currentFloor < stopList.getFirst()) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    currentFloor += 1;
                    setCurrentFloor(currentFloor);
                    System.out.println("Elevator " + elevatorId + " lamp - floor " + currentFloor);
                }
                currentState = elevatorState.DOORS_OPENING;
            }
            if (currentState == elevatorState.MOVING_DOWN) {
                setState(elevatorState.MOVING_DOWN);
                System.out.println("Elevator " + this.elevatorId + " : Moving Down");
                while (currentFloor > stopList.getFirst()) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    currentFloor -= 1;
                    setCurrentFloor(currentFloor);
                    System.out.println("Elevator " + elevatorId + " lamp - floor " + currentFloor);
                }
                currentState = elevatorState.DOORS_OPENING;
            }

        }

    }

    public synchronized void addStops(int pickupFloor, int destinationFloor, String direction){
        // This is the logic for adding floors to the floor list of stops for the elevator
        // Currently I have decided that starvation is a myth from big tech
        if(currentState == elevatorState.WAITING && stopList.isEmpty() && nextDownList.isEmpty() && nextUpList.isEmpty()){
            stopList.add(pickupFloor);
            stopList.add(destinationFloor);
        }
        else if (direction.equals("Up") && this.direction.equals("Up") &&
                (currentFloor <= pickupFloor || currentFloor >= stopList.getFirst())) {
            stopList.add(pickupFloor);
            stopList.add(destinationFloor);
            Collections.sort(stopList);
            HashSet<Integer> set = new HashSet<>(stopList);
            stopList.clear();
            stopList.addAll(set);
        }
        else if (direction.equals("Down") && (this.direction.equals("Down") &&
                (currentFloor >= pickupFloor || currentFloor <= stopList.getFirst()))){
            stopList.add(pickupFloor);
            stopList.add(destinationFloor);
            HashSet<Integer> set = new HashSet<>(stopList);
            stopList.clear();
            stopList.addAll(set);
            Collections.sort(stopList, Comparator.reverseOrder());

        } else{
            if (direction.equals("Up")) {
                nextUpList.add(pickupFloor);
                nextUpList.add(destinationFloor);
                Collections.sort(nextUpList);
                HashSet<Integer> set = new HashSet<>(nextUpList);
                nextUpList.clear();
                nextUpList.addAll(set);
            }
            else if (direction.equals("Down")) {
                nextDownList.add(pickupFloor);
                nextDownList.add(destinationFloor);
                HashSet<Integer> set = new HashSet<>(nextDownList);
                nextDownList.clear();
                nextDownList.addAll(set);
                Collections.sort(nextDownList, Comparator.reverseOrder());
            } else {
                System.out.println("Unable to add request to queue");
            }
        }
        System.out.println("Request for elevator added to elevator queue");
        //System.out.println(stopList + "is size: " + stopList.size());
    }
    public synchronized elevatorState getState() {
        return currentState;
    }
    public synchronized void setState(elevatorState state){currentState = state;}
    public synchronized List<Integer> getStopList(){
        return stopList;
    }
    public synchronized List<Integer> getNextUpList(){return nextUpList;}
    public synchronized List<Integer> getNextDownList(){return nextDownList;}
    public synchronized int getCurrentFloor(){return currentFloor;}
    public synchronized void setCurrentFloor(int floor){currentFloor = floor;}
    public String getDirection(){
        return direction;
    }

    public synchronized String getInfo(){
        int numberOfStops = stopList.size() + nextUpList.size() + nextDownList.size();
        return elevatorId + "," + getState() + "," + getDirection() + "," + getCurrentFloor() + "," + numberOfStops;
    }
}
