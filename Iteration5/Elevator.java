import java.util.*;

public class Elevator implements Runnable {
    public enum elevatorState {
        DOORS_OPENING,
        DOORS_CLOSING,
        MOVING_UP,
        MOVING_DOWN,
        WAITING
    }

    // Initialize all neccessary variables
    private final int elevatorId;
    private elevatorState currentState;
    int currentFloor;
    private final ElevatorSubsystem elevatorSubsystem;
    private List<Integer> stopList;
    private List<Integer> nextDownList;
    private List<Integer> nextUpList;
    private List<Passenger> passengers;
    private String direction = "Null";
    private int currentPassengerCount;
    private final int maxPassengerCapacity;


    // Create an instance of each variable above
    public Elevator(int elevatorId, ElevatorSubsystem elevatorSubsystem) {
        this.elevatorId = elevatorId;
        this.elevatorSubsystem = elevatorSubsystem;
        this.stopList = new ArrayList<>();
        this.nextDownList = new ArrayList<>();
        this.nextUpList = new ArrayList<>();
        this.passengers = new ArrayList<>();
        this.currentPassengerCount = 0;
        this.maxPassengerCapacity = 8;
        currentState = elevatorState.WAITING;
        currentFloor = 1;
        System.out.println("Elevator created: " + this.elevatorId);
    }

