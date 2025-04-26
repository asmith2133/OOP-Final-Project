import javax.swing.*;
import java.awt.*;

public class TicTacToeGUI {
    private JFrame frame;
    private JPanel boardPanel, controlPanel;
    private JButton[][] buttons;
    private JButton resetButton, quitButton;
    private JComboBox<String> difficultyBox, boardColorBox, playerColorBox, opponentColorBox, boardSizeBox;
    private JLabel xScoreLabel, oScoreLabel, drawLabel;
    private int boardSize = 3; // Default board size
    private String currentPlayer; // The current player will be passed from Player class
    private int xScore = 0, oScore = 0, drawCount = 0;
    private Color playerColor = Color.BLACK; // Default player color
    private Color xColor = Color.BLACK; // Default for Xs
    private Color oColor = Color.RED; // Default for Os
    private boolean gameOver = false;
    private boolean isHumanVsAI = true; // Set the Default as AI
    private AiPlayer aiPlayer;

    public TicTacToeGUI(String startingPlayer, boolean isHumanVsAI) {
        this.currentPlayer = startingPlayer; // Set the starting player based on the selection
        this.isHumanVsAI = isHumanVsAI; // Set the starting player to an AI based on the selection


        frame = new JFrame("Tic-Tac-Toe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 900);
        frame.setLayout(new BorderLayout());

        // Control Panel (Top)
        controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));

        difficultyBox = new JComboBox<>(new String[]{"Easy", "Medium", "Hard"});
        boardColorBox = new JComboBox<>(new String[]{"Gray", "White", "Blue"});
        playerColorBox = new JComboBox<>(new String[]{"Black", "Red", "Green"});
        opponentColorBox = new JComboBox<>(new String[]{"Red", "Green", "Blue"});
        boardSizeBox = new JComboBox<>(new String[]{"3x3", "4x4", "5x5"});

        if (isHumanVsAI){
            String aiSymbol = startingPlayer.equals("X") ? "O" : "X";
            //Initialize with easy difficulty
            aiPlayer = new AiPlayer(aiSymbol, this);

            //Set the combobox to easy
            difficultyBox.setSelectedItem("Easy");

            //If AI goes first, makes a move
            if (startingPlayer.equals(aiSymbol)) {
                SwingUtilities.invokeLater(() -> {
                    aiPlayer.makeMove();
                });
            }
        }

        if (isHumanVsAI){
            difficultyBox.addActionListener(e -> {
                if (aiPlayer != null) {
                    aiPlayer.setDifficulty((String) difficultyBox.getSelectedItem());
                }
            });
        }

        // Customize Combo Boxes
        customizeComboBox(boardColorBox);
        customizeComboBox(playerColorBox);
        customizeComboBox(opponentColorBox);
        customizeComboBox(boardSizeBox);
        customizeComboBox(difficultyBox);

        playerColorBox.addActionListener(e -> updatePlayerColor());  // Only update player color on selection
        opponentColorBox.addActionListener(e -> updateOpponentColor());
        boardColorBox.addActionListener(e -> updateBoardColor());
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
        if (isHumanVsAI) { // If the player goes against an AI the difficulty box will appear
            settingsPanel.add(new JLabel("Difficulty:"));
            settingsPanel.add(difficultyBox);
            difficultyBox.setEnabled(true);
        } else {
            difficultyBox.setEnabled(false);
        }
        settingsPanel.add(new JLabel("Board Color:"));
        settingsPanel.add(boardColorBox);
        settingsPanel.add(new JLabel("Player Color:"));
        settingsPanel.add(playerColorBox);
        if (!isHumanVsAI) { // If the player is playing against another player, lets both choose their colors
            settingsPanel.add(new JLabel("Player 2 Color:"));
            settingsPanel.add(opponentColorBox);
        }
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
                        if (currentPlayer.equals("X")) {
                            sourceButton.setForeground(xColor); // Set X color
                        } else {
                            sourceButton.setForeground(oColor); // Set O color
                        }
                        if (checkWinner()) {
                            // Play win/lose sound depending on who won
                            if (isHumanVsAI && !currentPlayer.equals("X")) {
                                new Thread(() -> SoundPlayer.playSound("sounds/lose.wav")).start();  // Human wins, AI loses
                            } else {
                                new Thread(() -> SoundPlayer.playSound("sounds/win.wav")).start();   // Win sound
                            }

                            // Show a message if there's a winner
                            JOptionPane.showMessageDialog(frame, currentPlayer + " wins!");
                            updateScore();  // Update the score here, based on the current winner
                            disableBoard();  // Disable the board after a winner is found
                            gameOver = true;
                        } else if (isBoardFull()) {
                            // Play draw sound
                            new Thread(() -> SoundPlayer.playSound("sounds/draw.wav")).start();
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

        updateBoardColor(); // Make sure board color is updated on board creation

        boardPanel.revalidate();
        boardPanel.repaint();

        resetBoard(); // will reset the board automatically at the first time and will start the game with the board color selescted
    }

    private void updateBoardSize() {
        // Picks up the size for the board
        String selectedSize = (String) boardSizeBox.getSelectedItem();
        // Selects the board size and stores it in boardSize
        switch (selectedSize) {
            case "3x3" -> boardSize = 3;
            case "4x4" -> boardSize = 4;
            case "5x5" -> boardSize = 5;
        }
        // Makes a new board with the new size
        createBoard();
    }

    private void updateBoardColor() {
        // Picks up the color for the board
        String color = (String) boardColorBox.getSelectedItem();
        // Changes the color name into the color object
        Color boardColor = switch (color) {
            case "Gray" -> Color.GRAY;
            case "White" -> Color.WHITE;
            case "Blue" -> Color.BLUE;
            default -> Color.GRAY;
        };
        // Applies selected color to the board panel
        boardPanel.setBackground(boardColor);
    }

