import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;

public class Main extends JFrame {
    
    private int tankSize = 25;
    private int yFrameOffset = 35;
    private int xFrameOffset = 12;
    private int wallSize = 25;
    private Color wallColor= new Color(100, 235, 96);
    private int mapWidth;
    private int mapHeight;
    private BufferedImage map;

    private HashMap<Integer, Point> generateWalls() {
        HashMap<Integer, Point> walls = new HashMap<>();
        int key = 0;
        for(int i = 0; i < mapWidth; i++) {
            for(int j = 0; j < mapHeight; j++) {
                Color cellColor = new Color(map.getRGB(i, j));

                if(cellColor.getRed() >= wallColor.getRed()-10 && cellColor.getRed() <= wallColor.getRed()+10 &&
                        cellColor.getGreen() >= wallColor.getGreen()-10 && cellColor.getGreen() <= wallColor.getGreen()+10 &&
                        cellColor.getBlue() >= wallColor.getBlue()-10 && cellColor.getBlue() <= wallColor.getBlue()+10) {
                    walls.put(key, new Point(i, j));
                    key++;
                }
            }
        }
        System.out.println(walls.values());
        return walls;
    }

    public class GamePanel extends JPanel {
        Tank tank = new Tank(50, 50, tankSize);
        boolean left, right, down, up = false;
        boolean clockwise, counterClockwise = false;
        HashMap<Integer, Point> walls = generateWalls();


        public GamePanel() {
            setFocusable(true);
            requestFocusInWindow();
            super.setBackground(Color.black);
            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if(e.getKeyCode() == KeyEvent.VK_W) {
                        up = true;
                    }
                    if(e.getKeyCode() == KeyEvent.VK_A) {
                        left = true;
                    }
                    if(e.getKeyCode() == KeyEvent.VK_S) {
                        down = true;
                    }
                    if(e.getKeyCode() == KeyEvent.VK_D) {
                        right = true;
                    }
                    if(e.getKeyCode() == KeyEvent.VK_Q) {
                        counterClockwise = true;
                    }
                    if(e.getKeyCode() == KeyEvent.VK_E) {
                        clockwise = true;
                    }
                }
            });
            addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    if(e.getKeyCode() == KeyEvent.VK_W) {
                        up = false;
                    }
                    if(e.getKeyCode() == KeyEvent.VK_A) {
                        left = false;
                    }
                    if(e.getKeyCode() == KeyEvent.VK_S) {
                        down = false;
                    }
                    if(e.getKeyCode() == KeyEvent.VK_D) {
                        right = false;
                    }
                    if(e.getKeyCode() == KeyEvent.VK_Q) {
                        counterClockwise = false;
                    }
                    if(e.getKeyCode() == KeyEvent.VK_E) {
                        clockwise = false;
                    }

                }
            });
        }


        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            int direction = 0;
            int turn = 0;
            if(up) {
                direction = 1;
            }
            if(left) {
                direction = 2;
            }
            if(down) {
                direction = 3;
            }
            if(right) {
                direction = 4;
            }
            if(clockwise) {
                turn = 1;
            }
            if(counterClockwise) {
                turn = 2;
            }

            tank.move(direction, walls);
            tank.turn(turn);
            g2d.drawImage(map, 0, 0, Color.green, null);
            tank.draw(g2d);
            repaint();

        }

    }



    public Main() {
        try {
            map = ImageIO.read(new File("Background.jpg"));
        }
        catch(IOException ex) {
            System.err.println("Image file not found");
        }
        mapWidth = map.getWidth(null);
        mapHeight = map.getHeight(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(mapWidth+xFrameOffset, mapHeight+yFrameOffset);
        add(new GamePanel());
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Main().setVisible(true);
            }
        });

    }
}
