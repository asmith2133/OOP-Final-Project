import javax.swing.*;
import java.awt.*;

public class ResetChecking extends JPanel {
    private JButton yesButton, noButton;

    public ResetChecking(JPanel parentPanel, String currentPlayer, ResetSelectionListener listener) {
        // Create the buttons
        yesButton = new JButton("Yes");
        noButton = new JButton("No");

        // Set the layout for the panel
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Add the instructions and buttons
        add(new JLabel("Do you want to continue with the same player (" + currentPlayer + ")?"));
        add(Box.createVerticalStrut(20)); // Add some space between components
        add(yesButton);
        add(Box.createVerticalStrut(10)); // Add space between buttons
        add(noButton);

        // Add Action Listeners to the buttons
        yesButton.addActionListener(e -> listener.onResetConfirmed(true));  // Continue with same player
        noButton.addActionListener(e -> listener.onResetConfirmed(false));  // Change the player

        // Customize button appearance
        customizeButton(yesButton);
        customizeButton(noButton);
    }

    private void customizeButton(JButton button) {
        button.setPreferredSize(new Dimension(150, 40));
        button.setBackground(Color.BLACK);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setFont(new Font("Arial", Font.BOLD, 16));
    }

    // Define the listener interface
    public interface ResetSelectionListener {
        void onResetConfirmed(boolean continueWithSamePlayer); // Confirmation
        void onPlayerSelectionRequested(); // For character change selection
    }
}
