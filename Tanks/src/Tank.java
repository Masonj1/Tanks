import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Tank {
    int xPos;
    int yPos;
    Point pos;
    Color color = Color.red;
    int size = 50;
    int turretAngle = 0;
    boolean alive = true;

    public Tank(int x, int y, int tankSize) {
        xPos = x;
        yPos = y;
        size = tankSize;
    }

    public ArrayList<Image> getExplosion() {
        ArrayList<Image> explosion = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            try {
                Image frame = ImageIO.read(new File("Sprites/General/Explosion" + i));
                explosion.add(frame);
            }
            catch (IOException ex) {
                System.err.println("Error: Sprite not found");
            }
        }
        alive = false;
        return explosion;
    }


    public void move(int direction, HashMap<Integer, Point> walls) {
        if(!walls.containsValue(new Point(xPos, yPos-1)) &&
                !walls.containsValue(new Point(xPos+size/2, yPos-1)) &&
                !walls.containsValue(new Point(xPos+size, yPos-1)) && direction == 1) {
            yPos--;
        }
        else if(!walls.containsValue(new Point(xPos-1, yPos)) &&
                !walls.containsValue(new Point(xPos-1, yPos+size/2)) &&
                !walls.containsValue(new Point(xPos-1, yPos+size)) && direction == 2) {
            xPos--;
        }
        else if(!walls.containsValue(new Point(xPos, yPos+size+1)) &&
                !walls.containsValue(new Point(xPos+size/2, yPos+size)) &&
                !walls.containsValue(new Point(xPos+size, yPos+size)) && direction == 3) {
            yPos++;
        }
        else if(!walls.containsValue(new Point(xPos+size, yPos)) &&
                !walls.containsValue(new Point(xPos+size, yPos+size/2)) &&
                !walls.containsValue(new Point(xPos+size, yPos+size)) && direction == 4) {
            xPos++;
        }

    }

    public void turn(int turnDirection) {
        if(turnDirection == 1) {
            turretAngle--;
        }
        else if(turnDirection == 2) {
            turretAngle++;
        }
    }

    public int getSize() {
        return size;
    }



    public void draw(Graphics2D g2d) {
        Color oldColor = g2d.getColor();
        g2d.setColor(color);
        g2d.fillRect(xPos, yPos, size, size);
        g2d.rotate(Math.toRadians(turretAngle));
        g2d.fillRect(xPos+size/4, yPos+size/4, size/2, size);
        g2d.setColor(oldColor);
    }

}
