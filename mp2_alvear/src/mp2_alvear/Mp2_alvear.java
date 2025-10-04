package mp2_alvear;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class Mp2_alvear extends JFrame {
    private CardLayout cardLayout;
    private JPanel drawingPanel;
    private int currentCard = 0;
    private final int TOTAL_CARDS = 9;
    
    public Mp2_alvear() {
        setTitle("MP2 Graphics Assignment - Alvear");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        // Create main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Create drawing panel with CardLayout
        cardLayout = new CardLayout();
        drawingPanel = new JPanel(cardLayout);
        
        // Add all drawing panels
        drawingPanel.add(new CrossWithQuadCurves(), "cross1");
        drawingPanel.add(new CrossWithGeneralPath(), "cross2");
        drawingPanel.add(new ChristmasTree(), "tree");
        drawingPanel.add(new UnionPanel(), "union");
        drawingPanel.add(new IntersectionPanel(), "intersection");
        drawingPanel.add(new SymmetricDifferencePanel(), "symmetric");
        drawingPanel.add(new RelativeDifferenceCrossTreePanel(), "diff1");
        drawingPanel.add(new RelativeDifferenceTreeCrossPanel(), "diff2");
        drawingPanel.add(new ComparisonPanel(), "comparison");
        
        // Create button panel
        JPanel buttonPanel = new JPanel();
        JButton prevButton = new JButton("Previous");
        JButton nextButton = new JButton("Next");
        JLabel statusLabel = new JLabel("1 / " + TOTAL_CARDS);
        
        prevButton.addActionListener(e -> {
            if (currentCard > 0) {
                currentCard--;
                showCard(currentCard);
                statusLabel.setText((currentCard + 1) + " / " + TOTAL_CARDS);
            }
        });
        
        nextButton.addActionListener(e -> {
            if (currentCard < TOTAL_CARDS - 1) {
                currentCard++;
                showCard(currentCard);
                statusLabel.setText((currentCard + 1) + " / " + TOTAL_CARDS);
            }
        });
        
        buttonPanel.add(prevButton);
        buttonPanel.add(statusLabel);
        buttonPanel.add(nextButton);
        
        mainPanel.add(drawingPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void showCard(int index) {
        String[] cards = {"cross1", "cross2", "tree", "union", "intersection", 
                         "symmetric", "diff1", "diff2", "comparison"};
        cardLayout.show(drawingPanel, cards[index]);
    }
    
    // Panel 1: Cross with Lines and QuadCurves
    class CrossWithQuadCurves extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Scale factors for responsiveness
            double scaleX = getWidth() / 800.0;
            double scaleY = getHeight() / 600.0;
            double scale = Math.min(scaleX, scaleY);
            
            g2d.scale(scale, scale);
            
            // Title
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            g2d.drawString("1. Cross with Lines and QuadCurves", 50, 30);
            
            g2d.setStroke(new BasicStroke(2));
            
            // Starting point A(250, 280)
            int startX = 250;
            int startY = 280;
            
            // Draw the cross shape
            GeneralPath cross = new GeneralPath();
            
            // Top part
            cross.moveTo(startX, startY);
            cross.lineTo(startX, startY - 100); // Move up 100
            cross.quadTo(startX, startY - 120, startX + 20, startY - 120); // Top left curve
            cross.lineTo(startX + 80, startY - 120); // Horizontal top
            cross.quadTo(startX + 100, startY - 120, startX + 100, startY - 100); // Top right curve
            cross.lineTo(startX + 100, startY); // Down to center right
            
            // Right part
            cross.lineTo(startX + 200, startY); // Right horizontal
            cross.quadTo(startX + 220, startY, startX + 220, startY + 20); // Right top curve
            cross.lineTo(startX + 220, startY + 80); // Down
            cross.quadTo(startX + 220, startY + 100, startX + 200, startY + 100); // Right bottom curve
            cross.lineTo(startX + 100, startY + 100); // Back to center
            
            // Bottom part
            cross.lineTo(startX + 100, startY + 200); // Down
            cross.quadTo(startX + 100, startY + 220, startX + 80, startY + 220); // Bottom right curve
            cross.lineTo(startX + 20, startY + 220); // Horizontal bottom
            cross.quadTo(startX, startY + 220, startX, startY + 200); // Bottom left curve
            cross.lineTo(startX, startY + 100); // Up to center
            
            // Left part
            cross.lineTo(startX - 100, startY + 100); // Left horizontal bottom
            cross.quadTo(startX - 120, startY + 100, startX - 120, startY + 80); // Left bottom curve
            cross.lineTo(startX - 120, startY + 20); // Up
            cross.quadTo(startX - 120, startY, startX - 100, startY); // Left top curve
            cross.closePath();
            
            g2d.setColor(Color.BLUE);
            g2d.draw(cross);
            
            // Draw points and labels
            g2d.setColor(Color.RED);
            g2d.fillOval(startX - 3, startY - 3, 6, 6);
            g2d.drawString("A", startX - 20, startY);
        }
    }
    
    // Panel 2: Cross with GeneralPath
    class CrossWithGeneralPath extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            double scaleX = getWidth() / 800.0;
            double scaleY = getHeight() / 600.0;
            double scale = Math.min(scaleX, scaleY);
            
            g2d.scale(scale, scale);
            
            // Title
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            g2d.drawString("2. Cross with GeneralPath", 50, 30);
            
            g2d.setStroke(new BasicStroke(2));
            
            GeneralPath cross = createCrossPath(250, 280);
            
            g2d.setColor(Color.BLUE);
            g2d.fill(cross);
            g2d.setColor(Color.DARK_GRAY);
            g2d.draw(cross);
        }
    }
    
    // Panel 3: Christmas Tree
    class ChristmasTree extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            double scaleX = getWidth() / 800.0;
            double scaleY = getHeight() / 600.0;
            double scale = Math.min(scaleX, scaleY);
            
            g2d.scale(scale, scale);
            
            // Title
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            g2d.drawString("3. Christmas Tree with GeneralPath", 50, 30);
            
            GeneralPath tree = createTreePath(300, 100);
            
            // Draw tree layers
            g2d.setColor(Color.GREEN);
            g2d.fill(tree);
            g2d.setColor(Color.DARK_GRAY);
            g2d.setStroke(new BasicStroke(1));
            g2d.draw(tree);
        }
    }
    
    // Panel 4: Union
    class UnionPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            double scaleX = getWidth() / 800.0;
            double scaleY = getHeight() / 600.0;
            double scale = Math.min(scaleX, scaleY);
            
            g2d.scale(scale, scale);
            
            // Title
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            g2d.drawString("4.1 Union of Cross and Tree", 50, 30);
            
            Area crossArea = new Area(createCrossPath(200, 200));
            Area treeArea = new Area(createTreePath(350, 150));
            
            // Union
            Area union = new Area(crossArea);
            union.add(treeArea);
            
            g2d.setColor(new Color(100, 150, 200, 150));
            g2d.fill(union);
            g2d.setColor(Color.DARK_GRAY);
            g2d.setStroke(new BasicStroke(2));
            g2d.draw(union);
        }
    }
    
    // Panel 5: Intersection
    class IntersectionPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            double scaleX = getWidth() / 800.0;
            double scaleY = getHeight() / 600.0;
            double scale = Math.min(scaleX, scaleY);
            
            g2d.scale(scale, scale);
            
            // Title
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            g2d.drawString("4.2 Intersection of Cross and Tree", 50, 30);
            
            Area crossArea = new Area(createCrossPath(250, 200));
            Area treeArea = new Area(createTreePath(300, 150));
            
            // Intersection
            Area intersection = new Area(crossArea);
            intersection.intersect(treeArea);
            
            // Draw outlines
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{5}, 0));
            g2d.draw(createCrossPath(250, 200));
            g2d.draw(createTreePath(300, 150));
            
            // Draw intersection
            g2d.setColor(new Color(255, 100, 100, 200));
            g2d.fill(intersection);
            g2d.setColor(Color.RED);
            g2d.setStroke(new BasicStroke(2));
            g2d.draw(intersection);
        }
    }
    
    // Panel 6: Symmetric Difference
    class SymmetricDifferencePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            double scaleX = getWidth() / 800.0;
            double scaleY = getHeight() / 600.0;
            double scale = Math.min(scaleX, scaleY);
            
            g2d.scale(scale, scale);
            
            // Title
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            g2d.drawString("4.3 Symmetric Difference", 50, 30);
            
            Area crossArea = new Area(createCrossPath(250, 200));
            Area treeArea = new Area(createTreePath(300, 150));
            
            // Symmetric difference
            Area symDiff = new Area(crossArea);
            symDiff.exclusiveOr(treeArea);
            
            g2d.setColor(new Color(150, 100, 200, 150));
            g2d.fill(symDiff);
            g2d.setColor(Color.DARK_GRAY);
            g2d.setStroke(new BasicStroke(2));
            g2d.draw(symDiff);
        }
    }
    
    // Panel 7: Relative Difference (Cross - Tree)
    class RelativeDifferenceCrossTreePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            double scaleX = getWidth() / 800.0;
            double scaleY = getHeight() / 600.0;
            double scale = Math.min(scaleX, scaleY);
            
            g2d.scale(scale, scale);
            
            // Title
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            g2d.drawString("4.4 Relative Difference (Cross - Tree)", 50, 30);
            
            Area crossArea = new Area(createCrossPath(250, 200));
            Area treeArea = new Area(createTreePath(300, 150));
            
            // Cross minus Tree
            Area diff = new Area(crossArea);
            diff.subtract(treeArea);
            
            // Draw outlines
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{5}, 0));
            g2d.draw(createTreePath(300, 150));
            
            g2d.setColor(new Color(100, 200, 100, 150));
            g2d.fill(diff);
            g2d.setColor(Color.DARK_GRAY);
            g2d.setStroke(new BasicStroke(2));
            g2d.draw(diff);
        }
    }
    
    // Panel 8: Relative Difference (Tree - Cross)
    class RelativeDifferenceTreeCrossPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            double scaleX = getWidth() / 800.0;
            double scaleY = getHeight() / 600.0;
            double scale = Math.min(scaleX, scaleY);
            
            g2d.scale(scale, scale);
            
            // Title
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            g2d.drawString("4.5 Relative Difference (Tree - Cross)", 50, 30);
            
            Area crossArea = new Area(createCrossPath(250, 200));
            Area treeArea = new Area(createTreePath(300, 150));
            
            // Tree minus Cross
            Area diff = new Area(treeArea);
            diff.subtract(crossArea);
            
            // Draw outlines
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{5}, 0));
            g2d.draw(createCrossPath(250, 200));
            
            g2d.setColor(new Color(200, 200, 100, 150));
            g2d.fill(diff);
            g2d.setColor(Color.DARK_GRAY);
            g2d.setStroke(new BasicStroke(2));
            g2d.draw(diff);
        }
    }
    
    // Panel 9: All comparisons
    class ComparisonPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            double scaleX = getWidth() / 800.0;
            double scaleY = getHeight() / 600.0;
            double scale = Math.min(scaleX, scaleY);
            
            g2d.scale(scale, scale);
            
            // Title
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            g2d.drawString("Area Operations Comparison", 50, 30);
            
            g2d.setFont(new Font("Arial", Font.PLAIN, 12));
            
            // Small scale for comparison
            int y = 100;
            int x1 = 150, x2 = 400, x3 = 650;
            
            // Union
            Area cross1 = new Area(createSmallCross(x1, y));
            Area tree1 = new Area(createSmallTree(x1 + 30, y - 20));
            cross1.add(tree1);
            g2d.setColor(new Color(100, 150, 200, 150));
            g2d.fill(cross1);
            g2d.drawString("Union", x1 - 30, y + 80);
            
            // Intersection
            Area cross2 = new Area(createSmallCross(x2, y));
            Area tree2 = new Area(createSmallTree(x2 + 30, y - 20));
            cross2.intersect(tree2);
            g2d.setColor(new Color(255, 100, 100, 200));
            g2d.fill(cross2);
            g2d.drawString("Intersection", x2 - 40, y + 80);
            
            // Symmetric Difference
            Area cross3 = new Area(createSmallCross(x3, y));
            Area tree3 = new Area(createSmallTree(x3 + 30, y - 20));
            cross3.exclusiveOr(tree3);
            g2d.setColor(new Color(150, 100, 200, 150));
            g2d.fill(cross3);
            g2d.drawString("Symmetric Diff", x3 - 45, y + 80);
            
            // Bottom row
            y = 300;
            
            // Cross - Tree
            Area cross4 = new Area(createSmallCross(x1, y));
            Area tree4 = new Area(createSmallTree(x1 + 30, y - 20));
            cross4.subtract(tree4);
            g2d.setColor(new Color(100, 200, 100, 150));
            g2d.fill(cross4);
            g2d.drawString("Cross - Tree", x1 - 35, y + 80);
            
            // Tree - Cross
            Area cross5 = new Area(createSmallCross(x2, y));
            Area tree5 = new Area(createSmallTree(x2 + 30, y - 20));
            tree5.subtract(cross5);
            g2d.setColor(new Color(200, 200, 100, 150));
            g2d.fill(tree5);
            g2d.drawString("Tree - Cross", x2 - 35, y + 80);
            
            // Original shapes
            g2d.setColor(Color.BLUE);
            g2d.fill(createSmallCross(x3, y));
            g2d.setColor(Color.GREEN);
            g2d.fill(createSmallTree(x3 + 30, y - 20));
            g2d.drawString("Original Shapes", x3 - 45, y + 80);
        }
        
        private GeneralPath createSmallCross(int x, int y) {
            GeneralPath path = new GeneralPath();
            int s = 15; // size
            path.moveTo(x - s, y - s/3);
            path.lineTo(x - s, y + s/3);
            path.lineTo(x - s/3, y + s/3);
            path.lineTo(x - s/3, y + s);
            path.lineTo(x + s/3, y + s);
            path.lineTo(x + s/3, y + s/3);
            path.lineTo(x + s, y + s/3);
            path.lineTo(x + s, y - s/3);
            path.lineTo(x + s/3, y - s/3);
            path.lineTo(x + s/3, y - s);
            path.lineTo(x - s/3, y - s);
            path.lineTo(x - s/3, y - s/3);
            path.closePath();
            return path;
        }
        
        private GeneralPath createSmallTree(int x, int y) {
            GeneralPath path = new GeneralPath();
            // Top triangle
            path.moveTo(x, y);
            path.lineTo(x - 20, y + 20);
            path.lineTo(x + 20, y + 20);
            path.closePath();
            // Middle triangle
            path.moveTo(x, y + 10);
            path.lineTo(x - 25, y + 35);
            path.lineTo(x + 25, y + 35);
            path.closePath();
            // Bottom triangle
            path.moveTo(x, y + 25);
            path.lineTo(x - 30, y + 50);
            path.lineTo(x + 30, y + 50);
            path.closePath();
            // Trunk
            path.moveTo(x - 5, y + 50);
            path.lineTo(x - 5, y + 60);
            path.lineTo(x + 5, y + 60);
            path.lineTo(x + 5, y + 50);
            path.closePath();
            return path;
        }
    }
    
    // Helper method to create cross path
    private GeneralPath createCrossPath(int startX, int startY) {
        GeneralPath cross = new GeneralPath();
        
        // Create a cross with rounded corners
        int armLength = 100;
        int armWidth = 40;
        int cornerRadius = 20;
        
        cross.moveTo(startX - armWidth/2, startY - armLength);
        cross.quadTo(startX - armWidth/2, startY - armLength - cornerRadius, 
                     startX - armWidth/2 + cornerRadius, startY - armLength - cornerRadius);
        cross.lineTo(startX + armWidth/2 - cornerRadius, startY - armLength - cornerRadius);
        cross.quadTo(startX + armWidth/2, startY - armLength - cornerRadius,
                     startX + armWidth/2, startY - armLength);
        cross.lineTo(startX + armWidth/2, startY - armWidth/2);
        cross.lineTo(startX + armLength, startY - armWidth/2);
        cross.quadTo(startX + armLength + cornerRadius, startY - armWidth/2,
                     startX + armLength + cornerRadius, startY - armWidth/2 + cornerRadius);
        cross.lineTo(startX + armLength + cornerRadius, startY + armWidth/2 - cornerRadius);
        cross.quadTo(startX + armLength + cornerRadius, startY + armWidth/2,
                     startX + armLength, startY + armWidth/2);
        cross.lineTo(startX + armWidth/2, startY + armWidth/2);
        cross.lineTo(startX + armWidth/2, startY + armLength);
        cross.quadTo(startX + armWidth/2, startY + armLength + cornerRadius,
                     startX + armWidth/2 - cornerRadius, startY + armLength + cornerRadius);
        cross.lineTo(startX - armWidth/2 + cornerRadius, startY + armLength + cornerRadius);
        cross.quadTo(startX - armWidth/2, startY + armLength + cornerRadius,
                     startX - armWidth/2, startY + armLength);
        cross.lineTo(startX - armWidth/2, startY + armWidth/2);
        cross.lineTo(startX - armLength, startY + armWidth/2);
        cross.quadTo(startX - armLength - cornerRadius, startY + armWidth/2,
                     startX - armLength - cornerRadius, startY + armWidth/2 - cornerRadius);
        cross.lineTo(startX - armLength - cornerRadius, startY - armWidth/2 + cornerRadius);
        cross.quadTo(startX - armLength - cornerRadius, startY - armWidth/2,
                     startX - armLength, startY - armWidth/2);
        cross.lineTo(startX - armWidth/2, startY - armWidth/2);
        cross.closePath();
        
        return cross;
    }
    
    // Helper method to create tree path
    private GeneralPath createTreePath(int startX, int startY) {
        GeneralPath tree = new GeneralPath();
        
        // Star on top
        int starSize = 20;
        double angle = Math.PI / 5;
        for (int i = 0; i < 10; i++) {
            double r = (i % 2 == 0) ? starSize : starSize / 2;
            double x = startX + 150 + r * Math.cos(i * angle - Math.PI / 2);
            double y = startY + r * Math.sin(i * angle - Math.PI / 2);
            if (i == 0) tree.moveTo(x, y);
            else tree.lineTo(x, y);
        }
        tree.closePath();
        
        // Top layer
        tree.moveTo(startX + 150, startY + 30);
        tree.lineTo(startX + 70, startY + 150);
        tree.lineTo(startX + 230, startY + 150);
        tree.closePath();
        
        // Middle layer
        tree.moveTo(startX + 150, startY + 100);
        tree.lineTo(startX + 50, startY + 250);
        tree.lineTo(startX + 250, startY + 250);
        tree.closePath();
        
        // Bottom layer
        tree.moveTo(startX + 150, startY + 200);
        tree.lineTo(startX + 30, startY + 350);
        tree.lineTo(startX + 270, startY + 350);
        tree.closePath();
        
        // Trunk
        tree.moveTo(startX + 135, startY + 350);
        tree.lineTo(startX + 135, startY + 380);
        tree.lineTo(startX + 165, startY + 380);
        tree.lineTo(startX + 165, startY + 350);
        tree.closePath();
        
        return tree;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Mp2_alvear frame = new Mp2_alvear();
            frame.setVisible(true);
        });
    }
}