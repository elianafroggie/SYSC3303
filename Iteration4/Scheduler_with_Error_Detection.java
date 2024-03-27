package org.example;
import java.io.IOException;
import java.net.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;

public class Scheduler {

    DatagramSocket receiveSocket, sendSocket;
    DatagramPacket floorPacket;
    String floorDataString, elevatorDataString;
    private int numElevators;

    public Scheduler() {
        try {
            // Construct a datagram socket and bind it to port 25 and use it to forward packets
            // between client and server
            sendSocket = new DatagramSocket();
            receiveSocket = new DatagramSocket(25);
        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        }
    }

    public void scheduleRequests(){
        byte floorData[] = new byte[20];
        byte elevatorData[] = new byte[50];
        while(true){
            // Receive packet from floor with an elevator request
            try {
                floorPacket = new DatagramPacket(floorData, floorData.length);
                receiveSocket.receive(floorPacket);
                floorDataString = new String(floorPacket.getData(), 0, floorPacket.getLength());
            }catch (IOException e){
                throw new RuntimeException(e);
            }
            System.out.println("Floor data received " +  floorDataString);

            // Send request for current elevator states to elevatorSubsystem
            byte request[] = new byte[]{0,0,0,0};
            try{
                DatagramPacket requestPacket = new DatagramPacket(request, 4, InetAddress.getLocalHost(), 69);
                sendSocket.send(requestPacket);
            }catch (IOException e){
                throw new RuntimeException(e);
            }

            // Receive elevator states from elevatorSubsystem
            try{
                DatagramPacket elevatorPacket = new DatagramPacket(elevatorData, elevatorData.length);
                receiveSocket.receive(elevatorPacket);
                elevatorDataString = new String(elevatorPacket.getData(), 0, elevatorPacket.getLength());
            }catch (IOException e){
                throw new RuntimeException(e);
            }

            // Send elevatorSubsystem the elevator that will make the stop and the information
            try{
                byte stopRequest[] = scheduleStop(floorDataString, elevatorDataString).getBytes();
                DatagramPacket stopRequestPacket = new DatagramPacket(stopRequest, stopRequest.length, InetAddress.getLocalHost(), 69);
                sendSocket.send(stopRequestPacket);
            }catch (IOException e){
                throw new RuntimeException(e);
            }

            // Notify floor subsystem that it can continue sending requests
            try{
                byte notify[] = new byte[]{0,0,0,0};
                DatagramPacket notifyPacket = new DatagramPacket(notify, 4, floorPacket.getAddress(), floorPacket.getPort());
                sendSocket.send(notifyPacket);
            }catch (IOException e){
                throw new RuntimeException(e);
            }
        }



    }
    public String scheduleStop(String floorDataString, String elevatorDataString){
        String[] floorParts = floorDataString.split(",");

        int pickupRequest = Integer.parseInt(floorParts[0].trim());
        //System.out.println("Pickup request: " + pickupRequest);
        String directionRequest = floorParts[1].trim();
        int destinationRequest = Integer.parseInt(floorParts[2].trim());

        String[] elevatorSubsystemParts = elevatorDataString.split(",");
        ArrayList<Integer> numStopList = new ArrayList<>();

        // Big ol scheduler, each elevator string contains 5 elements (elevatorString size will be multiple of 5)
        for(int i = 0; i < elevatorSubsystemParts.length; i += 5){
            // For each of the elevators, parse and see if it looks like a good spot to add a stop
            //System.out.println(elevatorDataString);
            int elevatorId = Integer.parseInt(elevatorSubsystemParts[i].trim());
            String state = elevatorSubsystemParts[1+i].trim();
            String direction = elevatorSubsystemParts[2+i].trim();
            int currentFloor = Integer.parseInt(elevatorSubsystemParts[3+i].trim());
            int numStops = Integer.parseInt(elevatorSubsystemParts[4+i].trim());
            numStopList.add(numStops);

            if(state.equals("WAITING") && numStops == 0){
                return pickupRequest + "," + destinationRequest + "," + directionRequest + "," + elevatorId;
            }else if(directionRequest.equals(direction) && direction.equals("Down") && currentFloor > pickupRequest){
                return pickupRequest + "," + destinationRequest + "," + directionRequest + "," + elevatorId;
            }else if((directionRequest.equals(direction) && direction.equals("Up") && currentFloor < pickupRequest)){
                return pickupRequest + "," + destinationRequest + "," + directionRequest + "," + elevatorId;
            }
        }
        // If no good spots, add the stop to the elevator with the fewest stops to deal with
        int smallestWait = numStopList.get(0);
        int smallestWaitId = 0;
        for(int i = 0; i < numStopList.size(); i++){
            if(numStopList.get(i) <= smallestWait){
                smallestWait = numStopList.get(i);
                smallestWaitId = i;
            }
        }
        return pickupRequest + "," + destinationRequest + "," + directionRequest + "," + smallestWaitId;
    }

    // method where the logic is detected if an elevator takes a long time to reach a floor
    // if we do detect a fault, then we will forcefully shut down the particular elevator
    // the logic is implemented through timers and monitoring the elevator states
    public void detectHandleFaults(String elevatorDataString) {

        // parse the elevator data string in order to extract the needed information such as "elevatorID, state, currentFloor, numStops"
        String[] elevatorSubsystemParts = elevatorDataString.split(",");

        int elevatorId = Integer.parseInt(elevatorSubsystemParts[0].trim());
        String state = elevatorSubsystemParts[1].trim();
        int currentFloor = Integer.parseInt(elevatorSubsystemParts[3].trim());
        int numStops = Integer.parseInt(elevatorSubsystemParts[4].trim());

        // check condition to see if the elevator is stuck or not functioning as we want it to according to its state
        if (state.equals("MOVING_DOWN") || state.equals("MOVING_UP")){

            long takenTime = System.currentTimeMillis() - elevator.getStartTime();

            int targetFloor = elevator.getTargetFloor();

            int distanceTarget = Math.abs(currentFloor - targetFloor);

            long expectedTime = calculateExpectedTime(currentFloor, targetFloor);
            long threshold = 15000;

            if (takenTime > expectedTime + threshold) {
                // If the elevator is taking too long, we can shut it down
                powerOffElevator(elevatorId);
            }
        }
    }

    // method to calculate the expected time for the elevator to reach a floor level
    private long calculateExpectedTime(int currentFloor, int targetFloor) {

        // calculate the distance it takes between to reach the target floor from the current floor
        int distance = Math.abs(targetFloor - currentFloor);

        // assumes it takes 6 seconds to travel a floor
        long travelTimeBetweenFloors = 6000;

        // in ms
        long totalTravellingTime = distance * travelTimeBetweenFloors;

        // return the time the elevator will reach the target floor
        return totalTravellingTime + System.currentTimeMillis();
    }

    // Method to shut down the elevators
    private void powerOffElevator(int elevatorId) {
        ElevatorSubsystem elevatorSubsystem = new ElevatorSubsystem(numElevators);

        Elevator[] elevators = elevatorSubsystem.elevators;

        // check if the elevatorID doesn't exceed the length of the elevators array, and the elevatorID isn't negative
        if (elevatorId < elevators.length && elevatorId >= 0) {
            Elevator elevator = elevators[elevatorId];
            elevator.stop();
            elevator.settingState("This is a Fault!");
        }

    }
}


