/*
AI player class implements different difficulty levels: easy, medium, and hard.
 */

import java.util.Random;

public class AiPlayer {
    private String difficulty = "Easy";
    private String aiSymbol;
    private String humanSymbol;
    private TicTacToeGUI game;

    public AiPlayer(String aiSymbol, TicTacToeGUI game) {
        this.aiSymbol = aiSymbol;
        this.humanSymbol = aiSymbol.equals("X") ? "O" : "X";
        this.game = game;
    }

    public AiPlayer(String difficulty, String aiSymbol, TicTacToeGUI game) {
        this.difficulty = difficulty;
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
        // Try to win if possible
        for (int i = 0; i < game.getBoardSize(); i++) {
            for (int j = 0; j < game.getBoardSize(); j++) {
                if (game.isSpotEmpty(i, j)) {
                    if (wouldWin(i, j, aiSymbol)) {
                        game.makeAiMove(i, j, aiSymbol);
                        return;
                    }
                }
            }
        }

        // Block opponent if they can win
        for (int i = 0; i < game.getBoardSize(); i++) {
            for (int j = 0; j < game.getBoardSize(); j++) {
                if (game.isSpotEmpty(i, j)) {
                    if (wouldWin(i, j, humanSymbol)) {
                        game.makeAiMove(i, j, aiSymbol);
                        return;
                    }
                }
            }
        }

        // Otherwise make a random move
        makeEasyMove();
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
    private boolean wouldWin(int row, int col, String symbol) {
        // Simulate the move
        game.getButton(row, col).setText(symbol);
        boolean win = game.checkWinner();
        // Undo the move
        game.getButton(row, col).setText("");
        return win;
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
        if (game.checkWinner()) {
            return isMaximizing ? -10 + depth : 10 - depth;
        }

        if (game.isBoardFull()) {
            return 0;
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
}
