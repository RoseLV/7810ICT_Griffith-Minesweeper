import javax.swing.*;

public class GameMVC {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable(){

            @Override
            public void run() {
                GameModel model = new GameModel();
                GameView view = new GameView(model);
                GameController controller = new GameController(model, view);
                controller.startGame();
            }
        });
    }
}
