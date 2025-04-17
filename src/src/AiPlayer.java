/*
AI player class implements different difficulty levels: easy, medium, and hard.
*/
import javax.swing.*;
import java.util.Random;

class AiPlayer extends Player {
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

    //Makes a move based on the difficulty level chosen.
    @Override
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
        if (tryWinningMove(board)) return;
        if (blockOpponentMove(board)) return;
        makeEasyMove(board);
    }

    //Hard Move - uses the minimax algorithm to find the optimal move
    private void makeHardMove(gameBoard board) {

        //Finds the best move
        int[] bestMove = findBestMove(board);
        //plays the best move
        board.updateBoard(bestMove[0], bestMove[1], getSymbol());
    }

    //Check if the AI can win in the next move and makes that move if available.
    private boolean tryWinningMove(gameBoard board) {

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board.isSpotEmpty(i, j)) {
                    //Simulates the move
                    board.updateBoard(i, j, getSymbol());
                    boolean isWin = board.checkWin();
                    //Undo move
                    board.updateBoard(i, j, ' ');
                    if (isWin) {
                        board.updateBoard(i, j, getSymbol());
                        retrun true;
                    }
                }
            }
        }
        return false;
    }

    //Checks if the opponent can win with the next move and blocks them if they can.
    private boolean blockOpponentMove(gameBoard board) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board.isSpotEmpty(i, j)) {
                    //Simulate opponent's move to win
                    board.updateBoard(i, j, opponentSymbol);
                    boolean isWin = board.checkWin();
                    //undo the simulation
                    board.updateBoard(i, j, ' ');
                    //If opponent would win here, blocks them
                    if (isWin) {
                        board.updateBoard(i, j, getSymbol());
                        return true;
                    }
                }
            }
        }
        return false;//Returns false if the AI does not need to block the player.
    }

    //Finds the best move using the minimax algorithm.
    private int[] findBestMove(gameBoard board) {
        int bestScore = Integer.MIN_VALUE; //Initialize with the worst possible score
        int[] bestMove = new int[]{-1, -1}; //Initialize with invalid move

        //Evaluates all moves possible
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if(board.isSpotEmpty(i, j)) {
                    //Try this move
                    board.updateBoard(i, j, getSymbol());
                    //Calculate score for this move (minimax with depth 0, minimizing turn)
                    int score = minimax(board, 0, false);
                    //Undo the move
                    board.updateBoard(i, j, ' ');
                    //Update best move if this score is better.
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

    //Minimax algorithm implementation
    private int minimax(gameBoard board, int depth, boolean isMaximizing) {
        //Base cases
        if (board.checkWin()) {
            //checks if teh board is a win
            //Subtract depth to prefer faster wins
            return isMaximizing ? -10 + depth : 10 - depth;
        }
        if (board.isFull()) {
            return 0; //draw
        }

        if (isMaximizing) {
            //AI trys to maximize score
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board.isSpotEmpty(i, j)) {
                        //Tries move
                        board.updateBoard(i, j, getSymbol());
                        //Recursively evaluate this move
                        int score = minimax(board, depth + 1, false);
                        //Undo the move
                        board.updateBoard(i, j, ' ');
                        //Tracks the best move
                        bestScore = Math.max(score, bestScore);
                    }
                }
            }
            return bestScore;
        }
        else {
            //Opponent's turn - try to minimize score
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board.isSpotEmpty(i, j)) {
                        //Simulate opponent's move
                        board.updateBoard(i, j, opponentSymbol);
                        //Recursively evaluate this move
                        int score = minimax(board, depth + 1, true);
                        //Undo the move
                        board.updateBoard(i, j, ' ');
                        //Track the worst score
                        bestScore = Math.min(score, bestScore);
                    }
                }
            }
            return bestScore;
        }
    }
}


