package finalproject_alvear;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import javax.sound.sampled.*; 
import javax.swing.*;

public class Finalproject_alvear extends JPanel implements ActionListener {

    private Timer timer;
    private Random random = new Random();

    // --- MAIN CHARACTER VARIABLES ---
    private double characterX = 100;
    private double characterY = 290; // Ground level
    private int characterDirection = 1; // 1 for right, -1 for left
    
    // Jump Physics for Main Character
    private double charVelocityY = 0;
    private double gravity = 1.5;
    private boolean isCharJumping = false;

    // --- BIRD VARIABLES ---
    private double birdX = -50;
    private double birdY = 50;
    private boolean wingFlap = false;
    private int flapCounter = 0;

    // --- DRAGON VARIABLES ---
    private double dragonX = -150;
    private double dragonY = 80;
    private int dragonWingCounter = 0;
    private boolean dragonWingUp = false;

    // --- LISTS ---
    private ArrayList<MusicNote> musicNotes;
    private ArrayList<Person> people;

    // Original dimensions for responsive scaling
    private final int ORIGINAL_WIDTH = 345;
    private final int ORIGINAL_HEIGHT = 356;

    public Finalproject_alvear() {
        this.setPreferredSize(new Dimension(ORIGINAL_WIDTH * 2, ORIGINAL_HEIGHT * 2));
        
        musicNotes = new ArrayList<>();
        people = new ArrayList<>();

        // Start Background Music
        playMusic("C:/Users/rog/Documents/NetBeansProjects/finalproject_alvear/src/finalproject_alvear/medieval_music.wav"); 

        // Initialize and start the animation timer (30 FPS)
        timer = new Timer(30, this);
        timer.start();
    }

