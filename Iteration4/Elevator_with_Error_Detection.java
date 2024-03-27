package org.example;
import java.util.*;

public class Elevator implements Runnable {
    // enum for the elevator states which includes the fault states
    public enum elevatorState {
        DOORS_OPENING,
        DOORS_CLOSING,
        MOVING_UP,
        MOVING_DOWN,
        WAITING,
        MALFUNCTION_FAULT_DOOR,
        ARRIVAL_SENSOR_FAILTURE_FAULT,
        STUCK_BETWEEN_FLOORS_FAULT
    }
    private final int elevatorId;
    private elevatorState currentState;
    int currentFloor;
    private final ElevatorSubsystem elevatorSubsystem;
    private List<Integer> stopList;
    private List<Integer> nextDownList;
    private List<Integer> nextUpList;
    private String direction = "Null";

    // timer variables for door fault detection
    private Timer elevatordoorTimer;
    private TimerTask elevatordoorTimerTask;
    private boolean doorFaultDetection = false;
    private final long elevatorDoorTimeout = 3000;

    // timer variables for floor fault detection
    private Timer floorTimer;
    private TimerTask floorTimerTask;
    private boolean floorFaultDetection = false;
    private final long elevatorFloorTimeout = 3000;

    private long startTime;
    private int targetFloor;

    private String state;

    public Elevator(int elevatorId, ElevatorSubsystem elevatorSubsystem) {
        this.elevatorId = elevatorId;
        this.elevatorSubsystem = elevatorSubsystem;
        this.stopList = new ArrayList<>();
        this.nextDownList = new ArrayList<>();
        this.nextUpList = new ArrayList<>();
        currentState = elevatorState.WAITING;
        currentFloor = 1;
        System.out.println("Elevator created: " + this.elevatorId);

        // initializing the timers involved
        timersInitialized();
    }

    public void settingState(String state) {this.state = state;}

    public void stop() {
        // printing the elevator has stopped
        System.out.println("The Elevator has Stopped");
    }
    public long getStartTime() {
        return startTime;
    }

    public void startElevatorMovement() {
        this.startTime = System.currentTimeMillis();
    }

    public int getTargetFloor() {
        return targetFloor;
    }

    // method for initializing the timers for the fault detection
    private void timersInitialized(){
        // Initialize the Door Timer
        // Creates an instance of timer class for the elevator door
        elevatordoorTimer = new Timer();
        // Creates an instance for the door task to be scheduled
        elevatordoorTimerTask = new TimerTask() {
            @Override
            public void run() {
                handlingDoorFault();
            }
        };

        // Initialize the Floor Timer
        // Creates an instance of timer class for the floors
        floorTimer = new Timer();
        // Creates an instance for the floors task to be scheduled
        floorTimerTask = new TimerTask() {
            @Override
            public void run() {
                handlingFloorFault();
            }
        };
    }

    // starts the timer for controlling the elevator door, where the timer task will be simulated after the delay duration
    private void startTimerDoor() {
        elevatordoorTimer.schedule(elevatordoorTimerTask, elevatorDoorTimeout);
    }

    // starts the timer for controlling the elevator floors, where the timer task will be simulated after the delay duration
    private void startTimerFloor() {
        floorTimer.schedule(floorTimerTask, elevatorFloorTimeout);
    }

    private void resettingDoorTimer() {
        // call to the cancel() method to stop the execution of TimerTask
        elevatordoorTimer.cancel();
        // call to the purge() method in order to clear the internal state and basically remove any cancelled tasks from the Timer's task queue list
        elevatordoorTimer.purge();
        // Creates an instance of timer class for the elevator door
        elevatordoorTimer = new Timer();
        // Creates an instance for the door task to be scheduled
        elevatordoorTimerTask = new TimerTask() {
            @Override
            public void run() {
                handlingDoorFault();
            }
        };
        // ensures that the timer is reset and will start counting down again after each door closure
        // this will allow the system to check if the door remains open for longer than expected
        elevatordoorTimer.schedule(elevatordoorTimerTask, elevatorDoorTimeout);
    }

