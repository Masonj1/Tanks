import javax.sound.sampled.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;

class Tank {
    // The tank is initially not moving in any direction
    private int tankDirection = 0;
    // Stores the initial position of the tank
    private int initialX;
    private int initialY;
    // Keeps track of the tank's position
    private int xPos;
    private int yPos;
    // Stores the color of the outline and the main color of the tank
    private Color treadColor = Color.white;
    private Color color;
    // Keeps the size of the tank
    private int size;
    // Keeps the angle that the turret is at
    private int turretAngle = 0;
    // The tank starts out as alive
    private boolean alive = true;
    // Path to the explosion sound effect file
    private File explosionFile = new File("sounds/Explosion.wav");

    /** Constructor for the tank class
     *
     * @param x - the initial x position of the tank
     * @param y - the initial y position of the tank
     * @param tankSize - the size of the tank
     * @param tankColor - the main color of the tank
     */
    Tank(int x, int y, int tankSize, Color tankColor) {
        // Assigns attributes to the tank according to arguments
        initialX = x;
        initialY = y;
        xPos = x;
        yPos = y;
        size = tankSize;
        color = tankColor;
    }

    /** Stores the space that the tank takes up as an ArrayList of ArrayLists of Points
     *
     * @return - An ArrayList containing all points on the perimeter of the tank, organized top, left, bottom, then right
     */
    private ArrayList<ArrayList<Point>> TankSpace() {
        // Stores all points on the specified side of the tank
        ArrayList<Point> right = new ArrayList<>();
        ArrayList<Point> left = new ArrayList<>();
        ArrayList<Point> top = new ArrayList<>();
        ArrayList<Point> bottom = new ArrayList<>();
        // Stores all of the perimeter ArrayLists
        ArrayList<ArrayList<Point>> tankSpace = new ArrayList<>();
        // Adds all points on the perimeter of the tank to their respective ArrayLists
        for(int i = 0; i <= size; i++) {
            top.add(new Point(xPos+i, yPos));
            bottom.add(new Point(xPos+i, yPos+size));
            left.add(new Point(xPos, yPos+i));
            right.add(new Point(xPos+size, yPos+i));
        }
        // Adds the ArrayLists comprising the tanks perimeter together
        tankSpace.add(top);
        tankSpace.add(left);
        tankSpace.add(bottom);
        tankSpace.add(right);
        // Returns the result
        return tankSpace;
    }
    /** Moves the tank according to directional input
     *
     * @param direction - the direction that the tank is trying to go
     * @param walls - the set of all walls in the environment
     */
    void move(int direction, HashSet<Point> walls) {
        // If the tank is moving, it is set to face the direction it is moving in
        if(direction != 0) {
            tankDirection = direction;
        }
        boolean move = true;
        ArrayList<ArrayList<Point>> tankSpace = TankSpace();
        // The tank moves in the direction it is assigned as long as there is no wall
        if (direction == 1) {
            // The top of the tank is checked for walls before moving up
            for(Point p : tankSpace.get(0)) {
                if(walls.contains(new Point((int) p.getX(), (int) p.getY()-1))) {
                    move = false;
                }
            }
            if(move) {
                yPos--;
            }
        }
        else if (direction == 2) {
            // The left side of the tank is checked for walls before moving left
            for(Point p : tankSpace.get(1)) {
                if(walls.contains(new Point((int) p.getX()-1, (int) p.getY()))) {
                    move = false;
                }
            }
            if(move) {
                xPos--;
            }
        }
        else if (direction == 3) {
            // The bottom of the tank is checked for walls before moving down
            for(Point p : tankSpace.get(2)) {
                if(walls.contains(new Point((int) p.getX(), (int) p.getY()+1))) {
                    move = false;
                }
            }
            if(move) {
                yPos++;
            }
        }
        else if (direction == 4) {
            // The right side of the tank is checked for walls before moving right
            for(Point p : tankSpace.get(3)) {
                if(walls.contains(new Point((int) p.getX()+1, (int) p.getY()))) {
                    move = false;
                }
            }
            if(move) {
                xPos++;
            }
        }
        // If the tank is in the teleporter then it is sent back to its initial position
        if (yPos - size >= 225 && yPos + size <= 325 && xPos >= 350 && xPos <= 450) {
            teleport();
        }

    }

