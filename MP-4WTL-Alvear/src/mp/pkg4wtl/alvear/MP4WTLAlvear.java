package mp.pkg4wtl.alvear;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.*;
import java.util.List;

public class MP4WTLAlvear extends JFrame {

    public MP4WTLAlvear() {
        setTitle("4 way traffic with different cars");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        IntersectionPanel panel = new IntersectionPanel(900, 900);
        getContentPane().add(panel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        panel.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MP4WTLAlvear::new);
    }
}

/* --- Panel & simulation --- */
class IntersectionPanel extends JPanel {

    private final int width, height;
    private final java.util.Timer spawnTimer = new java.util.Timer(true);
    private final javax.swing.Timer animationTimer;
    private final java.util.List<Lane> lanes = new ArrayList<>();
    private final int centerX = 450, centerY = 450;
    private final Random rng = new Random();

    // timing in seconds
    private final int GREEN_SEC = 8;
    private final int YELLOW_SEC = 3;

    public IntersectionPanel(int w, int h) {
        this.width = w;
        this.height = h;
        setPreferredSize(new Dimension(w, h));
        setBackground(new Color(28, 28, 28));

        int laneSpacing = 100;

        // N->S lane (right side of vertical road) - stop just before intersection box
        lanes.add(new Lane(1, "N->S", centerX + laneSpacing / 2, -80, 0, 1, centerY - 130));

        // S->N lane (left side of vertical road)
        lanes.add(new Lane(3, "S->N", centerX - laneSpacing / 2, height + 80, 0, -1, centerY + 130));

        // W->E lane (bottom side of horizontal road)
        lanes.add(new Lane(2, "W->E", -80, centerY + laneSpacing / 2, 1, 0, centerX - 130));

        // E->W lane (top side of horizontal road)
        lanes.add(new Lane(4, "E->W", width + 80, centerY - laneSpacing / 2, -1, 0, centerX + 130));

        TrafficController controller = new TrafficController(GREEN_SEC, YELLOW_SEC);
        for (Lane ln : lanes) {
            ln.setController(controller);
        }

        // attempt spawn every 250ms with higher probability for more realistic traffic
        spawnTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                for (Lane lane : lanes) {
                    if (rng.nextDouble() < 0.25) {
                        SwingUtilities.invokeLater(lane::spawnCarIfSpace);
                    }
                }
            }
        }, 0, 250);

        // animation ~25 FPS
        animationTimer = new javax.swing.Timer(40, e -> {
            double dt = 0.04;
            for (Lane lane : lanes) lane.update(dt);
            repaint();
        });

        controller.start();
    }

    public void start() { animationTimer.start(); }

    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // --- CONCRETE CORNERS + BUILDINGS (replaces grass) ---
        int pad = 16;
        int block = 200;
        Color concrete = new Color(135, 135, 135);
        g.setColor(concrete);
        g.fillRect(0, 0, block, block);                       // NW plaza
        g.fillRect(width - block, 0, block, block);           // NE plaza
        g.fillRect(0, height - block, block, block);          // SW plaza
        g.fillRect(width - block, height - block, block, block); // SE plaza

        drawBuildingBlock(g, pad, pad, block - 2 * pad, block - 2 * pad);                                  // NW building
        drawBuildingBlock(g, width - block + pad, pad, block - 2 * pad, block - 2 * pad);                   // NE building
        drawBuildingBlock(g, pad, height - block + pad, block - 2 * pad, block - 2 * pad);                  // SW building
        drawBuildingBlock(g, width - block + pad, height - block + pad, block - 2 * pad, block - 2 * pad);  // SE building

        // sidewalks (draw before roads)
        int roadW = 240;
        int sidewalkW = 20;
        Color sidewalkColor = new Color(180, 180, 185);
        g.setColor(sidewalkColor);
        
        // Vertical road sidewalks
        g.fillRect(centerX - roadW / 2 - sidewalkW, 0, sidewalkW, height); // left sidewalk
        g.fillRect(centerX + roadW / 2, 0, sidewalkW, height); // right sidewalk
        
        // Horizontal road sidewalks
        g.fillRect(0, centerY - roadW / 2 - sidewalkW, width, sidewalkW); // top sidewalk
        g.fillRect(0, centerY + roadW / 2, width, sidewalkW); // bottom sidewalk
        
        // Add sidewalk tiles/texture
        g.setColor(new Color(160, 160, 165, 100));
        g.setStroke(new BasicStroke(1f));
        // Vertical sidewalk tiles
        for (int y = 0; y < height; y += 30) {
            g.drawLine(centerX - roadW / 2 - sidewalkW, y, centerX - roadW / 2, y);
            g.drawLine(centerX + roadW / 2, y, centerX + roadW / 2 + sidewalkW, y);
        }
        // Horizontal sidewalk tiles
        for (int x = 0; x < width; x += 30) {
            g.drawLine(x, centerY - roadW / 2 - sidewalkW, x, centerY - roadW / 2);
            g.drawLine(x, centerY + roadW / 2, x, centerY + roadW / 2 + sidewalkW);
        }
        
        // roads
        int laneOffset = 50;
        int stopRange = 160;
        g.setColor(new Color(50, 50, 50));
        g.fillRect(centerX - roadW / 2, 0, roadW, height);
        g.fillRect(0, centerY - roadW / 2, width, roadW);

        // center lines
        g.setColor(new Color(255, 204, 0));
        g.setStroke(new BasicStroke(4f));
        g.drawLine(centerX, 0, centerX, centerY - stopRange);
        g.drawLine(centerX, centerY + stopRange, centerX, height);
        g.drawLine(0, centerY, centerX - stopRange, centerY);
        g.drawLine(centerX + stopRange, centerY, width, centerY);

        // dashed dividers
        g.setColor(Color.WHITE);
        float[] dashPattern = {25f, 25f};
        g.setStroke(new BasicStroke(5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, dashPattern, 0f));
        for (int y = 0; y < centerY - stopRange; y += 50) {
            g.drawLine(centerX - laneOffset, y, centerX - laneOffset, y + 25);
            g.drawLine(centerX + laneOffset, y, centerX + laneOffset, y + 25);
        }
        for (int y = centerY + stopRange; y < height; y += 50) {
            g.drawLine(centerX - laneOffset, y, centerX - laneOffset, y + 25);
            g.drawLine(centerX + laneOffset, y, centerX + laneOffset, y + 25);
        }
        for (int x = 0; x < centerX - stopRange; x += 50) {
            g.drawLine(x, centerY - laneOffset, x + 25, centerY - laneOffset);
            g.drawLine(x, centerY + laneOffset, x + 25, centerY + laneOffset);
        }
        for (int x = centerX + stopRange; x < width; x += 50) {
            g.drawLine(x, centerY - laneOffset, x + 25, centerY - laneOffset);
            g.drawLine(x, centerY + laneOffset, x + 25, centerY + laneOffset);
        }

        // crosswalks
        g.setStroke(new BasicStroke(10f));
        g.drawLine(centerX - 100, centerY - 120, centerX + 100, centerY - 120);
        g.drawLine(centerX - 100, centerY + 120, centerX + 100, centerY + 120);
        g.drawLine(centerX - 120, centerY - 100, centerX - 120, centerY + 100);
        g.drawLine(centerX + 120, centerY - 100, centerX + 120, centerY + 100);
        
        // Add zebra stripes to crosswalks
        g.setColor(Color.WHITE);
        g.setStroke(new BasicStroke(8f));
        for (int i = -90; i <= 90; i += 20) {
            g.drawLine(centerX + i, centerY - 125, centerX + i, centerY - 115);
            g.drawLine(centerX + i, centerY + 115, centerX + i, centerY + 125);
        }
        for (int i = -90; i <= 90; i += 20) {
            g.drawLine(centerX - 125, centerY + i, centerX - 115, centerY + i);
            g.drawLine(centerX + 115, centerY + i, centerX + 125, centerY + i);
        }
        
        // sidewalk curbs at corners
        g.setColor(new Color(140, 140, 145));
        g.setStroke(new BasicStroke(3f));
        int curbDist = 130;
        g.drawRoundRect(centerX - curbDist, centerY - curbDist, 20, 20, 8, 8);
        g.drawRoundRect(centerX + curbDist - 20, centerY - curbDist, 20, 20, 8, 8);
        g.drawRoundRect(centerX - curbDist, centerY + curbDist - 20, 20, 20, 8, 8);
        g.drawRoundRect(centerX + curbDist - 20, centerY + curbDist - 20, 20, 20, 8, 8);

        // stop box
        g.setStroke(new BasicStroke(4f));
        g.setColor(new Color(230, 230, 230, 100));
        g.drawRect(centerX - 120, centerY - 120, 240, 240);

        // lanes (lights + vehicles)
        for (Lane lane : lanes) lane.draw(g);

        g.dispose();
    }

    /** Small helper to draw a building with windows inside a concrete block **/
    private void drawBuildingBlock(Graphics2D g, int x, int y, int w, int h) {
        Color wall = new Color(90, 90, 95);
        Color outline = new Color(70, 70, 74);
        g.setColor(wall);
        g.fillRoundRect(x, y, w, h, 12, 12);
        g.setColor(outline);
        g.setStroke(new BasicStroke(3f));
        g.drawRoundRect(x, y, w, h, 12, 12);

        g.setColor(new Color(235, 235, 180, 210));
        int cols = Math.max(3, w / 40);
        int rows = Math.max(3, h / 40);
        int margin = 12;
        int winW = Math.max(6, (w - 2 * margin) / cols - 6);
        int winH = Math.max(8, (h - 2 * margin) / rows - 6);
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int wx = x + margin + c * (winW + 6);
                int wy = y + margin + r * (winH + 6);
                g.fillRoundRect(wx, wy, winW, winH, 3, 3);
            }
        }
    }
}

