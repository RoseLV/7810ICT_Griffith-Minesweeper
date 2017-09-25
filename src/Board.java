import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent; // This two go to controller
import java.util.Random;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Graphics; // this three go to VIew


public abstract class Board{

    private static int CELL_SIZE;
    public static int getCellSize() {
        return CELL_SIZE;
    }
    public static void setCellSize(int cellSize) {
        CELL_SIZE = cellSize;
    }

    /** Model: Value, define the constants will be used in the game.*/
    private int N_COLORS  = 9;
    public int getN_COLORS() {
        return N_COLORS;
    }
    public void setN_COLORS(int n_COLORS) {
        N_COLORS = n_COLORS;
    }

    /**
     * There are 13 images used in this game.
     * A cell can be surrounded by maximum of 8 mines,
     * so we need numbers 1..8.
     * We need images for an empty cell, a mine, a covered cell, a marked cell and finally for a wrongly marked cell.
     * The size of each of the images is 15x15px.
     */
    private int NUM_IMAGES = 13;
    private boolean inGame;
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

    public int getCOVER_FOR_CELL() {
        return COVER_FOR_CELL;
    }

    public int getMARK_FOR_CELL() {
        return MARK_FOR_CELL;
    }

    public int getEMPTY_CELL() {
        return EMPTY_CELL;
    }

    public int getMINE_CELL() {
        return MINE_CELL;
    }

    private final int COVERED_MINE_CELL = MINE_CELL + COVER_FOR_CELL;
    public int getCOVERED_MINE_CELL() {
        return COVERED_MINE_CELL;
    }

    private final int MARKED_MINE_CELL = COVERED_MINE_CELL + MARK_FOR_CELL;
    public int getMARKED_MINE_CELL() {
        return MARKED_MINE_CELL;
    }

    /**
     * The field is an array of numbers. Each cell in the field has a specific number.
     * E.g. a mine cell has number 9, while a cell with number 2, meaning it is adjacent to two mines.
     * The numbers are added, line33, 34. For example, a covered mine has number 19, 9 for the mine and 10 for the cell cover etc.
     */
    private int[] field; // one dimension array
    public int[] getField() {
        return field;
    }

    public void setField(int[] field) {
        this.field = field;
    }

    public Image[] getImg() {
        return img;
    }

    public void setImg(Image[] img) {
        this.img = img;
    }

    public int getAll_cells() {
        return all_cells;
    }

    public void setAll_cells(int all_cells) {
        this.all_cells = all_cells;
    }

    // This grey variables are ready to make getter & setter?
    private int N_MINES = 10;
    public int getN_MINES() {
        return N_MINES;
    }
    public void setN_MINES(int n_MINES) {
        N_MINES = n_MINES;
    }

    /**
     * The minefield in our game has 10 hidden mines.
     * There are 16 rows and 16 columns in this field.
     * So there are 256 cells together in the minefield.
     */
    private int N_ROWS = 16;
    public int getN_COLS() {
        return N_COLS;
    }
    public void setN_COLS(int n_COLS) {
        N_COLS = n_COLS;
    }

    private int N_COLS = 16;
    public int getN_ROWS() {
        return N_ROWS;
    }
    public void setN_ROWS(int n_ROWS) {
        N_ROWS = n_ROWS;
    }

    private int mines_left;
    public int getMines_left() {
        return mines_left;
    }
    public void setMines_left(int mines_left) {
        this.mines_left = mines_left;
    }

    private Image[] img;

    private int all_cells;

    //protected JLabel statusbar;  // can go
    //protected JLabel timeBar;    // can go


    // Board constructor
    public Board(int N_COLS, int N_ROWS) {

        this.N_COLS = N_COLS;
        this.N_ROWS = N_ROWS;
        //this.statusbar = statusbar;
        //this.timeBar = timeBar;
        this.img = this.loadImages();
        newGame();
    }


    protected abstract Image[] loadImages();
    public abstract neighbor[] getNeighbors(int index);

    // state: boolean inGame
    public boolean getInGame() {
        return inGame;
    }
    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }

    // start a new game
    public void newGame() {

        int i;
        int position;
        int cell;
        inGame = true;
        //timeBar.setText("0"); // Display time.
        mines_left = N_MINES;
        Random random = new Random();

        // These lines set up the mine field. Every cell is covered by default.
        all_cells = N_ROWS * N_COLS; // 16 * 16
        field = new int[all_cells];

        for (i = 0; i < all_cells; i++)
            field[i] = COVER_FOR_CELL; // field[i] all == 10 initially

        /** Set the number of line left ob the bottom status bar.*/
        //statusbar.setText(Integer.toString(mines_left));

        int mine = 0;
        /** random generate N_MINES mines. */
        while (mine < N_MINES) {  // all_cells: 196
            position = (int) (all_cells * random.nextDouble()); /*distributed double value between 0.0 and 1.0*/

            if ((position < all_cells) && (field[position] != COVERED_MINE_CELL)) {
                field[position] = COVERED_MINE_CELL;
                mine++;

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

    // recursive??!   // TODO: HOW TO LET THIS METHOD TO be used in GameController
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

    // TODO: HOW TO LET THIS METHOD TO be used in GameController
    public boolean isMineCell(int index) {
        int cell = field[index];
        return cell == MINE_CELL;
    }


}
