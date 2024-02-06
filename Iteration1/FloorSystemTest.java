import java.util.ArrayList;

public class FloorSystemTest {

    public static void main(String[] args) {
        testFloorSystem();
    }


//Test if floor subsystem can be instantiated and the thread started
    private static void testFloorSystem() {
        Scheduler sharedBuffer = new Scheduler();
        ArrayList<Floor> floors = new ArrayList<>();

        try {
            // Create an instance of FloorSystem
            FloorSystem floorSystem = new FloorSystem(sharedBuffer, floors, 5);

            // Start the FloorSystem thread
            Thread floorSystemThread = new Thread(floorSystem, "FloorSystem");
            floorSystemThread.start();
            Thread.sleep(5000);

            // Stop thread and end test
            floorSystemThread.interrupt();
            floorSystemThread.join();

            //Return message
            System.out.println("FloorSystem is looking good.");
        } catch (InterruptedException e) {
            System.err.println("FloorSystem test interrupted: " );
}}}
