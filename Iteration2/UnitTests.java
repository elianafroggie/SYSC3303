import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalTime;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class UnitTests {
    Scheduler scheduler;
    FloorSystem floorSystem;
    Elevator elevator;
    Inform inform;
    Thread floorThread;
    Thread elevatorThread;

    // Before each test, instantiate a new scheduler, floorSystem, elevator, floorThread, elevatorThread and an initial inform condition
    // this is very similar to Main.java.
    @BeforeEach
    public void setup(){
        scheduler = new Scheduler();
        floorSystem = new FloorSystem(scheduler, new ArrayList<>(), 5);
        elevator = new Elevator(scheduler);
        floorThread = new Thread(floorSystem, "FloorSystem");
        elevatorThread = new Thread(elevator, "Elevator");
        inform = new Inform(0,5, "Down", 0, LocalTime.now());
    }

    // Begin by testing construction
    @Test
    void testConstruction(){
        // Test inform construction
        assertEquals(0,inform.getButton(), "Inform Floor Constructor Test Failed");
        assertEquals(5,inform.getCurrentFloor(), "Inform Floor Constructor Test Failed");
        assertEquals("Down",inform.getTravelDirection(), "Inform Floor Constructor Test Failed");
        assertEquals(0,inform.getMessageSender(), "Inform Floor Constructor Test Failed");
        assertNotNull(inform.getCurrentTime(), "Inform Floor Constructor Test Failed");

        //Test elevator construction
        assertEquals(0, elevator.getCurrentFloor(), "Elevator not initialized successfully");

        //Test scheduler construction
        assertNotNull(scheduler, "Scheduler can't be null");
        assertNotNull(scheduler.getElevatorQueue(), "ElevatorQueue must be initialized");
        assertNotNull(scheduler.getFloorRequests(), "FloorRequests must be initialized");
        assertNotNull(scheduler.getElevatorRequests(), "ElevatorRequests must be initialized");
    }

    // Test to make sure current floor is updated
    @Test
    void testSetFloor(){
        elevator.updateCurrentFloor(3);
        assertEquals(3, elevator.getCurrentFloor(), "Elevator not updated successfully");
    }

    // Test scheduler
    @Test
    void testDownButton(){
        CountDownLatch latch = new CountDownLatch(1);
        elevatorThread.start();
        scheduler.send(inform);
        assertEquals(inform, scheduler.receive(0));
        try{
            latch.await();
        } catch (InterruptedException e ){
            Thread.currentThread().interrupt(); // Preserve interrupt status
            fail("Test interrupted while waiting for latch");
        }
        assertEquals(5, elevator.getCurrentFloor(), "Elevator didn't go to floor 5");
    }

    @Test
    void testUpButton(){
        CountDownLatch latch = new CountDownLatch(1);
        inform = new Inform(0,2, "Up", 0, LocalTime.now());
        elevatorThread.start();
        scheduler.send(inform);
        assertEquals(inform, scheduler.receive(0));
        try{
            latch.await();
        } catch (InterruptedException e ){
            Thread.currentThread().interrupt(); // Preserve interrupt status
            fail("Test interrupted while waiting for latch");
        }
        assertEquals(2, elevator.getCurrentFloor(), "Elevator didn't go to floor 2");
    }

    @Test
    void testFloorQueuesRequested(){
        CountDownLatch latch = new CountDownLatch(1);
        inform = new Inform(0,2, "Down", 0, LocalTime.now());
        elevatorThread.start();
        scheduler.send(inform);
        assertEquals(inform, scheduler.receive(0), "Message not received by scheduler");
        System.out.println(elevator.getCurrentFloorQueue());
        try{
            latch.await();
        } catch (InterruptedException e ){
            Thread.currentThread().interrupt(); // Preserve interrupt status
            fail("Test interrupted while waiting for latch");
        }
        // Check if queue now has an element in it
        assertEquals(0, elevator.getCurrentFloor(), "Elevator didn't receive correct message");
    }

    @Test
    void testElevatorQueuesRequested(){
        CountDownLatch latch = new CountDownLatch(1);
        inform = new Inform(4,1, "Up", 0, LocalTime.now());
        elevatorThread.start();
        scheduler.send(inform);
        assertEquals(inform, scheduler.receive(1), "Message not received by scheduler");
        //System.out.println(elevator.getCurrentElevatorQueue());
        try{
            latch.await();
        } catch (InterruptedException e ){
            Thread.currentThread().interrupt(); // Preserve interrupt status
            fail("Test interrupted while waiting for latch");
        }
        // Check if queue now has an element in it
        assertEquals(4, elevator.getCurrentFloor(), "Elevator didn't receive correct message");
    }
}
