import java.util.Arrays;

// Represents the Tic-Tac-Toe board
class gameBoard {
    private char[][] board; // 3x3 board
    private static final int SIZE = 3;

    public gameBoard() {
        board = new char[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            Arrays.fill(board[i], ' '); // Initialize board with empty spaces
        }
    }

    public void displayBoard() {
        for (char[] row : board) {
            System.out.println("| " + row[0] + " | " + row[1] + " | " + row[2] + " |"); // Display board
        }
    }

    public boolean updateBoard(int row, int col, char symbol) {
        if (board[row][col] == ' ') { // Check if cell is empty
            board[row][col] = symbol;
            return true;
        }
        return false; // Move not allowed
    }

    // Adds a method to GameBoard class to check if a specific spot is empty
    public boolean isSpotEmpty(int row, int col) {
        return board[row][col] == ' ';  // Returns true if the spot is empty (i.e., ' '), false otherwise
    }

    public boolean isFull() {
        for (char[] row : board) {
            for (char cell : row) {
                if (cell == ' ') return false; // If any cell is empty, board is not full
            }
        }
        return true;
    }

    public boolean checkWin() {
        for (int i = 0; i < SIZE; i++) {
            if (board[i][0] != ' ' && board[i][0] == board[i][1] && board[i][1] == board[i][2]) return true; // Row win
            if (board[0][i] != ' ' && board[0][i] == board[1][i] && board[1][i] == board[2][i]) return true; // Column win
        }
        return (board[0][0] != ' ' && board[0][0] == board[1][1] && board[1][1] == board[2][2]) || // Diagonal win
                (board[0][2] != ' ' && board[0][2] == board[1][1] && board[1][1] == board[2][0]);
    }
}