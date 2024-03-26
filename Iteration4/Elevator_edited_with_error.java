package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.*;

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
    private List<Integer> stopList;
    private List<Integer> nextDownList = new ArrayList<>();
    private List<Integer> nextUpList = new ArrayList<>();
    private String direction = "";

    // Defining constants for different error types
    // Represents a no error condition
    private final String NO_ERROR = "0";

    // Represents a temporary or transient error condition
    private final String TRANSIENT_ERROR = "1";

    // Represents a permanent or critical error condition
    private final String PERMANENT_ERROR = "2";



    public Elevator(int elevatorId, ElevatorSubsystem elevatorSubsystem) {
        this.elevatorId = elevatorId;
        this.elevatorSubsystem = elevatorSubsystem;
        this.stopList = new ArrayList<>();
        this.nextUpList = new ArrayList<>();
        this.nextDownList = new ArrayList<>();
        currentState = elevatorState.WAITING;
        System.out.println("Elevator created: " + this.elevatorId);
    }

    @Override
    public void run() {
        // Simulate elevator operations
        while (true) {
            if (currentState == elevatorState.WAITING) {
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
                    direction = "";
                }
            }
            if (currentState == elevatorState.MOVING_UP) {
                System.out.println("Elevator " + this.elevatorId + " : Moving up");
                while (currentFloor < stopList.get(0)) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    currentFloor += 1;
                }
                currentState = elevatorState.DOORS_OPENING;
            }
            if (currentState == elevatorState.MOVING_DOWN) {
                System.out.println("Elevator " + this.elevatorId + " : Moving Down");
                while (currentFloor > stopList.get(0)) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    currentFloor -= 1;
                }
                currentState = elevatorState.DOORS_OPENING;
            }

        }

    }

    public synchronized void addStops(int pickupFloor, int destinationFloor, String direction){
        int index = 0;
        System.out.println("Floors requested: " + pickupFloor + destinationFloor);
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
        System.out.println(stopList);
    }

    public synchronized elevatorState getState() {
        return currentState;
    }

    public synchronized List<Integer> getStopList(){
        return stopList;
    }
    public synchronized List<Integer>getNextUpList(){
        System.out.println("Next up list: " + nextUpList);
        return nextUpList;
    }

    public synchronized List<Integer>getNextDownList(){
        System.out.println("Next down list: " + nextDownList);
        return nextDownList;
    }
    public String getDirection(){
        return direction;
    }

    public void DoorStuckOpenHandling() {

    }

    public void DoorStuckClosedHandling() {

    }

    public void handleError(String error, boolean doorOpen, int destinationFloor) {
        int elevatorNum = 2;
        switch(error) {
            case NO_ERROR:
                if(destinationFloor > currentFloor) {
                    currentState = elevatorState.WAITING;
                    System.out.println("Elevator " + elevatorNum + " is moving up");
                }
                if(destinationFloor < currentFloor) {
                    currentState = elevatorState.WAITING;
                    System.out.println("Elevator is moving down");
                } else {
                    currentState = elevatorState.WAITING;
                    System.out.println("Idle State");
                }
                if(doorOpen) {
                    System.out.println("Door Open");
                } else {
                    System.out.println("Door Closed");
                }
                break;
        }
    }

}
