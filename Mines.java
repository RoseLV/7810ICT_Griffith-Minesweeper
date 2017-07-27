import java.awt.*;
import javax.swing.*;


public class Mines extends JFrame {

    private final int FRAME_WIDTH = 250;
    private final int FRAME_HEIGHT = 307;

    private final Board game;
    private final JLabel statusbar;
    private final JLabel timeBar;
    private final JMenuItem squareCell, hexCell;
    //private Timer timer;

    public Mines() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setLocationRelativeTo(null);
        setTitle("Minesweeper");

        statusbar = new JLabel("");
        JPanel bottomBar = new JPanel();
        bottomBar.setLayout(new BoxLayout(bottomBar, BoxLayout.X_AXIS));
        timeBar = new JLabel("0");
        bottomBar.add(statusbar);
        bottomBar.add(javax.swing.Box.createHorizontalGlue());
        bottomBar.add(timeBar);
        bottomBar.setMaximumSize(new Dimension(FRAME_WIDTH, 6));
        add(bottomBar, BorderLayout.SOUTH);
        // ?? can use board like this?: with two parameters without predefine?
        game = new Board(statusbar, timeBar);
        add(game);

        //
        JMenuBar menuBar = new JMenuBar();

        JMenu option = new JMenu("Option");

        squareCell = new JMenuItem("SquareCell");
        //squareCell.addActionListener(this);
        option.add(squareCell);

        hexCell = new JMenuItem("HexCell");
        //hexCell.addActionListener(this);
        option.add(hexCell);
        menuBar.add(option);
        setJMenuBar(menuBar);
    }

    /*public void actionPerformed(ActionEvent e) {

        if (e.getSource() == squareCell) {
            print("");
        }
        if (e.getSource() == hexCell){
            print("hexCell");
        }
    }
    */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable(){

            @Override
            public void run() {
                JFrame ex = new Mines();
                ex.setVisible(true);
            }
        });
    }
}