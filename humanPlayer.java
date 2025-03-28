import java.util.Scanner;

    // humanPlayer class
    class humanPlayer extends player {
        private Scanner scanner;

        public humanPlayer(String name, char symbol) {
            super(name, symbol);
            scanner = new Scanner(System.in);
        }

        @Override
        public void makeMove(gameBoard board) {
            int row, col; // Store row and column of player's move
            while (true) {
                // Prompt user for row and column to place letter
                System.out.println(getName() + ", enter row (0-2):");
                row = scanner.nextInt();
                System.out.println(getName() + ", enter column (0-2):");
                col = scanner.nextInt();

                // Ensures the move is valid (cell is empty)
                if (row >= 0 && row < 3 && col >= 0 && col < 3 && board.isSpotEmpty(row, col)) {
                    board.updateBoard(row, col, getSymbol()); // Update the board with player move
                    break; // Exit loop once move is made
                } else {
                    // Error message if cell is occupied
                    System.out.println("Invalid move. Try again.");
                }
            }
        }
    }


