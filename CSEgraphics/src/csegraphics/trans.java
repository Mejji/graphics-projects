package csegraphics;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.*;
import java.awt.geom.AffineTransform;

public class trans extends Frame {

    private int windowHeight;

    trans(int height) {
        windowHeight = height;
    }

    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        AffineTransform yUp = new AffineTransform();
        yUp.setToScale(1, -1);
        AffineTransform translate = new AffineTransform();
        translate.setToTranslation(40, windowHeight - 50);
        yUp.preConcatenate(translate);
        g2d.transform(yUp);
        g2d.setStroke(new BasicStroke(5.0f));

        //generate original image
        Rectangle2D.Double rect = new Rectangle2D.Double(180, 120, 100, 60);
        g2d.draw(rect);

        //apply the transformation - scaling sx = 2, sy = 0.5
        AffineTransform scaling = new AffineTransform();
        scaling.setToScale(2, 0.5);

        //apply rotation at 45 degrees
        AffineTransform rotation = new AffineTransform();
        rotation.setToRotation(Math.PI / 4);

        //apply shearing sx = 1, sy =0
        AffineTransform shear = new AffineTransform();
        shear.setToShear(1, 0);

        //apply translation with dx = 140, dy= 80
        AffineTransform translation = new AffineTransform();
        translation.setToTranslation(140, 80);

        //draw the scaled object usng dashed line
        g2d.setStroke(new BasicStroke(5.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 8.0f, new float[]{50.0f, 10.0f}, 4.0f));
        g2d.draw(scaling.createTransformedShape(rect));
        g2d.draw(rotation.createTransformedShape(rect));
        g2d.draw(shear.createTransformedShape(rect));
        g2d.draw(translation.createTransformedShape(rect));

        //Draw the cartesian place (Q1 only)
        g2d.setStroke(new BasicStroke(1.0f));
        drawSimpleCoordinateSystem(800, 400, g2d);
    }

    public static void drawSimpleCoordinateSystem(int xmax, int ymax,Graphics2D g2d) 
    {
        int xOffset = 0;
        int yOffset = 0;
        int step = 20;
        String s;
        //Remember the actual font.
        Font fo = g2d.getFont();
        //Use a small font.
        int fontSize = 13;
        Font fontCoordSys = new Font("serif", Font.PLAIN, fontSize);

        //To make the font upside down, a reflection w.r.t. the x-axis is needed.
        AffineTransform flip = new AffineTransform();
        flip.setToScale(1, -1);

        //Shift the font back to the baseline after reflection.
        AffineTransform lift = new AffineTransform();
        lift.setToTranslation(0, fontSize);
        flip.preConcatenate(lift);

        //Generate the font with the letters upside down.
        Font fontUpsideDown = fontCoordSys.deriveFont(flip);

        g2d.setFont(fontUpsideDown);

        //x-axis
        g2d.drawLine(xOffset, yOffset, xmax, yOffset);
        //Marks and labels for the x-axis.
        for (int i = xOffset + step; i <= xmax; i = i + step) {
            g2d.drawLine(i, yOffset - 2, i, yOffset + 2);
            g2d.drawString(String.valueOf(i), i - 7, yOffset - 30);
        }

        //y-axis
        g2d.drawLine(xOffset, yOffset, xOffset, ymax);

        //Marks and labels for the y-axis.
        s = "  "; //for indention of numbers < 100
        for (int i = yOffset + step; i <= ymax; i = i + step) {
            g2d.drawLine(xOffset - 2, i, xOffset + 2, i);
            if (i > 99) {
                s = "";
            }
            g2d.drawString(s + String.valueOf(i), xOffset - 25, i - 20);
        }

        //Reset to the original font.
        g2d.setFont(fo);

    }

    public static void main(String[] args) {
        int height = 700;
        trans t = new trans(height);
        t.setTitle("Transformation Demo - Alvear");
        t.setSize(700, height);
        t.setVisible(true);
        t.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });

    }

}