    private void updatePlayerColor() {
        // Gets the color from the player
        String color = (String) playerColorBox.getSelectedItem();
        // Assigns the color to the x player
        xColor = switch (color) {
            case "Black" -> Color.BLACK;
            case "Red" -> Color.RED;
            case "Green" -> Color.GREEN;
            default -> Color.BLACK;
        };
        // If the player is going against AI, assign the AI with a color
        if (isHumanVsAI) {
            oColor = (xColor == Color.BLACK) ? Color.RED : Color.BLACK; // auto-assign
        }
    }

    private void updateOpponentColor() {
        // If other player is not an AI, then this runs
        if (!isHumanVsAI) {
            // Gets the color from the other player
            String color = (String) opponentColorBox.getSelectedItem();
            // Assigns the color to the o player
            oColor = switch (color) {
                case "Red" -> Color.RED;
                case "Green" -> Color.GREEN;
                case "Blue" -> Color.BLUE;
                default -> Color.RED;
            };
        }
    }

    public boolean checkWinner() {
        for (int i = 0; i < boardSize; i++) {
            if (isWinningLine(i, 0, 0, 1) || isWinningLine(0, i, 1, 0)) {
                return true;
            }
        }
        return isWinningLine(0, 0, 1, 1) || isWinningLine(0, boardSize - 1, 1, -1);
    }

    private boolean isWinningLine(int startX, int startY, int dx, int dy) {
        String first = buttons[startX][startY].getText();
        if (first.equals("")) return false;
        for (int i = 1; i < boardSize; i++) {
            int x = startX + i * dx;
            int y = startY + i * dy;
            if (!buttons[x][y].getText().equals(first)) return false;
        }
        for (int i = 0; i < boardSize; i++) {
            int x = startX + i * dx;
            int y = startY + i * dy;
            buttons[x][y].setBackground(Color.GREEN);
        }
        return true;
    }

    public boolean isBoardFull() {
        for (int i = 0; i < boardSize; i++)
            for (int j = 0; j < boardSize; j++)
                if (buttons[i][j].getText().equals("")) return false;
        return true;
    }

    private void resetBoard() {
        for (int i = 0; i < boardSize; i++)
            for (int j = 0; j < boardSize; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setBackground(null);
                buttons[i][j].setEnabled(true);
            }
        enableBoard();
        gameOver = false;
    }

    private void showResetCheckingPanel() {
        ResetChecking resetPanel = new ResetChecking(new JPanel(), currentPlayer, new ResetChecking.ResetSelectionListener() {
            public void onResetConfirmed(boolean keepSame) {
                if (keepSame) resetBoard();
                else onPlayerSelectionRequested();
            }
            public void onPlayerSelectionRequested() {
                showPlayerSelectionPanel();
            }
        });
        JOptionPane.showMessageDialog(frame, resetPanel, "Reset Game", JOptionPane.PLAIN_MESSAGE);
    }

    private void showPlayerSelectionPanel() {
        String[] options = {"X", "O"};
        int choice = JOptionPane.showOptionDialog(frame, "Choose your character:", "Player Selection",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (choice == 0) currentPlayer = "X";
        else if (choice == 1) currentPlayer = "O";
        updatePlayerColor();
        resetBoard();
    }

    private void updateScore() {
        if (currentPlayer.equals("X")) {
            xScore++;
            xScoreLabel.setText("X: " + xScore);
        } else {
            oScore++;
            oScoreLabel.setText("O: " + oScore);
        }
    }

    private void enableBoard() {
        for (JButton[] row : buttons)
            for (JButton button : row)
                button.setEnabled(true);
    }

    private void disableBoard() {
        for (JButton[] row : buttons)
            for (JButton button : row)
                button.setEnabled(false);
    }

    private void customizeComboBox(JComboBox<String> comboBox) {
        comboBox.setPreferredSize(new Dimension(100, 30));
        comboBox.setBackground(Color.BLACK);
        comboBox.setForeground(Color.WHITE);
        comboBox.setOpaque(true);
        comboBox.setBorder(BorderFactory.createEmptyBorder());
        comboBox.setRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setFont(new Font("Arial", Font.BOLD, 14));
                setBackground(isSelected ? Color.BLACK : Color.DARK_GRAY);
                setForeground(Color.WHITE);
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
        currentPlayer = currentPlayer.equals("X") ? "O" : "X";
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

                // Create the Adversary selection frame
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

    // Will display the window to have selection for symbol and player type
    private static void showPlayerSelection(boolean isHumanVsAI) {
        // Creates a new window for player selection
        JFrame playerFrame = new JFrame("Player Selection");
        playerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        playerFrame.setSize(400, 200);
        // Attaches the listener
        Player playerPanel = new Player(player -> {
            // Closes the window when selected
            playerFrame.dispose();
            // Launch the mode based on selections
            new TicTacToeGUI(player, isHumanVsAI);
        });
        // Add the panel into the frame
        playerFrame.add(playerPanel);
        playerFrame.setVisible(true);
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

    public void makeAiMove(int row, int col, String symbol) {
        buttons[row][col].setText(symbol);
        buttons[row][col].setForeground(playerColor);

        if (checkWinner()) {
            JOptionPane.showMessageDialog(frame, symbol + " wins!");
            updateScore();
            disableBoard();
        } else if (isBoardFull()) {
            JOptionPane.showMessageDialog(frame, "It's a draw!");
            drawCount++;
            drawLabel.setText("Draw: " + drawCount);
        } else {
            switchPlayer();
        }
    }
}