/* --- TrafficController & Light --- */
class TrafficController {

    private final int greenSec, yellowSec;
    private final int phaseLength;
    private int phase = 0; // 0 => lanes 1&3 green; 1 => lanes 2&4 green
    private double phaseTimeRemaining;
    private final javax.swing.Timer tick;
    private final java.util.List<Runnable> listeners = new ArrayList<>();

    public TrafficController(int greenSec, int yellowSec) {
        this.greenSec = greenSec;
        this.yellowSec = yellowSec;
        this.phaseLength = greenSec + yellowSec;
        this.phaseTimeRemaining = phaseLength;
        tick = new javax.swing.Timer(200, e -> {
            phaseTimeRemaining -= 0.2;
            if (phaseTimeRemaining <= 0) {
                phase = 1 - phase;
                phaseTimeRemaining = phaseLength;
            }
            notifyListeners();
        });
    }

    public void start() { tick.start(); }
    public void stop() { tick.stop(); }

    public int getPhase() { return phase; }
    public double getPhaseTimeRemaining() { return Math.max(0, phaseTimeRemaining); }
    public int getGreenSec() { return greenSec; }
    public int getYellowSec() { return yellowSec; }
    public int getPhaseLength() { return phaseLength; }

    public void addListener(Runnable r) { listeners.add(r); }
    private void notifyListeners() { for (Runnable r : listeners) r.run(); }
}

