import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SquareBoard extends Board {
    // constructor
    public SquareBoard(int n_mines, int n_rows, int n_cols, JLabel statusbar, JLabel timeBar) {
        //
        super(statusbar, timeBar);
        this.N_MINES = n_mines;
        this.N_ROWS = n_rows;
        this.N_COLS = n_cols;
        all_cells = N_ROWS * N_COLS;
        newGame();
    }

    @Override
    public Image[] loadImages() {
        Image[] images = new Image[NUM_IMAGES];
        for (int i = 0; i < NUM_IMAGES; i++) { // 12 pictures in total
            images[i] = (new ImageIcon(this.getClass().getResource("s" + i + ".png"))).getImage();
        }
        return images;
    }

    @Override
    public neighbor[] getNeighbors(int index) {
        // 2d [i][j] to 1d [k] = i * N_COLS + j
        // 1d[k] to 2d[i][j]
        // i = k / N_COLS
        // j = k % N_COLS

        int num_neighbors = 8;
        neighbor[] neighbors = new neighbor[num_neighbors];

        int row = index / N_COLS;
        int col = index % N_COLS;

        int nbr_row = -1;
        int nbr_col = -1;
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

            if (nbr_col == 1) {
                nbr_row++;
                nbr_col = -1;
                continue;
            }

            nbr_col++;
        }

        return neighbors;
    }
}

