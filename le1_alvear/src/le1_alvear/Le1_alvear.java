package le1_alvear;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;

public class Le1_alvear extends Frame {

    @Override
    public void paint(Graphics g) {
        Graphics2D ctx = (Graphics2D) g;
        ctx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        ctx.setStroke(new BasicStroke(2.0f));
        ctx.setFont(new Font("Arial", Font.PLAIN, 16));

        // ------------------------------------------------------------
        // 1.A  Outline segments for the T-shape (kept for reference)
        // ------------------------------------------------------------
        Line2D.Double tSegAB = new Line2D.Double(100, 250, 150, 250);
        Line2D.Double tSegBC = new Line2D.Double(150, 250, 150, 200);
        Line2D.Double tSegCD = new Line2D.Double(150, 200, 200, 200);
        Line2D.Double tSegDE = new Line2D.Double(200, 200, 200, 500);
        Line2D.Double tSegEF = new Line2D.Double(200, 500, 400, 500);
        Line2D.Double tSegFG = new Line2D.Double(400, 500, 400, 200);
        Line2D.Double tSegGH = new Line2D.Double(400, 200, 450, 200);
        Line2D.Double tSegHI = new Line2D.Double(450, 200, 450, 250);
        Line2D.Double tSegIJ = new Line2D.Double(450, 250, 500, 250);
        Line2D.Double tSegJK = new Line2D.Double(500, 250, 500, 150);
        Line2D.Double tSegKL = new Line2D.Double(500, 150, 450, 100);
        Line2D.Double tSegLM = new Line2D.Double(450, 100, 150, 100);
        Line2D.Double tSegMN = new Line2D.Double(150, 100, 100, 150);
        Line2D.Double tSegNA = new Line2D.Double(100, 150, 100, 250);
        // (leave outlines commented if you don’t want them)
        // ctx.setPaint(Color.BLACK);
        // ctx.draw(tSegAB); ctx.draw(tSegBC); ctx.draw(tSegCD); ctx.draw(tSegDE);
        // ctx.draw(tSegEF); ctx.draw(tSegFG); ctx.draw(tSegGH); ctx.draw(tSegHI);
        // ctx.draw(tSegIJ); ctx.draw(tSegJK); ctx.draw(tSegKL); ctx.draw(tSegLM);
        // ctx.draw(tSegMN); ctx.draw(tSegNA);

        // ------------------------------------------------------------
        // 1.B  T-shape as a GeneralPath (filled magenta)
        // ------------------------------------------------------------
        GeneralPath tShapePath = new GeneralPath();
        tShapePath.moveTo(100, 250);
        tShapePath.lineTo(150, 250);
        tShapePath.lineTo(150, 200);
        tShapePath.lineTo(200, 200);
        tShapePath.lineTo(200, 500);
        tShapePath.lineTo(400, 500);
        tShapePath.lineTo(400, 200);
        tShapePath.lineTo(450, 200);
        tShapePath.lineTo(450, 250);
        tShapePath.lineTo(500, 250);
        tShapePath.lineTo(500, 150);
        tShapePath.lineTo(450, 100);
        tShapePath.lineTo(150, 100);
        tShapePath.lineTo(100, 150);
        tShapePath.closePath();

        ctx.setPaint(new Color(180, 60, 230, 160)); // magenta-ish
        ctx.fill(tShapePath);
        ctx.setPaint(Color.BLACK);
        ctx.draw(tShapePath);

        // ------------------------------------------------------------
        // 2.A  Boat with individual lines (kept for reference)
        // ------------------------------------------------------------
        Line2D.Double bAB = new Line2D.Double(100, 350, 150, 450);
        Line2D.Double bBC = new Line2D.Double(150, 450, 450, 450);
        Line2D.Double bCD = new Line2D.Double(450, 450, 500, 350);
        Line2D.Double bDE = new Line2D.Double(500, 350, 305, 350);
        Line2D.Double bEF = new Line2D.Double(295, 350, 305, 350);
        Line2D.Double bFA = new Line2D.Double(295, 350, 100, 350);
        Line2D.Double bFG = new Line2D.Double(295, 350, 295, 150);
        Line2D.Double bGH = new Line2D.Double(295, 150, 305, 150);
        Line2D.Double bHJ = new Line2D.Double(305, 150, 305, 200);
        Line2D.Double bJI = new Line2D.Double(305, 200, 455, 200);
        Line2D.Double bJF = new Line2D.Double(305, 200, 305, 350);
        Line2D.Double bDiag = new Line2D.Double(455, 200, 305, 150);
        // ctx.setPaint(Color.BLACK);
        // ctx.draw(bAB); ctx.draw(bBC); ctx.draw(bCD); ctx.draw(bDE); ctx.draw(bEF);
        // ctx.draw(bFA); ctx.draw(bFG); ctx.draw(bGH); ctx.draw(bHJ); ctx.draw(bJI);
        // ctx.draw(bJF); ctx.draw(bDiag);

        // ------------------------------------------------------------
        // 2.B  Boat as a GeneralPath (filled green)
        // ------------------------------------------------------------
        GeneralPath boatPath = new GeneralPath();
        boatPath.moveTo(100, 350);
        boatPath.lineTo(150, 450);
        boatPath.lineTo(450, 450);
        boatPath.lineTo(500, 350);
        boatPath.lineTo(295, 350);
        boatPath.lineTo(295, 150);
        boatPath.lineTo(305, 150);
        boatPath.lineTo(405, 200);  // (adjusted to match your last edit)
        boatPath.lineTo(355, 200);
        boatPath.lineTo(305, 200);
        boatPath.lineTo(305, 350);
        boatPath.lineTo(100, 350);
        boatPath.closePath();

        ctx.setPaint(new Color(0, 255, 0, 160)); // green
        ctx.fill(boatPath);
        ctx.setPaint(Color.BLACK);
        ctx.draw(boatPath);

        // ------------------------------------------------------------
        // 3a  Relative difference  (T minus Boat)
        // ------------------------------------------------------------
        Area areaTOnly = new Area(tShapePath);
        areaTOnly.subtract(new Area(boatPath));
        ctx.setPaint(new Color(255, 0, 0, 140)); // red
        ctx.fill(areaTOnly);

        // ------------------------------------------------------------
        // 3b  Symmetric difference  (T XOR Boat)
        // ------------------------------------------------------------
        Area areaSym = new Area(tShapePath);
        areaSym.exclusiveOr(new Area(boatPath));
        ctx.setPaint(new Color(0, 0, 255, 120)); // blue
        ctx.fill(areaSym);

        // Optional labels
        ctx.setPaint(Color.BLACK);
        ctx.drawString("3a: Relative Difference (T \\ Boat) = Red", 80, 560);
        ctx.drawString("3b: Symmetric Difference (T ⊕ Boat) = Blue", 80, 580);
    }

    public static void main(String[] args) {
        Le1_alvear win = new Le1_alvear();
        win.setTitle("Written by Alvear, Mark Josh");
        win.setBackground(Color.WHITE);
        win.setSize(900, 900);
        win.setForeground(Color.BLACK);
        win.setVisible(true);

        win.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override public void windowClosing(java.awt.event.WindowEvent e) {
                System.exit(0);
            }
        });
    }
}
