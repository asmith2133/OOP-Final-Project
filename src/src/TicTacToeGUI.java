import javax.swing.*;
import java.awt.*;

public class TicTacToeGUI {
    private JFrame frame;
    private JPanel boardPanel, controlPanel;
    private JButton[][] buttons;
    private JButton resetButton, quitButton;
    private JComboBox<String> difficultyBox, boardColorBox, playerColorBox, boardSizeBox;
    private JLabel xScoreLabel, oScoreLabel, drawLabel;
    private int boardSize = 3; // Default board size
    private String currentPlayer; // The current player will be passed from Player class
    private int xScore = 0, oScore = 0, drawCount = 0;
    private Color playerColor = Color.BLACK; // Default player color
    private boolean gameOver = false;

    public TicTacToeGUI(String startingPlayer) {
        this.currentPlayer = startingPlayer; // Set the starting player based on the selection

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

        playerColorBox.addActionListener(e -> updatePlayerColor());  // Only update player color on selection

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
        resetButton.addActionListener(e -> showResetCheckingPanel()); // Show the confirmation dialog


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
                    if (gameOver) return; // Do nothing if the game is over
                    JButton sourceButton = (JButton) e.getSource();
                    if (sourceButton.getText().equals("")) {  // If the button is empty
                        sourceButton.setText(currentPlayer);  // Set the text to the current player
                        sourceButton.setForeground(playerColor); // Set the color of the player
                        if (checkWinner()) {
                            // Show a message if there's a winner
                            JOptionPane.showMessageDialog(frame, currentPlayer + " wins!");
                            updateScore();  // Update the score here, based on the current winner
                            disableBoard();  // Disable the board after a winner is found
                            gameOver = true;
                        } else if (isBoardFull()) {
                            JOptionPane.showMessageDialog(frame, "It's a draw!");
                            drawCount++;
                            drawLabel.setText("Draw: " + drawCount);
                            disableBoard();
                            gameOver = true;
                        }
                        switchPlayer();  // Switch the player after every click (if no winner yet)
                    }
                });
                boardPanel.add(buttons[i][j]);
            }
        }

        boardPanel.revalidate();
        boardPanel.repaint();
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
        // Check rows and columns dynamically
        for (int i = 0; i < boardSize; i++) {
            if (isWinningLine(i, 0, 0, 1) || isWinningLine(0, i, 1, 0)) {
                return true;
            }
        }

        // Check diagonals
        if (isWinningLine(0, 0, 1, 1) || isWinningLine(0, boardSize - 1, 1, -1)) {
            return true;
        }

        return false;
    }

    private boolean isWinningLine(int startX, int startY, int dx, int dy) {
        String first = buttons[startX][startY].getText();
        if (first.equals("")) return false;

        for (int i = 1; i < boardSize; i++) {
            int x = startX + i * dx;
            int y = startY + i * dy;
            if (!buttons[x][y].getText().equals(first)) {
                return false;
            }
        }
        // Highlight the winning line
        for (int i = 0; i < boardSize; i++) {
            int x = startX + i * dx;
            int y = startY + i * dy;
            buttons[x][y].setBackground(Color.GREEN);
        }
        return true;
    }

    private boolean isBoardFull() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (buttons[i][j].getText().equals("")) {
                    return false;
                }
            }
        }
        return true;
    }

    private void resetBoard() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                buttons[i][j].setText(""); // Clear the board
                buttons[i][j].setBackground(null); // Reset background color
            }
        }

        // Re-enable the board and set the current player without overriding it
        enableBoard(); // Re-enable the board
        gameOver = false; // Reset the game state
        // Don't reset the player; keep the player selected from showPlayerSelectionPanel()

    }



    //yes or no if the player at the reset wonder to continue with its same character or wants to change
    private void showResetCheckingPanel() {
        ResetChecking resetPanel = new ResetChecking(new JPanel(), currentPlayer, new ResetChecking.ResetSelectionListener() {
            @Override
            public void onResetConfirmed(boolean continueWithSamePlayer) {
                if (continueWithSamePlayer) {
                    resetBoard();  // Keep the same player and reset the board
                } else {
                    onPlayerSelectionRequested();  // Let the player choose their character again
                }
            }

            @Override
            public void onPlayerSelectionRequested() {
                showPlayerSelectionPanel();  // Open the character selection screen
            }
        });

        // Display the ResetChecking panel within a dialog
        JOptionPane.showMessageDialog(frame, resetPanel, "Reset Game", JOptionPane.PLAIN_MESSAGE);
    }


    //reactivate the selection on the reset
    private void showPlayerSelectionPanel() {
        // Create the dialog to choose X or O
        String[] options = {"X", "O"};
        int choice = JOptionPane.showOptionDialog(frame,
                "Choose your character:", // Message
                "Player Selection", // Title
                JOptionPane.DEFAULT_OPTION, // Option type
                JOptionPane.QUESTION_MESSAGE, // Message type
                null, // Icon
                options, // Options
                options[0]); // Default option (X)

        // If "X" is selected
        if (choice == 0) {
            currentPlayer = "X";
        }
        // If "O" is selected
        else if (choice == 1) {
            currentPlayer = "O";
        }

        // Update the player color based on the selected character
        updatePlayerColor();

        // Reset the board and game state
        resetBoard(); // Reset the board after the player selection
    }



    private void updatePlayerColor() {
        String color = (String) playerColorBox.getSelectedItem();
        switch (color) {
            case "Black" -> playerColor = Color.BLACK;
            case "Red" -> playerColor = Color.RED;
            case "Green" -> playerColor = Color.GREEN;
        }
    }

    private void updateScore() {
        // Check the winner, and update the score accordingly
        if (currentPlayer.equals("X")) {
            xScore++;  // X wins
            xScoreLabel.setText("X: " + xScore);
        } else if (currentPlayer.equals("O")) {
            oScore++;  // O wins
            oScoreLabel.setText("O: " + oScore);
        }
    }


    private void enableBoard() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                buttons[i][j].setEnabled(true); // Re-enable all buttons
            }
        }
    }

    private void disableBoard() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                buttons[i][j].setEnabled(false); // Disable all buttons
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
        // Create the Player panel and display it first
        JFrame playerFrame = new JFrame("Player Selection");
        playerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        playerFrame.setSize(400, 200);

        Player playerPanel = new Player(new Player.PlayerSelectionListener() {
            @Override
            public void onPlayerSelected(String player) {
                // Close the player selection window
                playerFrame.dispose();

                // Create and display the Tic-Tac-Toe game window with the selected player
                new TicTacToeGUI(player);  // Pass the selected player (X or O)
            }
        });

        playerFrame.add(playerPanel);
        playerFrame.setVisible(true);
    }
}