/* --- TrafficLight per lane --- */
class TrafficLight {

    enum State { GREEN, YELLOW, RED }
    private State state = State.RED;
    private double countdown = 0.0;
    private final int laneGroup; // 0 or 1
    private final TrafficController ctrl;

    public TrafficLight(int laneGroup, TrafficController ctrl) {
        this.laneGroup = laneGroup;
        this.ctrl = ctrl;
        ctrl.addListener(this::updateFromController);
        updateFromController();
    }

    private void updateFromController() {
        int phase = ctrl.getPhase();
        double t = ctrl.getPhaseTimeRemaining();
        if (phase == laneGroup) {
            if (t > ctrl.getYellowSec()) { state = State.GREEN; countdown = t; }
            else { state = State.YELLOW; countdown = t; }
        } else {
            state = State.RED;
            countdown = t + ctrl.getPhaseLength();
        }
    }

    public State getState() { return state; }
    public int getCountdownSec() { return (int) Math.ceil(countdown); }
    public double getCountdown() { return countdown; }
}

/* --- Lane class --- */
class Lane {

    private final int id;
    private final String name;
    final double startX, startY;
    final double dirX, dirY;
    final double stopCoord; // x or y of stop line (depending on axis)
    private final java.util.List<Car> cars = Collections.synchronizedList(new ArrayList<>());
    private TrafficController controller;
    private TrafficLight light;
    private final Random rng = new Random();

