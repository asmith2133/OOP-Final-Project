import javax.swing.*;
import java.awt.*;

public class TicTacToeGUI {
    private JFrame frame;
    private JPanel boardPanel, controlPanel;
    private JButton[][] buttons;
    private JButton resetButton, quitButton;
    private JComboBox<String> difficultyBox, boardColorBox, playerColorBox, boardSizeBox;
    private JLabel xScoreLabel, oScoreLabel, drawLabel;

    public TicTacToeGUI() {
        frame = new JFrame("Tic-Tac-Toe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLayout(new BorderLayout());

        // Control Panel (Top)
        controlPanel = new JPanel(new GridLayout(2, 5));
        difficultyBox = new JComboBox<>(new String[]{"Easy", "Medium", "Hard"});

        boardColorBox = new JComboBox<>(new String[]{"Gray", "White", "Blue"});
        boardColorBox.setPreferredSize(new Dimension(100, 30));

        playerColorBox = new JComboBox<>(new String[]{"Black", "Red", "Green"});
        playerColorBox.setPreferredSize(new Dimension(100, 30));

        boardSizeBox = new JComboBox<>(new String[]{"3x3", "4x4", "5x5"});
        boardSizeBox.setPreferredSize(new Dimension(100, 30));

        xScoreLabel = new JLabel("X: 0", SwingConstants.CENTER);
        drawLabel = new JLabel("Draw: 0", SwingConstants.CENTER);
        oScoreLabel = new JLabel("O: 0", SwingConstants.CENTER);

        JPanel scorePanel = new JPanel(new GridLayout(1, 3));
        scorePanel.add(xScoreLabel);
        scorePanel.add(drawLabel);
        scorePanel.add(oScoreLabel);

        controlPanel.add(difficultyBox);
        controlPanel.add(boardColorBox);
        controlPanel.add(playerColorBox);
        controlPanel.add(boardSizeBox);
        controlPanel.add(scorePanel);

        frame.add(controlPanel, BorderLayout.NORTH);

        // Board Panel (Center)
        boardPanel = new JPanel(new GridLayout(3, 3));
        buttons = new JButton[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setFont(new Font("Arial", Font.BOLD, 40));
                boardPanel.add(buttons[i][j]);
            }
        }
        frame.add(boardPanel, BorderLayout.CENTER);

        // Bottom Panel (Reset and Quit Buttons)
        JPanel bottomPanel = new JPanel(new FlowLayout());
        resetButton = new JButton("Reset");
        quitButton = new JButton("Quit");
        bottomPanel.add(resetButton);
        bottomPanel.add(quitButton);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        // Display the frame
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TicTacToeGUI::new);
    }
}
