import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.Timer;


public class Mines extends JFrame implements ActionListener{

    private final int CELL_SIZE = 13;

    private int FRAME_WIDTH; //250;
    private int FRAME_HEIGHT; //307;

    private final HexBoard hex_game;
    private final SquareBoard cube_game;
    private final JLabel statusbar;
    private final JLabel timeBar;
    private Timer timer;

    private final JMenuItem squareCell, hexCell;
    //private Timer timer;

    // constructor
    public Mines() {
        int N_MINES = 5;
        int N_COLS = 16;
        int N_ROWS = 16;
        //int time = 0;

        FRAME_HEIGHT = N_ROWS * CELL_SIZE + 66 ;
        FRAME_WIDTH = N_COLS * CELL_SIZE + 6;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //time = new Timer(1000,(ActionListener) this);

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


        HexBoard.CELL_SIZE = CELL_SIZE;
        //SquareBoard.CELL_SIZE = CELL_SIZE;
        hex_game = new HexBoard(N_MINES, N_ROWS, N_COLS, statusbar, timeBar);
        cube_game = new SquareBoard(statusbar, timeBar);
        add(hex_game);
        add(cube_game);

        timer = new Timer(1000, this);
        timer.start();

        // VIEW - java swing
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


        if(e.getSource() == timer)
        {
            String time=timeBar.getText().trim();
            int t=Integer.parseInt(time);
            //System.out.println(t);
            if(hex_game.getInGame()==false){
                timer.stop();
            }
            else{
                t++;
                timeBar.setText(t+"");
            }
        }

        if (e.getSource() == hexCell){
            hex_game.init();

        }

        if (e.getSource() == squareCell) {
            cube_game.newGame(10, 16, 16);
        }

    }

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

