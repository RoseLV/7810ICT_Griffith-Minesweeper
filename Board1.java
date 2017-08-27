import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Board1 extends JPanel {
    public static int CELL_SIZE;

    /**
     * There are 13 images used in this game.
     * A cell can be surrounded by maximum of 8 mines,
     * so we need numbers 1..8.
     * We need images for an empty cell, a mine, a covered cell, a marked cell and finally for a wrongly marked cell.
     * The size of each of the images is 15x15px.
     */
    private final int NUM_IMAGES = 13;
    /**
     * A mine field is an array of numbers.
     * For example 0 denotes an empty cell.
     * Number 10 is used for a cell cover as well as for a mark.
     * Using constants improves readability of the code.
     */
    private final int COVER_FOR_CELL = 10;
    private final int MARK_FOR_CELL = 10;
    private final int EMPTY_CELL = 0;
    private final int MINE_CELL = 9;
    private final int COVERED_MINE_CELL = MINE_CELL + COVER_FOR_CELL;
    private final int MARKED_MINE_CELL = COVERED_MINE_CELL + MARK_FOR_CELL;

    private final int DRAW_MINE = 9;
    private final int DRAW_COVER = 10;
    private final int DRAW_MARK = 11;
    private final int DRAW_WRONG_MARK = 12;
    /**The minefield in our game has 10 hidden mines.
     * There are 16 rows and 16 columns in this field.
     * So there are 256 cells together in the minefield.
     */
    private final int N_MINES;
    private final int N_ROWS;
    private final int N_COLS;
    /**
     * The field is an array of numbers. Each cell in the field has a specific number.
     * E.g. a mine cell has number 9, while a cell with number 2, meaning it is adjacent to two mines.
     * The numbers are added, line33, 34. For example, a covered mine has number 19, 9 for the mine and 10 for the cell cover etc.
     */
    private int[] field;  // one dimension array
    private boolean inGame;
    private int mines_left;
    private Image[] img;

    private int all_cells;
    private JLabel statusbar;
    private JLabel timeBar;

    public Board1(int n_mines, int n_rows, int n_cols, JLabel statusbar, JLabel timeBar) {
        this.N_MINES = n_mines;
        this.N_ROWS = n_rows;
        this.N_COLS = n_cols;
        this.statusbar = statusbar;
        this.timeBar = timeBar;
        init();
    }

    // Board1's constructor
    private void init() {
/**
 * Load images into the array ima. The images are named 0.png, 1.png ... 12.png.
 */
        img = new Image[NUM_IMAGES];  // NUM_IMAGES== 13
        for (int i = 0; i < NUM_IMAGES; i++) { // 12 pictures in total
            img[i] = (new ImageIcon(this.getClass().getResource(i + ".png"))).getImage();
        }

        setDoubleBuffered(true);
        addMouseListener(new MinesAdapter());
        /**
         * newGame() initiates the Minesweeper game.
         */
        newGame();
    }

    private static class neighbor {
        public enum position {
            TOP_LEFT,
            TOP_RIGHT,
            LEFT,
            RIGHT,
            BOTTOM_LEFT,
            BOTTOM_RIGHT,
            OUT_OF_BOUNDS
        }
        public static final position[] positions = position.values();

        private final int index;
        private final position location;

        // constructor
        public neighbor (int index, position location) {
            this.index = index;
            this.location = location;
            // information we need
            // this.<> resolves naming conflict
        }

        public int getIndex() {
            return this.index;
        }

        public position getLocation() {
            return this.location;
        }
    }

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
                neighbors[i] = new neighbor(newRow * N_COLS + newCol, neighbor.positions[i]);
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


    private void newGame() {

        timeBar.setText("0");
        int current_col;
        int current_row;
        int i;
        int position;
        int cell;
        Random random = new Random();
        inGame = true;
        mines_left = N_MINES;
        /**
         * These lines set up the mine field. Every cell is covered by default.
         */
        all_cells = N_ROWS * N_COLS; // 16 * 16
        field = new int[all_cells];

        for (i = 0; i < all_cells; i++)
            field[i] = COVER_FOR_CELL; // field[i] all == 10 initially
        /** Set the number of line left ob the bottom status bar.*/
        statusbar.setText(Integer.toString(mines_left));

        /** In the while cycle we randomly position all mines in the field. */
        i = 0;
        /**
         * random generate mines
         * */
        while (i < N_MINES*2) {    // 当i< N_MINES: 10的时候  // all_cells: 196
            position = (int) (all_cells * random.nextDouble()); /*distributed double value between 0.0 and 1.0*/

            if ((position < all_cells) && (field[position] != COVERED_MINE_CELL)) {
                field[position] = COVERED_MINE_CELL;
                i++;

                neighbor[] neighbors = getNeighbors(position);
                for (neighbor n : neighbors) {
                    cell = n.getIndex();
                    if (n.location == neighbor.position.OUT_OF_BOUNDS ||
                        field[cell] == COVERED_MINE_CELL) {
                        continue;
                    }

                    field[cell]++;
                }
            }
        }
    }



    public void find_empty_cells(int position) {
        neighbor[] neighbors = getNeighbors(position);
        for (neighbor n : neighbors) {
            if (n.location == neighbor.position.OUT_OF_BOUNDS) {
                continue;
            }

            int cell = n.getIndex();
            // if nerghbor is mine or already been uncovered, do nothing.
            if (field[cell] == COVERED_MINE_CELL || field[cell] <= MINE_CELL) {
                continue;
            }
            // uncover
            field[cell] -= COVER_FOR_CELL;
            if (field[cell] == EMPTY_CELL)
                find_empty_cells(cell);
        }
    }

    @Override
    public void paintComponent(Graphics g) {

        int cell;
        int uncover = 0;

        for (int i = 0; i < N_ROWS; i++) {
            for (int j = 0; j < N_COLS; j++) {

                cell = field[(i * N_COLS) + j];

                if (inGame && cell == MINE_CELL)
                    inGame = false;
                /**
                 * If the game is over and all uncovered mines will be shown if any left and show all wrongly marked cells if any.
                 */
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
                /** This code line draws every cell on the board. */
                int extraX = i % 2 == 0 ? 6 : 0;
                g.drawImage(img[cell], (j * CELL_SIZE) + extraX,
                        (i * CELL_SIZE), this);
                // image, x, y, this
            }
        }

        if (uncover == 0 && inGame) {
            inGame = false;
            statusbar.setText("Game won");
        } else if (!inGame)
            statusbar.setText("Game lost");
    }


    class MinesAdapter extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {

            int x = e.getX();
            int y = e.getY();
            // get the current cell's row number
            int cRow = y / CELL_SIZE;   // CELL_SIZE = 20
            // odd row stay, even row move left 10.
            x -= cRow % 2 == 0 ? 6 : 0;
            // get the current cell's column number
            int cCol = x / CELL_SIZE;

            boolean rep = false;

            System.out.println(String.format("x=%d y=%d r=%d c=%d", x,y,cRow,cCol));
            System.out.println(field[cRow * N_COLS + cCol]);


            if (!inGame) {
                newGame();
                repaint();
            }


            if ((x < N_COLS * CELL_SIZE) && (y < N_ROWS * CELL_SIZE)) {

                if (e.getButton() == MouseEvent.BUTTON3) {

                    if (field[(cRow * N_COLS) + cCol] > MINE_CELL) {
                        rep = true;

                        if (field[(cRow * N_COLS) + cCol] <= COVERED_MINE_CELL) {
                            if (mines_left > 0) {
                                /**
                                 * If we right click on an unmarked cell, we add MARK_FOR_CELL to the number representing the cell.
                                 * This leads to drawing a covered cell with a mark in the paintComponent() method.
                                 */
                                field[(cRow * N_COLS) + cCol] += MARK_FOR_CELL;
                                mines_left--;
                                statusbar.setText(Integer.toString(mines_left));
                            } else
                                statusbar.setText("No marks left");
                        } else {

                            field[(cRow * N_COLS) + cCol] -= MARK_FOR_CELL;
                            mines_left++;
                            statusbar.setText(Integer.toString(mines_left));
                        }
                    }

                } else {
                    /**
                     * Nothing happens if click on the covered & marked cell.
                     * It must by first uncovered by another right click and only then it is possible to left click on it.
                     */
                    if (field[(cRow * N_COLS) + cCol] > COVERED_MINE_CELL) {
                        return;
                    }

                    if ((field[(cRow * N_COLS) + cCol] > MINE_CELL) &&
                            (field[(cRow * N_COLS) + cCol] < MARKED_MINE_CELL)) {
                        /**
                         * A left click removes a cover from the cell.
                         */
                        field[(cRow * N_COLS) + cCol] -= COVER_FOR_CELL;
                        rep = true;
                        /**
                         * In case we left clicked on a mine, the game is over.
                         * If we left clicked on an empty cell,
                         * we call the find_empty_cells() method which recursively
                         * finds all adjacent empty cells.
                         */
                        if (field[(cRow * N_COLS) + cCol] == MINE_CELL)
                            inGame = false;
                        if (field[(cRow * N_COLS) + cCol] == EMPTY_CELL)
                            find_empty_cells((cRow * N_COLS) + cCol);
                    }
                }

                if (rep)
                    repaint();

            }
        }
    }
}