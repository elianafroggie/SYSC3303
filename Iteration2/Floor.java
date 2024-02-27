import java.time.LocalTime;

public class Floor {
    private int floorNumber; //the floor number
    private boolean upButton; //true if the up button is pressed otherwise false
    private boolean downButton; //true if the down button is pressed otherwise false
    private boolean upLamp; //true if the up Lamp is on
    private boolean downLamp; //true if the down Lamp is on

    public Floor(int floorNumber) {
        this.floorNumber = floorNumber;
        this.downButton = false;
        this.downLamp = false;
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

    public boolean isDownLamp() {
        return downLamp;
    }

    public void pressDownButton() {
        this.downButton = true;
        this.downLamp = true;
    }

    public void upWait() {
        this.upButton = false;
    }
    public void downWait() {
        this.downButton = false;
    }

    public void pressUpButton() {
        this.upButton = true;
        this.upLamp = true;
    }
}