    private void resettingFloorTimer() {
        // call to the cancel() method to stop the execution of TimerTask
        floorTimer.cancel();
        // call to the purge() method in order to clear the internal state and basically remove any cancelled tasks from the Timer's task queue list
        floorTimer.purge();
        // Creates an instance of timer class for the elevator door
        floorTimer = new Timer();
        // Creates an instance for the door task to be scheduled
        floorTimerTask = new TimerTask() {
            @Override
            public void run() {
                handlingFloorFault();
            }
        };
        // ensures that the timer is reset and will start counting down again after each movement in the floors
        // this will allow the system to check if the elevator can't reach a floor within the expected duration
        floorTimer.schedule(floorTimerTask, elevatorFloorTimeout);
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
                        if (currentFloor == stopList.get(0)) {
                            currentState = elevatorState.DOORS_OPENING;
                        }
                        if (currentFloor > stopList.get(0)) {
                            currentState = elevatorState.MOVING_DOWN;

                        }
                        if (currentFloor < stopList.get(0)) {
                            currentState = elevatorState.MOVING_UP;
                        }
                        direction = "Down";
                    }
                    if (stopList.get(0) < stopList.get(1)) {
                        if (currentFloor == stopList.get(0)) {
                            currentState = elevatorState.DOORS_OPENING;
                            //System.out.println("Doors opening on floor: " + currentFloor);
                        }
                        if (currentFloor > stopList.get(0)) {
                            currentState = elevatorState.MOVING_DOWN;
                        }
                        if (currentFloor < stopList.get(0)) {
                            currentState = elevatorState.MOVING_UP;
                        }
                        direction = "Up";
                    }
                }
            }

            // reset the timers if the current state for the elevators isn't waiting for an event
            if (currentState != elevatorState.WAITING) {
                resettingDoorTimer();
                resettingFloorTimer();
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
                stopList.remove(0);

                if(stopList.size()>1) {
                    if (stopList.get(0) < stopList.get(1) || currentFloor < stopList.get(0)) {
                        currentState = elevatorState.MOVING_UP;
                    }
                    if (stopList.get(0) > stopList.get(1)) {
                        currentState = elevatorState.MOVING_DOWN;
                    }
                }
                if(stopList.size() == 1){
                    //System.out.println("Size is 1");
                    if(stopList.get(0) > currentFloor){
                        currentState = elevatorState.MOVING_UP;
                    }
                    if(stopList.get(0) < currentFloor){
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
                while (currentFloor < stopList.get(0)) {
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
                while (currentFloor > stopList.get(0)) {
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
                (currentFloor <= pickupFloor || currentFloor >= stopList.get(0))) {
            stopList.add(pickupFloor);
            stopList.add(destinationFloor);
            Collections.sort(stopList);
            HashSet<Integer> set = new HashSet<>(stopList);
            stopList.clear();
            stopList.addAll(set);
        }
        else if (direction.equals("Down") && (this.direction.equals("Down") &&
                (currentFloor >= pickupFloor || currentFloor <= stopList.get(0)))){
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

        // when the stop requests are added to the elevator queue, then we can start and reset the timers
        resettingDoorTimer();
        resettingFloorTimer();
    }

    // method will handle the door timeout faults that we can detect
    private void handlingDoorFault() {
        doorFaultDetection = true;

        // situation where the elevator doors are stuck open, then we can attempt to close them
        if (currentState == elevatorState.DOORS_OPENING) {
            currentState = elevatorState.DOORS_CLOSING;

            System.out.println("Door Fault Detection has been Observed in the Elevator" + elevatorId + ". Trying to Close the Elevator Doors.....");
        } else {
            currentState = elevatorState.MALFUNCTION_FAULT_DOOR;

            System.out.println("Door Fault Detection has been Observed in the Elevator" + elevatorId + ". Elevators will Stay Stuck Open due to the Malfunction Issue.....");
        }
    }

    // method to handle the floor timeout faults that can be detected
    private void handlingFloorFault() {
        floorFaultDetection = true;

        currentState = elevatorState.STUCK_BETWEEN_FLOORS_FAULT;

        System.out.println("Floor Timeout Fault has been Detected in the Elevator" + elevatorId);
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