    // motion params - adjusted for higher traffic volume
    private final double maxSpeed = 160.0;  // px/s (slightly faster)
    private final double accel = 280.0;     // px/s^2 (quicker acceleration)
    private final double decel = 650.0;     // px/s^2 (strong braking)

    public Lane(int id, String name, double startX, double startY, double dirX, double dirY, double stopCoord) {
        this.id = id;
        this.name = name;
        this.startX = startX;
        this.startY = startY;
        this.dirX = dirX;
        this.dirY = dirY;
        this.stopCoord = stopCoord;
    }

    public void setController(TrafficController c) {
        this.controller = c;
        int group = (id == 1 || id == 3) ? 0 : 1;
        this.light = new TrafficLight(group, c);
        c.addListener(() -> {});
    }

    public void spawnCarIfSpace() {
        synchronized (cars) {
            Car last = cars.isEmpty() ? null : cars.get(cars.size() - 1);
            if (last == null) {
                cars.add(Car.random(startX, startY, dirX, dirY, maxSpeed, id, rng));
            } else {
                double d = (Math.abs(dirX) > 0) ? Math.abs(last.x - startX) : Math.abs(last.y - startY);
                double minSpacing = last.length + 35; // Reduced spacing for more traffic
                if (d > minSpacing) {
                    cars.add(Car.random(startX, startY, dirX, dirY, maxSpeed, id, rng));
                }
            }
        }
    }

