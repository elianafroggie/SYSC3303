import java.time.LocalTime;
import java.util.*;
public class FloorSystem extends Thread {
    private Scheduler sharedBuffer; //shared buffer between threads
    private ArrayList<Floor> floors; //an array of all the floors in the building

    public FloorSystem(Scheduler sharedBuffer, int numFloors) {
        this.sharedBuffer = sharedBuffer;
        initializeFloors(numFloors);
    }

    private void initializeFloors(int numFloors) {
        ArrayList<Floor> newFloors = new ArrayList<>();
        for (int i = 0; i < numFloors; i++) {
            newFloors.add(new Floor(i));
        }
        this.floors = newFloors;
    }

    public Floor getFloor(int floor) {
        return floors.get(floor - 1);
    }
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " is available.");
    }
}
