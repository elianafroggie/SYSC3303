##  SYSC 3303 - Project Iteration 4

## Source Files Included:

(1) Floor.java: This source file contains the implementation that can read requests from CSV file (data.csv) format and send them to the Scheduler for processing in the Elevator System via UDP messages.

(2) Scheduler.java: This source file manages incoming requests from the ClientFloor and coordinates the scheduling of the elevator operations based on the current state of the Elevator. It does this via UDP messages between the Floor and the ElevatorSubsystem.

(3) ElevatorSubsystem.java: This source file instantiates Elevator threads and manages communication between the scheduler and individual elevator threads. The scheduler sends messages to specific elevators using the addStop method.

(4) Elevator.java: These thread(s) are instantiated by ElevatorSubsystem for the number of elevators in the system. This source file contains the state machine for the elevator and prints off the state transitions. Additionally, this file allows for stops to be added to each elevator.

(4) Main.java: This source file serves as the starting point for the elevator system, in terms of initializing and starting the ElevatorSubsystem, Scheduler, and Floor subsystem components to control the functional operations for the elevator. It also starts the threads for Floor, ElevatorSubsystem and Scheduler components to get concurrent program execution for the overall system.

(5) pom.xml: Maven dependencies required for the program.


## Step-by-Step Setup Instructions:

(1) Download the ZIP archive file onto your Computer, and Open the Iteration4 Project Folder. 

(2) Open IntelliJ IDEA IDE and Setup a new Workspace. 

(3) Under the Main Menu, Click File and then Open… You should be able to view the imported Iteration4 Project Folder in the Package Explorer screen. 

(4) Open and View the JAVA Test file and Execute it.
** Note: This project is built in IntelliJ using the Maven build system, if copying and pasting files ensure that you use this build system


### Group Roles:

Hemilkumar Patel: 
- Add error detection via Elevator_with_Error_Detection.java and Floor_with_Error_Detection.java
- Wrote testing framework ElevatorTest.java
- Created diagrams classDiagram.png, sequence diagram.png and state machine.png
- README.md

Ben Mostafa: 
- Wrote testing framework FloorTest.java and SchedulerTest.java
- README.md

Cholen Premjeyanth: 
- Created UML Timing Diagram
- Assited with error detection
- README.md

Eliana Schartner: 
- Created code for elevator, wrote Main.java, Floor.java, Scheduler.java, Elevator.java and ElevatorSubsystem.java
- data.csv
- README.md
