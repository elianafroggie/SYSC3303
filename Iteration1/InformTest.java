import java.time.LocalTime;

public class InformTest {

    public static void main(String[] args) {
        testInformFloorConstructor();
        testInformElevatorConstructor();
    }

    private static void testInformFloorConstructor() {
        System.out.println("Testing Inform Floor Constructor");

        // Create an Inform instance using the Floor constructor
        Inform inform = new Inform(2, 3, "Up", 0, LocalTime.now());

        // Check if the values are set correctly
        assertCondition(inform.getButton() == 2, "Inform Floor Constructor Test Failed");
        assertCondition(inform.getCurrentFloor() == 3, "Inform Floor Constructor Test Failed");
        assertCondition(inform.getTravelDirection().equals("Up"), "Inform Floor Constructor Test Failed");
        assertCondition(inform.getMessageSender() == 0, "Inform Floor Constructor Test Failed");
        assertCondition(inform.getCurrentTime() != null, "Inform Floor Constructor Test Failed");

        System.out.println("Inform Floor Constructor Test Passed");
    }

    private static void testInformElevatorConstructor() {
        System.out.println("Testing Inform Elevator Constructor...");

        // Create an Inform instance using the Elevator constructor
        Inform inform = new Inform(5);

        // Check if the values are set correctly
        assertCondition(inform.getButton() == 0, "Inform Elevator Constructor Test Failed");
        assertCondition(inform.getCurrentFloor() == 5, "Inform Elevator Constructor Test Failed");
        assertCondition(inform.getTravelDirection().equals(""), "Inform Elevator Constructor Test Failed");
        assertCondition(inform.getMessageSender() == 1, "Inform Elevator Constructor Test Failed");
        assertCondition(inform.getCurrentTime() == null, "Inform Elevator Constructor Test Failed");

        System.out.println("Inform Elevator Constructor Test Passed");
    }

    private static void assertCondition(boolean condition, String errorMessage) {
        if (!condition) {
            throw new AssertionError(errorMessage);
        }
    }
}
