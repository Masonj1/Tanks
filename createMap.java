import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.lang.Math;

public class createMap extends JFrame {

    // Creates variables for the dimensions of the black/white grid
    private float xDim;
    private float yDim;
    // Creates variables for the width and height of each cell of the grid
    private float squareWidth;
    private float squareHeight;
    // Creates an arrayList to store all white cells in
    public ArrayList<Point> whites = new ArrayList<>();
    // Creates an arrayList to track all cells covered in the current move
    private ArrayList<Point> currentMove = new ArrayList<>();

    private class ButtonPanel extends JPanel {
        private ButtonPanel() {
            setPreferredSize(new Dimension(750, 100));
            add(new JButton("Save"));
        }
    }

    private class DrawingPanel extends JPanel{

        private DrawingPanel() {
            setPreferredSize(new Dimension(750, 600));
            // Sets the dimensions of the grid
            xDim = 30;
            yDim = 24;
            // If the mouse is pressed or dragged, the fillSquares function is called
            addMouseListener(new MouseAdapter() {

                @Override
                public void mousePressed(MouseEvent e) {
                    fillSquares(e);

                }
            });
            addMouseMotionListener(new MouseMotionAdapter()
            {

                @Override
                public void mouseDragged(MouseEvent e) {
                    fillSquares(e);
                }
            });
            // When the mouse is released, the current move ends and the array storing its cells is cleared
            addMouseListener(new MouseAdapter() {

                @Override
                public void mouseReleased(MouseEvent e) {
                    currentMove.clear();
                    savePic();
                }
            });
        }

        private void fillSquares(MouseEvent e) {
            // Finds the cell that the user clicked on and stores it as a point
            int xClick = Math.round((e.getX()-squareWidth/2) / squareWidth);
            int yClick = Math.round((e.getY()-squareHeight/2) / squareHeight);
            Point square = new Point (xClick, yClick);
            // A boolean variable tracks if a cell should be white or black
            boolean white = false;
            // Tells if a cell is contained in the current move
            boolean current = false;
            // Searches through all of the white cells in the grid to see if the user clicked a white cell
            for(Point p : whites) {
                if(p.x == square.x && p.y == square.y) {
                    white = true;
                }

            }
            // Iterates through every square covered in the current move to prevent rapid toggling caused by the
            // MouseMotionAdapter for dragging
            for(Point p: currentMove) {
                if(p.x == square.x && p.y == square.y) {
                    current = true;
                }
            }
            // If the user clicked a white square not covered in the current move, then the square turns black
            if(white && !current) {
                whites.remove(square);
                currentMove.add(square);
            }
            // If the user clicks a black square not covered in the current move, then the square turns white
            else if(!white && !current){
                whites.add(square);
                currentMove.add(square);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            // The Graphics object is converted to a Graphics2D object
            Graphics2D g2 = (Graphics2D) g;
            super.paintComponent(g);
            // The width an height of each square are calculated using the dimensions of the grid and size of the window
            squareWidth = getWidth()/xDim;
            squareHeight = getHeight()/yDim;
            // Iterates for each cell in the grid
            for(int i = 0; i < xDim; i++) {
                for(int j = 0; j < yDim; j++) {
                    // Sets the default color of a cell to black
                    g2.setColor(Color.black);
                    if(i == 0 || i == 29|| j == 0 || j == 23){
                        g2.setColor(Color.white);
                    }
                    if(13 <= i && i <= 16 && 10 <= j && j <= 13 ){
                        g2.setColor(Color.white);
                    }
                    if((i == 13 && (j == 10 || j == 13) || (i == 16 && (j== 10 || j == 13)))){
                        g2.setColor(Color.black);
                    }

                    // Checks if the cell is white and changes its color accordingly
                    for(Point p : whites) {
                        if(p.x == i && p.y == j) {
                            g2.setColor(Color.white);
                        }

                    }
                    // Fills the cell with its given color and draws a gray outline
                    g2.fill(new Rectangle2D.Double(i*squareWidth, j*squareHeight, squareWidth, squareHeight));
                    g2.setColor(Color.gray);
                    g2.draw(new Rectangle2D.Double(i*squareWidth, j*squareHeight, squareWidth, squareHeight));
                }
            }
            // Paints the frame after all the objects have been drawn
            repaint();

        }


    }


    private createMap() {
        // Sets the program to exit after the user closes the graphics window and initializes the window with a size of
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(750, 700);
        setLayout(new FlowLayout());
        // Creates a new DrawingPanel with the grid size given by command line arguments
        add(new DrawingPanel());
        add(new ButtonPanel());

    }


    public static void main(String args[]) {
        EventQueue.invokeLater(() -> {
            // Tries to use command line arguments to set the dimensions of the grid and uses the default dimensions if
            // that fails
            try {
                new createMap().setVisible(true);
            }
            catch(NumberFormatException | ArrayIndexOutOfBoundsException ex) {
                new createMap().setVisible(true);
            }
        });

    }
    public void savePic(){
        BufferedImage map = new BufferedImage(750, 600, BufferedImage.TYPE_INT_RGB);
        for (int col = 0; col < 750; col ++){
            for (int row = 0; row < 600; row++){
                map.setRGB(col, row, 256);
                int i = Math.round((col-25/2f) / 25f);
                int j = Math.round((row-25/2f) / 25f);
                for ( Point p: whites){
                    if (p.x == i && p.y == j){
                        map.setRGB(col, row, 100);
                    }
                }
                if(i == 0 || i == 29|| j == 0 || j == 23){
                    map.setRGB(col, row, 100);
                }
                if(13 <= i && i <= 16 && 10 <= j && j <= 13 ){
                    map.setRGB(col, row, 100);
                }
                if((i == 13 && (j == 10 || j == 13) || (i == 16 && (j== 10 || j == 13)))){
                    map.setRGB(col, row, 0);
                }

            }
        }
        getContentPane().setLayout(new FlowLayout());
        getContentPane().add(new JLabel(new ImageIcon(map)));
        pack();
        setVisible(true);
    }
}