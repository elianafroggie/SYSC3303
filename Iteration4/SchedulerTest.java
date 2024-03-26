import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.*;

import static org.junit.jupiter.api.Assertions.*;

public class SchedulerTest {

    private Scheduler scheduler = new Scheduler();
    private static final int PORT_NUMBER = 2025; // Port number to be used for the DatagramSocket


    @Test
    public void testConstructor() {
        assertNotNull(scheduler);
        assertNotNull(scheduler.receiveSocket);
        assertNotNull(scheduler.sendSocket);
    }

    @Test
    public void testScheduleStop() {
        String floorDataString = "1,Up,5";
        String elevatorDataString = "1,WAITING,Up,3,0,2,MOVING,Down,4,1";

        String result = scheduler.scheduleStop(floorDataString, elevatorDataString);

        assertEquals("1,5,Up,1", result);
    }

    @Test
    public void testScheduleRequests() {
        // Redirect System.out for assertion on printed output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            // Simulate sending and receiving data
            DatagramPacket floorPacket = new DatagramPacket(new byte[20], 20, InetAddress.getLocalHost(), PORT_NUMBER);
            scheduler.receiveSocket.send(floorPacket);
            scheduler.receiveSocket.send(floorPacket);

            scheduler.scheduleRequests();

        } catch (IOException e) {
            fail("Exception occurred: " + e.getMessage());
        } finally {
            scheduler.scheduleStop("1,Up,5", "1,WAITING,Up,3,0,2,MOVING,Down,4,1");
            // Restore System.out
            System.setOut(originalOut);
        }
    }
}
