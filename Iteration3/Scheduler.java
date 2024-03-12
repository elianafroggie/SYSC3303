import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Scheduler {
    DatagramSocket floorSocket, elevatorSendSocket, elevatorReceiveSocket;
    int numElevators = 2; // Hard coding number of elevators
    String floorDataString, elevatorDataString;

    public Scheduler() {
        try {
            floorSocket = new DatagramSocket(23);
            elevatorSendSocket = new DatagramSocket();
            elevatorReceiveSocket = new DatagramSocket();
            floorDataString = "";
        } catch (SocketException se) {
            se.printStackTrace();
        }
    }

    public void schedulerRequests() {
        // Construct buffers for holding Datagram from clientFloor and from serverElevator
        byte[] floorData = new byte[13];
        byte[] elevatorData = new byte[100];

        while (true) {
            // This while loop acts as a scheduler

            // Receive one floor request
            try {
                DatagramPacket floorPacket = new DatagramPacket(floorData, floorData.length);
                floorSocket.receive(floorPacket);
                floorDataString = new String(floorPacket.getData(), 0, floorPacket.getLength());
                System.out.println("Packet Received by Scheduler from Floor: " + floorDataString);

                DatagramPacket fromElevatorPacket = new DatagramPacket(elevatorData, elevatorData.length);
                elevatorReceiveSocket.receive(fromElevatorPacket);
                elevatorDataString = new String(fromElevatorPacket.getData(), 0, fromElevatorPacket.getLength());
                System.out.println("Packet Received by Scheduler from Elevator Subsystem: " + fromElevatorPacket);

            } catch (IOException e) {
                throw new RuntimeException(e);
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

/*
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
    }*/
    }
}
