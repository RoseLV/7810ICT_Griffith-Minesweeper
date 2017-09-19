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

    //int field[][];
    protected int N_COLORS; //protected int N_COLORS = 5;

    // TODO: modify constructor
    public ColorBoard(int n_rows, int n_cols, JLabel statusbar, JLabel timeBar) {
        super(statusbar, timeBar);
        this.N_ROWS = n_rows;
        this.N_COLS = n_cols;
        all_cells = N_ROWS * N_COLS;
        //newGame();
        NUM_IMAGES = 5;
        N_COLORS = 5;
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

                // This N_COLORS should be purple color?
                int num = random.nextInt(5) + 1;
                // 生成的随机数为0~n-1，所以后边+1，转换一下就成1到8的随机整数
                field[i * N_COLS + j] += num;
            }
        }

        // int pairs ok to put here (before define function)?
        int N_MINES = 0;
        //public int calculateMines(int field[][]){

        //TODO: find NUMBER of pairs with same color(value) cell right, bottom.
        for (int i = 0;i<N_ROWS-1;i++) {
            for (int j = 0; j < N_COLS - 1; j++) {
                // Can I make this shorter？只找右边和下边；
                int idx = i * N_COLS + j;
                int bottom = (i + 1) * N_COLS + j;
                int right = i * N_COLS + j + 1;
                if (field[idx] == field[bottom] || field[idx] == field[right]) {
                    N_MINES += 1;
                }
            }
        }
        //return N_MINES;   // pairs = mines, show left bottom corner.
        //}

        mines_left = N_MINES;

        inGame = true;
        statusbar.setText(Integer.toString(N_MINES));
        timeBar.setText("0");
    }




    // TODO: find_number_of_neighbor_mines() in 4 directions:上下左右, ?/4, show picture.
    // How to delete this function defined in the parent class.
    @Override
    public neighbor[] getNeighbors(int index) {
        return null;
    }


    // TODO: mouse press function change: left, right
    /*
    leftClick: uncover;
    rightClick: set flag;
    */

        // TODO: 改写一下game won, game over, 图片变化；
        // TODO: 怎么找雷 让uncover-- 是个问题？


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


