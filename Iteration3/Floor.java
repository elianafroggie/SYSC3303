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
    DatagramSocket toSchedulerSocket;
    int time = 0;

    public Floor(){
        try{
            // Try to construct the sendReceive socket that will bind to the available port
            // and will attempt to send/receive requests to and from the scheduler
            toSchedulerSocket = new DatagramSocket();
        }catch(SocketException se){
            se.printStackTrace();
            System.exit(1);
        }
    }

    /*
    This method will parse the csv (which is separated by commas) and will send each request to
    the destination port which is the receiveSocket port in the scheduler class, in this case 23
     */
    public void readCSV(String csvFile, String delimiter, int port){
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(csvFile))){
            String request; // Creates request, which will be a string from the csv for each request
            int clockTime = 0; // Create integer for time (assumes csv time is ordered from smallest to largest)
            while((request = bufferedReader.readLine()) != null){
                String[] data = request.split(delimiter);
                int time  = Integer.parseInt(data[0]);
                int floor = Integer.parseInt(data[1]);
                String direction = data[2];
                int destination = Integer.parseInt(data[3]);

                while(time != clockTime){
                    try{
                        Thread.sleep(1000);
                        clockTime += 1;
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                DatagramPacket sendRequest = constructRequestPacket(floor, direction, destination);
                sendRequestPacket(sendRequest);

            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /*
    Constructs message out of data read from csv file and converts it to bytes to be sent to the scheduler
     */
    private DatagramPacket constructRequestPacket(int floor, String direction, int destination){
        String messageToScheduler = floor + "," + direction + "," + destination;
        byte[] data = messageToScheduler.getBytes();
        try{
            return new DatagramPacket(data, data.length, InetAddress.getLocalHost(), 23);
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }

    private void sendRequestPacket(DatagramPacket sendRequest){
        try{
            toSchedulerSocket.send(sendRequest);
            System.out.println("Packet Sent from Floor to Scheduler: " + new String(sendRequest.getData()));
        }catch(IOException e){
            e.printStackTrace();
            System.exit(1);

        }
    }
}
