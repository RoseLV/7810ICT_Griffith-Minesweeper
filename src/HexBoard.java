import java.awt.Image;
import javax.swing.ImageIcon;

public class HexBoard extends SquareBoard {
    public HexBoard(int numMines, int numRows, int numCols) {
        super(numMines, numRows, numCols);
    }

    @Override
    public Image[] loadImages() {
        numImages = 13;
        Image[] images = new Image[numImages];
        for (int i = 0; i < numImages; i++) { // 12 pictures in total
            images[i] = (new ImageIcon(this.getClass().getResource("h" + i + ".png"))).getImage();
        }
        return images;
    }

    @Override
    public neighbor[] getNeighbors(int index) {
        int num_neighbors = 6;
        neighbor[] neighbors = new neighbor[num_neighbors];

        int row = index / numCols;
        int col = index % numCols;

        boolean evenRow = row % 2 == 0;
        int nbr_row = -1;
        int nbr_col = evenRow ? 0 : -1;
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

            if (nbr_row == 0 && nbr_col == 1) {
                nbr_row++;
                nbr_col = evenRow ? 0 : -1;
                continue;
            }

            if (nbr_col == (evenRow ? 1 : 0)) {
                nbr_row++;
                nbr_col = nbr_row == 0 ? -1 : evenRow ? 0 : -1;
                continue;
            }

            nbr_col++;
        }
        return neighbors;
    }

    @Override
    public int getIndexAt(int x, int y, int cellSize) {
        // get the current cell's row number
        int cRow = y / cellSize;
        // odd row stay, even row move left 10.
        int cCol = (x - (cRow % 2 == 0 ? 6 : 0)) / cellSize;

        return cRow * numCols + cCol;
    }

    @Override
    public MinesweeperCell getCellAt(int i, int j, int cellSize) {
        MinesweeperCell original = super.getCellAt(i, j, cellSize);
        int newX = original.getX() + (i % 2 == 0 ? 6 : 0);
        return new MinesweeperCell(newX, original.getY(), original.getImage());
    }
}