    @Override
    public void run() {
        // run() is a state machine that contains all neccessary elevator state.
        System.out.println("Running");
        while (true) {
            // Call getter methods
            stopList = getStopList();
            nextDownList = getNextDownList();
            nextUpList = getNextUpList();
            if (currentState == elevatorState.WAITING) {
                setState(elevatorState.WAITING); // Set state so it's easily accessed
                if(stopList.isEmpty()){
                    // If the stopList is empty, check nextUpList and nextDownList and if they are not empty, the 
                    // largest of the 2 becomes the new stopList.
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
                    // Check direction of the stop list
                    if (stopList.get(0) > stopList.get(1)) {
                        // Check if elevator is already on correct floor
                        if (currentFloor == stopList.getFirst()) {
                            currentState = elevatorState.DOORS_OPENING;
                            Iterator<Passenger> iterator = passengers.iterator();
                            while (iterator.hasNext()) {
                                Passenger pass = iterator.next();
                                if (pass.getDestinationFloor() == currentFloor) {
                                    passengerExits();
                                    iterator.remove(); // Use iterator's remove method to avoid ConcurrentModificationException
                                    try {
                                        Thread.sleep(500);
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            }
                        }
                        if (currentFloor > stopList.getFirst()) {
                            currentState = elevatorState.MOVING_DOWN;

                        }
                        if (currentFloor < stopList.getFirst()) {
                            currentState = elevatorState.MOVING_UP;
                        }
                        direction = "Down";
                    }
                    //Check direction again (this tile to check if going up)
                    if (stopList.get(0) < stopList.get(1)) {
                        if (currentFloor == stopList.getFirst()) {
                            currentState = elevatorState.DOORS_OPENING;
                            Iterator<Passenger> iterator = passengers.iterator();
                            while (iterator.hasNext()) {
                                Passenger pass = iterator.next();
                                if (pass.getDestinationFloor() == currentFloor) {
                                    passengerExits();
                                    iterator.remove(); // Use iterator's remove method to avoid ConcurrentModificationException
                                    try {
                                        Thread.sleep(500);
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            }
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
                    Thread.sleep(4000); // Doors take 4 seconds to open
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                Iterator<Passenger> iterator = passengers.iterator();
                while (iterator.hasNext()) {
                    Passenger pass = iterator.next();
                    if (pass.getDestinationFloor() == currentFloor) {
                        passengerExits();
                        iterator.remove(); // Use iterator's remove method to avoid ConcurrentModificationException
                        try {
                            Thread.sleep(500); //Each passenger takes 0.5 seconds to board
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                System.out.println("Elevator " + this.elevatorId + " : Doors open on floor: " + currentFloor);
                currentState = elevatorState.DOORS_CLOSING;
            }
            if (currentState == elevatorState.DOORS_CLOSING) {
                setState(elevatorState.DOORS_CLOSING);
                try {
                    Thread.sleep(3000); //Doors take 3 seconds to close
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Elevator " + this.elevatorId + " : Doors closed on floor: " + currentFloor);
                // After doors have opened and closed on a floor, that floor is removed from the stopList
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
                        Thread.sleep(2000); // Takes 2 seconds to travel between floors
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    currentFloor += 1;
                    setCurrentFloor(currentFloor);
                }
                currentState = elevatorState.DOORS_OPENING;
                Iterator<Passenger> iterator = passengers.iterator();
                while (iterator.hasNext()) {
                    Passenger pass = iterator.next();
                    if (pass.getDestinationFloor() == currentFloor) {
                        passengerExits();
                        iterator.remove(); // Use iterator's remove method to avoid ConcurrentModificationException
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
            if (currentState == elevatorState.MOVING_DOWN) {
                setState(elevatorState.MOVING_DOWN);
                System.out.println("Elevator " + this.elevatorId + " : Moving Down");
                while (currentFloor > stopList.getFirst()) {
                    try {
                        Thread.sleep(2000); // Takes 2 seconds to move between floors
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    currentFloor -= 1;
                    setCurrentFloor(currentFloor);
                }
                currentState = elevatorState.DOORS_OPENING;
                Iterator<Passenger> iterator = passengers.iterator();
                while (iterator.hasNext()) {
                    Passenger pass = iterator.next();
                    if (pass.getDestinationFloor() == currentFloor) {
                        passengerExits();
                        iterator.remove(); // Use iterator's remove method to avoid ConcurrentModificationException
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }

        }

    }

    public synchronized void addStops(int pickupFloor, int destinationFloor, String direction, int passIn){
        // This is the logic for adding floors to the floor list of stops for the elevator
        if((getCurrentPassengerCount() + passIn) > getMaxPassengerCapacity()) {
            System.out.println("Elevator " + this.elevatorId + " is at full capacity. Cannot add more stops.");
        }

        // Check if stop list is empty
        else if(currentState == elevatorState.WAITING && stopList.isEmpty() && nextDownList.isEmpty() && nextUpList.isEmpty()){
            stopList.add(pickupFloor);
            stopList.add(destinationFloor);
            int i = 0;
            while (passIn > i) {
                passengers.add(new Passenger(pickupFloor, destinationFloor));
                i++;
                currentPassengerCount++;
            }
        }

        // Check if elevator is going in the same direction of request and if the elevator can pickup the passenger
        else if (direction.equals("Up") && this.direction.equals("Up") &&
                (currentFloor <= pickupFloor || currentFloor >= stopList.getFirst())) {
            stopList.add(pickupFloor);
            stopList.add(destinationFloor);
            Collections.sort(stopList);
            HashSet<Integer> set = new HashSet<>(stopList);
            stopList.clear();
            stopList.addAll(set);
            int i = 0;
            while (passIn > i) {
                passengers.add(new Passenger(pickupFloor, destinationFloor));
                i++;
                currentPassengerCount++;
            }
        }
        else if (direction.equals("Down") && (this.direction.equals("Down") &&
                (currentFloor >= pickupFloor || currentFloor <= stopList.getFirst()))){
            stopList.add(pickupFloor);
            stopList.add(destinationFloor);
            HashSet<Integer> set = new HashSet<>(stopList);
            stopList.clear();
            stopList.addAll(set);
            Collections.sort(stopList, Comparator.reverseOrder());
            int i = 0;
            while (passIn > i) {
                passengers.add(new Passenger(pickupFloor, destinationFloor));
                i++;
                currentPassengerCount++;
            }

        // If unable to add the passenger to the current trip, the passenger's request goes to nextUpList or nextDownList
        // to be serviced when the oppertunity arises
        } else{
            if (direction.equals("Up")) {
                nextUpList.add(pickupFloor);
                nextUpList.add(destinationFloor);
                Collections.sort(nextUpList);
                HashSet<Integer> set = new HashSet<>(nextUpList);
                nextUpList.clear();
                nextUpList.addAll(set);
                int i = 0;
                while (passIn > i) {
                    passengers.add(new Passenger(pickupFloor, destinationFloor));
                    i++;
                    currentPassengerCount++;
                }
            }
            else if (direction.equals("Down")) {
                nextDownList.add(pickupFloor);
                nextDownList.add(destinationFloor);
                HashSet<Integer> set = new HashSet<>(nextDownList);
                nextDownList.clear();
                nextDownList.addAll(set);
                Collections.sort(nextDownList, Comparator.reverseOrder());
                int i = 0;
                while (passIn > i) {
                    passengers.add(new Passenger(pickupFloor, destinationFloor));
                    i++;
                    currentPassengerCount++;
                }
            } else {
                System.out.println("Unable to add request to queue");
            }
        }
        System.out.println("Request for elevator added to elevator queue. Current passenger count: " + getCurrentPassengerCount());
        //System.out.println(stopList + "is size: " + stopList.size());
    }

    // Getter functions
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
    public int getCurrentPassengerCount() {
        return currentPassengerCount;
    }
    public int getMaxPassengerCapacity() {
        return maxPassengerCapacity;
    }
    public void passengerExits() {
        // Decrease the passenger count when passengers exit, ensuring it doesn't go below zero
        currentPassengerCount --;
        if(currentPassengerCount < 0) {
            currentPassengerCount = 0;
        }
        System.out.println("1 passenger has exited Elevator: " + this.elevatorId + ". Current passenger count: " + getCurrentPassengerCount());
    }

    public synchronized String getInfo(){
        int numberOfStops = stopList.size() + nextUpList.size() + nextDownList.size();
        return elevatorId + "," + getState() + "," + getDirection() + "," + getCurrentFloor() + "," + numberOfStops + "," + getCurrentPassengerCount();
    }
}
