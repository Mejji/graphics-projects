package mp_trans_alvear;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.*;

public class Mp_trans_alvear extends Frame 
{
    private static final double ARM_EXTEND = 100;  
    private static final double HALF_THICK = 40;  
    private static final double THICK = 2 * HALF_THICK; 
    private static final double RADIUS = 20;    
    private static final double START_A_X = 250;
    private static final double START_A_Y = 280;

    @Override
    public void paint(Graphics g) 
    {
        Graphics2D g2d = (Graphics2D) g;
        
        AffineTransform saveTx = g2d.getTransform();
        Stroke saveStroke = g2d.getStroke();
        Font saveFont = g2d.getFont();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int marginLeft = 40, marginTop = 50;
        AffineTransform world = new AffineTransform();
        world.setToScale(1, -1);
        world.preConcatenate(AffineTransform.getTranslateInstance(marginLeft, getHeight() - marginTop));
        g2d.transform(world);

        g2d.setColor(Color.DARK_GRAY);
        g2d.setStroke(new BasicStroke(1f));
        drawSimpleCoordinateSystem(1000, 640, g2d);
        
        // cross
        final double totalSpan = 2 * ARM_EXTEND + THICK; 
        final double cx = START_A_X + totalSpan / 2.0;  
        final double cy = START_A_Y;
        Shape cross = buildRoundedCross(cx, cy, totalSpan, THICK, RADIUS); // GeneralPath

        // original
        g2d.setStroke(new BasicStroke(3f));
        g2d.setColor(new Color(30, 30, 30));
        g2d.draw(cross);

        //dashed
        float[] dash = {16f, 10f};
        Stroke dashed = new BasicStroke(3f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10f, dash, 0f);
        g2d.setStroke(dashed);

        // 1) Scaling (2, 1/4) blue, dashed
        g2d.setColor(new Color(0, 114, 178));
        AffineTransform s = AffineTransform.getScaleInstance(2.0, 0.25);
        g2d.draw(s.createTransformedShape(cross));

        // 2) Rotation 30° green, dashed
        g2d.setColor(new Color(0, 158, 115));
        AffineTransform r = AffineTransform.getRotateInstance(Math.toRadians(30));
        g2d.draw(r.createTransformedShape(cross));

        // 3) Shearing (1, 1/2)  magenta, dashed
        g2d.setColor(new Color(204, 121, 167));
        AffineTransform sh = AffineTransform.getShearInstance(1.0, 0.5);
        g2d.draw(sh.createTransformedShape(cross));

        // 4) Translation (100, 60)  cyan, dashed
        g2d.setColor(new Color(0, 190, 190));
        AffineTransform t = AffineTransform.getTranslateInstance(100, 60);
        g2d.draw(t.createTransformedShape(cross));

        //restored them
        g2d.setTransform(saveTx);
        g2d.setStroke(saveStroke);
        g2d.setFont(saveFont);
    }
    private static Shape buildRoundedCross(double cx, double cy, double span, double thick, double radius) 
    {
        RoundRectangle2D.Double hBar = new RoundRectangle2D.Double(
                cx - span / 2.0,
                cy - thick / 2.0,
                span, thick,
                2 * radius, 2 * radius
        );
        RoundRectangle2D.Double vBar = new RoundRectangle2D.Double(
                cx - thick / 2.0,
                cy - span / 2.0,
                thick, span,
                2 * radius, 2 * radius
        );

        Area a = new Area(hBar);
        a.add(new Area(vBar));
        PathIterator it = a.getPathIterator(null);
        GeneralPath gp = new GeneralPath(Path2D.WIND_NON_ZERO);
        float[] c = new float[6];
        while (!it.isDone()) {
            int seg = it.currentSegment(c);
            switch (seg) {
                case PathIterator.SEG_MOVETO -> gp.moveTo(c[0], c[1]);
                case PathIterator.SEG_LINETO -> gp.lineTo(c[0], c[1]);
                case PathIterator.SEG_QUADTO -> gp.quadTo(c[0], c[1], c[2], c[3]);
                case PathIterator.SEG_CUBICTO -> gp.curveTo(c[0], c[1], c[2], c[3], c[4], c[5]);
                case PathIterator.SEG_CLOSE -> gp.closePath();
            }
            it.next();
        }
        return gp;
    }

    // coords
    private static void drawSimpleCoordinateSystem(int xmax, int ymax, Graphics2D g2d) 
    {
        int step = 20;
        g2d.drawLine(0, 0, xmax, 0);
        g2d.drawLine(0, 0, 0, ymax);
        for (int x = step; x <= xmax; x += step) g2d.drawLine(x, -2, x, 2);
        for (int y = step; y <= ymax; y += step) g2d.drawLine(-2, y, 2, y);

        Font f = new Font("SansSerif", Font.PLAIN, 11);
        for (int x = step; x <= xmax; x += step) {
            AffineTransform s = g2d.getTransform();
            g2d.translate(x - 6, -16);
            g2d.scale(1, -1);
            g2d.setFont(f);
            g2d.drawString(String.valueOf(x), 0, 0);
            g2d.setTransform(s);
        }
        for (int y = step; y <= ymax; y += step) {
            AffineTransform s = g2d.getTransform();
            g2d.translate(-26, -(y - 4));
            g2d.scale(1, -1);
            g2d.setFont(f);
            g2d.drawString(String.valueOf(y), 0, 0);
            g2d.setTransform(s);
        }
    }

    public static void main(String[] args) 
    {
        Mp_trans_alvear f = new Mp_trans_alvear();
        f.setTitle("Machine Problem Transformation - ALVEAR");
        f.setSize(1100, 750);
        f.setVisible(true);
        f.addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { System.exit(0); }
        });
    }
}
