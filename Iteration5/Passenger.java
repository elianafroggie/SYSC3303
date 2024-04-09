public class Passenger {
    private int pickupFloor;
    private int destinationFloor;

    public Passenger(int pickupFloor, int destinationFloor) {
        this.pickupFloor = pickupFloor;
        this.destinationFloor = destinationFloor;
    }

    // Getters
    public int getPickupFloor() {
        return pickupFloor;
    }

    public int getDestinationFloor() {
        return destinationFloor;
    }

    @Override
    public String toString() {
        return "Pickup Floor: " + pickupFloor + ", Destination Floor: " + destinationFloor;
    }
}
