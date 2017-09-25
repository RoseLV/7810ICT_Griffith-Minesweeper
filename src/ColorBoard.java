import java.awt.Image;
import java.util.Random;
import javax.swing.ImageIcon;

public class ColorBoard extends SquareBoard {
    private int N_COLORS;

    public ColorBoard(int numRows, int numCols) {
        super(0, numRows, numCols);
        N_COLORS = 5;
    }

    @Override
    public Image[] loadImages() {
        numImages = 13;
        Image[] images = new Image[numImages];
        for (int i = 0; i < numImages; i++) {
            images[i] = (new ImageIcon(this.getClass().getResource("c" + i + ".png"))).getImage().getScaledInstance(13, 13, 0);
        }
        return images;
    }

    @Override
    public void newGame() {
        numMines = 0;

        inGame = true;
        gameWon = false;

        totalCells = numRows * numCols;
        field = new int[totalCells];
        for (int i = 0; i < totalCells; i++)
            field[i] = COVER_FOR_CELL;

        Random random = new Random();
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                int num = random.nextInt(N_COLORS) + 1;
                field[i * numCols + j] += num;
            }
        }

        for (int i = 0; i < numRows - 1; i++) {
            for (int j = 0; j < numCols - 1; j++) {
                int idx = i * numCols + j;
                int bottom = (i + 1) * numCols + j;
                int right = i * numCols + j + 1;
                if (field[idx] == field[bottom] || field[idx] == field[right])
                    numMines++;
            }
        }

        uncoveredCells = 0;
        minesLeft = numMines;
        statusText = String.format("Mines left: %2d", minesLeft);
    }

    @Override
    public boolean isMineCell(int index) {
        if (field[index] > COVER_FOR_CELL)
            return false;
        if (index-1 > -1 && field[index] == field[index-1])
            return true;
        if (index-numCols > -1 && field[index] == field[index-numCols])
            return true;
        if (index+1 < field.length && field[index] == field[index+1])
            return true;
        if (index+numCols < field.length && field[index] == field[index+numCols])
            return true;
        return false;
    }

    @Override
    public MinesweeperCell getCellAt(int i, int j, int cellSize) {
        MinesweeperCell original = super.getCellAt(i, j, cellSize);
        if (!isCoveredCell(i * numCols + j))
            original.setText(Integer.toString(numNeighborMines(i, j)));
        return original;
    }

    private int numNeighborMines(int i, int j) {
        int mines = 0;
        int thisColor = field[i * numCols + j];
        if (thisColor == getColor(i - 1, j))
            mines++;
        if (thisColor == getColor(i + 1, j))
            mines++;
        if (thisColor == getColor(i, j + 1))
            mines++;
        if (thisColor == getColor(i, j - 1))
            mines++;
        return mines;
    }

    private int getColor(int i, int j) {
        int index = i * numCols + j;
        if (index < 0 || index >= field.length)
            return -1;
        int value = field[index];
        if (isFlagCell(index))
            value -= MARK_FOR_CELL;
        if (isCoveredCell(index))
            value -= COVER_FOR_CELL;
        return value;
    }
}
