import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class ColorBoard extends Board {
    // lots of constant defined in the Board need to be changed in this game extension; because
    // squareBoard: all empty cells have same value; however, colorBoard: different colors have different value;
    // squareBoard: empty mine cells with 1-8 has 8 values;  colorBoard: different color with 1-4 has 8 values;
    // squareBoard: flag, wrong flag, cover;  colorBoard: flag, wrong flag, cover;
    // squareBoard: bomb;

    protected int N_COLORS; //protected int N_COLORS = 5;

    public ColorBoard(int n_rows, int n_cols, JLabel statusbar, JLabel timeBar) {
        super(statusbar, timeBar);
        this.N_ROWS = n_rows;
        this.N_COLS = n_cols;
        all_cells = N_ROWS * N_COLS;
        NUM_IMAGES = 9;
        N_COLORS = 9;
    }

    // TODO: if use image, need to use 4*4 + 3 = 19 images?
    @Override
    public Image[] loadImages() {
        /**
         * Load images into the array ima. The images are named h0.png, h1.png ... h12.png.
         */
        Image[] images = new Image[NUM_IMAGES];
        for (int i = 0; i < NUM_IMAGES; i++) { // 4 pictures in total
            images[i] = (new ImageIcon(this.getClass().getResource("c" + i + ".png"))).getImage();
        }
        return images;
    }

    // this function generate random number from 0-4 for all cells in the Board
    // Why it's not used? -- change generateNumber To newGame;
    @Override
    public void newGame() {

        int all_cells = N_ROWS * N_COLS; // 16 * 16
        field = new int[all_cells];

        for (int i = 0; i < all_cells; i++)
            field[i] = COVER_FOR_CELL;

        for (int i = 0; i < N_ROWS; i++) {
            for (int j = 0; j < N_COLS; j++) {
                Random random = new Random();

                int num = random.nextInt(5) + 1;
                field[i * N_COLS + j] += num;
            }
        }
        mines_left = find_number_of_neighbor_mines();
        inGame = true;
        statusbar.setText(Integer.toString(mines_left));
        timeBar.setText("0");
    }


    public int find_number_of_neighbor_mines()
    {   int N_MINES = 0;
        //Find NUMBER of pairs(mines) with same color(value) cell right, bottom.
        for (int i = 0;i<N_ROWS-1;i++) {
            for (int j = 0; j < N_COLS - 1; j++) {
                int idx = i * N_COLS + j;
                int bottom = (i + 1) * N_COLS + j;
                int right = i * N_COLS + j + 1;
                if (field[idx] == field[bottom] || field[idx] == field[right]) {
                    N_MINES += 1;
                }
            }
        }
        return N_MINES;
    }



    // TODO: show how many mines around a cell, and put the picture.
    /*public int find_number_neighbor_mines()
    {


    }
    */


    // TODO: WHY all the flags are wrong???
    // HOW TO WIN?




    // have to override this function that defined in the parent class.
    @Override
    public neighbor[] getNeighbors(int index) {
        return null;
    }
    @Override
    public boolean isMineCell(int index) {
        if (field[index] > COVER_FOR_CELL)
            return false;
        if (index-1 > -1 && field[index] == field[index-1])
            return true;
        if (index-N_COLS > -1 && field[index] == field[index-N_COLS])
            return true;
        if (index+1 < field.length && field[index] == field[index+1])
            return true;
        if (index+N_COLS < field.length && field[index] == field[index+N_COLS])
            return true;
        return false;
    }

}


