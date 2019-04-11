import javax.sound.sampled.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;

public class Missile {

    private int age = 0;
    private int velocity = 3;
    private int size = 10;
    private boolean alive = true;
    private Color color = Color.white;
    private Tank target;
    private float xVelocity;
    private float yVelocity;
    private float xPos;
    private float yPos;
    private Clip soundEffects;
    private String fireFile = "sounds/Fire.wav";
    private String ricochetFile = "sounds/Ricochet.wav";

    Missile(Tank owner, Tank otherPlayer) {
        try {
            soundEffects = AudioSystem.getClip();
            soundEffects.open(loadSound(fireFile));
            soundEffects.start();
        }
        catch(LineUnavailableException | IOException e) {
            System.err.println("Unable to play fire sound");
        }
        xPos = owner.getxPos()+owner.getSize()/2-size;
        yPos = owner.getyPos()+owner.getSize()/2-size;
        target = otherPlayer;
        xVelocity = (float) Math.sin(Math.toRadians(owner.getTurretAngle()))*velocity;
        yVelocity = (float) Math.cos(Math.toRadians(owner.getTurretAngle()))*velocity;
    }

    void update(HashSet<Point> walls) {
        if(xVelocity > 0 && walls.contains(new Point((int) xPos+size*2, (int) yPos+size)) ||
                xVelocity < 0 && walls.contains(new Point((int) xPos, (int) yPos+size))) {
            try {
                soundEffects = AudioSystem.getClip();
                soundEffects.open(loadSound(ricochetFile));
                soundEffects.start();
            }
            catch(LineUnavailableException | IOException e) {
                System.err.println("Unable to play ricochet sound");
            }
            xVelocity *= -1;
        }
        if(yVelocity > 0 && walls.contains(new Point((int) xPos+size, (int) yPos+size*2)) ||
                yVelocity < 0 && walls.contains(new Point((int) xPos+size, (int) yPos))) {
            yVelocity *= -1;
            try {
                soundEffects = AudioSystem.getClip();
                soundEffects.open(loadSound(ricochetFile));
                soundEffects.start();
            }
            catch(LineUnavailableException | IOException e) {
                System.err.println("Unable to play ricochet sound");
            }
        }
        xPos += xVelocity;
        yPos += yVelocity;
        age++;
        if(xPos+size >= target.getxPos() && xPos <= target.getxPos()+target.getSize() && yPos+size >= target.getyPos() &&
                yPos <= target.getyPos()+target.getSize() && target.isAlive()) {
            alive = false;
            target.kill();
        }
        if(age >= 200) {
            alive = false;
        }
    }

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

    boolean isAlive() {
        return alive;
    }

    void draw(Graphics2D g2d) {
        Color oldColor = g2d.getColor();
        g2d.setColor(color);
        g2d.fillOval((int) xPos+size/2, (int) yPos+size/2, size, size);
        g2d.setColor(oldColor);
    }
}