    public void playMusic(String filepath) {
        try {
            File musicPath = new File(filepath);
            if (musicPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                System.out.println("Can't find file: " + filepath);
            }
        } catch (Exception ex) {
            System.out.println("Error playing sound: " + ex.getMessage());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // --- RESPONSIVE SCALING ---
        double scaleX = (double) getWidth() / ORIGINAL_WIDTH;
        double scaleY = (double) getHeight() / ORIGINAL_HEIGHT;
        g2.scale(scaleX, scaleY);
        
        // 1. Draw Background (Sky, Hills, Castles, Floor)
        drawBackground(g2);
        
        // 2. Draw Dragon (Behind people)
        drawDragon(g2);

        // 3. Draw Background People (Walking on grass)
        drawPeople(g2);

        // 4. Draw Bird
        drawFlyingBird(g2);
        
        // 5. Draw Main Character (Scaled Down)
        drawMainCharacterScaled(g2);

        // 6. Draw Music Notes
        drawMusicNotes(g2);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        updateMainCharacter();
        updateBird();
        updateDragon();
        updatePeople();
        updateMusicNotes();
        repaint();
    }

    // ================== UPDATE LOGIC ==================

    private void updateMainCharacter() {
        // 1. Movement Logic
        characterX += characterDirection * 2.5; // Increased speed slightly for better flow
        
        // Bounce off walls
        if (characterX > 280) {
            characterDirection = -1;
        } else if (characterX < 40) {
            characterDirection = 1;
        }

        // 2. Dodging Logic (Jump over people)
        if (!isCharJumping) {
            for (Person p : people) {
                double distance = p.x - characterX;

                // Check direction: Is the person IN FRONT of us?
                boolean personIsAhead = false;
                if (characterDirection == 1 && distance > 0) personIsAhead = true;   // Moving Right, person is to the right
                if (characterDirection == -1 && distance < 0) personIsAhead = true;  // Moving Left, person is to the left

                // If person is ahead and close (within 70 pixels), JUMP!
                if (personIsAhead && Math.abs(distance) < 70) {
                    charVelocityY = -20; // Jump power (slightly higher to clear them)
                    isCharJumping = true;
                    break; // Stop checking, we are already jumping
                }
            }
        }

        // 3. Gravity Physics
        if (isCharJumping) {
            characterY += charVelocityY;
            charVelocityY += gravity;

            // Landing logic
            if (characterY >= 290) { 
                characterY = 290;
                isCharJumping = false;
                charVelocityY = 0;
            }
        }
    }

    private void updateBird() {
        birdX += 2.5;
        if (birdX > ORIGINAL_WIDTH + 50) {
            birdX = -50;
        }
        flapCounter++;
        if (flapCounter % 10 == 0) wingFlap = !wingFlap;
    }

    private void updateDragon() {
        dragonX += 1.2;
        if (dragonX > ORIGINAL_WIDTH + 150) {
            dragonX = -150;
            dragonY = 40 + Math.random() * 80;
        }
        dragonWingCounter++;
        if (dragonWingCounter % 15 == 0) dragonWingUp = !dragonWingUp;
    }

    private void updatePeople() {
        // Spawn new person randomly
        if (people.size() < 4 && Math.random() < 0.01) {
            people.add(new Person());
        }

        Iterator<Person> it = people.iterator();
        while (it.hasNext()) {
            Person p = it.next();
            p.update();
            // Remove if out of bounds
            if (p.x > ORIGINAL_WIDTH + 50 || p.x < -50) {
                it.remove();
            }
        }
    }

    private void updateMusicNotes() {
        if (Math.random() < 0.05) { 
            double headX = characterX + (characterDirection == 1 ? 20 : -20);
            double headY = characterY - 90; 
            musicNotes.add(new MusicNote(headX, headY));
        }

        Iterator<MusicNote> it = musicNotes.iterator();
        while (it.hasNext()) {
            MusicNote note = it.next();
            note.update();
            if (!note.isAlive()) {
                it.remove();
            }
        }
    }

    // ================== DRAWING HELPERS ==================

    private void drawMainCharacterScaled(Graphics2D g2) {
        AffineTransform oldTransform = g2.getTransform();
        
        g2.translate(characterX, characterY);
        g2.scale(0.4, 0.4);
        g2.translate(-175, -330); // Offset to center feet at 0,0

        if (characterDirection == -1) {
            g2.translate(175, 0); 
            g2.scale(-1, 1);      
            g2.translate(-175, 0); 
        }
        
        drawCharacter(g2); 
        
        g2.setTransform(oldTransform);
    }

    private void drawPeople(Graphics2D g2) {
        for (Person p : people) {
            p.draw(g2);
        }
    }

    private void drawDragon(Graphics2D g2) {
        AffineTransform old = g2.getTransform();
        g2.translate(dragonX, dragonY);
        g2.setColor(new Color(139, 0, 0)); 
        
        GeneralPath dragonBody = new GeneralPath();
        dragonBody.moveTo(0, 0);
        dragonBody.curveTo(10, -5, 30, 15, 50, 10);
        dragonBody.curveTo(60, 5, 70, -10, 80, -5);
        dragonBody.lineTo(85, 0);
        
        g2.setStroke(new BasicStroke(6, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.draw(dragonBody);
        g2.fill(new Ellipse2D.Double(80, -5, 12, 8)); 
        
        GeneralPath wings = new GeneralPath();
        g2.setColor(new Color(100, 0, 0));
        if (dragonWingUp) {
            wings.moveTo(40, 5); wings.lineTo(60, -20); wings.lineTo(70, 5);
        } else {
            wings.moveTo(40, 5); wings.lineTo(60, 25); wings.lineTo(70, 5);
        }
        g2.fill(wings);
        g2.setTransform(old);
    }

    private void drawCastle(Graphics2D g2, int x, int y) {
        g2.setColor(new Color(105, 105, 105));
        g2.fill(new Rectangle2D.Double(x, y, 40, 60));
        g2.setColor(new Color(80, 80, 80));
        g2.fill(new Rectangle2D.Double(x - 15, y + 20, 15, 40));
        g2.fill(new Rectangle2D.Double(x + 40, y + 20, 15, 40));
        g2.setColor(new Color(50, 50, 60));
        Polygon mainRoof = new Polygon();
        mainRoof.addPoint(x - 5, y); mainRoof.addPoint(x + 45, y); mainRoof.addPoint(x + 20, y - 25);
        g2.fill(mainRoof);
        Polygon leftRoof = new Polygon();
        leftRoof.addPoint(x - 18, y + 20); leftRoof.addPoint(x + 3, y + 20); leftRoof.addPoint(x - 7, y + 5);
        g2.fill(leftRoof);
        Polygon rightRoof = new Polygon();
        rightRoof.addPoint(x + 37, y + 20); rightRoof.addPoint(x + 58, y + 20); rightRoof.addPoint(x + 47, y + 5);
        g2.fill(rightRoof);
        g2.setColor(new Color(30, 30, 30));
        g2.fill(new Arc2D.Double(x + 10, y + 40, 20, 20, 0, 180, Arc2D.CHORD));
        g2.setColor(Color.BLACK);
        g2.fillRect(x + 15, y + 15, 10, 15);
    }

    private void drawMusicNotes(Graphics2D g2) {
        for (MusicNote note : musicNotes) {
            note.draw(g2);
        }
    }
    
    private void drawFlyingBird(Graphics2D g2) {
        AffineTransform old = g2.getTransform();
        g2.translate(birdX, birdY);
        g2.setColor(Color.BLACK);
        GeneralPath bird = new GeneralPath();
        if (wingFlap) { 
            bird.moveTo(0, 10); bird.quadTo(10, 0, 20, 10); bird.quadTo(30, 0, 40, 10);
        } else { 
            bird.moveTo(0, 5); bird.quadTo(10, 15, 20, 5); bird.quadTo(30, 15, 40, 5);
        }
        g2.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.draw(bird);
        g2.setTransform(old);
    }
    
    private void drawBackground(Graphics2D g2) {
        GradientPaint sky = new GradientPaint(0, 0, new Color(70, 130, 180), 0, 300, new Color(135, 206, 235));
        g2.setPaint(sky);
        g2.fill(new Rectangle2D.Double(0, 0, ORIGINAL_WIDTH, ORIGINAL_HEIGHT));

        g2.setColor(new Color(34, 139, 34)); 
        g2.fill(new Arc2D.Double(-100, 200, 400, 200, 0, 180, Arc2D.CHORD));
        g2.setColor(new Color(0, 100, 0));   
        g2.fill(new Arc2D.Double(150, 220, 300, 200, 0, 180, Arc2D.CHORD));

        drawCastle(g2, 50, 190);
        AffineTransform old = g2.getTransform();
        g2.translate(280, 210);
        g2.scale(0.7, 0.7);
        drawCastle(g2, 0, 0);
        g2.setTransform(old);

        g2.setColor(new Color(119, 136, 153)); 
        g2.fill(new Rectangle2D.Double(0, 290, ORIGINAL_WIDTH, 66));

        g2.setColor(new Color(47, 79, 79)); 
        g2.setStroke(new BasicStroke(1));
        for (int y = 290; y < ORIGINAL_HEIGHT; y += 20) {
            int offset = (y % 40 == 0) ? 0 : 25;
            for (int x = -25; x < ORIGINAL_WIDTH; x += 50) {
                g2.draw(new Rectangle2D.Double(x + offset, y, 50, 20));
            }
        }

        g2.setColor(new Color(139, 0, 0)); 
        GeneralPath banner = new GeneralPath();
        banner.moveTo(30, 0); banner.lineTo(30, 120); banner.lineTo(55, 100); banner.lineTo(80, 120); banner.lineTo(80, 0);
        banner.closePath();
        g2.fill(banner);
        g2.setColor(new Color(218, 165, 32));
        g2.fill(new Rectangle2D.Double(30, 0, 50, 10)); 
        g2.draw(banner);
    }
    
    // --- INNER CLASS: BACKGROUND PERSON ---
    class Person {
        double x, y, startY;
        double speed;
        Color shirtColor;
        boolean jumping = false;
        double velY = 0;

        public Person() {
            if (random.nextBoolean()) {
                x = -20;
                speed = 1.5 + random.nextDouble(); // Slightly faster people
            } else {
                x = ORIGINAL_WIDTH + 20;
                speed = -(1.5 + random.nextDouble());
            }
            startY = 250 + random.nextInt(30); // Keep them closer to ground level
            y = startY;
            shirtColor = new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
        }

        public void update() {
            x += speed;
            if (!jumping && random.nextDouble() < 0.01) { // Occasional hop
                jumping = true;
                velY = -5;
            }
            if (jumping) {
                y += velY;
                velY += 0.5;
                if (y >= startY) {
                    y = startY;
                    jumping = false;
                }
            }
        }

        public void draw(Graphics2D g2) {
            AffineTransform old = g2.getTransform();
            g2.translate(x, y);
            g2.setColor(Color.BLACK); 
            g2.setStroke(new BasicStroke(2));
            if (Math.abs(x % 20) < 10) {
                g2.drawLine(0, 15, -3, 25);
                g2.drawLine(0, 15, 3, 25);
            } else {
                g2.drawLine(0, 15, 0, 25);
            }
            g2.setColor(shirtColor); 
            g2.fill(new Rectangle2D.Double(-4, 5, 8, 12));
            g2.setColor(new Color(255, 220, 177)); 
            g2.fill(new Ellipse2D.Double(-4, -4, 8, 8));
            g2.setTransform(old);
        }
    }

    // --- INNER CLASS: MUSIC NOTE ---
    class MusicNote {
        double x, y;
        double scale = 0.5;
        float alpha = 1.0f;
        Color color;

        public MusicNote(double startX, double startY) {
            this.x = startX;
            this.y = startY;
            Color[] colors = {Color.ORANGE, Color.YELLOW, Color.WHITE, new Color(255, 215, 0)};
            this.color = colors[(int)(Math.random() * colors.length)];
        }

        public void update() {
            y -= 1.5; 
            scale += 0.02; 
            alpha -= 0.015; 
            if (alpha < 0) alpha = 0;
        }

        public boolean isAlive() { return alpha > 0; }

        public void draw(Graphics2D g2) {
            AffineTransform old = g2.getTransform();
            g2.translate(x, y);
            g2.scale(scale, scale);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2.setColor(this.color);
            g2.setStroke(new BasicStroke(3));
            GeneralPath note = new GeneralPath();
            note.moveTo(0, 0); note.lineTo(0, 25);
            note.append(new Ellipse2D.Double(-6, 20, 10, 8), false); 
            note.moveTo(0, 0); note.curveTo(0, 0, 8, 5, 8, 15); 
            g2.draw(note);
            g2.fill(new Ellipse2D.Double(-6, 20, 10, 8)); 
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            g2.setTransform(old);
        }
    }
    private void drawCharacter(Graphics2D g2) {
        // ... PASTE THE HUGE SVG CODE FOR THE CHARACTER HERE ...
        // See previous response for the full content of this method.
        // It begins with "GeneralPath path121 = new GeneralPath();"
        // and ends with "g2.fill(path234);"
        
        // -- RE-INSERT THE SVG CODE FROM THE PREVIOUS ANSWER HERE --
        // (If you need me to paste it again fully, let me know, but it is identical)
        
        // To make this compile for testing purposes, I will add a placeholder shape:
        // REMOVE THIS PLACEHOLDER WHEN PASTING THE REAL CODE
        // g2.setColor(Color.ORANGE);
        // g2.fillRect(70, 20, 180, 320); 
        
        // Element 121
        GeneralPath path121 = new GeneralPath();
        path121.moveTo(72d, 138d);
        path121.lineTo(73d, 95d);
        path121.curveTo(73d, 93d, 78d, 94d, 79d, 94d);
        path121.lineTo(79d, 88d);
        path121.curveTo(86d, 88d, 86d, 88d, 85d, 81d);
        path121.curveTo(87d, 81d, 91d, 82d, 92d, 81d);
        path121.lineTo(92d, 50d);
        path121.curveTo(94d, 49d, 96d, 49d, 98d, 49d);
        path121.lineTo(98d, 37d);
        path121.curveTo(98d, 36d, 103d, 37d, 104d, 36d);
        path121.curveTo(105d, 35d, 104d, 31d, 105d, 30d);
        path121.curveTo(106d, 29d, 110d, 31d, 111d, 30d);
        path121.curveTo(112d, 28d, 110d, 23d, 111d, 23d);
        path121.lineTo(123d, 23d);
        path121.curveTo(124d, 23d, 124d, 19d, 124d, 17d);
        path121.lineTo(219d, 17d);
        path121.lineTo(219d, 23d);
        path121.lineTo(232d, 23d);
        path121.curveTo(233d, 23d, 232d, 29d, 232d, 30d);
        path121.curveTo(239d, 30d, 239d, 30d, 239d, 36d);
        path121.lineTo(245d, 36d);
        path121.curveTo(245d, 36d, 245d, 49d, 245d, 49d);
        path121.curveTo(246d, 50d, 251d, 49d, 252d, 50d);
        path121.curveTo(253d, 51d, 250d, 61d, 252d, 62d);
        path121.curveTo(253d, 62d, 257d, 62d, 258d, 62d);
        path121.lineTo(258d, 87d);
        path121.curveTo(259d, 89d, 264d, 88d, 264d, 88d);
        path121.curveTo(265d, 88d, 264d, 93d, 264d, 93d);
        path121.curveTo(265d, 94d, 264d, 95d, 263d, 95d);
        path121.lineTo(240d, 95d);
        path121.lineTo(239d, 94d);
        path121.curveTo(239d, 99d, 238d, 103d, 239d, 107d);
        path121.curveTo(236d, 107d, 234d, 107d, 232d, 106d);
        path121.curveTo(232d, 106d, 232d, 106d, 231d, 106d);
        path121.curveTo(231d, 102d, 231d, 92d, 231d, 89d);
        path121.lineTo(232d, 88d);
        path121.curveTo(231d, 89d, 229d, 89d, 227d, 88d);
        path121.curveTo(227d, 91d, 227d, 93d, 227d, 95d);
        path121.curveTo(225d, 95d, 223d, 96d, 221d, 95d);
        path121.lineTo(221d, 100d);
        path121.lineTo(219d, 100d);
        path121.curveTo(217d, 102d, 216d, 102d, 213d, 100d);
        path121.curveTo(213d, 97d, 213d, 93d, 213d, 89d);
        path121.curveTo(212d, 88d, 214d, 87d, 215d, 87d);
        path121.curveTo(222d, 89d, 221d, 80d, 214d, 82d);
        path121.curveTo(211d, 82d, 212d, 74d, 213d, 74d);
        path121.curveTo(214d, 74d, 214d, 74d, 214d, 75d);
        path121.curveTo(218d, 75d, 222d, 75d, 225d, 75d);
        path121.lineTo(226d, 75d);
        path121.curveTo(226d, 77d, 226d, 79d, 226d, 81d);
        path121.curveTo(227d, 80d, 229d, 80d, 231d, 80d);
        path121.curveTo(231d, 79d, 232d, 77d, 232d, 75d);
        path121.curveTo(230d, 75d, 228d, 75d, 226d, 74d);
        path121.lineTo(226d, 74d);
        path121.curveTo(226d, 68d, 226d, 62d, 226d, 56d);
        path121.curveTo(227d, 55d, 232d, 55d, 232d, 57d);
        path121.curveTo(233d, 61d, 233d, 64d, 233d, 68d);
        path121.lineTo(232d, 68d);
        path121.curveTo(233d, 68d, 233d, 68d, 233d, 68d);
        path121.curveTo(235d, 68d, 237d, 68d, 239d, 69d);
        path121.curveTo(240d, 75d, 240d, 81d, 240d, 88d);
        path121.lineTo(240d, 87d);
        path121.curveTo(244d, 87d, 247d, 87d, 250d, 87d);
        path121.curveTo(251d, 81d, 251d, 75d, 251d, 70d);
        path121.curveTo(248d, 70d, 246d, 70d, 245d, 69d);
        path121.curveTo(244d, 66d, 244d, 63d, 245d, 61d);
        path121.curveTo(246d, 56d, 245d, 56d, 240d, 56d);
        path121.lineTo(239d, 55d);
        path121.lineTo(238d, 55d);
        path121.curveTo(238d, 51d, 238d, 47d, 238d, 44d);
        path121.curveTo(232d, 45d, 231d, 44d, 231d, 38d);
        path121.curveTo(225d, 38d, 224d, 37d, 225d, 31d);
        path121.lineTo(226d, 30d);
        path121.curveTo(226d, 30d, 226d, 31d, 225d, 31d);
        path121.curveTo(224d, 31d, 215d, 31d, 214d, 31d);
        path121.lineTo(213d, 30d);
        path121.lineTo(212d, 29d);
        path121.curveTo(213d, 27d, 213d, 26d, 212d, 24d);
        path121.lineTo(131d, 25d);
        path121.lineTo(130d, 24d);
        path121.curveTo(130d, 26d, 130d, 28d, 130d, 30d);
        path121.curveTo(126d, 30d, 122d, 30d, 117d, 30d);
        path121.lineTo(117d, 36d);
        path121.curveTo(115d, 37d, 113d, 37d, 111d, 37d);
        path121.curveTo(111d, 39d, 111d, 41d, 111d, 43d);
        path121.curveTo(109d, 43d, 107d, 43d, 104d, 43d);
        path121.curveTo(105d, 47d, 105d, 51d, 105d, 55d);
        path121.lineTo(104d, 55d);
        path121.curveTo(104d, 57d, 99d, 57d, 98d, 55d);
        path121.lineTo(99d, 56d);
        path121.lineTo(99d, 81d);
        path121.lineTo(98d, 81d);
        path121.curveTo(98d, 80d, 104d, 80d, 104d, 81d);
        path121.lineTo(103d, 81d);
        path121.lineTo(103d, 57d);
        path121.lineTo(104d, 56d);
        path121.lineTo(105d, 55d);
        path121.curveTo(111d, 56d, 111d, 55d, 111d, 49d);
        path121.lineTo(112d, 49d);
        path121.curveTo(115d, 49d, 117d, 49d, 117d, 47d);
        path121.curveTo(116d, 43d, 118d, 41d, 123d, 42d);
        path121.curveTo(123d, 40d, 123d, 38d, 124d, 37d);
        path121.curveTo(124d, 37d, 124d, 37d, 124d, 36d);
        path121.curveTo(125d, 35d, 126d, 35d, 126d, 36d);
        path121.lineTo(131d, 36d);
        path121.curveTo(155d, 35d, 180d, 35d, 204d, 35d);
        path121.curveTo(207d, 36d, 210d, 36d, 213d, 36d);
        path121.lineTo(214d, 37d);
        path121.lineTo(214d, 42d);
        path121.curveTo(216d, 42d, 217d, 42d, 219d, 42d);
        path121.curveTo(221d, 43d, 221d, 48d, 219d, 49d);
        path121.curveTo(216d, 49d, 211d, 49d, 208d, 49d);
        path121.curveTo(207d, 49d, 207d, 49d, 207d, 48d);
        path121.lineTo(207d, 42d);
        path121.curveTo(207d, 43d, 207d, 43d, 206d, 43d);
        path121.lineTo(131d, 43d);
        path121.curveTo(130d, 43d, 130d, 43d, 130d, 42d);
        path121.curveTo(131d, 43d, 131d, 43d, 130d, 43d);
        path121.curveTo(131d, 49d, 130d, 50d, 124d, 49d);
        path121.curveTo(124d, 51d, 124d, 53d, 123d, 55d);
        path121.curveTo(124d, 56d, 124d, 56d, 123d, 56d);
        path121.lineTo(118d, 56d);
        path121.curveTo(118d, 59d, 118d, 61d, 118d, 63d);
        path121.curveTo(116d, 63d, 114d, 63d, 112d, 63d);
        path121.lineTo(112d, 74d);
        path121.curveTo(113d, 74d, 113d, 74d, 113d, 75d);
        path121.curveTo(115d, 75d, 117d, 74d, 119d, 75d);
        path121.curveTo(121d, 75d, 122d, 75d, 124d, 74d);
        path121.curveTo(124d, 74d, 124d, 81d, 123d, 81d);
        path121.curveTo(118d, 80d, 116d, 82d, 117d, 87d);
        path121.lineTo(117d, 87d);
        path121.curveTo(113d, 87d, 111d, 88d, 111d, 90d);
        path121.lineTo(111d, 94d);
        path121.curveTo(109d, 94d, 106d, 94d, 104d, 94d);
        path121.lineTo(103d, 93d);
        path121.curveTo(104d, 92d, 104d, 90d, 103d, 89d);
        path121.curveTo(100d, 88d, 96d, 88d, 93d, 89d);
        path121.curveTo(93d, 91d, 92d, 93d, 92d, 95d);
        path121.curveTo(90d, 94d, 88d, 94d, 85d, 94d);
        path121.curveTo(87d, 95d, 87d, 95d, 85d, 96d);
        path121.curveTo(85d, 98d, 85d, 100d, 85d, 101d);
        path121.curveTo(85d, 102d, 80d, 102d, 79d, 100d);
        path121.lineTo(79d, 101d);
        path121.curveTo(79d, 107d, 79d, 113d, 79d, 118d);
        path121.curveTo(80d, 119d, 80d, 138d, 79d, 139d);
        path121.curveTo(79d, 139d, 74d, 139d, 72d, 138d);
        path121.closePath();
        g2.setColor(new Color(9, 5, 25));
        g2.fill(path121);

        // Element 122
        GeneralPath path122 = new GeneralPath();
        path122.moveTo(220d, 165d);
        path122.lineTo(220d, 164d);
        path122.curveTo(228d, 165d, 237d, 165d, 245d, 165d);
        path122.curveTo(245d, 183d, 246d, 202d, 246d, 221d);
        path122.curveTo(246d, 221d, 245d, 221d, 245d, 221d);
        path122.curveTo(246d, 223d, 242d, 222d, 241d, 222d);
        path122.curveTo(240d, 222d, 239d, 223d, 239d, 224d);
        path122.lineTo(239d, 235d);
        path122.curveTo(237d, 236d, 235d, 236d, 233d, 236d);
        path122.curveTo(233d, 238d, 233d, 240d, 233d, 241d);
        path122.curveTo(228d, 241d, 224d, 241d, 220d, 241d);
        path122.curveTo(219d, 243d, 220d, 245d, 220d, 247d);
        path122.lineTo(220d, 247d);
        path122.curveTo(220d, 248d, 219d, 248d, 219d, 248d);
        path122.curveTo(216d, 249d, 205d, 249d, 201d, 248d);
        path122.lineTo(201d, 247d);
        path122.curveTo(201d, 248d, 201d, 254d, 201d, 254d);
        path122.curveTo(201d, 255d, 200d, 255d, 200d, 254d);
        path122.lineTo(169d, 254d);
        path122.curveTo(170d, 255d, 170d, 261d, 168d, 262d);
        path122.curveTo(165d, 261d, 163d, 262d, 163d, 264d);
        path122.curveTo(163d, 284d, 163d, 304d, 163d, 323d);
        path122.lineTo(168d, 323d);
        path122.curveTo(168d, 323d, 169d, 324d, 169d, 324d);
        path122.lineTo(163d, 325d);
        path122.curveTo(162d, 326d, 163d, 331d, 162d, 331d);
        path122.lineTo(150d, 331d);
        path122.curveTo(149d, 333d, 150d, 336d, 149d, 337d);
        path122.curveTo(147d, 337d, 147d, 332d, 149d, 331d);
        path122.curveTo(149d, 331d, 149d, 332d, 149d, 332d);
        path122.curveTo(145d, 332d, 134d, 333d, 131d, 332d);
        path122.lineTo(131d, 331d);
        path122.curveTo(130d, 332d, 129d, 331d, 130d, 330d);
        path122.curveTo(130d, 330d, 129d, 330d, 129d, 330d);
        path122.curveTo(129d, 326d, 129d, 317d, 129d, 313d);
        path122.lineTo(130d, 312d);
        path122.curveTo(128d, 310d, 134d, 311d, 135d, 311d);
        path122.curveTo(136d, 311d, 136d, 307d, 135d, 307d);
        path122.curveTo(133d, 307d, 131d, 307d, 131d, 305d);
        path122.curveTo(132d, 304d, 135d, 304d, 136d, 305d);
        path122.curveTo(136d, 305d, 136d, 305d, 137d, 305d);
        path122.lineTo(137d, 304d);
        path122.curveTo(139d, 304d, 140d, 305d, 141d, 305d);
        path122.lineTo(142d, 247d);
        path122.curveTo(142d, 245d, 142d, 243d, 143d, 241d);
        path122.lineTo(144d, 240d);
        path122.curveTo(148d, 241d, 152d, 241d, 156d, 241d);
        path122.curveTo(155d, 241d, 155d, 240d, 155d, 240d);
        path122.curveTo(154d, 238d, 154d, 224d, 155d, 223d);
        path122.curveTo(155d, 223d, 155d, 223d, 156d, 223d);
        path122.curveTo(154d, 221d, 161d, 220d, 162d, 222d);
        path122.curveTo(162d, 222d, 163d, 222d, 163d, 222d);
        path122.curveTo(165d, 224d, 158d, 224d, 157d, 224d);
        path122.curveTo(157d, 226d, 157d, 228d, 156d, 228d);
        path122.lineTo(156d, 227d);
        path122.lineTo(180d, 227d);
        path122.lineTo(181d, 228d);
        path122.curveTo(180d, 227d, 180d, 221d, 182d, 222d);
        path122.curveTo(182d, 222d, 183d, 221d, 183d, 221d);
        path122.curveTo(185d, 221d, 191d, 221d, 193d, 221d);
        path122.lineTo(194d, 222d);
        path122.curveTo(192d, 221d, 192d, 215d, 195d, 215d);
        path122.lineTo(199d, 215d);
        path122.curveTo(199d, 209d, 200d, 207d, 206d, 208d);
        path122.lineTo(206d, 203d);
        path122.curveTo(207d, 201d, 213d, 201d, 214d, 203d);
        path122.curveTo(213d, 203d, 213d, 203d, 213d, 202d);
        path122.curveTo(212d, 201d, 212d, 192d, 213d, 191d);
        path122.lineTo(214d, 190d);
        path122.curveTo(214d, 189d, 216d, 189d, 219d, 189d);
        path122.curveTo(218d, 181d, 218d, 173d, 219d, 166d);
        path122.lineTo(220d, 165d);
        path122.closePath();
        g2.setColor(new Color(182, 102, 74));
        g2.fill(path122);

        // Element 123
        GeneralPath path123 = new GeneralPath();
        path123.moveTo(168d, 254d);
        path123.curveTo(179d, 255d, 190d, 254d, 201d, 254d);
        path123.curveTo(201d, 254d, 201d, 260d, 200d, 261d);
        path123.lineTo(182d, 261d);
        path123.curveTo(183d, 262d, 183d, 264d, 183d, 266d);
        path123.curveTo(188d, 266d, 193d, 266d, 199d, 267d);
        path123.curveTo(201d, 267d, 204d, 267d, 207d, 267d);
        path123.lineTo(207d, 273d);
        path123.curveTo(205d, 274d, 203d, 274d, 201d, 274d);
        path123.lineTo(201d, 274d);
        path123.curveTo(200d, 278d, 201d, 283d, 201d, 286d);
        path123.lineTo(201d, 287d);
        path123.curveTo(201d, 286d, 201d, 286d, 201d, 286d);
        path123.lineTo(207d, 286d);
        path123.curveTo(207d, 286d, 207d, 281d, 207d, 281d);
        path123.curveTo(211d, 280d, 215d, 280d, 219d, 280d);
        path123.lineTo(219d, 286d);
        path123.curveTo(217d, 286d, 214d, 286d, 212d, 286d);
        path123.lineTo(213d, 286d);
        path123.lineTo(212d, 343d);
        path123.curveTo(202d, 343d, 192d, 343d, 182d, 343d);
        path123.curveTo(180d, 342d, 181d, 335d, 182d, 333d);
        path123.curveTo(182d, 332d, 181d, 330d, 181d, 328d);
        path123.curveTo(181d, 323d, 182d, 318d, 182d, 314d);
        path123.curveTo(181d, 313d, 181d, 312d, 181d, 312d);
        path123.lineTo(168d, 312d);
        path123.lineTo(168d, 325d);
        path123.lineTo(162d, 325d);
        path123.curveTo(162d, 309d, 162d, 293d, 162d, 277d);
        path123.curveTo(162d, 273d, 162d, 269d, 162d, 264d);
        path123.curveTo(162d, 259d, 164d, 261d, 169d, 261d);
        path123.lineTo(169d, 255d);
        path123.closePath();
        g2.setColor(new Color(9, 4, 27));
        g2.fill(path123);

        // Element 124
        GeneralPath path124 = new GeneralPath();
        path124.moveTo(143d, 242d);
        path124.curveTo(143d, 254d, 143d, 268d, 142d, 280d);
        path124.curveTo(142d, 288d, 143d, 297d, 142d, 305d);
        path124.curveTo(141d, 306d, 138d, 305d, 136d, 305d);
        path124.curveTo(136d, 297d, 136d, 288d, 136d, 280d);
        path124.lineTo(135d, 281d);
        path124.lineTo(131d, 281d);
        path124.lineTo(131d, 305d);
        path124.lineTo(130d, 305d);
        path124.lineTo(136d, 306d);
        path124.curveTo(136d, 307d, 137d, 312d, 136d, 312d);
        path124.curveTo(135d, 312d, 130d, 311d, 130d, 312d);
        path124.lineTo(130d, 330d);
        path124.curveTo(130d, 330d, 131d, 331d, 131d, 331d);
        path124.lineTo(149d, 331d);
        path124.lineTo(149d, 338d);
        path124.lineTo(124d, 338d);
        path124.lineTo(124d, 325d);
        path124.curveTo(120d, 323d, 115d, 326d, 111d, 325d);
        path124.lineTo(111d, 319d);
        path124.curveTo(111d, 318d, 105d, 318d, 105d, 318d);
        path124.curveTo(104d, 319d, 104d, 319d, 103d, 318d);
        path124.curveTo(103d, 317d, 103d, 312d, 104d, 312d);
        path124.curveTo(105d, 311d, 109d, 311d, 111d, 311d);
        path124.lineTo(110d, 311d);
        path124.lineTo(110d, 255d);
        path124.lineTo(111d, 255d);
        path124.lineTo(112d, 254d);
        path124.curveTo(117d, 254d, 117d, 254d, 117d, 249d);
        path124.lineTo(118d, 248d);
        path124.curveTo(118d, 248d, 118d, 247d, 118d, 247d);
        path124.curveTo(121d, 247d, 133d, 246d, 135d, 247d);
        path124.lineTo(136d, 248d);
        path124.curveTo(135d, 247d, 135d, 241d, 136d, 241d);
        path124.curveTo(138d, 240d, 141d, 240d, 143d, 242d);
        path124.closePath();
        g2.setColor(new Color(10, 4, 27));
        g2.fill(path124);

        // Element 125
        GeneralPath path125 = new GeneralPath();
        path125.moveTo(104d, 216d);
        path125.curveTo(105d, 214d, 110d, 215d, 111d, 216d);
        path125.curveTo(112d, 217d, 113d, 222d, 111d, 222d);
        path125.curveTo(111d, 222d, 111d, 221d, 112d, 221d);
        path125.curveTo(113d, 221d, 121d, 221d, 123d, 221d);
        path125.lineTo(123d, 222d);
        path125.curveTo(125d, 222d, 125d, 228d, 123d, 228d);
        path125.curveTo(124d, 228d, 124d, 228d, 124d, 228d);
        path125.curveTo(132d, 227d, 141d, 227d, 148d, 228d);
        path125.lineTo(149d, 228d);
        path125.lineTo(148d, 227d);
        path125.curveTo(148d, 225d, 147d, 218d, 148d, 216d);
        path125.curveTo(148d, 215d, 149d, 216d, 149d, 216d);
        path125.curveTo(150d, 216d, 150d, 216d, 150d, 216d);
        path125.curveTo(151d, 219d, 151d, 234d, 150d, 234d);
        path125.curveTo(149d, 236d, 147d, 237d, 144d, 236d);
        path125.curveTo(144d, 238d, 144d, 240d, 143d, 241d);
        path125.curveTo(141d, 242d, 139d, 241d, 137d, 241d);
        path125.curveTo(136d, 243d, 137d, 248d, 136d, 248d);
        path125.lineTo(118d, 248d);
        path125.curveTo(118d, 255d, 118d, 255d, 111d, 254d);
        path125.lineTo(111d, 311d);
        path125.curveTo(111d, 313d, 105d, 311d, 104d, 312d);
        path125.lineTo(104d, 318d);
        path125.curveTo(104d, 318d, 111d, 318d, 111d, 318d);
        path125.lineTo(111d, 324d);
        path125.curveTo(111d, 324d, 111d, 318d, 111d, 318d);
        path125.curveTo(110d, 319d, 105d, 318d, 105d, 319d);
        path125.curveTo(105d, 319d, 105d, 320d, 105d, 320d);
        path125.curveTo(104d, 320d, 103d, 319d, 102d, 318d);
        path125.curveTo(102d, 319d, 102d, 319d, 102d, 319d);
        path125.curveTo(95d, 320d, 86d, 320d, 80d, 319d);
        path125.lineTo(79d, 318d);
        path125.curveTo(77d, 320d, 78d, 315d, 78d, 314d);
        path125.lineTo(77d, 308d);
        path125.curveTo(77d, 304d, 79d, 303d, 84d, 304d);
        path125.curveTo(83d, 299d, 85d, 297d, 90d, 298d);
        path125.lineTo(91d, 299d);
        path125.curveTo(91d, 299d, 91d, 299d, 92d, 299d);
        path125.curveTo(91d, 301d, 90d, 301d, 87d, 300d);
        path125.curveTo(88d, 305d, 86d, 308d, 81d, 307d);
        path125.curveTo(81d, 309d, 81d, 311d, 80d, 312d);
        path125.lineTo(80d, 311d);
        path125.lineTo(91d, 311d);
        path125.lineTo(91d, 312d);
        path125.lineTo(91d, 307d);
        path125.curveTo(91d, 305d, 93d, 304d, 97d, 304d);
        path125.curveTo(97d, 298d, 98d, 297d, 104d, 298d);
        path125.lineTo(104d, 299d);
        path125.lineTo(103d, 299d);
        path125.lineTo(103d, 249d);
        path125.lineTo(104d, 249d);
        path125.curveTo(103d, 247d, 105d, 247d, 106d, 247d);
        path125.curveTo(110d, 248d, 111d, 246d, 110d, 242d);
        path125.curveTo(111d, 240d, 113d, 240d, 116d, 241d);
        path125.curveTo(118d, 240d, 117d, 233d, 112d, 236d);
        path125.lineTo(111d, 235d);
        path125.curveTo(108d, 236d, 105d, 236d, 101d, 236d);
        path125.curveTo(100d, 236d, 99d, 236d, 98d, 235d);
        path125.curveTo(98d, 235d, 98d, 235d, 98d, 234d);
        path125.curveTo(100d, 235d, 111d, 235d, 111d, 234d);
        path125.curveTo(111d, 234d, 111d, 229d, 111d, 229d);
        path125.curveTo(109d, 229d, 107d, 229d, 105d, 228d);
        path125.lineTo(104d, 228d);
        path125.curveTo(103d, 224d, 103d, 220d, 103d, 217d);
        path125.lineTo(104d, 216d);
        path125.curveTo(104d, 216d, 104d, 216d, 104d, 215d);
        path125.closePath();
        g2.setColor(new Color(180, 103, 73));
        g2.fill(path125);

        // Element 126
        GeneralPath path126 = new GeneralPath();
        path126.moveTo(239d, 107d);
        path126.curveTo(241d, 106d, 242d, 106d, 245d, 106d);
        path126.lineTo(245d, 102d);
        path126.curveTo(245d, 101d, 245d, 100d, 246d, 100d);
        path126.curveTo(246d, 100d, 246d, 100d, 247d, 100d);
        path126.curveTo(249d, 99d, 263d, 99d, 264d, 100d);
        path126.lineTo(264d, 100d);
        path126.curveTo(263d, 100d, 263d, 95d, 264d, 94d);
        path126.curveTo(264d, 94d, 270d, 94d, 271d, 95d);
        path126.curveTo(271d, 97d, 271d, 99d, 272d, 100d);
        path126.curveTo(273d, 101d, 275d, 100d, 277d, 100d);
        path126.curveTo(278d, 104d, 277d, 108d, 277d, 112d);
        path126.curveTo(277d, 123d, 277d, 134d, 277d, 145d);
        path126.curveTo(275d, 145d, 273d, 145d, 271d, 145d);
        path126.lineTo(270d, 145d);
        path126.lineTo(270d, 108d);
        path126.lineTo(271d, 108d);
        path126.curveTo(270d, 108d, 270d, 108d, 270d, 107d);
        path126.curveTo(270d, 107d, 270d, 108d, 270d, 108d);
        path126.curveTo(267d, 108d, 254d, 108d, 252d, 108d);
        path126.lineTo(252d, 107d);
        path126.curveTo(253d, 107d, 253d, 113d, 252d, 114d);
        path126.curveTo(250d, 113d, 246d, 113d, 245d, 114d);
        path126.curveTo(245d, 122d, 245d, 130d, 246d, 138d);
        path126.curveTo(248d, 139d, 250d, 139d, 252d, 139d);
        path126.curveTo(253d, 139d, 253d, 144d, 252d, 146d);
        path126.curveTo(252d, 145d, 252d, 145d, 253d, 144d);
        path126.curveTo(255d, 144d, 269d, 144d, 270d, 144d);
        path126.lineTo(271d, 146d);
        path126.curveTo(271d, 147d, 271d, 150d, 271d, 152d);
        path126.curveTo(269d, 152d, 266d, 151d, 265d, 152d);
        path126.curveTo(264d, 153d, 265d, 157d, 264d, 158d);
        path126.curveTo(262d, 158d, 261d, 158d, 259d, 158d);
        path126.curveTo(258d, 159d, 257d, 157d, 258d, 157d);
        path126.curveTo(258d, 155d, 258d, 153d, 257d, 152d);
        path126.lineTo(257d, 152d);
        path126.lineTo(251d, 152d);
        path126.curveTo(248d, 153d, 246d, 153d, 246d, 152d);
        path126.lineTo(244d, 151d);
        path126.curveTo(245d, 149d, 245d, 147d, 245d, 146d);
        path126.curveTo(243d, 145d, 241d, 145d, 239d, 146d);
        path126.lineTo(239d, 145d);
        path126.curveTo(238d, 140d, 238d, 136d, 239d, 132d);
        path126.curveTo(239d, 124d, 239d, 115d, 239d, 107d);
        path126.closePath();
        g2.setColor(new Color(10, 5, 26));
        g2.fill(path126);

        // Element 127
        GeneralPath path127 = new GeneralPath();
        path127.moveTo(72d, 300d);
        path127.lineTo(78d, 299d);
        path127.curveTo(79d, 297d, 79d, 295d, 79d, 293d);
        path127.lineTo(80d, 293d);
        path127.curveTo(84d, 294d, 86d, 293d, 86d, 291d);
        path127.curveTo(85d, 273d, 85d, 254d, 86d, 236d);
        path127.curveTo(86d, 235d, 86d, 235d, 86d, 235d);
        path127.curveTo(90d, 235d, 94d, 235d, 98d, 235d);
        path127.curveTo(98d, 235d, 98d, 235d, 98d, 236d);
        path127.curveTo(98d, 237d, 98d, 239d, 98d, 241d);
        path127.curveTo(96d, 241d, 94d, 241d, 92d, 241d);
        path127.lineTo(92d, 242d);
        path127.curveTo(92d, 261d, 92d, 280d, 91d, 299d);
        path127.curveTo(90d, 299d, 86d, 299d, 85d, 299d);
        path127.curveTo(84d, 300d, 86d, 304d, 85d, 305d);
        path127.curveTo(83d, 306d, 80d, 304d, 79d, 306d);
        path127.curveTo(78d, 308d, 79d, 311d, 79d, 314d);
        path127.curveTo(79d, 315d, 78d, 319d, 79d, 319d);
        path127.lineTo(102d, 319d);
        path127.curveTo(102d, 319d, 103d, 318d, 103d, 318d);
        path127.curveTo(104d, 318d, 105d, 320d, 105d, 321d);
        path127.curveTo(104d, 322d, 104d, 323d, 104d, 325d);
        path127.lineTo(73d, 325d);
        path127.lineTo(73d, 300d);
        path127.closePath();
        g2.setColor(new Color(8, 4, 23));
        g2.fill(path127);

        // Element 128
        GeneralPath path128 = new GeneralPath();
        path128.moveTo(220d, 267d);
        path128.curveTo(220d, 265d, 220d, 263d, 220d, 261d);
        path128.curveTo(220d, 261d, 220d, 260d, 221d, 260d);
        path128.curveTo(222d, 260d, 230d, 260d, 232d, 260d);
        path128.lineTo(232d, 261d);
        path128.curveTo(231d, 261d, 231d, 255d, 232d, 254d);
        path128.lineTo(233d, 254d);
        path128.curveTo(239d, 254d, 245d, 254d, 252d, 254d);
        path128.curveTo(252d, 255d, 252d, 260d, 251d, 260d);
        path128.curveTo(249d, 260d, 247d, 261d, 246d, 261d);
        path128.lineTo(246d, 261d);
        path128.curveTo(246d, 263d, 247d, 264d, 247d, 266d);
        path128.curveTo(253d, 265d, 253d, 267d, 253d, 272d);
        path128.curveTo(252d, 287d, 252d, 302d, 253d, 317d);
        path128.lineTo(257d, 317d);
        path128.lineTo(258d, 318d);
        path128.curveTo(257d, 319d, 252d, 318d, 252d, 318d);
        path128.curveTo(252d, 319d, 253d, 323d, 252d, 324d);
        path128.curveTo(250d, 324d, 250d, 319d, 252d, 319d);
        path128.lineTo(246d, 319d);
        path128.curveTo(244d, 316d, 244d, 313d, 245d, 310d);
        path128.curveTo(245d, 298d, 245d, 285d, 245d, 272d);
        path128.curveTo(244d, 270d, 244d, 268d, 245d, 267d);
        path128.curveTo(245d, 268d, 244d, 268d, 242d, 268d);
        path128.curveTo(239d, 267d, 236d, 267d, 233d, 268d);
        path128.lineTo(233d, 267d);
        path128.lineTo(233d, 267d);
        path128.curveTo(233d, 269d, 233d, 271d, 232d, 274d);
        path128.lineTo(232d, 273d);
        path128.curveTo(228d, 274d, 224d, 274d, 220d, 274d);
        path128.lineTo(220d, 273d);
        path128.lineTo(221d, 274d);
        path128.curveTo(220d, 275d, 220d, 278d, 221d, 279d);
        path128.curveTo(221d, 279d, 221d, 279d, 221d, 278d);
        path128.curveTo(223d, 278d, 230d, 278d, 232d, 278d);
        path128.lineTo(233d, 279d);
        path128.lineTo(233d, 324d);
        path128.curveTo(233d, 324d, 233d, 324d, 233d, 324d);
        path128.curveTo(233d, 323d, 250d, 323d, 251d, 323d);
        path128.lineTo(252d, 324d);
        path128.curveTo(251d, 325d, 251d, 325d, 250d, 325d);
        path128.curveTo(247d, 325d, 237d, 325d, 234d, 325d);
        path128.curveTo(233d, 325d, 232d, 325d, 232d, 324d);
        path128.lineTo(232d, 280d);
        path128.lineTo(220d, 280d);
        path128.curveTo(220d, 287d, 220d, 287d, 214d, 286d);
        path128.lineTo(213d, 286d);
        path128.curveTo(216d, 286d, 218d, 286d, 219d, 285d);
        path128.lineTo(219d, 281d);
        path128.curveTo(215d, 280d, 211d, 280d, 207d, 280d);
        path128.lineTo(207d, 286d);
        path128.curveTo(205d, 286d, 203d, 286d, 201d, 286d);
        path128.curveTo(201d, 282d, 201d, 278d, 201d, 274d);
        path128.curveTo(202d, 273d, 204d, 273d, 205d, 273d);
        path128.curveTo(207d, 273d, 207d, 269d, 206d, 267d);
        path128.lineTo(208d, 267d);
        path128.curveTo(208d, 267d, 208d, 266d, 208d, 266d);
        path128.curveTo(211d, 266d, 216d, 266d, 219d, 266d);
        path128.lineTo(220d, 267d);
        path128.closePath();
        g2.setColor(new Color(158, 95, 75));
        g2.fill(path128);

        // Element 129
        GeneralPath path129 = new GeneralPath();
        path129.moveTo(214d, 100d);
        path129.curveTo(215d, 100d, 218d, 100d, 220d, 100d);
        path129.curveTo(220d, 100d, 220d, 100d, 220d, 100d);
        path129.lineTo(221d, 101d);
        path129.lineTo(221d, 106d);
        path129.curveTo(223d, 106d, 225d, 106d, 226d, 107d);
        path129.curveTo(226d, 107d, 226d, 107d, 226d, 108d);
        path129.curveTo(227d, 110d, 227d, 112d, 227d, 115d);
        path129.lineTo(227d, 144d);
        path129.curveTo(228d, 145d, 230d, 145d, 231d, 144d);
        path129.lineTo(232d, 146d);
        path129.curveTo(232d, 150d, 232d, 154d, 232d, 158d);
        path129.curveTo(232d, 159d, 232d, 159d, 231d, 159d);
        path129.lineTo(221d, 159d);
        path129.curveTo(221d, 161d, 221d, 163d, 221d, 164d);
        path129.curveTo(221d, 164d, 221d, 164d, 221d, 163d);
        path129.curveTo(228d, 163d, 236d, 163d, 242d, 163d);
        path129.curveTo(244d, 163d, 245d, 163d, 246d, 164d);
        path129.lineTo(247d, 165d);
        path129.lineTo(247d, 221d);
        path129.lineTo(246d, 222d);
        path129.curveTo(246d, 222d, 246d, 222d, 246d, 222d);
        path129.curveTo(245d, 223d, 245d, 223d, 245d, 223d);
        path129.curveTo(244d, 224d, 242d, 224d, 241d, 222d);
        path129.curveTo(241d, 222d, 241d, 222d, 241d, 222d);
        path129.curveTo(242d, 222d, 245d, 222d, 245d, 221d);
        path129.lineTo(245d, 165d);
        path129.lineTo(220d, 165d);
        path129.curveTo(219d, 165d, 219d, 165d, 219d, 165d);
        path129.lineTo(218d, 159d);
        path129.curveTo(215d, 160d, 213d, 160d, 214d, 158d);
        path129.lineTo(214d, 157d);
        path129.curveTo(216d, 157d, 218d, 157d, 219d, 156d);
        path129.lineTo(218d, 146d);
        path129.lineTo(215d, 146d);
        path129.curveTo(214d, 146d, 212d, 146d, 213d, 145d);
        path129.lineTo(212d, 144d);
        path129.lineTo(212d, 111d);
        path129.lineTo(213d, 111d);
        path129.curveTo(211d, 109d, 212d, 103d, 214d, 100d);
        path129.curveTo(213d, 101d, 213d, 101d, 213d, 100d);
        path129.curveTo(212d, 100d, 212d, 100d, 213d, 100d);
        path129.lineTo(212d, 99d);
        path129.curveTo(212d, 96d, 212d, 93d, 212d, 90d);
        path129.lineTo(213d, 89d);
        path129.curveTo(213d, 89d, 213d, 89d, 214d, 89d);
        path129.curveTo(213d, 93d, 214d, 97d, 214d, 100d);
        path129.closePath();
        g2.setColor(new Color(123, 80, 78));
        g2.fill(path129);

        // Element 130
        GeneralPath path130 = new GeneralPath();
        path130.moveTo(246d, 138d);
        path130.curveTo(244d, 137d, 245d, 117d, 245d, 114d);
        path130.curveTo(246d, 112d, 250d, 113d, 252d, 113d);
        path130.lineTo(252d, 107d);
        path130.curveTo(252d, 107d, 270d, 107d, 270d, 107d);
        path130.curveTo(270d, 107d, 271d, 108d, 271d, 108d);
        path130.lineTo(271d, 145d);
        path130.lineTo(277d, 145d);
        path130.curveTo(277d, 146d, 272d, 145d, 271d, 146d);
        path130.curveTo(271d, 147d, 272d, 151d, 270d, 152d);
        path130.lineTo(270d, 146d);
        path130.lineTo(252d, 146d);
        path130.curveTo(251d, 146d, 252d, 140d, 251d, 139d);
        path130.curveTo(250d, 138d, 247d, 140d, 246d, 138d);
        path130.quadTo(247d, 137d, 246d, 138d);
        path130.closePath();
        g2.setColor(new Color(179, 100, 73));
        g2.fill(path130);

        // Element 131
        GeneralPath path131 = new GeneralPath();
        path131.moveTo(240d, 242d);
        path131.lineTo(252d, 242d);
        path131.curveTo(251d, 248d, 251d, 248d, 258d, 248d);
        path131.lineTo(258d, 318d);
        path131.curveTo(257d, 318d, 252d, 318d, 252d, 318d);
        path131.curveTo(252d, 318d, 251d, 314d, 251d, 313d);
        path131.curveTo(251d, 303d, 252d, 293d, 251d, 283d);
        path131.curveTo(251d, 279d, 252d, 274d, 252d, 270d);
        path131.curveTo(252d, 269d, 252d, 267d, 251d, 267d);
        path131.curveTo(251d, 267d, 247d, 267d, 245d, 267d);
        path131.curveTo(246d, 265d, 245d, 262d, 245d, 261d);
        path131.lineTo(246d, 261d);
        path131.curveTo(252d, 262d, 253d, 260d, 251d, 254d);
        path131.curveTo(246d, 254d, 239d, 254d, 233d, 254d);
        path131.curveTo(231d, 254d, 231d, 248d, 233d, 247d);
        path131.lineTo(238d, 247d);
        path131.curveTo(237d, 243d, 238d, 242d, 239d, 242d);
        path131.closePath();
        g2.setColor(new Color(14, 9, 30));
        g2.fill(path131);

        // Element 132
        GeneralPath path132 = new GeneralPath();
        path132.moveTo(232d, 158d);
        path132.curveTo(232d, 154d, 232d, 149d, 232d, 145d);
        path132.curveTo(234d, 145d, 237d, 146d, 239d, 145d);
        path132.curveTo(239d, 150d, 239d, 154d, 240d, 158d);
        path132.curveTo(240d, 157d, 240d, 157d, 241d, 158d);
        path132.curveTo(246d, 158d, 252d, 158d, 258d, 157d);
        path132.curveTo(259d, 163d, 259d, 166d, 252d, 165d);
        path132.lineTo(252d, 165d);
        path132.lineTo(252d, 222d);
        path132.curveTo(250d, 222d, 248d, 222d, 246d, 222d);
        path132.lineTo(246d, 164d);
        path132.lineTo(221d, 164d);
        path132.curveTo(220d, 164d, 220d, 159d, 220d, 158d);
        path132.curveTo(222d, 157d, 230d, 159d, 232d, 158d);
        path132.closePath();
        g2.setColor(new Color(10, 4, 27));
        g2.fill(path132);

        // Element 133
        GeneralPath path133 = new GeneralPath();
        path133.moveTo(104d, 94d);
        path133.curveTo(104d, 96d, 100d, 95d, 98d, 95d);
        path133.curveTo(98d, 95d, 99d, 95d, 99d, 95d);
        path133.curveTo(99d, 97d, 99d, 105d, 99d, 106d);
        path133.lineTo(98d, 106d);
        path133.lineTo(99d, 105d);
        path133.lineTo(103d, 105d);
        path133.curveTo(104d, 102d, 104d, 99d, 104d, 96d);
        path133.lineTo(105d, 94d);
        path133.curveTo(106d, 94d, 109d, 94d, 111d, 94d);
        path133.curveTo(110d, 87d, 110d, 87d, 117d, 88d);
        path133.lineTo(117d, 88d);
        path133.curveTo(117d, 88d, 117d, 89d, 117d, 89d);
        path133.curveTo(112d, 88d, 111d, 89d, 112d, 94d);
        path133.curveTo(111d, 96d, 106d, 96d, 105d, 94d);
        path133.curveTo(105d, 95d, 106d, 95d, 106d, 95d);
        path133.curveTo(106d, 96d, 106d, 104d, 106d, 106d);
        path133.lineTo(105d, 106d);
        path133.curveTo(106d, 107d, 106d, 107d, 105d, 108d);
        path133.curveTo(103d, 108d, 101d, 108d, 99d, 108d);
        path133.lineTo(100d, 123d);
        path133.curveTo(100d, 127d, 98d, 128d, 93d, 127d);
        path133.curveTo(93d, 151d, 93d, 175d, 93d, 198d);
        path133.lineTo(92d, 203d);
        path133.curveTo(93d, 205d, 93d, 207d, 93d, 209d);
        path133.curveTo(94d, 208d, 97d, 208d, 98d, 209d);
        path133.curveTo(100d, 209d, 100d, 215d, 99d, 216d);
        path133.curveTo(98d, 216d, 98d, 216d, 98d, 216d);
        path133.lineTo(97d, 215d);
        path133.curveTo(97d, 211d, 97d, 210d, 93d, 210d);
        path133.curveTo(93d, 213d, 93d, 215d, 92d, 215d);
        path133.curveTo(92d, 216d, 92d, 216d, 92d, 216d);
        path133.curveTo(91d, 216d, 91d, 216d, 91d, 216d);
        path133.lineTo(90d, 211d);
        path133.curveTo(86d, 211d, 85d, 211d, 86d, 209d);
        path133.curveTo(86d, 208d, 88d, 208d, 90d, 208d);
        path133.curveTo(91d, 182d, 91d, 154d, 90d, 128d);
        path133.curveTo(88d, 128d, 86d, 128d, 86d, 126d);
        path133.curveTo(86d, 127d, 86d, 127d, 87d, 127d);
        path133.curveTo(87d, 128d, 87d, 136d, 87d, 138d);
        path133.lineTo(86d, 138d);
        path133.curveTo(87d, 140d, 85d, 140d, 84d, 140d);
        path133.curveTo(81d, 139d, 80d, 141d, 81d, 144d);
        path133.curveTo(83d, 144d, 85d, 144d, 86d, 145d);
        path133.curveTo(86d, 145d, 87d, 145d, 87d, 146d);
        path133.lineTo(87d, 190d);
        path133.lineTo(86d, 190d);
        path133.curveTo(85d, 192d, 80d, 192d, 80d, 190d);
        path133.curveTo(79d, 191d, 79d, 191d, 79d, 190d);
        path133.curveTo(81d, 189d, 83d, 190d, 85d, 190d);
        path133.lineTo(85d, 145d);
        path133.curveTo(78d, 146d, 78d, 146d, 79d, 139d);
        path133.curveTo(77d, 139d, 73d, 140d, 73d, 138d);
        path133.curveTo(74d, 138d, 78d, 139d, 79d, 138d);
        path133.curveTo(79d, 138d, 79d, 129d, 79d, 128d);
        path133.curveTo(79d, 119d, 78d, 111d, 79d, 102d);
        path133.curveTo(79d, 101d, 78d, 100d, 80d, 100d);
        path133.lineTo(80d, 101d);
        path133.curveTo(80d, 107d, 80d, 113d, 80d, 119d);
        path133.curveTo(81d, 119d, 83d, 119d, 84d, 119d);
        path133.lineTo(85d, 104d);
        path133.curveTo(85d, 103d, 85d, 102d, 86d, 100d);
        path133.lineTo(86d, 99d);
        path133.lineTo(91d, 99d);
        path133.lineTo(90d, 95d);
        path133.curveTo(88d, 96d, 86d, 96d, 86d, 94d);
        path133.lineTo(92d, 94d);
        path133.curveTo(92d, 93d, 91d, 88d, 92d, 88d);
        path133.curveTo(93d, 87d, 103d, 87d, 104d, 88d);
        path133.curveTo(105d, 88d, 105d, 93d, 105d, 94d);
        path133.closePath();
        g2.setColor(new Color(122, 80, 78));
        g2.fill(path133);

        // Element 134
        GeneralPath path134 = new GeneralPath();
        path134.moveTo(72d, 197d);
        path134.lineTo(78d, 197d);
        path134.curveTo(79d, 196d, 79d, 197d, 78d, 198d);
        path134.curveTo(79d, 198d, 79d, 198d, 79d, 198d);
        path134.curveTo(79d, 208d, 79d, 219d, 78d, 228d);
        path134.curveTo(77d, 229d, 75d, 229d, 72d, 228d);
        path134.lineTo(72d, 197d);
        path134.closePath();
        g2.setColor(new Color(10, 5, 25));
        g2.fill(path134);

        // Element 135
        GeneralPath path135 = new GeneralPath();
        path135.moveTo(241d, 222d);
        path135.lineTo(241d, 222d);
        path135.curveTo(240d, 223d, 240d, 233d, 240d, 234d);
        path135.curveTo(242d, 233d, 244d, 233d, 245d, 234d);
        path135.curveTo(244d, 235d, 242d, 236d, 240d, 235d);
        path135.lineTo(239d, 236d);
        path135.curveTo(238d, 237d, 240d, 240d, 240d, 242d);
        path135.curveTo(240d, 242d, 239d, 242d, 239d, 243d);
        path135.curveTo(239d, 244d, 239d, 246d, 239d, 248d);
        path135.lineTo(232d, 248d);
        path135.curveTo(232d, 249d, 233d, 254d, 233d, 254d);
        path135.lineTo(251d, 254d);
        path135.curveTo(252d, 254d, 252d, 260d, 252d, 261d);
        path135.curveTo(251d, 262d, 247d, 261d, 245d, 261d);
        path135.curveTo(246d, 259d, 251d, 261d, 251d, 260d);
        path135.lineTo(251d, 255d);
        path135.lineTo(232d, 255d);
        path135.lineTo(232d, 261d);
        path135.lineTo(220d, 261d);
        path135.curveTo(220d, 263d, 221d, 265d, 219d, 267d);
        path135.curveTo(218d, 267d, 218d, 262d, 219d, 261d);
        path135.curveTo(219d, 261d, 219d, 262d, 219d, 262d);
        path135.curveTo(218d, 262d, 208d, 262d, 208d, 262d);
        path135.lineTo(207d, 261d);
        path135.curveTo(209d, 261d, 208d, 266d, 207d, 267d);
        path135.curveTo(206d, 273d, 209d, 275d, 201d, 274d);
        path135.lineTo(201d, 286d);
        path135.lineTo(206d, 286d);
        path135.curveTo(206d, 286d, 207d, 280d, 207d, 280d);
        path135.curveTo(207d, 280d, 207d, 280d, 207d, 281d);
        path135.curveTo(207d, 282d, 207d, 285d, 207d, 287d);
        path135.lineTo(200d, 287d);
        path135.lineTo(200d, 274d);
        path135.curveTo(200d, 273d, 205d, 273d, 206d, 273d);
        path135.lineTo(206d, 268d);
        path135.curveTo(206d, 267d, 200d, 268d, 198d, 267d);
        path135.curveTo(201d, 266d, 203d, 266d, 206d, 266d);
        path135.lineTo(205d, 263d);
        path135.curveTo(206d, 261d, 206d, 259d, 207d, 261d);
        path135.curveTo(207d, 260d, 208d, 260d, 208d, 260d);
        path135.curveTo(209d, 259d, 218d, 259d, 219d, 260d);
        path135.lineTo(220d, 261d);
        path135.curveTo(218d, 260d, 218d, 255d, 220d, 255d);
        path135.curveTo(220d, 255d, 220d, 255d, 219d, 255d);
        path135.curveTo(219d, 256d, 202d, 256d, 202d, 256d);
        path135.lineTo(201d, 255d);
        path135.curveTo(203d, 256d, 203d, 261d, 200d, 262d);
        path135.curveTo(200d, 262d, 184d, 262d, 182d, 262d);
        path135.lineTo(182d, 261d);
        path135.lineTo(200d, 261d);
        path135.lineTo(200d, 254d);
        path135.lineTo(169d, 254d);
        path135.curveTo(169d, 254d, 200d, 254d, 200d, 254d);
        path135.curveTo(200d, 253d, 200d, 248d, 201d, 248d);
        path135.lineTo(220d, 248d);
        path135.curveTo(220d, 246d, 219d, 243d, 220d, 242d);
        path135.curveTo(222d, 242d, 221d, 246d, 221d, 248d);
        path135.curveTo(221d, 247d, 221d, 247d, 221d, 247d);
        path135.curveTo(222d, 246d, 231d, 246d, 231d, 247d);
        path135.lineTo(232d, 248d);
        path135.lineTo(231d, 247d);
        path135.lineTo(231d, 242d);
        path135.lineTo(221d, 242d);
        path135.curveTo(221d, 242d, 220d, 241d, 220d, 241d);
        path135.curveTo(221d, 240d, 230d, 241d, 232d, 241d);
        path135.curveTo(231d, 234d, 233d, 235d, 238d, 234d);
        path135.curveTo(239d, 234d, 238d, 225d, 239d, 223d);
        path135.curveTo(239d, 222d, 240d, 222d, 241d, 222d);
        path135.closePath();
        g2.setColor(new Color(94, 58, 58));
        g2.fill(path135);

        // Element 136
        GeneralPath path136 = new GeneralPath();
        path136.moveTo(80d, 190d);
        path136.curveTo(81d, 192d, 81d, 195d, 80d, 196d);
        path136.curveTo(81d, 200d, 81d, 205d, 79d, 208d);
        path136.curveTo(79d, 211d, 79d, 213d, 79d, 216d);
        path136.lineTo(80d, 217d);
        path136.curveTo(80d, 220d, 80d, 224d, 80d, 228d);
        path136.curveTo(83d, 227d, 85d, 227d, 85d, 229d);
        path136.quadTo(84d, 230d, 85d, 229d);
        path136.curveTo(87d, 228d, 87d, 234d, 86d, 234d);
        path136.curveTo(86d, 234d, 86d, 234d, 86d, 233d);
        path136.curveTo(86d, 233d, 96d, 233d, 97d, 233d);
        path136.lineTo(98d, 234d);
        path136.curveTo(96d, 230d, 97d, 226d, 98d, 221d);
        path136.curveTo(97d, 220d, 97d, 219d, 97d, 217d);
        path136.curveTo(94d, 218d, 93d, 217d, 92d, 216d);
        path136.lineTo(92d, 215d);
        path136.curveTo(94d, 214d, 96d, 214d, 98d, 215d);
        path136.curveTo(98d, 215d, 98d, 215d, 98d, 215d);
        path136.curveTo(99d, 214d, 101d, 214d, 100d, 216d);
        path136.lineTo(99d, 217d);
        path136.lineTo(99d, 234d);
        path136.curveTo(99d, 234d, 98d, 235d, 98d, 235d);
        path136.curveTo(99d, 234d, 99d, 234d, 99d, 234d);
        path136.curveTo(100d, 233d, 108d, 233d, 110d, 234d);
        path136.lineTo(110d, 235d);
        path136.curveTo(109d, 234d, 109d, 233d, 110d, 230d);
        path136.curveTo(107d, 231d, 105d, 230d, 105d, 229d);
        path136.lineTo(111d, 229d);
        path136.curveTo(111d, 229d, 111d, 235d, 111d, 235d);
        path136.curveTo(110d, 236d, 100d, 234d, 98d, 236d);
        path136.curveTo(100d, 236d, 100d, 238d, 98d, 240d);
        path136.curveTo(102d, 243d, 93d, 244d, 92d, 242d);
        path136.lineTo(93d, 242d);
        path136.lineTo(93d, 289d);
        path136.lineTo(92d, 290d);
        path136.curveTo(92d, 290d, 93d, 290d, 93d, 290d);
        path136.curveTo(92d, 293d, 92d, 295d, 93d, 298d);
        path136.curveTo(93d, 299d, 92d, 299d, 91d, 299d);
        path136.lineTo(91d, 299d);
        path136.lineTo(91d, 242d);
        path136.curveTo(91d, 240d, 97d, 242d, 98d, 241d);
        path136.lineTo(98d, 236d);
        path136.lineTo(85d, 236d);
        path136.lineTo(85d, 291d);
        path136.curveTo(85d, 291d, 86d, 291d, 86d, 292d);
        path136.curveTo(85d, 294d, 81d, 293d, 79d, 294d);
        path136.curveTo(80d, 301d, 79d, 300d, 72d, 300d);
        path136.lineTo(78d, 299d);
        path136.curveTo(78d, 292d, 78d, 292d, 85d, 292d);
        path136.lineTo(85d, 236d);
        path136.curveTo(79d, 235d, 78d, 236d, 78d, 229d);
        path136.lineTo(72d, 228d);
        path136.lineTo(78d, 228d);
        path136.lineTo(78d, 198d);
        path136.curveTo(78d, 197d, 74d, 197d, 72d, 197d);
        path136.lineTo(78d, 196d);
        path136.curveTo(79d, 195d, 78d, 191d, 79d, 190d);
        path136.curveTo(79d, 190d, 79d, 191d, 79d, 190d);
        path136.closePath();
        g2.setColor(new Color(94, 69, 63));
        g2.fill(path136);

        // Element 137
        GeneralPath path137 = new GeneralPath();
        path137.moveTo(245d, 146d);
        path137.curveTo(246d, 146d, 245d, 150d, 246d, 152d);
        path137.lineTo(257d, 152d);
        path137.curveTo(259d, 152d, 258d, 158d, 258d, 158d);
        path137.curveTo(259d, 158d, 263d, 158d, 264d, 158d);
        path137.curveTo(263d, 159d, 260d, 158d, 258d, 158d);
        path137.curveTo(259d, 165d, 259d, 165d, 252d, 165d);
        path137.lineTo(252d, 222d);
        path137.lineTo(252d, 164d);
        path137.curveTo(253d, 164d, 257d, 165d, 258d, 164d);
        path137.lineTo(258d, 158d);
        path137.lineTo(240d, 158d);
        path137.curveTo(239d, 156d, 239d, 154d, 240d, 152d);
        path137.curveTo(242d, 150d, 242d, 156d, 241d, 158d);
        path137.curveTo(241d, 157d, 241d, 157d, 242d, 156d);
        path137.curveTo(244d, 156d, 255d, 156d, 257d, 156d);
        path137.lineTo(258d, 158d);
        path137.curveTo(257d, 157d, 256d, 156d, 256d, 154d);
        path137.lineTo(251d, 153d);
        path137.curveTo(246d, 156d, 240d, 153d, 244d, 148d);
        path137.curveTo(243d, 147d, 243d, 146d, 242d, 145d);
        path137.curveTo(243d, 145d, 244d, 145d, 245d, 145d);
        path137.closePath();
        g2.setColor(new Color(163, 97, 77));
        g2.fill(path137);

        // Element 138
        GeneralPath path138 = new GeneralPath();
        path138.moveTo(264d, 94d);
        path138.curveTo(266d, 94d, 269d, 95d, 271d, 94d);
        path138.lineTo(272d, 100d);
        path138.curveTo(271d, 100d, 271d, 101d, 270d, 100d);
        path138.lineTo(270d, 94d);
        path138.lineTo(264d, 94d);
        path138.curveTo(264d, 94d, 264d, 100d, 264d, 100d);
        path138.lineTo(246d, 100d);
        path138.curveTo(245d, 100d, 246d, 106d, 246d, 107d);
        path138.curveTo(243d, 107d, 241d, 107d, 239d, 107d);
        path138.curveTo(238d, 107d, 238d, 107d, 239d, 106d);
        path138.curveTo(238d, 102d, 239d, 98d, 239d, 94d);
        path138.curveTo(239d, 94d, 239d, 94d, 240d, 94d);
        path138.curveTo(247d, 94d, 255d, 94d, 263d, 94d);
        path138.curveTo(264d, 94d, 264d, 93d, 264d, 93d);
        path138.closePath();
        g2.setColor(new Color(161, 99, 80));
        g2.fill(path138);

        // Element 139
        GeneralPath path139 = new GeneralPath();
        path139.moveTo(207d, 280d);
        path139.lineTo(220d, 280d);
        path139.curveTo(220d, 280d, 220d, 286d, 220d, 286d);
        path139.curveTo(218d, 286d, 214d, 285d, 214d, 286d);
        path139.lineTo(213d, 344d);
        path139.lineTo(182d, 343d);
        path139.lineTo(212d, 343d);
        path139.lineTo(212d, 286d);
        path139.curveTo(212d, 285d, 218d, 286d, 219d, 286d);
        path139.lineTo(219d, 280d);
        path139.curveTo(219d, 280d, 207d, 280d, 207d, 280d);
        path139.curveTo(207d, 280d, 207d, 280d, 207d, 280d);
        path139.closePath();
        g2.setColor(new Color(8, 6, 28));
        g2.fill(path139);

        // Element 140
        GeneralPath path140 = new GeneralPath();
        path140.moveTo(239d, 236d);
        path140.curveTo(239d, 234d, 245d, 236d, 245d, 234d);
        path140.curveTo(244d, 234d, 244d, 224d, 244d, 224d);
        path140.lineTo(245d, 223d);
        path140.curveTo(245d, 223d, 245d, 223d, 246d, 222d);
        path140.curveTo(245d, 225d, 246d, 234d, 245d, 235d);
        path140.curveTo(245d, 236d, 240d, 235d, 239d, 236d);
        path140.closePath();
        g2.setColor(new Color(182, 102, 74));
        g2.fill(path140);

        // Element 141
        GeneralPath path141 = new GeneralPath();
        path141.moveTo(124d, 55d);
        path141.curveTo(124d, 53d, 124d, 51d, 124d, 49d);
        path141.curveTo(130d, 50d, 131d, 49d, 130d, 43d);
        path141.curveTo(155d, 43d, 182d, 43d, 206d, 43d);
        path141.curveTo(207d, 45d, 207d, 47d, 207d, 50d);
        path141.curveTo(211d, 49d, 216d, 49d, 220d, 50d);
        path141.lineTo(220d, 50d);
        path141.curveTo(220d, 55d, 220d, 56d, 225d, 55d);
        path141.curveTo(226d, 55d, 227d, 56d, 227d, 56d);
        path141.curveTo(226d, 62d, 226d, 68d, 226d, 75d);
        path141.lineTo(226d, 74d);
        path141.curveTo(221d, 75d, 217d, 75d, 213d, 75d);
        path141.curveTo(213d, 73d, 212d, 71d, 212d, 69d);
        path141.curveTo(210d, 70d, 201d, 70d, 200d, 69d);
        path141.curveTo(200d, 67d, 199d, 65d, 199d, 63d);
        path141.lineTo(137d, 63d);
        path141.curveTo(137d, 63d, 137d, 64d, 137d, 64d);
        path141.curveTo(137d, 67d, 137d, 72d, 134d, 68d);
        path141.curveTo(131d, 68d, 127d, 68d, 124d, 68d);
        path141.curveTo(125d, 69d, 125d, 75d, 124d, 75d);
        path141.curveTo(122d, 75d, 121d, 75d, 119d, 74d);
        path141.curveTo(117d, 75d, 115d, 75d, 113d, 75d);
        path141.curveTo(113d, 75d, 112d, 75d, 111d, 75d);
        path141.lineTo(111d, 62d);
        path141.curveTo(111d, 62d, 117d, 62d, 117d, 62d);
        path141.curveTo(117d, 61d, 117d, 57d, 117d, 56d);
        path141.curveTo(118d, 55d, 122d, 56d, 124d, 55d);
        path141.closePath();
        g2.setColor(new Color(243, 244, 231));
        g2.fill(path141);

        // Element 142
        GeneralPath path142 = new GeneralPath();
        path142.moveTo(130d, 24d);
        path142.lineTo(212d, 24d);
        path142.curveTo(215d, 24d, 213d, 28d, 214d, 30d);
        path142.lineTo(226d, 30d);
        path142.curveTo(226d, 31d, 226d, 35d, 226d, 36d);
        path142.curveTo(227d, 37d, 231d, 36d, 232d, 37d);
        path142.curveTo(233d, 38d, 232d, 42d, 233d, 43d);
        path142.curveTo(234d, 44d, 238d, 42d, 239d, 43d);
        path142.curveTo(240d, 44d, 238d, 53d, 239d, 56d);
        path142.lineTo(240d, 56d);
        path142.curveTo(245d, 55d, 246d, 57d, 245d, 63d);
        path142.lineTo(240d, 63d);
        path142.curveTo(240d, 65d, 240d, 67d, 239d, 68d);
        path142.curveTo(239d, 69d, 233d, 69d, 233d, 68d);
        path142.lineTo(233d, 57d);
        path142.curveTo(232d, 56d, 232d, 56d, 232d, 56d);
        path142.lineTo(231d, 55d);
        path142.curveTo(232d, 51d, 231d, 49d, 227d, 50d);
        path142.lineTo(226d, 50d);
        path142.curveTo(226d, 50d, 225d, 50d, 225d, 49d);
        path142.lineTo(225d, 43d);
        path142.curveTo(220d, 44d, 218d, 43d, 218d, 38d);
        path142.lineTo(214d, 37d);
        path142.lineTo(213d, 36d);
        path142.curveTo(211d, 36d, 209d, 36d, 207d, 36d);
        path142.curveTo(206d, 36d, 206d, 31d, 206d, 30d);
        path142.lineTo(206d, 31d);
        path142.lineTo(131d, 31d);
        path142.lineTo(130d, 30d);
        path142.curveTo(130d, 28d, 130d, 26d, 130d, 24d);
        path142.closePath();
        g2.setColor(new Color(212, 148, 79));
        g2.fill(path142);

        // Element 143
        GeneralPath path143 = new GeneralPath();
        path143.moveTo(130d, 24d);
        path143.lineTo(130d, 30d);
        path143.lineTo(206d, 30d);
        path143.curveTo(208d, 30d, 207d, 36d, 207d, 36d);
        path143.curveTo(207d, 36d, 213d, 35d, 213d, 36d);
        path143.curveTo(184d, 36d, 155d, 37d, 126d, 36d);
        path143.curveTo(126d, 35d, 127d, 35d, 129d, 35d);
        path143.curveTo(129d, 33d, 129d, 32d, 130d, 30d);
        path143.curveTo(130d, 30d, 130d, 30d, 130d, 30d);
        path143.curveTo(130d, 28d, 130d, 25d, 130d, 24d);
        path143.closePath();
        g2.setColor(new Color(174, 102, 75));
        g2.fill(path143);

        // Element 144
        GeneralPath path144 = new GeneralPath();
        path144.moveTo(134d, 68d);
        path144.curveTo(135d, 68d, 135d, 69d, 136d, 68d);
        path144.curveTo(137d, 68d, 135d, 62d, 137d, 62d);
        path144.lineTo(200d, 62d);
        path144.curveTo(201d, 63d, 200d, 68d, 201d, 68d);
        path144.curveTo(202d, 69d, 208d, 69d, 210d, 69d);
        path144.curveTo(211d, 68d, 212d, 68d, 213d, 68d);
        path144.curveTo(214d, 68d, 213d, 73d, 214d, 74d);
        path144.lineTo(226d, 74d);
        path144.curveTo(226d, 75d, 226d, 75d, 226d, 75d);
        path144.lineTo(214d, 75d);
        path144.curveTo(214d, 76d, 213d, 80d, 214d, 81d);
        path144.curveTo(215d, 83d, 222d, 78d, 220d, 87d);
        path144.curveTo(219d, 90d, 214d, 86d, 214d, 89d);
        path144.lineTo(213d, 89d);
        path144.lineTo(211d, 88d);
        path144.curveTo(209d, 89d, 207d, 89d, 206d, 87d);
        path144.curveTo(206d, 82d, 206d, 82d, 201d, 82d);
        path144.curveTo(199d, 82d, 199d, 77d, 200d, 75d);
        path144.curveTo(197d, 74d, 195d, 71d, 194d, 69d);
        path144.curveTo(194d, 69d, 194d, 70d, 194d, 70d);
        path144.curveTo(176d, 69d, 157d, 69d, 139d, 70d);
        path144.curveTo(138d, 70d, 137d, 71d, 137d, 74d);
        path144.curveTo(140d, 75d, 136d, 77d, 134d, 77d);
        path144.curveTo(131d, 76d, 128d, 76d, 125d, 76d);
        path144.curveTo(125d, 82d, 125d, 84d, 119d, 83d);
        path144.lineTo(118d, 87d);
        path144.curveTo(118d, 88d, 118d, 88d, 117d, 88d);
        path144.curveTo(117d, 88d, 117d, 88d, 117d, 88d);
        path144.curveTo(118d, 81d, 115d, 80d, 124d, 81d);
        path144.lineTo(124d, 75d);
        path144.curveTo(122d, 75d, 120d, 75d, 119d, 75d);
        path144.curveTo(119d, 75d, 119d, 75d, 119d, 74d);
        path144.curveTo(121d, 74d, 122d, 75d, 124d, 74d);
        path144.curveTo(124d, 73d, 123d, 70d, 124d, 68d);
        path144.curveTo(127d, 68d, 131d, 68d, 134d, 68d);
        path144.closePath();
        g2.setColor(new Color(177, 102, 78));
        g2.fill(path144);

        // Element 145
        GeneralPath path145 = new GeneralPath();
        path145.moveTo(130d, 30d);
        path145.curveTo(130d, 30d, 130d, 30d, 130d, 30d);
        path145.lineTo(129d, 32d);
        path145.lineTo(119d, 32d);
        path145.curveTo(119d, 34d, 119d, 36d, 118d, 36d);
        path145.curveTo(118d, 37d, 118d, 38d, 117d, 37d);
        path145.curveTo(116d, 38d, 115d, 38d, 112d, 38d);
        path145.curveTo(113d, 40d, 113d, 42d, 112d, 42d);
        path145.curveTo(113d, 44d, 112d, 44d, 110d, 44d);
        path145.curveTo(110d, 45d, 108d, 45d, 106d, 44d);
        path145.curveTo(106d, 46d, 106d, 48d, 105d, 50d);
        path145.curveTo(106d, 51d, 106d, 52d, 106d, 54d);
        path145.curveTo(109d, 55d, 110d, 53d, 109d, 50d);
        path145.lineTo(110d, 50d);
        path145.curveTo(109d, 49d, 110d, 48d, 111d, 48d);
        path145.curveTo(112d, 46d, 114d, 43d, 117d, 42d);
        path145.curveTo(116d, 42d, 117d, 42d, 117d, 42d);
        path145.curveTo(118d, 41d, 119d, 40d, 121d, 39d);
        path145.curveTo(122d, 37d, 123d, 37d, 124d, 37d);
        path145.curveTo(125d, 44d, 124d, 43d, 117d, 43d);
        path145.curveTo(118d, 50d, 118d, 50d, 111d, 50d);
        path145.curveTo(111d, 56d, 111d, 56d, 104d, 56d);
        path145.lineTo(104d, 82d);
        path145.lineTo(98d, 82d);
        path145.lineTo(98d, 56d);
        path145.lineTo(104d, 56d);
        path145.lineTo(104d, 43d);
        path145.curveTo(104d, 42d, 110d, 43d, 110d, 42d);
        path145.curveTo(111d, 42d, 110d, 38d, 111d, 37d);
        path145.curveTo(112d, 36d, 116d, 37d, 117d, 36d);
        path145.curveTo(117d, 35d, 117d, 31d, 117d, 30d);
        path145.lineTo(130d, 30d);
        path145.closePath();
        g2.setColor(new Color(158, 96, 77));
        g2.fill(path145);

        // Element 146
        GeneralPath path146 = new GeneralPath();
        path146.moveTo(239d, 56d);
        path146.curveTo(240d, 56d, 245d, 55d, 245d, 56d);
        path146.curveTo(246d, 57d, 245d, 68d, 246d, 68d);
        path146.curveTo(247d, 69d, 251d, 68d, 252d, 69d);
        path146.curveTo(253d, 71d, 251d, 84d, 251d, 87d);
        path146.curveTo(247d, 88d, 243d, 88d, 239d, 88d);
        path146.curveTo(239d, 82d, 239d, 75d, 239d, 69d);
        path146.curveTo(238d, 69d, 238d, 68d, 239d, 68d);
        path146.curveTo(239d, 66d, 239d, 64d, 239d, 62d);
        path146.lineTo(245d, 62d);
        path146.curveTo(245d, 61d, 245d, 57d, 244d, 57d);
        path146.curveTo(243d, 56d, 239d, 57d, 239d, 56d);
        path146.closePath();
        g2.setColor(new Color(174, 101, 74));
        g2.fill(path146);

        // Element 147
        GeneralPath path147 = new GeneralPath();
        path147.moveTo(226d, 50d);
        path147.lineTo(227d, 50d);
        path147.curveTo(227d, 53d, 228d, 55d, 232d, 54d);
        path147.lineTo(232d, 56d);
        path147.curveTo(233d, 56d, 233d, 56d, 233d, 57d);
        path147.curveTo(233d, 61d, 233d, 64d, 233d, 68d);
        path147.lineTo(239d, 68d);
        path147.curveTo(239d, 68d, 239d, 69d, 239d, 69d);
        path147.lineTo(232d, 69d);
        path147.lineTo(232d, 57d);
        path147.curveTo(232d, 56d, 228d, 56d, 226d, 56d);
        path147.lineTo(226d, 75d);
        path147.curveTo(226d, 75d, 232d, 74d, 232d, 75d);
        path147.curveTo(231d, 76d, 230d, 76d, 228d, 76d);
        path147.curveTo(228d, 79d, 228d, 80d, 226d, 82d);
        path147.curveTo(226d, 80d, 226d, 78d, 226d, 76d);
        path147.curveTo(226d, 76d, 226d, 75d, 226d, 75d);
        path147.curveTo(226d, 75d, 226d, 75d, 226d, 75d);
        path147.curveTo(226d, 74d, 226d, 74d, 226d, 73d);
        path147.curveTo(226d, 67d, 226d, 62d, 226d, 56d);
        path147.curveTo(219d, 56d, 219d, 56d, 220d, 50d);
        path147.lineTo(207d, 50d);
        path147.curveTo(206d, 50d, 207d, 44d, 207d, 43d);
        path147.lineTo(131d, 43d);
        path147.curveTo(131d, 50d, 131d, 50d, 124d, 50d);
        path147.curveTo(124d, 51d, 125d, 54d, 124d, 55d);
        path147.lineTo(124d, 49d);
        path147.curveTo(130d, 49d, 130d, 49d, 130d, 43d);
        path147.lineTo(207d, 43d);
        path147.curveTo(208d, 43d, 206d, 49d, 208d, 49d);
        path147.lineTo(220d, 49d);
        path147.lineTo(220d, 43d);
        path147.curveTo(210d, 44d, 214d, 41d, 213d, 37d);
        path147.curveTo(214d, 37d, 218d, 36d, 219d, 37d);
        path147.curveTo(220d, 38d, 219d, 41d, 220d, 42d);
        path147.curveTo(221d, 43d, 224d, 42d, 226d, 43d);
        path147.curveTo(227d, 44d, 225d, 48d, 227d, 50d);
        path147.closePath();
        g2.setColor(new Color(168, 100, 74));
        g2.fill(path147);

        // Element 148
        GeneralPath path148 = new GeneralPath();
        path148.moveTo(220d, 100d);
        path148.curveTo(220d, 100d, 219d, 95d, 220d, 94d);
        path148.curveTo(220d, 94d, 225d, 94d, 226d, 94d);
        path148.curveTo(226d, 93d, 225d, 88d, 226d, 88d);
        path148.curveTo(227d, 87d, 232d, 87d, 232d, 88d);
        path148.lineTo(232d, 106d);
        path148.lineTo(238d, 106d);
        path148.curveTo(239d, 106d, 238d, 96d, 239d, 94d);
        path148.curveTo(238d, 96d, 239d, 105d, 239d, 107d);
        path148.lineTo(239d, 108d);
        path148.curveTo(238d, 107d, 233d, 107d, 233d, 108d);
        path148.curveTo(233d, 110d, 233d, 113d, 232d, 115d);
        path148.lineTo(232d, 115d);
        path148.curveTo(231d, 113d, 231d, 111d, 231d, 108d);
        path148.curveTo(229d, 109d, 227d, 108d, 226d, 107d);
        path148.curveTo(226d, 107d, 226d, 107d, 226d, 107d);
        path148.curveTo(225d, 105d, 225d, 103d, 225d, 101d);
        path148.curveTo(224d, 102d, 222d, 102d, 220d, 100d);
        path148.curveTo(220d, 100d, 220d, 100d, 219d, 100d);
        path148.closePath();
        g2.setColor(new Color(123, 78, 76));
        g2.fill(path148);

        // Element 149
        GeneralPath path149 = new GeneralPath();
        path149.moveTo(86d, 100d);
        path149.curveTo(85d, 107d, 86d, 113d, 85d, 119d);
        path149.curveTo(85d, 120d, 79d, 121d, 79d, 119d);
        path149.curveTo(79d, 116d, 78d, 101d, 80d, 100d);
        path149.curveTo(80d, 100d, 85d, 101d, 85d, 100d);
        path149.curveTo(85d, 100d, 84d, 97d, 86d, 96d);
        path149.curveTo(87d, 97d, 87d, 99d, 86d, 100d);
        path149.closePath();
        g2.setColor(new Color(203, 144, 76));
        g2.fill(path149);

        // Element 150
        GeneralPath path150 = new GeneralPath();
        path150.moveTo(130d, 30d);
        path150.curveTo(130d, 32d, 130d, 34d, 130d, 36d);
        path150.curveTo(129d, 36d, 127d, 36d, 126d, 36d);
        path150.curveTo(126d, 37d, 125d, 36d, 124d, 36d);
        path150.curveTo(122d, 38d, 119d, 38d, 118d, 36d);
        path150.lineTo(118d, 30d);
        path150.curveTo(118d, 30d, 130d, 30d, 130d, 30d);
        path150.closePath();
        g2.setColor(new Color(210, 147, 83));
        g2.fill(path150);

        // Element 151
        GeneralPath path151 = new GeneralPath();
        path151.moveTo(232d, 75d);
        path151.curveTo(233d, 75d, 232d, 80d, 232d, 82d);
        path151.lineTo(226d, 82d);
        path151.curveTo(226d, 82d, 226d, 75d, 226d, 75d);
        path151.lineTo(232d, 75d);
        path151.closePath();
        g2.setColor(new Color(243, 243, 237));
        g2.fill(path151);

        // Element 152
        GeneralPath path152 = new GeneralPath();
        path152.moveTo(86d, 94d);
        path152.lineTo(92d, 94d);
        path152.curveTo(92d, 94d, 92d, 100d, 92d, 100d);
        path152.lineTo(85d, 100d);
        path152.curveTo(86d, 99d, 85d, 97d, 85d, 96d);
        path152.curveTo(86d, 96d, 85d, 95d, 85d, 94d);
        path152.closePath();
        g2.setColor(new Color(202, 143, 86));
        g2.fill(path152);

        // Element 153
        GeneralPath path153 = new GeneralPath();
        path153.moveTo(124d, 36d);
        path153.curveTo(124d, 37d, 124d, 37d, 124d, 37d);
        path153.curveTo(122d, 38d, 123d, 39d, 121d, 40d);
        path153.curveTo(120d, 41d, 119d, 41d, 118d, 42d);
        path153.curveTo(116d, 41d, 116d, 38d, 117d, 37d);
        path153.curveTo(117d, 37d, 117d, 37d, 118d, 36d);
        path153.curveTo(120d, 36d, 122d, 37d, 124d, 36d);
        path153.closePath();
        g2.setColor(new Color(162, 97, 71));
        g2.fill(path153);

        // Element 154
        GeneralPath path154 = new GeneralPath();
        path154.moveTo(119d, 74d);
        path154.lineTo(119d, 75d);
        path154.curveTo(117d, 75d, 115d, 75d, 114d, 75d);
        path154.curveTo(115d, 74d, 117d, 75d, 119d, 74d);
        path154.closePath();
        g2.setColor(new Color(215, 153, 71));
        g2.fill(path154);

        // Element 155
        GeneralPath path155 = new GeneralPath();
        path155.moveTo(194d, 70d);
        path155.curveTo(195d, 71d, 195d, 72d, 195d, 74d);
        path155.curveTo(197d, 74d, 199d, 74d, 200d, 75d);
        path155.curveTo(201d, 77d, 200d, 79d, 200d, 82d);
        path155.curveTo(207d, 81d, 208d, 81d, 207d, 88d);
        path155.curveTo(209d, 88d, 213d, 86d, 213d, 89d);
        path155.curveTo(213d, 92d, 213d, 97d, 212d, 100d);
        path155.curveTo(211d, 100d, 209d, 100d, 207d, 100d);
        path155.lineTo(208d, 101d);
        path155.lineTo(208d, 150d);
        path155.curveTo(214d, 150d, 215d, 152d, 215d, 157d);
        path155.lineTo(214d, 158d);
        path155.curveTo(214d, 159d, 218d, 158d, 219d, 159d);
        path155.curveTo(220d, 159d, 219d, 164d, 220d, 165d);
        path155.curveTo(220d, 167d, 219d, 171d, 219d, 173d);
        path155.curveTo(219d, 179d, 220d, 184d, 220d, 190d);
        path155.lineTo(214d, 190d);
        path155.curveTo(214d, 190d, 214d, 203d, 214d, 203d);
        path155.lineTo(207d, 203d);
        path155.curveTo(207d, 211d, 207d, 208d, 202d, 209d);
        path155.curveTo(199d, 210d, 202d, 215d, 200d, 216d);
        path155.curveTo(199d, 216d, 195d, 216d, 194d, 216d);
        path155.lineTo(194d, 222d);
        path155.curveTo(194d, 222d, 182d, 222d, 182d, 222d);
        path155.curveTo(181d, 222d, 182d, 229d, 181d, 229d);
        path155.lineTo(156d, 229d);
        path155.lineTo(156d, 223d);
        path155.curveTo(156d, 223d, 162d, 223d, 162d, 223d);
        path155.curveTo(162d, 223d, 162d, 222d, 162d, 222d);
        path155.curveTo(161d, 221d, 160d, 217d, 162d, 216d);
        path155.curveTo(162d, 216d, 162d, 215d, 162d, 215d);
        path155.curveTo(166d, 215d, 176d, 215d, 180d, 215d);
        path155.lineTo(180d, 216d);
        path155.curveTo(180d, 214d, 179d, 210d, 181d, 210d);
        path155.lineTo(181d, 211d);
        path155.lineTo(157d, 211d);
        path155.lineTo(156d, 210d);
        path155.curveTo(157d, 211d, 157d, 216d, 156d, 216d);
        path155.curveTo(155d, 217d, 150d, 217d, 150d, 216d);
        path155.curveTo(149d, 216d, 149d, 216d, 149d, 217d);
        path155.curveTo(149d, 220d, 149d, 225d, 149d, 229d);
        path155.lineTo(124d, 229d);
        path155.lineTo(124d, 222d);
        path155.lineTo(111d, 222d);
        path155.curveTo(112d, 215d, 110d, 217d, 104d, 216d);
        path155.curveTo(104d, 216d, 104d, 215d, 104d, 215d);
        path155.curveTo(105d, 211d, 103d, 209d, 99d, 210d);
        path155.lineTo(98d, 209d);
        path155.curveTo(97d, 208d, 97d, 206d, 97d, 204d);
        path155.curveTo(95d, 204d, 93d, 204d, 92d, 203d);
        path155.curveTo(92d, 178d, 92d, 152d, 92d, 127d);
        path155.curveTo(93d, 126d, 98d, 127d, 98d, 126d);
        path155.curveTo(99d, 125d, 98d, 111d, 98d, 108d);
        path155.curveTo(99d, 106d, 105d, 108d, 105d, 106d);
        path155.lineTo(105d, 95d);
        path155.curveTo(106d, 94d, 110d, 95d, 111d, 94d);
        path155.curveTo(112d, 93d, 110d, 89d, 111d, 88d);
        path155.curveTo(112d, 88d, 116d, 88d, 117d, 88d);
        path155.curveTo(118d, 87d, 117d, 83d, 118d, 82d);
        path155.curveTo(119d, 81d, 123d, 83d, 124d, 82d);
        path155.curveTo(125d, 80d, 123d, 77d, 124d, 76d);
        path155.curveTo(126d, 75d, 136d, 76d, 137d, 75d);
        path155.curveTo(137d, 75d, 137d, 74d, 137d, 74d);
        path155.lineTo(138d, 70d);
        path155.curveTo(156d, 69d, 174d, 69d, 193d, 70d);
        path155.lineTo(194d, 71d);
        path155.closePath();
        g2.setColor(new Color(215, 153, 71));
        g2.fill(path155);

        // Element 156
        GeneralPath path156 = new GeneralPath();
        path156.moveTo(155d, 242d);
        path156.curveTo(154d, 249d, 156d, 248d, 162d, 249d);
        path156.lineTo(162d, 254d);
        path156.curveTo(161d, 255d, 156d, 253d, 156d, 255d);
        path156.lineTo(156d, 312d);
        path156.lineTo(150d, 312d);
        path156.curveTo(150d, 319d, 149d, 318d, 143d, 318d);
        path156.lineTo(143d, 324d);
        path156.curveTo(143d, 324d, 141d, 325d, 141d, 325d);
        path156.lineTo(130d, 325d);
        path156.lineTo(130d, 312d);
        path156.curveTo(137d, 313d, 137d, 313d, 137d, 306d);
        path156.lineTo(143d, 306d);
        path156.lineTo(144d, 242d);
        path156.lineTo(155d, 242d);
        path156.closePath();
        g2.setColor(new Color(213, 150, 74));
        g2.fill(path156);

        // Element 157
        GeneralPath path157 = new GeneralPath();
        path157.moveTo(150d, 216d);
        path157.curveTo(150d, 216d, 155d, 216d, 156d, 216d);
        path157.curveTo(156d, 215d, 155d, 210d, 156d, 210d);
        path157.lineTo(181d, 210d);
        path157.curveTo(181d, 211d, 181d, 216d, 180d, 216d);
        path157.lineTo(162d, 216d);
        path157.curveTo(162d, 218d, 162d, 220d, 162d, 222d);
        path157.curveTo(161d, 222d, 156d, 222d, 156d, 223d);
        path157.lineTo(156d, 241d);
        path157.curveTo(156d, 241d, 155d, 241d, 154d, 242d);
        path157.curveTo(152d, 242d, 146d, 241d, 143d, 242d);
        path157.curveTo(143d, 239d, 143d, 237d, 143d, 235d);
        path157.curveTo(144d, 235d, 149d, 236d, 149d, 234d);
        path157.lineTo(150d, 216d);
        path157.closePath();
        g2.setColor(new Color(13, 7, 24));
        g2.fill(path157);

        // Element 158
        GeneralPath path158 = new GeneralPath();
        path158.moveTo(136d, 280d);
        path158.curveTo(137d, 288d, 136d, 297d, 136d, 305d);
        path158.curveTo(135d, 305d, 132d, 306d, 130d, 306d);
        path158.lineTo(130d, 280d);
        path158.curveTo(132d, 280d, 135d, 280d, 136d, 280d);
        path158.closePath();
        g2.setColor(new Color(238, 239, 231));
        g2.fill(path158);

        // Element 159
        GeneralPath path159 = new GeneralPath();
        path159.moveTo(172d, 242d);
        path159.curveTo(175d, 241d, 178d, 241d, 181d, 241d);
        path159.curveTo(181d, 243d, 181d, 245d, 181d, 248d);
        path159.lineTo(156d, 248d);
        path159.curveTo(156d, 248d, 156d, 242d, 156d, 242d);
        path159.curveTo(161d, 242d, 167d, 241d, 172d, 242d);
        path159.closePath();
        g2.setColor(new Color(12, 4, 26));
        g2.fill(path159);

        // Element 160
        GeneralPath path160 = new GeneralPath();
        path160.moveTo(226d, 186d);
        path160.curveTo(226d, 182d, 230d, 184d, 232d, 184d);
        path160.lineTo(232d, 203d);
        path160.lineTo(226d, 203d);
        path160.curveTo(226d, 197d, 226d, 192d, 226d, 186d);
        path160.closePath();
        g2.setColor(new Color(17, 6, 26));
        g2.fill(path160);

        // Element 161
        GeneralPath path161 = new GeneralPath();
        path161.moveTo(226d, 203d);
        path161.curveTo(226d, 204d, 226d, 205d, 226d, 206d);
        path161.curveTo(226d, 209d, 226d, 213d, 226d, 216d);
        path161.curveTo(224d, 216d, 220d, 215d, 220d, 218d);
        path161.curveTo(218d, 218d, 218d, 217d, 219d, 216d);
        path161.curveTo(219d, 216d, 220d, 215d, 220d, 214d);
        path161.curveTo(220d, 212d, 220d, 204d, 220d, 203d);
        path161.curveTo(221d, 202d, 225d, 203d, 226d, 203d);
        path161.curveTo(226d, 198d, 225d, 191d, 226d, 186d);
        path161.curveTo(227d, 187d, 226d, 188d, 227d, 189d);
        path161.curveTo(227d, 193d, 227d, 198d, 227d, 203d);
        path161.closePath();
        g2.setColor(new Color(16, 7, 31));
        g2.fill(path161);

        // Element 162
        GeneralPath path162 = new GeneralPath();
        path162.moveTo(150d, 331d);
        path162.lineTo(150d, 325d);
        path162.curveTo(153d, 326d, 159d, 324d, 161d, 325d);
        path162.curveTo(163d, 325d, 162d, 331d, 161d, 331d);
        path162.lineTo(150d, 331d);
        path162.closePath();
        g2.setColor(new Color(13, 4, 25));
        g2.fill(path162);

        // Element 163
        GeneralPath path163 = new GeneralPath();
        path163.moveTo(220d, 222d);
        path163.lineTo(214d, 222d);
        path163.curveTo(213d, 224d, 214d, 227d, 213d, 228d);
        path163.quadTo(212d, 227d, 213d, 228d);
        path163.curveTo(212d, 230d, 209d, 229d, 207d, 229d);
        path163.curveTo(207d, 230d, 207d, 236d, 206d, 236d);
        path163.lineTo(194d, 236d);
        path163.lineTo(194d, 242d);
        path163.lineTo(182d, 242d);
        path163.curveTo(181d, 243d, 182d, 246d, 181d, 248d);
        path163.lineTo(181d, 242d);
        path163.curveTo(178d, 242d, 175d, 242d, 172d, 242d);
        path163.curveTo(173d, 240d, 181d, 241d, 181d, 241d);
        path163.curveTo(181d, 240d, 180d, 235d, 182d, 235d);
        path163.curveTo(182d, 234d, 183d, 235d, 184d, 235d);
        path163.curveTo(187d, 235d, 191d, 235d, 194d, 235d);
        path163.lineTo(194d, 229d);
        path163.curveTo(194d, 229d, 206d, 229d, 206d, 229d);
        path163.curveTo(206d, 222d, 206d, 222d, 213d, 222d);
        path163.lineTo(214d, 216d);
        path163.lineTo(214d, 217d);
        path163.curveTo(214d, 221d, 214d, 222d, 219d, 221d);
        path163.lineTo(220d, 222d);
        path163.closePath();
        g2.setColor(new Color(86, 52, 50));
        g2.fill(path163);

        // Element 164
        GeneralPath path164 = new GeneralPath();
        path164.moveTo(219d, 216d);
        path164.curveTo(219d, 216d, 220d, 217d, 220d, 218d);
        path164.curveTo(219d, 219d, 220d, 221d, 220d, 222d);
        path164.curveTo(213d, 222d, 213d, 222d, 214d, 216d);
        path164.lineTo(219d, 216d);
        path164.closePath();
        g2.setColor(new Color(12, 3, 17));
        g2.fill(path164);

        // Element 165
        GeneralPath path165 = new GeneralPath();
        path165.moveTo(136d, 305d);
        path165.curveTo(136d, 305d, 136d, 305d, 136d, 305d);
        path165.lineTo(136d, 280d);
        path165.curveTo(137d, 280d, 136d, 302d, 136d, 305d);
        path165.closePath();
        g2.setColor(new Color(213, 150, 74));
        g2.fill(path165);

        // Element 166
        GeneralPath path166 = new GeneralPath();
        path166.moveTo(181d, 267d);
        path166.lineTo(182d, 274d);
        path166.lineTo(194d, 274d);
        path166.curveTo(194d, 277d, 194d, 280d, 194d, 283d);
        path166.curveTo(194d, 284d, 195d, 286d, 194d, 286d);
        path166.lineTo(182d, 286d);
        path166.curveTo(181d, 286d, 182d, 280d, 181d, 280d);
        path166.lineTo(169d, 280d);
        path166.curveTo(169d, 280d, 168d, 279d, 168d, 279d);
        path166.lineTo(168d, 267d);
        path166.lineTo(181d, 267d);
        path166.closePath();
        g2.setColor(new Color(172, 99, 76));
        g2.fill(path166);

        // Element 167
        GeneralPath path167 = new GeneralPath();
        path167.moveTo(188d, 293d);
        path167.curveTo(188d, 292d, 193d, 293d, 194d, 292d);
        path167.curveTo(194d, 302d, 194d, 312d, 194d, 322d);
        path167.curveTo(194d, 323d, 195d, 324d, 195d, 325d);
        path167.curveTo(195d, 329d, 193d, 333d, 194d, 338d);
        path167.lineTo(188d, 338d);
        path167.lineTo(188d, 293d);
        path167.closePath();
        g2.setColor(new Color(192, 113, 84));
        g2.fill(path167);

        // Element 168
        GeneralPath path168 = new GeneralPath();
        path168.moveTo(200d, 338d);
        path168.lineTo(200d, 293d);
        path168.curveTo(200d, 292d, 207d, 292d, 207d, 294d);
        path168.lineTo(207d, 337d);
        path168.curveTo(206d, 338d, 202d, 337d, 200d, 338d);
        path168.closePath();
        g2.setColor(new Color(166, 96, 76));
        g2.fill(path168);

        // Element 169
        GeneralPath path169 = new GeneralPath();
        path169.moveTo(182d, 261d);
        path169.lineTo(200d, 261d);
        path169.curveTo(202d, 260d, 201d, 256d, 201d, 254d);
        path169.lineTo(220d, 254d);
        path169.lineTo(220d, 260d);
        path169.lineTo(207d, 260d);
        path169.curveTo(206d, 260d, 207d, 266d, 206d, 267d);
        path169.curveTo(204d, 267d, 201d, 267d, 198d, 267d);
        path169.curveTo(193d, 267d, 187d, 267d, 182d, 267d);
        path169.lineTo(182d, 261d);
        path169.closePath();
        g2.setColor(new Color(169, 100, 79));
        g2.fill(path169);

        // Element 170
        GeneralPath path170 = new GeneralPath();
        path170.moveTo(168d, 287d);
        path170.curveTo(169d, 285d, 174d, 286d, 175d, 287d);
        path170.curveTo(175d, 288d, 175d, 304d, 175d, 305d);
        path170.curveTo(174d, 306d, 170d, 305d, 168d, 306d);
        path170.lineTo(168d, 287d);
        path170.closePath();
        g2.setColor(new Color(130, 86, 78));
        g2.fill(path170);

        // Element 171
        GeneralPath path171 = new GeneralPath();
        path171.moveTo(136d, 274d);
        path171.lineTo(124d, 274d);
        path171.lineTo(124d, 318d);
        path171.curveTo(124d, 319d, 117d, 319d, 117d, 317d);
        path171.lineTo(117d, 275d);
        path171.curveTo(117d, 272d, 123d, 274d, 124d, 273d);
        path171.lineTo(124d, 267d);
        path171.curveTo(122d, 267d, 118d, 268d, 117d, 267d);
        path171.curveTo(117d, 266d, 117d, 260d, 118d, 260d);
        path171.lineTo(136d, 260d);
        path171.curveTo(136d, 260d, 136d, 261d, 136d, 261d);
        path171.lineTo(136d, 274d);
        path171.closePath();
        g2.setColor(new Color(162, 98, 77));
        g2.fill(path171);

        // Element 172
        GeneralPath path172 = new GeneralPath();
        path172.moveTo(112d, 236d);
        path172.curveTo(112d, 236d, 112d, 235d, 113d, 235d);
        path172.curveTo(115d, 235d, 117d, 234d, 118d, 237d);
        path172.curveTo(118d, 243d, 116d, 241d, 111d, 241d);
        path172.curveTo(111d, 243d, 111d, 247d, 110d, 248d);
        path172.curveTo(110d, 248d, 104d, 247d, 104d, 249d);
        path172.lineTo(104d, 299d);
        path172.curveTo(103d, 299d, 99d, 299d, 98d, 299d);
        path172.curveTo(97d, 300d, 99d, 304d, 98d, 305d);
        path172.curveTo(96d, 306d, 93d, 305d, 92d, 306d);
        path172.curveTo(91d, 307d, 93d, 312d, 91d, 312d);
        path172.lineTo(79d, 312d);
        path172.lineTo(79d, 306d);
        path172.curveTo(86d, 307d, 86d, 306d, 86d, 299d);
        path172.curveTo(88d, 299d, 90d, 300d, 91d, 299d);
        path172.curveTo(92d, 298d, 91d, 293d, 91d, 291d);
        path172.curveTo(91d, 291d, 92d, 290d, 92d, 290d);
        path172.lineTo(92d, 241d);
        path172.curveTo(94d, 241d, 96d, 242d, 98d, 241d);
        path172.curveTo(99d, 241d, 98d, 240d, 98d, 240d);
        path172.curveTo(99d, 239d, 99d, 238d, 99d, 236d);
        path172.curveTo(103d, 236d, 107d, 236d, 111d, 235d);
        path172.closePath();
        g2.setColor(new Color(213, 150, 72));
        g2.fill(path172);

        // Element 173
        GeneralPath path173 = new GeneralPath();
        path173.moveTo(100d, 216d);
        path173.curveTo(102d, 215d, 103d, 215d, 104d, 216d);
        path173.curveTo(105d, 218d, 104d, 228d, 105d, 228d);
        path173.curveTo(106d, 230d, 110d, 229d, 110d, 229d);
        path173.curveTo(111d, 229d, 110d, 234d, 110d, 234d);
        path173.lineTo(98d, 234d);
        path173.lineTo(98d, 216d);
        path173.curveTo(99d, 216d, 100d, 216d, 100d, 216d);
        path173.closePath();
        g2.setColor(new Color(14, 5, 28));
        g2.fill(path173);

        // Element 174
        GeneralPath path174 = new GeneralPath();
        path174.moveTo(104d, 216d);
        path174.curveTo(104d, 216d, 104d, 216d, 104d, 216d);
        path174.curveTo(103d, 216d, 102d, 216d, 100d, 216d);
        path174.curveTo(100d, 215d, 99d, 216d, 98d, 216d);
        path174.curveTo(99d, 213d, 98d, 211d, 98d, 209d);
        path174.curveTo(99d, 209d, 104d, 209d, 104d, 210d);
        path174.curveTo(105d, 211d, 104d, 214d, 104d, 216d);
        path174.closePath();
        g2.setColor(new Color(177, 101, 75));
        g2.fill(path174);

        // Element 175
        GeneralPath path175 = new GeneralPath();
        path175.moveTo(112d, 236d);
        path175.curveTo(111d, 237d, 110d, 236d, 109d, 237d);
        path175.curveTo(108d, 237d, 100d, 236d, 100d, 237d);
        path175.curveTo(99d, 237d, 100d, 239d, 99d, 239d);
        path175.curveTo(99d, 240d, 99d, 240d, 99d, 240d);
        path175.curveTo(99d, 239d, 98d, 236d, 99d, 236d);
        path175.curveTo(99d, 235d, 110d, 236d, 112d, 236d);
        path175.closePath();
        g2.setColor(new Color(204, 159, 109));
        g2.fill(path175);

        // Element 176
        GeneralPath path176 = new GeneralPath();
        path176.moveTo(239d, 107d);
        path176.curveTo(239d, 119d, 239d, 132d, 239d, 144d);
        path176.curveTo(240d, 145d, 239d, 146d, 238d, 145d);
        path176.lineTo(233d, 145d);
        path176.curveTo(232d, 145d, 232d, 135d, 233d, 133d);
        path176.curveTo(233d, 131d, 238d, 131d, 239d, 132d);
        path176.lineTo(238d, 132d);
        path176.lineTo(238d, 108d);
        path176.lineTo(239d, 107d);
        path176.curveTo(238d, 109d, 236d, 109d, 234d, 109d);
        path176.lineTo(234d, 132d);
        path176.curveTo(234d, 132d, 233d, 132d, 233d, 132d);
        path176.curveTo(232d, 128d, 232d, 123d, 233d, 119d);
        path176.curveTo(231d, 118d, 231d, 116d, 233d, 115d);
        path176.curveTo(233d, 114d, 232d, 108d, 233d, 107d);
        path176.curveTo(233d, 107d, 238d, 107d, 239d, 107d);
        path176.closePath();
        g2.setColor(new Color(163, 97, 77));
        g2.fill(path176);

        // Element 177
        GeneralPath path177 = new GeneralPath();
        path177.moveTo(233d, 132d);
        path177.curveTo(233d, 133d, 233d, 133d, 233d, 133d);
        path177.curveTo(233d, 137d, 233d, 141d, 233d, 145d);
        path177.curveTo(235d, 145d, 236d, 145d, 238d, 145d);
        path177.curveTo(238d, 145d, 239d, 144d, 239d, 144d);
        path177.curveTo(239d, 144d, 240d, 145d, 241d, 145d);
        path177.curveTo(242d, 145d, 244d, 145d, 245d, 145d);
        path177.curveTo(245d, 145d, 243d, 145d, 242d, 145d);
        path177.curveTo(242d, 146d, 241d, 147d, 240d, 146d);
        path177.curveTo(241d, 148d, 241d, 150d, 240d, 151d);
        path177.curveTo(239d, 154d, 240d, 156d, 240d, 158d);
        path177.curveTo(238d, 156d, 240d, 146d, 238d, 145d);
        path177.lineTo(232d, 145d);
        path177.curveTo(232d, 146d, 233d, 155d, 233d, 157d);
        path177.curveTo(232d, 158d, 233d, 158d, 232d, 158d);
        path177.curveTo(232d, 154d, 232d, 150d, 232d, 145d);
        path177.lineTo(231d, 144d);
        path177.curveTo(231d, 135d, 231d, 127d, 231d, 119d);
        path177.lineTo(233d, 119d);
        path177.curveTo(232d, 124d, 233d, 128d, 233d, 132d);
        path177.closePath();
        g2.setColor(new Color(123, 78, 76));
        g2.fill(path177);

        // Element 178
        GeneralPath path178 = new GeneralPath();
        path178.moveTo(220d, 280d);
        path178.curveTo(220d, 278d, 220d, 276d, 220d, 274d);
        path178.curveTo(224d, 274d, 229d, 274d, 232d, 274d);
        path178.curveTo(233d, 272d, 233d, 270d, 233d, 267d);
        path178.curveTo(233d, 267d, 238d, 267d, 239d, 268d);
        path178.curveTo(242d, 267d, 245d, 268d, 245d, 271d);
        path178.curveTo(245d, 285d, 245d, 300d, 244d, 313d);
        path178.curveTo(244d, 319d, 245d, 319d, 250d, 319d);
        path178.curveTo(250d, 319d, 251d, 319d, 252d, 319d);
        path178.lineTo(252d, 325d);
        path178.lineTo(233d, 325d);
        path178.lineTo(233d, 280d);
        path178.lineTo(221d, 280d);
        path178.closePath();
        g2.setColor(new Color(8, 6, 28));
        g2.fill(path178);

        // Element 179
        GeneralPath path179 = new GeneralPath();
        path179.moveTo(246d, 318d);
        path179.lineTo(246d, 285d);
        path179.lineTo(248d, 289d);
        path179.curveTo(249d, 289d, 250d, 284d, 251d, 286d);
        path179.curveTo(251d, 294d, 251d, 303d, 251d, 311d);
        path179.curveTo(251d, 312d, 250d, 313d, 251d, 314d);
        path179.curveTo(251d, 315d, 252d, 316d, 251d, 317d);
        path179.curveTo(251d, 318d, 247d, 317d, 246d, 317d);
        path179.closePath();
        g2.setColor(new Color(159, 96, 76));
        g2.fill(path179);

        // Element 180
        GeneralPath path180 = new GeneralPath();
        path180.moveTo(208d, 267d);
        path180.curveTo(207d, 265d, 207d, 263d, 207d, 261d);
        path180.lineTo(220d, 261d);
        path180.lineTo(220d, 267d);
        path180.lineTo(208d, 267d);
        path180.closePath();
        g2.setColor(new Color(11, 4, 22));
        g2.fill(path180);

        // Element 181
        GeneralPath path181 = new GeneralPath();
        path181.moveTo(220d, 280d);
        path181.curveTo(219d, 278d, 220d, 275d, 220d, 274d);
        path181.lineTo(232d, 274d);
        path181.curveTo(233d, 274d, 232d, 268d, 232d, 267d);
        path181.lineTo(245d, 267d);
        path181.curveTo(245d, 271d, 245d, 275d, 245d, 279d);
        path181.curveTo(245d, 285d, 245d, 292d, 246d, 298d);
        path181.curveTo(246d, 304d, 244d, 311d, 245d, 316d);
        path181.curveTo(246d, 320d, 248d, 317d, 251d, 319d);
        path181.curveTo(250d, 319d, 246d, 319d, 245d, 319d);
        path181.curveTo(245d, 319d, 244d, 314d, 244d, 314d);
        path181.curveTo(244d, 312d, 244d, 311d, 245d, 310d);
        path181.curveTo(245d, 297d, 244d, 284d, 244d, 271d);
        path181.curveTo(244d, 271d, 242d, 268d, 242d, 268d);
        path181.curveTo(241d, 268d, 240d, 268d, 239d, 268d);
        path181.curveTo(238d, 268d, 238d, 268d, 237d, 268d);
        path181.curveTo(236d, 267d, 234d, 268d, 233d, 268d);
        path181.curveTo(233d, 269d, 233d, 274d, 232d, 274d);
        path181.lineTo(220d, 274d);
        path181.lineTo(220d, 280d);
        path181.closePath();
        g2.setColor(new Color(14, 9, 30));
        g2.fill(path181);

        // Element 182
        GeneralPath path182 = new GeneralPath();
        path182.moveTo(246d, 268d);
        path182.curveTo(251d, 267d, 252d, 268d, 251d, 273d);
        path182.curveTo(250d, 277d, 249d, 278d, 246d, 280d);
        path182.lineTo(246d, 268d);
        path182.closePath();
        g2.setColor(new Color(159, 96, 76));
        g2.fill(path182);

        // Element 183
        GeneralPath path183 = new GeneralPath();
        path183.moveTo(233d, 324d);
        path183.lineTo(232d, 280d);
        path183.lineTo(233d, 324d);
        path183.closePath();
        g2.setColor(new Color(14, 9, 30));
        g2.fill(path183);

        // Element 184
        GeneralPath path184 = new GeneralPath();
        path184.moveTo(212d, 100d);
        path184.curveTo(213d, 100d, 213d, 100d, 214d, 100d);
        path184.curveTo(213d, 104d, 213d, 108d, 213d, 111d);
        path184.lineTo(213d, 145d);
        path184.curveTo(213d, 146d, 218d, 145d, 220d, 146d);
        path184.curveTo(219d, 147d, 220d, 157d, 220d, 158d);
        path184.curveTo(219d, 159d, 215d, 158d, 214d, 158d);
        path184.curveTo(214d, 151d, 214d, 151d, 207d, 152d);
        path184.lineTo(207d, 101d);
        path184.curveTo(209d, 100d, 211d, 100d, 212d, 101d);
        path184.closePath();
        g2.setColor(new Color(11, 5, 21));
        g2.fill(path184);

        // Element 185
        GeneralPath path185 = new GeneralPath();
        path185.moveTo(226d, 108d);
        path185.lineTo(232d, 107d);
        path185.curveTo(232d, 107d, 231d, 114d, 232d, 115d);
        path185.curveTo(232d, 116d, 233d, 118d, 232d, 119d);
        path185.curveTo(232d, 120d, 232d, 121d, 232d, 122d);
        path185.curveTo(232d, 130d, 232d, 138d, 232d, 146d);
        path185.curveTo(230d, 145d, 226d, 146d, 226d, 144d);
        path185.curveTo(226d, 132d, 226d, 120d, 226d, 108d);
        path185.closePath();
        g2.setColor(new Color(12, 4, 24));
        g2.fill(path185);

        // Element 186
        GeneralPath path186 = new GeneralPath();
        path186.moveTo(241d, 222d);
        path186.curveTo(242d, 222d, 244d, 222d, 245d, 223d);
        path186.curveTo(244d, 226d, 246d, 232d, 245d, 234d);
        path186.lineTo(239d, 234d);
        path186.curveTo(239d, 232d, 238d, 224d, 239d, 223d);
        path186.curveTo(240d, 222d, 241d, 223d, 241d, 222d);
        path186.closePath();
        g2.setColor(new Color(8, 4, 19));
        g2.fill(path186);

        // Element 187
        GeneralPath path187 = new GeneralPath();
        path187.moveTo(220d, 100d);
        path187.curveTo(222d, 101d, 224d, 100d, 226d, 100d);
        path187.curveTo(226d, 103d, 226d, 105d, 226d, 107d);
        path187.lineTo(220d, 107d);
        path187.curveTo(220d, 107d, 220d, 100d, 220d, 100d);
        path187.closePath();
        g2.setColor(new Color(208, 147, 87));
        g2.fill(path187);

        // Element 188
        GeneralPath path188 = new GeneralPath();
        path188.moveTo(246d, 138d);
        path188.lineTo(246d, 114d);
        path188.curveTo(246d, 113d, 252d, 114d, 252d, 115d);
        path188.lineTo(252d, 138d);
        path188.lineTo(246d, 138d);
        path188.closePath();
        g2.setColor(new Color(210, 147, 77));
        g2.fill(path188);

        // Element 189
        GeneralPath path189 = new GeneralPath();
        path189.moveTo(258d, 108d);
        path189.curveTo(259d, 114d, 258d, 114d, 252d, 114d);
        path189.lineTo(252d, 108d);
        path189.curveTo(252d, 108d, 258d, 108d, 258d, 108d);
        path189.closePath();
        g2.setColor(new Color(207, 143, 82));
        g2.fill(path189);

        // Element 190
        GeneralPath path190 = new GeneralPath();
        path190.moveTo(80d, 190d);
        path190.curveTo(81d, 190d, 84d, 191d, 86d, 190d);
        path190.lineTo(86d, 145d);
        path190.lineTo(80d, 145d);
        path190.curveTo(80d, 145d, 80d, 139d, 80d, 139d);
        path190.curveTo(80d, 139d, 86d, 140d, 86d, 138d);
        path190.lineTo(86d, 126d);
        path190.lineTo(91d, 126d);
        path190.curveTo(91d, 126d, 92d, 135d, 92d, 135d);
        path190.lineTo(92d, 209d);
        path190.lineTo(86d, 209d);
        path190.curveTo(86d, 209d, 84d, 208d, 84d, 208d);
        path190.curveTo(85d, 205d, 85d, 201d, 84d, 198d);
        path190.lineTo(80d, 198d);
        path190.lineTo(80d, 196d);
        path190.curveTo(80d, 195d, 79d, 192d, 80d, 190d);
        path190.closePath();
        g2.setColor(new Color(9, 4, 22));
        g2.fill(path190);

        // Element 191
        GeneralPath path191 = new GeneralPath();
        path191.moveTo(80d, 196d);
        path191.curveTo(81d, 197d, 85d, 196d, 85d, 197d);
        path191.curveTo(86d, 199d, 85d, 206d, 86d, 209d);
        path191.curveTo(86d, 211d, 90d, 209d, 91d, 210d);
        path191.curveTo(92d, 212d, 91d, 216d, 92d, 216d);
        path191.curveTo(93d, 216d, 98d, 216d, 98d, 216d);
        path191.curveTo(98d, 218d, 98d, 220d, 98d, 222d);
        path191.lineTo(98d, 223d);
        path191.curveTo(97d, 224d, 86d, 224d, 85d, 223d);
        path191.lineTo(85d, 222d);
        path191.curveTo(85d, 217d, 84d, 216d, 80d, 217d);
        path191.curveTo(79d, 214d, 79d, 211d, 79d, 208d);
        path191.curveTo(79d, 204d, 79d, 200d, 79d, 196d);
        path191.closePath();
        g2.setColor(new Color(203, 144, 74));
        g2.fill(path191);

        // Element 192
        GeneralPath path192 = new GeneralPath();
        path192.moveTo(98d, 107d);
        path192.lineTo(98d, 125d);
        path192.curveTo(98d, 126d, 93d, 126d, 92d, 126d);
        path192.lineTo(92d, 107d);
        path192.lineTo(98d, 107d);
        path192.closePath();
        g2.setColor(new Color(9, 4, 21));
        g2.fill(path192);

        // Element 193
        GeneralPath path193 = new GeneralPath();
        path193.moveTo(104d, 94d);
        path193.curveTo(105d, 98d, 105d, 102d, 104d, 106d);
        path193.lineTo(98d, 106d);
        path193.curveTo(98d, 106d, 98d, 95d, 98d, 95d);
        path193.curveTo(98d, 94d, 103d, 94d, 104d, 94d);
        path193.closePath();
        g2.setColor(new Color(13, 7, 21));
        g2.fill(path193);

        // Element 194
        GeneralPath path194 = new GeneralPath();
        path194.moveTo(92d, 203d);
        path194.lineTo(98d, 203d);
        path194.curveTo(98d, 205d, 98d, 207d, 98d, 209d);
        path194.curveTo(96d, 209d, 94d, 209d, 92d, 209d);
        path194.curveTo(92d, 207d, 92d, 205d, 92d, 203d);
        path194.closePath();
        g2.setColor(new Color(174, 99, 69));
        g2.fill(path194);

        // Element 195
        GeneralPath path195 = new GeneralPath();
        path195.moveTo(98d, 216d);
        path195.curveTo(96d, 215d, 94d, 216d, 92d, 216d);
        path195.lineTo(92d, 210d);
        path195.curveTo(98d, 209d, 98d, 209d, 98d, 216d);
        path195.closePath();
        g2.setColor(new Color(13, 5, 15));
        g2.fill(path195);

        // Element 196
        GeneralPath path196 = new GeneralPath();
        path196.moveTo(220d, 254d);
        path196.lineTo(200d, 254d);
        path196.curveTo(202d, 252d, 200d, 250d, 201d, 249d);
        path196.curveTo(201d, 247d, 211d, 249d, 213d, 249d);
        path196.curveTo(218d, 248d, 220d, 246d, 219d, 254d);
        path196.closePath();
        g2.setColor(new Color(10, 5, 29));
        g2.fill(path196);

        // Element 197
        GeneralPath path197 = new GeneralPath();
        path197.moveTo(232d, 254d);
        path197.lineTo(232d, 260d);
        path197.lineTo(221d, 260d);
        path197.curveTo(220d, 260d, 220d, 256d, 220d, 254d);
        path197.lineTo(232d, 254d);
        path197.closePath();
        g2.setColor(new Color(16, 5, 29));
        g2.fill(path197);

        // Element 198
        GeneralPath path198 = new GeneralPath();
        path198.moveTo(220d, 242d);
        path198.lineTo(232d, 242d);
        path198.curveTo(232d, 242d, 232d, 248d, 232d, 248d);
        path198.lineTo(221d, 248d);
        path198.curveTo(220d, 248d, 220d, 243d, 220d, 242d);
        path198.closePath();
        g2.setColor(new Color(9, 4, 23));
        g2.fill(path198);

        // Element 199
        GeneralPath path199 = new GeneralPath();
        path199.moveTo(220d, 254d);
        path199.lineTo(220d, 248d);
        path199.curveTo(222d, 248d, 231d, 248d, 232d, 249d);
        path199.curveTo(232d, 249d, 232d, 253d, 232d, 254d);
        path199.lineTo(220d, 254d);
        path199.closePath();
        g2.setColor(new Color(172, 101, 81));
        g2.fill(path199);

        // Element 200
        Shape shape200 = new Rectangle2D.Double(233d, 242d, 6d, 6d);
        g2.setColor(new Color(160, 99, 83));
        g2.fill(shape200);

        // Element 201
        GeneralPath path201 = new GeneralPath();
        path201.moveTo(238d, 236d);
        path201.curveTo(239d, 241d, 239d, 241d, 233d, 241d);
        path201.lineTo(233d, 236d);
        path201.curveTo(233d, 236d, 238d, 236d, 238d, 236d);
        path201.closePath();
        g2.setColor(new Color(16, 5, 30));
        g2.fill(path201);

        // Element 202
        GeneralPath path202 = new GeneralPath();
        path202.moveTo(79d, 208d);
        path202.curveTo(80d, 209d, 79d, 216d, 80d, 216d);
        path202.curveTo(87d, 215d, 85d, 216d, 86d, 222d);
        path202.curveTo(86d, 223d, 95d, 223d, 96d, 223d);
        path202.curveTo(97d, 222d, 98d, 222d, 98d, 222d);
        path202.curveTo(98d, 226d, 98d, 230d, 98d, 234d);
        path202.lineTo(86d, 234d);
        path202.curveTo(85d, 234d, 86d, 230d, 85d, 229d);
        path202.curveTo(84d, 228d, 81d, 229d, 79d, 228d);
        path202.curveTo(80d, 222d, 79d, 215d, 79d, 208d);
        path202.closePath();
        g2.setColor(new Color(182, 104, 73));
        g2.fill(path202);

        // Element 203
        Shape shape203 = new Rectangle2D.Double(80d, 229d, 6d, 6d);
        g2.setColor(new Color(15, 7, 29));
        g2.fill(shape203);

        // Element 204
        GeneralPath path204 = new GeneralPath();
        path204.moveTo(242d, 146d);
        path204.curveTo(247d, 147d, 243d, 150d, 243d, 151d);
        path204.curveTo(243d, 152d, 246d, 153d, 247d, 154d);
        path204.curveTo(248d, 154d, 249d, 153d, 250d, 152d);
        path204.curveTo(252d, 152d, 255d, 153d, 258d, 152d);
        path204.lineTo(258d, 158d);
        path204.lineTo(241d, 158d);
        path204.curveTo(240d, 158d, 241d, 152d, 240d, 152d);
        path204.curveTo(240d, 150d, 239d, 147d, 240d, 146d);
        path204.curveTo(240d, 146d, 242d, 146d, 242d, 146d);
        path204.closePath();
        g2.setColor(new Color(173, 99, 79));
        g2.fill(path204);

        // Element 205
        GeneralPath path205 = new GeneralPath();
        path205.moveTo(258d, 94d);
        path205.curveTo(258d, 96d, 258d, 98d, 258d, 100d);
        path205.curveTo(258d, 100d, 248d, 99d, 246d, 100d);
        path205.curveTo(244d, 101d, 246d, 105d, 245d, 106d);
        path205.curveTo(244d, 107d, 241d, 106d, 240d, 106d);
        path205.lineTo(240d, 94d);
        path205.lineTo(258d, 94d);
        path205.closePath();
        g2.setColor(new Color(210, 149, 82));
        g2.fill(path205);

        // Element 206
        GeneralPath path206 = new GeneralPath();
        path206.moveTo(134d, 68d);
        path206.curveTo(131d, 69d, 127d, 68d, 124d, 68d);
        path206.curveTo(125d, 68d, 132d, 68d, 134d, 68d);
        path206.closePath();
        g2.setColor(new Color(215, 153, 71));
        g2.fill(path206);

        // Element 207
        GeneralPath path207 = new GeneralPath();
        path207.moveTo(232d, 56d);
        path207.curveTo(228d, 55d, 226d, 56d, 226d, 50d);
        path207.curveTo(234d, 49d, 232d, 50d, 232d, 56d);
        path207.closePath();
        g2.setColor(new Color(174, 101, 74));
        g2.fill(path207);

        // Element 208
        GeneralPath path208 = new GeneralPath();
        path208.moveTo(200d, 62d);
        path208.lineTo(200d, 68d);
        path208.lineTo(137d, 68d);
        path208.curveTo(136d, 68d, 136d, 62d, 137d, 62d);
        path208.lineTo(200d, 62d);
        path208.closePath();
        g2.setColor(new Color(15, 10, 29));
        g2.fill(path208);

        // Element 209
        GeneralPath path209 = new GeneralPath();
        path209.moveTo(212d, 69d);
        path209.curveTo(213d, 71d, 213d, 73d, 212d, 74d);
        path209.curveTo(209d, 75d, 205d, 75d, 201d, 74d);
        path209.curveTo(201d, 73d, 201d, 71d, 201d, 69d);
        path209.lineTo(212d, 69d);
        path209.closePath();
        g2.setColor(new Color(13, 7, 26));
        g2.fill(path209);

        // Element 210
        GeneralPath path210 = new GeneralPath();
        path210.moveTo(136d, 69d);
        path210.curveTo(136d, 71d, 136d, 73d, 136d, 74d);
        path210.curveTo(132d, 75d, 128d, 75d, 124d, 74d);
        path210.curveTo(124d, 73d, 124d, 71d, 124d, 69d);
        path210.lineTo(136d, 69d);
        path210.closePath();
        g2.setColor(new Color(8, 4, 21));
        g2.fill(path210);
        
        // Element 211
        GeneralPath path211 = new GeneralPath();
        path211.moveTo(194d, 69d);
        path211.curveTo(195d, 69d, 195d, 70d, 194d, 70d);
        path211.curveTo(184d, 69d, 173d, 69d, 162d, 69d);
        path211.curveTo(161d, 69d, 160d, 70d, 158d, 70d);
        path211.curveTo(152d, 70d, 145d, 69d, 139d, 70d);
        path211.curveTo(138d, 70d, 137d, 73d, 137d, 74d);
        path211.lineTo(137d, 69d);
        path211.curveTo(137d, 69d, 194d, 69d, 194d, 69d);
        path211.closePath();
        g2.setColor(new Color(207, 161, 109));
        g2.fill(path211);

        // Element 212
        GeneralPath path212 = new GeneralPath();
        path212.moveTo(194d, 69d);
        path212.curveTo(196d, 68d, 195d, 70d, 196d, 71d);
        path212.curveTo(198d, 72d, 199d, 73d, 200d, 75d);
        path212.curveTo(194d, 75d, 194d, 76d, 194d, 70d);
        path212.curveTo(194d, 70d, 194d, 70d, 194d, 69d);
        path212.closePath();
        g2.setColor(new Color(182, 111, 76));
        g2.fill(path212);

        // Element 213
        GeneralPath path213 = new GeneralPath();
        path213.moveTo(201d, 69d);
        path213.lineTo(201d, 74d);
        path213.lineTo(212d, 74d);
        path213.lineTo(212d, 69d);
        path213.curveTo(214d, 71d, 213d, 73d, 213d, 75d);
        path213.lineTo(201d, 75d);
        path213.curveTo(200d, 75d, 200d, 70d, 201d, 69d);
        path213.closePath();
        g2.setColor(new Color(15, 10, 29));
        g2.fill(path213);

        // Element 214
        GeneralPath path214 = new GeneralPath();
        path214.moveTo(124d, 69d);
        path214.lineTo(124d, 74d);
        path214.lineTo(136d, 74d);
        path214.lineTo(136d, 69d);
        path214.curveTo(137d, 70d, 137d, 75d, 136d, 75d);
        path214.lineTo(125d, 75d);
        path214.curveTo(124d, 75d, 124d, 70d, 124d, 69d);
        path214.closePath();
        g2.setColor(new Color(15, 10, 29));
        g2.fill(path214);

        // Element 215
        GeneralPath path215 = new GeneralPath();
        path215.moveTo(104d, 56d);
        path215.curveTo(104d, 58d, 104d, 60d, 104d, 62d);
        path215.curveTo(104d, 62d, 103d, 62d, 102d, 64d);
        path215.curveTo(102d, 65d, 105d, 68d, 102d, 67d);
        path215.curveTo(102d, 67d, 102d, 66d, 102d, 65d);
        path215.curveTo(101d, 64d, 99d, 63d, 98d, 63d);
        path215.curveTo(98d, 61d, 99d, 58d, 98d, 56d);
        path215.lineTo(104d, 56d);
        path215.closePath();
        g2.setColor(new Color(187, 120, 81));
        g2.fill(path215);

        // Element 216
        GeneralPath path216 = new GeneralPath();
        path216.moveTo(110d, 44d);
        path216.curveTo(112d, 44d, 112d, 47d, 111d, 48d);
        path216.curveTo(111d, 49d, 111d, 49d, 110d, 50d);
        path216.curveTo(110d, 51d, 105d, 51d, 105d, 50d);
        path216.curveTo(105d, 48d, 105d, 45d, 105d, 44d);
        path216.curveTo(105d, 43d, 110d, 44d, 110d, 44d);
        path216.closePath();
        g2.setColor(new Color(211, 148, 92));
        g2.fill(path216);

        // Element 217
        GeneralPath path217 = new GeneralPath();
        path217.moveTo(117d, 37d);
        path217.curveTo(118d, 38d, 117d, 40d, 118d, 42d);
        path217.curveTo(117d, 42d, 117d, 42d, 117d, 42d);
        path217.curveTo(115d, 44d, 113d, 44d, 112d, 42d);
        path217.lineTo(112d, 37d);
        path217.curveTo(113d, 37d, 115d, 37d, 117d, 37d);
        path217.closePath();
        g2.setColor(new Color(210, 146, 80));
        g2.fill(path217);

        // Element 218
        GeneralPath path218 = new GeneralPath();
        path218.moveTo(110d, 50d);
        path218.curveTo(110d, 50d, 111d, 55d, 110d, 55d);
        path218.lineTo(105d, 55d);
        path218.curveTo(105d, 53d, 105d, 51d, 105d, 50d);
        path218.lineTo(110d, 50d);
        path218.closePath();
        g2.setColor(new Color(171, 106, 84));
        g2.fill(path218);

        // Element 219
        GeneralPath path219 = new GeneralPath();
        path219.moveTo(117d, 42d);
        path219.curveTo(115d, 45d, 114d, 47d, 111d, 48d);
        path219.curveTo(111d, 47d, 111d, 45d, 110d, 44d);
        path219.curveTo(111d, 43d, 111d, 43d, 112d, 42d);
        path219.curveTo(113d, 42d, 116d, 43d, 117d, 42d);
        path219.closePath();
        g2.setColor(new Color(162, 97, 71));
        g2.fill(path219);

        // Element 220
        GeneralPath path220 = new GeneralPath();
        path220.moveTo(240d, 68d);
        path220.curveTo(240d, 69d, 245d, 69d, 245d, 69d);
        path220.curveTo(245d, 69d, 246d, 80d, 246d, 82d);
        path220.curveTo(245d, 87d, 245d, 88d, 240d, 88d);
        path220.lineTo(240d, 69d);
        path220.closePath();
        g2.setColor(new Color(216, 151, 80));
        g2.fill(path220);

        // Element 221
        GeneralPath path221 = new GeneralPath();
        path221.moveTo(226d, 56d);
        path221.curveTo(221d, 56d, 219d, 57d, 220d, 51d);
        path221.curveTo(220d, 48d, 222d, 49d, 224d, 49d);
        path221.curveTo(227d, 49d, 226d, 53d, 226d, 55d);
        path221.closePath();
        g2.setColor(new Color(15, 5, 25));
        g2.fill(path221);

        // Element 222
        GeneralPath path222 = new GeneralPath();
        path222.moveTo(212d, 100d);
        path222.lineTo(212d, 100d);
        path222.curveTo(211d, 101d, 209d, 100d, 207d, 100d);
        path222.curveTo(208d, 99d, 211d, 100d, 212d, 100d);
        path222.closePath();
        g2.setColor(new Color(177, 102, 78));
        g2.fill(path222);

        // Element 223
        GeneralPath path223 = new GeneralPath();
        path223.moveTo(143d, 82d);
        path223.lineTo(143d, 94d);
        path223.curveTo(142d, 95d, 137d, 94d, 136d, 94d);
        path223.curveTo(136d, 95d, 137d, 99d, 136d, 100d);
        path223.curveTo(135d, 101d, 131d, 100d, 130d, 101d);
        path223.curveTo(130d, 102d, 130d, 106d, 130d, 107d);
        path223.lineTo(118d, 107d);
        path223.curveTo(118d, 104d, 117d, 101d, 117d, 98d);
        path223.curveTo(117d, 93d, 119d, 95d, 124d, 95d);
        path223.curveTo(123d, 88d, 123d, 87d, 130d, 88d);
        path223.curveTo(130d, 86d, 130d, 82d, 131d, 82d);
        path223.lineTo(143d, 82d);
        path223.closePath();
        g2.setColor(new Color(232, 198, 131));
        g2.fill(path223);

        // Element 224
        GeneralPath path224 = new GeneralPath();
        path224.moveTo(200d, 196d);
        path224.curveTo(201d, 192d, 200d, 188d, 200d, 184d);
        path224.curveTo(202d, 184d, 206d, 184d, 207d, 184d);
        path224.curveTo(207d, 185d, 207d, 195d, 207d, 196d);
        path224.curveTo(206d, 197d, 202d, 196d, 200d, 196d);
        path224.closePath();
        g2.setColor(new Color(12, 3, 15));
        g2.fill(path224);

        // Element 225
        Shape shape225 = new Rectangle2D.Double(207d, 171d, 6d, 12d);
        g2.setColor(new Color(12, 5, 20));
        g2.fill(shape225);

        // Element 226
        GeneralPath path226 = new GeneralPath();
        path226.moveTo(200d, 196d);
        path226.curveTo(200d, 199d, 201d, 201d, 200d, 203d);
        path226.curveTo(198d, 203d, 196d, 203d, 194d, 203d);
        path226.curveTo(194d, 202d, 194d, 197d, 194d, 197d);
        path226.curveTo(195d, 196d, 199d, 197d, 200d, 196d);
        path226.closePath();
        g2.setColor(new Color(19, 11, 15));
        g2.fill(path226);

        // Element 227
        GeneralPath path227 = new GeneralPath();
        path227.moveTo(206d, 151d);
        path227.lineTo(206d, 146d);
        path227.curveTo(205d, 133d, 206d, 120d, 206d, 107d);
        path227.curveTo(206d, 105d, 205d, 102d, 206d, 101d);
        path227.lineTo(206d, 151d);
        path227.closePath();
        g2.setColor(new Color(204, 159, 110));
        g2.fill(path227);

        // Element 228
        GeneralPath path228 = new GeneralPath();
        path228.moveTo(194d, 203d);
        path228.curveTo(194d, 205d, 194d, 208d, 194d, 209d);
        path228.curveTo(192d, 209d, 192d, 204d, 194d, 204d);
        path228.lineTo(193d, 204d);
        path228.lineTo(184d, 204d);
        path228.lineTo(184d, 204d);
        path228.curveTo(185d, 204d, 184d, 207d, 183d, 208d);
        path228.lineTo(193d, 208d);
        path228.curveTo(193d, 208d, 193d, 209d, 193d, 209d);
        path228.curveTo(193d, 209d, 193d, 209d, 192d, 210d);
        path228.curveTo(191d, 210d, 183d, 210d, 182d, 209d);
        path228.curveTo(181d, 209d, 181d, 204d, 181d, 203d);
        path228.curveTo(182d, 203d, 192d, 203d, 194d, 203d);
        path228.closePath();
        g2.setColor(new Color(13, 7, 24));
        g2.fill(path228);

        // Element 229
        Shape shape229 = new Rectangle2D.Double(182d, 236d, 12d, 6d);
        g2.setColor(new Color(13, 3, 24));
        g2.fill(shape229);

        // Element 230
        Shape shape230 = new Rectangle2D.Double(194d, 229d, 12d, 6d);
        g2.setColor(new Color(11, 3, 24));
        g2.fill(shape230);

        // Element 231
        GeneralPath path231 = new GeneralPath();
        path231.moveTo(213d, 228d);
        path231.lineTo(207d, 228d);
        path231.curveTo(207d, 226d, 206d, 223d, 209d, 222d);
        path231.curveTo(214d, 222d, 213d, 224d, 213d, 228d);
        path231.closePath();
        g2.setColor(new Color(18, 5, 26));
        g2.fill(path231);

        // Element 232
        GeneralPath path232 = new GeneralPath();
        path232.moveTo(123d, 274d);
        path232.curveTo(123d, 275d, 123d, 277d, 123d, 278d);
        path232.curveTo(123d, 278d, 122d, 278d, 122d, 278d);
        path232.curveTo(121d, 281d, 123d, 282d, 123d, 284d);
        path232.curveTo(123d, 287d, 123d, 297d, 123d, 300d);
        path232.curveTo(123d, 301d, 122d, 302d, 122d, 303d);
        path232.curveTo(122d, 304d, 123d, 305d, 123d, 306d);
        path232.curveTo(124d, 310d, 124d, 318d, 118d, 318d);
        path232.curveTo(118d, 314d, 117d, 308d, 118d, 304d);
        path232.curveTo(118d, 303d, 120d, 303d, 119d, 301d);
        path232.curveTo(119d, 299d, 118d, 299d, 118d, 298d);
        path232.curveTo(117d, 291d, 119d, 282d, 118d, 274d);
        path232.lineTo(123d, 274d);
        path232.closePath();
        g2.setColor(new Color(168, 99, 81));
        g2.fill(path232);

        // Element 233
        GeneralPath path233 = new GeneralPath();
        path233.moveTo(233d, 132d);
        path233.lineTo(233d, 108d);
        path233.curveTo(233d, 108d, 238d, 108d, 238d, 108d);
        path233.lineTo(238d, 132d);
        path233.curveTo(238d, 133d, 234d, 133d, 233d, 133d);
        path233.curveTo(233d, 133d, 233d, 133d, 233d, 132d);
        path233.closePath();
        g2.setColor(new Color(208, 150, 84));
        g2.fill(path233);

        // Element 234
        GeneralPath path234 = new GeneralPath();
        path234.moveTo(194d, 209d);
        path234.lineTo(182d, 209d);
        path234.curveTo(182d, 208d, 183d, 206d, 183d, 206d);
        path234.curveTo(184d, 206d, 183d, 204d, 184d, 204d);
        path234.lineTo(194d, 204d);
        path234.lineTo(194d, 209d);
        path234.closePath();
        g2.setColor(new Color(10, 6, 24));
        g2.fill(path234);

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Final Project Alvear - Animated");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new Finalproject_alvear());
            frame.pack(); // Sizes the frame based on the panel's preferred size
            frame.setMinimumSize(new Dimension(400, 350));
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}