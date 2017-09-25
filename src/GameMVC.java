import javax.swing.*;

public class GameMVC {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable(){

            @Override
            public void run() {
                // abstract class cannot be instantiated
                // instead, how to use three subclass which can be instantiated?
                Board gameModel = new SquareBoard();
                GameView view = new GameView(gameModel);
                GameController controller = new GameController(gameModel, view);
                controller.startGame();
            }
        });
    }
}
