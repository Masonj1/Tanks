import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.util.*;

public class TankPicture extends JFrame {

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new TankPicture().setVisible(true));
    }

    public TankPicture() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(800, 800);
        add(new DrawingPanel());
    }


    private class DrawingPanel extends JPanel {


        public DrawingPanel() {

        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            int x = 90;
            int y = 90;
            Color squareColor = Color.blue;
            Color squareColorOutline = Color.white;

            //initialize color
            g2d.setColor(squareColorOutline);
            //left tread
            g2d.setColor(squareColor);
            g2d.fillRect(x + 22, y, 8, 30);
            g2d.setColor(squareColorOutline);
            g2d.drawRect(x + 22, y, 8, 30);
            //right tread
            g2d.setColor(squareColor);
            g2d.fillRect(x, y, 8, 30);
            g2d.setColor(squareColorOutline);
            g2d.drawRect(x, y, 8, 30);
            //main compartment
            g2d.setColor(squareColor);
            g2d.fillRect(x+5, y+5, 20, 20);
            g2d.setColor(squareColorOutline);
            g2d.drawRect(x+5, y+5, 20, 20);


            // public static void drawSquareTankTurret(float x, float y, float angle){
            double angle = Math.PI/4;

            //hatch
            Graphics2D g1 = (Graphics2D) g.create();
            g1.rotate(angle, x+10 + 10/2, y+10+10/2);

            g1.setColor(squareColor);
            g1.fillRect(x+10, y+10, 10, 10);
            g1.setColor(squareColorOutline);
            g1.drawRect(x+10, y+10, 10, 10);

            g1.dispose();

            //turret

            Graphics2D g2 = (Graphics2D) g.create();
            g2.rotate(angle, x +10 +10/2, y+10+10/2);

            g2.setColor(squareColor);
            g2.fillRect(x+13, y, 4, 10);
            g2.setColor(squareColorOutline);
            g2.drawRect(x+13, y, 4, 10);
            g2.dispose();
            // }



            Graphics2D gg = (Graphics2D) g.create();
            int rx = 200;
            int ry = 250;
            int width = 30;
            int height = 30;
            gg.rotate(Math.PI/4, rx + width/2, ry + height/2);
            gg.setColor(Color.black);
            g2d.setColor(Color.black);
            gg.drawRect(rx, ry, width, height);
            gg.drawRect(rx + 10, ry + 10, width, height);

            g2d.drawRect(rx, ry, width, height);
            gg.dispose();

            int circleX = 90;
            int circleY = 130;
            Color circleColor = Color.red;
            Color circleColorOutline = Color.white;

            //initialize color
            g2d.setColor(circleColorOutline);
            //left tread
            g2d.setColor(circleColor);
            g2d.fillOval(circleX + 22, circleY, 8, 30);
            g2d.setColor(circleColorOutline);
            g2d.drawOval(circleX + 22, circleY, 8, 30);
            //right tread
            g2d.setColor(circleColor);
            g2d.fillOval(circleX, circleY, 8, 30);
            g2d.setColor(circleColorOutline);
            g2d.drawOval(circleX, circleY, 8, 30);
            //main compartment
            g2d.setColor(circleColor);
            g2d.fillOval(circleX+5, circleY+5, 20, 20);
            g2d.setColor(circleColorOutline);
            g2d.drawOval(circleX+5, circleY+5, 20, 20);


            //hatch
            //turret

            // public static void drawCircleTankTurret(float x, float y, float angle){
            double angle0 = -Math.PI/4;

            //hatch
            Graphics2D g3 = (Graphics2D) g.create();
            g3.rotate(angle0, circleX+10 + 10/2, circleY+10+10/2);

            g3.setColor(circleColor);
            g3.fillOval(circleX+10, circleY+10, 10, 10);
            g3.setColor(circleColorOutline);
            g3.drawOval(circleX+10, circleY+10, 10, 10);

            g3.dispose();

            //turret

            Graphics2D g4 = (Graphics2D) g.create();
            g4.rotate(angle0, circleX +10 +10/2, circleY+10+10/2);

            g4.setColor(circleColor);
            g4.fillOval(circleX+13, circleY, 4, 10);
            g4.setColor(circleColorOutline);
            g4.drawOval(circleX+13, circleY, 4, 10);

            g4.dispose();
            // }

        }
    }



}