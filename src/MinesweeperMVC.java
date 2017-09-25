import javax.swing.SwingUtilities;

public class MinesweeperMVC {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run() {
                MinesweeperModel model = new MinesweeperModel();
                MinesweeperView view = new MinesweeperView(model);
                MinesweeperController controller = new MinesweeperController(model, view);
                controller.runMinesweeper();
            }
        });
    }
}