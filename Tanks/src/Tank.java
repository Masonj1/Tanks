import java.awt.*;
import java.util.HashMap;

public class Tank {
    private int tankDirection = 1;
    private int initialX;
    private int initialY;
    private int xPos;
    private int yPos;
    private Color treadColor = Color.white;
    private Color color;
    private int size = 30;
    private int turretAngle = 0;
    private boolean alive = true;

    public Tank(int x, int y, int tankSize, Color tankColor) {
        initialX = x;
        initialY = y;
        xPos = x;
        yPos = y;
        size = tankSize;
        color = tankColor;
    }

    public Tank(int x, int y, Color tankColor) {
        xPos = x;
        yPos = y;
        color = tankColor;
    }


    public void move(int direction, HashMap<Integer, Point> walls) {
        tankDirection = direction;
        if(!walls.containsValue(new Point(xPos, yPos)) &&
                !walls.containsValue(new Point(xPos+size/2, yPos)) &&
                !walls.containsValue(new Point(xPos+size, yPos)) && direction == 1) {
            yPos--;
        }
        else if(!walls.containsValue(new Point(xPos, yPos)) &&
                !walls.containsValue(new Point(xPos, yPos+size/2)) &&
                !walls.containsValue(new Point(xPos, yPos+size)) && direction == 2) {
            xPos--;
        }
        else if(!walls.containsValue(new Point(xPos, yPos+size)) &&
                !walls.containsValue(new Point(xPos+size/2, yPos+size)) &&
                !walls.containsValue(new Point(xPos+size, yPos+size)) && direction == 3) {
            yPos++;
        }
        else if(!walls.containsValue(new Point(xPos+size, yPos)) &&
                !walls.containsValue(new Point(xPos+size, yPos+size/2)) &&
                !walls.containsValue(new Point(xPos+size, yPos+size)) && direction == 4) {
            xPos++;
        }
        if(yPos-size >= 225 && yPos+size <= 325 && xPos >= 350 && xPos <= 450) {
            teleport();
        }

    }

    public void turn(int turnDirection) {
        if(turnDirection == 1) {
            turretAngle++;
        }
        else if(turnDirection == 2) {
            turretAngle--;
        }
    }

    public int getSize() {
        return size;
    }

    public int getxPos() {
        return xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public int getTurretAngle() {
        return turretAngle;
    }

    public void setTurretAngle(int turretAngle) {
        this.turretAngle = turretAngle;
    }

    public boolean isAlive() {
        return alive;
    }

    public void kill() {
        alive = false;
    }

    public void draw(Graphics2D g2d) {
        Color oldColor = g2d.getColor();
        g2d.setColor(color);
        g2d.fillRect(xPos + 22, yPos, 8, 30);
        g2d.setColor(treadColor);
        g2d.drawRect(xPos + 22, yPos, 8, 30);
        g2d.setColor(color);
        g2d.fillRect(xPos, yPos, 8, 30);
        g2d.setColor(treadColor);
        g2d.drawRect(xPos, yPos, 8, 30);
        //main compartment
        g2d.setColor(color);
        g2d.fillRect(xPos + 5, yPos + 5, 20, 20);
        g2d.setColor(treadColor);
        g2d.drawRect(xPos + 5, yPos + 5, 20, 20);
        g2d.rotate(Math.toRadians(-turretAngle), xPos+size/2, yPos+size/2);
        g2d.setColor(color);
        g2d.fillRect(xPos+10, yPos+size/2, 10, 20);
        g2d.setColor(treadColor);
        g2d.drawRect(xPos+10, yPos+size/2, 10, 20);
        g2d.setColor(color);
        g2d.fillOval(xPos+5, yPos+5, 20, 20);
        g2d.setColor(treadColor);
        g2d.drawOval(xPos+5, yPos+5, 20, 20);
        g2d.rotate(Math.toRadians(turretAngle), xPos+size/2, yPos+size/2);
        g2d.setColor(oldColor);
    }

    public void teleport() {
        xPos = initialX;
        yPos = initialY;
    }

}
