import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.net.*;

import static org.junit.jupiter.api.Assertions.*;

public class FloorTest {

    private Floor floor;
    private int PORT_NUMBER = 2025; // Port number to be used for the DatagramSocket

    @BeforeEach
    public void setUp() {
        floor = new Floor();
        // Set the DatagramSocket port to 4018
        try {
            floor.toFromSchedulerSocket = new DatagramSocket(PORT_NUMBER);
            PORT_NUMBER += 1;
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConstructor() {
        assertNotNull(floor);
        assertNotNull(floor.toFromSchedulerSocket);
    }

    @Test
    public void testReadCSV() {
        try {
            // Prepare a CSV string with test data
            String csvData = "1,2,UP,3\n" +
                    "4,5,DOWN,6\n" +
                    "7,8,UP,9";

            // Create a temporary CSV file with the test data
            File tempFile = File.createTempFile("test", ".csv");
            tempFile.deleteOnExit(); // Delete the temp file after the test finishes
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
            writer.write(csvData);
            writer.close();

            // Invoke the readCSV method with the temp file
            floor.readCSV(tempFile.getAbsolutePath(), ",");

            // You can add assertions here based on the expected behavior of the method
            // For example, you can verify that certain packets were sent or received
        } catch (IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testConstructRequestPacket() throws UnknownHostException {
        DatagramPacket packet = floor.constructRequestPacket(1, "UP", 5);

        assertNotNull(packet);
        assertEquals("1,UP,5", new String(packet.getData()));
        assertEquals(InetAddress.getLocalHost(), packet.getAddress());
        assertEquals(2025, packet.getPort());
    }

    @Test
    public void testSendRequestPacket() {
        // Redirect System.out for assertion on printed output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            // Create a dummy DatagramPacket
            byte[] data = "Test data".getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), PORT_NUMBER);

            // Call sendRequestPacket method
            floor.sendRequestPacket(packet);

            // Verify the output
            assertEquals("Packet Sent from Floor to Scheduler: Test data\n", outputStream.toString());
        } catch (IOException e) {
            fail("Exception occurred: " + e.getMessage());
        } finally {
            // Restore System.out
            System.setOut(originalOut);
        }
    }
}
