abstract class player {
    protected String name;
    protected char symbol;

    public player(String name, char symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    public abstract void makeMove(gameBoard board); // Abstract method for making a move

    public String getName() {
        return name;
    }
    public char getSymbol() {
        return symbol;
    }
}
