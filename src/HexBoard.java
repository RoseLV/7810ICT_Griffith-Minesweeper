import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class HexBoard extends Board {

    public HexBoard(int n_mines, int n_rows, int n_cols, JLabel statusbar, JLabel timeBar) {
        super(statusbar, timeBar);
        this.N_MINES = n_mines;
        this.N_ROWS = n_rows;
        this.N_COLS = n_cols;
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