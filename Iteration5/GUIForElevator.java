//package org.example;
import javax.swing.*;
import java.awt.*;

/**
 * Represents the GUI as the Display Console that shows where each of the Elevators is in Real-Time for the Elevator Control System.
 */
public class GUIForElevator extends JFrame {
    private JPanel[] elevatorPanels;

    // Creates a GUIForElevator object with the detailed number of elevators present in the system
    public GUIForElevator(int numElevators) {
        super("Elevator Control System");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Grid Layout of the JFrame is Set to a 2 by 2 grid, getting the quadrant format on GUI
        setLayout(new GridLayout(2, 2));

        // Initializing an array that will be able to hold the elevator panels/windows
        elevatorPanels = new JPanel[numElevators];

        // ElevatorPanel for each elevator is created and added to the GUI Design
        for (int i = 0; i < numElevators; i++) {
            ElevatorPanel  panel = new ElevatorPanel(i);
            elevatorPanels[i] = panel;
            add(panel);
        }
        // Set the Frame become in the Visible State
        setVisible(true);
    }

    // Updates the position of the elevator at the specific index level to the given current floor
    public void updateElevatorLocation(int elevatorInd, int currentFloor, String state, int passengers) {
        ((ElevatorPanel)elevatorPanels[elevatorInd]).updatingPosition(currentFloor, state, passengers);
    }

    // Function for the individual panels that displays information for each of the elevators of the system
    static class ElevatorPanel extends JPanel {
        private JLabel positionLabel;
        private JLabel elevatorImageLabel;
        private JLabel elevatorIDLabel;

        // Created a ElevatorPanel with the provided elevator number
        public ElevatorPanel(int elevatorNum) {
            // Setting the Layout of the Panel to BorderLayout
            setLayout(new BorderLayout());

            // Constructed a JLabel to show the elevator number you are viewing for clarity, center aligned the text, and added elevatorIDLabel to the north of the GUI panel
            elevatorIDLabel = new JLabel("Elevator - " + elevatorNum);
            elevatorIDLabel.setHorizontalAlignment(SwingConstants.CENTER);
            add(elevatorIDLabel, BorderLayout.NORTH);

            // Constructed a JLabel to show the current floor info, center aligned the text, and added the positionLabel to the middle of the GUI panel
            positionLabel = new JLabel("The Elevator Subsystem is Currently at Floor Number: ");
            positionLabel.setHorizontalAlignment(SwingConstants.CENTER);
            add(positionLabel, BorderLayout.CENTER);

            // Setting the background color and border of the panel color
            setBackground(Color.LIGHT_GRAY);
            setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

            // Setting the font of the positionLabel to Serif font type, Bold, and of size 15
            positionLabel.setFont(new Font("Serif", Font.BOLD, 15));

            // Sets the text color for the positionLabel to the color Black
            positionLabel.setForeground(Color.BLACK);
        }

        // Updates the position label with the current floor that the elevator is currently on
        public void updatingPosition(int floor, String state, int passengers) {
            positionLabel.setText("<html>The Elevator Subsystem is Currently at Floor Number: " + floor +
                    "<br>The current state is: " + state + "<br>The passenger count is: " + passengers + "</html>");
        }
    }
}
