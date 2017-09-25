import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.Timer;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Graphics;


public class GameView extends JFrame {
    // Use private better : least privilege
    private final int DRAW_MINE = 9;
    private final int DRAW_COVER = 10;
    private final int DRAW_MARK = 11;
    private final int DRAW_WRONG_MARK = 12;
    private final int CELL_SIZE = 13;
    private int FRAME_WIDTH, FRAME_HEIGHT;
    private final JPanel statusBar;
    private JLabel statusText, timeText;
    private static final int BORDER_WIDTH = 3;
    private static final int MENU_BAR_HEIGHT = 60;
    private static final int STATUS_BAR_HEIGHT = 6;

    private final JMenuBar menuBar;

    public JMenuItem getSquareCell() {
        return squareCell;
    }

    public void setSquareCell(JMenuItem squareCell) {
        this.squareCell = squareCell;
    }

    public JMenuItem getHexCell() {
        return hexCell;
    }

    public void setHexCell(JMenuItem hexCell) {
        this.hexCell = hexCell;
    }

    public JMenuItem getColorCell() {
        return colorCell;
    }

    public void setColorCell(JMenuItem colorCell) {
        this.colorCell = colorCell;
    }

    private JMenuItem squareCell, hexCell, colorCell;
    private Board gameModel;


    // constructor
    public GameView(Board gameModel) throws HeadlessException {
        super("Minesweeper");
        this.gameModel = gameModel;

        // set window size
        this.FRAME_HEIGHT = this.gameModel.getN_ROWS() * CELL_SIZE + MENU_BAR_HEIGHT;
        this.FRAME_WIDTH = this.gameModel.getN_COLS() * CELL_SIZE + BORDER_WIDTH;
        setSize(this.FRAME_WIDTH, this.FRAME_HEIGHT);
        setLocationRelativeTo(null);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setJMenuBar(this.menuBar = this.createMenuBar());
        //this.paintComponent();// paintComponent
        add(this.statusBar = this.createStatusBar(), BorderLayout.SOUTH);
    }

    private JPanel createStatusBar() {
        JPanel bottomBar = new JPanel();
        bottomBar.setLayout(new BoxLayout(bottomBar, BoxLayout.X_AXIS));
        bottomBar.add(this.statusText = new JLabel());
        bottomBar.add(javax.swing.Box.createHorizontalGlue());
        bottomBar.add(this.timeText = new JLabel());
        bottomBar.setMaximumSize(new Dimension(FRAME_WIDTH, STATUS_BAR_HEIGHT));
        return bottomBar;
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu option = new JMenu("Option");
        option.add(this.squareCell = new JMenuItem("SquareCell"));
        option.add(this.hexCell = new JMenuItem("HexCell"));
        option.add(this.colorCell = new JMenuItem("ColorCell"));
        menuBar.add(option);
        return menuBar;
    }

// update View
    public void updateGame(Board gameModel) {
        // here will change the game depending on user selection
        removeAll();
        add(menuBar);
        add(statusBar);
        //add(gameModel);
        add(statusText, BorderLayout.SOUTH);
        add(timer);
        // this revalidate ensures display is updated correctly
        revalidate();
    }


    @Override
    public void paintComponent(Graphics g) {

        int idx;
        int cell;
        int uncover = 0;

        for (int i = 0; i < gameModel.getN_ROWS(); i++) {
            for (int j = 0; j < gameModel.getN_COLS(); j++) {

                idx = (i * gameModel.getN_COLS()) + j;
                cell = gameModel.getField()[idx];

                if (gameModel.getInGame() && gameModel.isMineCell(idx))
                    gameModel.setInGame(false);
                /**
                 * If the game is over and all uncovered mines will be shown if any left and show all wrongly marked cells if any.
                 */
                if (!gameModel.getInGame()) {
                    if (cell == gameModel.getCOVERED_MINE_CELL()) {
                        cell = DRAW_MINE;
                    } else if (cell == gameModel.getMARKED_MINE_CELL()) {
                        cell = DRAW_MARK;
                    } else if (cell > gameModel.getCOVERED_MINE_CELL()) {
                        cell = DRAW_WRONG_MARK;
                    } else if (cell > gameModel.getMINE_CELL()) {
                        cell = DRAW_COVER;
                    }
                } else {
                    if (cell > gameModel.getCOVERED_MINE_CELL())
                        cell = DRAW_MARK;
                    else if (cell > gameModel.getMINE_CELL()) {
                        cell = DRAW_COVER;
                        uncover++;
                    }
                }

                /** This code line draws every cell on the board. */
                drawImage(g, gameModel.getImg()[cell], i, j);
            }
        }

        if (uncover == 0 && gameModel.getInGame()) {
            gameModel.setInGame(false);
            statusText.setText("Game won");
        }
        else if (!gameModel.getInGame())
            statusText.setText("Game lost");

    }

}