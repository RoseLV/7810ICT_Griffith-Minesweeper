import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class MinesweeperView extends JFrame {
    private static final String TITLE = "Minesweeper";

    private static final int BORDER_WIDTH = 3;
    private static final int MENU_BAR_HEIGHT = 60;
    private static final int STATUS_BAR_HEIGHT = 6;
    private final int FRAME_WIDTH, FRAME_HEIGHT;
    private final int CELL_SIZE = 13;

    private JMenuItem[] gameOptions;
    private JPanel gameBoard;
    private JLabel statusText, timerText;

    private final MinesweeperModel model;

    public MinesweeperView(MinesweeperModel minesweeperModel) {
        super(MinesweeperView.TITLE);
        model = minesweeperModel;

        FRAME_HEIGHT = model.getRows() * CELL_SIZE + MENU_BAR_HEIGHT + STATUS_BAR_HEIGHT;
        FRAME_WIDTH = model.getCols() * CELL_SIZE + 2 * BORDER_WIDTH;

        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        setJMenuBar(createMenuBar());
        add(gameBoard = new MinesweeperBoard());
        add(createStatusBar(), BorderLayout.SOUTH);
        updateTimerText(0);
    }

    public int getCellSize() {
        return CELL_SIZE;
    }

    public void refreshGameBoard() {
        gameBoard.repaint();
    }

    public void registerGameBoardAction(MouseAdapter action) {
        gameBoard.addMouseListener(action);
    }

    public void registerMenuItemAction(ActionListener action) {
        for(JMenuItem gameOption : gameOptions)
            gameOption.addActionListener(action);
    }

    public void updateStatusText(String status) {
        statusText.setText(status);
    }

    public void updateTimerText(int time) {
        timerText.setText(String.format("%4d  ", time));
    }

    private JMenuBar createMenuBar() {
        JMenuBar menu = new JMenuBar();

        MinesweeperModel.GAME_TYPE[] gameTypes = MinesweeperModel.GAME_TYPE.values();
        gameOptions = new JMenuItem [gameTypes.length];

        JMenu option = new JMenu("Option");
        for(int i = 0; i < gameTypes.length; i++)
            option.add(gameOptions[i] = new JMenuItem(gameTypes[i].toString()));
        menu.add(option);

        return menu;
    }

    private JPanel createStatusBar() {
        JPanel bar = new JPanel();
        bar.setLayout(new BoxLayout(bar, BoxLayout.X_AXIS));
        bar.add(statusText = new JLabel());
        bar.add(javax.swing.Box.createHorizontalGlue());
        bar.add(timerText = new JLabel());
        bar.setMaximumSize(new Dimension(FRAME_WIDTH, STATUS_BAR_HEIGHT));
        return bar;
    }

    private class MinesweeperBoard extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            for(int row = 0; row < model.getRows(); row++) {
                for(int col = 0; col < model.getCols(); col++) {
                    MinesweeperCell cell = model.getCellAt(row, col, CELL_SIZE);
                    g.drawImage(cell.getImage(), cell.getX(), cell.getY(), this);

                    if (cell.hasText()) {
                        g.setColor(Color.WHITE);
                        g.drawString(cell.getText(), cell.getX(), cell.getY() + CELL_SIZE);
                    }
                }
            }
        }
    }
}