    public void update(double dt) {
        synchronized (cars) {
            for (int i = 0; i < cars.size(); i++) {
                Car car = cars.get(i);

                // distance to stop line using this vehicle's length
                double halfL = car.halfLen();
                double distToStop;
                if (Math.abs(dirX) > 0) {
                    distToStop = (dirX > 0) ? (stopCoord - (car.x + halfL))
                                            : ((car.x - halfL) - stopCoord);
                } else {
                    distToStop = (dirY > 0) ? (stopCoord - (car.y + halfL))
                                            : ((car.y - halfL) - stopCoord);
                }

                // gap to the vehicle ahead (using both vehicles' lengths)
                Car ahead = (i == 0) ? null : cars.get(i - 1);
                double gapToAhead = Double.POSITIVE_INFINITY;
                if (ahead != null) {
                    double aheadHalf = ahead.halfLen();
                    if (Math.abs(dirX) > 0) {
                        double aheadRear = ahead.x - Math.signum(dirX) * aheadHalf;
                        double thisFront = car.x + Math.signum(dirX) * halfL;
                        gapToAhead = (dirX > 0) ? (aheadRear - thisFront) : (thisFront - aheadRear);
                    } else {
                        double aheadRear = ahead.y - Math.signum(dirY) * aheadHalf;
                        double thisFront = car.y + Math.signum(dirY) * halfL;
                        gapToAhead = (dirY > 0) ? (aheadRear - thisFront) : (thisFront - aheadRear);
                    }
                }

                double stoppingDistance = (car.v * car.v) / (2.0 * decel) + 10.0;

                double desiredSpeed;
                boolean pastStop = (distToStop < -30);
                TrafficLight.State state = light.getState();

                // Determine if car has crossed the stop line (committed to intersection)
                boolean crossedStopLine = (distToStop < 0);

                if (pastStop) {
                    // Completely through the intersection
                    desiredSpeed = maxSpeed;
                } else if (crossedStopLine) {
                    // Already committed - MUST continue through intersection
                    desiredSpeed = maxSpeed;
                } else if (state == TrafficLight.State.GREEN) {
                    desiredSpeed = maxSpeed;
                } else if (state == TrafficLight.State.YELLOW) {
                    // If we can stop safely before the line, stop. Otherwise proceed.
                    if (distToStop > stoppingDistance) {
                        desiredSpeed = 0;  // Can stop safely
                    } else {
                        desiredSpeed = maxSpeed;  // Too close to stop, go through
                    }
                } else {
                    // RED light - stop at the line
                    desiredSpeed = 0;
                }

                // Following distance logic
                if (ahead != null && !crossedStopLine) {
                    // Calculate how close we should get based on whether we're stopping or moving
                    double targetGap;
                    
                    if (desiredSpeed == 0 || ahead.v < 5) {
                        // We're stopping OR car ahead is stopped/very slow
                        // Get close but leave small buffer (just vehicle length + small gap)
                        targetGap = 8;  // Very tight spacing when stopped
                    } else {
                        // Normal following - speed-based gap
                        targetGap = Math.max(12, car.v * 0.4);
                    }
                    
                    if (gapToAhead < 2) {
                        // Too close! Emergency slow down
                        desiredSpeed = Math.min(desiredSpeed, ahead.v * 0.3);
                    } else if (gapToAhead < targetGap) {
                        // Within target range - match speed of car ahead
                        desiredSpeed = Math.min(desiredSpeed, ahead.v);
                    } else if (gapToAhead < targetGap * 2) {
                        // A bit far but approaching - gradual slow down
                        desiredSpeed = Math.min(desiredSpeed, maxSpeed * 0.7);
                    }
                    // else: gap is good, maintain desired speed
                }

                if (car.v < desiredSpeed) car.v = Math.min(car.v + accel * dt, desiredSpeed);
                else car.v = Math.max(car.v - decel * dt, desiredSpeed);

                car.x += car.v * dirX * dt;
                car.y += car.v * dirY * dt;

                if (car.x < -200 || car.x > 1200 || car.y < -200 || car.y > 1200) {
                    cars.remove(i);
                    i--;
                }
            }
        }
    }

