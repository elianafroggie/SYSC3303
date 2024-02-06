public class ElevatorTest {

    public static void main(String[] args) {
        testElevatorInitialization();
        testUpdateCurrentFloor();
    }

  // Check to make sure elevator can be initialized
    private static void testElevatorInitialization() {
        Scheduler scheduler = new Scheduler();
        Elevator elevator = new Elevator(scheduler);

        System.out.println("Testing elevator initialization");

        if (elevator.getCurrentFloor() == 1 && elevator.getTargetFloor() == 1) {
            System.out.println("Elevator initialized successfully");
        } else {
            System.out.println("Elevator not initialized successfully");
        }
    }

  // Check to make sure current floor can be successfully updated
    private static void testUpdateCurrentFloor() {
        Scheduler scheduler = new Scheduler();
        Elevator elevator = new Elevator(scheduler);

        System.out.println("Testing update current floor");

        elevator.updateCurrentFloor(5);
        
        if (elevator.getCurrentFloor() == 5) {
            System.out.println("Current floor updated successfully");
        } else {
            System.out.println("Current floor failed to update successfully");
        }
    }
}
