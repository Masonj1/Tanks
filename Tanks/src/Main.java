import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Main extends JFrame {

    private int tankSize = 30;
    private Color wallColor= new Color(100, 235, 96);
    private int mapWidth;
    private int mapHeight;
    private BufferedImage map;
    private BufferedImage startScreen;
    private ArrayList<Missile> missiles = new ArrayList<>();
    private boolean gameOver = false;
    private String winner;
    private boolean start = true;
    private int buttonX = 75;
    private int createMapX;
    private int buttonY;
    private int buttonWidth = 200;
    private int buttonHeight = 100;
    private Color winnerColor;
    private MouseAdapter getButton = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            if(e.getX() <= buttonX + buttonWidth && e.getX() >= buttonX &&
                    e.getY() <= buttonY + buttonHeight && e.getY() >= buttonY) {
                removeMouseListener(getButton);
                start = false;
            }
            if(e.getX() <= createMapX + buttonWidth && e.getX() >= createMapX &&
                    e.getY() <= buttonY + buttonHeight && e.getY() >= buttonY) {
                dispose();
                createMap.makeNewMap();
            }
        }
    };

    private HashMap<Integer, Point> generateWalls() {
        HashMap<Integer, Point> walls = new HashMap<>();
        int key = 0;
        for(int i = 0; i < mapWidth; i++) {
            for(int j = 0; j < mapHeight; j++) {
                Color cellColor = new Color(map.getRGB(i, j));

                if(cellColor.getRed() >= wallColor.getRed()-5 && cellColor.getRed() <= wallColor.getRed()+5 &&
                        cellColor.getGreen() >= wallColor.getGreen()-5 && cellColor.getGreen() <= wallColor.getGreen()+5 &&
                        cellColor.getBlue() >= wallColor.getBlue()-5 && cellColor.getBlue() <= wallColor.getBlue()+5) {
                    walls.put(key, new Point(i, j));
                    key++;
                }
            }
        }
        return walls;
    }

    private Font loadFont() {
        try {
        //Creates the font to use
        Font gameFont = Font.createFont(Font.TRUETYPE_FONT, new File("zorque.ttf")).deriveFont(75f);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        //Registers the font
        ge.registerFont(gameFont);
        return gameFont;
        }
        catch (IOException | FontFormatException e) {
            e.printStackTrace();
            return null;
        }
    }

    public class GamePanel extends JPanel {
        Tank player1 = new Tank(50, 50, tankSize, Color.red);
        Tank player2 = new Tank(map.getWidth()-60, map.getHeight()-60, tankSize, Color.blue);
        boolean left1, right1, down1, up1, left2, right2, down2, up2 = false;
        boolean clockwise1, counterClockwise1, clockwise2, counterClockwise2 = false;
        HashMap<Integer, Point> walls = generateWalls();
        Font gameFont = loadFont();


        private GamePanel() {
            createMapX = map.getWidth()-buttonX-buttonWidth;
            buttonY = map.getHeight()-150;
            setFont(gameFont);
            setFocusable(true);
            requestFocusInWindow();
            super.setBackground(Color.black);
            addMouseListener(getButton);
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
            addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_SPACE && player1.isAlive()) {
                        missiles.add(new Missile(player1, player2));
                    } else if (e.getKeyCode() == KeyEvent.VK_ALT && player2.isAlive()) {
                        missiles.add(new Missile(player2, player1));
                    }
                }
            });
        }


        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            if(start) startScreen(g2d);
            else {
                g2d.drawRect(0,0,getWidth(), getHeight());
                int direction = getP1Move();
                int turn = getP1Turret();
                player1.move(direction, walls);
                player1.turn(turn);
                direction = getP2Move();
                turn = getP2Turret();
                player2.move(direction, walls);
                player2.turn(turn);

                if (gameOver) {
                    animate(g2d);
                    g2d.setColor(winnerColor);
                    g2d.drawString(winner + " wins!", 100, getHeight() / 2);
                }
                else {
                    animate(g2d);
                }
            }
            repaint();

        }

        private void animate(Graphics2D g2d) {
            g2d.drawImage(map, 0, 0, null);
            ArrayList<Missile> deadMissiles = new ArrayList<>();
            for (Missile missile : missiles) {
                if (missile.isAlive()) {
                    missile.draw(g2d);
                    missile.update(walls);
                } else {
                    deadMissiles.add(missile);
                }
            }
            missiles.removeAll(deadMissiles);
            if (player2.isAlive()) {
                player2.draw(g2d);
            } else {
                gameOver = true;
                winnerColor = Color.red;
                winner = "Player 1";

            }
            if (player1.isAlive()) {
                player1.draw(g2d);

            } else {
                winner = "Player 2";
                winnerColor = Color.blue;
                gameOver = true;
            }

        }

        private int getP1Turret() {
            if (clockwise1) {
                return 1;
            }
            if (counterClockwise1) {
                return 2;
            }
            return 0;
        }

        private int getP2Turret() {
            if (clockwise2) {
                return 1;
            }
            if (counterClockwise2) {
                return 2;
            }
            return 0;
        }

        private int getP1Move() {
            if (up1) {
                return 1;
            }
            if (left1) {
                return 2;
            }
            if (down1) {
                return 3;
            }
            if (right1) {
                return 4;
            }
            return 0;
        }

        private int getP2Move() {
            if (up2) {
                return 1;
            }
            if (left2) {
                return 2;
            }
            if (down2) {
                return 3;
            }
            if (right2) {
                return 4;
            }
            return 0;
        }

        private void startScreen(Graphics2D g2d) {
            g2d.drawImage(startScreen, 0, 0, null);
            g2d.setColor(Color.blue);
            g2d.setFont(gameFont);
            g2d.drawString("Tanks", getWidth()/2-120, 75);
            g2d.setFont(new Font("TimesNewRoman", Font.PLAIN, 25));
            g2d.drawString("(Inspired by the player1 level from Tron)", getWidth()/2-200, 100);
            g2d.fillRect(buttonX, buttonY, buttonWidth, buttonHeight);
            g2d.setColor(Color.black);
            g2d.setFont(gameFont.deriveFont(Font.BOLD, 25f));
            g2d.drawString("Start", buttonX+60, buttonY+60);
            g2d.setColor(Color.red);
            g2d.fillRect(createMapX, buttonY, buttonWidth, buttonHeight);
            g2d.setColor(Color.black);
            g2d.setFont(gameFont.deriveFont(Font.BOLD, 25f));
            g2d.drawString("Custom Map", createMapX+10, buttonY+60);

        }

    }



    private Main() {
        int yFrameOffset = 35;
        int xFrameOffset = 12;
        try {
            setFont(new Font("Courier", Font.BOLD,75));
            startScreen = ImageIO.read(new File("startBackground.jpg"));
            map = ImageIO.read(new File("Background.jpg"));
            mapWidth = map.getWidth(null);
            mapHeight = map.getHeight(null);
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            setSize(mapWidth+xFrameOffset, mapHeight+yFrameOffset);
            setResizable(false);
            add(new GamePanel());
        }
        catch(IOException ex) {
            try {
                map = ImageIO.read(new File("defaultBackground.jpg"));
                mapWidth = map.getWidth(null);
                mapHeight = map.getHeight(null);
                setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                setSize(mapWidth+xFrameOffset, mapHeight+yFrameOffset);
                setResizable(false);
                add(new GamePanel());
            }
            catch(IOException e) {
                System.err.println("No map file not found");
            }
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new Main().setVisible(true));

    }
}
