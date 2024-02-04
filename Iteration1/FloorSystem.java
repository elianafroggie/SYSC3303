/**
 * @author Ben Mostafa | BeanJ4m
 */

import java.util.*;
public class FloorSystem implements Runnable {
    private Scheduler sharedBuffer; //shared buffer between threads
    private ArrayList<Floor> floors; //an array of all the floors in the building

    public FloorSystem(Scheduler sharedBuf, int floors) {
        this.sharedBuffer = sharedBuf;
        initializeFloors(floors);
    }

    private void initializeFloors(int floors) {
        for (int i = 0; i < floors; i++) {

            this.floors.add(Floor(i));

        }
    }
}