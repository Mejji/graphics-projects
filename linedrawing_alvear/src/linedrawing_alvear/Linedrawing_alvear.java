package linedrawing_alvear;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Linedrawing_alvear extends JFrame {
    
    private static final int CELL_SIZE = 20;
    private static final int GRID_SIZE = 20;
    private ArrayList<Point> ddaPoints;
    private ArrayList<Point> bresenhamPoints;
    
    public Linedrawing_alvear() {
        setTitle("Line Drawing Algorithms - DDA vs Bresenham");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(1, 2));
        
        // Calculate points for both algorithms
        calculateDDA(3, 17, 14, 4);
        calculateBresenham(3, 17, 14, 4);
        
        // Create panels for both algorithms
        JPanel ddaPanel = new DrawPanel(ddaPoints, "DDA Algorithm");
        JPanel bresenhamPanel = new DrawPanel(bresenhamPoints, "Bresenham Algorithm");
        
        add(ddaPanel);
        add(bresenhamPanel);
        
        setSize(900, 500);
        setLocationRelativeTo(null);
        setVisible(true);
        
        // Print detailed computations
        printDDAComputations();
        printBresenhamComputations();
    }
    
    private void calculateDDA(int x1, int y1, int x2, int y2) {
        ddaPoints = new ArrayList<>();
        
        int dx = x2 - x1;
        int dy = y2 - y1;
        int steps;
        
        if (Math.abs(dx) > Math.abs(dy)) {
            steps = Math.abs(dx);
        } else {
            steps = Math.abs(dy);
        }
        
        float xIncrement = (float) dx / steps;
        float yIncrement = (float) dy / steps;
        
        float x = x1;
        float y = y1;
        
        System.out.println("\n=== DDA Algorithm Calculations ===");
        System.out.println("Starting point: (" + x1 + ", " + y1 + ")");
        System.out.println("Ending point: (" + x2 + ", " + y2 + ")");
        System.out.println("dx = " + dx + ", dy = " + dy);
        System.out.println("Steps = " + steps);
        System.out.println("xIncrement = " + xIncrement);
        System.out.println("yIncrement = " + yIncrement);
        System.out.println("\nStep\tx\ty\tRounded(x,y)");
        
        for (int i = 0; i <= steps; i++) {
            int roundedX = Math.round(x);
            int roundedY = Math.round(y);
            ddaPoints.add(new Point(roundedX, roundedY));
            
            System.out.printf("%d\t%.2f\t%.2f\t(%d, %d)\n", 
                i, x, y, roundedX, roundedY);
            
            x += xIncrement;
            y += yIncrement;
        }
    }
    
    private void calculateBresenham(int x1, int y1, int x2, int y2) {
        bresenhamPoints = new ArrayList<>();
        
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        
        int x = x1;
        int y = y1;
        
        int xInc = (x2 > x1) ? 1 : -1;
        int yInc = (y2 > y1) ? 1 : -1;
        
        System.out.println("\n=== Bresenham Algorithm Calculations ===");
        System.out.println("Starting point: (" + x1 + ", " + y1 + ")");
        System.out.println("Ending point: (" + x2 + ", " + y2 + ")");
        System.out.println("dx = " + dx + ", dy = " + dy);
        System.out.println("xInc = " + xInc + ", yInc = " + yInc);
        
        if (dx > dy) {
            // Slope |m| < 1
            int p = 2 * dy - dx;
            System.out.println("Slope |m| < 1");
            System.out.println("Initial decision parameter p0 = 2*dy - dx = " + p);
            System.out.println("\nStep\tx\ty\tp");
            
            int step = 0;
            while (x != x2 || y != y2) {
                bresenhamPoints.add(new Point(x, y));
                System.out.println(step + "\t" + x + "\t" + y + "\t" + p);
                
                x += xInc;
                if (p < 0) {
                    p = p + 2 * dy;
                } else {
                    y += yInc;
                    p = p + 2 * dy - 2 * dx;
                }
                step++;
            }
            bresenhamPoints.add(new Point(x2, y2));
            System.out.println(step + "\t" + x2 + "\t" + y2 + "\t-");
            
        } else {
            // Slope |m| >= 1
            int p = 2 * dx - dy;
            System.out.println("Slope |m| >= 1");
            System.out.println("Initial decision parameter p0 = 2*dx - dy = " + p);
            System.out.println("\nStep\tx\ty\tp");
            
            int step = 0;
            while (x != x2 || y != y2) {
                bresenhamPoints.add(new Point(x, y));
                System.out.println(step + "\t" + x + "\t" + y + "\t" + p);
                
                y += yInc;
                if (p < 0) {
                    p = p + 2 * dx;
                } else {
                    x += xInc;
                    p = p + 2 * dx - 2 * dy;
                }
                step++;
            }
            bresenhamPoints.add(new Point(x2, y2));
            System.out.println(step + "\t" + x2 + "\t" + y2 + "\t-");
        }
    }
    
    private void printDDAComputations() {
        System.out.println("\n=== DDA Algorithm Summary ===");
        System.out.println("Line from (3, 17) to (14, 4)");
        System.out.println("dx = 14 - 3 = 11");
        System.out.println("dy = 4 - 17 = -13");
        System.out.println("m = dy/dx = -13/11 = -1.18");
        System.out.println("|dy| > |dx|, so we increment y by ±1 and calculate x");
        System.out.println("\nPixels plotted (DDA):");
        for (Point p : ddaPoints) {
            System.out.print("(" + p.x + "," + p.y + ") ");
        }
        System.out.println();
    }
    
    private void printBresenhamComputations() {
        System.out.println("\n=== Bresenham Algorithm Summary ===");
        System.out.println("Line from (3, 17) to (14, 4)");
        System.out.println("dx = |14 - 3| = 11");
        System.out.println("dy = |4 - 17| = 13");
        System.out.println("Since dy > dx, we use the steep line case");
        System.out.println("Initial p = 2*dx - dy = 2*11 - 13 = 9");
        System.out.println("\nPixels plotted (Bresenham):");
        for (Point p : bresenhamPoints) {
            System.out.print("(" + p.x + "," + p.y + ") ");
        }
        System.out.println();
    }
    
    class DrawPanel extends JPanel {
        private ArrayList<Point> points;
        private String title;
        
        public DrawPanel(ArrayList<Point> points, String title) {
            this.points = points;
            this.title = title;
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createLineBorder(Color.BLACK));
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            
            // Draw title
            g2d.setFont(new Font("Arial", Font.BOLD, 14));
            g2d.drawString(title, 10, 20);
            
            // Draw grid
            g2d.setColor(Color.LIGHT_GRAY);
            for (int i = 0; i <= GRID_SIZE; i++) {
                // Vertical lines
                g2d.drawLine(i * CELL_SIZE, 40, i * CELL_SIZE, 40 + GRID_SIZE * CELL_SIZE);
                // Horizontal lines
                g2d.drawLine(0, 40 + i * CELL_SIZE, GRID_SIZE * CELL_SIZE, 40 + i * CELL_SIZE);
            }
            
            // Draw axes labels
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.PLAIN, 10));
            for (int i = 0; i <= GRID_SIZE; i += 2) {
                g2d.drawString(String.valueOf(i), i * CELL_SIZE - 5, 35);
                g2d.drawString(String.valueOf(GRID_SIZE - i), GRID_SIZE * CELL_SIZE + 5, 
                              45 + i * CELL_SIZE);
            }
            
            // Draw the line points
            g2d.setColor(Color.RED);
            for (Point p : points) {
                if (p.x >= 0 && p.x <= GRID_SIZE && p.y >= 0 && p.y <= GRID_SIZE) {
                    int screenX = p.x * CELL_SIZE;
                    int screenY = 40 + (GRID_SIZE - p.y) * CELL_SIZE;
                    g2d.fillRect(screenX - CELL_SIZE/2, screenY - CELL_SIZE/2, 
                               CELL_SIZE, CELL_SIZE);
                }
            }
            
            // Draw start and end points with different colors
            if (points.size() > 0) {
                Point start = points.get(0);
                Point end = points.get(points.size() - 1);
                
                // Start point in green
                g2d.setColor(Color.GREEN);
                int startX = start.x * CELL_SIZE;
                int startY = 40 + (GRID_SIZE - start.y) * CELL_SIZE;
                g2d.fillOval(startX - 5, startY - 5, 10, 10);
                g2d.drawString("Start", startX + 10, startY);
                
                // End point in blue
                g2d.setColor(Color.BLUE);
                int endX = end.x * CELL_SIZE;
                int endY = 40 + (GRID_SIZE - end.y) * CELL_SIZE;
                g2d.fillOval(endX - 5, endY - 5, 10, 10);
                g2d.drawString("End", endX + 10, endY);
            }
            
            // Draw the ideal line for reference
            g2d.setColor(new Color(0, 0, 255, 50));
            g2d.setStroke(new BasicStroke(2));
            if (points.size() > 1) {
                Point start = points.get(0);
                Point end = points.get(points.size() - 1);
                g2d.drawLine(start.x * CELL_SIZE, 40 + (GRID_SIZE - start.y) * CELL_SIZE,
                           end.x * CELL_SIZE, 40 + (GRID_SIZE - end.y) * CELL_SIZE);
            }
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Linedrawing_alvear();
        });
        
        // Additional console output for clarity
        System.out.println("\n========================================");
        System.out.println("LINE DRAWING ALGORITHMS COMPARISON");
        System.out.println("Line from (3, 17) to (14, 4)");
        System.out.println("========================================");
        
        System.out.println("\nKey Observations:");
        System.out.println("1. The line has a negative slope (going down from left to right)");
        System.out.println("2. |m| = 13/11 ≈ 1.18, which is > 1");
        System.out.println("3. For DDA: Since |dy| > |dx|, we increment y and calculate x");
        System.out.println("4. For Bresenham: We use the steep line case (dy > dx)");
        System.out.println("5. Both algorithms produce similar results with slight variations");
        System.out.println("\nThe graphical window shows both algorithms side by side.");
        System.out.println("Red squares represent the plotted pixels.");
        System.out.println("Green circle marks the start point (3, 17)");
        System.out.println("Blue circle marks the end point (14, 4)");
        System.out.println("Light blue line shows the ideal mathematical line for reference.");
    }
}