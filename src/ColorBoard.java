import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class ColorBoard extends Board {
    int field[][];

    public ColorBoard(int n_color, int n_rows, int n_cols, JLabel statusbar, JLabel timeBar) {
        super(statusbar, timeBar);
        this.N_MINES = n_color;
        this.N_ROWS = n_rows;
        this.N_COLS = n_cols;
        all_cells = N_ROWS * N_COLS;
        newGame();
    }



    public int[][] generateNeighbors() {

        for (int i = 0; i < N_ROWS; i++) {
            for (int j = 0; j < N_COLS; j++) {
                Random random = new Random();
                int num = random.nextInt(8) + 1;
                //random.nextInt(n);
                // 生成的随即数为0~n-1，所以后边+1，转换一下就成1到8的随机整数
                field[i][j] = num;
            }
        }
        System.out.println(field);
        return field;
    }


    @Override
    public Image[] loadImages() {
        /**
         * Load images into the array ima. The images are named h0.png, h1.png ... h12.png.
         */
        Image[] images = new Image[NUM_IMAGES];
        for (int i = 0; i < NUM_IMAGES; i++) { // 12 pictures in total
            images[i] = (new ImageIcon(this.getClass().getResource("c" + i + ".png"))).getImage();
        }
        return images;
    }

    public void findPairs(){


    }

    // same as generateneighbors, this should be find neighbor in 8 directions
    @Override
    public neighbor[] getNeighbors(int index) {

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


