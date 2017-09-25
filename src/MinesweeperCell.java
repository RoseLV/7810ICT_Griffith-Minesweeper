import java.awt.Image;

public class MinesweeperCell {
    private final int x;
    private final int y;
    private final Image img;

    private String text = null;

    public MinesweeperCell(int x, int y, Image img) {
        this.x = x;
        this.y = y;
        this.img = img;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Image getImage() {
        return img;
    }

    public String getText() {
        return text;
    }

    public boolean hasText() {
        return text != null;
    }

    public void setText(String newText) {
        text = newText;
    }
}