    public void draw(Graphics2D g) {
        // lane label
        g.setFont(g.getFont().deriveFont(Font.BOLD, 12f));
        g.setColor(Color.WHITE);
        int lx = (int) startX, ly = (int) startY;
        if (Math.abs(dirX) > 0) g.drawString("Lane " + id + " (" + name + ")", lx - 30, ly + (dirY == 0 ? -10 : 0));
        else g.drawString("Lane " + id + " (" + name + ")", lx + 6, ly + 14);

        // traffic light box near corners
        int boxW = 44, boxH = 120;
        int tx = 0, ty = 0;
        int cornerOffset = 130;

        if (id == 1) { tx = (int) (450 + cornerOffset);        ty = (int) (450 - cornerOffset - boxH); }
        else if (id == 3) { tx = (int) (450 - cornerOffset - boxW); ty = (int) (450 + cornerOffset); }
        else if (id == 2) { tx = (int) (450 - cornerOffset - boxW); ty = (int) (450 - cornerOffset - boxH); }
        else if (id == 4) { tx = (int) (450 + cornerOffset);        ty = (int) (450 + cornerOffset); }

        g.setColor(new Color(50, 50, 50, 240));
        g.fillRoundRect(tx, ty, boxW, boxH, 8, 8);
        g.setColor(Color.DARK_GRAY);
        g.setStroke(new BasicStroke(2f));
        g.drawRoundRect(tx, ty, boxW, boxH, 8, 8);

        int cx = tx + boxW / 2;
        int cy = ty + 18;
        Color rcol = (light.getState() == TrafficLight.State.RED) ? Color.RED : new Color(60, 60, 60);
        Color ycol = (light.getState() == TrafficLight.State.YELLOW) ? Color.YELLOW : new Color(60, 60, 60);
        Color gcol = (light.getState() == TrafficLight.State.GREEN) ? Color.GREEN : new Color(60, 60, 60);
        g.setColor(rcol); g.fillOval(cx - 12, cy - 6, 24, 24);
        g.setColor(ycol); g.fillOval(cx - 12, cy + 22, 24, 24);
        g.setColor(gcol); g.fillOval(cx - 12, cy + 50, 24, 24);

        g.setColor(Color.WHITE);
        g.setFont(g.getFont().deriveFont(Font.BOLD, 13f));
        g.drawString(light.getCountdownSec() + "s", tx + boxW / 2 - 10, ty + boxH + 16);

        // stop line (draw thicker and more visible)
        g.setColor(Color.WHITE);
        g.setStroke(new BasicStroke(5f));
        if (Math.abs(dirX) > 0) {
            int yLine = (int) (stopCoordY());
            g.drawLine((int) stopCoord, yLine - 50, (int) stopCoord, yLine + 50);
        } else {
            int xLine = (int) (stopCoordX());
            g.drawLine(xLine - 50, (int) stopCoord, xLine + 50, (int) stopCoord);
        }

        // vehicles
        synchronized (cars) {
            for (Car c : cars) c.draw(g);
        }
    }

    private double stopCoordX() { return (Math.abs(dirX) > 0) ? stopCoord : (startX); }
    private double stopCoordY() { return (Math.abs(dirY) > 0) ? stopCoord : (startY); }
}

/* --- Vehicle --- */
class Car {

    enum Kind { TRUCK, TAXI, SEDAN, SPORTS }

    double x, y;
    double v = 0;
    final double dx, dy;
    final double maxSpeed;
    final int laneId;

    final Kind kind;
    final Color color;   // base/body color

    // geometry (length along travel axis and width across)
    final double length;
    final double width;

    private Car(double x, double y, double dx, double dy, double maxSpeed, int laneId,
                Kind kind, Color color, double length, double width) {
        this.x = x; this.y = y;
        this.dx = dx; this.dy = dy;
        this.maxSpeed = maxSpeed;
        this.laneId = laneId;
        this.kind = kind;
        this.color = color;
        this.length = length;
        this.width = width;
    }

    /** Factory that randomly picks a vehicle type and reasonable color **/
    public static Car random(double x, double y, double dx, double dy, double maxSpeed, int laneId, Random rng) {
        int pick = rng.nextInt(4); // 0..3
        switch (pick) {
            case 0: // TRUCK
                return new Car(x, y, dx, dy, maxSpeed, laneId,
                        Kind.TRUCK,
                        new Color(90 + rng.nextInt(120), 90 + rng.nextInt(120), 90 + rng.nextInt(120)),
                        58, 22);
            case 1: // TAXI
                return new Car(x, y, dx, dy, maxSpeed, laneId,
                        Kind.TAXI, new Color(255, 210, 0), 44, 20);
            case 2: // SEDAN
                return new Car(x, y, dx, dy, maxSpeed, laneId,
                        Kind.SEDAN, randomNice(rng), 42, 20);
            default: // SPORTS
                return new Car(x, y, dx, dy, maxSpeed, laneId,
                        Kind.SPORTS, randomBold(rng), 38, 18);
        }
    }

