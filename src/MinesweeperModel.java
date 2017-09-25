public class MinesweeperModel {
    public enum GAME_TYPE {
        SQUARE_CELL,
        HEX_CELL,
        COLOR_CELL
    }
    private GAME_TYPE currentGame;
    private Board currentBoard;

    private int NUM_MINES = 10;
    private int NUM_ROWS = 16;
    private int NUM_COLS = 16;


    public MinesweeperModel() {
        startGame(GAME_TYPE.SQUARE_CELL);
    }

    public void startGame(GAME_TYPE newGameType) {
        currentGame = newGameType;
        currentBoard = getGameBoard(newGameType);
        currentBoard.newGame();
    }

    public MinesweeperCell getCellAt(int i, int j, int cellSize) {
        return currentBoard.getCellAt(i, j, cellSize);
    }

    public int getIndexFromCoordinate(int x, int y, int cellSize) {
        return currentBoard.getIndexAt(x, y, cellSize);
    }

    public boolean clickedOnBoard(int x, int y, int cellSize) {
        return currentBoard.isClickOnBoard(x, y, cellSize);
    }

    public String processClickAt(int index, int mouseButton) {
        return currentBoard.processClickAt(index, mouseButton);
    }

    public int getCols() {
        return NUM_COLS;
    }

    public int getRows() {
        return NUM_ROWS;
    }

    public String getStatusText() {
        return currentBoard.getStatusText();
    }

    public boolean gameInProgress() {
        return currentBoard.inGame();
    }

    private Board getGameBoard(GAME_TYPE gameType) {
        switch(gameType) {
            case SQUARE_CELL:
                return new SquareBoard(NUM_MINES, NUM_ROWS, NUM_COLS);
            case HEX_CELL:
                return new HexBoard(NUM_MINES, NUM_ROWS, NUM_COLS);
            case COLOR_CELL:
                return new ColorBoard(NUM_ROWS, NUM_COLS);
            default:
                return null;
        }
    }
}