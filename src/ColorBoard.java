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


    int field[][];
    protected int N_COLORS = 5;

    // TODO: modify constructor
    public ColorBoard(int n_rows, int n_cols, JLabel statusbar, JLabel timeBar) {
        super(statusbar, timeBar);
        this.N_ROWS = n_rows;
        this.N_COLS = n_cols;
        all_cells = N_ROWS * N_COLS;
        newGame();
    }


    // this function generate random number from 0-8 for all cells in the Board
    // Why it's not used?
    public int[][] generateNumber(int N_COLORS) {

        for (int i = 0; i < N_ROWS; i++) {
            for (int j = 0; j < N_COLS; j++) {
                Random random = new Random();

                // This N_COLORS should be purple color?
                int num = random.nextInt(N_COLORS) + 1;
                // 生成的随机数为0~n-1，所以后边+1，转换一下就成1到8的随机整数
                field[i][j] = num;
            }
        }
        System.out.println(field);
        return field;
    }

    // int pairs ok to put here (before define function)?
    int N_MINES;
    public int calculateMines(int field[][]){

        //TODO: find NUMBER of pairs with same color(value) cell right, bottom.
        for (int i = 0; i < N_ROWS-1; i++) {
            for (int j = 0; j < N_COLS-1; j++) {
                // Can I make this shorter？只找右边和下边；
                if(     field[i][j] == field[i+1][j] ||
                        field[i][j] == field[i][j+1]
                        ){
                    N_MINES += 1;
                }
            }
        }
        return N_MINES;   // pairs = mines, show left bottom corner.
    }



    protected int NUM_IMAGES = 5;
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



    // TODO: find_number_of_neighbor_mines() in 4 directions:上下左右, ?/4, show picture.
    // How to delete this function defined in the parent class.
    @Override
    public neighbor[] getNeighbors(int index) {
        int num_neighbors = 4;
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


    // TODO: mouse press function change: left, right
    /*
    leftClick: uncover;
    rightClick: set flag;
    */



    @Override
    public void paintComponent(Graphics g) {

        // TODO: 改写一下game won, game over, 图片变化；
        int cell;
        int uncover = 0;

        for (int i = 0; i < N_ROWS; i++) {
            for (int j = 0; j < N_COLS; j++) {

                cell = field[i][j];

                if (inGame && cell == MINE_CELL) // 这里mine_cell改成any both pair location both be uncovered.
                    inGame = false;
                // change to color board condition
                if (!inGame) {
                    if (cell == COVERED_MINE_CELL) {
                        cell = DRAW_MINE;
                    } else if (cell == MARKED_MINE_CELL) {
                        cell = DRAW_MARK;
                    } else if (cell > COVERED_MINE_CELL) {
                        cell = DRAW_WRONG_MARK;
                    } else if (cell > MINE_CELL) {
                        cell = DRAW_COVER;
                    }
                } else {
                    if (cell > COVERED_MINE_CELL)
                        cell = DRAW_MARK;
                    else if (cell > MINE_CELL) {
                        cell = DRAW_COVER;
                        uncover++;
                    }
                }

                g.drawImage(img[cell], (j * CELL_SIZE),
                        (i * CELL_SIZE), this);
            }
        }


        //TODO: 怎么找雷 让uncover-- 是个问题？
        if (uncover == 0 && inGame) {
            inGame = false;
            statusbar.setText("Game won");
        }
        else if (!inGame)
            statusbar.setText("Game lost");
    }

}


