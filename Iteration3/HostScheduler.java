import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class HostScheduler {
    DatagramSocket floorSocket;
    List<DatagramSocket> elevatorSockets;
    int numElevators = 2; // Hard coding number of elevators
    List<String> elevatorDataList;
    String floorDataString;

    public HostScheduler() {
        try {
            floorSocket = new DatagramSocket(23);
            elevatorSockets = new ArrayList<>();
            elevatorDataList = new ArrayList<>();
            floorDataString = "";
            try {
                for (int i = 0; i < numElevators; i++) {
                    int port = 3000 + i;
                    DatagramSocket elevatorSocket = new DatagramSocket(port); // Needs port number
                    elevatorSockets.add(elevatorSocket);
                }
            } catch (SocketException se) {
                se.printStackTrace();
            }
        } catch (SocketException se) {
            se.printStackTrace();
        }
    }

    public void schedulerRequests() {
        // Construct buffers for holding Datagram from clientFloor and from serverElevator
        byte[] floorData = new byte[13];
        byte[] elevatorData = new byte[7];

        while (true) {
            // This while loop acts as a scheduler

            // Receive one floor request
            try {
                DatagramPacket floorPacket = new DatagramPacket(floorData, floorData.length);
                floorSocket.receive(floorPacket);
                floorDataString = new String(floorPacket.getData(), 0, floorPacket.getLength());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Receive current elevator data (direction and current floor) from each elevator
            for (DatagramSocket elevatorSocket : elevatorSockets) {
                try {
                    DatagramPacket elevatorPacket = new DatagramPacket(elevatorData, elevatorData.length);
                    elevatorSocket.receive(elevatorPacket);
                    String elevatorDataString = new String(elevatorPacket.getData(), 0, elevatorPacket.getLength());
                    elevatorDataList.add(elevatorDataString);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            // Now that we have received one floor request and the current states of all elevators,
            // we can process the requests and schedule where to send our requests
            String[] floorInfo = floorDataString.split(",");
            int currentFloor = Integer.parseInt(floorInfo[0]);
            String direction = floorInfo[1];
            int destinationFloor = Integer.parseInt(floorInfo[2]);
            String messageToElevator = currentFloor + "," + destinationFloor + "," + direction;
            byte[] data = messageToElevator.getBytes();
            int messageSent = 0;

            for (String elevatorDataString : elevatorDataList) {
                String[] elevatorInfo = elevatorDataString.split(",");
                int elevatorCurrentFloor = Integer.parseInt(elevatorInfo[0]);
                String elevatorDirection = elevatorInfo[1];
                int port = Integer.parseInt(elevatorInfo[2]);
                if (direction.equals(elevatorDirection) && messageSent != 1) {
                    if ((direction.equals("Up") && elevatorCurrentFloor < currentFloor) || (direction.equals("Down") &&
                            elevatorCurrentFloor > currentFloor) || elevatorDirection.isEmpty()) {
                        try {
                            DatagramPacket toElevatorPacket = new DatagramPacket(data, data.length,
                                    InetAddress.getLocalHost(), 3000 + port);
                            DatagramSocket elevatorMessage = new DatagramSocket(toElevatorPacket.getPort());
                            elevatorMessage.send(toElevatorPacket);
                            System.out.println("Scheduler sent request to elevator to go " + direction + " from floor "
                                    + currentFloor + "to floor " + destinationFloor);
                        } catch (UnknownHostException e) {
                            throw new RuntimeException(e);
                        } catch (SocketException e) {
                            throw new RuntimeException(e);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        messageSent += 1;
                    }
                }
            }
            if(messageSent == 0){
                for (String elevatorDataString : elevatorDataList) {
                    String[] elevatorInfo = elevatorDataString.split(",");
                    int elevatorCurrentFloor = Integer.parseInt(elevatorInfo[0]);
                    String elevatorDirection = elevatorInfo[1];
                    int port = Integer.parseInt(elevatorInfo[2]);
                    try {
                        DatagramPacket toElevatorPacket = new DatagramPacket(data, data.length,
                                InetAddress.getLocalHost(), 3000 + port);
                        DatagramSocket elevatorMessage = new DatagramSocket(toElevatorPacket.getPort());
                        elevatorMessage.send(toElevatorPacket);
                        System.out.println("Scheduler sent request to elevator to go " + direction + " from floor "
                                + currentFloor + "to floor " + destinationFloor);
                    } catch (UnknownHostException e) {
                        throw new RuntimeException(e);
                    } catch (SocketException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    messageSent += 1;
                }
            }
        }
    }
}
