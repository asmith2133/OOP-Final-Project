/*
AI player class implements different difficulty levels: easy, medium, and hard.
*/
import javax.swing.*;
import java.util.Random;

class AiPlayer {
    private String difficultyLvl; //Stores the difficulty level
    private String aiSymbol; //Stores the symbol of the human player
    private String humanSymbol;
    private TicTacToeGUI game;

    //Constructor for the AI player
    public AiPlayer(String difficultyLvl, String aiSymbol, TicTacToeGUI game) {

        this.difficultyLvl = difficultyLvl;
        this.aiSymbol = aiSymbol;
        //Determine opponent's symbol
        this.humanSymbol = aiSymbol.equals("X") ? "O" : "X";
        this.game = game;
    }

    //Makes a move based on the difficulty level chosen
    public void makeMove() {
        switch (difficultyLvl) {
            case "Easy" :
                makeEasyMove(); //Uses random move
                break;
            case "Medium" :
                makeMediumMove();//Uses basic block strategy
                break;
            case "Hard" :
                makeHardMove(); //Uses a minimax algorithm
                break;
        }
    }

    //Easy difficulty move - makes a random move in an open space.
    private void makeEasyMove() {
        Random rand = new Random();
        int row, column;
        do {
            row = rand.nextInt(game.getBoardSize());
            column = rand.nextInt(game.getBoardSize());
        } while (!game.isSpotEmpty(row, column));

        game.makeAiMove(row, column, aiSymbol);
    }

    //Medium move - tries to win if possible, tries to block opponent if needed, otherwise just makes an easy move.
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

    //Hard Move - uses the minimax algorithm to find the optimal move
    private void makeHardMove() {
        int[] bestMove = findBestMove();
        game.makeAiMove(bestMove[0], bestMove[1], aiSymbol);
    }

    private boolean wouldWin(int row, int col, String symbol) {
        // Simulate the move
        game.getButton(row, col).setText(symbol);
        boolean win = game.checkWinner();
        // Undo the move
        game.getButton(row, col).setText("");
        return win;
    }

    //Finds the best move using minimax algorithm.
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


