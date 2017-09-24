import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.Timer;


public class GameView extends JFrame {
    private final int CELL_SIZE = 13;
    private int FRAME_WIDTH, FRAME_HEIGHT;

    private final JPanel statusBar;
    private JLabel statusText, timeText;

    private final JMenuBar menuBar;
    private JMenuItem squareCell, hexCell, colorCell;

    private GameModel gameModel;

    // constructor
    public GameView(GameModel gameModel) throws HeadlessException {
        super("Minesweeper");
        this.gameModel = gameModel;

        // set window size
        this.FRAME_HEIGHT = this.gameModel.getRows() * CELL_SIZE + 66;
        this.FRAME_WIDTH = this.gameModel.getCols() * CELL_SIZE + 6;
        setSize(this.FRAME_WIDTH, this.FRAME_HEIGHT);
        setLocationRelativeTo(null);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setJMenuBar(this.menuBar = this.createMenuBar());
        add(this.gameModel.getGameBoard());// paintComponent
        add(this.statusBar = this.createStatusBar(), BorderLayout.SOUTH);
    }

    private JPanel createStatusBar() {
        JPanel bottomBar = new JPanel();
        bottomBar.setLayout(new BoxLayout(bottomBar, BoxLayout.X_AXIS));
        bottomBar.add(this.statusText = new JLabel());
        bottomBar.add(javax.swing.Box.createHorizontalGlue());
        bottomBar.add(this.timeText = new JLabel());
        bottomBar.setMaximumSize(new Dimension(FRAME_WIDTH, 6));
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


    public void updateGame(Board game) {
        // here will change the game depending on user selection
        removeAll();
        add(game);
        add(statusBar, BorderLayout.SOUTH);

        // this revalidate ensures display is updated correctly
        revalidate();
    }

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

// Difference between squareboard(hgexboard) and the extension(colorboard) is the number of mines
// of the former is defined, whereas the colorboard is not. Generate cell's color first and then find out
// and calculate the number of mines.