    // helpers for colors
    private static Color randomNice(Random r) {
        int[] bases = {90, 110, 130, 150};
        return new Color(bases[r.nextInt(bases.length)] + r.nextInt(80),
                         bases[r.nextInt(bases.length)] + r.nextInt(80),
                         bases[r.nextInt(bases.length)] + r.nextInt(80));
    }
    private static Color randomBold(Random r) {
        Color[] palette = {
            new Color(220, 60, 50),  // red
            new Color(40, 120, 240), // blue
            new Color(245, 130, 30), // orange
            new Color(30, 200, 140)  // teal
        };
        return palette[r.nextInt(palette.length)];
    }

    public double halfLen() { return length / 2.0; }

    public void draw(Graphics2D g) {
        AffineTransform old = g.getTransform();
        g.translate(x, y);
        double angle = 0;
        if (dx == 1 && dy == 0) angle = 0;
        else if (dx == -1 && dy == 0) angle = Math.PI;
        else if (dx == 0 && dy == 1) angle = Math.PI / 2;
        else if (dx == 0 && dy == -1) angle = -Math.PI / 2;
        g.rotate(angle);

        switch (kind) {
            case TRUCK -> drawTruck(g);
            case TAXI -> drawTaxi(g);
            case SEDAN -> drawSedan(g);
            case SPORTS -> drawSports(g);
        }

        g.setTransform(old);
    }

    /* ---- Drawers ---- */

    private void drawTruck(Graphics2D g) {
        int L = (int) Math.round(length);
        int W = (int) Math.round(width);
        int trailerL = (int) Math.round(L * 0.62);
        int cabL = L - trailerL;

        int trailerX = -L / 2;
        int cabX = trailerX + trailerL;

        Color trailer = color;
        Color cab = brighten(color, 0.18);
        Color window = new Color(200, 220, 255, 220);

        g.setColor(trailer);
        g.fillRoundRect(trailerX, -W / 2, trailerL, W, 6, 6);

        g.setColor(cab);
        g.fillRoundRect(cabX, -W / 2, cabL, W, 6, 6);

        g.setColor(window);
        int winW = Math.max(6, (int) (cabL * 0.5));
        int winH = Math.max(6, (int) (W * 0.45));
        g.fillRoundRect(cabX + Math.max(2, (int) (cabL * 0.12)), -W / 2 + 3, winW, winH, 4, 4);

        g.setColor(Color.BLACK);
        int wheelW = 6, wheelH = 4;
        g.fillRect(trailerX + (int) (trailerL * 0.25), W / 2 - 2, wheelW, wheelH);
        g.fillRect(trailerX + (int) (trailerL * 0.25), -W / 2 - wheelH + 2, wheelW, wheelH);
        g.fillRect(trailerX + (int) (trailerL * 0.6), W / 2 - 2, wheelW, wheelH);
        g.fillRect(trailerX + (int) (trailerL * 0.6), -W / 2 - wheelH + 2, wheelW, wheelH);
        g.fillRect(cabX + (int) (cabL * 0.65), W / 2 - 2, wheelW, wheelH);
        g.fillRect(cabX + (int) (cabL * 0.65), -W / 2 - wheelH + 2, wheelW, wheelH);
    }

    private void drawSedan(Graphics2D g) {
        int L = (int) Math.round(length);
        int W = (int) Math.round(width);
        int bodyX = -L / 2;

        // body
        g.setColor(color);
        g.fillRoundRect(bodyX, -W / 2, L, W, 10, 10);

        // roof
        g.setColor(brighten(color, 0.22));
        int roofL = (int) (L * 0.55);
        g.fillRoundRect(bodyX + (int) (L * 0.22), - (int)(W * 0.45), roofL, (int) (W * 0.9), 8, 8);

        // windows
        g.setColor(new Color(200, 220, 255, 220));
        g.fillRoundRect(bodyX + (int) (L * 0.28), - (int)(W * 0.35), (int) (roofL * 0.75), (int) (W * 0.7), 6, 6);

        // wheels
        g.setColor(Color.BLACK);
        int w = 5, h = 4;
        g.fillRect(bodyX + (int) (L * 0.2),  W/2 - 2, w, h);
        g.fillRect(bodyX + (int) (L * 0.2), -W/2 - h + 2, w, h);
        g.fillRect(bodyX + (int) (L * 0.75), W/2 - 2, w, h);
        g.fillRect(bodyX + (int) (L * 0.75),-W/2 - h + 2, w, h);
    }

