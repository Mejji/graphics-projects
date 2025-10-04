package csegraphics;

/**
 *
 * @author Mejji
 */
import java.awt.geom.*;
import java.awt.*;
import java.awt.event.*;  // needed for WindowAdapter & WindowEvent

public class stringandgeom extends Frame 
{

    public void paint(Graphics g) 
    {
        Graphics2D g2d = (Graphics2D) g;
        BasicStroke bs = new BasicStroke(10.0f);
        g2d.setStroke(bs);
        g2d.setFont(new Font("Arial", Font.BOLD, 40));

        //1. Draw the string
        g2d.drawString("First Drawing in Java - ALVEAR", 100, 130); //move right 100 and move down 130
        
        //2. Draw the line
        Line2D.Double line1 = new Line2D.Double(80, 150, 180, 300);
        g2d.draw(line1);
        
        //3. Draw QuadCurve
        g2d.setPaint(Color.MAGENTA);
        QuadCurve2D.Double qc1 = new QuadCurve2D.Double(80, 150, 100, 400,180,300);
        g2d.draw(qc1);
        
        //4. Draw Cubic 
        CubicCurve2D.Double cc1 = new CubicCurve2D.Double(180, 300, 250, 440, 350, 150, 410, 300);
        g2d.draw(cc1);
        
        //5. Draw the Rectangle Square
        Rectangle2D.Double rect1 = new Rectangle2D.Double(410,300,300,250);
        g2d.setPaint(Color.RED);
        g2d.draw(rect1);
        g2d.setPaint(Color.ORANGE);
        g2d.fill(rect1);
        
        //6. Create the Optimal square inside the rectangle in the most lower left side (fill green)
        Rectangle2D.Double sq1 = new Rectangle2D.Double(410, 300, 250, 250);
        g2d.setPaint(Color.GREEN);
        g2d.fill(sq1);
        
        //Draw the ellipse whose equation is (x-400)^2 / 14400 + (y - 350)^2  / 8100 = 1
        //w = 2*sqrt(14400) = 240
        //h = 2*sqrt(8100) = 180
        //x = 400 - 120 = 280
        //y = 350 - 90 = 260
        
        Ellipse2D.Double elli1 = new Ellipse2D.Double(280, 260, 240, 180);
        g2d.setPaint(Color.PINK);
        g2d.fill(elli1);
        
        
        //7. Drawing an ARC
        Arc2D.Double arc_pie = new Arc2D.Double(sq1,20,80,Arc2D.PIE);
        g2d.fill(arc_pie);
        
        Arc2D.Double arc_open = new Arc2D.Double(sq1,110,40,Arc2D.OPEN);
        g2d.fill(arc_open);
        
        Arc2D.Double arc_chord = new Arc2D.Double(sq1,170,45,Arc2D.CHORD);
        g2d.fill(arc_chord);
        
        //8. General Path
        GeneralPath gp = new GeneralPath();
        gp.moveTo(50,50);
        gp.lineTo(50, 200);
        gp.quadTo(150, 500, 250, 200);
        gp.curveTo(350, 100, 150, 150, 100, 100);
        gp.lineTo(50, 50);
        g2d.setPaint(Color.GREEN);
        g2d.fill(gp);
        
        //9. Areas
        Ellipse2D.Double circ1 = new Ellipse2D.Double(150,200,200, 200 );
        g2d.fill(circ1);
        
        Area A = new Area(gp);
        Area B = new Area(circ1);
        
        //10.1 Union
        A.add(B);
        g2d.fill(A);
        
        //10.2 Intersect
        A.intersect(B);
        g2d.fill(A);
        
        
    }

    public static void main(String[] args) 
    {
        stringandgeom sj = new stringandgeom();
        sj.setBackground(Color.CYAN);
        sj.setTitle("Drawing String in Java");
        sj.setSize(900, 700);
        sj.setForeground(Color.blue);
        sj.setVisible(true);

        // Add a Window Listener for exit
        sj.addWindowListener(new WindowAdapter() 
        {
            public void windowClosing(WindowEvent we) 
            {
                System.exit(0); 
            }
        });
    }
}

