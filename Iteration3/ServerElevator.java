import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

class ServerElevator {
    DatagramSocket sendSocket, receiveSocket;
    private elevatorState currentState;
    private int currentFloor;
    private String direction;
    private List<Integer> stopList;
    private List<Integer> nextDownList;
    private List<Integer> nextUpList;
    private int port;


    private enum elevatorState {
        DOORS_OPENING,
        DOORS_CLOSING,
        MOVING_UP,
        MOVING_DOWN,
        WAITING;
    }

    public ServerElevator(int port) {
        try {
            // Construct DatagramSockets for sending/receiving. The server will receive packets from port 1027 + port.
            sendSocket = new DatagramSocket();
            receiveSocket = new DatagramSocket(1027+port);
            this.port = port;
        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        }
        currentState = elevatorState.WAITING;
        stopList = new ArrayList<>();
        nextUpList = new ArrayList<>();
        nextDownList = new ArrayList<>();
        currentFloor = 1;
    }

    public synchronized void checkForMessages() {
        // Receive a packet from the scheduler
        byte[] receiveData = new byte[12];
        DatagramPacket fromSchedulerPacket = new DatagramPacket(receiveData, receiveData.length);
        try {
            receiveSocket.receive(fromSchedulerPacket);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Convert the byte array to a string
        String message = new String(fromSchedulerPacket.getData());

        // Split the string by comma to get two parts (assuming format is "integer1,integer2")
        String[] parts = message.split(",");

        // Parse the integers from the string parts
        int pickupFloor = Integer.parseInt(parts[0].trim());
        int destinationFloor = Integer.parseInt(parts[1].trim());
        String direction = parts[2].trim();
        int index = 0;

        // This is the logic for adding floors to the floor list of stops for the elevator
        // Currently I have decided that starvation is a myth from big tech
        if (direction.equals("Up") && (currentState == elevatorState.MOVING_UP || currentState ==
                elevatorState.WAITING) && currentFloor <= pickupFloor) {
            while (index < stopList.size() && stopList.get(index) < pickupFloor) {
                index++;
            }
            stopList.add(index, pickupFloor);

            while (index < stopList.size() && stopList.get(index) < destinationFloor) {
                index++;
            }
            stopList.add(index, destinationFloor);
        }
        if (direction.equals("Down") && (currentState == elevatorState.MOVING_DOWN || currentState ==
                elevatorState.WAITING) && currentFloor >= pickupFloor) {
            while (index < stopList.size() && stopList.get(index) > pickupFloor) {
                index++;
            }
            stopList.add(index, pickupFloor);

            while (index < stopList.size() && stopList.get(index) < destinationFloor) {
                index++;
            }
            stopList.add(index, destinationFloor);
        } else {
            if (direction.equals("Up")) {
                while (index < nextUpList.size() && nextUpList.get(index) < pickupFloor) {
                    index++;
                }
                nextUpList.add(index, pickupFloor);
                while (index < nextUpList.size() && nextUpList.get(index) < destinationFloor) {
                    index++;
                }
                nextUpList.add(index, destinationFloor);
            }
            if (direction.equals("Down")) {
                while (index < nextDownList.size() && nextDownList.get(index) > pickupFloor) {
                    index++;
                }
                nextDownList.add(index, pickupFloor);

                while (index < nextDownList.size() && nextDownList.get(index) < destinationFloor) {
                    index++;
                }
                nextDownList.add(index, destinationFloor);
            } else {
                System.out.println("Unable to add request to queue");
            }
        }
        System.out.println("Request for elevator added to elevator queue");
        notifyAll();
    }

    public synchronized void elevatorStateMachine() {
        // Logic for waiting state
        if (currentState == elevatorState.WAITING) {
            if (stopList != null) {
                if (stopList.get(0) > stopList.get(1)) {
                    if (currentFloor == stopList.get(0)) {
                        currentState = elevatorState.DOORS_OPENING;
                        System.out.println("Doors opening on floor: " + currentFloor);
                    }
                    if (currentFloor > stopList.get(0)) {
                        currentState = elevatorState.MOVING_DOWN;
                    }
                    if (currentFloor < stopList.get(0)) {
                        currentState = elevatorState.MOVING_UP;
                    }
                    direction = "Down";
                }
                if (stopList.get(0) < stopList.get(1)) {
                    if (currentFloor == stopList.get(0)) {
                        currentState = elevatorState.DOORS_OPENING;
                        System.out.println("Doors opening on floor: " + currentFloor);
                    }
                    if (currentFloor < stopList.get(0)) {
                        currentState = elevatorState.MOVING_DOWN;
                    }
                    if (currentFloor > stopList.get(0)) {
                        currentState = elevatorState.MOVING_UP;
                    }
                    direction = "Up";
                }
            } else {
                if (nextUpList.isEmpty() && nextDownList.isEmpty()) {
                    currentState = elevatorState.WAITING;
                    direction = "";
                }
                if (nextUpList.size() >= nextDownList.size()) {
                    stopList = new ArrayList<>(nextUpList);
                    nextUpList.clear();
                    direction = "Up";
                } else {
                    stopList = new ArrayList<>(nextDownList);
                    nextDownList.clear();
                    direction = "Down";
                }
            }
            sendStateToScheduler(direction, currentFloor);
        }
        if (currentState == elevatorState.DOORS_OPENING) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Doors open on floor: " + currentFloor);
            sendStateToScheduler(direction, currentFloor);
            currentState = elevatorState.DOORS_CLOSING;
        }
        if (currentState == elevatorState.DOORS_CLOSING) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Doors closed on floor: " + currentFloor);
            sendStateToScheduler(direction, currentFloor);
            if (stopList.get(0) < stopList.get(1)) {
                currentState = elevatorState.MOVING_UP;
                direction = "Up";
            }
            if (stopList.get(0) > stopList.get(1)) {
                currentState = elevatorState.MOVING_DOWN;
                direction = "Down";
            }
        }
        if (currentState == elevatorState.MOVING_UP) {
            System.out.println("Moving up");
            while (currentFloor < stopList.getFirst()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                currentFloor += 1;
            }
            stopList.removeFirst();
            currentState = elevatorState.DOORS_OPENING;
            sendStateToScheduler(direction, currentFloor);
        }
        if (currentState == elevatorState.MOVING_DOWN) {
            System.out.println("Moving Down");
            while (currentFloor > stopList.getFirst()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                currentFloor -= 1;
            }
            stopList.removeFirst();
            currentState = elevatorState.DOORS_OPENING;
            sendStateToScheduler(direction, currentFloor);
        }

    }

    private void sendStateToScheduler(String direction, int currentFloor) {
        String messageToScheduler = currentFloor + "," + direction + "," + port;
        byte[] data = messageToScheduler.getBytes();
        InetAddress address;
        try {
            address = InetAddress.getLocalHost();
            DatagramPacket packetToScheduler = new DatagramPacket(data, data.length, address, (3000 + port));
            sendSocket.send(packetToScheduler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startThreads() {
        Thread messageThread, stateMachineThread; // Create Threads

        // Create Runnables for the producer and consumers and pass them the shared box
        Runnable checkForMessageRunnable = new CheckForMessages(this);
        Runnable elevatorStateMachineRunnable = new ElevatorStateMachine(this);

        // Create threads for the runnables instantiated above
        messageThread = new Thread(checkForMessageRunnable);
        stateMachineThread = new Thread(elevatorStateMachineRunnable);

        // Start the threads for concurrent execution
        messageThread.start();
        stateMachineThread.start();
    }
}

    class CheckForMessages implements Runnable {
        private ServerElevator elevator;

        public CheckForMessages(ServerElevator elevator) {
            this.elevator = elevator;
        }

        public void run() {
            while (true) {
                elevator.checkForMessages();
            }
        }
    }


class ElevatorStateMachine implements Runnable{
    private ServerElevator elevator;
    public ElevatorStateMachine(ServerElevator elevator){
        this.elevator = elevator;
    }
    public void run(){
        while(true){
            elevator.elevatorStateMachine();
        }
    }
}
