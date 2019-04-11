import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class Main extends JFrame {

    // Creates two tank objects
    private Tank player1;
    private Tank player2;
    // Sets the color to look for in determining where walls are on the map
    private Color wallColor= new Color(100, 235, 96);
    // Variables are created to store the map's width and height when it is loaded
    private int mapWidth;
    private int mapHeight;
    // Variable to hold the image of the game map
    private BufferedImage map;
    // Holds the image of the start screen
    private BufferedImage startScreen;
    // Stores all active missiles for both tanks in the game
    private ArrayList<Missile> missiles = new ArrayList<>();
    // Boolean tracks whether the game has ended or not
    private boolean gameOver = false;
    // Color is created to draw the victory screen with at game over
    private Color winnerColor;
    // Boolean determines whether or not to display the start screen
    private boolean start = true;
    // String stores the winner of the game to be shown on the screen
    private String winner;
    // Variables are created to store the location and size of start screen buttons
    private int buttonX = 75;
    private int createMapX;
    private int buttonY;
    private int buttonWidth = 200;
    private int buttonHeight = 100;
    // A clip to play background music is created
    private Clip backgroundMusic;
    // Booleans store whether each tank is going a certain direction and the direction of turret turning
    private boolean left1, right1, down1, up1, left2, right2, down2, up2 = false;
    private boolean clockwise1, counterClockwise1, clockwise2, counterClockwise2 = false;
    // A HashMap is used to store the location of walls
    private HashSet<Point> walls;
    // A mouse adapter responds when a user clicks on either the start button or the map creation button
    private MouseAdapter getButton = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            // Paths to background music are stored as Strings
            String gameMusic = "sounds/gameMusic.wav";
            String mapCreation = "sounds/creationMusic.wav";
            // Checks if the user clicked the start button
            if(e.getX() <= buttonX + buttonWidth && e.getX() >= buttonX &&
                    e.getY() <= buttonY + buttonHeight && e.getY() >= buttonY) {
                // Removes the mouse listener and stops the start menu music
                removeMouseListener(getButton);
                backgroundMusic.stop();
                // Starts a thread for object updating
                objectUpdater gameController = new objectUpdater();
                gameController.start();
                loopMusic(gameMusic);
                // Exits the start state
                start = false;
            }
            // Checks if the user clicks the map creation button
            if(e.getX() <= createMapX + buttonWidth && e.getX() >= createMapX &&
                    e.getY() <= buttonY + buttonHeight && e.getY() >= buttonY) {
                // Gets rid of the current JFrame and stops the intro music
                dispose();
                backgroundMusic.stop();
                loopMusic(mapCreation);
                // Runs the map creation program with the background clip
                createMap.makeNewMap(backgroundMusic);
            }
        }
    };

    /** Loops music on the background clip
     *
     * @param gameMusic - The path to the wav file containing the background music
     */
    private void loopMusic(String gameMusic) {
        try {
            // Loads the game music and plays it on a loop
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(loadSound(gameMusic));
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
        }
        // If for some reason the music fails to play, the error is reported and the type of exception is printed
        catch(IOException | LineUnavailableException | NullPointerException ex) {
            System.err.println("Unable to play background music");
            System.err.println(ex);
        }
    }

    /** Loads an audio file into the audio input stream
     *
     * @param fileName - The path to the audio file
     * @return An AudioInputStream object connected to the specified file
     */
    private AudioInputStream loadSound(String fileName) {
        try {
            // Tries to load the file and return the stream
            return AudioSystem.getAudioInputStream(new File(fileName));
        }
        // Reports any errors and the exceptions generated and returns null
        catch(IOException | UnsupportedAudioFileException e) {
            System.err.println("Unable to open " + fileName);
            System.err.println(e);
            return null;
        }
    }

    /** Creates the HashSet containing all the walls in a map
     *
     */
    private void generateWalls() {
        // Sets the walls HashSet to an empty HashSet
        walls = new HashSet<>();
        // Iterates through every pixel in the map
        for(int i = 0; i < mapWidth; i++) {
            for(int j = 0; j < mapHeight; j++) {
                // Checks the color of the pixel in the map
                Color cellColor = new Color(map.getRGB(i, j));
                // If the pixel is close enough to the wall color it is added to the set
                if(cellColor.getRed() >= wallColor.getRed()-5 && cellColor.getRed() <= wallColor.getRed()+5 &&
                        cellColor.getGreen() >= wallColor.getGreen()-5 && cellColor.getGreen() <= wallColor.getGreen()+5 &&
                        cellColor.getBlue() >= wallColor.getBlue()-5 && cellColor.getBlue() <= wallColor.getBlue()+5) {
                    walls.add(new Point(i, j));
                }
            }
        }
    }

    /** Loads the custom font for the game
     *
     * @return A font generated from the 'zorque.ttf' text file
     */
    private Font loadFont() {
        try {
        // Creates the font to use
        Font gameFont = Font.createFont(Font.TRUETYPE_FONT, new File("zorque.ttf")).deriveFont(75f);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        // Registers the font
        ge.registerFont(gameFont);
        return gameFont;
        }
        // Handles failure to produce a font from the file
        catch (IOException | FontFormatException e) {
            e.printStackTrace();
            return null;
        }
    }
    // JPanel that the game is displayed on
    public class GamePanel extends JPanel {
        // Loads the custom game font
        Font gameFont = loadFont();
        // Sets the path to the start screen music
        String introMusic = "sounds/introMusic.wav";
        // Sets the default size of the tanks
        int tankSize = 30;

        /** Constructor for the game panel, calls all one-time operations necessary before the game can be played
         */
        private GamePanel() {
            // Sets the title of the graphics frame
            setTitle("Tanks");
            // Creates two tanks at opposite corners of the map
            player1 = new Tank(50, 50, tankSize, Color.red);
            player2 = new Tank(map.getWidth()-60, map.getHeight()-60, tankSize, Color.blue);
            // Creates the set of walls
            generateWalls();
            // Tries to start the intro music
            try {
                backgroundMusic = AudioSystem.getClip();
                AudioInputStream soundInput = loadSound(introMusic);
                backgroundMusic.open(soundInput);
                backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
            }
            // If unable to play intro music, the error and its exception is reported
            catch(LineUnavailableException | IOException | NullPointerException e) {
                System.err.println("Unable to play intro music");
                System.err.println(e);
            }
            // Sets the x location for the map creation button in the start screen
            createMapX = map.getWidth()-buttonX-buttonWidth;
            // sets the y location for both the start game and map creation buttons
            buttonY = map.getHeight()-150;
            // Allows for key listeners
            setFocusable(true);
            requestFocusInWindow();
            // Sets the background color to black
            super.setBackground(Color.black);
            // Starts to listen for the user to click the buttons
            addMouseListener(getButton);
            // Listens for player 1 directional input and handles it appropriately
            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_W) {
                        up1 = true;
                    }
                    if (e.getKeyCode() == KeyEvent.VK_A) {
                        left1 = true;
                    }
                    if (e.getKeyCode() == KeyEvent.VK_S) {
                        down1 = true;
                    }
                    if (e.getKeyCode() == KeyEvent.VK_D) {
                        right1 = true;
                    }
                    if (e.getKeyCode() == KeyEvent.VK_Q) {
                        counterClockwise1 = true;
                    }
                    if (e.getKeyCode() == KeyEvent.VK_E) {
                        clockwise1 = true;
                    }
                }
            });
            // If player 1 releases a key, the tank stops moving
            addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_W) {
                        up1 = false;
                    }
                    if (e.getKeyCode() == KeyEvent.VK_A) {
                        left1 = false;
                    }
                    if (e.getKeyCode() == KeyEvent.VK_S) {
                        down1 = false;
                    }
                    if (e.getKeyCode() == KeyEvent.VK_D) {
                        right1 = false;
                    }
                    if (e.getKeyCode() == KeyEvent.VK_Q) {
                        counterClockwise1 = false;
                    }
                    if (e.getKeyCode() == KeyEvent.VK_E) {
                        clockwise1 = false;
                    }

                }
            });
            // Listens for player 2 directional input and handles it appropriately
            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_UP) {
                        up2 = true;
                    }
                    if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                        left2 = true;
                    }
                    if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                        down2 = true;
                    }
                    if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                        right2 = true;
                    }
                    if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                        counterClockwise2 = true;
                    }
                    if (e.getKeyCode() == KeyEvent.VK_SLASH) {
                        clockwise2 = true;
                    }
                }
            });
            // If player 2 releases a key, the tank stops moving
            addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_UP) {
                        up2 = false;
                    }
                    if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                        left2 = false;
                    }
                    if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                        down2 = false;
                    }
                    if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                        right2 = false;
                    }
                    if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                        counterClockwise2 = false;
                    }
                    if (e.getKeyCode() == KeyEvent.VK_SLASH) {
                        clockwise2 = false;
                    }
                }
            });
            // If either player presses the fire button then a missile is added with the other player as its target
            addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_SPACE && player1.isAlive()) {
                        missiles.add(new Missile(player1, player2));
                    } else if (e.getKeyCode() == KeyEvent.VK_PERIOD && player2.isAlive()) {
                        missiles.add(new Missile(player2, player1));
                    }
                }
            });
        }


        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Downcasts to a Graphics2D object
            Graphics2D g2d = (Graphics2D) g;
            // Checks if the game is still in the start screen
            if(start) startScreen(g2d);
            // Runs the game if out of the start screen
            else {
                // Draws the map in the background
                g2d.drawImage(map, 0, 0, null);
                // Draws each missile currently in the game
                for (Missile missile : missiles) {
                    missile.draw(g2d);
                }
                // If the game ends, the winner is displayed in their color
                if (gameOver) {
                    g2d.setColor(winnerColor);
                    g2d.setFont(gameFont.deriveFont(75f));
                    g2d.drawString(winner + " wins!", 100, getHeight() / 2);
                }
                // If one of the players isn't alive, the game ends and the players are prompted to either play again or exit
                if (!player2.isAlive()) {
                    // If the game has not already been set to over, it is done so here
                    if(!gameOver) {
                        // The option pane is given its own thread so that the game thread can keep working
                        new endgamePrompt().start();
                        // The game is set to over and the appropriate winner color and string is assigned
                        gameOver = true;
                        winnerColor = Color.red;
                        winner = "Player 1";
                    }

                }
                else {
                    // If player 2 is alive, then their tank is drawn
                    player2.draw(g2d);
                }
                if (!player1.isAlive()) {
                    if(!gameOver) {
                        new endgamePrompt().start();
                        gameOver = true;
                        winnerColor = Color.blue;
                        winner = "Player 2";
                        add(new JOptionPane());
                    }
                }
                else {
                    // If player 1 is alive, then their tank is drawn
                    player1.draw(g2d);
                }
            }
            // All the drawing that occurred is painted on the panel
            repaint();

        }


        /** Displays the start screen and creates the selection buttons
         *
         * @param g2d - the Graphics2D object to use in drawing
         */
        private void startScreen(Graphics2D g2d) {
            // Draws the start screen background image starting at the top left corner
            g2d.drawImage(startScreen, 0, 0, null);
            // Sets the color and font for the title and then draws it in the center
            g2d.setFont(gameFont);
            g2d.setColor(Color.blue);
            g2d.drawString("Tanks", getWidth()/2-120, 75);
            // Sets the font for the subtitle and draws it under the title
            g2d.setFont(new Font("TimesNewRoman", Font.PLAIN, 25));
            g2d.drawString("(Inspired by the tank level from Tron)", getWidth()/2-200, 100);
            // Creates a visible play button
            g2d.fillRect(buttonX, buttonY, buttonWidth, buttonHeight);
            // Draws the text for the play button in black text in the custom font
            g2d.setColor(Color.black);
            g2d.drawRect(buttonX+2, buttonY+2, buttonWidth-4, buttonHeight-4);
            g2d.setFont(gameFont.deriveFont(Font.BOLD, 25f));
            g2d.drawString("Start", buttonX+60, buttonY+60);
            // Draws a visible map creation button in red
            g2d.setColor(Color.red);
            g2d.fillRect(createMapX, buttonY, buttonWidth, buttonHeight);
            // Draws the text for the map creation button in black text in the custom font
            g2d.setColor(Color.black);
            g2d.drawRect(createMapX+2, buttonY+2, buttonWidth-4, buttonHeight-4);
            g2d.setFont(gameFont.deriveFont(Font.BOLD, 25f));
            g2d.drawString("Custom Map", createMapX+10, buttonY+60);

        }
    }

    /** Handles all object updating for the game
     */
    private class objectUpdater extends Thread {

        @Override
        public void run() {
            // The updates are done approximately 30 times per second to allow for fluid animation
            Timer updateAnimationTimer = new Timer(33, (ActionListener) -> {
                // The direction of tank and turret movement are stored as integers, assigned from player 1 first, then
                // player 2
                int direction = getP1Move();
                int turn = getP1Turret();
                // After the tank and turret direction for player 1 is found, the tank is updated accordingly
                player1.move(direction, walls);
                player1.turn(turn);
                direction = getP2Move();
                turn = getP2Turret();
                // After the tank and turret direction for player 2 is found, the tank is updated accordingly
                player2.move(direction, walls);
                player2.turn(turn);
                // Since attempting to modify an ArrayList while iterating through it will raise a concurrent modification
                // exception, the missiles that die in this frame are stored in their own ArrayList and deleted after
                // iteration
                ArrayList<Missile> deadMissiles = new ArrayList<>();
                // Updates every missile and reads which ones are dead
                for (Missile missile : missiles) {
                    // If a missile is alive, it keeps moving, otherwise it is added to the dead list for deletion
                    if (missile.isAlive()) {
                        missile.update(walls);
                    } else {
                        deadMissiles.add(missile);
                    }
                }
                // Removes all dead missiles from the current frame
                missiles.removeAll(deadMissiles);
            });
            // Starts the object update timer
            updateAnimationTimer.start();
        }
    }

    /** Displays the post-game prompt on its own thread to allow the game to continue to operate while it waits for input
     */
    private class endgamePrompt extends Thread {

        @Override
        public void run() {
            // Since dialog box options are taken as an array, that array is created here
            String[] options = new String[]{"Play Again", "Quit"};
            // Displays a yes/no dialog box and gets the button the user presses as an integer
            int choice = JOptionPane.showOptionDialog(null, "Quit Game or Play Again?",
                    "Game Over", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                    options, options[0]);
            // If the user clicks yes, then the program restarts; otherwise, the program exits
            if (choice == JOptionPane.YES_OPTION) {
                dispose();
                backgroundMusic.stop();
                main(null);
            }
            else {
                System.exit(0);
            }
        }

    }

    /** Gets the direction that player 1's turret is going
     *
     * @return 1 for clockwise, 2 for counterclockwise, and 0 for not moving
     */
    private int getP1Turret() {
        if (clockwise1) {
            return 1;
        }
        if (counterClockwise1) {
            return 2;
        }
        return 0;
    }

    /** Gets the direction that player 2's turret is going
     *
     * @return 1 for clockwise, 2 for counterclockwise, and 0 for not moving
     */
    private int getP2Turret() {
        if (clockwise2) {
            return 1;
        }
        if (counterClockwise2) {
            return 2;
        }
        return 0;
    }

    /** Gets the direction that player 1's tank is going
     *
     * @return 1 for up, 2 for left, 3 for down, 4 for right and 0 for not moving
     */
    private int getP1Move() {
        return getMove(up1, left1, down1, right1);
    }

    /** Gets the direction that player 2's tank is going
     *
     * @return 1 for up, 2 for left, 3 for down, 4 for right and 0 for not moving
     */
    private int getP2Move() {
        return getMove(up2, left2, down2, right2);
    }

    /** Gets the direction that a player's tank is going
     *
     * @return 1 for up, 2 for left, 3 for down, 4 for right and 0 for not moving
     */
    private int getMove(boolean up, boolean left, boolean down, boolean right) {
        if (up) {
            return 1;
        }
        if (left) {
            return 2;
        }
        if (down) {
            return 3;
        }
        if (right) {
            return 4;
        }
        return 0;
    }



    /** Constructor for the game frame
     */
    private Main() {
        // The difference between frame size and panel size
        int yFrameOffset = 35;
        int xFrameOffset = 12;
        // Tries to read from a user created background file
        try {
            // Creates the start screen image using the path to the image file
            startScreen = ImageIO.read(new File("startBackground.jpg"));
            // Tries to look for and load a user created map
            map = ImageIO.read(new File("Background.jpg"));
            // Stores the width and height of the map
            mapWidth = map.getWidth(null);
            mapHeight = map.getHeight(null);
            // Sets the program to exit when the graphics window is closed
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            // Sets the size according to the map size and frame offsets
            setSize(mapWidth+xFrameOffset, mapHeight+yFrameOffset);
            // Prevents the user from resizing the game
            setResizable(false);
            // Adds the drawing panel for the game
            add(new GamePanel());
        }
        // If no user created background file exists, the default background is used
        catch(IOException ex) {
            try {
                // Loads the default map of the game
                map = ImageIO.read(new File("defaultBackground.jpg"));
                // Stores the width and height of the map
                mapWidth = map.getWidth(null);
                mapHeight = map.getHeight(null);
                // Sets the program to exit when the graphics window is closed
                setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                // Sets the size according to the map size and frame offsets
                setSize(mapWidth+xFrameOffset, mapHeight+yFrameOffset);
                // Prevents the user from resizing the game
                setResizable(false);
                // Adds the drawing panel for the game
                add(new GamePanel());
            }
            // If no map can be read, then the program prints an error and exits
            catch(IOException e) {
                System.err.println("No map file found");
            }
        }
    }

    public static void main(String[] args) {
        // Starts the game
        EventQueue.invokeLater(() -> new Main().setVisible(true));

    }
}