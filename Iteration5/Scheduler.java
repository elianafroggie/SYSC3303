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

    public Scheduler() {
        try {
            // Construct a datagram socket and bind it to port 2025 and use it to forward packets
            // between client and server
            sendSocket = new DatagramSocket();
            receiveSocket = new DatagramSocket(2025);
        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        }
    }

    public void scheduleRequests(){
        byte floorData[] = new byte[20];
        byte elevatorData[] = new byte[100];
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
                DatagramPacket notifyPacket = new DatagramPacket(notify, notify.length, floorPacket.getAddress(), floorPacket.getPort());
                sendSocket.send(notifyPacket);
            }catch (IOException e){
                throw new RuntimeException(e);
            }
        }



    }
    public String scheduleStop(String floorDataString, String elevatorDataString){
        String[] floorParts = floorDataString.split(",");

        int pickupRequest = Integer.parseInt(floorParts[0].trim());
        String directionRequest = floorParts[1].trim();
        int destinationRequest = Integer.parseInt(floorParts[2].trim());

        String[] elevatorSubsystemParts = elevatorDataString.split(",");
        ArrayList<Integer> numStopList = new ArrayList<>();

        // Big ol scheduler, each elevator string contains 5 elements (elevatorString size will be multiple of 5)
        for(int i = 0; i < elevatorSubsystemParts.length - 1; i += 6){
            // For each of the elevators, parse and see if it looks like a good spot to add a stop
            //System.out.println(elevatorSubsystemParts.length);
            int elevatorId = Integer.parseInt(elevatorSubsystemParts[i].trim());
            String state = elevatorSubsystemParts[1+i].trim();
            String direction = elevatorSubsystemParts[2+i].trim();
            int currentFloor = Integer.parseInt(elevatorSubsystemParts[3+i].trim());
            int numStops = Integer.parseInt(elevatorSubsystemParts[4+i].trim());
            int capacity = Integer.parseInt(elevatorSubsystemParts[5+i].trim());
            if(capacity < 5) {
                numStopList.add(numStops);
            }
            else{
                numStopList.add(100);
            }

            if(state.equals("WAITING") && numStops == 0){
                return pickupRequest + "," + destinationRequest + "," + directionRequest + "," + elevatorId;
            }else if(directionRequest.equals(direction) && direction.equals("Down") && currentFloor > pickupRequest && capacity < 5){
                return pickupRequest + "," + destinationRequest + "," + directionRequest + "," + elevatorId;
            }else if((directionRequest.equals(direction) && direction.equals("Up") && currentFloor < pickupRequest && capacity < 5)){
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
        if(smallestWait == 100){
            byte elevatorData[] = new byte[100];
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
            return scheduleStop(floorDataString, elevatorDataString);
        }
        return pickupRequest + "," + destinationRequest + "," + directionRequest + "," + smallestWaitId;
    }

}