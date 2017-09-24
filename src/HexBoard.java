import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class HexBoard extends Board {

    protected int N_MINES;
    protected int N_COLS;
    protected int N_ROWS;

    public HexBoard(int N_MINES, int N_COLS, int N_ROWS, JLabel statusbar, JLabel timeBar) {
        super(N_COLS, N_ROWS, statusbar, timeBar);
        this.N_MINES = N_MINES;
    }

    @Override
    public Image[] loadImages() {
        /**
         * Load images into the array ima. The images are named h0.png, h1.png ... h12.png.
         */
        Image[] images = new Image[NUM_IMAGES];
        for (int i = 0; i < NUM_IMAGES; i++) { // 12 pictures in total
            images[i] = (new ImageIcon(this.getClass().getResource("h" + i + ".png"))).getImage();
        }
        return images;
    }

    @Override
    public neighbor[] getNeighbors(int index) {
        // 2d [i][j] to 1d [k] = i * N_COLS + j
        // 1d[k] to 2d[i][j]
        // i = k / N_COLS
        // j = k % N_COLS

        int num_neighbors = 6;
        neighbor[] neighbors = new neighbor[num_neighbors];

        int row = index / N_COLS;
        int col = index % N_COLS;

        boolean evenRow = row % 2 == 0;
        int nbr_row = -1;
        int nbr_col = evenRow ? 0 : -1;
        for (int i = 0; i < num_neighbors; i++) {
            int newRow = row + nbr_row;
            int newCol = col + nbr_col;
            if (newRow > -1 && newRow < N_ROWS && newCol > -1 && newCol < N_COLS) {
                neighbors[i] = new neighbor(newRow * N_COLS + newCol, neighbor.position.IN_BOUNDS);
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
    public void drawImage(Graphics g, Image img, int i, int j) {
        int extraX = i % 2 == 0 ? 6 : 0;
        g.drawImage(img, (j * CELL_SIZE) + extraX, (i * CELL_SIZE), this);

    }

    @Override
    public int getIndexFromMouseEvent(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        // get the current cell's row number
        int cRow = y / CELL_SIZE;   // CELL_SIZE = 20
        // odd row stay, even row move left 10.
        x -= cRow % 2 == 0 ? 6 : 0;
        // get the current cell's column number
        int cCol = x / CELL_SIZE;
        return cRow * N_COLS + cCol;
    }
}