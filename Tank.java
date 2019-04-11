import javax.sound.sampled.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

public class Tank {

    private int tankDirection = 0;
    private int initialX;
    private int initialY;
    private int xPos;
    private int yPos;
    private Color treadColor = Color.white;
    private Color color;
    private int size = 30;
    private int turretAngle = 0;
    private boolean alive = true;
    private File explosionFile = new File("sounds/Explosion.wav");

    public Tank(int x, int y, int tankSize, Color tankColor) {
        initialX = x;
        initialY = y;
        xPos = x;
        yPos = y;
        size = tankSize;
        color = tankColor;
    }


    public void move(int direction, HashSet<Point> walls) {
        if(direction != 0) {
            tankDirection = direction;
        }
        if (!walls.contains(new Point(xPos, yPos-1)) &&
                !walls.contains(new Point(xPos + size / 2, yPos-1)) &&
                !walls.contains(new Point(xPos + size, yPos-1)) && direction == 1) {
            yPos--;
        } else if (!walls.contains(new Point(xPos-1, yPos)) &&
                !walls.contains(new Point(xPos-1, yPos + size / 2)) &&
                !walls.contains(new Point(xPos-1, yPos + size)) && direction == 2) {
            xPos--;
        } else if (!walls.contains(new Point(xPos, yPos + size+1)) &&
                !walls.contains(new Point(xPos + size / 2, yPos + size+1)) &&
                !walls.contains(new Point(xPos + size, yPos + size+1)) && direction == 3) {
            yPos++;
        } else if (!walls.contains(new Point(xPos + size+1, yPos)) &&
                !walls.contains(new Point(xPos + size+1, yPos + size / 2)) &&
                !walls.contains(new Point(xPos + size+1, yPos + size)) && direction == 4) {
            xPos++;
        } else{
            justHitWall = true;
            direction = (direction +2)%4;
        } // for computer mover
        if (yPos - size >= 225 && yPos + size <= 325 && xPos >= 350 && xPos <= 450) {
            teleport();
        }

    }

    public void turn(int turnDirection) {
        if (turnDirection == 1) {
            turretAngle++;
        } else if (turnDirection == 2) {
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

    public boolean isAlive() {
        return alive;
    }

    public void kill() {
        try {
            AudioInputStream explosionInput = AudioSystem.getAudioInputStream(explosionFile);
            Clip killTank = AudioSystem.getClip();
            killTank.open(explosionInput);
            killTank.start();
        }
        catch(UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Unable to play explosion file");
            System.err.println(e);
        }
        alive = false;
    }

    public void draw(Graphics2D g2d) {
        Color oldColor = g2d.getColor();
        g2d.setColor(color);
        if(tankDirection % 2 == 1 || tankDirection == 0) {
            g2d.fillRect(xPos + 22, yPos, 8, 30);
            g2d.setColor(treadColor);
            g2d.drawRect(xPos + 22, yPos, 8, 30);
            g2d.setColor(color);
            g2d.fillRect(xPos, yPos, 8, 30);
            g2d.setColor(treadColor);
            g2d.drawRect(xPos, yPos, 8, 30);
        }
        else {
            g2d.fillRect(xPos, yPos + 22, 30, 8);
            g2d.setColor(treadColor);
            g2d.drawRect(xPos, yPos + 22, 30, 8);
            g2d.setColor(color);
            g2d.fillRect(xPos, yPos, 30, 8);
            g2d.setColor(treadColor);
            g2d.drawRect(xPos, yPos, 30, 8);

        }
        //main compartment
        g2d.setColor(color);
        g2d.fillRect(xPos + 5, yPos + 5, 20, 20);
        g2d.setColor(treadColor);
        g2d.drawRect(xPos + 5, yPos + 5, 20, 20);
        g2d.rotate(Math.toRadians(-turretAngle), xPos + size / 2, yPos + size / 2);
        g2d.setColor(color);
        g2d.fillRect(xPos + 10, yPos + size / 2, 10, 20);
        g2d.setColor(treadColor);
        g2d.drawRect(xPos + 10, yPos + size / 2, 10, 20);
        g2d.setColor(color);
        g2d.fillOval(xPos + 5, yPos + 5, 20, 20);
        g2d.setColor(treadColor);
        g2d.drawOval(xPos + 5, yPos + 5, 20, 20);
        g2d.rotate(Math.toRadians(turretAngle), xPos + size / 2, yPos + size / 2);
        g2d.setColor(oldColor);
    }

    public void teleport() {
        xPos = initialX;
        yPos = initialY;
    }

    public void autoMove(Tank that, HashMap<Integer, Point> walls){
        int dir = 0;
        float xDistance = this.xPos - that.xPos;
        float yDistance = this.yPos - that.yPos;
        float range = 1;

        if(justHitWall){
            this.justHitWall= false;
            if(Math.abs(yDistance) > range) {
                if(yDistance>0){
                    dir = 1; // if other is under this then move down
                }
                else{
                    dir = 3; // if other is above this then move up
                }
            }
            else {
                // move along x axis first
                if (xDistance > 0) {
                    dir = 2; // if other is to the left of this then move left
                } else {
                    dir = 4; // if other is to the right of this then move right
                }
            }
        }
        else{
            if(Math.abs(xDistance) > range) {
                System.out.println("Moving along x axis");
                System.out.println(xDistance);

                // move along x axis first
                if (xDistance > 0) {
                    dir = 2; // if other is to the left of this then move left
                } else {
                    dir = 4; // if other is to the right of this then move right
                }
            }

            else {
                System.out.println(yDistance);

                if(yDistance>0){
                    System.out.println("Moving along y axis");
                    dir = 1; // if other is under this then move down
                }
                else{
                    dir = 3; // if other is above this then move up

                }
            }
        }


        move(dir, walls);
    } // added this

}