##  SYSC 3303 - Project Iteration 5

## Source Files Included:

(1) Elevator.java: These thread(s) are instantiated by ElevatorSubsystem for the number of elevators in the system. This source file contains the state machine for the elevator and prints off the state transitions. Additionally, this file allows for stops to be added to each elevator. Iteration 5 Modifications include: the state machine and the add stops method for the elevators will also include the capacity limit implementation for the passengers where it will follow the design where the passengers would have to wait until an elevator car is empty before directly boarding on, the count of the passengers will increment according to what state transition condition the passengers tend to follow, it will decrease the passenger count whenever the passengers are getting off the elevator once they reach the destination floor verifying the count doesn't go below value 0, and lastly the current passenger count on the particular elevator will be updated onto the getInfo() method as a string when the program is executed.  

(2) ElevatorSubsystem.java: This source file instantiates Elevator threads and manages communication between the scheduler and individual elevator threads. The scheduler sends messages to specific elevators using the addStop method. Iteration 5 Modifications include: the passIn parameter is passed to the addStop method for the number of passenger input that enters the elevator car, and the information will still be used by the scheduler class to send the messages to the different elevators using the updated addStop method. 

(3) Floor.java: This source file contains the implementation that can read requests from a CSV file (data.csv) format. The CSV file includes the time when a passenger begins the wait at a floor, the floor location where the passenger is waiting, if they are going in an upward or downward direction, and the destination floor details. It sends the information to the Scheduler for processing in the Elevator System via UDP messages. No significant changes were made to this file from Iteration 4. 

(4) GUIForElevator.java: This source file contains GUI implementation for the elevator control system. The GUI acts as a display console that shows the real-time positions of each of the elevator car in the system. The updateElevatorLocation() method is being used to update the position of each elevator on the GUI display in a real-time manner. The GUI was designed using the Swing components in Java, it consists of four panels in total, each representing an elevator car organized in a 2 by 2 grid layout. Each of the elevator panels displays the following information: the elevator number, current floor, the present state, and the passenger count as this will keep changing dynamically as time goes on. The updatingPosition() method will update the position of the individual elevators according to the current floor level. 

(5) Main.java: This source file serves as the starting point for the elevator system, in terms of initializing and starting the ElevatorSubsystem, Scheduler, and Floor subsystem components to control the functional operations for the elevator. It also starts the threads for Floor, ElevatorSubsystem and Scheduler components to get concurrent program execution for the overall system. Iteration 5 modifications include: the GUIForElevator object is instantiated with 4 elevators which was a part of the project requirement, and simulating the elevator movement over the time duration for the accurate position updates.

(6) Passenger.java: This source file contains the passenger class with the attributes representing the pickup and destination floors, with the getter methods and the toString() method to retrieve and display the passenger information.

(7) Scheduler.java: This source file manages incoming requests from the ClientFloor and coordinates the scheduling of the elevator operations based on the current state of the Elevator. It does this via UDP messages between the Floor and the ElevatorSubsystem. Iteration 5 Modifications include: improving the scheduling algorithm so that the elevator capacity is involved in the process and distributing the add stops among the elevators in a better way. Also, the scheduling will use a recursive process as it will keep trying to schedule if the elevator car is at a maximum capacity limit. This will enhance the elevator control system making it more efficient. 


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