    private void drawTaxi(Graphics2D g) {
        int L = (int) Math.round(length);
        int W = (int) Math.round(width);
        int bodyX = -L / 2;

        // body (yellow)
        g.setColor(color);
        g.fillRoundRect(bodyX, -W / 2, L, W, 10, 10);

        // checker stripe
        g.setColor(Color.BLACK);
        int stripeY = -2;
        g.fillRect(bodyX + 3, stripeY, L - 6, 4);
        g.setColor(Color.WHITE);
        for (int i = bodyX + 6; i < bodyX + L - 6; i += 12) {
            g.fillRect(i, stripeY, 6, 4);
        }

        // roof sign
        g.setColor(Color.WHITE);
        g.fillRoundRect(bodyX + (int) (L * 0.42), -W / 2 - 6, (int) (L * 0.16), 6, 3, 3);

        // windows
        g.setColor(new Color(190, 210, 240, 230));
        g.fillRoundRect(bodyX + (int) (L * 0.22), - (int)(W * 0.34), (int) (L * 0.56), (int) (W * 0.68), 6, 6);

        // wheels
        g.setColor(Color.BLACK);
        int w = 5, h = 4;
        g.fillRect(bodyX + (int) (L * 0.18),  W/2 - 2, w, h);
        g.fillRect(bodyX + (int) (L * 0.18), -W/2 - h + 2, w, h);
        g.fillRect(bodyX + (int) (L * 0.74),  W/2 - 2, w, h);
        g.fillRect(bodyX + (int) (L * 0.74), -W/2 - h + 2, w, h);
    }

    private void drawSports(Graphics2D g) {
        int L = (int) Math.round(length);
        int W = (int) Math.round(width);
        int bodyX = -L / 2;

        // low body
        g.setColor(color);
        g.fillRoundRect(bodyX, -W / 2, L, W, 14, 14);

        // racing stripe
        g.setColor(Color.WHITE);
        g.fillRect(bodyX + (int) (L * 0.1), -2, (int) (L * 0.8), 4);

        // canopy / windshield
        g.setColor(new Color(180, 200, 235, 230));
        g.fillRoundRect(bodyX + (int) (L * 0.35), - (int)(W * 0.32), (int) (L * 0.3), (int) (W * 0.64), 8, 8);

        // little spoiler
        g.setColor(darken(color, 0.25));
        g.fillRoundRect(bodyX + (int) (L * 0.07), -W/2 - 2, (int) (L * 0.1), 4, 2, 2);

        // wheels (slightly wider)
        g.setColor(Color.BLACK);
        int w = 6, h = 4;
        g.fillRect(bodyX + (int) (L * 0.18),  W/2 - 2, w, h);
        g.fillRect(bodyX + (int) (L * 0.18), -W/2 - h + 2, w, h);
        g.fillRect(bodyX + (int) (L * 0.78),  W/2 - 2, w, h);
        g.fillRect(bodyX + (int) (L * 0.78), -W/2 - h + 2, w, h);
    }

    /* small color helpers */
    private static Color brighten(Color base, double by) {
        int r = (int) Math.min(255, base.getRed() + 255 * by);
        int g = (int) Math.min(255, base.getGreen() + 255 * by);
        int b = (int) Math.min(255, base.getBlue() + 255 * by);
        return new Color(r, g, b);
    }
    private static Color darken(Color base, double by) {
        int r = (int) Math.max(0, base.getRed() * (1.0 - by));
        int g = (int) Math.max(0, base.getGreen() * (1.0 - by));
        int b = (int) Math.max(0, base.getBlue() * (1.0 - by));
        return new Color(r, g, b);
    }
}