##  SYSC 3303 - Project Iteration 1

### Source Files Included:

(1) Elevator.java - This source file represents the Elevator subsystem in the concurrent system, handling the basic functionality of the elevator of moving between floor levels.

(2) Floor.java - This source file represents the Floor subsystem in the concurrent system, so it reads events from a file and sends the requests to the Scheduler for further processing.

(3) Scheduler.java - This source file its as the Central Server, mainly managing the communication between the Elevator and Floor threads, handling send and received requests, and controlling the flow of the information.

(4) Inform.java - This source file has a Inform class defined, which is encapsulating information based upon requests and messages passed between the three subsystems, including events.


### Step-by-Step Setup Instructions:

(1) Download the ZIP archive file onto your Computer, and Open the Iteration 1 Project Folder. 
(2) Open IntelliJ IDEA IDE and Setup a new Workspace. 
(3) Under the Main Menu, Click File and then Open… You should be able to view the imported Iteration 1 Project Folder in the Package Explorer screen.
(4) Open and View the JAVA Test file and Execute it.


### Testing Instructions:

Open up the following Test files one by one: ElevatorTest.java, FloorTest.java, SchedulerTest.java, and InformTest.java. And, run each method in each test files individually with the JUnit Test Cases.


### Group Roles:

Hemilkumar Patel: Elevator.java, Inform.java, and README.md
Ben Mostafa: Floor.java, and README.md
Cholen Premjeyanth: Scheduler.java, UML Class Diagram, and README.md
Eliana Schartner: Sequence Diagram, JUnit Test files, and README.md
