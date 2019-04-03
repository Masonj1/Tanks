import java.awt.*;
import java.util.HashMap;

public class Missile {

    int age = 0;
    int velocity = 3;
    int size = 10;
    boolean alive = true;
    Color color = Color.white;
    Tank target;
    float xVelocity;
    float yVelocity;
    float xPos;
    float yPos;

    public Missile(Tank owner, Tank otherPlayer) {
        xPos = owner.getxPos()+owner.getSize()/2-size;
        yPos = owner.getyPos()+owner.getSize()/2-size;
        target = otherPlayer;
        xVelocity = (float) Math.sin(Math.toRadians(owner.getTurretAngle()))*velocity;
        yVelocity = (float) Math.cos(Math.toRadians(owner.getTurretAngle()))*velocity;
    }

    public void update(HashMap<Integer, Point> walls) {
        if(xVelocity > 0 && walls.containsValue(new Point((int) xPos+size*2, (int) yPos+size)) ||
                xVelocity < 0 && walls.containsValue(new Point((int) xPos, (int) yPos+size))) {
            xVelocity *= -1;
        }
        if(yVelocity > 0 && walls.containsValue(new Point((int) xPos+size, (int) yPos+size*2)) ||
                yVelocity < 0 && walls.containsValue(new Point((int) xPos+size, (int) yPos))) {
            yVelocity *= -1;
        }
        xPos += xVelocity;
        yPos += yVelocity;
        age++;
        if(xPos+size >= target.getxPos() && xPos <= target.getxPos()+target.getSize() && yPos+size >= target.getyPos() &&
                yPos <= target.getyPos()+target.getSize()) {
            alive = false;
            target.kill();
        }
        if(age == 200) {
            alive = false;
        }
    }

    public void draw(Graphics2D g2d) {
        Color oldColor = g2d.getColor();
        g2d.setColor(color);
        g2d.fillOval((int) xPos+size/2, (int) yPos+size/2, size, size);
        g2d.setColor(oldColor);
    }
}
