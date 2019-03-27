import java.awt.*;

public class Wall {
    int xPos;
    int yPos;
    Point pos;
    int width = 30;
    int height = 30;

    public Wall(int x, int y) {
        xPos = x;
        yPos = y;
        pos = new Point(x, y);
    }

    public void draw(Graphics g) {
        g.fillRect(xPos, yPos, width, height);
    }

}
