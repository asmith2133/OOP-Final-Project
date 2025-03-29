import javax.swing.*;
import java.awt.*;

public class Player extends JPanel {
    interface PlayerSelectionListener {
        void onPlayerSelected(String player);
    }

    public Player(PlayerSelectionListener listener) {
        setBackground(Color.ORANGE);
        setLayout(new GridLayout(2, 1, 10, 10));

        JLabel playerChoice = new JLabel("Which player would you like to be: X or O?", SwingConstants.CENTER);
        playerChoice.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel buttonPanel = new JPanel();
        JButton xButton = new JButton("X");
        JButton oButton = new JButton("O");

        xButton.setFont(new Font("Arial", Font.BOLD, 20));
        oButton.setFont(new Font("Arial", Font.BOLD, 20));

        xButton.setPreferredSize(new Dimension(80, 40));
        oButton.setPreferredSize(new Dimension(80, 40));

        xButton.addActionListener(e -> listener.onPlayerSelected("X"));
        oButton.addActionListener(e -> listener.onPlayerSelected("O"));

        buttonPanel.add(xButton);
        buttonPanel.add(oButton);

        add(playerChoice);
        add(buttonPanel);
    }
}