    /** Turns the tank's turret according to directional input
     *
     * @param turnDirection - the direction to turn the turret
     */
    void turn(int turnDirection) {
        // Turns clockwise, counterclockwise, or nowhere
        if (turnDirection == 1) {
            turretAngle++;
        } else if (turnDirection == 2) {
            turretAngle--;
        }
    }

    // Returns the size of the tank
    int getSize() {
        return size;
    }

    // Returns the x position of the tank
    int getxPos() {
        return xPos;
    }

    // Returns the y position of the tank
    int getyPos() {
        return yPos;
    }

    // Returns the current angle of the turret
    int getTurretAngle() {
        return turretAngle;
    }

    // Returns a boolean for whether the tank is alive or not
    boolean isAlive() {
        return alive;
    }

    /** Kills the tank and plays the explosion sound effect
     */
    void kill() {
        try {
            // Loads the explosion sound effect into a new clip and plays it
            AudioInputStream explosionInput = AudioSystem.getAudioInputStream(explosionFile);
            Clip killTank = AudioSystem.getClip();
            killTank.open(explosionInput);
            killTank.start();
        }
        // If unable to play the explosion sound, the error and its exception is reported
        catch(UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Unable to play explosion file");
            System.err.println(e);
        }
        // The tank is killed
        alive = false;
    }

    /** Method by which the tank draws itself
     *
     * @param g2d - the Graphics2D object with which to draw the tank
     */
    void draw(Graphics2D g2d) {
        // Saves the color of g2d and sets the new color to the tank's color
        Color oldColor = g2d.getColor();
        g2d.setColor(color);
        // If the tank is facing up or down, the treads are drawn vertically
        if(tankDirection % 2 == 1 || tankDirection == 0) {
            g2d.fillRect(xPos + 22, yPos, 8, 30);
            g2d.setColor(treadColor);
            g2d.drawRect(xPos + 22, yPos, 8, 30);
            g2d.setColor(color);
            g2d.fillRect(xPos, yPos, 8, 30);
            g2d.setColor(treadColor);
            g2d.drawRect(xPos, yPos, 8, 30);
        }
        // If the tank is facing sideways, the treads are drawn horizontally
        else {
            g2d.fillRect(xPos, yPos + 22, 30, 8);
            g2d.setColor(treadColor);
            g2d.drawRect(xPos, yPos + 22, 30, 8);
            g2d.setColor(color);
            g2d.fillRect(xPos, yPos, 30, 8);
            g2d.setColor(treadColor);
            g2d.drawRect(xPos, yPos, 30, 8);

        }
        // After the treads, the main compartment is drawn on top
        g2d.setColor(color);
        g2d.fillRect(xPos + 5, yPos + 5, 20, 20);
        g2d.setColor(treadColor);
        g2d.drawRect(xPos + 5, yPos + 5, 20, 20);
        // The graphics object is rotated to the current turret angle and transformed to the origin and then the turret
        // is drawn on top of the main compartment
        g2d.rotate(Math.toRadians(-turretAngle), xPos + size / 2, yPos + size / 2);
        g2d.setColor(color);
        g2d.fillRect(xPos + 10, yPos + size / 2, 10, 20);
        g2d.setColor(treadColor);
        g2d.drawRect(xPos + 10, yPos + size / 2, 10, 20);
        g2d.setColor(color);
        g2d.fillOval(xPos + 5, yPos + 5, 20, 20);
        g2d.setColor(treadColor);
        g2d.drawOval(xPos + 5, yPos + 5, 20, 20);
        // After the turret is drawn, the graphics object is rotated back and the old color is restored
        g2d.rotate(Math.toRadians(turretAngle), xPos + size / 2, yPos + size / 2);
        g2d.setColor(oldColor);
    }

    /** Sends the tank back to its initial position
     */
    private void teleport() {
        xPos = initialX;
        yPos = initialY;
    }


}