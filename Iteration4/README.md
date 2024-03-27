Source Files Included:
(1) ClientFloor.java: This source file contains the implementation that can read requests from CSV file format and send them to the Scheduler for processing in the Elevator System.

(2) HostScheduler.java: This source file manages incoming requests from the ClientFloor and coordinates the scheduling of the elevator operations based on the current state of the Elevator.

(3) ServerElevator.java: This source file represents the individual elevators of the system. It handles receiving the data requests from the Scheduler, updating its current state, and having movements between the floors.

(4) Main.java: This source file serves as the starting point for the elevator system, in terms of initializing and starting the ServerElevator, HostScheduler, and ClientFloor subsystem components to control the functional operations for the elevator. It also starts the threads for ClientFloor and HostScheduler components to get concurrent program execution for the overall system.

Step-by-Step Setup Instructions:
(1) Download the ZIP archive file onto your Computer, and Open the Iteration3 Project Folder. (2) Open IntelliJ IDEA IDE and Setup a new Workspace. (3) Under the Main Menu, Click File and then Open… You should be able to view the imported Iteration3 Project Folder in the Package Explorer screen. (4) Open and View the JAVA Test file and Execute it.

Actions from Members for this iteration:
Group Roles:
Hemilkumar Patel: Elevator.java, Unit Testing and README.md

Ben Mostafa: Floor.java, FloorSystem.java, Unit Testing and README.md, 

Cholen Premjeyanth: Scheduler.java, UML Timing Diagram, and README.md

Eliana Schartner: Scheduler.java, Elevator.java, JUnit Test files, and README.md
