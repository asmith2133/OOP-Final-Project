import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class TicTacToeGUI {
    private JFrame frame;
    private JPanel boardPanel, controlPanel;
    private JButton[][] buttons;
    private JButton resetButton, quitButton;
    private JComboBox<String> difficultyBox, boardColorBox, playerColorBox, boardSizeBox;
    private JLabel xScoreLabel, oScoreLabel, drawLabel;
    private int boardSize = 3; // Default board size
    private String currentPlayer = "X"; // Default starting player

    public TicTacToeGUI() {
        frame = new JFrame("Tic-Tac-Toe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLayout(new BorderLayout());

        // Control Panel (Top)
        controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));

        difficultyBox = new JComboBox<>(new String[]{"Easy", "Medium", "Hard"});
        boardColorBox = new JComboBox<>(new String[]{"Gray", "White", "Blue"});
        playerColorBox = new JComboBox<>(new String[]{"Black", "Red", "Green"});
        boardSizeBox = new JComboBox<>(new String[]{"3x3", "4x4", "5x5"});

        // Customize Combo Boxes
        customizeComboBox(boardColorBox);
        customizeComboBox(playerColorBox);
        customizeComboBox(boardSizeBox);
        customizeComboBox(difficultyBox);

        boardSizeBox.addActionListener(e -> updateBoardSize());

        xScoreLabel = new JLabel("X: 0", SwingConstants.CENTER);
        xScoreLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Adjust the font size here
        drawLabel = new JLabel("Draw: 0", SwingConstants.CENTER);
        drawLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Adjust the font size here
        oScoreLabel = new JLabel("O: 0", SwingConstants.CENTER);
        oScoreLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Adjust the font size here

        JPanel scorePanel = new JPanel(new GridLayout(1, 3));
        scorePanel.add(xScoreLabel);
        scorePanel.add(drawLabel);
        scorePanel.add(oScoreLabel);

        JPanel settingsPanel = new JPanel();
        settingsPanel.add(new JLabel("Difficulty:"));
        settingsPanel.add(difficultyBox);
        settingsPanel.add(new JLabel("Board Color:"));
        settingsPanel.add(boardColorBox);
        settingsPanel.add(new JLabel("Player Color:"));
        settingsPanel.add(playerColorBox);
        settingsPanel.add(new JLabel("Board Size:"));
        settingsPanel.add(boardSizeBox);

        controlPanel.add(settingsPanel);
        controlPanel.add(scorePanel);
        frame.add(controlPanel, BorderLayout.NORTH);

        // Board Panel (Center)
        boardPanel = new JPanel();
        frame.add(boardPanel, BorderLayout.CENTER);
        createBoard();

        // Bottom Panel (Reset and Quit Buttons)
        JPanel bottomPanel = new JPanel(new FlowLayout());
        resetButton = new JButton("Reset");
        quitButton = new JButton("Quit");

        customizeButton(resetButton);
        customizeButton(quitButton);

        quitButton.addActionListener(e -> System.exit(0)); // Closes the application on click
        resetButton.addActionListener(e -> resetBoard()); // Resets the board

        bottomPanel.add(resetButton);
        bottomPanel.add(quitButton);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        // Display the frame
        frame.setVisible(true);
    }

    private void createBoard() {
        boardPanel.removeAll();
        boardPanel.setLayout(new GridLayout(boardSize, boardSize));
        buttons = new JButton[boardSize][boardSize];

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setFont(new Font("Arial", Font.BOLD, 40));
                buttons[i][j].setEnabled(true);  // Enable the button for clicking
                buttons[i][j].addActionListener(e -> {
                    // Only update the button if it hasn't been clicked already
                    JButton sourceButton = (JButton) e.getSource();
                    if (sourceButton.getText().equals("")) {  // If the button is empty
                        sourceButton.setText(currentPlayer);  // Set the text to the current player
                        switchPlayer();  // Switch the player after every click
                        if (checkWinner()) {
                            // Show a message if there's a winner
                            JOptionPane.showMessageDialog(frame, currentPlayer + " wins!");
                        }
                    }
                });
                boardPanel.add(buttons[i][j]);
            }
        }

        boardPanel.revalidate();
        boardPanel.repaint();
    }

    private void resetBoard() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                buttons[i][j].setText(""); // Clear the board
                buttons[i][j].setBackground(null); // Reset background color
            }
        }
        currentPlayer = "X"; // Reset the starting player
        enableBoard(); // Re-enable the board
    }

    private void updateBoardSize() {
        String selectedSize = (String) boardSizeBox.getSelectedItem();
        switch (selectedSize) {
            case "3x3" -> boardSize = 3;
            case "4x4" -> boardSize = 4;
            case "5x5" -> boardSize = 5;
        }
        createBoard();
    }

    private boolean checkWinner() {
        // Check rows
        for (int i = 0; i < boardSize; i++) {
            if (buttons[i][0].getText().equals(currentPlayer) &&
                    buttons[i][1].getText().equals(currentPlayer) &&
                    buttons[i][2].getText().equals(currentPlayer)) {
                // Highlight the winning buttons
                buttons[i][0].setBackground(Color.GREEN);
                buttons[i][1].setBackground(Color.GREEN);
                buttons[i][2].setBackground(Color.GREEN);
                return true; // Winning row
            }
            if (buttons[0][i].getText().equals(currentPlayer) &&
                    buttons[1][i].getText().equals(currentPlayer) &&
                    buttons[2][i].getText().equals(currentPlayer)) {
                // Highlight the winning buttons
                buttons[0][i].setBackground(Color.GREEN);
                buttons[1][i].setBackground(Color.GREEN);
                buttons[2][i].setBackground(Color.GREEN);
                return true; // Winning column
            }
        }

        // Check diagonals
        if (buttons[0][0].getText().equals(currentPlayer) &&
                buttons[1][1].getText().equals(currentPlayer) &&
                buttons[2][2].getText().equals(currentPlayer)) {
            // Highlight the winning buttons
            buttons[0][0].setBackground(Color.GREEN);
            buttons[1][1].setBackground(Color.GREEN);
            buttons[2][2].setBackground(Color.GREEN);
            return true; // Winning diagonal
        }
        if (buttons[0][2].getText().equals(currentPlayer) &&
                buttons[1][1].getText().equals(currentPlayer) &&
                buttons[2][0].getText().equals(currentPlayer)) {
            // Highlight the winning buttons
            buttons[0][2].setBackground(Color.GREEN);
            buttons[1][1].setBackground(Color.GREEN);
            buttons[2][0].setBackground(Color.GREEN);
            return true; // Winning diagonal
        }

        return false;
    }

    private void enableBoard() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                buttons[i][j].setEnabled(true); // Re-enable all buttons
            }
        }
    }

    private void customizeComboBox(JComboBox<String> comboBox) {
        comboBox.setPreferredSize(new Dimension(100, 30));
        comboBox.setBackground(Color.BLACK);
        comboBox.setForeground(Color.WHITE);
        comboBox.setOpaque(true);
        comboBox.setBorder(BorderFactory.createEmptyBorder());

        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setFont(new Font("Arial", Font.BOLD, 14));
                if (isSelected) {
                    setBackground(Color.BLACK);
                    setForeground(Color.WHITE);
                } else {
                    setBackground(Color.DARK_GRAY);
                    setForeground(Color.WHITE);
                }
                setOpaque(true);
                return this;
            }
        });

        comboBox.setFocusable(false);
    }

    private void customizeButton(JButton button) {
        button.setPreferredSize(new Dimension(120, 40));
        button.setBackground(Color.BLACK);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setFont(new Font("Arial", Font.BOLD, 16));
    }

    private void switchPlayer() {
        currentPlayer = currentPlayer.equals("X") ? "O" : "X";  // Switch between X and O
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TicTacToeGUI::new);
    }
}
