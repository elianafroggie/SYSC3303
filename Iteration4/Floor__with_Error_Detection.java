package org.example;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.*;

/*
This class will read a csv file that includes time someone begins waiting at a floor,
 the floor where that person is waiting, what direction they plan to go and where their
 destination is (floor they want to reach)
 */
public class Floor {
    DatagramSocket toFromSchedulerSocket;
    int time = 0;

    public Floor(){
        try{
            // Try to construct the sendReceive socket that will bind to the available port
            // and will attempt to send/receive requests to and from the scheduler
            toFromSchedulerSocket = new DatagramSocket();
        }catch(SocketException se){
            se.printStackTrace();
            System.exit(1);
        }
    }

    /*
    This method will parse the csv (which is separated by commas) and will send each request to
    the destination port which is the receiveSocket port in the scheduler class, in this case 25
     */
    public void readCSV(String csvFile, String delimiter){
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(csvFile))){
            String request; // Creates request, which will be a string from the csv for each request
            int clockTime = 0; // Create integer for time (assumes csv time is ordered from smallest to largest)
            while((request = bufferedReader.readLine()) != null){
                String[] data = request.split(delimiter);
                int time  = Integer.parseInt(data[0]);
                int floor = Integer.parseInt(data[1]);
                String direction = data[2];
                int destination = Integer.parseInt(data[3]);

                // Wait until it is time to send message to scheduler
                while(time != clockTime){
                    try{
                        Thread.sleep(1000);
                        clockTime += 1;
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                // Construct datagramPacket and send request to scheduler
                DatagramPacket sendRequest = constructRequestPacket(floor, direction, destination);
                sendRequestPacket(sendRequest);

                // Wait until scheduler schedules before sending over next request because idk how much a socket can take
                try{
                    byte[] responseData = new byte[4];
                    DatagramPacket responsePacket = new DatagramPacket(responseData, responseData.length);
                    toFromSchedulerSocket.receive(responsePacket);
                }catch (IOException e){
                    throw new RuntimeException(e);
                }

            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /*
    Constructs message out of data read from csv file and converts it to bytes to be sent to the scheduler
     */
    DatagramPacket constructRequestPacket(int floor, String direction, int destination){
        String messageToScheduler = floor + "," + direction + "," + destination;
        byte[] data = messageToScheduler.getBytes();
        try{
            return new DatagramPacket(data, data.length, InetAddress.getLocalHost(), 25);
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }

    /*
    Usage is obvious, sends packet to scheduler
     */
    void sendRequestPacket(DatagramPacket sendRequest){
        try{
            toFromSchedulerSocket.send(sendRequest);
            System.out.println("Packet Sent from Floor to Scheduler: " + new String(sendRequest.getData()));
        }catch(IOException e){
            e.printStackTrace();
            System.exit(1);

        }
    }

    // method to inject faults into the elevator system
    // the method will send a request to the scheduler telling it the type of fault and the affected floors
    public void faultInjection(String typeofFault, int floor) {
        // injected fault include door not functioning or arrival sensor failures
        String message = "The Fault of" + typeofFault + ", on the Floor" + floor;

        // Construct a DatagramPacket object, which will be used to send data over the network in a UDP communication type
        // Parameters involve:
        // (1) UDP will send data in form of byte arrays
        // (2) Specify the length of the data to sent in the packet
        // (3) IP Address of the Scheduler host where the packet will be send
        // (4) The port number on which the Scheduler is listening for the data on the incoming packets

        DatagramPacket faultIncomingPacket = new DatagramPacket(message.getBytes(), message.getBytes().length, toFromSchedulerSocket.getInetAddress(), toFromSchedulerSocket.getPort());

        try {
            toFromSchedulerSocket.send(faultIncomingPacket);

            System.out.println("The Fault Injection has a Message of: " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

