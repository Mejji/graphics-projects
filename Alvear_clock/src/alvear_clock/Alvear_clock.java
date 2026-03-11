package alvear_clock;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.GeneralPath;
import java.awt.image.*;
import javax.swing.*;
import java.util.*;
import javax.swing.Timer;
import java.text.SimpleDateFormat;

public class Alvear_clock extends JFrame {

    AnalogDigitalClock clockFace;

    public static void main(String[] args) {
        JFrame window = new Alvear_clock();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
    }

    public Alvear_clock() {
        Container content = this.getContentPane();
        content.setLayout(new BorderLayout());
        clockFace = new AnalogDigitalClock();
        content.add(clockFace, BorderLayout.CENTER);

        this.setTitle("Alvear_clock");
        this.pack();

        clockFace.start();
    }
}

class AnalogDigitalClock extends JPanel {

    private int hours = 0;
    private int minutes = 0;
    private int seconds = 0;
    private int millis = 0;

    private static final int spacing = 10;
    private static final float radPerSecMin = (float) (Math.PI / 30.0);
    private int size;
    private int centerX;
    private int centerY;
    private BufferedImage clockImage;
    private Timer t;
    private SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
    private SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy");

    public AnalogDigitalClock() {
        this.setPreferredSize(new Dimension(400, 400));
        this.setBackground(Color.white);

        t = new Timer(1000,
                new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                update();
            }
        });
    }

    public void update() {
        this.repaint();
    }

    public void start() {
        t.start();
    }

    public void stop() {
        t.stop();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        size = ((w < h) ? w : h) - 2 * spacing;
        centerX = size / 2 + spacing;
        centerY = size / 2 + spacing;

        if (clockImage == null
                || clockImage.getWidth() != w
                || clockImage.getHeight() != h) {

            clockImage = (BufferedImage) (this.createImage(w, h));

            Graphics2D gc = clockImage.createGraphics();
            gc.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            drawClockFace(gc);
        }

        Calendar now = Calendar.getInstance();
        hours = now.get(Calendar.HOUR);
        minutes = now.get(Calendar.MINUTE);
        seconds = now.get(Calendar.SECOND);
        millis = now.get(Calendar.MILLISECOND);
        int day = now.get(Calendar.DAY_OF_MONTH);

        g2.drawImage(clockImage, 0, 0, this);

        drawDateWindow(g2, day);

        drawDigitalTime(g2, now);

        drawClockHands(g2);
    }

    private void drawClockHands(Graphics2D g2) {
        int secondRadius = size / 2 - 20;
        int minuteRadius = secondRadius - 10;
        int hourRadius = secondRadius / 2;

        // Fractional seconds for smoother second hand position (millis currently updates once per second)
        float fseconds = seconds + (float) millis / 1000f;
        // unified convention: angle = PI/2 - theta  -> 0 points at 12 o'clock
        float secondAngle = (float) (Math.PI / 2.0 - radPerSecMin * fseconds);

        float fminutes = (float) (minutes + fseconds / 60.0);
        float minuteAngle = (float) (Math.PI / 2.0 - radPerSecMin * fminutes);

        // hours in 12-hour space
        float hours12 = hours % 12;
        float fhours = (float) (hours12 + fminutes / 60.0);
        // each hour = PI/6 (30 degrees)
        float hourAngle = (float) (Math.PI / 2.0 - (fhours * Math.PI / 6.0));

        float hourSine = (float) Math.sin(hourAngle);
        float hourCosine = (float) Math.cos(hourAngle);

        // Hour hand
        g2.setColor(new Color(30, 30, 30));
        g2.setStroke(new BasicStroke(8, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        drawRadius(g2, centerX, centerY, hourAngle, 0, hourRadius);

        // Draw circle with hollow cross near the tip
        int crossX = centerX + (int) ((hourRadius * 0.75) * Math.cos(hourAngle));
        int crossY = centerY - (int) ((hourRadius * 0.75) * Math.sin(hourAngle));

        g2.setColor(new Color(30, 30, 30));
        g2.fillOval(crossX - 10, crossY - 10, 20, 20);

        g2.setColor(Color.WHITE);
        int crossSize = 8;
        g2.fillRect(crossX - 2, crossY - crossSize, 4, crossSize * 2);
        g2.fillRect(crossX - crossSize, crossY - 2, crossSize * 2, 4);

        // Minute hand
        g2.setColor(new Color(30, 30, 30));
        g2.setStroke(new BasicStroke(6, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        drawRadius(g2, centerX, centerY, minuteAngle, 0, minuteRadius);

        // Second hand
        float secSine = (float) Math.sin(secondAngle);
        float secCosine = (float) Math.cos(secondAngle);

        g2.setColor(new Color(200, 0, 0));
        g2.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        // use a small tail by passing negative minRadius
        drawRadius(g2, centerX, centerY, secondAngle, -15, secondRadius);

        int lollipopX = centerX + (int) ((secondRadius * 0.7) * Math.cos(secondAngle));
        int lollipopY = centerY - (int) ((secondRadius * 0.7) * Math.sin(secondAngle));

        g2.setColor(new Color(200, 0, 0));
        g2.fillOval(lollipopX - 6, lollipopY - 6, 12, 12);
        g2.setColor(Color.WHITE);
        g2.fillOval(lollipopX - 3, lollipopY - 3, 6, 6);

        g2.setColor(new Color(30, 30, 30));
        g2.fillOval(centerX - 8, centerY - 8, 16, 16);
        g2.setColor(new Color(200, 0, 0));
        g2.fillOval(centerX - 4, centerY - 4, 8, 8);
    }

    private void drawClockFace(Graphics2D g) {
        g.setColor(new Color(180, 180, 180));
        g.fillOval(spacing - 5, spacing - 5, size + 10, size + 10);

        g.setColor(Color.white);
        g.fillOval(spacing, spacing, size, size);

        g.setColor(new Color(30, 30, 30));
        g.setStroke(new BasicStroke(2));
        g.drawOval(spacing, spacing, size, size);

        String[] romanNumerals = {"XII", "I", "II", "", "IV", "V", "VI", "VII", "VIII", "IX", "X", "XI"};
        g.setFont(new Font("Serif", Font.BOLD, 24));
        FontMetrics fm = g.getFontMetrics();

        for (int i = 0; i < 12; i++) {
            if (i == 3) {
                continue;
            }
            // Use the same angle convention used for hands
            double angle = Math.PI / 2.0 - (i * Math.PI / 6.0);
            int radius = size / 2 - 35;
            int x = centerX + (int) (radius * Math.cos(angle));
            int y = centerY - (int) (radius * Math.sin(angle));

            String numeral = romanNumerals[i];
            int strWidth = fm.stringWidth(numeral);
            int strHeight = fm.getAscent();

            g.setColor(new Color(30, 30, 30));
            g.drawString(numeral, x - strWidth / 2, y + strHeight / 3);
        }

        g.setStroke(new BasicStroke(2));
        for (int sec = 0; sec < 60; sec++) {
            if (sec % 5 != 0) {
                int ticStart = size / 2 - 15;
                int ticEnd = size / 2 - 10;
                g.setColor(new Color(30, 30, 30));
                double angle = Math.PI / 2.0 - (radPerSecMin * sec);
                drawRadius(g, centerX, centerY, angle, ticStart, ticEnd);
            }
        }

        g.setStroke(new BasicStroke(3));
        for (int hour = 0; hour < 12; hour++) {
            int ticStart = size / 2 - 20;
            int ticEnd = size / 2 - 10;
            g.setColor(new Color(30, 30, 30));
            double angle = Math.PI / 2.0 - (radPerSecMin * hour * 5);
            drawRadius(g, centerX, centerY, angle, ticStart, ticEnd);
        }

        Font alvearsFont = new Font("Serif", Font.BOLD, 20);
        Font dateJustFont = new Font("Serif", Font.PLAIN, 14);

        g.setFont(alvearsFont);
        FontMetrics fmAlvears = g.getFontMetrics();
        String alvearsText = "ALVEARS";
        int alvearsWidth = fmAlvears.stringWidth(alvearsText);
        int alvearsY = centerY - size / 5;

        g.setColor(new Color(30, 30, 30));
        int swordTopY = alvearsY - fmAlvears.getAscent() - 40;

        g.fillOval(centerX - 4, swordTopY, 8, 8);
        g.setStroke(new BasicStroke(2));
        g.drawArc(centerX - 5, swordTopY - 3, 10, 6, 0, 180);

        int gripTopY = swordTopY + 7;
        int gripHeight = 15;
        g.fillRect(centerX - 3, gripTopY, 6, gripHeight);
        g.setColor(new Color(100, 100, 100));
        g.setStroke(new BasicStroke(1.5f));
        for (int i = 2; i < gripHeight; i += 3) {
            g.drawLine(centerX - 3, gripTopY + i, centerX + 3, gripTopY + i - 2);
        }
        g.setColor(new Color(30, 30, 30));

        int crossguardY = gripTopY + gripHeight;
        GeneralPath crossguard = new GeneralPath();
        crossguard.moveTo(centerX - 1, crossguardY);
        crossguard.quadTo(centerX - 8, crossguardY + 2, centerX - 14, crossguardY - 2); // Left top curve
        crossguard.quadTo(centerX - 8, crossguardY + 8, centerX - 1, crossguardY + 4);   // Left bottom curve
        crossguard.lineTo(centerX + 1, crossguardY + 4);
        crossguard.quadTo(centerX + 8, crossguardY + 8, centerX + 14, crossguardY - 2); // Right bottom curve
        crossguard.quadTo(centerX + 8, crossguardY + 2, centerX + 1, crossguardY);    // Right top curve
        crossguard.closePath();
        g.fill(crossguard);

        int bladeTopY = crossguardY + 4;
        Polygon blade = new Polygon();
        blade.addPoint(centerX, bladeTopY + 15); // Tip
        blade.addPoint(centerX - 5, bladeTopY);  // Top-left
        blade.addPoint(centerX + 5, bladeTopY);  // Top-right
        g.fill(blade);

        g.drawString(alvearsText, centerX - alvearsWidth / 2, alvearsY);

        g.setFont(dateJustFont);
        FontMetrics fmDate = g.getFontMetrics();
        String dateJust = "DATE JUST";
        int dateJustWidth = fmDate.stringWidth(dateJust);
        g.drawString(dateJust, centerX - dateJustWidth / 2, alvearsY + fmDate.getAscent());

        g.setFont(new Font("Times New Roman", Font.PLAIN, 12));
        FontMetrics seaD = g.getFontMetrics();
        String sea = "CRUSADER";
        int seaDWidth = seaD.stringWidth(sea);
        g.setColor(Color.BLACK);
        g.drawString(sea, centerX - seaDWidth / 2, centerY + size / 6);

        g.setFont(new Font("Serif", Font.PLAIN, 10));
        FontMetrics pmade = g.getFontMetrics();
        String made = "MADE IN THE PHILIPPINES";
        int pmadeWidth = pmade.stringWidth(made);
        g.setColor(Color.RED);
        g.drawString(made, centerX - pmadeWidth / 2, centerY + size / 5 + size / 25);
    }

    /**
     * Draw a radial line. Angle is in the same convention as the rest of the code:
     * angle = Math.PI/2 - theta, so that angle = PI/2 points to 12 o'clock.
     * minRadius and maxRadius are distances from center; minRadius can be negative
     * to draw a tail behind the center.
     */
    private void drawRadius(Graphics g, int x, int y, double angle,
            int minRadius, int maxRadius) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);

        int dxmin = (int) (minRadius * cos);
        int dymin = (int) (minRadius * sin);

        int dxmax = (int) (maxRadius * cos);
        int dymax = (int) (maxRadius * sin);

        // Note: screen y increases downward so subtract dy to go upward
        g.drawLine(x + dxmin, y - dymin, x + dxmax, y - dymax);
    }

    private void drawDateWindow(Graphics2D g2, int day) {
        int dateWindowWidth = 30;
        int dateWindowHeight = 24;
        int dateX = centerX + size / 2 - 55;
        int dateY = centerY - dateWindowHeight / 2;

        g2.setColor(Color.white);
        g2.fillRect(dateX, dateY, dateWindowWidth, dateWindowHeight);

        g2.setColor(new Color(30, 30, 30));
        g2.setStroke(new BasicStroke(2));
        g2.drawRect(dateX, dateY, dateWindowWidth, dateWindowHeight);

        g2.setFont(new Font("Serif", Font.BOLD, 16));
        FontMetrics fm = g2.getFontMetrics();
        String dayStr = String.valueOf(day);
        int strWidth = fm.stringWidth(dayStr);
        int strHeight = fm.getAscent();

        g2.setColor(new Color(30, 30, 30));
        g2.drawString(dayStr, dateX + (dateWindowWidth - strWidth) / 2,
                dateY + (dateWindowHeight + strHeight) / 2 - 2);
    }

    private void drawDigitalTime(Graphics2D g2, Calendar now) {
        int digitalY = centerY + size / 12;

        String time = String.format("%02d:%02d:%02d",
            now.get(Calendar.HOUR) == 0 ? 12 : now.get(Calendar.HOUR),
            now.get(Calendar.MINUTE),
            now.get(Calendar.SECOND));

        g2.setColor(Color.WHITE);
        int boxWidth = 80;
        int boxHeight = 22;
        g2.fillRect(centerX - boxWidth / 2, digitalY - boxHeight + 6,
                    boxWidth, boxHeight);

        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2));
        g2.drawRect(centerX - boxWidth / 2, digitalY - boxHeight + 6,
                    boxWidth, boxHeight);

        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Monospaced", Font.BOLD, 14));
        FontMetrics fm = g2.getFontMetrics();
        int timeWidth = fm.stringWidth(time);
        g2.drawString(time, centerX - timeWidth / 2, digitalY);
    }
}
