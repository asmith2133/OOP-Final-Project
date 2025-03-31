class AiPlayer extends Player {
    private String difficultyLvl;
    private char opponentSymbol;

    public AiPlayer(String name, char opponentSymbol, String difficultyLvl) {
        super(name, opponentSymbol);
        this.difficultyLvl = difficultyLvl;
        this.opponentSymbol = (opponentSymbol == 'X') ? 'O' : 'X';
    }

    @Override
    public void makeMove(gameBoard board) {
        switch (difficultyLvl) {
            case "Easy" -> makeEasyMove(board);
            case "Medium" -> makeMediumMove(board);
            case "Hard" -> makeHardMove(board);
        }
    }

    private void makeEasyMove(gameBoard board) {
        java.util.Random rand = new java.util.Random();
        int row, column;
        do {
            row = rand.nextInt(3);
            column = rand.nextInt(3);
        } while (!board.isSpotEmpty(row, column));
        board.updateBoard(row, column, getSymbol());
    }

    private void makeMediumMove(gameBoard board) {
        if (tryWinningMove(board)) return;
        if (blockOpponentMove(board)) return;
        makeEasyMove(board);
    }

    private void makeHardMove(gameBoard board) {

        int[] bestMove = findBestMove(board);
        board.updateBoard(bestMove[0], bestMove[1], getSymbol());
    }

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

    private boolean blockOpponentMove(gameBoard board) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board.isSpotEmpty(i, j)) {
                    board.updateBoard(i, j, opponentSymbol);
                    boolean isWin = board.checkWin();

                    board.updateBoard(i, j, ' ');
                    if (isWin) {
                        board.updateBoard(i, j, getSymbol());
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private int[] findBestMove(gameBoard board) {
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = new int[]{-1, -1};

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if(board.isSpotEmpty(i, j)) {

                    board.updateBoard(i, j, getSymbol());
                    int score = minimax(board, 0, false);

                    board.updateBoard(i, j, ' ');
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

    private int minimax(gameBoard board, int depth, boolean isMaximizing) {
        if (board.checkWin()) {
            return isMaximizing ? -10 + depth : 10 - depth;
        }
        if (board.isFull()) {
            return 0;
        }

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board.isSpotEmpty(i, j)) {
                        board.updateBoard(i, j, getSymbol());
                        int score = minimax(board, depth + 1, false);
                        board.updateBoard(i, j, ' ');
                        bestScore = Math.max(score, bestScore);
                    }
                }
            }
            return bestScore;
        }
        else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board.isSpotEmpty(i, j)) {
                        board.updateBoard(i, j, opponentSymbol);
                        int score = minimax(board, depth + 1, true);
                        board.updateBoard(i, j, ' ');
                        bestScore = Math.min(score, bestScore);
                    }
                }
            }
            return bestScore;
        }
    }
}
