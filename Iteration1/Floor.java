/**
 * @author Ben Mostafa | BeanJ4m
 */

public class Floor {
    private int floorNumber; //the floor number
    private boolean upButton; //true if the up button is prssed otherwise false
    private boolean downButton; //true if the down button is pressed otherwise false
    private boolean upLamp; //true if the up lamp is on
    private boolean downlamp; //true if the down lamp is on

    public void Floor(int floorNumber) {
        this.floorNumber = floorNumber;
        this.downButton = false;
        this.downlamp = false;
        this.upButton = false;
        this.upLamp = false;
    }

    public int getFloorNumber() {
        return this.floorNumber;
    }

    public boolean isUpButton() {
        return upButton;
    }

    public boolean isDownButton() {
        return downButton;
    }

    public boolean isUpLamp() {
        return upLamp;
    }

    public boolean isDownlamp() {
        return downlamp;
    }

    public void pressDownButton() {
        this.downButton = true;
        this.downlamp = true;
    }

    public void pressUpButton() {
        this.upButton = true;
        this.upButton = true;
    }
}
