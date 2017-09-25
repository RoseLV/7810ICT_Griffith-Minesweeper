import java.awt.Image;
import java.awt.event.MouseEvent;
import java.util.Random;
import javax.swing.ImageIcon;

public class SquareBoard extends Board {
    protected final int COVER_FOR_CELL = 10;
    protected final int MARK_FOR_CELL = 10;
    protected final int EMPTY_CELL = 0;
    protected final int MINE_CELL = 9;
    protected final int COVERED_MINE_CELL = MINE_CELL + COVER_FOR_CELL;
    protected final int MARKED_MINE_CELL = COVERED_MINE_CELL + MARK_FOR_CELL;

    protected final int DRAW_MINE = 9;
    protected final int DRAW_COVER = 10;
    protected final int DRAW_MARK = 11;
    protected final int DRAW_WRONG_MARK = 12;


    public SquareBoard(int numMines, int numRows, int numCols) {
        super();
        this.numMines = numMines;
        this.numRows = numRows;
        this.numCols = numCols;
        this.totalCells = numRows * numCols;
    }

    @Override
    public void newGame() {
        inGame = true;
        gameWon = false;

        uncoveredCells = 0;
        minesLeft = numMines;
        statusText = String.format("Mines left: %2d", minesLeft);

        totalCells = numRows * numCols;
        field = new int[totalCells];
        for (int i = 0; i < totalCells; i++)
            field[i] = COVER_FOR_CELL;

        Random random = new Random();
        int i = 0, position, index;
        while (i < numMines) {
            position = (int) (totalCells * random.nextDouble());

            if (position < totalCells && !isCoveredMineCell(position)) {
                field[position] = COVERED_MINE_CELL;
                i++;

                neighbor[] neighbors = getNeighbors(position);
                for (neighbor n : neighbors) {
                    index = n.getIndex();
                    if (n.getLocation() == neighbor.position.OUT_OF_BOUNDS || isCoveredMineCell(index)) {
                        continue;
                    }

                    field[index]++;
                }
            }
        }
    }

    @Override
    public neighbor[] getNeighbors(int index) {
        int num_neighbors = 8;
        neighbor[] neighbors = new neighbor[num_neighbors];

        int row = index / numCols;
        int col = index % numCols;

        int nbr_row = -1;
        int nbr_col = -1;
        for (int i = 0; i < num_neighbors; i++) {
            int newRow = row + nbr_row;
            int newCol = col + nbr_col;
            if (newRow > -1 && newRow < numRows && newCol > -1 && newCol < numCols) {
                neighbors[i] = new neighbor(newRow * numCols + newCol, neighbor.position.IN_BOUNDS);
            } else {
                neighbors[i] = new neighbor(-1, neighbor.position.OUT_OF_BOUNDS);
            }

            if (nbr_row == 0 && nbr_col == -1) {
                // skip position at 0,0 (this position)
                nbr_col = 1;
                continue;
            }

            if (nbr_col == 1) {
                nbr_row++;
                nbr_col = -1;
                continue;
            }

            nbr_col++;
        }

        return neighbors;
    }

    @Override
    public Image[] loadImages() {
        numImages = 13;
        Image[] images = new Image[numImages];
        for (int i = 0; i < numImages; i++) {
            images[i] = (new ImageIcon(this.getClass().getResource("s" + i + ".png"))).getImage().getScaledInstance(13, 13, 0);
        }
        return images;
    }

    @Override
    public MinesweeperCell getCellAt(int i, int j, int cellSize) {
        int index = i * numCols + j;
        int cell = field[index];

        if (inGame) {
            // show current status
            if (cell > COVERED_MINE_CELL)
                cell = DRAW_MARK;
            else if (cell > MINE_CELL) {
                cell = DRAW_COVER;
            }
        } else {
            // show the answer
            if (cell == COVERED_MINE_CELL) {
                cell = DRAW_MINE;
            } else if (cell == MARKED_MINE_CELL) {
                cell = DRAW_MARK;
            } else if (cell > COVERED_MINE_CELL) {
                cell = DRAW_WRONG_MARK;
            } else if (cell > MINE_CELL) {
                cell = DRAW_COVER;
            }
        }

        return new MinesweeperCell(j * cellSize, i * cellSize, images[cell]);
    }

    @Override
    public int getIndexAt(int x, int y, int cellSize) {
        int cCol = x / cellSize;
        int cRow = y / cellSize;
        return cRow * numCols + cCol;
    }

    @Override
    public String processClickAt(int index, int mouseButton) {
        if (!inGame) {
            newGame();
            return statusText;
        }

        if (!isCoveredCell(index))
            return statusText;

        if (mouseButton == MouseEvent.BUTTON1) {
            // left click
            if (!isFlagCell(index))
                uncoverCell(index);

            if (isMineCell(index))
                loseGame();
        }

        if (mouseButton == MouseEvent.BUTTON3) {
            // right click
            if(!isFlagCell(index))
                flagCell(index);
            else
                unflagCell(index);
        }

        updateGameStatus();
        return statusText;
    }

    @Override
    public void loseGame() {
        inGame = false;
        gameWon = false;
        statusText = "Boom! :x";
    }

    @Override
    public void winGame() {
        inGame = false;
        gameWon = true;
        statusText = "Win! :)";
    }

    @Override
    public void updateGameStatus() {
        if (!inGame)
            return;

        statusText = String.format("Mines left: %2d", minesLeft);

        if (minesLeft > 0)
            // havent flagged all mines
            return;

        if (uncoveredCells == totalCells - numMines) {
            // correctly flagged all mines
            winGame();
        }
    }

    @Override
    public boolean isClickOnBoard(int x, int y, int cellSize) {
        return true;
        // return (i < numCols * cellSize) && (j < numRows * cellSize);
    }

    @Override
    public boolean isFlagCell(int index) {
        return field[index] >= COVER_FOR_CELL + MARK_FOR_CELL;
    }

    @Override
    public boolean isMineCell(int index) {
        return field[index] == MINE_CELL;
    }

    @Override
    public boolean isCoveredCell(int index) {
        return field[index] >= COVER_FOR_CELL;
    }

    @Override
    public boolean isCoveredMineCell(int index) {
        return field[index] == COVERED_MINE_CELL;
    }

    @Override
    public void flagCell(int index) {
        if (minesLeft == 0)
            return;

        minesLeft--;
        field[index] += MARK_FOR_CELL;
    }

    @Override
    public void unflagCell(int index) {
        minesLeft++;
        field[index] -= MARK_FOR_CELL;
    }

    @Override
    public void uncoverCell(int index) {
        uncoveredCells++;
        field[index] -= COVER_FOR_CELL;
        if (field[index] == EMPTY_CELL)
            find_empty_cells(index);
    }
}

