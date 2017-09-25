import java.awt.*;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.*;
import javax.swing.Timer;

public class GameController {
    private Board gameModel;
    private GameView gameView;

    int N_MINES = gameModel.getN_MINES();
    int N_ROWS = gameModel.getN_ROWS();
    int N_COLS = gameModel.getN_COLS();
    int N_COLORS = gameModel.getN_COLORS();
    boolean inGame = gameModel.getInGame();
    int mines_left = gameModel.getMines_left();
    int[] field = gameModel.getField();

    // constructor
    public GameController(Board gameModel, GameView gameView) {
        this.gameModel = gameModel;
        this.gameView = gameView;
    }

    public void startGame() {
        //this.gameModel.setGameBoard(createSquareBoard());
        // listener should go to controller, need Jpanel
        setDoubleBuffered(true);
        addMouseListener(new MinesAdapter());
        this.gameModel.newGame();
        this.gameView.setVisible(true);
    }

    // move action listener to controller
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == timer) {
            String time = gameView.timeText.getText().trim();
            int t = Integer.parseInt(time);
            //System.out.println(t);
            if (!gameModel.getInGame()) {
                timer.stop();
            } else {
                t++;
                gameView.timeText.setText(t + "");
            }
        }

        if (e.getSource() == gameView.getSquareCell()) {
            this.gameView.updateGame(new SquareBoard(N_MINES, N_ROWS, N_COLS));
        }

        if (e.getSource() == gameView.getHexCell()) {
            this.gameView.updateGame(new HexBoard(N_MINES, N_ROWS, N_COLS));
        }

        if (e.getSource() == gameView.getColorCell()) {
            this.gameView.updateGame(new ColorBoard(N_COLORS, N_ROWS, N_COLS));
        }
    }


    // TODO: write getter setter in gameModel so get the data here;
    class MinesAdapter extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            boolean rep = false;

            int index = getIndexFromMouseEvent(e);

            if (!gameModel.getInGame()) {
                gameModel.newGame();
                gameView.repaint();
            }


            if (clickedOnBoard(e)) {

                if (e.getButton() == MouseEvent.BUTTON3) {

                    if (field[index] > gameModel.getMINE_CELL()) {
                        rep = true;

                        if (field[index] <= gameModel.getCOVERED_MINE_CELL()) {
                            if (gameModel.getMines_left() > 0) {
                                /**
                                 * If we right click on an unmarked cell, we add MARK_FOR_CELL to the number representing the cell.
                                 * This leads to drawing a covered cell with a mark in the paintComponent() method.
                                 * */

                                field[index] += gameModel.getMARK_FOR_CELL();
                                mines_left--;
                                gameView.statusText.setText(Integer.toString(mines_left));
                            } else
                                gameView.statusText.setText("No marks left");
                        } else {
                            field[index] -= gameModel.getMARK_FOR_CELL();
                            mines_left++;
                            gameView.statusText.setText(Integer.toString(mines_left));
                        }
                    }

                } else {

                    /**
                     * Nothing happens if click on the covered & marked cell.
                     * It must by first uncovered by another right click and only then it is possible to left click on it.
                     */
                    if (field[index] > gameModel.getCOVERED_MINE_CELL()) {
                        return;
                    }

                    if ((field[index] > gameModel.getMINE_CELL()) &&
                            (field[index] < gameModel.getMARKED_MINE_CELL())) {

                        /**
                         * A left click removes a cover from the cell.
                         */
                        field[index] -= gameModel.getCOVER_FOR_CELL();
                        rep = true;

                        /**
                         * In case we left clicked on a mine, the game is over.
                         * If we left clicked on an empty cell,
                         * we call the find_empty_cells() method which recursively
                         * finds all adjacent empty cells.
                         * */
                        if (gameModel.isMineCell(index))
                            gameModel.setInGame(false);
                        if (field[index] == gameModel.getEMPTY_CELL())
                            gameModel.find_empty_cells(index);
                    }
                }
                if (rep)
                    gameView.repaint();

            }
        }
    }

}
