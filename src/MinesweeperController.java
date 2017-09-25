import java.awt.event.*;
import javax.swing.*;

public class MinesweeperController {
    private final MinesweeperModel model;
    private final MinesweeperView view;

    private final GameAction gameAction;
    private final MenuAction menuAction;
    private final TimerControl timerControl;

    public MinesweeperController(MinesweeperModel model, MinesweeperView view) {
        this.model = model;
        this.view = view;

        this.menuAction = new MenuAction();
        this.menuAction.register();

        this.gameAction = new GameAction();
        this.gameAction.register();

        this.timerControl = new TimerControl();
    }

    public void runMinesweeper() {
        view.setVisible(true);
        view.updateStatusText(model.getStatusText());
        timerControl.startTimer();
    }

    private class GameAction extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if (!timerControl.isRunning())
                timerControl.resetTimer();

            if (model.clickedOnBoard(e.getX(), e.getY(), view.getCellSize())) {
                int index = model.getIndexFromCoordinate(e.getX(), e.getY(), view.getCellSize());
                String status = model.processClickAt(index, e.getButton());
                view.updateStatusText(status);
                view.refreshGameBoard();

                if (!model.gameInProgress())
                    timerControl.stopTimer();
            }
        }

        public void register() {
            view.registerGameBoardAction(this);
        }
    }

    private class MenuAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String gameType = ((JMenuItem) e.getSource()).getText();
            model.startGame(MinesweeperModel.GAME_TYPE.valueOf(gameType));
            view.refreshGameBoard();
            view.updateStatusText(model.getStatusText());
            timerControl.resetTimer();
        }

        public void register() {
            view.registerMenuItemAction(this);
        }
    }

    private class TimerControl implements ActionListener {
        private Timer timer;
        private int currentTime;
        private boolean isRunning;

        public TimerControl() {
            this.currentTime = 0;
            this.isRunning = false;
            this.timer = new Timer(1000, this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            currentTime++;
            updateTimeDisplay();
        }

        public boolean isRunning() {
            return this.isRunning;
        }

        public void startTimer() {
            this.timer.start();
            this.isRunning = true;
        }

        public void stopTimer() {
            this.timer.stop();
            this.isRunning = false;
        }

        public void resetTimer() {
            this.currentTime = 0;
            this.timer.restart();
            this.isRunning = true;
            updateTimeDisplay();
        }

        public void updateTimeDisplay() {
            view.updateTimerText(this.currentTime);
        }
    }
}