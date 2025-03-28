import java.util.*;

// Manages game flow
class base {
    private gameBoard board;
    private player currentPlayer;
    private player player1;
    private player player2;

    public base() {
        board = new gameBoard(); // initialize board
        Scanner scanner = new Scanner(System.in);

        // Player 1 name and symbol
        System.out.println("Enter name for Player 1:");
        String name1 = scanner.nextLine();
        System.out.println(name1 + ", choose your symbol (X or O):");
        char symbol1 = scanner.next().charAt(0);

        // Error checking for valid symbol
        while (symbol1 != 'X' && symbol1 != 'O') {
            System.out.println("Invalid choice! Choose either X or O:");
            symbol1 = scanner.next().charAt(0);
        }
        char symbol2 = (symbol1 == 'X') ? 'O' : 'X'; // Player 2 symbol is opposite of player 1 symbol

        // Get name for player 2
        System.out.println("Enter name for Player 2:");
        scanner.nextLine(); // Skip line
        String name2 = scanner.nextLine();

        // Player objects
        player1 = new humanPlayer(name1, symbol1);
        player2 = new humanPlayer(name2, symbol2);
        currentPlayer = player1;
    }

    public void startGame() {
        while (!board.isFull() && !board.checkWin()) { // Game loop runs until board is full or a player wins
            board.displayBoard(); // Display CURRENT board
            currentPlayer.makeMove(board);

            // End game if win detected
            if (board.checkWin()) {
                System.out.println(currentPlayer.getName() + " wins!");
                break;
            }

            // End if draw detected
            if (board.isFull() && !board.checkWin()) {
                System.out.println("It's a draw!");
                break;
            }

            switchTurn(); // Switch turn after each move
        }
        board.displayBoard(); // Display final game board
        System.out.println("Game Over!");
    }

    private void switchTurn() {
        currentPlayer = (currentPlayer == player1) ? player2 : player1; // Swap players
    }
}





