##  SYSC 3303 - Project Iteration 5

## Source Files Included:

(1) Elevator.java:

(2) ElevatorSubsystem.java: This source file instantiates Elevator threads and manages communication between the scheduler and individual elevator threads. The scheduler sends messages to specific elevators using the addStop method. Iteration 5 Modifications include: the passIn parameter is passed to the addStop method for the number of passenger input that enters the elevator car, and the information will still be used by the scheduler class to send the messages to the different elevators using the altered addStop method. 

(3) Floor.java: This source file contains the implementation that can read requests from a CSV file (data.csv) format. The CSV file includes the time when a passenger begins the wait at a floor, the floor location where the passenger is waiting, if they are going in an upward or downward direction, and the destination floor details. It sends the information to the Scheduler for processing in the Elevator System via UDP messages. No significant changes were made to this file from Iteration 4. 

(4) GUIForElevator.java: This source file contains the GUI as a display console implementation that displays where each of the elevator cars is in a real-time manner
for the elevator control system. It updates the position of the elevator car at the specific index it is currently at, compared to the current given floor value. It also updates the position label with the current floor level that the elevator car is at, onto the individual panels on the GUI display that has information for the 4 of the elevator cars that are concurrently running together. 

(5) Main.java: This source file serves as the starting point for the elevator system, in terms of initializing and starting the ElevatorSubsystem, Scheduler, and Floor subsystem components to control the functional operations for the elevator. It also starts the threads for Floor, ElevatorSubsystem and Scheduler components to get concurrent program execution for the overall system. Iteration 5 modifications include: the GUIForElevator object is instantiated with 4 elevators which was a part of the project requirement, and simulation of the elevator movement over the time duration where it is going through an iteration for the objective of updating the elevator car positions accurately.

(6) Passenger.java: This source file contains the passenger implementation with the getter methods for the pickup floor and the destination floor when the program is running, outputting the values for the pickup and destination floors as a string.

(7) Scheduler.java: 


## Step-by-Step Setup Instructions:

(1) Download the ZIP archive file onto your Computer, and Open the Iteration5 Project Folder. 

(2) Open IntelliJ IDEA IDE and Setup a new Workspace. 

(3) Under the Main Menu, Click File and then Open… You should be able to view the imported Iteration5 Project Folder in the Package Explorer screen. 

(4) Open and View the JAVA source files and do the execution process.
** Note: This project is built in IntelliJ using the Maven build system, if copying and pasting files ensure that you use this build system at first


### Group Roles:

Hemilkumar Patel: 
- Created and updated GUIforElevator.java for GUI display implementation
- Created State Machine Diagram for Scheduler.java, and the UML Sequence Diagram for the different error scenarios
- Final Report
- README.md on Iteration 5

Ben Mostafa: 
- Bug Fixes
- Worked on the Capacity Limit, and Passenger.java
- Updated ElevatorSubsystem.java and Scheduler.java
- Final Report

Cholen Premjeyanth: 
- GUI implementation for GUIForElevator.java
- Final Report

Eliana Schartner: 
- Did bug fixes for the GUIForElevator.java and the Capacity Limit
- Updated Main.java
- Created separate files for running on different terminals on one computer
- Final Report


