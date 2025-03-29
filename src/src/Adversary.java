import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Adversary extends JPanel {
    public Adversary(ActionListener startGameListener) {
        // Set the layout for the panel
        setBackground(Color.ORANGE);
        setLayout(new GridLayout(2, 1, 10, 10));


        // Label for the question
        JLabel lblAdversary = new JLabel("Would you like to play against a human or an AI?");

        // Create buttons for Human and AI choices
        JButton HumanAd = new JButton("Human");
        JButton AIAd = new JButton("AI");

        // ActionListener for the "Human" button
        HumanAd.addActionListener(e -> {
            // Set the game mode to Human vs Human
            // Call the startGameListener to initiate the TicTacToeGUI (Human vs Human)
            startGameListener.actionPerformed(e); // This will trigger starting the human vs human game
        });

        // ActionListener for the "AI" button
        AIAd.addActionListener(e -> {
            // Set the game mode to Human vs AI
            // Here, you would handle AI game setup, for now we just start the AI mode
            // You might need a separate action to handle AI-specific logic
            startGameListener.actionPerformed(e); // This would start the AI game mode (you can handle this separately)
        });

        // Add components to the panel
        add(lblAdversary);
        add(HumanAd);
        add(AIAd);
    }
}
