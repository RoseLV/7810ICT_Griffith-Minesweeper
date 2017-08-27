import java.awt.*;
import javax.swing.*;
import java.awt.event.*;


public class Mines1 extends JFrame implements ActionListener{

    private final int CELL_SIZE = 13;

    /**
     * frame size
     * menubar
    */
    private int FRAME_WIDTH; //250;
    private int FRAME_HEIGHT; //307;

    private final Board1 game;
    private final JLabel statusbar;
    private final JLabel timeBar;
    private final JMenuItem squareCell, hexCell;
    //private Timer timer;

    public Mines1() {
        int N_MINES = 10;
        int N_COLS = 16;
        int N_ROWS = 16;

        FRAME_HEIGHT = N_ROWS * CELL_SIZE + 66 ;
        FRAME_WIDTH = N_COLS * CELL_SIZE + 6;

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


        Board1.CELL_SIZE = CELL_SIZE;
        game = new Board1(N_MINES, N_ROWS, N_COLS, statusbar, timeBar);
        add(game);

        //
        JMenuBar menuBar = new JMenuBar();

        JMenu option = new JMenu("Option");

        squareCell = new JMenuItem("SquareCell");
        squareCell.addActionListener(this);
        option.add(squareCell);

        hexCell = new JMenuItem("HexCell");
        hexCell.addActionListener(this);
        option.add(hexCell);

        menuBar.add(option);
        setJMenuBar(menuBar);
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == squareCell) {
            System.out.println("square");
            //b = new Board1();
        }
        if (e.getSource() == hexCell){
            System.out.println("hexcell");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable(){

            @Override
            public void run() {
                JFrame ex = new Mines1();
                ex.setVisible(true);
            }
        });
    }
}