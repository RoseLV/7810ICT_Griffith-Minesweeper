import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;


/*

// Board.java
abstract public class Board {
    [private|protected|public] int intvar;

    public void setIntVar(int newIntVar) {
        intvar = newIntVar;
    }

    public int getIntVar() {
        return intvar;
    }

    public void blah(){
    }

    public abstract void newGame();
}

// SquareBoard.java
public class SquareBoard extends Board {

    public void newGame () {
        // if private
        int mycopy = super.intvar; // can't do
        super.intvar = xx (write)
    }
}

// Main.java
public class Main {
  public static void main(String[] args){
    Board board = new Board(); // if class declared abstract, cannot do this

  // if private
    board.intvar = 14; // not allowed because private, won't compile

    board.setIntVar(14);
    int myIntVar = board.getIntVar();

  // if protected
    board.intvar = 14; // not allowed, same reason as private

  // if public
    board.intvar = 14; // ok
}
}
 */
public abstract class Board extends JPanel {

    public static int CELL_SIZE;

    // define the constants will be used in the game.
    /**
     * There are 13 images used in this game.
     * A cell can be surrounded by maximum of 8 mines,
     * so we need numbers 1..8.
     * We need images for an empty cell, a mine, a covered cell, a marked cell and finally for a wrongly marked cell.
     * The size of each of the images is 15x15px.
     */
    protected final int NUM_IMAGES = 13;

    /**
     * A mine field is an array of numbers.
     * For example 0 denotes an empty cell.
     * Number 10 is used for a cell cover as well as for a mark.
     * Using constants improves readability of the code.
     */
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

    /**The minefield in our game has 10 hidden mines.
     * There are 16 rows and 16 columns in this field.
     * So there are 256 cells together in the minefield.
     */
    protected int N_MINES = 10;
    protected int N_ROWS = 16;
    protected int N_COLS = 16;

    /**
     * The field is an array of numbers. Each cell in the field has a specific number.
     * E.g. a mine cell has number 9, while a cell with number 2, meaning it is adjacent to two mines.
     * The numbers are added, line33, 34. For example, a covered mine has number 19, 9 for the mine and 10 for the cell cover etc.
     */
    protected int[] field; // one dimension array
    protected boolean inGame;
    protected int mines_left;
    protected Image[] img;

    protected int all_cells;
    protected JLabel statusbar;
    protected JLabel timeBar;

    // Board constructor
    public Board(JLabel statusbar, JLabel timeBar) {
        this.statusbar = statusbar;
        this.timeBar = timeBar;

        this.img = this.loadImages();

        setDoubleBuffered(true);

        addMouseListener(new MinesAdapter());
        newGame();
    }


    protected abstract Image[] loadImages();

    public abstract neighbor[] getNeighbors(int index);

    public boolean inGame() {
        return inGame;
    }

    public void newGame() {

        timeBar.setText("0");

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
        while (i < N_MINES) {    // 当i< N_MINES: 10的时候  // all_cells: 196
            position = (int) (all_cells * random.nextDouble()); /*distributed double value between 0.0 and 1.0*/

            if ((position < all_cells) && (field[position] != COVERED_MINE_CELL)) {
                field[position] = COVERED_MINE_CELL;
                i++;

                neighbor[] neighbors = getNeighbors(position);
                for (neighbor n : neighbors) {
                    cell = n.getIndex();
                    if (n.getLocation() == neighbor.position.OUT_OF_BOUNDS ||
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
            if (n.getLocation() == neighbor.position.OUT_OF_BOUNDS) {
                continue;
            }

            int cell = n.getIndex();
            // if neighbor is mine or already been uncovered, do nothing.
            if (field[cell] == COVERED_MINE_CELL || field[cell] <= MINE_CELL) {
                continue;
            }
            // uncover
            field[cell] -= COVER_FOR_CELL;
            if (field[cell] == EMPTY_CELL)
                find_empty_cells(cell);
        }
    }

    class MinesAdapter extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {

            int x = e.getX();
            int y = e.getY();

            int cCol = x / CELL_SIZE;
            int cRow = y / CELL_SIZE;

            boolean rep = false;


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

                    if (field[(cRow * N_COLS) + cCol] > COVERED_MINE_CELL) {
                        return;
                    }

                    if ((field[(cRow * N_COLS) + cCol] > MINE_CELL) &&
                            (field[(cRow * N_COLS) + cCol] < MARKED_MINE_CELL)) {

                        field[(cRow * N_COLS) + cCol] -= COVER_FOR_CELL;
                        rep = true;

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

    @Override
    public void paintComponent(Graphics g) {

        int cell;
        int uncover = 0;

        for (int i = 0; i < N_ROWS; i++) {
            for (int j = 0; j < N_COLS; j++) {

                cell = field[(i * N_COLS) + j];

                if (inGame && cell == MINE_CELL)
                    inGame = false;

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

        if (uncover == 0 && inGame) {
            inGame = false;
            statusbar.setText("Game won");
        }
        else if (!inGame)
            statusbar.setText("Game lost");

    }


}



