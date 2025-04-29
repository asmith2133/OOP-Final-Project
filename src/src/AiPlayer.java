/*
AI player class implements different difficulty levels: easy, medium, and hard.
 */

import javax.swing.*;
import java.util.Random;

public class AiPlayer {
    private String difficulty = "Easy";
    private String aiSymbol;
    private String humanSymbol;
    private TicTacToeGUI game;


    public AiPlayer(String aiSymbol, TicTacToeGUI game) {
        this.difficulty = "Easy";
        this.aiSymbol = aiSymbol;
        this.humanSymbol = aiSymbol.equals("X") ? "O" : "X";
        this.game = game;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * Makes a move based on the selected difficulty level.
     */
    public void makeMove() {
        SwingUtilities.invokeLater(() -> {
            switch (difficulty) {
                case "Easy":
                    makeEasyMove();
                    break;
                case "Medium":
                    makeMediumMove();
                    break;
                case "Hard":
                    makeHardMove();
                    break;
            }
        });
    }

    /**
     * Easy difficulty - makes random moves.
     */
    private void makeEasyMove() {
        Random rand = new Random();
        int row, col;
        do {
            row = rand.nextInt(game.getBoardSize());
            col = rand.nextInt(game.getBoardSize());
        } while (!game.isSpotEmpty(row, col));

        game.makeAiMove(row, col, aiSymbol);
    }

    /**
     * Medium difficulty - tries to win or block opponent.
     */
    private void makeMediumMove() {
        int needed = getNeededToWin() - 1;

        //try to win
        if (tryCompleteLine(aiSymbol, needed)) return;

        //block opponent
        if (tryCompleteLine(humanSymbol, needed)) return;

        // Otherwise make a random move
        makeEasyMove();
    }

    private boolean tryCompleteLine(String symbol, int needed) {
        for (int i = 0; i < game.getBoardSize(); i++){
            for (int j = 0; j < game.getBoardSize(); j++){
                if (game.isSpotEmpty(i, j) && wouldCompleteLine(i, j, symbol, needed)) {
                    game.makeAiMove(i, j, aiSymbol);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean wouldCompleteLine(int row, int column, String symbol, int needed) {
        return checkPartialLine(row, column, 1, 0, symbol, needed) ||
                checkPartialLine(row, column, 0, 1, symbol, needed) ||
                checkPartialLine(row, column, 1, 1, symbol, needed) ||
                checkPartialLine(row, column, 1, -1, symbol, needed);
    }

    private boolean checkPartialLine(int row, int col, int dx, int dy, String symbol, int needed) {
        int size = game.getBoardSize();
        int total = 1; // Count the current (empty) spot we're checking

        // Check in positive direction
        for (int i = 1; i < needed; i++) {
            int x = row + i * dx;
            int y = col + i * dy;
            if (x < 0 || x >= size || y < 0 || y >= size) break;
            String spot = game.getButton(x, y).getText();
            if (spot.equals(symbol)) {
                total++;
            } else if (!spot.isEmpty()) {
                break; // Opponent's symbol blocks the line
            }
        }

        // Check in negative direction
        for (int i = 1; i < needed; i++) {
            int x = row - i * dx;
            int y = col - i * dy;
            if (x < 0 || x >= size || y < 0 || y >= size) break;
            String spot = game.getButton(x, y).getText();
            if (spot.equals(symbol)) {
                total++;
            } else if (!spot.isEmpty()) {
                break; // Opponent's symbol blocks the line
            }
        }

        return total >= needed;
    }

    /**
     * Hard difficulty - uses minimax algorithm for optimal moves.
     */
    private void makeHardMove() {
        int[] bestMove = findBestMove();
        game.makeAiMove(bestMove[0], bestMove[1], aiSymbol);
    }

    /**
     * Checks if placing a symbol at (row, col) would result in a win.
     */
    private boolean wouldWin(int row, int column, String symbol, boolean simulated) {
        String originalText = game.getButton(row, column).getText();
        // Simulate the move
        game.getButton(row, column).setText(symbol);
        int neededToWin = getNeededToWin();
        boolean win = checkWinAround(row, column, symbol, neededToWin);
        // Undo the move
        game.getButton(row, column).setText(originalText);
        return win;
    }

    private int getNeededToWin() {
        int size = game.getBoardSize();
        return size >= 5 ? 5 : (size == 4 ? 4 : 3);
    }

    private boolean checkWinAround(int row, int column, String symbol, int neededToWin) {
        return checkDirection(row,column, 1, 0, symbol, neededToWin)||
               checkDirection(row,column, 0, 1, symbol, neededToWin)||
               checkDirection(row,column, 1, 1, symbol, neededToWin)||
               checkDirection(row,column, 1, -1, symbol, neededToWin);
    }

    private boolean checkDirection(int row, int column, int dx, int dy, String symbol, int neededToWin) {
        int count = 1;
        int size = game.getBoardSize();

        //Search in positive direction
        for (int i = 1; i < neededToWin; i++) {
            int x = row + i * dx;
            int y = column + i * dy;
            if (x < 0 || x >= size || y < 0 || y >= size ||
                    !game.getButton(x, y).getText().equals(symbol)) {
                break;
            }
            count++;
        }

        // Search in negative direction
        for (int i = 1; i < neededToWin; i++) {
            int x = row - i * dx;
            int y = column - i * dy;
            if (x < 0 || x >= size || y < 0 || y >= size ||
                    !game.getButton(x, y).getText().equals(symbol)) {
                break;
            }
            count++;
        }

        return count >= neededToWin;
    }

    /**
     * Finds the best move using minimax algorithm.
     */
    private int[] findBestMove() {
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = new int[]{-1, -1};

        for (int i = 0; i < game.getBoardSize(); i++) {
            for (int j = 0; j < game.getBoardSize(); j++) {
                if (game.isSpotEmpty(i, j)) {
                    // Try the move
                    game.getButton(i, j).setText(aiSymbol);
                    int score = minimax(0, false);
                    // Undo the move
                    game.getButton(i, j).setText("");

                    if (score > bestScore) {
                        bestScore = score;
                        bestMove[0] = i;
                        bestMove[1] = j;
                    }
                }
            }
        }
        return bestMove;
    }

    /**
     * Minimax algorithm implementation.
     */
    private int minimax(int depth, boolean isMaximizing) {
        // Check for terminal states
        if (game.checkWinner(true)) {
            return isMaximizing ? -100 + depth : 100 - depth;
        }

        if (game.isBoardFull()) {
            return 0;
        }

        if (depth >= (game.getBoardSize() > 3 ? 3 : 5)) {
            return evaluateBoard();
        }

        if (isMaximizing) {
            // AI's turn - maximize score
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < game.getBoardSize(); i++) {
                for (int j = 0; j < game.getBoardSize(); j++) {
                    if (game.isSpotEmpty(i, j)) {
                        game.getButton(i, j).setText(aiSymbol);
                        int score = minimax(depth + 1, false);
                        game.getButton(i, j).setText("");
                        bestScore = Math.max(score, bestScore);
                    }
                }
            }
            return bestScore;
        } else {
            // Human's turn - minimize score
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < game.getBoardSize(); i++) {
                for (int j = 0; j < game.getBoardSize(); j++) {
                    if (game.isSpotEmpty(i, j)) {
                        game.getButton(i, j).setText(humanSymbol);
                        int score = minimax(depth + 1, true);
                        game.getButton(i, j).setText("");
                        bestScore = Math.min(score, bestScore);
                    }
                }
            }
            return bestScore;
        }
    }

    private int evaluateBoard() {
        int score = 0;
        int needed = getNeededToWin();

        for (int i = 0; i < game.getBoardSize(); i++) {
            for (int j = 0; j < game.getBoardSize(); j++) {
                if (game.isSpotEmpty(i, j)) {
                    if (wouldWin(i, j, aiSymbol, true)) score += 10;
                    if (wouldWin(i, j, humanSymbol, true)) score -= 8;
                }
            }
        }
        return score;
    }
}
