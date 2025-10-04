package mp1alvear;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class Mp1alvear extends Frame {

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        //renders
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(3.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        //Line
        Color charcoal = new Color(46, 52, 64);
        //quadcurve
        Color teal = new Color(63, 185, 132);
        //cubiccurve
        Color indigo = new Color(90, 125, 255);
        //rectabgle
        Color crimson = new Color(232, 90, 112);
        //elipse
        Color gold = new Color(246, 201, 14);
        //arc
        Color coral = new Color(255, 138, 92);
        //Texts
        Color ink = new Color(26, 26, 26);

        //Name
        g2d.setFont(new Font("Arial", Font.PLAIN, 10));
        g2d.setColor(ink);
        g2d.drawString("Alvear, Mark Josh", 30, 50);
        g2d.drawString("4CSE", 30, 65);

        //lines(polygon A-B-C-D-E-A)
        g2d.setColor(charcoal);
        int[] x = {80, 500, 300, 450, 200, 80};
        int[] y = {475, 125, 300, 500, 375, 475};
        g2d.drawPolyline(x, y, x.length);

        //quad curve
        g2d.setColor(teal);
        QuadCurve2D.Double quad = new QuadCurve2D.Double(175, 500, 200, 300, 400, 550);
        g2d.draw(quad);

        //cubic curve
        g2d.setColor(indigo);
        CubicCurve2D.Double cubic = new CubicCurve2D.Double(375, 300, 450, 550, 550, 450, 575, 350);
        g2d.draw(cubic);

        //rectangle
        g2d.setColor(crimson);
        Rectangle2D.Double rect = new Rectangle2D.Double(100, 100, 150, 200);
        g2d.draw(rect);

        // Ellipse from equation: ((x-100)^2 / 400) + ((y-90)^2 / 900) = 1
        // Center (h,k) = (100, 90)
        // Semi-minor axis b = sqrt(400) = 20 -> width = 2*b = 40
        // Semi-major axis a = sqrt(900) = 30 -> height = 2*a = 60
        // Upper-left corner: (h-b, k-a) = (100-20, 90-30) = (80, 60)
        g2d.setColor(gold);
        Ellipse2D.Double ellipse = new Ellipse2D.Double(80, 60, 40, 60);
        g2d.draw(ellipse);

        //arc pie
        g2d.setColor(coral);
        Arc2D.Double arcPie = new Arc2D.Double(300, 50, 100, 75, 15, 75, Arc2D.PIE);
        g2d.fill(arcPie);

        //labels
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        g2d.setColor(ink);
        g2d.drawString("Line", 255, 400);
        g2d.drawString("QuadCurve", 360, 585);
        g2d.drawString("CubicCurve", 585, 360);
        g2d.drawString("Rectangle", 120, 95);
        g2d.drawString("Ellipse", 130, 120);
        g2d.drawString("Arc (Pie)", 415, 100);
    }

    public static void main(String[] args) {
        Mp1alvear f = new Mp1alvear();
        f.setTitle("Machine Problem 1 – Java 2D");
        f.setSize(900, 700);
        f.setBackground(new Color(245, 245, 245));
        f.setVisible(true);

        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }
}
