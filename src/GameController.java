import java.awt.*;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.event.*;
import javax.swing.Timer;

public class GameController {
    private GameModel gameModel;
    private GameView gameView;

    // constructor
    public GameController(GameModel gameModel, GameView gameView) {
        this.gameModel = gameModel;
        this.gameView = gameView;
    }

    public void startGame() {
        this.gameModel.setGameBoard(createSquareBoard());
        this.gameModel.newGame();
        this.gameView.setVisible(true);
    }

    public Board createSquareBoard() {
        Board squareboard = new SquareBoard();
    }


    // move action listener to controller
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == timer) {
            String time = timeBar.getText().trim();
            int t = Integer.parseInt(time);
            //System.out.println(t);
            if (!game.inGame()) {
                timer.stop();
            } else {
                t++;
                timeBar.setText(t + "");
            }
        }

        if (e.getSource() == squareCell) {
            this.changeGame(new SquareBoard(N_MINES, N_ROWS, N_COLS, statusbar, timeBar));
        }

        if (e.getSource() == hexCell) {
            this.changeGame(new HexBoard(N_MINES, N_ROWS, N_COLS, statusbar, timeBar));
        }

        if (e.getSource() == colorCell) {
            this.changeGame(new ColorBoard(N_COLORS, N_ROWS, N_COLS, statusbar, timeBar));
        }

    }
}
