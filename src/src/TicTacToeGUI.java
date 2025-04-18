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
    private String currentPlayer; // The current player will be passed from Player class
    private int xScore = 0, oScore = 0, drawCount = 0;
    private Color playerColor = Color.BLACK; // Default player color
    private AiPlayer aiPlayer;

    public TicTacToeGUI(String startingPlayer, boolean vsAI) {
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

        playerColorBox.addActionListener(e -> updatePlayerColor());

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

        if (vsAI) {
            String aiSymbol = startingPlayer.equals("X") ? "O" : "X";
            aiPlayer = new AiPlayer((String) difficultyBox.getSelectedItem(), aiSymbol,
                    this);
        }

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
                        sourceButton.setForeground(playerColor); // Set the color of the player
                        switchPlayer();  // Switch the player after every click
                        if (checkWinner()) {
                            // Show a message if there's a winner
                            JOptionPane.showMessageDialog(frame, currentPlayer + " wins!");
                            updateScore();
                            disableBoard();
                        }
                    }
                });
                boardPanel.add(buttons[i][j]);
            }
        }

        boardPanel.revalidate();
        boardPanel.repaint();
        resetBoard(); // to make the board resetting automatically with the color of the board color

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

    public boolean checkWinner() {
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

    private void updatePlayerColor() {
        String color = (String) playerColorBox.getSelectedItem();
        switch (color) {
            case "Black" -> playerColor = Color.BLACK;
            case "Red" -> playerColor = Color.RED;
            case "Green" -> playerColor = Color.GREEN;
        }
    }

    private void updateScore() {
        if (currentPlayer.equals("X")) {
            oScore++; // O wins
            oScoreLabel.setText("O: " + oScore);
        } else {
            xScore++; // X wins
            xScoreLabel.setText("X: " + xScore);
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

    public int getBoardSize() {
        return boardSize;
    }

    public JButton getButton(int row, int column) {
        return buttons[row][column];
    }

    public boolean isSpotEmpty(int row, int column) {
        return buttons[row][column].getText().isEmpty();
    }

    public boolean isBoardFull() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (buttons[i][j].getText().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    public void makeAiMove(int row, int column, String symbol) {
        buttons[row][column].setText(symbol);
        buttons[row][column].setBackground(playerColor);

        if (checkWinner()) {
            JOptionPane.showMessageDialog(frame, symbol + " wins!");
            updateScore();
            disableBoard();
        }
        else if (isBoardFull()) {
            JOptionPane.showMessageDialog(frame, "It's a draw!");
            drawCount++;
            drawLabel.setText("Draw: " + drawCount);
        }
        else {
            switchPlayer();
        }
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

                /// Create the Adversary selection frame
                JFrame adversaryFrame = new JFrame("Adversary Selection");
                adversaryFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                adversaryFrame.setSize(400, 200);

                Adversary adversaryPanel = new Adversary(e -> {
                    // This ActionListener is called when either Human or AI button is clicked
                    JButton source = (JButton) e.getSource();
                    boolean vsAI = source.getText().equals("AI");

                    // Close the adversary selection window
                    adversaryFrame.dispose();

                    // Create and display the Tic-Tac-Toe game window with the selected player and opponent type
                    SwingUtilities.invokeLater(() -> {
                        new TicTacToeGUI(player, vsAI);  // Pass the selected player (X or O) and whether it's vs AI
                    });
                });
                adversaryFrame.add(adversaryPanel);
                adversaryFrame.setVisible(true);
            }
        });

        playerFrame.add(playerPanel);
        playerFrame.setVisible(true);
    }
}
