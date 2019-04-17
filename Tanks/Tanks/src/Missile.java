import javax.sound.sampled.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;

class Missile {

    // Tracks how many frames a missile has been alive for
    private int age = 0;
    // Sets the radius of each missile
    private int radius = 10;
    // Each missile starts out alive and lasts until its age expires
    private boolean alive = true;
    // Missiles are white by default
    private Color color = Color.white;
    // Allows for the missile to not hurt its owner
    private Tank target;
    // The velocity in each direction of the missile
    private float xVelocity;
    private float yVelocity;
    // The current position of the missile
    private float xPos;
    private float yPos;
    // The clip to play firing and ricochet sound effects on
    private Clip soundEffects;

    /** Constructor for the missile class
     *
     * @param owner - the tank that fires the missile
     * @param otherPlayer - the tank that the missile is fired at
     */
    Missile(Tank owner, Tank otherPlayer) {
        // Attempts to load and play the firing sound effect at the initialization of each missile
        try {
            soundEffects = AudioSystem.getClip();
            String fireFile = "sounds/Fire.wav";
            soundEffects.open(loadSound(fireFile));
            soundEffects.start();
        }
        // If unable to play, an error is reported to the console
        catch(LineUnavailableException | IOException e) {
            System.err.println("Unable to play fire sound");
        }
        // The initial position of the missile is set to the initial position of its owner
        xPos = owner.getxPos()+owner.getSize()/2- radius;
        yPos = owner.getyPos()+owner.getSize()/2- radius;
        // The target is set
        target = otherPlayer;
        // The x and y velocities are determined by the angle of the owner's turret
        xVelocity = (float) Math.sin(Math.toRadians(owner.getTurretAngle()));
        yVelocity = (float) Math.cos(Math.toRadians(owner.getTurretAngle()));
    }

    /** Moves missiles and causes them to bounce off walls
     *
     * @param walls - the set of all walls in the game
     */
    void update(HashSet<Point> walls) {
        // The max number of movements a missile can make before expiring
        int maxAge = 600;
        // Stores the path to the ricochet sound file
        String ricochetFile = "sounds/Ricochet.wav";
        // Checks if the missile hits a wall in the x or y direction
        boolean hitWallX = false;
        boolean hitWallY = false;
        // Runs through every point on the perimeter of the missile
        for(int i = 1; i < radius*2; i++) {
            if(walls.contains(new Point((int) xPos+i, (int) yPos)) ||
                    walls.contains(new Point((int) xPos+i, (int) yPos+radius*2))) {
                hitWallY = true;
            }
            if(walls.contains(new Point((int) xPos,(int) yPos+i)) ||
                    walls.contains(new Point((int) xPos+radius*2, (int) yPos+i))) {
                hitWallX = true;
            }
        }
        // If the missile hits a wall, it rebounds in the opposite direction
        if(hitWallX || hitWallY) {
            try {
                // Plays the ricochet sound
                soundEffects = AudioSystem.getClip();
                soundEffects.open(loadSound(ricochetFile));
                soundEffects.start();
            }
            // Tells the console if the ricochet sound failed to play
            catch (LineUnavailableException | IOException e) {
                System.err.println("Unable to play ricochet sound");
            }
        }
        if(hitWallX) {
            // Missile rebounds off the wall into the other direction
            xVelocity *= -1;
        }
        if(hitWallY) {
            // Missile rebounds off the wall into the other direction
            yVelocity *= -1;
        }
        // Updates the missile's position
        xPos += xVelocity;
        yPos += yVelocity;
        // Adds one frame to the age of the missile
        age++;
        // If the missile hits the target tank, it kills it and itself
        if(xPos+ radius >= target.getxPos() && xPos <= target.getxPos()+target.getSize() && yPos+ radius >= target.getyPos() &&
                yPos <= target.getyPos()+target.getSize() && target.isAlive()) {
            alive = false;
            target.kill();
        }
        // If the missile's age expires then it dies
        if(age >= maxAge) {
            alive = false;
        }
    }

    /** Loads a sound effect for playing
     *
     * @param fileName - the path to the sound file
     * @return An AudioInputStream linked to the sound file
     */
    private AudioInputStream loadSound(String fileName) {
        try {
            return AudioSystem.getAudioInputStream(new File(fileName));
        }
        catch(IOException | UnsupportedAudioFileException e) {
            System.err.println("Unable to open " + fileName);
            System.err.println(e);
            return null;
        }
    }

    // Returns whether the missile is alive or not
    boolean isAlive() {
        return alive;
    }

    /** Draws the missile in the graphics window
     *
     * @param g2d - the Graphics2D object to use in drawing
     */
    void draw(Graphics2D g2d) {
        // Stores the color of g2d
        Color oldColor = g2d.getColor();
        // Draws the missile at its location
        g2d.setColor(color);
        g2d.fillOval((int) xPos+ radius /2, (int) yPos+ radius /2, radius, radius);
        // Returns the graphics object to its original color
        g2d.setColor(oldColor);
    }
}