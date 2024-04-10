##  SYSC 3303 - Project Iteration 2

## Source Files Included:

(1) Elevator.java - This source file represents the Elevator subsystem in the concurrent system, handling the basic functionality of the elevator of moving between floor levels. Additionally, it has been modified for Iteration 2 so that it can integrate with the Scheduler subsystem, in receiving and processing the requests from it.
     
(2) Floor.java - This source file represents the Floor subsystem in the concurrent system, so it reads events from a file and sends the requests to the Scheduler for further processing.

(3) Scheduler.java - This source file its as the Central Server, mainly managing the communication between the Elevator and Floor threads, handling send and received requests, and controlling the flow of the information. It has been enhanced in Iteration 2 so that it can have the functionality of coordinating with the Elevator subsystem and processing its requests. The Scheduler is notified of the elevator arrivals at the different floors.
      
(4) Inform.java - This source file has a Inform class defined, which is encapsulating information based upon requests and messages passed between the three subsystems, including events.

(5) FloorSystems.java - This source file instantiate a system of floors.

(6) UnitTests.java - Testing the overall framework.


## Step-by-Step Setup Instructions:

(1) Download the ZIP archive file onto your Computer, and Open the Iteration 2 Project Folder.

(2) Open IntelliJ IDEA IDE and Setup a new Workspace. 

(3) Under the Main Menu, Click File and then Open… You should be able to view the imported Iteration 2 Project Folder in the Package Explorer screen.

(4) Open and View the JAVA Test file and Execute it.

## Testing Instructions:

Run UnitTests.java using the Maven build system.

Group Roles:

Hemilkumar Patel: Updated Elevator.java and implemented Scheduler.java, and README.md

Ben Mostafa: Bug Fixes in FloorSystem.java and Elevator.java, and README.md

Cholen Premjeyanth: UML Class Diagram, and README.md

Eliana Schartner: State Machine Diagram, Updated UML Class Diagram, JUnit Test file, minor bug fixes and README.md
