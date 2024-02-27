import java.util.*;
public class FloorSystem extends Thread {
    private Scheduler sharedBuffer; //shared buffer between threads
    private ArrayList<Floor> floors; //an array of all the floors in the building

    public FloorSystem(Scheduler sharedBuffer, ArrayList<Floor> floors, int numFloors) {
        this.sharedBuffer = sharedBuffer;
        initializeFloors(numFloors);
    }

    private void initializeFloors(int numFloors) {
        ArrayList<Floor> floors = new ArrayList<>();
        for (int i = 0; i < numFloors; i++) {
            floors.add(new Floor(i));
        }
        this.floors = floors;
        //for (Floor floor : floors) {
        //  System.out.println("Floor number: " + floor.getFloorNumber());
    }
}
