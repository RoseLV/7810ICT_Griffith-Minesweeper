import java.awt.Image;


public abstract class Board {
    protected int numImages;
    protected Image[] images;

    protected String statusText;
    protected boolean inGame, gameWon;
    protected int[] field;

    protected int numMines, minesLeft, uncoveredCells;
    protected int numRows, numCols, totalCells;

    // Board constructor
    public Board() {
        this.statusText = "";
        this.images = this.loadImages();
    }

    protected abstract Image[] loadImages();
    public abstract neighbor[] getNeighbors(int index);

    public abstract boolean isClickOnBoard(int x, int y, int cellSize);
    public abstract int getIndexAt(int x, int y, int cellSize);

    public abstract MinesweeperCell getCellAt(int i, int j, int cellSize);
    public abstract String processClickAt(int index, int mouseButton);

    public abstract boolean isFlagCell(int index);
    public abstract boolean isMineCell(int index);
    public abstract boolean isCoveredCell(int index);
    public abstract boolean isCoveredMineCell(int index);

    public abstract void flagCell(int index);
    public abstract void unflagCell(int index);
    public abstract void uncoverCell(int index);

    public abstract void newGame();
    public abstract void loseGame();
    public abstract void winGame();
    public abstract void updateGameStatus();

    public String getStatusText() {
        return statusText;
    }

    public boolean inGame() {
        return inGame;
    }

    public boolean isGameWon() {
        return gameWon;
    }

    public void find_empty_cells(int position) {
        neighbor[] neighbors = getNeighbors(position);
        for (neighbor n : neighbors) {
            if (n.getLocation() == neighbor.position.OUT_OF_BOUNDS) {
                // ensure valid neighbor cell
                continue;
            }

            int index = n.getIndex();
            if (!isCoveredCell(index) || isCoveredMineCell(index) || isFlagCell(index)) {
                // don't uncover already revealed cell or mine cell
                continue;
            }

            // uncover
            uncoverCell(index);
        }
    }
}
