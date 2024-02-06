public class SchedulerTest {

    public static void main(String[] args) {
        testSchedulerProcessing();
    }

    private static void testSchedulerProcessing() {
        Scheduler scheduler = new Scheduler();

        // Send request from floor to scheduler
        Inform floorRequest = new Inform(1, 3, "Up", 0, null );//Current time is not relevant in this iteration
        scheduler.send(floorRequest);

        // Send request from elevator to scheduler
        Inform elevatorRequest = new Inform(2);
        scheduler.send(elevatorRequest);

        // Simulate some delay to allow processing in the scheduler
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ignored) {
        }

        System.out.println("Scheduler tested");
    }
}
