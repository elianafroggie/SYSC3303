/**
 * @author Ben Mostafa | BeanJ4m
 */

import java.util.*;
public class FloorSystem implements Runnable {
    private Scheduler sharedBuffer; //shared buffer between threads
    private ArrayList<Floor> floors; //an array of all the floors in the building

    public FloorSystem(Scheduler sharedBuf, ArrayList<Floor>  floors, int numFloors) {
        this.sharedBuffer = sharedBuf;
        initializeFloors(floors, numFloors);
    }

    private void initializeFloors(ArrayList<Floor> floors, int numFloors) {
        for (int i = 0; i < numFloors; i++) {
            this.floors.add(new Floor(i));


        }
    }

    @Override
    public void run() {

    }
}