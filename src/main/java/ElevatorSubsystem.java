import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.List;

public class ElevatorSubsystem {
    private final Elevator[] elevators;
    DatagramSocket sendSocket, receiveSocket;
    DatagramPacket sendPacket, receivePacket;

    public ElevatorSubsystem(int numElevators) {
        elevators = new Elevator[numElevators];
        for (int i = 0; i < numElevators; i++) {
            elevators[i] = new Elevator(i, this);
            Thread elevatorThread = new Thread(elevators[i]);
            elevatorThread.start();
        }
        try {
            // Construct DatagramSockets for sending/receiving. The server will receive packets from port 69.
            sendSocket = new DatagramSocket();
            receiveSocket = new DatagramSocket(69);
        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        }
    }

    public void sendReceiveRequests() {
        // Initialize the elevators
        byte data[] = new byte[50];
        while (true) {
            // Wait for packet to be sent from scheduler requesting current elevator state
            receivePacket = new DatagramPacket(data, data.length);
            try {
                receiveSocket.receive(receivePacket);
            } catch (IOException e) {
                System.out.print("IO Exception: likely:");
                System.out.println("Timed Out.\n" + e);
                e.printStackTrace();
                System.exit(1);
            }

            // Send current elevator state
            if (data[0] == 0 && data[1] == 0) {
                byte[] info = getInfo().getBytes();
                sendPacket = new DatagramPacket(info, info.length, receivePacket.getAddress(), 25);
                try {
                    sendSocket.send(sendPacket);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            } else {
                int length = receivePacket.getLength();
                // Convert the received data byte array to a string
                String receivedData = new String(data, 0, length);

                // Split the received string by comma to get individual components
                String[] parts = receivedData.split(",");

                // Extract the elevator ID, pickup floor, destination floor, and direction
                int elevatorId = Integer.parseInt(parts[3].trim());
                int pickupFloor = Integer.parseInt(parts[0].trim());
                int destinationFloor = Integer.parseInt(parts[1].trim());
                String direction = parts[2].trim();
                addStops(pickupFloor, destinationFloor, direction, elevatorId);
            }

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

    public String getInfo(){
        String packet = "";
        for(int i = 0; i < elevators.length; i++){
            String string = elevators[i].getInfo();
            packet = packet + string + ",";
        }
        return packet;
    }

    public void run(){}

}