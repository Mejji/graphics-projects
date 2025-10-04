/*

    created by Mark Josh Alvear.

*/
package alvearmj_projectprelims;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class AlvearMj_ProjectPrelims extends JPanel {
  /** Virtual canvas size from SVG: */
  private static final int BASE_W = 422;
  private static final int BASE_H = 447;

  private boolean drawOutlines = true;
  private boolean drawShading  = true;
  private BufferedImage cache;       // cached rendering at current size/toggles
  private int cacheW = -1, cacheH = -1;
  private boolean dirty = true;

  public void setDrawOutlines(boolean v){ drawOutlines = v; dirty = true; repaint(); }
  public void setDrawShading (boolean v){ drawShading  = v; dirty = true; repaint(); }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setPaint(Color.WHITE);
    g2.fillRect(0,0,getWidth(),getHeight());

    // Cache invalidation
    if (cache == null || cacheW != getWidth() || cacheH != getHeight() || dirty) {
      cacheW = getWidth(); cacheH = getHeight();
      cache = new BufferedImage(cacheW, cacheH, BufferedImage.TYPE_INT_ARGB);
      Graphics2D cg = cache.createGraphics();
      cg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      cg.setPaint(Color.WHITE);
      cg.fillRect(0,0,cacheW,cacheH);

      // --- Virtual canvas scale & center ---
      double sx = cacheW / (double) BASE_W;
      double sy = cacheH / (double) BASE_H;
      double s = Math.min(sx, sy);
      double ox = (cacheW - BASE_W * s) * 0.5;
      double oy = (cacheH - BASE_H * s) * 0.5;
      AffineTransform old = cg.getTransform();
      cg.translate(ox, oy);
      cg.scale(s, s);

      // ---- Composition using painters with local transforms ----
      paintScene(cg);

      cg.setTransform(old);
      cg.dispose();
      dirty = false;
    }

    g2.drawImage(cache, 0, 0, null);
  }

  /** Place/scale/rotate parts using local transforms to demonstrate AffineTransform usage. */
  private void paintScene(Graphics2D g2){
    // You can tweak these transforms to reposition parts without touching path coordinates.
    FacePainter  face  = new FacePainter();
    HairPainter  hair  = new HairPainter();
    HandPainter  hand  = new HandPainter();
    PlushPainter plush = new PlushPainter();
    MiscPainter  misc  = new MiscPainter();

    // Example placements (local coordinates). Reset transform per painter.
    AffineTransform at;

    at = new AffineTransform();
    at.translate(0, 0);
    face.paint(g2, at, drawOutlines, drawShading);

    at = new AffineTransform();
    at.translate(0, 0);
    hair.paint(g2, at, drawOutlines, drawShading);

    at = new AffineTransform();
    at.translate(0, 0);
    hand.paint(g2, at, drawOutlines, drawShading);

    at = new AffineTransform();
    at.translate(0, 0);
    plush.paint(g2, at, drawOutlines, drawShading);

    at = new AffineTransform();
    at.translate(0, 0);
    misc.paint(g2, at, drawOutlines, drawShading);

    // Soft ground shadow (blur-like pass via multiple translucent ellipses)
    if (drawShading) drawSoftShadow(g2);
  }

  /** Simple soft shadow under the whole composition. */
  private void drawSoftShadow(Graphics2D g2){
    Composite old = g2.getComposite();
    g2.setPaint(new Color(0,0,0,40));
    for(int i=0;i<8;i++){
      double exp = 1.0 + i*0.06;
      Shape e = new Ellipse2D.Double(BASE_W*0.15*exp, BASE_H*0.82*exp, BASE_W*0.7/exp, BASE_H*0.10/exp);
      g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.10f));
      g2.fill(e);
    }
    g2.setComposite(old);
  }

  /** Painter for face elements. */
  static class FacePainter {
    /**
     * Paints this part using the provided Graphics2D and local transform.
     * @param g Graphics2D
     * @param at Local placement transform (translate/scale/rotate)
     * @param drawOutlines Toggle stroke drawing
     * @param drawShading  Toggle highlight overlays
     */
    void paint(Graphics2D g, AffineTransform at, boolean drawOutlines, boolean drawShading){
      Graphics2D g2 = (Graphics2D) g.create();
      g2.transform(at);
      // (no elements classified here)
      g2.dispose();
    }
  }

  /** Painter for hair elements. */
  static class HairPainter {
    /**
     * Paints this part using the provided Graphics2D and local transform.
     * @param g Graphics2D
     * @param at Local placement transform (translate/scale/rotate)
     * @param drawOutlines Toggle stroke drawing
     * @param drawShading  Toggle highlight overlays
     */
    void paint(Graphics2D g, AffineTransform at, boolean drawOutlines, boolean drawShading){
      Graphics2D g2 = (Graphics2D) g.create();
      g2.transform(at);
      // (no elements classified here)
      g2.dispose();
    }
  }

  /** Painter for hand elements. */
  static class HandPainter {
    /**
     * Paints this part using the provided Graphics2D and local transform.
     * @param g Graphics2D
     * @param at Local placement transform (translate/scale/rotate)
     * @param drawOutlines Toggle stroke drawing
     * @param drawShading  Toggle highlight overlays
     */
    void paint(Graphics2D g, AffineTransform at, boolean drawOutlines, boolean drawShading){
      Graphics2D g2 = (Graphics2D) g.create();
      g2.transform(at);
      // (no elements classified here)
      g2.dispose();
    }
  }

  /** Painter for plush elements. */
  static class PlushPainter {
    /**
     * Paints this part using the provided Graphics2D and local transform.
     * @param g Graphics2D
     * @param at Local placement transform (translate/scale/rotate)
     * @param drawOutlines Toggle stroke drawing
     * @param drawShading  Toggle highlight overlays
     */
    void paint(Graphics2D g, AffineTransform at, boolean drawOutlines, boolean drawShading){
      Graphics2D g2 = (Graphics2D) g.create();
      g2.transform(at);
      // (no elements classified here)
      g2.dispose();
    }
  }

  /** Painter for misc elements. */
  static class MiscPainter {
    /**
     * Paints this part using the provided Graphics2D and local transform.
     * @param g Graphics2D
     * @param at Local placement transform (translate/scale/rotate)
     * @param drawOutlines Toggle stroke drawing
     * @param drawShading  Toggle highlight overlays
     */
    void paint(Graphics2D g, AffineTransform at, boolean drawOutlines, boolean drawShading){
      Graphics2D g2 = (Graphics2D) g.create();
      g2.transform(at);
      // Element 0: Path
      GeneralPath path0 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path0.moveTo(95.5d, 447.0d);
      path0.lineTo(0.0d, 447.0d);
      path0.lineTo(0.0d, 0.0d);
      path0.lineTo(422.5d, 0.0d);
      path0.lineTo(422.5d, 447.0d);
      path0.lineTo(305.0d, 447.0d);
      path0.curveTo(306.2d, 439.0d, 308.7d, 430.8d, 309.5d, 422.7d);
      path0.curveTo(309.6d, 421.3d, 310.0d, 419.0d, 309.9d, 417.8d);
      path0.curveTo(309.9d, 411.7d, 307.1d, 409.2d, 310.0d, 403.0d);
      path0.curveTo(310.4d, 402.8d, 311.0d, 403.0d, 311.5d, 402.3d);
      path0.curveTo(312.1d, 401.6d, 311.8d, 400.9d, 312.0d, 400.5d);
      path0.curveTo(314.1d, 399.2d, 313.0d, 399.7d, 314.0d, 398.0d);
      path0.curveTo(316.5d, 396.5d, 317.4d, 395.1d, 318.5d, 392.5d);
      path0.curveTo(320.4d, 391.2d, 319.6d, 391.4d, 320.5d, 390.5d);
      path0.curveTo(320.7d, 390.3d, 320.8d, 390.2d, 321.0d, 390.0d);
      path0.curveTo(322.6d, 388.4d, 328.0d, 389.6d, 330.8d, 389.5d);
      path0.curveTo(343.1d, 389.0d, 362.4d, 381.2d, 367.6d, 369.3d);
      path0.curveTo(368.8d, 366.6d, 373.5d, 353.0d, 373.5d, 350.7d);
      path0.curveTo(373.5d, 349.4d, 372.7d, 347.9d, 373.1d, 346.8d);
      path0.curveTo(373.2d, 346.3d, 376.2d, 344.4d, 377.1d, 342.9d);
      path0.curveTo(383.2d, 332.1d, 367.6d, 326.8d, 364.6d, 318.6d);
      path0.curveTo(363.8d, 309.7d, 362.0d, 300.6d, 361.5d, 291.7d);
      path0.curveTo(360.8d, 279.9d, 361.6d, 273.5d, 358.1d, 261.6d);
      path0.curveTo(354.1d, 247.8d, 343.8d, 213.6d, 335.5d, 203.3d);
      path0.curveTo(325.6d, 190.9d, 305.0d, 187.5d, 291.1d, 181.4d);
      path0.curveTo(291.9d, 174.9d, 294.4d, 168.6d, 286.5d, 165.8d);
      path0.curveTo(285.1d, 165.2d, 279.6d, 164.0d, 278.2d, 164.0d);
      path0.lineTo(266.5d, 164.0d);
      path0.curveTo(268.3d, 154.2d, 258.9d, 152.4d, 251.6d, 150.6d);
      path0.curveTo(250.5d, 150.3d, 248.7d, 151.1d, 249.0d, 149.5d);
      path0.curveTo(250.1d, 148.3d, 249.8d, 146.5d, 250.0d, 145.0d);
      path0.curveTo(250.2d, 143.3d, 250.6d, 141.7d, 250.5d, 140.0d);
      path0.curveTo(254.9d, 140.8d, 256.6d, 138.4d, 258.9d, 135.2d);
      path0.curveTo(261.7d, 131.3d, 265.0d, 124.6d, 263.4d, 119.9d);
      path0.curveTo(263.2d, 119.0d, 261.6d, 118.1d, 261.5d, 118.0d);
      path0.curveTo(259.9d, 115.2d, 264.2d, 94.5d, 264.5d, 89.7d);
      path0.curveTo(265.6d, 74.0d, 261.7d, 60.7d, 251.5d, 48.8d);
      path0.curveTo(248.4d, 45.1d, 232.4d, 34.7d, 227.9d, 33.4d);
      path0.curveTo(222.5d, 31.8d, 217.7d, 32.9d, 212.3d, 32.5d);
      path0.curveTo(200.5d, 31.7d, 192.7d, 27.5d, 180.8d, 32.6d);
      path0.curveTo(175.0d, 35.1d, 170.3d, 40.1d, 164.5d, 42.2d);
      path0.curveTo(162.3d, 43.0d, 155.6d, 43.6d, 155.1d, 45.3d);
      path0.curveTo(154.3d, 48.1d, 156.2d, 49.5d, 152.8d, 52.0d);
      path0.curveTo(150.4d, 53.7d, 147.2d, 53.8d, 144.5d, 54.2d);
      path0.curveTo(143.5d, 54.9d, 144.2d, 58.8d, 144.0d, 60.2d);
      path0.curveTo(143.7d, 61.5d, 142.2d, 62.9d, 142.1d, 64.9d);
      path0.curveTo(142.0d, 66.1d, 142.6d, 67.1d, 142.4d, 68.1d);
      path0.curveTo(142.2d, 69.6d, 140.3d, 70.6d, 139.6d, 71.8d);
      path0.curveTo(133.7d, 81.3d, 136.7d, 92.9d, 136.0d, 102.8d);
      path0.curveTo(135.9d, 104.4d, 134.8d, 106.2d, 135.2d, 107.5d);
      path0.curveTo(135.5d, 108.6d, 137.0d, 109.5d, 137.6d, 110.6d);
      path0.curveTo(140.5d, 116.5d, 139.5d, 119.6d, 145.8d, 124.5d);
      path0.curveTo(150.7d, 128.3d, 152.7d, 127.2d, 158.5d, 126.5d);
      path0.curveTo(157.9d, 128.8d, 159.4d, 128.4d, 159.5d, 128.5d);
      path0.curveTo(159.6d, 128.6d, 159.5d, 129.3d, 160.0d, 129.7d);
      path0.curveTo(160.8d, 130.3d, 161.9d, 130.1d, 162.7d, 131.0d);
      path0.curveTo(163.8d, 132.1d, 165.0d, 136.1d, 165.9d, 137.8d);
      path0.curveTo(166.8d, 139.5d, 168.9d, 141.9d, 169.5d, 143.0d);
      path0.curveTo(169.6d, 143.3d, 169.4d, 144.3d, 169.7d, 145.0d);
      path0.curveTo(170.8d, 147.3d, 171.8d, 149.7d, 173.0d, 152.0d);
      path0.curveTo(166.4d, 151.8d, 161.4d, 160.2d, 162.6d, 166.1d);
      path0.curveTo(162.9d, 167.6d, 164.5d, 168.8d, 164.4d, 170.1d);
      path0.curveTo(164.2d, 172.2d, 161.7d, 175.4d, 161.9d, 177.9d);
      path0.curveTo(156.5d, 179.3d, 153.0d, 182.4d, 152.0d, 188.0d);
      path0.curveTo(151.7d, 189.7d, 151.4d, 190.5d, 152.0d, 192.5d);
      path0.curveTo(145.4d, 195.2d, 136.8d, 197.4d, 131.2d, 202.0d);
      path0.curveTo(125.7d, 206.6d, 115.8d, 219.7d, 112.6d, 225.8d);
      path0.curveTo(107.8d, 235.1d, 104.3d, 250.7d, 100.4d, 261.1d);
      path0.curveTo(95.3d, 274.5d, 88.5d, 287.2d, 83.4d, 300.2d);
      path0.curveTo(81.2d, 305.9d, 82.4d, 309.9d, 82.0d, 315.8d);
      path0.curveTo(81.6d, 322.7d, 80.6d, 329.2d, 81.0d, 336.3d);
      path0.curveTo(81.7d, 350.4d, 87.4d, 359.7d, 92.4d, 372.4d);
      path0.curveTo(94.4d, 377.6d, 95.1d, 384.3d, 100.3d, 387.4d);
      path0.curveTo(108.6d, 392.3d, 113.9d, 384.9d, 120.5d, 381.2d);
      path0.curveTo(113.5d, 397.4d, 106.8d, 413.8d, 100.6d, 430.4d);
      path0.curveTo(98.7d, 435.7d, 96.3d, 441.3d, 95.5d, 447.0d);
      path0.closePath();
      g2.setPaint(new Color(144, 144, 144));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path0);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path0);

      // Element 1: Path
      GeneralPath path1 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path1.moveTo(136.0d, 359.5d);
      path1.curveTo(140.7d, 354.9d, 145.6d, 349.7d, 149.4d, 344.2d);
      path1.curveTo(152.9d, 339.0d, 155.5d, 333.2d, 158.5d, 327.8d);
      path1.curveTo(162.2d, 326.6d, 166.0d, 326.5d, 169.5d, 324.8d);
      path1.curveTo(173.6d, 322.8d, 179.2d, 317.2d, 182.5d, 314.3d);
      path1.curveTo(183.1d, 313.8d, 183.7d, 313.4d, 184.5d, 313.5d);
      path1.curveTo(184.5d, 314.7d, 184.4d, 315.9d, 184.6d, 317.1d);
      path1.curveTo(185.4d, 321.0d, 198.1d, 333.9d, 201.5d, 337.8d);
      path1.curveTo(202.8d, 339.2d, 203.8d, 341.5d, 205.0d, 342.8d);
      path1.curveTo(209.2d, 347.2d, 218.9d, 352.8d, 223.8d, 357.2d);
      path1.curveTo(233.3d, 365.6d, 240.4d, 379.4d, 253.9d, 381.9d);
      path1.curveTo(259.8d, 383.0d, 266.6d, 382.8d, 272.7d, 383.6d);
      path1.curveTo(280.9d, 384.6d, 289.4d, 386.6d, 297.5d, 387.5d);
      path1.curveTo(299.7d, 387.8d, 302.3d, 387.9d, 304.5d, 388.0d);
      path1.curveTo(305.2d, 388.1d, 305.8d, 388.0d, 306.5d, 388.0d);
      path1.curveTo(309.9d, 388.2d, 314.5d, 388.6d, 317.8d, 388.5d);
      path1.curveTo(318.7d, 388.5d, 319.8d, 389.0d, 319.5d, 387.5d);
      path1.curveTo(320.0d, 387.5d, 320.5d, 387.5d, 321.0d, 387.5d);
      path1.lineTo(321.0d, 390.0d);
      path1.curveTo(320.8d, 390.2d, 320.7d, 390.3d, 320.5d, 390.5d);
      path1.curveTo(318.9d, 390.6d, 318.9d, 391.4d, 318.5d, 392.5d);
      path1.curveTo(316.2d, 394.1d, 315.4d, 395.5d, 314.0d, 398.0d);
      path1.curveTo(313.8d, 398.1d, 313.3d, 398.0d, 312.8d, 398.5d);
      path1.curveTo(312.2d, 399.1d, 312.3d, 399.9d, 312.0d, 400.5d);
      path1.curveTo(311.9d, 400.6d, 311.5d, 400.4d, 311.1d, 400.6d);
      path1.curveTo(310.6d, 401.0d, 310.3d, 402.4d, 310.0d, 403.0d);
      path1.curveTo(309.9d, 403.1d, 309.5d, 402.9d, 309.1d, 403.1d);
      path1.curveTo(308.8d, 403.4d, 305.1d, 409.4d, 305.0d, 409.8d);
      path1.curveTo(304.8d, 410.7d, 305.6d, 410.8d, 306.0d, 410.5d);
      path1.curveTo(306.2d, 410.4d, 306.5d, 408.5d, 307.5d, 408.5d);
      path1.curveTo(310.5d, 421.5d, 305.9d, 434.3d, 304.0d, 447.0d);
      path1.lineTo(96.5d, 447.0d);
      path1.curveTo(97.2d, 443.0d, 98.7d, 438.8d, 100.1d, 434.9d);
      path1.curveTo(105.9d, 418.9d, 114.1d, 394.7d, 122.1d, 380.3d);
      path1.curveTo(123.6d, 377.5d, 127.1d, 374.3d, 129.1d, 371.4d);
      path1.curveTo(131.7d, 367.6d, 133.7d, 363.3d, 136.0d, 359.5d);
      path1.closePath();
      g2.setPaint(new Color(53, 52, 52));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path1);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path1);

      // Element 2: Path
      GeneralPath path2 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path2.moveTo(157.5d, 197.0d);
      path2.curveTo(156.4d, 196.9d, 154.3d, 196.2d, 153.4d, 195.3d);
      path2.curveTo(153.0d, 194.7d, 153.1d, 193.6d, 153.0d, 193.5d);
      path2.curveTo(152.1d, 192.7d, 134.9d, 200.6d, 132.8d, 202.1d);
      path2.curveTo(130.1d, 204.1d, 126.5d, 208.3d, 124.3d, 211.0d);
      path2.curveTo(110.0d, 228.8d, 107.0d, 247.4d, 98.9d, 268.1d);
      path2.curveTo(94.5d, 279.2d, 88.6d, 290.0d, 84.4d, 300.7d);
      path2.curveTo(82.1d, 306.5d, 83.3d, 311.2d, 83.0d, 317.3d);
      path2.curveTo(82.6d, 327.7d, 81.3d, 338.2d, 83.7d, 348.5d);
      path2.lineTo(91.5d, 366.5d);
      path2.curveTo(90.5d, 360.6d, 90.6d, 355.3d, 91.6d, 349.4d);
      path2.curveTo(92.0d, 347.1d, 92.9d, 345.2d, 93.5d, 343.0d);
      path2.lineTo(94.0d, 343.0d);
      path2.curveTo(93.2d, 345.9d, 92.7d, 348.4d, 93.0d, 351.5d);
      path2.curveTo(92.8d, 351.5d, 92.6d, 351.4d, 92.5d, 351.5d);
      path2.curveTo(92.0d, 351.8d, 91.5d, 354.9d, 91.5d, 355.7d);
      path2.curveTo(91.2d, 361.1d, 94.6d, 378.7d, 97.6d, 383.2d);
      path2.curveTo(102.6d, 390.7d, 109.0d, 387.7d, 114.9d, 383.7d);
      path2.curveTo(120.9d, 379.6d, 127.4d, 373.7d, 130.5d, 366.7d);
      path2.curveTo(130.9d, 365.8d, 131.5d, 365.3d, 130.0d, 365.5d);
      path2.curveTo(131.8d, 364.3d, 134.3d, 361.2d, 136.0d, 359.5d);
      path2.curveTo(133.7d, 363.3d, 131.7d, 367.6d, 129.1d, 371.4d);
      path2.curveTo(127.1d, 374.3d, 123.6d, 377.5d, 122.0d, 380.3d);
      path2.curveTo(114.0d, 394.6d, 105.9d, 418.9d, 100.1d, 434.9d);
      path2.curveTo(98.7d, 438.7d, 97.2d, 443.0d, 96.5d, 447.0d);
      path2.lineTo(95.5d, 447.0d);
      path2.curveTo(96.3d, 441.3d, 98.6d, 435.7d, 100.6d, 430.4d);
      path2.curveTo(106.8d, 413.8d, 113.5d, 397.4d, 120.5d, 381.2d);
      path2.curveTo(113.9d, 384.9d, 108.6d, 392.3d, 100.3d, 387.4d);
      path2.curveTo(95.1d, 384.3d, 94.4d, 377.6d, 92.4d, 372.4d);
      path2.curveTo(87.4d, 359.6d, 81.7d, 350.4d, 81.0d, 336.3d);
      path2.curveTo(80.6d, 329.2d, 81.6d, 322.7d, 82.0d, 315.8d);
      path2.curveTo(82.4d, 309.9d, 81.1d, 305.9d, 83.4d, 300.2d);
      path2.curveTo(88.5d, 287.2d, 95.3d, 274.5d, 100.4d, 261.1d);
      path2.curveTo(104.3d, 250.7d, 107.8d, 235.1d, 112.6d, 225.8d);
      path2.curveTo(115.7d, 219.7d, 125.9d, 206.3d, 131.2d, 202.0d);
      path2.curveTo(136.8d, 197.4d, 145.4d, 195.2d, 152.0d, 192.5d);
      path2.curveTo(151.4d, 190.5d, 151.7d, 189.7d, 152.0d, 188.0d);
      path2.lineTo(152.5d, 188.0d);
      path2.curveTo(151.7d, 192.4d, 153.9d, 194.9d, 157.5d, 197.0d);
      path2.closePath();
      g2.setPaint(new Color(23, 23, 23));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path2);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path2);

      // Element 3: Path
      GeneralPath path3 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path3.moveTo(320.5d, 390.5d);
      path3.curveTo(319.6d, 391.4d, 320.4d, 391.2d, 318.5d, 392.5d);
      path3.curveTo(317.4d, 395.1d, 316.5d, 396.5d, 314.0d, 398.0d);
      path3.curveTo(313.0d, 399.7d, 314.1d, 399.2d, 312.0d, 400.5d);
      path3.curveTo(311.8d, 400.9d, 312.1d, 401.6d, 311.5d, 402.3d);
      path3.curveTo(311.0d, 403.0d, 310.4d, 402.8d, 310.0d, 403.0d);
      path3.curveTo(307.1d, 409.2d, 309.9d, 411.7d, 309.9d, 417.8d);
      path3.curveTo(309.9d, 419.0d, 309.6d, 421.3d, 309.5d, 422.7d);
      path3.curveTo(308.7d, 430.8d, 306.2d, 439.0d, 305.0d, 447.0d);
      path3.lineTo(304.0d, 447.0d);
      path3.curveTo(305.9d, 434.3d, 310.5d, 421.4d, 307.5d, 408.5d);
      path3.curveTo(306.5d, 408.5d, 306.2d, 410.4d, 306.0d, 410.5d);
      path3.curveTo(305.6d, 410.8d, 304.8d, 410.7d, 305.0d, 409.8d);
      path3.curveTo(305.1d, 409.4d, 308.8d, 403.3d, 309.1d, 403.1d);
      path3.curveTo(309.4d, 402.9d, 309.9d, 403.1d, 310.0d, 403.0d);
      path3.curveTo(310.3d, 402.4d, 310.6d, 401.0d, 311.1d, 400.6d);
      path3.curveTo(311.5d, 400.4d, 311.9d, 400.6d, 312.0d, 400.5d);
      path3.curveTo(312.2d, 399.9d, 312.2d, 399.1d, 312.8d, 398.5d);
      path3.curveTo(313.3d, 398.0d, 313.8d, 398.1d, 314.0d, 398.0d);
      path3.curveTo(315.4d, 395.5d, 316.1d, 394.0d, 318.5d, 392.5d);
      path3.curveTo(318.9d, 391.4d, 318.9d, 390.6d, 320.5d, 390.5d);
      path3.closePath();
      g2.setPaint(new Color(29, 29, 27));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path3);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path3);

      // Element 4: Path
      GeneralPath path4 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path4.moveTo(249.0d, 149.5d);
      path4.curveTo(248.7d, 151.1d, 250.5d, 150.3d, 251.6d, 150.6d);
      path4.curveTo(258.9d, 152.4d, 268.3d, 154.2d, 266.5d, 164.0d);
      path4.lineTo(278.2d, 164.0d);
      path4.curveTo(279.6d, 164.0d, 285.1d, 165.2d, 286.5d, 165.8d);
      path4.curveTo(294.4d, 168.6d, 291.9d, 174.9d, 291.1d, 181.4d);
      path4.curveTo(305.0d, 187.5d, 325.6d, 190.9d, 335.5d, 203.3d);
      path4.curveTo(343.8d, 213.6d, 354.1d, 247.8d, 358.1d, 261.6d);
      path4.curveTo(361.6d, 273.5d, 360.8d, 279.9d, 361.5d, 291.8d);
      path4.curveTo(362.0d, 300.7d, 363.8d, 309.7d, 364.6d, 318.6d);
      path4.curveTo(367.6d, 326.8d, 383.2d, 332.1d, 377.1d, 342.9d);
      path4.curveTo(376.2d, 344.4d, 373.2d, 346.3d, 373.1d, 346.8d);
      path4.curveTo(372.7d, 348.0d, 373.5d, 349.4d, 373.5d, 350.7d);
      path4.curveTo(373.5d, 353.0d, 368.8d, 366.6d, 367.6d, 369.3d);
      path4.curveTo(362.4d, 381.2d, 343.1d, 389.1d, 330.8d, 389.5d);
      path4.curveTo(328.0d, 389.6d, 322.6d, 388.4d, 321.0d, 390.0d);
      path4.lineTo(321.0d, 387.5d);
      path4.curveTo(324.4d, 387.4d, 327.1d, 387.6d, 330.5d, 387.0d);
      path4.curveTo(330.9d, 387.7d, 331.5d, 387.4d, 332.0d, 387.5d);
      path4.curveTo(342.2d, 390.1d, 363.0d, 378.1d, 366.9d, 368.1d);
      path4.curveTo(368.0d, 365.2d, 371.3d, 356.3d, 371.8d, 353.5d);
      path4.curveTo(372.2d, 351.3d, 371.6d, 347.9d, 372.1d, 346.3d);
      path4.curveTo(372.4d, 345.4d, 376.0d, 343.8d, 376.8d, 341.0d);
      path4.curveTo(379.6d, 330.1d, 365.4d, 327.2d, 363.2d, 318.0d);
      path4.curveTo(359.8d, 302.0d, 362.2d, 283.1d, 358.6d, 267.6d);
      path4.curveTo(355.8d, 255.0d, 349.5d, 238.1d, 345.0d, 225.7d);
      path4.curveTo(342.3d, 218.4d, 338.6d, 208.6d, 333.5d, 202.7d);
      path4.curveTo(323.3d, 191.2d, 303.2d, 188.3d, 289.7d, 182.0d);
      path4.curveTo(289.1d, 181.1d, 293.2d, 171.9d, 289.0d, 168.3d);
      path4.curveTo(286.4d, 166.1d, 278.2d, 165.1d, 274.8d, 165.0d);
      path4.curveTo(273.1d, 164.9d, 266.7d, 165.6d, 266.3d, 165.5d);
      path4.curveTo(264.9d, 165.0d, 265.7d, 160.4d, 265.2d, 158.6d);
      path4.curveTo(263.8d, 154.2d, 253.2d, 151.9d, 249.1d, 151.6d);
      path4.curveTo(247.7d, 154.9d, 249.8d, 158.3d, 250.0d, 161.7d);
      path4.curveTo(250.5d, 168.3d, 248.6d, 175.6d, 246.5d, 181.8d);
      path4.curveTo(245.2d, 185.8d, 240.3d, 194.7d, 240.0d, 197.8d);
      path4.curveTo(239.9d, 199.5d, 241.1d, 205.5d, 241.5d, 207.5d);
      path4.lineTo(241.0d, 207.5d);
      path4.curveTo(240.8d, 206.4d, 240.1d, 205.5d, 240.0d, 205.0d);
      path4.curveTo(239.7d, 203.5d, 239.0d, 200.6d, 239.0d, 199.2d);
      path4.curveTo(238.9d, 195.5d, 243.6d, 186.9d, 245.0d, 182.8d);
      path4.curveTo(248.1d, 173.9d, 251.0d, 162.6d, 247.0d, 153.5d);
      path4.curveTo(247.2d, 153.3d, 247.4d, 153.2d, 247.5d, 153.0d);
      path4.curveTo(248.1d, 151.9d, 248.2d, 150.7d, 248.5d, 149.5d);
      path4.lineTo(249.0d, 149.5d);
      path4.closePath();
      path4.moveTo(331.0d, 388.0d);
      path4.lineTo(328.0d, 388.0d);
      path4.lineTo(328.0d, 388.5d);
      path4.lineTo(331.0d, 388.5d);
      path4.lineTo(331.0d, 388.0d);
      path4.closePath();
      g2.setPaint(new Color(29, 29, 27));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path4);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path4);

      // Element 5: Path
      GeneralPath path5 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path5.moveTo(261.5d, 118.0d);
      path5.lineTo(261.0d, 118.5d);
      path5.curveTo(260.8d, 118.4d, 260.1d, 117.9d, 260.0d, 118.0d);
      path5.curveTo(259.5d, 120.8d, 256.6d, 121.5d, 255.0d, 123.5d);
      path5.curveTo(255.5d, 119.8d, 255.7d, 121.9d, 257.4d, 120.8d);
      path5.curveTo(258.3d, 120.2d, 258.7d, 119.0d, 259.2d, 118.2d);
      path5.curveTo(259.3d, 117.9d, 260.0d, 118.0d, 260.0d, 118.0d);
      path5.curveTo(261.0d, 116.1d, 260.3d, 109.8d, 260.5d, 107.3d);
      path5.curveTo(261.0d, 101.0d, 263.1d, 94.2d, 263.5d, 87.8d);
      path5.curveTo(264.9d, 63.5d, 252.1d, 46.0d, 231.0d, 35.7d);
      path5.curveTo(223.9d, 32.3d, 220.5d, 34.4d, 213.8d, 34.0d);
      path5.curveTo(203.9d, 33.5d, 195.2d, 29.3d, 185.0d, 32.2d);
      path5.curveTo(174.8d, 35.2d, 167.8d, 45.2d, 156.5d, 45.0d);
      path5.curveTo(157.2d, 49.9d, 154.7d, 53.5d, 149.9d, 54.7d);
      path5.curveTo(148.7d, 55.0d, 145.7d, 54.8d, 145.2d, 55.5d);
      path5.curveTo(144.4d, 56.9d, 145.6d, 58.9d, 145.4d, 60.1d);
      path5.curveTo(145.3d, 61.2d, 143.7d, 62.1d, 144.0d, 63.5d);
      path5.lineTo(149.0d, 62.5d);
      path5.curveTo(149.0d, 66.9d, 143.6d, 68.3d, 141.0d, 71.8d);
      path5.curveTo(136.3d, 78.1d, 136.9d, 91.6d, 137.0d, 99.2d);
      path5.curveTo(137.1d, 101.3d, 136.4d, 105.8d, 136.6d, 107.1d);
      path5.curveTo(136.8d, 108.6d, 138.3d, 109.1d, 138.7d, 110.0d);
      path5.curveTo(139.3d, 111.3d, 139.6d, 113.8d, 140.3d, 115.5d);
      path5.curveTo(141.6d, 118.8d, 146.8d, 125.3d, 150.3d, 126.0d);
      path5.curveTo(154.3d, 126.8d, 156.5d, 125.3d, 159.9d, 124.1d);
      path5.curveTo(161.0d, 123.7d, 163.9d, 123.3d, 164.5d, 122.8d);
      path5.curveTo(165.3d, 122.1d, 164.9d, 119.2d, 165.8d, 117.6d);
      path5.curveTo(166.6d, 116.1d, 169.2d, 114.1d, 170.5d, 112.2d);
      path5.curveTo(173.4d, 107.8d, 174.3d, 103.9d, 175.0d, 98.8d);
      path5.curveTo(175.1d, 98.0d, 175.0d, 97.5d, 175.8d, 97.0d);
      path5.curveTo(177.8d, 96.6d, 181.8d, 107.9d, 180.5d, 110.0d);
      path5.lineTo(182.0d, 107.5d);
      path5.curveTo(181.8d, 109.3d, 181.6d, 110.0d, 180.5d, 111.5d);
      path5.curveTo(185.0d, 111.5d, 191.8d, 112.6d, 193.0d, 117.5d);
      path5.lineTo(179.3d, 116.5d);
      path5.curveTo(176.4d, 117.1d, 173.4d, 116.7d, 170.5d, 117.0d);
      path5.curveTo(170.2d, 115.5d, 171.3d, 116.0d, 172.2d, 116.0d);
      path5.curveTo(178.1d, 115.8d, 184.1d, 116.2d, 190.0d, 116.0d);
      path5.curveTo(189.5d, 113.1d, 184.3d, 112.7d, 181.8d, 112.5d);
      path5.curveTo(178.2d, 112.2d, 169.4d, 111.9d, 169.5d, 117.0d);
      path5.curveTo(169.4d, 117.0d, 166.7d, 118.0d, 166.6d, 118.1d);
      path5.curveTo(166.4d, 118.3d, 166.2d, 119.5d, 166.2d, 119.9d);
      path5.curveTo(166.1d, 120.6d, 165.9d, 121.3d, 166.0d, 122.0d);
      path5.curveTo(166.8d, 122.0d, 167.7d, 122.0d, 168.5d, 122.0d);
      path5.curveTo(168.7d, 122.0d, 168.9d, 122.0d, 169.0d, 122.0d);
      path5.lineTo(167.5d, 123.0d);
      path5.curveTo(168.4d, 123.1d, 169.2d, 122.7d, 170.0d, 122.5d);
      path5.curveTo(170.2d, 122.5d, 170.4d, 122.5d, 170.5d, 122.5d);
      path5.curveTo(171.3d, 122.3d, 172.0d, 121.8d, 173.0d, 122.0d);
      path5.curveTo(173.2d, 122.2d, 173.4d, 122.3d, 173.5d, 122.5d);
      path5.curveTo(171.2d, 123.0d, 169.0d, 124.3d, 166.5d, 124.5d);
      path5.lineTo(166.0d, 124.5d);
      path5.lineTo(166.8d, 123.0d);
      path5.lineTo(159.1d, 125.8d);
      path5.lineTo(159.5d, 128.5d);
      path5.curveTo(166.1d, 128.9d, 165.3d, 138.0d, 170.0d, 141.5d);
      path5.curveTo(170.1d, 142.0d, 170.2d, 142.5d, 170.5d, 143.0d);
      path5.curveTo(170.4d, 142.9d, 170.2d, 143.0d, 170.0d, 143.0d);
      path5.lineTo(169.5d, 143.0d);
      path5.curveTo(168.9d, 141.9d, 166.8d, 139.5d, 165.9d, 137.8d);
      path5.curveTo(165.0d, 136.1d, 163.8d, 132.1d, 162.7d, 131.0d);
      path5.curveTo(161.9d, 130.1d, 160.8d, 130.3d, 160.0d, 129.7d);
      path5.curveTo(159.5d, 129.3d, 159.6d, 128.6d, 159.5d, 128.5d);
      path5.curveTo(159.4d, 128.4d, 157.9d, 128.8d, 158.5d, 126.5d);
      path5.curveTo(152.7d, 127.2d, 150.7d, 128.3d, 145.8d, 124.5d);
      path5.curveTo(139.5d, 119.6d, 140.5d, 116.5d, 137.6d, 110.6d);
      path5.curveTo(137.1d, 109.5d, 135.5d, 108.6d, 135.2d, 107.5d);
      path5.curveTo(134.8d, 106.2d, 135.9d, 104.4d, 136.0d, 102.8d);
      path5.curveTo(136.7d, 92.9d, 133.8d, 81.3d, 139.6d, 71.8d);
      path5.curveTo(140.3d, 70.6d, 142.2d, 69.6d, 142.4d, 68.1d);
      path5.curveTo(142.6d, 67.1d, 142.0d, 66.1d, 142.1d, 64.9d);
      path5.curveTo(142.3d, 62.9d, 143.7d, 61.5d, 144.0d, 60.2d);
      path5.curveTo(144.2d, 58.8d, 143.5d, 54.9d, 144.5d, 54.2d);
      path5.curveTo(147.3d, 53.8d, 150.4d, 53.7d, 152.8d, 52.0d);
      path5.curveTo(156.3d, 49.5d, 154.3d, 48.1d, 155.1d, 45.3d);
      path5.curveTo(155.6d, 43.6d, 162.3d, 43.0d, 164.5d, 42.2d);
      path5.curveTo(170.4d, 40.1d, 175.0d, 35.1d, 180.8d, 32.6d);
      path5.curveTo(192.8d, 27.5d, 200.5d, 31.7d, 212.3d, 32.5d);
      path5.curveTo(217.7d, 32.9d, 222.5d, 31.8d, 227.9d, 33.4d);
      path5.curveTo(232.4d, 34.7d, 248.4d, 45.1d, 251.5d, 48.8d);
      path5.curveTo(261.7d, 60.7d, 265.6d, 74.0d, 264.5d, 89.7d);
      path5.curveTo(264.2d, 94.5d, 259.9d, 115.2d, 261.5d, 118.0d);
      path5.closePath();
      path5.moveTo(148.0d, 63.5d);
      path5.curveTo(145.4d, 63.4d, 144.6d, 65.4d, 143.5d, 67.3d);
      path5.curveTo(145.5d, 66.8d, 147.3d, 65.4d, 148.0d, 63.5d);
      path5.closePath();
      path5.moveTo(143.8d, 64.0d);
      path5.lineTo(143.8d, 64.5d);
      path5.curveTo(144.1d, 64.3d, 144.1d, 64.2d, 143.8d, 64.0d);
      path5.closePath();
      path5.moveTo(179.4d, 111.4d);
      path5.curveTo(180.4d, 108.9d, 179.5d, 106.3d, 179.0d, 103.8d);
      path5.lineTo(176.0d, 98.5d);
      path5.curveTo(175.8d, 103.4d, 174.0d, 108.1d, 171.5d, 112.2d);
      path5.lineTo(179.4d, 111.4d);
      path5.closePath();
      g2.setPaint(new Color(29, 29, 27));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path5);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path5);

      // Element 6: Path
      GeneralPath path6 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path6.moveTo(170.0d, 143.0d);
      path6.lineTo(173.6d, 151.9d);
      path6.curveTo(173.8d, 152.1d, 174.7d, 152.1d, 175.0d, 152.0d);
      path6.curveTo(174.7d, 152.5d, 174.7d, 153.5d, 174.3d, 153.8d);
      path6.curveTo(174.1d, 154.1d, 173.6d, 154.0d, 173.5d, 154.0d);
      path6.curveTo(173.5d, 153.6d, 173.7d, 153.0d, 173.2d, 153.0d);
      path6.curveTo(166.8d, 152.4d, 160.7d, 162.7d, 164.8d, 168.0d);
      path6.curveTo(165.8d, 168.2d, 165.4d, 167.6d, 165.7d, 167.0d);
      path6.curveTo(168.2d, 162.5d, 169.8d, 157.7d, 173.5d, 154.0d);
      path6.curveTo(173.4d, 155.6d, 168.5d, 163.2d, 167.4d, 165.7d);
      path6.curveTo(165.4d, 170.0d, 163.1d, 175.9d, 162.0d, 180.5d);
      path6.lineTo(161.5d, 180.5d);
      path6.lineTo(160.8d, 179.5d);
      path6.curveTo(157.4d, 179.6d, 153.0d, 185.0d, 152.5d, 188.0d);
      path6.lineTo(152.0d, 188.0d);
      path6.curveTo(153.0d, 182.4d, 156.5d, 179.3d, 161.9d, 177.9d);
      path6.curveTo(161.7d, 175.4d, 164.2d, 172.2d, 164.4d, 170.1d);
      path6.curveTo(164.5d, 168.8d, 162.9d, 167.6d, 162.6d, 166.1d);
      path6.curveTo(161.4d, 160.3d, 166.4d, 151.8d, 173.0d, 152.0d);
      path6.curveTo(171.8d, 149.7d, 170.8d, 147.3d, 169.7d, 145.0d);
      path6.curveTo(169.4d, 144.3d, 169.6d, 143.3d, 169.5d, 143.0d);
      path6.lineTo(170.0d, 143.0d);
      path6.closePath();
      g2.setPaint(new Color(53, 53, 52));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path6);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path6);

      // Element 7: Path
      GeneralPath path7 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path7.moveTo(261.5d, 118.0d);
      path7.curveTo(261.6d, 118.1d, 263.2d, 119.0d, 263.4d, 119.9d);
      path7.curveTo(265.0d, 124.6d, 261.7d, 131.3d, 258.9d, 135.2d);
      path7.curveTo(256.6d, 138.4d, 254.9d, 140.8d, 250.5d, 140.0d);
      path7.curveTo(250.6d, 141.7d, 250.2d, 143.3d, 250.0d, 145.0d);
      path7.lineTo(249.5d, 145.0d);
      path7.curveTo(249.9d, 141.7d, 249.7d, 137.4d, 250.0d, 134.0d);
      path7.lineTo(251.0d, 134.0d);
      path7.curveTo(250.7d, 135.4d, 251.3d, 137.4d, 250.5d, 139.0d);
      path7.curveTo(254.9d, 140.2d, 257.8d, 135.3d, 259.7d, 132.0d);
      path7.curveTo(261.5d, 128.8d, 265.1d, 120.9d, 261.0d, 118.5d);
      path7.lineTo(261.5d, 118.0d);
      path7.closePath();
      g2.setPaint(new Color(52, 52, 52));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path7);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path7);

      // Element 8: Path
      GeneralPath path8 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path8.moveTo(250.0d, 145.0d);
      path8.curveTo(249.8d, 146.5d, 250.1d, 148.3d, 249.0d, 149.5d);
      path8.lineTo(248.5d, 149.5d);
      path8.curveTo(248.8d, 148.3d, 249.4d, 146.0d, 249.5d, 145.0d);
      path8.lineTo(250.0d, 145.0d);
      path8.closePath();
      g2.setPaint(new Color(73, 75, 59));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path8);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path8);

      // Element 9: Path
      GeneralPath path9 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path9.moveTo(267.0d, 327.0d);
      path9.curveTo(267.0d, 327.5d, 267.0d, 328.0d, 267.0d, 328.5d);
      path9.curveTo(266.2d, 328.6d, 266.4d, 329.7d, 267.0d, 330.0d);
      path9.curveTo(267.3d, 334.8d, 268.2d, 342.4d, 269.1d, 347.1d);
      path9.curveTo(269.5d, 348.8d, 271.4d, 355.8d, 272.8d, 356.5d);
      path9.curveTo(274.7d, 357.4d, 271.2d, 352.2d, 271.5d, 352.0d);
      path9.curveTo(281.7d, 352.4d, 290.4d, 351.9d, 299.8d, 348.1d);
      path9.curveTo(304.1d, 346.4d, 309.9d, 342.4d, 314.3d, 342.0d);
      path9.curveTo(322.9d, 341.1d, 335.1d, 345.2d, 341.5d, 351.0d);
      path9.curveTo(341.8d, 351.2d, 342.2d, 351.7d, 342.5d, 352.0d);
      path9.curveTo(347.7d, 357.3d, 352.4d, 367.1d, 349.7d, 374.5d);
      path9.curveTo(347.9d, 379.6d, 335.8d, 386.0d, 330.5d, 387.0d);
      path9.curveTo(327.1d, 387.6d, 324.4d, 387.4d, 321.0d, 387.5d);
      path9.curveTo(320.5d, 387.5d, 320.0d, 387.5d, 319.5d, 387.5d);
      path9.curveTo(315.3d, 387.5d, 310.6d, 387.7d, 306.5d, 387.5d);
      path9.curveTo(306.3d, 387.5d, 305.4d, 387.1d, 304.5d, 387.0d);
      path9.curveTo(302.2d, 386.8d, 299.8d, 386.8d, 297.5d, 386.5d);
      path9.curveTo(285.0d, 384.8d, 272.4d, 382.4d, 260.0d, 381.0d);
      path9.curveTo(260.1d, 379.1d, 262.3d, 379.1d, 263.7d, 378.4d);
      path9.curveTo(268.2d, 375.8d, 277.9d, 369.3d, 275.7d, 363.0d);
      path9.curveTo(275.0d, 360.9d, 270.6d, 357.8d, 269.1d, 355.7d);
      path9.curveTo(268.2d, 354.4d, 264.5d, 346.8d, 264.5d, 345.7d);
      path9.lineTo(264.5d, 336.7d);
      path9.curveTo(264.5d, 335.2d, 265.5d, 330.4d, 265.9d, 328.6d);
      path9.curveTo(266.0d, 327.7d, 265.7d, 326.7d, 267.0d, 327.0d);
      path9.closePath();
      g2.setPaint(new Color(208, 161, 134));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path9);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path9);

      // Element 10: Path
      GeneralPath path10 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path10.moveTo(260.0d, 381.0d);
      path10.curveTo(272.4d, 382.5d, 285.0d, 384.8d, 297.5d, 386.5d);
      path10.lineTo(297.5d, 387.5d);
      path10.curveTo(289.4d, 386.6d, 280.9d, 384.6d, 272.7d, 383.6d);
      path10.curveTo(266.6d, 382.8d, 259.8d, 382.9d, 253.9d, 381.9d);
      path10.curveTo(240.4d, 379.4d, 233.3d, 365.6d, 223.8d, 357.2d);
      path10.curveTo(218.9d, 352.8d, 209.2d, 347.2d, 205.0d, 342.7d);
      path10.curveTo(203.8d, 341.5d, 202.8d, 339.2d, 201.5d, 337.8d);
      path10.curveTo(198.1d, 333.9d, 185.4d, 321.0d, 184.6d, 317.1d);
      path10.curveTo(184.4d, 315.9d, 184.5d, 314.7d, 184.5d, 313.5d);
      path10.curveTo(183.7d, 313.4d, 183.1d, 313.8d, 182.5d, 314.3d);
      path10.curveTo(179.2d, 317.2d, 173.6d, 322.8d, 169.5d, 324.8d);
      path10.curveTo(166.0d, 326.4d, 162.2d, 326.6d, 158.5d, 327.8d);
      path10.curveTo(155.5d, 333.2d, 152.9d, 339.0d, 149.4d, 344.2d);
      path10.curveTo(145.6d, 349.7d, 140.7d, 354.9d, 136.0d, 359.5d);
      path10.curveTo(142.5d, 348.7d, 153.4d, 340.1d, 157.5d, 327.5d);
      path10.lineTo(157.5d, 327.0d);
      path10.curveTo(168.6d, 324.6d, 172.9d, 322.5d, 181.0d, 314.5d);
      path10.curveTo(181.1d, 314.4d, 181.7d, 314.7d, 181.9d, 314.4d);
      path10.lineTo(182.0d, 313.0d);
      path10.curveTo(183.1d, 312.6d, 184.4d, 312.0d, 185.0d, 311.0d);
      path10.lineTo(185.5d, 311.0d);
      path10.curveTo(185.3d, 314.2d, 185.2d, 316.6d, 186.9d, 319.4d);
      path10.curveTo(190.2d, 324.8d, 198.0d, 332.1d, 202.5d, 337.2d);
      path10.curveTo(203.8d, 338.8d, 204.8d, 341.1d, 206.3d, 342.5d);
      path10.curveTo(211.2d, 347.3d, 219.9d, 352.6d, 225.5d, 357.7d);
      path10.curveTo(234.2d, 365.7d, 242.3d, 379.1d, 254.8d, 380.9d);
      path10.curveTo(256.5d, 381.2d, 258.3d, 380.8d, 260.0d, 381.0d);
      path10.closePath();
      g2.setPaint(new Color(33, 32, 29));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path10);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path10);

      // Element 11: Path
      GeneralPath path11 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path11.moveTo(319.5d, 387.5d);
      path11.curveTo(319.8d, 389.0d, 318.7d, 388.5d, 317.8d, 388.5d);
      path11.curveTo(314.5d, 388.6d, 309.9d, 388.2d, 306.5d, 388.0d);
      path11.lineTo(306.5d, 387.5d);
      path11.curveTo(310.6d, 387.7d, 315.3d, 387.6d, 319.5d, 387.5d);
      path11.closePath();
      g2.setPaint(new Color(29, 29, 27));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path11);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path11);

      // Element 12: Path
      GeneralPath path12 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path12.moveTo(304.5d, 387.0d);
      path12.lineTo(304.5d, 388.0d);
      path12.curveTo(302.3d, 387.9d, 299.7d, 387.8d, 297.5d, 387.5d);
      path12.lineTo(297.5d, 386.5d);
      path12.curveTo(299.8d, 386.8d, 302.2d, 386.8d, 304.5d, 387.0d);
      path12.closePath();
      g2.setPaint(new Color(29, 29, 27));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path12);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path12);

      // Element 13: Path
      GeneralPath path13 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path13.moveTo(306.5d, 387.5d);
      path13.lineTo(306.5d, 388.0d);
      path13.curveTo(305.8d, 388.0d, 305.2d, 388.0d, 304.5d, 388.0d);
      path13.lineTo(304.5d, 387.0d);
      path13.curveTo(305.4d, 387.1d, 306.3d, 387.5d, 306.5d, 387.5d);
      path13.closePath();
      g2.setPaint(new Color(33, 32, 29));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path13);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path13);

      // Element 14: Path
      GeneralPath path14 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path14.moveTo(93.5d, 343.0d);
      path14.curveTo(92.9d, 345.2d, 92.0d, 347.1d, 91.7d, 349.4d);
      path14.curveTo(90.6d, 355.3d, 90.6d, 360.6d, 91.5d, 366.5d);
      path14.lineTo(83.8d, 348.5d);
      path14.curveTo(81.3d, 338.2d, 82.6d, 327.7d, 83.0d, 317.3d);
      path14.curveTo(83.3d, 311.2d, 82.1d, 306.5d, 84.4d, 300.7d);
      path14.curveTo(88.6d, 290.0d, 94.6d, 279.2d, 98.9d, 268.1d);
      path14.curveTo(107.0d, 247.4d, 110.0d, 228.8d, 124.3d, 211.1d);
      path14.curveTo(126.5d, 208.3d, 130.1d, 204.1d, 132.8d, 202.1d);
      path14.curveTo(134.9d, 200.6d, 152.2d, 192.8d, 153.0d, 193.5d);
      path14.curveTo(153.1d, 193.6d, 153.0d, 194.8d, 153.5d, 195.3d);
      path14.curveTo(154.3d, 196.3d, 156.4d, 196.9d, 157.5d, 197.0d);
      path14.curveTo(157.7d, 197.0d, 157.9d, 197.0d, 158.0d, 197.0d);
      path14.curveTo(159.1d, 198.2d, 159.2d, 199.5d, 159.0d, 201.0d);
      path14.curveTo(158.9d, 201.7d, 158.7d, 202.3d, 158.5d, 203.0d);
      path14.curveTo(158.0d, 205.3d, 157.7d, 207.7d, 157.5d, 210.0d);
      path14.curveTo(155.5d, 210.0d, 151.1d, 209.9d, 149.5d, 211.3d);
      path14.curveTo(149.0d, 211.7d, 149.1d, 212.5d, 149.0d, 212.5d);
      path14.curveTo(148.9d, 212.7d, 148.7d, 212.8d, 148.5d, 213.0d);
      path14.curveTo(148.4d, 213.1d, 147.7d, 213.0d, 146.9d, 214.1d);
      path14.curveTo(144.5d, 217.7d, 144.2d, 223.1d, 148.5d, 225.0d);
      path14.curveTo(149.5d, 226.2d, 151.3d, 226.9d, 151.5d, 227.1d);
      path14.curveTo(151.7d, 227.5d, 151.4d, 228.5d, 151.5d, 229.0d);
      path14.curveTo(151.5d, 229.2d, 151.5d, 229.4d, 151.5d, 229.5d);
      path14.curveTo(149.7d, 231.5d, 150.4d, 234.8d, 149.6d, 237.2d);
      path14.curveTo(149.2d, 237.8d, 145.3d, 237.8d, 144.1d, 239.2d);
      path14.curveTo(143.9d, 239.4d, 144.1d, 239.9d, 144.0d, 240.0d);
      path14.curveTo(143.1d, 240.4d, 141.9d, 241.3d, 141.4d, 242.2d);
      path14.curveTo(139.7d, 245.0d, 138.7d, 254.7d, 143.5d, 255.0d);
      path14.curveTo(143.2d, 258.3d, 139.6d, 259.7d, 138.9d, 262.9d);
      path14.curveTo(137.2d, 263.7d, 137.1d, 262.2d, 136.7d, 262.0d);
      path14.curveTo(135.0d, 261.2d, 133.2d, 260.4d, 131.3d, 260.5d);
      path14.curveTo(128.6d, 260.8d, 124.2d, 264.7d, 123.6d, 267.4d);
      path14.curveTo(123.1d, 269.9d, 123.6d, 273.6d, 126.0d, 274.5d);
      path14.curveTo(126.1d, 275.3d, 126.0d, 276.2d, 126.0d, 277.0d);
      path14.curveTo(125.6d, 277.5d, 124.8d, 278.2d, 124.5d, 278.5d);
      path14.curveTo(120.3d, 278.6d, 115.2d, 277.8d, 112.6d, 281.8d);
      path14.curveTo(109.5d, 286.6d, 111.3d, 290.9d, 116.4d, 292.9d);
      path14.curveTo(117.6d, 293.4d, 119.8d, 293.5d, 120.6d, 294.0d);
      path14.curveTo(121.4d, 294.4d, 121.3d, 295.6d, 122.4d, 295.9d);
      path14.curveTo(126.1d, 297.1d, 128.3d, 294.2d, 131.5d, 293.5d);
      path14.curveTo(130.4d, 296.0d, 130.9d, 299.8d, 129.7d, 302.0d);
      path14.curveTo(129.2d, 303.0d, 127.4d, 304.2d, 126.5d, 305.3d);
      path14.curveTo(124.1d, 308.4d, 122.0d, 311.7d, 119.5d, 314.8d);
      path14.curveTo(118.3d, 316.3d, 116.2d, 317.3d, 116.0d, 319.5d);
      path14.curveTo(115.9d, 319.5d, 115.7d, 319.5d, 115.5d, 319.5d);
      path14.curveTo(103.4d, 321.7d, 97.4d, 328.8d, 93.7d, 340.5d);
      path14.curveTo(93.5d, 341.4d, 93.7d, 342.4d, 93.5d, 343.0d);
      path14.closePath();
      path14.moveTo(120.5d, 276.0d);
      path14.lineTo(120.5d, 233.0d);
      path14.curveTo(120.2d, 233.0d, 119.5d, 233.7d, 119.5d, 233.8d);
      path14.lineTo(119.5d, 257.0d);
      path14.lineTo(116.2d, 248.0d);
      path14.lineTo(114.0d, 237.8d);
      path14.lineTo(113.0d, 237.8d);
      path14.curveTo(113.9d, 244.0d, 116.1d, 249.8d, 118.0d, 255.8d);
      path14.curveTo(118.2d, 256.5d, 119.5d, 258.3d, 119.5d, 258.8d);
      path14.lineTo(119.5d, 274.8d);
      path14.curveTo(119.5d, 275.0d, 120.1d, 275.5d, 120.0d, 276.0d);
      path14.lineTo(120.5d, 276.0d);
      path14.closePath();
      path14.moveTo(117.0d, 313.0d);
      path14.lineTo(85.0d, 305.0d);
      path14.curveTo(84.0d, 305.7d, 85.1d, 305.9d, 85.8d, 306.0d);
      path14.curveTo(96.3d, 307.9d, 106.4d, 311.3d, 117.0d, 313.0d);
      path14.closePath();
      path14.moveTo(101.5d, 313.5d);
      path14.curveTo(98.0d, 314.0d, 94.4d, 313.8d, 90.9d, 314.7d);
      path14.curveTo(90.0d, 314.9d, 88.7d, 314.6d, 89.0d, 316.0d);
      path14.curveTo(92.9d, 315.2d, 96.8d, 315.0d, 100.8d, 314.5d);
      path14.curveTo(101.4d, 314.4d, 102.3d, 314.1d, 101.5d, 313.5d);
      path14.closePath();
      path14.moveTo(115.0d, 315.5d);
      path14.curveTo(107.2d, 315.9d, 98.4d, 317.3d, 91.2d, 320.4d);
      path14.curveTo(90.9d, 320.5d, 87.6d, 321.7d, 88.5d, 322.5d);
      path14.curveTo(94.2d, 320.3d, 100.2d, 318.1d, 106.3d, 317.1d);
      path14.curveTo(108.8d, 316.7d, 111.8d, 316.8d, 114.3d, 316.5d);
      path14.curveTo(115.0d, 316.4d, 115.8d, 316.1d, 115.0d, 315.5d);
      path14.closePath();
      path14.moveTo(88.0d, 322.5d);
      path14.lineTo(87.3d, 322.5d);
      path14.curveTo(87.3d, 322.5d, 86.5d, 323.5d, 86.5d, 323.5d);
      path14.lineTo(88.0d, 323.5d);
      path14.curveTo(88.0d, 323.5d, 88.0d, 322.5d, 88.0d, 322.5d);
      path14.closePath();
      path14.moveTo(85.5d, 325.5d);
      path14.lineTo(85.8d, 324.0d);
      path14.lineTo(85.0d, 324.8d);
      path14.lineTo(85.0d, 325.5d);
      path14.lineTo(85.5d, 325.5d);
      path14.closePath();
      g2.setPaint(new Color(52, 52, 52));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path14);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path14);

      // Element 15: Path
      GeneralPath path15 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path15.moveTo(135.5d, 291.5d);
      path15.lineTo(136.0d, 292.0d);
      path15.lineTo(134.5d, 293.3d);
      path15.curveTo(134.3d, 293.9d, 134.6d, 294.3d, 135.2d, 294.0d);
      path15.curveTo(136.5d, 293.4d, 139.5d, 289.3d, 140.7d, 288.2d);
      path15.curveTo(146.5d, 282.7d, 160.0d, 270.8d, 166.8d, 267.6d);
      path15.curveTo(167.6d, 267.2d, 168.7d, 266.7d, 169.5d, 266.8d);
      path15.curveTo(167.4d, 268.5d, 163.9d, 270.6d, 165.3d, 273.9d);
      path15.curveTo(165.4d, 274.1d, 167.7d, 276.5d, 167.8d, 276.5d);
      path15.curveTo(169.2d, 276.8d, 169.8d, 276.5d, 170.5d, 275.5d);
      path15.curveTo(170.9d, 275.6d, 171.9d, 275.3d, 171.5d, 276.2d);
      path15.curveTo(170.7d, 278.0d, 165.8d, 281.1d, 164.0d, 282.8d);
      path15.curveTo(161.8d, 284.7d, 160.7d, 286.4d, 159.0d, 288.5d);
      path15.curveTo(158.3d, 289.4d, 158.3d, 287.8d, 157.5d, 290.5d);
      path15.curveTo(156.3d, 290.7d, 155.6d, 291.2d, 155.5d, 292.5d);
      path15.curveTo(155.3d, 292.6d, 155.2d, 292.9d, 155.0d, 293.0d);
      path15.curveTo(153.9d, 294.0d, 152.6d, 294.8d, 152.1d, 296.3d);
      path15.curveTo(151.9d, 296.9d, 153.0d, 297.5d, 153.5d, 298.0d);
      path15.curveTo(154.1d, 298.6d, 154.8d, 299.5d, 155.5d, 300.0d);
      path15.curveTo(156.4d, 300.7d, 156.9d, 301.6d, 158.2d, 301.5d);
      path15.curveTo(161.5d, 301.4d, 175.7d, 290.1d, 179.0d, 287.5d);
      path15.curveTo(180.0d, 286.8d, 180.8d, 286.4d, 181.5d, 285.8d);
      path15.curveTo(183.0d, 284.6d, 184.5d, 281.9d, 187.0d, 281.5d);
      path15.curveTo(186.2d, 284.7d, 188.6d, 289.5d, 188.5d, 292.3d);
      path15.curveTo(188.5d, 295.0d, 185.3d, 296.5d, 183.8d, 298.0d);
      path15.curveTo(179.3d, 302.4d, 174.4d, 308.5d, 168.9d, 311.1d);
      path15.curveTo(164.6d, 313.1d, 159.5d, 314.6d, 155.0d, 316.3d);
      path15.curveTo(154.2d, 316.6d, 153.2d, 316.1d, 153.5d, 317.5d);
      path15.curveTo(149.7d, 318.8d, 148.9d, 318.6d, 146.6d, 322.3d);
      path15.curveTo(139.8d, 333.1d, 127.0d, 347.5d, 117.3d, 356.0d);
      path15.curveTo(112.7d, 360.0d, 107.2d, 362.7d, 102.5d, 366.5d);
      path15.lineTo(102.0d, 366.5d);
      path15.curveTo(99.4d, 362.0d, 97.0d, 356.5d, 94.1d, 352.2d);
      path15.curveTo(93.7d, 351.6d, 94.3d, 351.5d, 93.0d, 351.5d);
      path15.curveTo(92.7d, 348.4d, 93.3d, 345.9d, 94.0d, 343.0d);
      path15.curveTo(95.8d, 336.0d, 100.3d, 327.2d, 106.7d, 323.4d);
      path15.curveTo(109.5d, 321.7d, 112.7d, 321.1d, 115.5d, 319.5d);
      path15.curveTo(115.7d, 319.5d, 115.9d, 319.5d, 116.0d, 319.5d);
      path15.curveTo(118.3d, 319.1d, 125.5d, 308.3d, 127.5d, 305.8d);
      path15.curveTo(128.4d, 304.7d, 130.9d, 302.8d, 131.3d, 302.0d);
      path15.curveTo(132.6d, 299.2d, 130.0d, 292.6d, 135.5d, 291.5d);
      path15.closePath();
      path15.moveTo(147.5d, 319.0d);
      path15.lineTo(142.5d, 315.5d);
      path15.curveTo(141.9d, 316.1d, 146.1d, 320.2d, 147.2d, 320.0d);
      path15.lineTo(147.5d, 319.0d);
      path15.closePath();
      g2.setPaint(new Color(217, 178, 150));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path15);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path15);

      // Element 16: Path
      GeneralPath path16 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path16.moveTo(153.5d, 317.5d);
      path16.curveTo(169.2d, 312.4d, 173.3d, 310.5d, 184.8d, 298.5d);
      path16.curveTo(185.4d, 297.8d, 185.5d, 296.2d, 186.5d, 297.3d);
      path16.lineTo(185.0d, 311.0d);
      path16.curveTo(184.4d, 312.0d, 183.1d, 312.6d, 182.0d, 313.0d);
      path16.curveTo(177.0d, 315.0d, 171.0d, 316.0d, 166.5d, 319.2d);
      path16.curveTo(166.8d, 319.9d, 167.8d, 319.2d, 168.3d, 319.1d);
      path16.curveTo(172.4d, 317.8d, 176.6d, 315.1d, 181.0d, 314.5d);
      path16.curveTo(172.9d, 322.5d, 168.6d, 324.6d, 157.5d, 327.0d);
      path16.curveTo(156.1d, 327.3d, 153.9d, 326.9d, 152.5d, 327.8d);
      path16.curveTo(152.3d, 329.0d, 153.4d, 328.5d, 154.1d, 328.4d);
      path16.curveTo(155.2d, 328.2d, 156.5d, 327.9d, 157.5d, 327.5d);
      path16.curveTo(153.4d, 340.1d, 142.5d, 348.7d, 136.0d, 359.5d);
      path16.curveTo(134.3d, 361.2d, 131.8d, 364.3d, 130.0d, 365.5d);
      path16.curveTo(129.9d, 365.6d, 129.6d, 365.4d, 129.5d, 365.5d);
      path16.curveTo(124.9d, 368.2d, 116.6d, 369.3d, 111.2d, 369.5d);
      path16.curveTo(107.8d, 369.6d, 106.0d, 369.2d, 103.0d, 367.5d);
      path16.curveTo(102.8d, 367.2d, 102.6d, 366.9d, 102.5d, 366.5d);
      path16.curveTo(107.1d, 362.7d, 112.7d, 360.0d, 117.3d, 356.0d);
      path16.curveTo(127.0d, 347.5d, 139.8d, 333.1d, 146.6d, 322.3d);
      path16.curveTo(148.9d, 318.6d, 149.6d, 318.8d, 153.5d, 317.5d);
      path16.closePath();
      g2.setPaint(new Color(209, 164, 135));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path16);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path16);

      // Element 17: Path
      GeneralPath path17 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path17.moveTo(102.0d, 366.5d);
      path17.lineTo(102.5d, 366.5d);
      path17.curveTo(102.6d, 366.9d, 102.8d, 367.2d, 103.0d, 367.5d);
      path17.curveTo(103.8d, 369.3d, 104.4d, 369.8d, 106.5d, 370.3d);
      path17.curveTo(110.9d, 371.2d, 124.1d, 369.7d, 128.0d, 367.3d);
      path17.curveTo(128.8d, 366.8d, 129.6d, 366.7d, 129.5d, 365.5d);
      path17.curveTo(129.6d, 365.4d, 129.9d, 365.6d, 130.0d, 365.5d);
      path17.curveTo(131.6d, 365.3d, 130.9d, 365.8d, 130.5d, 366.7d);
      path17.curveTo(127.4d, 373.7d, 121.2d, 379.5d, 115.0d, 383.7d);
      path17.curveTo(108.7d, 387.9d, 102.6d, 390.7d, 97.6d, 383.2d);
      path17.curveTo(94.7d, 378.7d, 91.2d, 361.1d, 91.5d, 355.8d);
      path17.curveTo(91.6d, 354.9d, 92.0d, 351.8d, 92.5d, 351.5d);
      path17.lineTo(100.6d, 365.7d);
      path17.lineTo(102.0d, 366.5d);
      path17.closePath();
      g2.setPaint(new Color(53, 53, 52));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path17);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path17);

      // Element 18: Path
      GeneralPath path18 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path18.moveTo(152.5d, 188.0d);
      path18.curveTo(153.0d, 185.0d, 157.5d, 179.6d, 160.8d, 179.5d);
      path18.lineTo(161.5d, 180.5d);
      path18.curveTo(159.4d, 185.7d, 159.9d, 191.6d, 159.0d, 197.0d);
      path18.curveTo(158.7d, 197.0d, 158.3d, 197.0d, 158.0d, 197.0d);
      path18.curveTo(157.8d, 197.0d, 157.7d, 197.0d, 157.5d, 197.0d);
      path18.curveTo(153.9d, 194.9d, 151.7d, 192.4d, 152.5d, 188.0d);
      path18.closePath();
      g2.setPaint(new Color(231, 214, 128));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path18);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path18);

      // Element 19: Path
      GeneralPath path19 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path19.moveTo(115.5d, 319.5d);
      path19.curveTo(112.7d, 321.1d, 109.5d, 321.7d, 106.7d, 323.4d);
      path19.curveTo(100.3d, 327.2d, 95.8d, 336.0d, 94.0d, 343.0d);
      path19.lineTo(93.5d, 343.0d);
      path19.curveTo(93.7d, 342.4d, 93.4d, 341.4d, 93.7d, 340.5d);
      path19.curveTo(97.3d, 328.7d, 103.3d, 321.7d, 115.5d, 319.5d);
      path19.closePath();
      g2.setPaint(new Color(35, 33, 30));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path19);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path19);

      // Element 20: Path
      GeneralPath path20 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path20.moveTo(93.0d, 351.5d);
      path20.curveTo(94.2d, 351.5d, 93.7d, 351.6d, 94.1d, 352.2d);
      path20.curveTo(97.0d, 356.5d, 99.3d, 362.0d, 102.0d, 366.5d);
      path20.lineTo(100.6d, 365.7d);
      path20.lineTo(92.5d, 351.5d);
      path20.curveTo(92.6d, 351.5d, 92.8d, 351.5d, 93.0d, 351.5d);
      path20.closePath();
      g2.setPaint(new Color(35, 33, 30));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path20);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path20);

      // Element 21: Path
      GeneralPath path21 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path21.moveTo(332.0d, 387.5d);
      path21.curveTo(336.7d, 386.8d, 342.9d, 382.9d, 346.7d, 379.9d);
      path21.curveTo(354.8d, 373.6d, 351.7d, 362.6d, 346.4d, 355.3d);
      path21.curveTo(345.4d, 354.0d, 344.4d, 351.8d, 342.5d, 352.0d);
      path21.curveTo(342.2d, 351.7d, 341.8d, 351.3d, 341.5d, 351.0d);
      path21.curveTo(341.7d, 349.7d, 341.1d, 349.6d, 340.4d, 348.9d);
      path21.curveTo(334.2d, 343.4d, 318.8d, 338.9d, 311.0d, 341.5d);
      path21.curveTo(311.3d, 333.9d, 311.0d, 327.2d, 308.7d, 320.0d);
      path21.curveTo(307.4d, 315.9d, 304.3d, 310.9d, 303.3d, 306.9d);
      path21.curveTo(302.0d, 301.2d, 307.0d, 284.4d, 308.6d, 277.9d);
      path21.curveTo(311.0d, 268.6d, 313.6d, 258.7d, 316.4d, 249.6d);
      path21.curveTo(318.3d, 243.2d, 321.6d, 236.2d, 323.3d, 230.0d);
      path21.curveTo(323.5d, 229.4d, 324.0d, 228.3d, 322.7d, 228.5d);
      path21.curveTo(319.8d, 236.2d, 317.0d, 244.0d, 314.6d, 251.9d);
      path21.curveTo(310.7d, 265.2d, 302.8d, 290.3d, 302.0d, 303.3d);
      path21.curveTo(301.7d, 308.5d, 307.4d, 317.9d, 308.8d, 323.9d);
      path21.curveTo(310.3d, 330.1d, 309.6d, 335.4d, 310.5d, 341.5d);
      path21.curveTo(299.7d, 346.0d, 295.1d, 350.9d, 281.7d, 351.0d);
      path21.curveTo(278.3d, 351.1d, 274.6d, 349.7d, 271.0d, 351.0d);
      path21.curveTo(269.5d, 345.8d, 269.5d, 340.6d, 269.0d, 335.2d);
      path21.curveTo(268.5d, 329.6d, 268.8d, 324.4d, 268.5d, 319.3d);
      path21.curveTo(268.2d, 316.3d, 265.9d, 312.3d, 265.1d, 309.1d);
      path21.curveTo(264.0d, 304.3d, 264.5d, 299.3d, 263.4d, 294.9d);
      path21.curveTo(262.7d, 292.3d, 261.0d, 290.2d, 260.2d, 287.5d);
      path21.curveTo(259.4d, 284.1d, 259.5d, 278.1d, 258.0d, 275.4d);
      path21.curveTo(257.6d, 274.7d, 254.1d, 273.5d, 254.5d, 275.5d);
      path21.curveTo(254.3d, 275.5d, 254.1d, 275.5d, 254.0d, 275.5d);
      path21.curveTo(252.1d, 275.9d, 252.9d, 276.6d, 252.5d, 277.0d);
      path21.curveTo(244.2d, 286.4d, 252.4d, 301.3d, 249.4d, 313.1d);
      path21.curveTo(248.4d, 316.7d, 246.6d, 316.2d, 244.0d, 318.0d);
      path21.curveTo(242.1d, 318.2d, 240.5d, 315.6d, 239.6d, 314.2d);
      path21.curveTo(235.6d, 307.8d, 234.1d, 299.0d, 231.2d, 292.0d);
      path21.curveTo(230.4d, 289.9d, 224.3d, 278.7d, 225.5d, 277.5d);
      path21.curveTo(235.5d, 277.4d, 246.2d, 277.8d, 254.5d, 271.2d);
      path21.lineTo(257.0d, 268.0d);
      path21.curveTo(257.2d, 267.8d, 257.3d, 267.7d, 257.5d, 267.5d);
      path21.curveTo(259.8d, 267.6d, 262.0d, 263.3d, 262.7d, 261.5d);
      path21.curveTo(264.2d, 257.9d, 264.6d, 254.8d, 264.0d, 251.0d);
      path21.curveTo(263.6d, 248.9d, 262.0d, 243.7d, 260.5d, 242.3d);
      path21.curveTo(260.2d, 242.0d, 259.6d, 242.1d, 259.5d, 242.0d);
      path21.curveTo(259.3d, 241.8d, 259.2d, 241.6d, 259.0d, 241.5d);
      path21.curveTo(260.1d, 241.1d, 258.9d, 240.1d, 258.5d, 239.7d);
      path21.curveTo(257.5d, 238.8d, 255.4d, 237.9d, 255.0d, 237.3d);
      path21.curveTo(254.1d, 236.1d, 254.0d, 233.1d, 253.0d, 231.8d);
      path21.curveTo(252.6d, 231.3d, 252.1d, 231.6d, 252.0d, 231.5d);
      path21.curveTo(251.5d, 231.1d, 251.4d, 231.1d, 251.0d, 230.5d);
      path21.curveTo(250.8d, 230.3d, 250.7d, 230.2d, 250.5d, 230.0d);
      path21.curveTo(250.4d, 229.8d, 250.5d, 229.6d, 250.5d, 229.5d);
      path21.curveTo(249.5d, 226.7d, 247.2d, 227.0d, 244.5d, 227.0d);
      path21.curveTo(244.6d, 221.6d, 243.7d, 216.2d, 242.8d, 211.0d);
      path21.curveTo(242.5d, 209.7d, 241.6d, 208.2d, 241.5d, 207.5d);
      path21.curveTo(241.1d, 205.4d, 239.9d, 199.4d, 240.0d, 197.8d);
      path21.curveTo(240.3d, 194.6d, 245.1d, 185.7d, 246.5d, 181.7d);
      path21.curveTo(248.6d, 175.5d, 250.5d, 168.3d, 250.0d, 161.7d);
      path21.curveTo(249.8d, 158.2d, 247.7d, 154.8d, 249.0d, 151.5d);
      path21.curveTo(253.1d, 151.8d, 263.8d, 154.2d, 265.2d, 158.6d);
      path21.curveTo(265.7d, 160.4d, 264.9d, 165.0d, 266.3d, 165.4d);
      path21.curveTo(266.7d, 165.6d, 273.1d, 164.9d, 274.8d, 165.0d);
      path21.curveTo(278.2d, 165.1d, 286.4d, 166.1d, 289.0d, 168.3d);
      path21.curveTo(293.2d, 171.9d, 289.1d, 181.1d, 289.7d, 182.0d);
      path21.curveTo(303.2d, 188.3d, 323.3d, 191.2d, 333.5d, 202.7d);
      path21.curveTo(338.6d, 208.5d, 342.3d, 218.4d, 345.0d, 225.7d);
      path21.curveTo(349.5d, 238.1d, 355.8d, 255.0d, 358.6d, 267.6d);
      path21.curveTo(362.2d, 283.1d, 359.8d, 302.0d, 363.2d, 318.0d);
      path21.curveTo(365.4d, 327.2d, 379.6d, 330.0d, 376.8d, 341.0d);
      path21.curveTo(376.0d, 343.7d, 372.4d, 345.4d, 372.1d, 346.3d);
      path21.curveTo(371.6d, 347.8d, 372.2d, 351.3d, 371.8d, 353.5d);
      path21.curveTo(371.3d, 356.2d, 368.0d, 365.1d, 366.9d, 368.1d);
      path21.curveTo(363.0d, 378.0d, 342.2d, 390.1d, 332.0d, 387.5d);
      path21.closePath();
      path21.moveTo(253.8d, 193.0d);
      path21.lineTo(253.8d, 193.5d);
      path21.curveTo(254.1d, 193.3d, 254.1d, 193.2d, 253.8d, 193.0d);
      path21.closePath();
      path21.moveTo(253.0d, 193.5d);
      path21.curveTo(252.7d, 193.2d, 247.7d, 196.2d, 248.5d, 197.0d);
      path21.curveTo(249.3d, 197.0d, 253.7d, 194.2d, 253.0d, 193.5d);
      path21.closePath();
      path21.moveTo(248.0d, 197.0d);
      path21.curveTo(247.7d, 196.7d, 244.1d, 199.3d, 243.4d, 199.7d);
      path21.curveTo(243.2d, 199.9d, 241.6d, 200.2d, 242.5d, 201.0d);
      path21.curveTo(243.0d, 201.4d, 248.8d, 197.8d, 248.0d, 197.0d);
      path21.closePath();
      path21.moveTo(363.0d, 336.0d);
      path21.curveTo(362.7d, 335.7d, 355.9d, 334.8d, 354.7d, 334.6d);
      path21.curveTo(342.0d, 332.7d, 328.7d, 331.7d, 316.0d, 333.0d);
      path21.curveTo(315.6d, 334.8d, 317.6d, 333.5d, 318.7d, 333.5d);
      path21.curveTo(334.0d, 333.3d, 347.4d, 334.4d, 362.4d, 336.9d);
      path21.curveTo(363.4d, 337.1d, 363.3d, 336.3d, 363.0d, 336.0d);
      path21.closePath();
      g2.setPaint(new Color(53, 52, 52));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path21);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path21);

      // Element 22: Path
      GeneralPath path22 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path22.moveTo(233.0d, 137.5d);
      path22.curveTo(234.3d, 138.2d, 236.0d, 139.8d, 237.5d, 140.5d);
      path22.curveTo(237.7d, 140.8d, 237.6d, 141.5d, 238.2d, 142.0d);
      path22.curveTo(238.7d, 142.4d, 239.3d, 142.4d, 239.5d, 142.5d);
      path22.curveTo(240.6d, 144.5d, 242.6d, 146.5d, 243.8d, 148.2d);
      path22.curveTo(244.5d, 149.2d, 246.6d, 152.5d, 247.0d, 153.5d);
      path22.curveTo(251.0d, 162.6d, 248.1d, 173.9d, 245.0d, 182.8d);
      path22.curveTo(243.6d, 186.9d, 238.9d, 195.5d, 239.0d, 199.2d);
      path22.curveTo(239.0d, 200.6d, 239.7d, 203.5d, 240.0d, 205.0d);
      path22.lineTo(237.0d, 206.0d);
      path22.curveTo(236.2d, 203.6d, 235.7d, 202.2d, 234.5d, 200.0d);
      path22.curveTo(234.4d, 199.8d, 234.8d, 199.0d, 234.2d, 199.0d);
      path22.curveTo(229.0d, 200.7d, 223.7d, 202.0d, 218.2d, 202.5d);
      path22.curveTo(217.3d, 202.6d, 216.2d, 201.9d, 216.5d, 203.5d);
      path22.curveTo(215.9d, 203.5d, 215.2d, 203.9d, 214.5d, 204.0d);
      path22.curveTo(212.7d, 202.8d, 208.1d, 204.3d, 207.5d, 201.0d);
      path22.curveTo(231.4d, 197.2d, 249.5d, 171.3d, 235.7d, 148.5d);
      path22.curveTo(231.9d, 142.2d, 226.9d, 138.7d, 220.8d, 134.9d);
      path22.curveTo(220.2d, 134.5d, 219.2d, 134.7d, 219.5d, 133.5d);
      path22.curveTo(223.8d, 133.9d, 229.2d, 135.5d, 233.0d, 137.5d);
      path22.closePath();
      g2.setPaint(new Color(153, 174, 132));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path22);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path22);

      // Element 23: Path
      GeneralPath path23 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path23.moveTo(250.0d, 134.0d);
      path23.curveTo(249.7d, 137.4d, 249.9d, 141.7d, 249.5d, 145.0d);
      path23.curveTo(249.4d, 146.0d, 248.8d, 148.3d, 248.5d, 149.5d);
      path23.curveTo(248.2d, 150.7d, 248.1d, 151.9d, 247.5d, 153.0d);
      path23.curveTo(246.2d, 151.4d, 245.3d, 149.2d, 244.2d, 147.6d);
      path23.curveTo(243.6d, 146.8d, 241.0d, 143.4d, 240.5d, 143.0d);
      path23.curveTo(240.2d, 140.5d, 241.4d, 138.2d, 242.5d, 136.0d);
      path23.curveTo(245.7d, 130.0d, 245.8d, 130.0d, 247.0d, 123.0d);
      path23.curveTo(247.2d, 121.9d, 246.8d, 121.8d, 247.5d, 120.5d);
      path23.curveTo(247.7d, 120.0d, 248.7d, 118.9d, 249.2d, 119.0d);
      path23.curveTo(252.5d, 122.9d, 250.4d, 129.2d, 250.0d, 134.0d);
      path23.closePath();
      g2.setPaint(new Color(208, 165, 139));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path23);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path23);

      // Element 24: Path
      GeneralPath path24 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path24.moveTo(240.0d, 205.0d);
      path24.curveTo(240.1d, 205.5d, 240.8d, 206.4d, 241.0d, 207.5d);
      path24.curveTo(242.3d, 214.4d, 243.6d, 220.9d, 243.5d, 228.0d);
      path24.curveTo(243.4d, 231.5d, 243.0d, 234.9d, 242.9d, 238.4d);
      path24.curveTo(240.7d, 239.1d, 238.6d, 240.1d, 236.5d, 241.0d);
      path24.curveTo(237.2d, 238.3d, 238.3d, 235.8d, 238.8d, 233.1d);
      path24.curveTo(240.7d, 223.7d, 239.9d, 215.1d, 237.0d, 206.0d);
      path24.lineTo(240.0d, 205.0d);
      path24.closePath();
      g2.setPaint(new Color(165, 184, 144));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path24);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path24);

      // Element 25: Path
      GeneralPath path25 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path25.moveTo(332.0d, 387.5d);
      path25.curveTo(331.5d, 387.4d, 330.9d, 387.7d, 330.5d, 387.0d);
      path25.curveTo(335.8d, 386.0d, 347.9d, 379.6d, 349.7d, 374.5d);
      path25.curveTo(352.5d, 367.1d, 347.7d, 357.3d, 342.5d, 352.0d);
      path25.curveTo(344.4d, 351.8d, 345.4d, 354.0d, 346.4d, 355.3d);
      path25.curveTo(351.7d, 362.6d, 354.8d, 373.6d, 346.7d, 379.9d);
      path25.curveTo(342.9d, 382.8d, 336.7d, 386.8d, 332.0d, 387.5d);
      path25.closePath();
      g2.setPaint(new Color(33, 32, 29));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path25);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path25);

      // Element 26: Path
      GeneralPath path26 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path26.moveTo(241.5d, 207.5d);
      path26.curveTo(241.7d, 208.2d, 242.5d, 209.7d, 242.8d, 211.0d);
      path26.curveTo(243.7d, 216.2d, 244.6d, 221.6d, 244.5d, 227.0d);
      path26.curveTo(247.2d, 227.0d, 249.5d, 226.7d, 250.5d, 229.5d);
      path26.curveTo(248.2d, 228.2d, 247.2d, 228.0d, 244.5d, 228.0d);
      path26.curveTo(244.2d, 228.0d, 243.8d, 228.0d, 243.5d, 228.0d);
      path26.curveTo(243.6d, 220.9d, 242.3d, 214.5d, 241.0d, 207.5d);
      path26.lineTo(241.5d, 207.5d);
      path26.closePath();
      g2.setPaint(new Color(35, 33, 30));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path26);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path26);

      // Element 27: Rect
      Shape shape27 = new Rectangle2D.Double(328.0d, 388.0d, 3.0d, 0.5d);
      g2.setPaint(new Color(53, 52, 52));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(shape27);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(shape27);

      // Element 28: Path
      GeneralPath path28 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path28.moveTo(240.5d, 143.0d);
      path28.curveTo(241.0d, 143.4d, 243.6d, 146.8d, 244.2d, 147.6d);
      path28.curveTo(245.3d, 149.2d, 246.2d, 151.5d, 247.5d, 153.0d);
      path28.curveTo(247.4d, 153.2d, 247.2d, 153.3d, 247.0d, 153.5d);
      path28.curveTo(246.6d, 152.5d, 244.5d, 149.2d, 243.8d, 148.2d);
      path28.curveTo(242.6d, 146.5d, 240.6d, 144.5d, 239.5d, 142.5d);
      path28.curveTo(239.8d, 142.7d, 240.2d, 142.8d, 240.5d, 143.0d);
      path28.closePath();
      g2.setPaint(new Color(66, 62, 53));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path28);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path28);

      // Element 29: Path
      GeneralPath path29 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path29.moveTo(260.0d, 118.0d);
      path29.curveTo(260.0d, 118.0d, 259.3d, 117.9d, 259.2d, 118.2d);
      path29.curveTo(258.7d, 119.0d, 258.3d, 120.2d, 257.4d, 120.8d);
      path29.curveTo(255.7d, 121.9d, 255.5d, 119.8d, 255.0d, 123.5d);
      path29.curveTo(255.0d, 123.7d, 255.0d, 123.8d, 255.0d, 124.0d);
      path29.curveTo(253.5d, 125.6d, 253.2d, 128.1d, 252.2d, 130.0d);
      path29.curveTo(251.9d, 130.7d, 252.3d, 131.3d, 251.0d, 131.0d);
      path29.curveTo(251.4d, 128.2d, 252.4d, 125.6d, 252.0d, 122.7d);
      path29.curveTo(251.7d, 120.5d, 249.7d, 119.1d, 250.5d, 117.2d);
      path29.curveTo(248.9d, 117.3d, 247.8d, 119.1d, 247.5d, 120.5d);
      path29.curveTo(246.8d, 121.8d, 247.2d, 121.9d, 247.0d, 123.0d);
      path29.curveTo(245.6d, 123.3d, 246.2d, 122.3d, 245.8d, 121.5d);
      path29.curveTo(245.2d, 119.9d, 244.4d, 118.4d, 244.0d, 116.8d);
      path29.curveTo(245.0d, 116.7d, 246.3d, 114.6d, 246.5d, 114.5d);
      path29.curveTo(249.1d, 113.6d, 248.3d, 115.4d, 250.5d, 112.0d);
      path29.curveTo(252.3d, 111.7d, 251.6d, 111.1d, 252.0d, 110.5d);
      path29.curveTo(252.7d, 110.2d, 253.7d, 110.4d, 254.5d, 109.5d);
      path29.curveTo(254.4d, 110.3d, 254.7d, 111.3d, 254.5d, 112.0d);
      path29.curveTo(254.5d, 112.1d, 254.1d, 112.3d, 254.0d, 112.5d);
      path29.curveTo(253.9d, 112.7d, 253.5d, 113.5d, 253.5d, 113.5d);
      path29.curveTo(253.3d, 113.7d, 253.1d, 113.8d, 253.0d, 114.0d);
      path29.curveTo(252.8d, 114.2d, 252.6d, 114.3d, 252.5d, 114.5d);
      path29.curveTo(252.4d, 114.6d, 251.8d, 114.4d, 251.6d, 114.6d);
      path29.curveTo(251.3d, 114.8d, 251.6d, 115.4d, 251.5d, 115.5d);
      path29.curveTo(251.4d, 115.7d, 251.1d, 115.8d, 251.0d, 116.0d);
      path29.curveTo(250.9d, 116.1d, 250.6d, 116.1d, 250.5d, 116.3d);
      path29.lineTo(251.0d, 117.0d);
      path29.curveTo(251.1d, 116.7d, 250.9d, 116.2d, 251.0d, 116.0d);
      path29.curveTo(251.1d, 115.8d, 251.3d, 115.6d, 251.5d, 115.5d);
      path29.curveTo(251.6d, 115.4d, 252.1d, 115.6d, 252.4d, 115.4d);
      path29.curveTo(252.6d, 115.1d, 252.4d, 114.6d, 252.5d, 114.5d);
      path29.curveTo(252.6d, 114.3d, 252.8d, 114.2d, 253.0d, 114.0d);
      path29.curveTo(253.1d, 113.8d, 253.3d, 113.7d, 253.5d, 113.5d);
      path29.curveTo(253.8d, 113.1d, 253.9d, 112.7d, 254.0d, 112.5d);
      path29.curveTo(254.1d, 112.3d, 254.4d, 112.2d, 254.5d, 112.0d);
      path29.curveTo(255.1d, 111.0d, 256.6d, 110.2d, 254.7d, 108.5d);
      path29.curveTo(253.3d, 108.3d, 252.7d, 109.6d, 252.0d, 110.5d);
      path29.curveTo(250.1d, 111.2d, 251.1d, 111.0d, 250.5d, 112.0d);
      path29.curveTo(248.6d, 112.3d, 246.0d, 111.8d, 244.5d, 113.5d);
      path29.curveTo(244.3d, 113.5d, 244.2d, 113.5d, 244.0d, 113.5d);
      path29.curveTo(243.0d, 113.5d, 242.0d, 113.5d, 241.0d, 113.5d);
      path29.curveTo(233.2d, 113.4d, 230.2d, 113.1d, 223.9d, 108.3d);
      path29.curveTo(223.5d, 107.9d, 222.6d, 108.1d, 222.5d, 108.0d);
      path29.curveTo(221.7d, 107.5d, 217.6d, 100.5d, 217.1d, 99.2d);
      path29.curveTo(216.2d, 96.9d, 216.2d, 95.0d, 215.2d, 93.0d);
      path29.curveTo(213.9d, 92.8d, 211.8d, 98.6d, 211.5d, 99.8d);
      path29.curveTo(209.7d, 105.9d, 210.2d, 112.5d, 215.7d, 116.5d);
      path29.curveTo(215.9d, 116.7d, 218.3d, 118.2d, 218.8d, 118.4d);
      path29.curveTo(220.0d, 119.1d, 222.8d, 120.2d, 224.0d, 119.0d);
      path29.lineTo(226.5d, 119.0d);
      path29.curveTo(226.5d, 119.0d, 226.5d, 119.7d, 226.5d, 119.7d);
      path29.curveTo(225.6d, 120.8d, 224.7d, 121.4d, 223.5d, 122.0d);
      path29.lineTo(216.3d, 122.0d);
      path29.curveTo(215.2d, 121.8d, 211.6d, 120.5d, 210.8d, 119.9d);
      path29.curveTo(209.9d, 119.3d, 209.6d, 118.2d, 209.3d, 118.1d);
      path29.curveTo(208.8d, 117.8d, 207.9d, 118.2d, 207.6d, 117.9d);
      path29.curveTo(207.2d, 117.6d, 204.1d, 111.3d, 203.8d, 110.4d);
      path29.curveTo(202.2d, 105.2d, 202.8d, 99.7d, 204.2d, 94.5d);
      path29.curveTo(205.6d, 89.3d, 210.4d, 80.1d, 209.5d, 74.7d);
      path29.curveTo(209.3d, 73.6d, 208.5d, 73.6d, 208.5d, 73.5d);
      path29.curveTo(208.4d, 73.3d, 208.1d, 72.7d, 208.0d, 72.5d);
      path29.curveTo(207.7d, 72.0d, 208.3d, 71.3d, 207.0d, 70.2d);
      path29.curveTo(204.3d, 68.2d, 200.5d, 69.0d, 197.6d, 69.8d);
      path29.curveTo(196.4d, 70.2d, 194.9d, 71.3d, 194.1d, 71.4d);
      path29.curveTo(192.9d, 71.6d, 191.4d, 70.7d, 190.2d, 70.5d);
      path29.curveTo(178.9d, 69.4d, 174.6d, 78.8d, 177.0d, 88.7d);
      path29.curveTo(177.8d, 91.9d, 180.0d, 94.5d, 181.0d, 97.0d);
      path29.curveTo(182.0d, 99.7d, 182.2d, 104.1d, 182.0d, 107.0d);
      path29.curveTo(182.0d, 107.2d, 182.0d, 107.3d, 182.0d, 107.5d);
      path29.lineTo(180.5d, 110.0d);
      path29.curveTo(181.8d, 107.8d, 177.8d, 96.6d, 175.7d, 97.0d);
      path29.curveTo(175.0d, 97.5d, 175.1d, 98.0d, 175.0d, 98.7d);
      path29.curveTo(174.2d, 103.9d, 173.4d, 107.8d, 170.4d, 112.2d);
      path29.curveTo(169.2d, 114.1d, 166.6d, 116.1d, 165.8d, 117.5d);
      path29.curveTo(164.8d, 119.2d, 165.2d, 122.1d, 164.5d, 122.8d);
      path29.curveTo(163.9d, 123.3d, 160.9d, 123.7d, 159.8d, 124.1d);
      path29.curveTo(156.5d, 125.3d, 154.3d, 126.7d, 150.2d, 126.0d);
      path29.curveTo(146.7d, 125.3d, 141.5d, 118.8d, 140.2d, 115.5d);
      path29.curveTo(139.5d, 113.8d, 139.3d, 111.3d, 138.7d, 110.0d);
      path29.curveTo(138.3d, 109.1d, 136.8d, 108.5d, 136.5d, 107.1d);
      path29.curveTo(136.3d, 105.8d, 137.0d, 101.3d, 137.0d, 99.2d);
      path29.curveTo(136.8d, 91.6d, 136.2d, 78.1d, 141.0d, 71.7d);
      path29.curveTo(143.5d, 68.3d, 149.0d, 66.8d, 149.0d, 62.5d);
      path29.lineTo(144.0d, 63.5d);
      path29.curveTo(143.6d, 62.1d, 145.2d, 61.2d, 145.4d, 60.1d);
      path29.curveTo(145.6d, 58.8d, 144.3d, 56.9d, 145.2d, 55.4d);
      path29.curveTo(145.6d, 54.8d, 148.7d, 54.9d, 149.9d, 54.6d);
      path29.curveTo(154.6d, 53.5d, 157.2d, 49.9d, 156.5d, 45.0d);
      path29.curveTo(167.8d, 45.2d, 174.7d, 35.2d, 185.0d, 32.2d);
      path29.curveTo(195.2d, 29.2d, 203.8d, 33.4d, 213.7d, 34.0d);
      path29.curveTo(220.5d, 34.3d, 223.9d, 32.2d, 231.0d, 35.7d);
      path29.curveTo(252.1d, 46.0d, 264.9d, 63.5d, 263.5d, 87.7d);
      path29.curveTo(263.1d, 94.1d, 261.0d, 101.0d, 260.5d, 107.2d);
      path29.curveTo(260.3d, 109.8d, 261.0d, 116.1d, 260.0d, 118.0d);
      path29.closePath();
      g2.setPaint(new Color(53, 53, 52));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path29);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path29);

      // Element 30: Path
      GeneralPath path30 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path30.moveTo(208.0d, 72.5d);
      path30.curveTo(208.1d, 72.7d, 208.4d, 73.3d, 208.5d, 73.5d);
      path30.curveTo(207.8d, 89.5d, 193.3d, 106.2d, 209.0d, 119.8d);
      path30.curveTo(204.0d, 123.1d, 200.0d, 120.5d, 195.1d, 119.6d);
      path30.curveTo(193.0d, 119.2d, 190.7d, 119.1d, 188.5d, 119.0d);
      path30.curveTo(187.8d, 119.0d, 187.2d, 119.0d, 186.5d, 119.0d);
      path30.curveTo(185.7d, 119.0d, 184.8d, 119.0d, 184.0d, 119.0d);
      path30.curveTo(182.6d, 119.1d, 181.1d, 118.9d, 180.0d, 120.0d);
      path30.curveTo(179.4d, 119.9d, 179.0d, 119.5d, 177.8d, 119.5d);
      path30.curveTo(175.1d, 119.7d, 171.0d, 120.9d, 168.5d, 122.0d);
      path30.curveTo(167.7d, 122.0d, 166.8d, 122.0d, 166.0d, 122.0d);
      path30.curveTo(165.9d, 121.3d, 166.0d, 120.6d, 166.2d, 119.9d);
      path30.curveTo(166.2d, 119.5d, 166.4d, 118.3d, 166.6d, 118.1d);
      path30.curveTo(166.7d, 118.0d, 169.4d, 117.0d, 169.5d, 117.0d);
      path30.curveTo(169.8d, 117.0d, 170.2d, 117.0d, 170.5d, 117.0d);
      path30.curveTo(173.4d, 116.7d, 176.4d, 117.1d, 179.3d, 116.5d);
      path30.lineTo(193.0d, 117.5d);
      path30.curveTo(191.8d, 112.5d, 185.0d, 111.5d, 180.5d, 111.5d);
      path30.curveTo(181.6d, 110.0d, 181.8d, 109.3d, 182.0d, 107.5d);
      path30.curveTo(182.0d, 107.3d, 182.0d, 107.2d, 182.0d, 107.0d);
      path30.curveTo(184.3d, 104.2d, 182.5d, 100.0d, 181.5d, 97.0d);
      path30.curveTo(180.3d, 93.2d, 177.8d, 89.2d, 177.5d, 85.3d);
      path30.curveTo(176.8d, 77.6d, 180.9d, 70.9d, 189.2d, 71.5d);
      path30.curveTo(194.5d, 71.9d, 192.2d, 72.6d, 197.7d, 71.0d);
      path30.curveTo(201.7d, 69.8d, 204.8d, 69.2d, 208.0d, 72.5d);
      path30.closePath();
      g2.setPaint(new Color(217, 179, 151));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path30);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path30);

      // Element 31: Path
      GeneralPath path31 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path31.moveTo(197.5d, 136.0d);
      path31.curveTo(195.6d, 136.1d, 194.4d, 137.4d, 192.4d, 137.0d);
      path31.curveTo(192.1d, 136.9d, 189.9d, 134.2d, 187.5d, 133.7d);
      path31.curveTo(183.6d, 132.9d, 179.0d, 137.5d, 178.0d, 141.0d);
      path31.curveTo(177.9d, 141.4d, 178.1d, 142.0d, 178.0d, 142.5d);
      path31.curveTo(174.7d, 142.5d, 172.6d, 141.8d, 170.0d, 140.0d);
      path31.curveTo(169.8d, 139.9d, 169.7d, 139.7d, 169.5d, 139.5d);
      path31.curveTo(167.4d, 135.3d, 163.8d, 129.0d, 166.5d, 124.5d);
      path31.curveTo(168.9d, 124.3d, 171.2d, 123.0d, 173.5d, 122.5d);
      path31.curveTo(178.8d, 121.4d, 192.7d, 120.2d, 197.2d, 123.1d);
      path31.curveTo(200.8d, 125.3d, 198.5d, 132.6d, 197.5d, 136.0d);
      path31.closePath();
      g2.setPaint(new Color(206, 187, 172));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path31);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path31);

      // Element 32: Path
      GeneralPath path32 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path32.moveTo(261.0d, 118.5d);
      path32.curveTo(265.1d, 120.9d, 261.5d, 128.8d, 259.7d, 132.0d);
      path32.curveTo(257.8d, 135.3d, 254.9d, 140.2d, 250.5d, 139.0d);
      path32.curveTo(251.3d, 137.4d, 250.7d, 135.5d, 251.0d, 134.0d);
      path32.curveTo(251.6d, 130.7d, 254.3d, 128.3d, 255.0d, 124.0d);
      path32.curveTo(255.0d, 123.8d, 255.0d, 123.7d, 255.0d, 123.5d);
      path32.curveTo(256.5d, 121.5d, 259.5d, 120.8d, 260.0d, 118.0d);
      path32.curveTo(260.1d, 117.9d, 260.8d, 118.4d, 261.0d, 118.5d);
      path32.closePath();
      g2.setPaint(new Color(209, 170, 143));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path32);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path32);

      // Element 33: Path
      GeneralPath path33 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path33.moveTo(180.0d, 120.0d);
      path33.curveTo(182.0d, 120.3d, 183.3d, 120.4d, 185.5d, 120.5d);
      path33.curveTo(185.8d, 120.5d, 186.2d, 120.5d, 186.5d, 120.5d);
      path33.curveTo(189.1d, 120.5d, 190.6d, 120.3d, 193.0d, 120.0d);
      path33.curveTo(193.2d, 120.0d, 193.5d, 120.0d, 193.7d, 120.0d);
      path33.lineTo(194.0d, 120.5d);
      path33.lineTo(193.5d, 121.0d);
      path33.curveTo(197.8d, 121.4d, 200.3d, 123.5d, 200.0d, 128.0d);
      path33.lineTo(202.9d, 125.0d);
      path33.curveTo(204.5d, 124.9d, 206.5d, 124.7d, 207.7d, 126.0d);
      path33.curveTo(208.0d, 126.3d, 207.9d, 126.8d, 208.0d, 127.0d);
      path33.lineTo(208.0d, 127.5d);
      path33.curveTo(206.8d, 126.1d, 205.0d, 125.6d, 203.2d, 126.0d);
      path33.curveTo(200.8d, 126.5d, 201.8d, 132.6d, 200.5d, 134.5d);
      path33.lineTo(204.5d, 134.5d);
      path33.curveTo(202.2d, 135.2d, 199.7d, 135.7d, 197.5d, 136.5d);
      path33.lineTo(197.5d, 136.0d);
      path33.curveTo(198.5d, 132.6d, 200.7d, 125.3d, 197.2d, 123.1d);
      path33.curveTo(192.7d, 120.2d, 178.8d, 121.4d, 173.5d, 122.5d);
      path33.curveTo(173.4d, 122.3d, 173.2d, 122.1d, 173.0d, 122.0d);
      path33.curveTo(172.0d, 121.1d, 171.0d, 121.2d, 170.5d, 122.5d);
      path33.curveTo(170.3d, 122.5d, 170.2d, 122.5d, 170.0d, 122.5d);
      path33.curveTo(169.8d, 122.0d, 169.4d, 122.0d, 169.0d, 122.0d);
      path33.curveTo(168.8d, 122.0d, 168.7d, 122.0d, 168.5d, 122.0d);
      path33.curveTo(171.0d, 120.9d, 175.1d, 119.7d, 177.8d, 119.5d);
      path33.curveTo(179.0d, 119.5d, 179.4d, 119.9d, 180.0d, 120.0d);
      path33.closePath();
      path33.moveTo(178.5d, 120.5d);
      path33.lineTo(176.5d, 120.5d);
      path33.lineTo(176.5d, 121.0d);
      path33.lineTo(178.5d, 121.0d);
      path33.lineTo(178.5d, 120.5d);
      path33.closePath();
      path33.moveTo(200.5d, 131.0d);
      path33.lineTo(200.5d, 129.5d);
      path33.lineTo(200.0d, 129.5d);
      path33.lineTo(200.0d, 131.0d);
      path33.lineTo(200.5d, 131.0d);
      path33.closePath();
      g2.setPaint(new Color(39, 36, 32));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path33);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path33);

      // Element 34: Path
      GeneralPath path34 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path34.moveTo(169.5d, 117.0d);
      path34.curveTo(169.3d, 111.9d, 178.2d, 112.2d, 181.8d, 112.5d);
      path34.curveTo(184.3d, 112.7d, 189.4d, 113.1d, 190.0d, 116.0d);
      path34.curveTo(184.1d, 116.2d, 178.1d, 115.8d, 172.2d, 116.0d);
      path34.curveTo(171.3d, 116.0d, 170.2d, 115.5d, 170.5d, 117.0d);
      path34.curveTo(170.2d, 117.0d, 169.8d, 117.0d, 169.5d, 117.0d);
      path34.closePath();
      g2.setPaint(new Color(53, 53, 52));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path34);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path34);

      // Element 35: Path
      GeneralPath path35 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path35.moveTo(159.5d, 128.5d);
      path35.lineTo(159.1d, 125.8d);
      path35.lineTo(166.8d, 123.0d);
      path35.lineTo(166.0d, 124.5d);
      path35.curveTo(162.4d, 127.0d, 166.4d, 137.2d, 168.6d, 139.4d);
      path35.curveTo(168.9d, 139.7d, 169.4d, 139.5d, 169.5d, 139.5d);
      path35.curveTo(169.7d, 139.7d, 169.8d, 139.9d, 170.0d, 140.0d);
      path35.curveTo(170.1d, 140.5d, 169.9d, 141.0d, 170.0d, 141.5d);
      path35.curveTo(165.3d, 138.0d, 166.1d, 128.9d, 159.5d, 128.5d);
      path35.closePath();
      g2.setPaint(new Color(52, 52, 52));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path35);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path35);

      // Element 36: Path
      GeneralPath path36 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path36.moveTo(148.0d, 63.5d);
      path36.curveTo(147.3d, 65.4d, 145.5d, 66.8d, 143.5d, 67.3d);
      path36.curveTo(144.6d, 65.4d, 145.4d, 63.4d, 148.0d, 63.5d);
      path36.closePath();
      g2.setPaint(new Color(144, 144, 144));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path36);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path36);

      // Element 37: Path
      GeneralPath path37 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path37.moveTo(173.0d, 122.0d);
      path37.curveTo(172.0d, 121.8d, 171.3d, 122.3d, 170.5d, 122.5d);
      path37.curveTo(171.0d, 121.2d, 172.0d, 121.2d, 173.0d, 122.0d);
      path37.closePath();
      g2.setPaint(new Color(52, 52, 52));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path37);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path37);

      // Element 38: Path
      GeneralPath path38 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path38.moveTo(170.0d, 122.5d);
      path38.curveTo(169.2d, 122.7d, 168.4d, 123.1d, 167.5d, 123.0d);
      path38.lineTo(169.0d, 122.0d);
      path38.curveTo(169.4d, 122.0d, 169.8d, 122.0d, 170.0d, 122.5d);
      path38.closePath();
      g2.setPaint(new Color(52, 52, 52));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path38);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path38);

      // Element 39: Path
      GeneralPath path39 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path39.moveTo(166.5d, 124.5d);
      path39.curveTo(163.7d, 129.0d, 167.4d, 135.3d, 169.5d, 139.5d);
      path39.curveTo(169.4d, 139.4d, 168.9d, 139.7d, 168.6d, 139.4d);
      path39.curveTo(166.4d, 137.2d, 162.4d, 127.0d, 166.0d, 124.5d);
      path39.lineTo(166.5d, 124.5d);
      path39.closePath();
      g2.setPaint(new Color(39, 36, 32));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path39);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path39);

      // Element 40: Path
      GeneralPath path40 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path40.moveTo(179.4d, 111.4d);
      path40.lineTo(171.5d, 112.2d);
      path40.curveTo(174.0d, 108.1d, 175.7d, 103.4d, 176.0d, 98.5d);
      path40.lineTo(179.0d, 103.8d);
      path40.curveTo(179.5d, 106.3d, 180.4d, 108.9d, 179.4d, 111.4d);
      path40.closePath();
      g2.setPaint(new Color(209, 170, 143));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path40);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path40);

      // Element 41: Path
      GeneralPath path41 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path41.moveTo(143.8d, 64.0d);
      path41.curveTo(144.1d, 64.2d, 144.1d, 64.3d, 143.8d, 64.5d);
      path41.lineTo(143.8d, 64.0d);
      path41.closePath();
      g2.setPaint(new Color(53, 53, 52));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path41);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path41);

      // Element 42: Path
      GeneralPath path42 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path42.moveTo(214.0d, 133.5d);
      path42.curveTo(215.7d, 133.4d, 217.8d, 133.4d, 219.5d, 133.5d);
      path42.curveTo(219.2d, 134.7d, 220.2d, 134.5d, 220.8d, 134.9d);
      path42.curveTo(226.9d, 138.7d, 231.9d, 142.2d, 235.7d, 148.5d);
      path42.curveTo(249.4d, 171.3d, 231.4d, 197.2d, 207.5d, 201.0d);
      path42.curveTo(208.1d, 204.3d, 212.7d, 202.8d, 214.5d, 204.0d);
      path42.curveTo(207.0d, 214.4d, 209.3d, 227.1d, 213.0d, 238.5d);
      path42.curveTo(213.7d, 240.7d, 217.1d, 241.5d, 219.3d, 241.5d);
      path42.curveTo(220.4d, 241.6d, 221.3d, 241.0d, 221.5d, 241.0d);
      path42.curveTo(223.0d, 241.0d, 224.5d, 240.9d, 226.0d, 241.2d);
      path42.curveTo(227.7d, 241.7d, 230.8d, 244.5d, 231.7d, 244.5d);
      path42.curveTo(232.9d, 244.5d, 239.3d, 240.7d, 241.2d, 239.9d);
      path42.curveTo(245.2d, 238.3d, 247.2d, 237.7d, 251.5d, 238.0d);
      path42.curveTo(248.2d, 241.0d, 247.1d, 246.7d, 246.5d, 251.0d);
      path42.curveTo(246.5d, 251.2d, 246.5d, 251.3d, 246.5d, 251.5d);
      path42.curveTo(246.2d, 254.2d, 245.9d, 256.2d, 246.0d, 259.0d);
      path42.curveTo(244.6d, 258.9d, 244.9d, 260.6d, 244.0d, 261.8d);
      path42.curveTo(240.6d, 266.1d, 233.5d, 269.6d, 228.1d, 270.4d);
      path42.curveTo(225.8d, 270.7d, 223.3d, 270.1d, 221.0d, 271.0d);
      path42.curveTo(220.2d, 269.8d, 218.5d, 267.2d, 217.5d, 266.2d);
      path42.curveTo(213.3d, 261.8d, 208.5d, 264.2d, 209.5d, 270.5d);
      path42.curveTo(204.2d, 269.4d, 198.8d, 269.9d, 193.5d, 269.5d);
      path42.curveTo(190.7d, 269.3d, 191.0d, 267.3d, 188.0d, 270.0d);
      path42.curveTo(187.5d, 270.4d, 186.7d, 270.7d, 186.5d, 271.5d);
      path42.curveTo(183.5d, 271.6d, 179.4d, 271.5d, 176.5d, 272.5d);
      path42.curveTo(178.3d, 268.7d, 174.7d, 264.6d, 170.8d, 265.0d);
      path42.curveTo(166.9d, 265.4d, 157.9d, 272.3d, 154.5d, 275.0d);
      path42.curveTo(154.0d, 274.0d, 153.0d, 274.8d, 152.3d, 275.1d);
      path42.curveTo(145.4d, 277.6d, 140.6d, 282.1d, 134.3d, 285.6d);
      path42.curveTo(129.6d, 288.3d, 124.5d, 290.2d, 119.5d, 292.5d);
      path42.curveTo(119.4d, 292.4d, 119.1d, 292.6d, 119.0d, 292.5d);
      path42.curveTo(118.9d, 291.9d, 119.1d, 291.1d, 119.0d, 290.5d);
      path42.curveTo(120.0d, 287.4d, 121.8d, 284.7d, 122.5d, 281.5d);
      path42.curveTo(123.0d, 280.8d, 124.2d, 278.9d, 124.5d, 278.5d);
      path42.curveTo(124.8d, 278.1d, 125.6d, 277.5d, 126.0d, 277.0d);
      path42.curveTo(127.0d, 276.0d, 127.0d, 276.7d, 128.0d, 275.0d);
      path42.lineTo(128.7d, 275.0d);
      path42.curveTo(131.5d, 272.3d, 135.3d, 269.5d, 137.8d, 266.3d);
      path42.curveTo(138.1d, 265.9d, 138.7d, 264.4d, 139.0d, 264.0d);
      path42.curveTo(141.0d, 261.3d, 143.2d, 258.2d, 145.0d, 255.5d);
      path42.curveTo(148.3d, 250.5d, 148.8d, 245.8d, 150.0d, 240.0d);
      path42.curveTo(150.7d, 236.5d, 151.5d, 232.7d, 151.5d, 229.5d);
      path42.curveTo(151.5d, 229.3d, 151.5d, 229.2d, 151.5d, 229.0d);
      path42.curveTo(152.9d, 228.7d, 152.3d, 227.2d, 152.5d, 226.5d);
      path42.curveTo(154.2d, 221.3d, 156.6d, 216.3d, 158.0d, 211.0d);
      path42.curveTo(158.6d, 208.7d, 160.1d, 205.0d, 158.5d, 203.0d);
      path42.curveTo(158.7d, 202.3d, 158.9d, 201.7d, 159.0d, 201.0d);
      path42.curveTo(159.2d, 199.8d, 159.8d, 198.7d, 160.0d, 197.0d);
      path42.curveTo(160.8d, 191.5d, 160.7d, 186.0d, 162.0d, 180.5d);
      path42.curveTo(163.1d, 175.9d, 165.4d, 170.0d, 167.4d, 165.7d);
      path42.curveTo(168.6d, 163.2d, 173.4d, 155.6d, 173.5d, 154.0d);
      path42.curveTo(173.6d, 153.9d, 174.1d, 154.1d, 174.3d, 153.8d);
      path42.curveTo(174.7d, 153.5d, 174.7d, 152.5d, 175.0d, 152.0d);
      path42.curveTo(176.9d, 151.7d, 176.2d, 150.6d, 177.2d, 149.5d);
      path42.curveTo(182.0d, 144.2d, 190.7d, 139.1d, 197.5d, 136.5d);
      path42.curveTo(199.7d, 135.7d, 202.2d, 135.2d, 204.5d, 134.5d);
      path42.curveTo(204.7d, 134.4d, 204.8d, 134.1d, 205.0d, 134.0d);
      path42.curveTo(206.8d, 135.3d, 209.3d, 133.6d, 211.0d, 133.5d);
      path42.curveTo(212.0d, 133.4d, 213.0d, 133.5d, 214.0d, 133.5d);
      path42.closePath();
      path42.moveTo(238.9d, 167.9d);
      path42.curveTo(239.5d, 167.0d, 238.9d, 164.5d, 237.3d, 165.0d);
      path42.curveTo(235.0d, 165.7d, 237.5d, 170.3d, 238.9d, 167.9d);
      path42.closePath();
      path42.moveTo(200.4d, 166.1d);
      path42.curveTo(195.3d, 167.6d, 201.3d, 176.1d, 204.4d, 170.7d);
      path42.curveTo(206.2d, 167.8d, 203.4d, 165.2d, 200.4d, 166.1d);
      path42.closePath();
      path42.moveTo(228.9d, 169.9d);
      path42.curveTo(230.1d, 168.1d, 227.5d, 165.7d, 226.2d, 167.5d);
      path42.curveTo(224.8d, 169.4d, 228.0d, 171.4d, 228.9d, 169.9d);
      path42.closePath();
      path42.moveTo(206.0d, 183.5d);
      path42.curveTo(206.1d, 183.4d, 206.9d, 183.5d, 207.4d, 182.7d);
      path42.curveTo(209.5d, 179.9d, 207.7d, 175.1d, 204.2d, 174.5d);
      path42.curveTo(195.3d, 173.1d, 194.2d, 185.8d, 202.8d, 185.5d);
      path42.curveTo(205.8d, 185.5d, 205.0d, 184.5d, 206.0d, 183.5d);
      path42.closePath();
      path42.moveTo(188.0d, 243.0d);
      path42.curveTo(193.9d, 250.2d, 201.3d, 247.6d, 206.8d, 241.5d);
      path42.curveTo(210.1d, 237.8d, 211.0d, 235.8d, 208.6d, 231.1d);
      path42.curveTo(207.4d, 228.6d, 203.4d, 224.7d, 201.7d, 222.3d);
      path42.curveTo(199.1d, 218.7d, 198.1d, 215.0d, 194.1d, 212.0d);
      path42.curveTo(191.2d, 209.8d, 186.0d, 209.7d, 182.6d, 210.9d);
      path42.curveTo(180.6d, 211.7d, 181.4d, 212.1d, 181.0d, 212.5d);
      path42.curveTo(178.6d, 213.4d, 178.8d, 212.9d, 177.5d, 215.5d);
      path42.curveTo(168.2d, 223.3d, 176.8d, 237.5d, 186.0d, 241.5d);
      path42.curveTo(186.3d, 241.9d, 186.5d, 242.4d, 187.0d, 242.7d);
      path42.curveTo(187.4d, 242.9d, 187.8d, 242.9d, 188.0d, 243.0d);
      path42.closePath();
      g2.setPaint(new Color(171, 192, 149));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path42);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path42);

      // Element 43: Path
      GeneralPath path43 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path43.moveTo(190.0d, 211.5d);
      path43.curveTo(190.0d, 215.4d, 191.8d, 219.2d, 193.8d, 222.5d);
      path43.curveTo(196.6d, 227.2d, 204.0d, 233.4d, 200.4d, 239.2d);
      path43.curveTo(198.9d, 241.7d, 195.4d, 243.4d, 194.0d, 246.0d);
      path43.curveTo(192.2d, 245.4d, 190.0d, 243.9d, 188.0d, 243.0d);
      path43.curveTo(187.9d, 242.9d, 188.1d, 242.2d, 187.5d, 241.8d);
      path43.curveTo(187.0d, 241.4d, 186.3d, 241.6d, 186.0d, 241.5d);
      path43.curveTo(183.4d, 238.2d, 179.7d, 236.2d, 177.2d, 233.0d);
      path43.curveTo(173.3d, 227.8d, 174.9d, 220.8d, 177.5d, 215.5d);
      path43.curveTo(178.9d, 214.4d, 179.5d, 214.3d, 181.0d, 212.5d);
      path43.curveTo(184.4d, 211.3d, 186.4d, 210.8d, 190.0d, 211.5d);
      path43.closePath();
      g2.setPaint(new Color(171, 192, 149));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path43);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path43);

      // Element 44: Path
      GeneralPath path44 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path44.moveTo(173.5d, 154.0d);
      path44.curveTo(169.8d, 157.7d, 168.2d, 162.5d, 165.7d, 167.0d);
      path44.curveTo(165.4d, 167.6d, 165.8d, 168.2d, 164.8d, 168.0d);
      path44.curveTo(160.7d, 162.7d, 166.8d, 152.3d, 173.3d, 153.0d);
      path44.curveTo(173.7d, 153.0d, 173.5d, 153.6d, 173.5d, 154.0d);
      path44.closePath();
      g2.setPaint(new Color(231, 214, 128));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path44);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path44);

      // Element 45: Path
      GeneralPath path45 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path45.moveTo(170.5d, 143.0d);
      path45.curveTo(173.1d, 144.0d, 175.0d, 144.8d, 177.9d, 144.6d);
      path45.curveTo(178.1d, 144.7d, 178.5d, 146.2d, 178.5d, 146.5d);
      path45.curveTo(178.5d, 148.9d, 176.5d, 149.2d, 175.5d, 150.8d);
      path45.curveTo(175.1d, 151.2d, 175.1d, 151.8d, 175.0d, 152.0d);
      path45.curveTo(174.7d, 152.1d, 173.8d, 152.1d, 173.6d, 151.9d);
      path45.lineTo(170.0d, 143.0d);
      path45.curveTo(170.2d, 143.0d, 170.4d, 142.9d, 170.5d, 143.0d);
      path45.closePath();
      g2.setPaint(new Color(209, 170, 143));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path45);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path45);

      // Element 46: Path
      GeneralPath path46 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path46.moveTo(162.0d, 180.5d);
      path46.curveTo(160.7d, 186.0d, 160.8d, 191.5d, 160.0d, 197.0d);
      path46.lineTo(159.0d, 197.0d);
      path46.curveTo(159.9d, 191.6d, 159.4d, 185.7d, 161.5d, 180.5d);
      path46.lineTo(162.0d, 180.5d);
      path46.closePath();
      g2.setPaint(new Color(73, 75, 59));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path46);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path46);

      // Element 47: Path
      GeneralPath path47 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path47.moveTo(247.5d, 120.5d);
      path47.curveTo(247.8d, 119.1d, 248.9d, 117.3d, 250.5d, 117.2d);
      path47.curveTo(249.7d, 119.1d, 251.8d, 120.5d, 252.0d, 122.7d);
      path47.curveTo(252.4d, 125.6d, 251.4d, 128.2d, 251.0d, 131.0d);
      path47.curveTo(252.3d, 131.2d, 251.9d, 130.7d, 252.2d, 130.0d);
      path47.curveTo(253.2d, 128.1d, 253.5d, 125.6d, 255.0d, 124.0d);
      path47.curveTo(254.3d, 128.3d, 251.6d, 130.7d, 251.0d, 134.0d);
      path47.lineTo(250.0d, 134.0d);
      path47.curveTo(250.4d, 129.1d, 252.5d, 122.9d, 249.2d, 119.0d);
      path47.curveTo(248.7d, 118.9d, 247.7d, 120.0d, 247.5d, 120.5d);
      path47.closePath();
      g2.setPaint(new Color(33, 32, 29));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path47);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path47);

      // Element 48: Path
      GeneralPath path48 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path48.moveTo(170.0d, 140.0d);
      path48.curveTo(172.6d, 141.8d, 174.7d, 142.5d, 178.0d, 142.5d);
      path48.curveTo(178.1d, 142.0d, 177.9d, 141.4d, 178.0d, 141.0d);
      path48.lineTo(178.5d, 141.0d);
      path48.curveTo(178.1d, 142.5d, 179.1d, 144.4d, 179.0d, 146.0d);
      path48.curveTo(178.9d, 146.0d, 178.7d, 146.4d, 178.5d, 146.5d);
      path48.curveTo(178.5d, 146.2d, 178.1d, 144.7d, 177.9d, 144.6d);
      path48.curveTo(175.0d, 144.8d, 173.1d, 144.0d, 170.5d, 143.0d);
      path48.curveTo(170.2d, 142.5d, 170.1d, 142.0d, 170.0d, 141.5d);
      path48.curveTo(169.9d, 141.0d, 170.1d, 140.5d, 170.0d, 140.0d);
      path48.closePath();
      path48.moveTo(171.8d, 142.0d);
      path48.lineTo(171.8d, 142.5d);
      path48.curveTo(172.1d, 142.3d, 172.1d, 142.2d, 171.8d, 142.0d);
      path48.closePath();
      g2.setPaint(new Color(39, 36, 32));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path48);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path48);

      // Element 49: Path
      GeneralPath path49 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path49.moveTo(216.0d, 274.0d);
      path49.curveTo(215.8d, 274.1d, 213.0d, 274.5d, 212.7d, 274.5d);
      path49.curveTo(210.9d, 274.4d, 209.8d, 267.8d, 211.0d, 266.2d);
      path49.curveTo(211.6d, 265.2d, 215.4d, 265.5d, 215.5d, 267.2d);
      path49.lineTo(217.0d, 267.0d);
      path49.lineTo(216.0d, 267.8d);
      path49.curveTo(216.0d, 268.9d, 217.5d, 272.3d, 217.0d, 273.0d);
      path49.curveTo(217.0d, 273.1d, 216.4d, 272.9d, 216.2d, 273.2d);
      path49.curveTo(215.9d, 273.4d, 216.0d, 274.0d, 216.0d, 274.0d);
      path49.closePath();
      g2.setPaint(new Color(216, 177, 150));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path49);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path49);

      // Element 50: Path
      GeneralPath path50 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path50.moveTo(217.0d, 273.0d);
      path50.curveTo(217.1d, 272.8d, 217.8d, 272.8d, 217.9d, 272.2d);
      path50.curveTo(218.0d, 271.7d, 216.1d, 267.2d, 217.8d, 268.0d);
      path50.curveTo(218.2d, 268.2d, 220.4d, 271.6d, 220.9d, 272.4d);
      path50.curveTo(222.1d, 274.1d, 221.6d, 275.1d, 223.5d, 276.0d);
      path50.curveTo(223.6d, 276.1d, 224.0d, 276.5d, 224.0d, 276.5d);
      path50.curveTo(223.6d, 280.8d, 227.5d, 285.8d, 229.2d, 290.0d);
      path50.curveTo(233.6d, 300.6d, 234.8d, 310.9d, 243.0d, 319.5d);
      path50.curveTo(243.1d, 319.6d, 243.1d, 320.4d, 243.7d, 321.0d);
      path50.curveTo(244.4d, 321.6d, 246.0d, 322.8d, 247.0d, 323.0d);
      path50.lineTo(243.5d, 318.5d);
      path50.curveTo(243.6d, 318.4d, 243.8d, 318.6d, 244.0d, 318.5d);
      path50.curveTo(247.1d, 317.4d, 249.4d, 317.3d, 250.4d, 313.6d);
      path50.curveTo(253.3d, 302.2d, 247.6d, 292.8d, 250.7d, 282.0d);
      path50.curveTo(251.0d, 281.3d, 252.3d, 277.2d, 252.5d, 277.0d);
      path50.curveTo(252.7d, 276.8d, 254.3d, 276.9d, 254.0d, 275.5d);
      path50.curveTo(254.1d, 275.5d, 254.3d, 275.5d, 254.5d, 275.5d);
      path50.curveTo(254.9d, 275.5d, 255.6d, 275.4d, 256.0d, 275.5d);
      path50.curveTo(255.0d, 278.2d, 254.8d, 282.3d, 256.5d, 284.8d);
      path50.curveTo(256.9d, 285.4d, 257.7d, 285.6d, 258.0d, 286.0d);
      path50.curveTo(263.3d, 292.6d, 262.3d, 299.5d, 263.6d, 307.1d);
      path50.curveTo(264.4d, 311.2d, 267.1d, 315.8d, 267.4d, 319.3d);
      path50.curveTo(267.7d, 321.6d, 267.0d, 324.4d, 267.0d, 327.0d);
      path50.curveTo(265.7d, 326.7d, 266.0d, 327.7d, 265.8d, 328.6d);
      path50.curveTo(265.5d, 330.4d, 264.5d, 335.2d, 264.5d, 336.7d);
      path50.lineTo(264.5d, 345.7d);
      path50.curveTo(264.5d, 346.8d, 268.2d, 354.4d, 269.1d, 355.7d);
      path50.curveTo(270.6d, 357.8d, 275.0d, 360.9d, 275.7d, 363.0d);
      path50.curveTo(277.9d, 369.3d, 268.2d, 375.8d, 263.6d, 378.4d);
      path50.curveTo(262.3d, 379.1d, 260.1d, 379.1d, 260.0d, 381.0d);
      path50.curveTo(258.3d, 380.8d, 256.5d, 381.2d, 254.8d, 380.9d);
      path50.curveTo(242.3d, 379.1d, 234.2d, 365.8d, 225.5d, 357.7d);
      path50.curveTo(219.8d, 352.6d, 211.2d, 347.3d, 206.2d, 342.5d);
      path50.curveTo(204.8d, 341.1d, 203.8d, 338.8d, 202.5d, 337.3d);
      path50.curveTo(198.0d, 332.1d, 190.2d, 324.9d, 186.9d, 319.4d);
      path50.curveTo(185.2d, 316.6d, 185.3d, 314.2d, 185.5d, 311.0d);
      path50.curveTo(185.7d, 307.8d, 186.8d, 298.7d, 188.0d, 295.8d);
      path50.curveTo(188.3d, 295.0d, 188.4d, 294.3d, 189.5d, 294.5d);
      path50.curveTo(190.5d, 299.4d, 191.1d, 306.0d, 193.3d, 310.5d);
      path50.curveTo(194.8d, 313.5d, 201.8d, 321.6d, 204.4d, 324.4d);
      path50.curveTo(204.9d, 324.9d, 204.9d, 325.8d, 206.0d, 325.5d);
      path50.lineTo(204.0d, 322.2d);
      path50.curveTo(201.4d, 319.2d, 197.9d, 315.7d, 195.6d, 312.2d);
      path50.curveTo(192.9d, 308.2d, 188.3d, 289.4d, 188.0d, 284.3d);
      path50.curveTo(187.9d, 282.9d, 188.0d, 281.4d, 188.0d, 280.0d);
      path50.curveTo(193.4d, 280.0d, 194.4d, 275.4d, 191.5d, 271.5d);
      path50.curveTo(191.3d, 271.3d, 191.1d, 271.2d, 191.0d, 271.0d);
      path50.curveTo(190.4d, 270.4d, 189.6d, 270.1d, 189.5d, 269.5d);
      path50.curveTo(198.7d, 270.6d, 196.3d, 283.6d, 201.0d, 289.5d);
      path50.curveTo(203.5d, 297.7d, 204.9d, 300.3d, 209.7d, 307.1d);
      path50.curveTo(211.7d, 309.8d, 215.1d, 316.2d, 218.4d, 316.9d);
      path50.curveTo(220.3d, 317.3d, 221.1d, 316.5d, 222.2d, 316.5d);
      path50.curveTo(222.5d, 316.5d, 222.5d, 318.3d, 223.5d, 317.5d);
      path50.curveTo(224.3d, 316.9d, 218.2d, 300.5d, 217.7d, 298.0d);
      path50.curveTo(216.9d, 294.0d, 216.9d, 290.1d, 215.7d, 286.0d);
      path50.curveTo(214.8d, 282.8d, 213.0d, 279.7d, 212.0d, 276.5d);
      path50.curveTo(212.1d, 276.4d, 211.8d, 275.9d, 212.1d, 275.6d);
      path50.curveTo(212.4d, 275.4d, 214.4d, 275.1d, 216.0d, 274.0d);
      path50.curveTo(216.1d, 273.9d, 216.6d, 274.1d, 216.9d, 273.9d);
      path50.curveTo(217.1d, 273.6d, 216.9d, 273.1d, 217.0d, 273.0d);
      path50.closePath();
      path50.moveTo(227.0d, 298.0d);
      path50.curveTo(226.9d, 297.9d, 224.2d, 298.8d, 223.7d, 298.9d);
      path50.curveTo(222.9d, 299.2d, 222.2d, 298.5d, 222.5d, 300.0d);
      path50.curveTo(223.0d, 300.4d, 228.3d, 299.0d, 227.0d, 298.0d);
      path50.closePath();
      path50.moveTo(207.5d, 327.5d);
      path50.curveTo(208.0d, 327.0d, 206.5d, 325.5d, 206.0d, 326.0d);
      path50.curveTo(205.5d, 326.5d, 207.0d, 328.0d, 207.5d, 327.5d);
      path50.closePath();
      path50.moveTo(234.5d, 327.5d);
      path50.curveTo(233.6d, 327.5d, 233.4d, 327.2d, 233.5d, 328.2d);
      path50.curveTo(233.7d, 329.4d, 235.1d, 332.8d, 235.8d, 334.0d);
      path50.curveTo(236.1d, 334.7d, 235.7d, 335.3d, 237.0d, 335.0d);
      path50.lineTo(234.5d, 327.5d);
      path50.closePath();
      path50.moveTo(220.0d, 334.5d);
      path50.curveTo(217.4d, 334.0d, 221.3d, 339.2d, 222.0d, 338.5d);
      path50.lineTo(220.0d, 334.5d);
      path50.closePath();
      path50.moveTo(224.0d, 341.0d);
      path50.lineTo(222.8d, 339.0d);
      path50.lineTo(222.0d, 339.0d);
      path50.curveTo(222.0d, 339.0d, 223.3d, 341.0d, 223.3d, 341.0d);
      path50.lineTo(224.0d, 341.0d);
      path50.closePath();
      g2.setPaint(new Color(216, 177, 150));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path50);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path50);

      // Element 51: Path
      GeneralPath path51 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path51.moveTo(191.0d, 271.0d);
      path51.lineTo(191.5d, 271.5d);
      path51.curveTo(191.9d, 276.1d, 193.3d, 278.6d, 187.5d, 279.5d);
      path51.curveTo(187.8d, 278.7d, 187.0d, 277.9d, 187.0d, 277.5d);
      path51.curveTo(186.6d, 274.6d, 187.2d, 273.5d, 188.0d, 271.0d);
      path51.lineTo(191.0d, 271.0d);
      path51.closePath();
      g2.setPaint(new Color(216, 177, 150));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path51);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path51);

      // Element 52: Path
      GeneralPath path52 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path52.moveTo(254.5d, 275.5d);
      path52.curveTo(254.1d, 273.5d, 257.6d, 274.7d, 258.0d, 275.4d);
      path52.curveTo(259.5d, 278.1d, 259.4d, 284.1d, 260.2d, 287.5d);
      path52.curveTo(261.0d, 290.2d, 262.7d, 292.3d, 263.4d, 294.9d);
      path52.curveTo(264.5d, 299.3d, 264.0d, 304.3d, 265.2d, 309.1d);
      path52.curveTo(265.9d, 312.3d, 268.2d, 316.3d, 268.5d, 319.3d);
      path52.curveTo(268.9d, 324.4d, 268.5d, 329.6d, 269.0d, 335.2d);
      path52.curveTo(269.5d, 340.6d, 269.5d, 345.8d, 271.0d, 351.0d);
      path52.curveTo(274.6d, 349.7d, 278.3d, 351.1d, 281.7d, 351.0d);
      path52.curveTo(295.1d, 350.9d, 299.7d, 346.0d, 310.5d, 341.5d);
      path52.curveTo(310.6d, 341.4d, 310.8d, 341.5d, 311.0d, 341.5d);
      path52.curveTo(318.8d, 338.9d, 334.3d, 343.4d, 340.4d, 348.9d);
      path52.curveTo(341.1d, 349.5d, 341.7d, 349.7d, 341.5d, 351.0d);
      path52.curveTo(335.1d, 345.2d, 322.9d, 341.1d, 314.3d, 342.0d);
      path52.curveTo(310.0d, 342.4d, 304.1d, 346.4d, 299.8d, 348.1d);
      path52.curveTo(290.4d, 351.9d, 281.7d, 352.4d, 271.5d, 352.0d);
      path52.curveTo(271.2d, 352.2d, 274.7d, 357.4d, 272.8d, 356.5d);
      path52.curveTo(271.4d, 355.8d, 269.5d, 348.8d, 269.1d, 347.1d);
      path52.curveTo(268.2d, 342.4d, 267.3d, 334.8d, 267.0d, 330.0d);
      path52.curveTo(267.0d, 329.5d, 267.0d, 329.0d, 267.0d, 328.5d);
      path52.curveTo(267.0d, 328.0d, 267.0d, 327.5d, 267.0d, 327.0d);
      path52.curveTo(267.0d, 324.4d, 267.7d, 321.6d, 267.4d, 319.3d);
      path52.curveTo(267.1d, 315.8d, 264.3d, 311.2d, 263.6d, 307.1d);
      path52.curveTo(262.3d, 299.5d, 263.3d, 292.6d, 258.0d, 286.0d);
      path52.lineTo(258.0d, 285.5d);
      path52.curveTo(259.1d, 284.4d, 257.9d, 276.6d, 257.4d, 276.1d);
      path52.curveTo(257.1d, 275.9d, 256.6d, 276.1d, 256.5d, 276.0d);
      path52.curveTo(256.3d, 275.9d, 256.1d, 275.5d, 256.0d, 275.5d);
      path52.curveTo(255.6d, 275.4d, 254.9d, 275.5d, 254.5d, 275.5d);
      path52.closePath();
      g2.setPaint(new Color(35, 33, 30));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path52);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path52);

      // Element 53: Path
      GeneralPath path53 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path53.moveTo(267.0d, 330.0d);
      path53.curveTo(266.4d, 329.7d, 266.2d, 328.6d, 267.0d, 328.5d);
      path53.curveTo(267.0d, 329.0d, 267.0d, 329.5d, 267.0d, 330.0d);
      path53.closePath();
      g2.setPaint(new Color(216, 177, 150));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path53);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path53);

      // Element 54: Path
      GeneralPath path54 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path54.moveTo(191.5d, 271.5d);
      path54.curveTo(194.4d, 275.4d, 193.4d, 280.0d, 188.0d, 280.0d);
      path54.curveTo(188.0d, 281.4d, 187.9d, 282.9d, 188.0d, 284.3d);
      path54.curveTo(188.3d, 289.4d, 192.9d, 308.2d, 195.6d, 312.2d);
      path54.curveTo(197.9d, 315.7d, 201.4d, 319.2d, 204.0d, 322.2d);
      path54.lineTo(206.0d, 325.5d);
      path54.curveTo(204.9d, 325.8d, 204.9d, 324.9d, 204.4d, 324.3d);
      path54.curveTo(201.8d, 321.6d, 194.8d, 313.5d, 193.3d, 310.4d);
      path54.curveTo(191.1d, 305.9d, 190.6d, 299.4d, 189.5d, 294.5d);
      path54.curveTo(188.4d, 294.3d, 188.3d, 295.0d, 188.0d, 295.8d);
      path54.curveTo(186.8d, 298.7d, 185.7d, 307.7d, 185.5d, 311.0d);
      path54.lineTo(185.0d, 311.0d);
      path54.lineTo(186.5d, 297.2d);
      path54.curveTo(185.5d, 296.2d, 185.4d, 297.8d, 184.8d, 298.5d);
      path54.curveTo(173.3d, 310.5d, 169.2d, 312.4d, 153.5d, 317.5d);
      path54.curveTo(153.2d, 316.1d, 154.2d, 316.6d, 155.0d, 316.3d);
      path54.curveTo(159.5d, 314.5d, 164.6d, 313.1d, 168.8d, 311.1d);
      path54.curveTo(174.4d, 308.5d, 179.3d, 302.4d, 183.7d, 298.0d);
      path54.curveTo(185.2d, 296.5d, 188.4d, 294.9d, 188.5d, 292.3d);
      path54.curveTo(188.6d, 289.5d, 186.2d, 284.7d, 187.0d, 281.5d);
      path54.curveTo(184.5d, 281.9d, 183.0d, 284.6d, 181.5d, 285.8d);
      path54.curveTo(180.8d, 286.4d, 180.0d, 286.7d, 179.0d, 287.5d);
      path54.lineTo(179.0d, 287.0d);
      path54.curveTo(179.9d, 286.4d, 183.8d, 282.5d, 184.7d, 281.5d);
      path54.curveTo(185.1d, 281.1d, 185.9d, 281.0d, 186.2d, 280.5d);
      path54.curveTo(186.7d, 279.7d, 186.5d, 278.4d, 186.5d, 277.5d);
      path54.lineTo(187.0d, 277.5d);
      path54.curveTo(187.0d, 277.9d, 187.8d, 278.7d, 187.5d, 279.5d);
      path54.curveTo(193.2d, 278.6d, 191.9d, 276.1d, 191.5d, 271.5d);
      path54.closePath();
      g2.setPaint(new Color(66, 62, 53));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path54);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path54);

      // Element 55: Path
      GeneralPath path55 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path55.moveTo(193.5d, 269.5d);
      path55.curveTo(195.8d, 272.2d, 197.4d, 275.4d, 198.1d, 278.9d);
      path55.curveTo(200.2d, 279.3d, 202.4d, 278.8d, 204.5d, 278.5d);
      path55.lineTo(205.0d, 279.0d);
      path55.curveTo(203.0d, 279.3d, 200.8d, 280.6d, 198.5d, 280.0d);
      path55.curveTo(199.3d, 283.0d, 200.1d, 286.0d, 201.0d, 289.0d);
      path55.curveTo(201.1d, 289.1d, 200.9d, 289.4d, 201.0d, 289.5d);
      path55.curveTo(196.3d, 283.6d, 198.7d, 270.6d, 189.5d, 269.5d);
      path55.curveTo(189.6d, 270.1d, 190.4d, 270.4d, 191.0d, 271.0d);
      path55.lineTo(188.0d, 271.0d);
      path55.curveTo(188.1d, 270.8d, 187.9d, 270.3d, 188.0d, 270.0d);
      path55.curveTo(191.0d, 267.3d, 190.7d, 269.3d, 193.5d, 269.5d);
      path55.closePath();
      g2.setPaint(new Color(66, 62, 53));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path55);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path55);

      // Element 56: Path
      GeneralPath path56 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path56.moveTo(191.5d, 271.5d);
      path56.lineTo(191.0d, 271.0d);
      path56.curveTo(191.2d, 271.2d, 191.4d, 271.3d, 191.5d, 271.5d);
      path56.closePath();
      g2.setPaint(new Color(66, 62, 53));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path56);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path56);

      // Element 57: Path
      GeneralPath path57 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path57.moveTo(182.0d, 313.0d);
      path57.lineTo(181.9d, 314.4d);
      path57.curveTo(181.7d, 314.7d, 181.1d, 314.4d, 181.0d, 314.5d);
      path57.curveTo(176.6d, 315.1d, 172.4d, 317.8d, 168.3d, 319.1d);
      path57.curveTo(167.8d, 319.2d, 166.8d, 319.9d, 166.5d, 319.2d);
      path57.curveTo(171.0d, 316.0d, 177.0d, 315.0d, 182.0d, 313.0d);
      path57.closePath();
      g2.setPaint(new Color(66, 62, 53));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path57);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path57);

      // Element 58: Path
      GeneralPath path58 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path58.moveTo(157.5d, 327.0d);
      path58.lineTo(157.5d, 327.5d);
      path58.curveTo(156.5d, 327.9d, 155.2d, 328.2d, 154.1d, 328.4d);
      path58.curveTo(153.4d, 328.5d, 152.3d, 329.0d, 152.5d, 327.8d);
      path58.curveTo(153.9d, 326.9d, 156.1d, 327.3d, 157.5d, 327.0d);
      path58.closePath();
      g2.setPaint(new Color(66, 62, 53));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path58);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path58);

      // Element 59: Path
      GeneralPath path59 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path59.moveTo(157.0d, 211.0d);
      path59.lineTo(152.0d, 226.0d);
      path59.curveTo(151.0d, 225.6d, 149.5d, 225.4d, 148.5d, 225.0d);
      path59.curveTo(148.3d, 224.7d, 146.8d, 222.6d, 146.8d, 222.5d);
      path59.curveTo(145.5d, 219.7d, 147.1d, 215.5d, 148.5d, 213.0d);
      path59.curveTo(148.7d, 212.8d, 148.8d, 212.7d, 149.0d, 212.5d);
      path59.curveTo(151.7d, 211.0d, 154.0d, 211.0d, 157.0d, 211.0d);
      path59.closePath();
      g2.setPaint(new Color(231, 214, 128));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path59);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path59);

      // Element 60: Path
      GeneralPath path60 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path60.moveTo(120.5d, 276.0d);
      path60.lineTo(120.0d, 276.0d);
      path60.curveTo(120.1d, 275.5d, 119.5d, 275.0d, 119.5d, 274.8d);
      path60.lineTo(119.5d, 258.8d);
      path60.curveTo(119.5d, 258.3d, 118.2d, 256.5d, 118.0d, 255.8d);
      path60.curveTo(116.1d, 249.8d, 113.9d, 244.0d, 113.0d, 237.8d);
      path60.lineTo(114.0d, 237.8d);
      path60.lineTo(116.2d, 248.0d);
      path60.lineTo(119.5d, 257.0d);
      path60.lineTo(119.5d, 233.8d);
      path60.curveTo(119.5d, 233.7d, 120.2d, 233.0d, 120.5d, 233.0d);
      path60.lineTo(120.5d, 276.0d);
      path60.closePath();
      g2.setPaint(new Color(23, 23, 23));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path60);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path60);

      // Element 61: Path
      GeneralPath path61 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path61.moveTo(124.5d, 278.5d);
      path61.curveTo(124.2d, 278.9d, 123.0d, 280.8d, 122.5d, 281.5d);
      path61.curveTo(122.6d, 281.0d, 123.2d, 279.6d, 123.0d, 279.5d);
      path61.curveTo(118.2d, 279.7d, 114.4d, 279.0d, 112.7d, 284.4d);
      path61.curveTo(110.7d, 290.4d, 115.0d, 290.6d, 119.0d, 292.5d);
      path61.curveTo(119.2d, 292.6d, 119.4d, 292.4d, 119.5d, 292.5d);
      path61.curveTo(120.5d, 292.9d, 121.3d, 294.1d, 122.6d, 294.6d);
      path61.curveTo(126.4d, 295.9d, 130.3d, 292.8d, 133.5d, 291.2d);
      path61.curveTo(134.2d, 290.9d, 135.2d, 291.1d, 135.5d, 291.0d);
      path61.lineTo(135.5d, 291.5d);
      path61.curveTo(130.0d, 292.6d, 132.5d, 299.2d, 131.2d, 302.0d);
      path61.curveTo(130.9d, 302.8d, 128.4d, 304.7d, 127.5d, 305.8d);
      path61.curveTo(125.5d, 308.2d, 118.2d, 319.1d, 116.0d, 319.5d);
      path61.curveTo(116.2d, 317.3d, 118.2d, 316.3d, 119.5d, 314.7d);
      path61.curveTo(121.9d, 311.7d, 124.1d, 308.3d, 126.5d, 305.3d);
      path61.curveTo(127.4d, 304.2d, 129.2d, 303.0d, 129.7d, 302.0d);
      path61.curveTo(130.9d, 299.8d, 130.4d, 295.9d, 131.5d, 293.5d);
      path61.curveTo(128.3d, 294.2d, 126.1d, 297.1d, 122.3d, 295.9d);
      path61.curveTo(121.3d, 295.6d, 121.3d, 294.4d, 120.6d, 294.0d);
      path61.curveTo(119.7d, 293.5d, 117.6d, 293.4d, 116.4d, 292.9d);
      path61.curveTo(111.3d, 290.9d, 109.5d, 286.6d, 112.6d, 281.8d);
      path61.curveTo(115.2d, 277.8d, 120.3d, 278.6d, 124.5d, 278.5d);
      path61.closePath();
      g2.setPaint(new Color(39, 36, 32));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path61);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path61);

      // Element 62: Path
      GeneralPath path62 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path62.moveTo(117.0d, 313.0d);
      path62.curveTo(106.4d, 311.3d, 96.3d, 307.9d, 85.8d, 306.0d);
      path62.curveTo(85.1d, 305.9d, 84.0d, 305.7d, 85.0d, 305.0d);
      path62.lineTo(117.0d, 313.0d);
      path62.closePath();
      g2.setPaint(new Color(17, 17, 16));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path62);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path62);

      // Element 63: Path
      GeneralPath path63 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path63.moveTo(115.0d, 315.5d);
      path63.curveTo(115.8d, 316.1d, 115.0d, 316.4d, 114.3d, 316.5d);
      path63.curveTo(111.8d, 316.8d, 108.8d, 316.6d, 106.3d, 317.1d);
      path63.curveTo(100.2d, 318.0d, 94.2d, 320.3d, 88.5d, 322.5d);
      path63.curveTo(87.6d, 321.7d, 90.9d, 320.5d, 91.2d, 320.4d);
      path63.curveTo(98.4d, 317.3d, 107.2d, 315.9d, 115.0d, 315.5d);
      path63.closePath();
      g2.setPaint(new Color(17, 17, 16));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path63);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path63);

      // Element 64: Path
      GeneralPath path64 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path64.moveTo(145.0d, 254.5d);
      path64.curveTo(144.9d, 254.7d, 145.1d, 255.2d, 145.0d, 255.5d);
      path64.curveTo(143.2d, 258.2d, 141.0d, 261.3d, 139.0d, 264.0d);
      path64.curveTo(138.7d, 263.9d, 138.3d, 264.1d, 138.0d, 264.0d);
      path64.curveTo(136.3d, 263.5d, 134.8d, 261.5d, 132.2d, 261.5d);
      path64.curveTo(129.8d, 261.4d, 127.4d, 263.4d, 126.0d, 265.3d);
      path64.curveTo(122.7d, 269.7d, 125.5d, 270.5d, 126.0d, 274.5d);
      path64.curveTo(126.6d, 274.7d, 127.4d, 274.8d, 128.0d, 275.0d);
      path64.curveTo(127.0d, 276.7d, 127.0d, 276.0d, 126.0d, 277.0d);
      path64.curveTo(125.9d, 276.2d, 126.1d, 275.3d, 126.0d, 274.5d);
      path64.curveTo(123.5d, 273.6d, 123.1d, 269.8d, 123.6d, 267.4d);
      path64.curveTo(124.2d, 264.7d, 128.6d, 260.8d, 131.3d, 260.5d);
      path64.curveTo(133.2d, 260.3d, 135.0d, 261.2d, 136.6d, 262.0d);
      path64.curveTo(137.1d, 262.2d, 137.2d, 263.7d, 138.9d, 262.9d);
      path64.curveTo(139.6d, 259.7d, 143.2d, 258.3d, 143.5d, 255.0d);
      path64.curveTo(138.7d, 254.7d, 139.7d, 245.0d, 141.4d, 242.1d);
      path64.curveTo(141.9d, 241.2d, 143.1d, 240.4d, 144.0d, 240.0d);
      path64.curveTo(141.3d, 243.9d, 138.3d, 253.4d, 145.0d, 254.5d);
      path64.closePath();
      g2.setPaint(new Color(39, 36, 32));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path64);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path64);

      // Element 65: Path
      GeneralPath path65 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path65.moveTo(158.0d, 211.0d);
      path65.curveTo(157.7d, 211.0d, 157.3d, 211.0d, 157.0d, 211.0d);
      path65.curveTo(154.0d, 211.0d, 151.7d, 211.0d, 149.0d, 212.5d);
      path65.curveTo(149.1d, 212.5d, 149.0d, 211.7d, 149.5d, 211.3d);
      path65.curveTo(151.0d, 209.9d, 155.4d, 210.0d, 157.5d, 210.0d);
      path65.curveTo(157.7d, 207.7d, 158.0d, 205.3d, 158.5d, 203.0d);
      path65.curveTo(160.1d, 205.0d, 158.6d, 208.7d, 158.0d, 211.0d);
      path65.closePath();
      g2.setPaint(new Color(39, 36, 32));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path65);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path65);

      // Element 66: Path
      GeneralPath path66 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path66.moveTo(150.0d, 240.0d);
      path66.lineTo(149.5d, 240.0d);
      path66.curveTo(149.6d, 239.7d, 149.7d, 238.6d, 149.2d, 238.5d);
      path66.curveTo(148.6d, 238.5d, 144.7d, 239.7d, 144.0d, 240.0d);
      path66.curveTo(144.1d, 239.9d, 143.9d, 239.4d, 144.1d, 239.1d);
      path66.curveTo(145.3d, 237.8d, 149.2d, 237.8d, 149.6d, 237.2d);
      path66.curveTo(150.4d, 234.8d, 149.7d, 231.5d, 151.5d, 229.5d);
      path66.curveTo(151.5d, 232.7d, 150.7d, 236.5d, 150.0d, 240.0d);
      path66.closePath();
      g2.setPaint(new Color(33, 32, 29));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path66);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path66);

      // Element 67: Path
      GeneralPath path67 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path67.moveTo(148.5d, 213.0d);
      path67.curveTo(147.1d, 215.5d, 145.5d, 219.7d, 146.8d, 222.5d);
      path67.curveTo(146.8d, 222.6d, 148.3d, 224.7d, 148.5d, 225.0d);
      path67.curveTo(144.1d, 223.1d, 144.5d, 217.7d, 146.9d, 214.1d);
      path67.curveTo(147.7d, 213.0d, 148.4d, 213.1d, 148.5d, 213.0d);
      path67.closePath();
      g2.setPaint(new Color(39, 36, 32));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path67);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path67);

      // Element 68: Path
      GeneralPath path68 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path68.moveTo(101.5d, 313.5d);
      path68.curveTo(102.3d, 314.1d, 101.4d, 314.4d, 100.8d, 314.5d);
      path68.curveTo(96.8d, 315.0d, 92.9d, 315.2d, 89.0d, 316.0d);
      path68.curveTo(88.7d, 314.6d, 90.0d, 314.9d, 90.9d, 314.7d);
      path68.curveTo(94.4d, 313.8d, 98.0d, 314.0d, 101.5d, 313.5d);
      path68.closePath();
      g2.setPaint(new Color(17, 17, 16));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path68);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path68);

      // Element 69: Path
      GeneralPath path69 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path69.moveTo(152.0d, 226.0d);
      path69.curveTo(152.2d, 226.1d, 152.3d, 226.4d, 152.5d, 226.5d);
      path69.curveTo(152.3d, 227.2d, 152.9d, 228.7d, 151.5d, 229.0d);
      path69.curveTo(151.4d, 228.4d, 151.7d, 227.5d, 151.4d, 227.1d);
      path69.curveTo(151.3d, 226.9d, 149.4d, 226.2d, 148.5d, 225.0d);
      path69.curveTo(149.5d, 225.4d, 151.0d, 225.6d, 152.0d, 226.0d);
      path69.closePath();
      g2.setPaint(new Color(33, 32, 29));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path69);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path69);

      // Element 70: Path
      GeneralPath path70 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path70.moveTo(159.0d, 197.0d);
      path70.lineTo(160.0d, 197.0d);
      path70.curveTo(159.8d, 198.7d, 159.2d, 199.8d, 159.0d, 201.0d);
      path70.curveTo(159.1d, 199.5d, 159.1d, 198.1d, 158.0d, 197.0d);
      path70.curveTo(158.3d, 197.0d, 158.7d, 197.0d, 159.0d, 197.0d);
      path70.closePath();
      g2.setPaint(new Color(23, 23, 23));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path70);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path70);

      // Element 71: Polygon
      GeneralPath path71 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path71.moveTo(88.0d, 322.5d);
      path71.lineTo(88.0d, 323.5d);
      path71.lineTo(86.5d, 323.5d);
      path71.lineTo(87.3d, 322.5d);
      path71.lineTo(88.0d, 322.5d);
      path71.closePath();
      g2.setPaint(new Color(17, 17, 16));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path71);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path71);

      // Element 72: Polygon
      GeneralPath path72 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path72.moveTo(85.5d, 325.5d);
      path72.lineTo(85.0d, 325.5d);
      path72.lineTo(85.0d, 324.8d);
      path72.lineTo(85.8d, 324.0d);
      path72.lineTo(85.5d, 325.5d);
      path72.closePath();
      g2.setPaint(new Color(17, 17, 16));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path72);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path72);

      // Element 73: Path
      GeneralPath path73 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path73.moveTo(157.5d, 290.5d);
      path73.curveTo(160.0d, 290.1d, 163.6d, 290.7d, 166.3d, 290.5d);
      path73.curveTo(169.0d, 290.3d, 171.8d, 289.2d, 174.5d, 289.8d);
      path73.curveTo(173.2d, 291.1d, 171.3d, 292.8d, 169.8d, 293.8d);
      path73.curveTo(167.8d, 295.2d, 159.9d, 300.1d, 158.1d, 300.4d);
      path73.curveTo(157.2d, 300.6d, 156.4d, 300.3d, 155.5d, 300.0d);
      path73.curveTo(154.8d, 299.5d, 154.1d, 298.6d, 153.5d, 298.0d);
      path73.lineTo(153.9d, 297.7d);
      path73.curveTo(152.8d, 295.4d, 155.1d, 295.1d, 155.0d, 293.0d);
      path73.curveTo(155.2d, 292.9d, 155.3d, 292.6d, 155.5d, 292.5d);
      path73.curveTo(156.4d, 291.8d, 156.8d, 292.7d, 157.5d, 290.5d);
      path73.closePath();
      g2.setPaint(new Color(52, 52, 52));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path73);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path73);

      // Element 74: Path
      GeneralPath path74 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path74.moveTo(179.0d, 287.0d);
      path74.lineTo(179.0d, 287.5d);
      path74.curveTo(175.7d, 290.1d, 161.4d, 301.3d, 158.2d, 301.5d);
      path74.curveTo(156.9d, 301.6d, 156.4d, 300.7d, 155.5d, 300.0d);
      path74.curveTo(156.4d, 300.3d, 157.2d, 300.6d, 158.1d, 300.4d);
      path74.curveTo(159.9d, 300.1d, 167.8d, 295.2d, 169.8d, 293.8d);
      path74.curveTo(171.3d, 292.8d, 173.2d, 291.1d, 174.5d, 289.7d);
      path74.curveTo(171.8d, 289.2d, 169.0d, 290.4d, 166.3d, 290.5d);
      path74.curveTo(163.6d, 290.7d, 160.0d, 290.1d, 157.5d, 290.5d);
      path74.curveTo(156.8d, 292.7d, 156.4d, 291.8d, 155.5d, 292.5d);
      path74.curveTo(155.6d, 291.2d, 156.3d, 290.7d, 157.5d, 290.5d);
      path74.curveTo(158.3d, 287.8d, 158.3d, 289.4d, 159.0d, 288.5d);
      path74.curveTo(158.9d, 289.0d, 158.9d, 289.3d, 159.5d, 289.5d);
      path74.curveTo(160.6d, 289.8d, 165.3d, 289.6d, 166.8d, 289.5d);
      path74.curveTo(169.6d, 289.4d, 176.8d, 288.4d, 179.0d, 287.0d);
      path74.closePath();
      g2.setPaint(new Color(33, 32, 29));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path74);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path74);

      // Element 75: Path
      GeneralPath path75 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path75.moveTo(176.5d, 272.5d);
      path75.curveTo(175.1d, 273.0d, 175.4d, 274.5d, 173.5d, 275.0d);
      path75.curveTo(174.3d, 273.8d, 175.0d, 272.9d, 175.5d, 271.5d);
      path75.curveTo(176.7d, 267.8d, 174.4d, 266.1d, 170.8d, 266.0d);
      path75.curveTo(169.2d, 266.0d, 169.4d, 266.1d, 170.7d, 266.5d);
      path75.curveTo(173.2d, 267.3d, 175.5d, 268.4d, 175.0d, 271.5d);
      path75.lineTo(172.5d, 274.5d);
      path75.lineTo(173.0d, 275.0d);
      path75.curveTo(173.2d, 275.0d, 173.3d, 275.0d, 173.5d, 275.0d);
      path75.curveTo(173.3d, 275.3d, 173.3d, 275.9d, 172.8d, 276.5d);
      path75.curveTo(169.3d, 280.9d, 162.2d, 284.1d, 159.5d, 289.5d);
      path75.curveTo(158.9d, 289.3d, 158.9d, 289.0d, 159.0d, 288.5d);
      path75.curveTo(160.7d, 286.4d, 161.8d, 284.7d, 164.0d, 282.7d);
      path75.curveTo(165.7d, 281.1d, 170.7d, 278.0d, 171.5d, 276.2d);
      path75.curveTo(171.9d, 275.3d, 170.9d, 275.5d, 170.5d, 275.5d);
      path75.curveTo(170.6d, 275.3d, 170.5d, 274.8d, 171.0d, 274.3d);
      path75.curveTo(171.9d, 273.3d, 174.3d, 272.9d, 174.5d, 271.2d);
      path75.curveTo(174.8d, 268.1d, 171.4d, 266.4d, 168.8d, 268.0d);
      path75.curveTo(168.4d, 268.2d, 168.0d, 269.2d, 167.5d, 269.8d);
      path75.curveTo(166.5d, 270.9d, 165.3d, 270.1d, 166.1d, 273.1d);
      path75.curveTo(166.8d, 276.0d, 168.3d, 275.3d, 170.5d, 275.5d);
      path75.curveTo(169.8d, 276.5d, 169.1d, 276.8d, 167.8d, 276.5d);
      path75.curveTo(167.6d, 276.5d, 165.4d, 274.0d, 165.3d, 273.9d);
      path75.curveTo(163.9d, 270.6d, 167.4d, 268.5d, 169.5d, 266.7d);
      path75.curveTo(168.7d, 266.7d, 167.5d, 267.2d, 166.8d, 267.5d);
      path75.curveTo(160.0d, 270.8d, 146.4d, 282.7d, 140.6d, 288.2d);
      path75.curveTo(139.4d, 289.3d, 136.5d, 293.4d, 135.2d, 294.0d);
      path75.curveTo(134.6d, 294.2d, 134.3d, 293.9d, 134.5d, 293.2d);
      path75.lineTo(135.9d, 291.9d);
      path75.lineTo(135.5d, 291.5d);
      path75.lineTo(135.5d, 291.0d);
      path75.curveTo(142.1d, 287.7d, 148.5d, 279.7d, 154.5d, 275.0d);
      path75.curveTo(157.9d, 272.3d, 166.8d, 265.4d, 170.8d, 265.0d);
      path75.curveTo(174.7d, 264.6d, 178.2d, 268.7d, 176.5d, 272.5d);
      path75.closePath();
      g2.setPaint(new Color(66, 62, 53));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path75);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path75);

      // Element 76: Path
      GeneralPath path76 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path76.moveTo(147.5d, 319.0d);
      path76.lineTo(147.2d, 320.0d);
      path76.curveTo(146.1d, 320.2d, 141.9d, 316.1d, 142.5d, 315.5d);
      path76.lineTo(147.5d, 319.0d);
      path76.closePath();
      g2.setPaint(new Color(73, 75, 59));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path76);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path76);

      // Element 77: Path
      GeneralPath path77 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path77.moveTo(153.5d, 298.0d);
      path77.curveTo(153.0d, 297.5d, 151.9d, 296.9d, 152.1d, 296.3d);
      path77.curveTo(152.6d, 294.8d, 153.9d, 294.0d, 155.0d, 293.0d);
      path77.curveTo(155.1d, 295.1d, 152.8d, 295.4d, 153.9d, 297.7d);
      path77.lineTo(153.5d, 298.0d);
      path77.closePath();
      g2.setPaint(new Color(33, 32, 29));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path77);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path77);

      // Element 78: Path
      GeneralPath path78 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path78.moveTo(103.0d, 367.5d);
      path78.curveTo(106.0d, 369.2d, 107.8d, 369.6d, 111.2d, 369.5d);
      path78.curveTo(116.6d, 369.3d, 124.9d, 368.2d, 129.5d, 365.5d);
      path78.curveTo(129.6d, 366.7d, 128.8d, 366.8d, 128.0d, 367.3d);
      path78.curveTo(124.1d, 369.7d, 110.9d, 371.2d, 106.5d, 370.3d);
      path78.curveTo(104.4d, 369.8d, 103.8d, 369.3d, 103.0d, 367.5d);
      path78.closePath();
      g2.setPaint(new Color(35, 33, 30));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path78);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path78);

      // Element 79: Path
      GeneralPath path79 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path79.moveTo(254.0d, 238.0d);
      path79.curveTo(255.8d, 238.5d, 257.6d, 240.3d, 259.0d, 241.5d);
      path79.curveTo(259.2d, 241.7d, 259.3d, 241.8d, 259.5d, 242.0d);
      path79.curveTo(261.0d, 245.0d, 262.5d, 247.6d, 263.0d, 251.0d);
      path79.curveTo(263.9d, 257.1d, 261.8d, 263.1d, 257.5d, 267.5d);
      path79.curveTo(257.3d, 267.7d, 257.2d, 267.8d, 257.0d, 268.0d);
      path79.curveTo(247.6d, 276.9d, 236.1d, 276.4d, 224.0d, 276.5d);
      path79.curveTo(224.0d, 276.5d, 223.6d, 276.1d, 223.5d, 276.0d);
      path79.curveTo(223.4d, 274.2d, 222.0d, 272.5d, 221.0d, 271.0d);
      path79.curveTo(223.3d, 270.1d, 225.8d, 270.7d, 228.1d, 270.4d);
      path79.curveTo(233.5d, 269.6d, 240.6d, 266.1d, 244.0d, 261.8d);
      path79.curveTo(244.9d, 260.6d, 244.6d, 258.8d, 246.0d, 259.0d);
      path79.curveTo(246.1d, 262.3d, 246.5d, 265.7d, 247.3d, 268.9d);
      path79.curveTo(247.6d, 269.8d, 247.6d, 271.9d, 249.0d, 271.5d);
      path79.curveTo(247.0d, 265.7d, 247.1d, 260.2d, 247.0d, 254.2d);
      path79.curveTo(247.0d, 253.4d, 247.3d, 252.0d, 246.5d, 251.5d);
      path79.curveTo(246.5d, 251.3d, 246.5d, 251.2d, 246.5d, 251.0d);
      path79.curveTo(247.9d, 251.3d, 247.4d, 250.3d, 247.6d, 249.4d);
      path79.curveTo(248.7d, 245.4d, 249.5d, 241.5d, 252.0d, 238.0d);
      path79.curveTo(252.6d, 238.1d, 253.5d, 237.9d, 254.0d, 238.0d);
      path79.closePath();
      g2.setPaint(new Color(155, 173, 131));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path79);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path79);

      // Element 80: Path
      GeneralPath path80 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path80.moveTo(310.5d, 341.5d);
      path80.curveTo(309.6d, 335.4d, 310.3d, 330.1d, 308.9d, 323.9d);
      path80.curveTo(307.4d, 317.9d, 301.7d, 308.5d, 302.0d, 303.3d);
      path80.curveTo(302.8d, 290.3d, 310.7d, 265.2d, 314.6d, 251.9d);
      path80.curveTo(317.0d, 244.0d, 319.8d, 236.2d, 322.8d, 228.5d);
      path80.curveTo(324.0d, 228.3d, 323.5d, 229.4d, 323.3d, 230.0d);
      path80.curveTo(321.6d, 236.2d, 318.3d, 243.2d, 316.4d, 249.6d);
      path80.curveTo(313.6d, 258.7d, 311.0d, 268.6d, 308.6d, 277.9d);
      path80.curveTo(307.0d, 284.4d, 302.0d, 301.2d, 303.3d, 306.9d);
      path80.curveTo(304.3d, 310.9d, 307.4d, 315.9d, 308.7d, 320.0d);
      path80.curveTo(311.0d, 327.2d, 311.3d, 333.9d, 311.0d, 341.5d);
      path80.curveTo(310.8d, 341.6d, 310.6d, 341.4d, 310.5d, 341.5d);
      path80.closePath();
      g2.setPaint(new Color(17, 17, 16));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path80);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path80);

      // Element 81: Path
      GeneralPath path81 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path81.moveTo(257.0d, 268.0d);
      path81.lineTo(254.5d, 271.2d);
      path81.curveTo(246.2d, 277.8d, 235.5d, 277.4d, 225.5d, 277.5d);
      path81.curveTo(224.3d, 278.7d, 230.4d, 289.9d, 231.2d, 292.0d);
      path81.curveTo(234.1d, 299.0d, 235.6d, 307.8d, 239.6d, 314.2d);
      path81.curveTo(240.5d, 315.6d, 242.1d, 318.2d, 244.0d, 318.0d);
      path81.lineTo(244.0d, 318.5d);
      path81.curveTo(243.8d, 318.5d, 243.6d, 318.4d, 243.5d, 318.5d);
      path81.curveTo(243.0d, 318.7d, 242.9d, 318.9d, 243.0d, 319.5d);
      path81.curveTo(234.8d, 310.9d, 233.6d, 300.6d, 229.2d, 290.0d);
      path81.curveTo(227.5d, 285.8d, 223.6d, 280.7d, 224.0d, 276.5d);
      path81.curveTo(236.1d, 276.4d, 247.6d, 276.9d, 257.0d, 268.0d);
      path81.closePath();
      g2.setPaint(new Color(33, 32, 29));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path81);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path81);

      // Element 82: Path
      GeneralPath path82 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path82.moveTo(363.0d, 336.0d);
      path82.curveTo(363.3d, 336.3d, 363.4d, 337.1d, 362.4d, 336.9d);
      path82.curveTo(347.4d, 334.4d, 334.0d, 333.3d, 318.7d, 333.5d);
      path82.curveTo(317.6d, 333.5d, 315.6d, 334.8d, 316.0d, 333.0d);
      path82.curveTo(328.7d, 331.7d, 342.0d, 332.7d, 354.7d, 334.6d);
      path82.curveTo(355.9d, 334.7d, 362.7d, 335.7d, 363.0d, 336.0d);
      path82.closePath();
      g2.setPaint(new Color(17, 17, 16));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path82);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path82);

      // Element 83: Path
      GeneralPath path83 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path83.moveTo(244.0d, 318.5d);
      path83.lineTo(244.0d, 318.0d);
      path83.curveTo(246.7d, 316.2d, 248.5d, 316.7d, 249.4d, 313.1d);
      path83.curveTo(252.4d, 301.3d, 244.2d, 286.4d, 252.5d, 277.0d);
      path83.curveTo(252.3d, 277.2d, 250.9d, 281.3d, 250.8d, 282.0d);
      path83.curveTo(247.6d, 292.8d, 253.3d, 302.3d, 250.4d, 313.6d);
      path83.curveTo(249.4d, 317.4d, 247.1d, 317.4d, 244.0d, 318.5d);
      path83.closePath();
      g2.setPaint(new Color(39, 36, 32));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path83);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path83);

      // Element 84: Path
      GeneralPath path84 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path84.moveTo(264.0d, 251.0d);
      path84.curveTo(264.6d, 254.8d, 264.3d, 257.9d, 262.7d, 261.5d);
      path84.curveTo(262.0d, 263.3d, 259.8d, 267.6d, 257.5d, 267.5d);
      path84.curveTo(261.8d, 263.1d, 263.9d, 257.1d, 263.0d, 251.0d);
      path84.lineTo(264.0d, 251.0d);
      path84.closePath();
      g2.setPaint(new Color(23, 23, 23));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path84);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path84);

      // Element 85: Path
      GeneralPath path85 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path85.moveTo(252.0d, 231.5d);
      path85.curveTo(252.1d, 231.6d, 252.6d, 231.3d, 253.0d, 231.8d);
      path85.curveTo(254.0d, 233.1d, 254.1d, 236.1d, 255.0d, 237.3d);
      path85.curveTo(255.4d, 237.9d, 257.5d, 238.8d, 258.5d, 239.7d);
      path85.curveTo(258.9d, 240.1d, 260.1d, 241.1d, 259.0d, 241.5d);
      path85.curveTo(257.6d, 240.3d, 255.8d, 238.5d, 254.0d, 238.0d);
      path85.curveTo(254.0d, 237.7d, 254.1d, 237.3d, 254.0d, 237.0d);
      path85.curveTo(253.6d, 235.2d, 252.4d, 233.3d, 252.0d, 231.5d);
      path85.closePath();
      g2.setPaint(new Color(29, 29, 27));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path85);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path85);

      // Element 86: Path
      GeneralPath path86 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path86.moveTo(264.0d, 251.0d);
      path86.lineTo(263.0d, 251.0d);
      path86.curveTo(262.5d, 247.6d, 261.0d, 245.0d, 259.5d, 242.0d);
      path86.curveTo(259.6d, 242.1d, 260.2d, 242.0d, 260.5d, 242.3d);
      path86.curveTo(262.0d, 243.7d, 263.7d, 248.9d, 264.0d, 251.0d);
      path86.closePath();
      g2.setPaint(new Color(33, 32, 29));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path86);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path86);

      // Element 87: Path
      GeneralPath path87 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path87.moveTo(248.0d, 197.0d);
      path87.curveTo(248.8d, 197.8d, 243.0d, 201.4d, 242.5d, 201.0d);
      path87.curveTo(241.6d, 200.2d, 243.2d, 199.9d, 243.4d, 199.7d);
      path87.curveTo(244.0d, 199.3d, 247.6d, 196.7d, 248.0d, 197.0d);
      path87.closePath();
      g2.setPaint(new Color(17, 17, 16));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path87);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path87);

      // Element 88: Path
      GeneralPath path88 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path88.moveTo(253.0d, 193.5d);
      path88.curveTo(253.7d, 194.2d, 249.3d, 197.0d, 248.5d, 197.0d);
      path88.curveTo(247.7d, 196.2d, 252.7d, 193.2d, 253.0d, 193.5d);
      path88.closePath();
      g2.setPaint(new Color(17, 17, 16));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path88);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path88);

      // Element 89: Path
      GeneralPath path89 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path89.moveTo(254.0d, 275.5d);
      path89.curveTo(254.3d, 276.9d, 252.7d, 276.8d, 252.5d, 277.0d);
      path89.curveTo(252.9d, 276.6d, 252.1d, 275.9d, 254.0d, 275.5d);
      path89.closePath();
      g2.setPaint(new Color(35, 33, 30));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path89);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path89);

      // Element 90: Path
      GeneralPath path90 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path90.moveTo(251.0d, 230.5d);
      path90.curveTo(250.8d, 230.4d, 250.6d, 230.2d, 250.5d, 230.0d);
      path90.curveTo(250.7d, 230.2d, 250.9d, 230.3d, 251.0d, 230.5d);
      path90.closePath();
      g2.setPaint(new Color(29, 29, 27));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path90);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path90);

      // Element 91: Path
      GeneralPath path91 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path91.moveTo(253.8d, 193.0d);
      path91.curveTo(254.1d, 193.2d, 254.1d, 193.3d, 253.8d, 193.5d);
      path91.lineTo(253.8d, 193.0d);
      path91.closePath();
      g2.setPaint(new Color(17, 17, 16));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path91);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path91);

      // Element 92: Path
      GeneralPath path92 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path92.moveTo(234.5d, 200.0d);
      path92.curveTo(235.7d, 202.2d, 236.2d, 203.6d, 237.0d, 206.0d);
      path92.curveTo(239.9d, 215.1d, 240.7d, 223.7d, 238.8d, 233.1d);
      path92.curveTo(238.3d, 235.8d, 237.2d, 238.3d, 236.5d, 241.0d);
      path92.curveTo(235.3d, 241.5d, 233.0d, 243.5d, 231.8d, 243.4d);
      path92.curveTo(230.8d, 243.3d, 228.7d, 241.2d, 227.1d, 240.6d);
      path92.curveTo(224.3d, 239.7d, 223.7d, 240.5d, 221.5d, 240.5d);
      path92.curveTo(218.3d, 240.5d, 215.9d, 240.0d, 213.0d, 238.5d);
      path92.curveTo(209.3d, 227.1d, 207.0d, 214.4d, 214.5d, 204.0d);
      path92.curveTo(215.2d, 203.9d, 215.9d, 203.5d, 216.5d, 203.5d);
      path92.curveTo(222.8d, 203.2d, 228.5d, 201.8d, 234.5d, 200.0d);
      path92.closePath();
      g2.setPaint(new Color(200, 216, 179));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path92);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path92);

      // Element 93: Path
      GeneralPath path93 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path93.moveTo(244.5d, 113.5d);
      path93.curveTo(244.5d, 113.5d, 244.7d, 114.1d, 245.2d, 114.0d);
      path93.lineTo(245.5d, 113.0d);
      path93.lineTo(246.5d, 113.0d);
      path93.curveTo(246.5d, 113.0d, 246.5d, 114.5d, 246.5d, 114.5d);
      path93.curveTo(246.3d, 114.6d, 244.9d, 116.6d, 244.0d, 116.8d);
      path93.curveTo(244.4d, 118.4d, 245.2d, 119.9d, 245.8d, 121.4d);
      path93.curveTo(246.1d, 122.2d, 245.5d, 123.3d, 247.0d, 123.0d);
      path93.curveTo(245.8d, 130.0d, 245.7d, 130.0d, 242.5d, 136.0d);
      path93.lineTo(236.0d, 138.5d);
      path93.curveTo(235.9d, 138.6d, 237.2d, 140.1d, 237.5d, 140.5d);
      path93.curveTo(236.0d, 139.8d, 234.3d, 138.2d, 233.0d, 137.5d);
      path93.lineTo(233.0d, 136.5d);
      path93.curveTo(235.0d, 136.9d, 238.8d, 135.8d, 240.5d, 134.7d);
      path93.curveTo(244.2d, 132.3d, 245.6d, 122.4d, 243.6d, 118.6d);
      path93.curveTo(242.6d, 116.7d, 240.3d, 116.8d, 238.5d, 116.5d);
      path93.lineTo(238.5d, 115.5d);
      path93.curveTo(239.6d, 115.5d, 240.4d, 115.8d, 241.5d, 116.0d);
      path93.lineTo(241.5d, 114.5d);
      path93.curveTo(242.4d, 114.4d, 243.6d, 114.8d, 244.0d, 113.5d);
      path93.curveTo(244.2d, 113.5d, 244.3d, 113.5d, 244.5d, 113.5d);
      path93.closePath();
      path93.moveTo(245.0d, 130.5d);
      path93.lineTo(245.0d, 129.0d);
      path93.lineTo(244.5d, 129.0d);
      path93.lineTo(244.5d, 130.5d);
      path93.lineTo(245.0d, 130.5d);
      path93.closePath();
      g2.setPaint(new Color(39, 36, 32));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path93);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path93);

      // Element 94: Path
      GeneralPath path94 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path94.moveTo(242.5d, 136.0d);
      path94.curveTo(241.4d, 138.2d, 240.2d, 140.5d, 240.5d, 143.0d);
      path94.curveTo(240.2d, 142.8d, 239.8d, 142.7d, 239.5d, 142.5d);
      path94.curveTo(239.3d, 142.4d, 238.7d, 142.4d, 238.2d, 142.0d);
      path94.curveTo(237.6d, 141.5d, 237.7d, 140.8d, 237.5d, 140.5d);
      path94.curveTo(237.2d, 140.1d, 235.9d, 138.6d, 236.0d, 138.5d);
      path94.lineTo(242.5d, 136.0d);
      path94.closePath();
      g2.setPaint(new Color(209, 170, 143));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path94);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path94);

      // Element 95: Path
      GeneralPath path95 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path95.moveTo(233.0d, 136.5d);
      path95.lineTo(233.0d, 137.5d);
      path95.curveTo(229.2d, 135.5d, 223.8d, 133.9d, 219.5d, 133.5d);
      path95.curveTo(217.8d, 133.4d, 215.7d, 133.4d, 214.0d, 133.5d);
      path95.curveTo(213.9d, 133.2d, 214.1d, 132.8d, 214.0d, 132.5d);
      path95.curveTo(219.8d, 132.1d, 224.8d, 133.4d, 230.1d, 135.1d);
      path95.curveTo(231.1d, 135.4d, 232.3d, 136.4d, 233.0d, 136.5d);
      path95.closePath();
      g2.setPaint(new Color(73, 75, 59));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path95);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path95);

      // Element 96: Path
      GeneralPath path96 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path96.moveTo(216.5d, 203.5d);
      path96.curveTo(216.2d, 201.9d, 217.3d, 202.6d, 218.2d, 202.5d);
      path96.curveTo(223.7d, 202.0d, 229.0d, 200.7d, 234.2d, 199.0d);
      path96.curveTo(234.8d, 199.0d, 234.3d, 199.8d, 234.5d, 200.0d);
      path96.curveTo(228.5d, 201.8d, 222.8d, 203.2d, 216.5d, 203.5d);
      path96.closePath();
      g2.setPaint(new Color(73, 75, 59));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path96);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path96);

      // Element 97: Path
      GeneralPath path97 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path97.moveTo(244.5d, 228.0d);
      path97.lineTo(243.7d, 238.0d);
      path97.curveTo(247.0d, 236.5d, 250.6d, 236.7d, 254.0d, 237.0d);
      path97.curveTo(254.1d, 237.3d, 254.0d, 237.7d, 254.0d, 238.0d);
      path97.curveTo(253.5d, 237.9d, 252.6d, 238.1d, 252.0d, 238.0d);
      path97.curveTo(251.8d, 238.0d, 251.7d, 238.0d, 251.5d, 238.0d);
      path97.curveTo(247.2d, 237.7d, 245.2d, 238.3d, 241.2d, 239.9d);
      path97.curveTo(239.3d, 240.7d, 232.9d, 244.5d, 231.7d, 244.5d);
      path97.curveTo(230.8d, 244.4d, 227.7d, 241.7d, 226.0d, 241.2d);
      path97.curveTo(224.5d, 240.9d, 223.0d, 241.0d, 221.5d, 241.0d);
      path97.lineTo(221.5d, 240.5d);
      path97.curveTo(223.7d, 240.5d, 224.3d, 239.7d, 227.1d, 240.6d);
      path97.curveTo(228.6d, 241.1d, 230.8d, 243.3d, 231.8d, 243.4d);
      path97.curveTo(233.0d, 243.5d, 235.3d, 241.5d, 236.5d, 241.0d);
      path97.curveTo(238.6d, 240.0d, 240.7d, 239.0d, 242.9d, 238.4d);
      path97.curveTo(243.0d, 234.9d, 243.4d, 231.5d, 243.5d, 228.0d);
      path97.curveTo(243.8d, 228.0d, 244.2d, 228.0d, 244.5d, 228.0d);
      path97.closePath();
      g2.setPaint(new Color(66, 62, 53));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path97);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path97);

      // Element 98: Path
      GeneralPath path98 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path98.moveTo(238.5d, 116.5d);
      path98.curveTo(240.3d, 116.8d, 242.6d, 116.7d, 243.6d, 118.6d);
      path98.curveTo(245.6d, 122.4d, 244.2d, 132.3d, 240.5d, 134.7d);
      path98.curveTo(238.8d, 135.8d, 235.0d, 136.8d, 233.0d, 136.5d);
      path98.curveTo(232.3d, 136.4d, 231.1d, 135.4d, 230.1d, 135.1d);
      path98.curveTo(224.8d, 133.4d, 219.8d, 132.1d, 214.0d, 132.5d);
      path98.curveTo(213.2d, 130.0d, 208.1d, 124.7d, 210.8d, 122.0d);
      path98.curveTo(211.8d, 120.9d, 215.0d, 122.7d, 216.4d, 122.9d);
      path98.curveTo(219.1d, 123.2d, 221.0d, 123.1d, 223.5d, 122.0d);
      path98.curveTo(224.7d, 121.4d, 225.7d, 120.8d, 226.5d, 119.7d);
      path98.lineTo(226.5d, 119.0d);
      path98.curveTo(226.5d, 119.0d, 224.0d, 119.0d, 224.0d, 119.0d);
      path98.lineTo(219.5d, 117.5d);
      path98.curveTo(221.5d, 117.5d, 223.5d, 116.8d, 225.5d, 116.5d);
      path98.curveTo(226.2d, 116.4d, 226.9d, 116.0d, 227.0d, 116.0d);
      path98.curveTo(230.5d, 115.6d, 235.1d, 115.9d, 238.5d, 116.5d);
      path98.closePath();
      g2.setPaint(new Color(206, 188, 173));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path98);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path98);

      // Element 99: Path
      GeneralPath path99 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path99.moveTo(222.5d, 108.0d);
      path99.curveTo(218.2d, 105.0d, 216.1d, 99.9d, 214.8d, 95.0d);
      path99.curveTo(211.0d, 100.4d, 209.7d, 109.2d, 215.0d, 114.0d);
      path99.curveTo(214.9d, 113.1d, 215.7d, 112.9d, 216.0d, 112.5d);
      path99.curveTo(216.1d, 113.4d, 215.5d, 114.1d, 216.7d, 114.0d);
      path99.curveTo(219.4d, 113.6d, 222.8d, 111.9d, 225.5d, 111.2d);
      path99.curveTo(222.9d, 107.4d, 219.0d, 111.1d, 216.0d, 112.5d);
      path99.curveTo(216.3d, 112.1d, 216.1d, 111.5d, 217.0d, 110.8d);
      path99.curveTo(217.7d, 110.2d, 222.7d, 108.4d, 222.5d, 108.0d);
      path99.curveTo(222.6d, 108.1d, 223.5d, 107.9d, 223.9d, 108.3d);
      path99.curveTo(230.2d, 113.1d, 233.2d, 113.4d, 241.0d, 113.5d);
      path99.lineTo(241.0d, 114.5d);
      path99.curveTo(236.7d, 115.1d, 232.8d, 115.0d, 228.5d, 115.0d);
      path99.lineTo(228.5d, 114.0d);
      path99.curveTo(229.2d, 113.8d, 230.2d, 114.5d, 230.0d, 113.3d);
      path99.curveTo(228.4d, 110.0d, 219.3d, 114.7d, 216.5d, 114.5d);
      path99.lineTo(216.7d, 116.0d);
      path99.curveTo(219.5d, 115.6d, 222.2d, 115.0d, 225.0d, 114.5d);
      path99.lineTo(225.0d, 115.0d);
      path99.lineTo(224.0d, 116.0d);
      path99.lineTo(225.5d, 116.5d);
      path99.curveTo(223.5d, 116.8d, 221.5d, 117.5d, 219.5d, 117.5d);
      path99.lineTo(224.0d, 119.0d);
      path99.curveTo(222.8d, 120.2d, 220.0d, 119.1d, 218.8d, 118.4d);
      path99.curveTo(218.4d, 118.2d, 215.9d, 116.7d, 215.7d, 116.5d);
      path99.curveTo(210.3d, 112.5d, 209.7d, 105.9d, 211.5d, 99.7d);
      path99.curveTo(211.8d, 98.6d, 213.9d, 92.7d, 215.2d, 93.0d);
      path99.curveTo(216.2d, 95.0d, 216.2d, 96.9d, 217.1d, 99.2d);
      path99.curveTo(217.6d, 100.5d, 221.7d, 107.5d, 222.5d, 108.0d);
      path99.closePath();
      path99.moveTo(233.5d, 113.5d);
      path99.lineTo(232.0d, 113.7d);
      path99.lineTo(232.8d, 114.5d);
      path99.lineTo(233.5d, 114.5d);
      path99.curveTo(233.5d, 114.5d, 233.5d, 113.5d, 233.5d, 113.5d);
      path99.closePath();
      path99.moveTo(218.8d, 116.5d);
      path99.lineTo(218.8d, 117.0d);
      path99.curveTo(219.1d, 116.8d, 219.1d, 116.7d, 218.8d, 116.5d);
      path99.closePath();
      g2.setPaint(new Color(29, 29, 27));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path99);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path99);

      // Element 100: Path
      GeneralPath path100 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path100.moveTo(208.0d, 72.5d);
      path100.curveTo(204.8d, 69.2d, 201.7d, 69.8d, 197.7d, 71.0d);
      path100.curveTo(192.2d, 72.6d, 194.5d, 71.9d, 189.2d, 71.5d);
      path100.curveTo(180.9d, 70.9d, 176.8d, 77.6d, 177.5d, 85.3d);
      path100.curveTo(177.8d, 89.2d, 180.3d, 93.2d, 181.5d, 97.0d);
      path100.lineTo(181.0d, 97.0d);
      path100.curveTo(180.1d, 94.5d, 177.8d, 91.9d, 177.0d, 88.7d);
      path100.curveTo(174.6d, 78.9d, 178.9d, 69.5d, 190.2d, 70.6d);
      path100.curveTo(191.5d, 70.7d, 192.9d, 71.6d, 194.1d, 71.4d);
      path100.curveTo(195.0d, 71.3d, 196.4d, 70.2d, 197.6d, 69.9d);
      path100.curveTo(200.5d, 69.0d, 204.4d, 68.2d, 207.0d, 70.3d);
      path100.curveTo(208.3d, 71.3d, 207.7d, 72.0d, 208.0d, 72.5d);
      path100.closePath();
      g2.setPaint(new Color(39, 36, 32));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path100);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path100);

      // Element 101: Path
      GeneralPath path101 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path101.moveTo(208.5d, 73.5d);
      path101.curveTo(208.5d, 73.6d, 209.3d, 73.7d, 209.5d, 74.7d);
      path101.curveTo(210.4d, 80.1d, 205.7d, 89.1d, 204.2d, 94.5d);
      path101.curveTo(202.8d, 99.9d, 202.2d, 105.2d, 203.9d, 110.4d);
      path101.curveTo(204.1d, 111.3d, 207.3d, 117.7d, 207.6d, 117.9d);
      path101.curveTo(207.9d, 118.2d, 208.9d, 117.8d, 209.4d, 118.1d);
      path101.curveTo(209.6d, 118.2d, 209.9d, 119.4d, 210.8d, 120.0d);
      path101.curveTo(211.7d, 120.5d, 215.3d, 121.8d, 216.3d, 122.0d);
      path101.lineTo(223.5d, 122.0d);
      path101.curveTo(221.1d, 123.1d, 219.1d, 123.3d, 216.4d, 122.9d);
      path101.curveTo(215.0d, 122.7d, 211.8d, 120.9d, 210.8d, 122.0d);
      path101.curveTo(208.2d, 124.7d, 213.3d, 130.0d, 214.0d, 132.5d);
      path101.curveTo(214.1d, 132.8d, 214.0d, 133.2d, 214.0d, 133.5d);
      path101.curveTo(213.0d, 133.6d, 212.0d, 133.4d, 211.0d, 133.5d);
      path101.lineTo(211.0d, 133.0d);
      path101.curveTo(209.6d, 131.7d, 209.1d, 128.8d, 208.0d, 127.5d);
      path101.lineTo(208.0d, 127.0d);
      path101.curveTo(208.8d, 128.1d, 209.9d, 129.5d, 210.5d, 130.4d);
      path101.curveTo(211.2d, 131.3d, 210.6d, 132.7d, 212.5d, 133.0d);
      path101.curveTo(212.3d, 130.0d, 209.3d, 127.2d, 209.1d, 124.1d);
      path101.curveTo(209.0d, 122.7d, 210.2d, 121.9d, 210.0d, 120.5d);
      path101.curveTo(207.5d, 120.8d, 205.5d, 122.6d, 202.8d, 122.5d);
      path101.curveTo(200.3d, 122.5d, 196.7d, 120.7d, 194.0d, 120.5d);
      path101.lineTo(193.8d, 120.0d);
      path101.curveTo(193.5d, 120.0d, 193.3d, 120.0d, 193.0d, 120.0d);
      path101.lineTo(188.5d, 120.0d);
      path101.curveTo(188.5d, 120.0d, 188.5d, 119.0d, 188.5d, 119.0d);
      path101.curveTo(190.7d, 119.1d, 193.0d, 119.3d, 195.1d, 119.7d);
      path101.curveTo(200.0d, 120.6d, 204.0d, 123.1d, 209.0d, 119.8d);
      path101.curveTo(193.3d, 106.2d, 207.8d, 89.5d, 208.5d, 73.5d);
      path101.closePath();
      g2.setPaint(new Color(35, 33, 30));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path101);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path101);

      // Element 102: Path
      GeneralPath path102 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path102.moveTo(182.0d, 107.0d);
      path102.curveTo(182.2d, 104.1d, 182.0d, 99.7d, 181.0d, 97.0d);
      path102.lineTo(181.5d, 97.0d);
      path102.curveTo(182.5d, 100.0d, 184.3d, 104.2d, 182.0d, 107.0d);
      path102.closePath();
      g2.setPaint(new Color(29, 29, 27));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path102);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path102);

      // Element 103: Path
      GeneralPath path103 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path103.moveTo(253.5d, 113.5d);
      path103.curveTo(253.5d, 113.5d, 253.9d, 112.6d, 254.0d, 112.5d);
      path103.curveTo(253.9d, 112.7d, 253.8d, 113.1d, 253.5d, 113.5d);
      path103.closePath();
      g2.setPaint(new Color(23, 23, 23));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path103);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path103);

      // Element 104: Path
      GeneralPath path104 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path104.moveTo(254.5d, 112.0d);
      path104.curveTo(254.7d, 111.3d, 254.4d, 110.3d, 254.5d, 109.5d);
      path104.curveTo(253.7d, 110.4d, 252.7d, 110.2d, 252.0d, 110.5d);
      path104.curveTo(252.7d, 109.6d, 253.3d, 108.3d, 254.7d, 108.5d);
      path104.curveTo(256.6d, 110.2d, 255.2d, 111.0d, 254.5d, 112.0d);
      path104.closePath();
      g2.setPaint(new Color(23, 23, 23));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path104);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path104);

      // Element 105: Path
      GeneralPath path105 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path105.moveTo(251.0d, 116.0d);
      path105.curveTo(250.9d, 116.2d, 251.1d, 116.7d, 251.0d, 117.0d);
      path105.lineTo(250.5d, 116.3d);
      path105.curveTo(250.6d, 116.1d, 250.9d, 116.1d, 251.0d, 116.0d);
      path105.closePath();
      g2.setPaint(new Color(23, 23, 23));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path105);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path105);

      // Element 106: Path
      GeneralPath path106 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path106.moveTo(251.0d, 116.0d);
      path106.curveTo(251.1d, 115.8d, 251.4d, 115.7d, 251.5d, 115.5d);
      path106.curveTo(251.3d, 115.6d, 251.2d, 115.8d, 251.0d, 116.0d);
      path106.closePath();
      g2.setPaint(new Color(23, 23, 23));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path106);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path106);

      // Element 107: Path
      GeneralPath path107 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path107.moveTo(252.0d, 110.5d);
      path107.curveTo(251.6d, 111.1d, 252.3d, 111.7d, 250.5d, 112.0d);
      path107.curveTo(251.1d, 111.0d, 250.2d, 111.2d, 252.0d, 110.5d);
      path107.closePath();
      g2.setPaint(new Color(23, 23, 23));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path107);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path107);

      // Element 108: Path
      GeneralPath path108 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path108.moveTo(250.5d, 112.0d);
      path108.curveTo(248.3d, 115.4d, 249.1d, 113.6d, 246.5d, 114.5d);
      path108.lineTo(246.5d, 113.0d);
      path108.curveTo(246.5d, 113.0d, 245.5d, 113.0d, 245.5d, 113.0d);
      path108.lineTo(245.2d, 114.0d);
      path108.curveTo(244.7d, 114.0d, 244.5d, 113.5d, 244.5d, 113.5d);
      path108.curveTo(246.0d, 111.8d, 248.6d, 112.3d, 250.5d, 112.0d);
      path108.closePath();
      g2.setPaint(new Color(23, 23, 23));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path108);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path108);

      // Element 109: Path
      GeneralPath path109 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path109.moveTo(254.0d, 112.5d);
      path109.curveTo(254.1d, 112.3d, 254.5d, 112.1d, 254.5d, 112.0d);
      path109.curveTo(254.4d, 112.2d, 254.1d, 112.3d, 254.0d, 112.5d);
      path109.closePath();
      g2.setPaint(new Color(23, 23, 23));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path109);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path109);

      // Element 110: Path
      GeneralPath path110 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path110.moveTo(253.0d, 114.0d);
      path110.curveTo(253.2d, 113.8d, 253.3d, 113.7d, 253.5d, 113.5d);
      path110.curveTo(253.4d, 113.7d, 253.2d, 113.8d, 253.0d, 114.0d);
      path110.closePath();
      g2.setPaint(new Color(23, 23, 23));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path110);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path110);

      // Element 111: Path
      GeneralPath path111 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path111.moveTo(252.5d, 114.5d);
      path111.curveTo(252.7d, 114.3d, 252.8d, 114.2d, 253.0d, 114.0d);
      path111.curveTo(252.8d, 114.2d, 252.7d, 114.3d, 252.5d, 114.5d);
      path111.closePath();
      g2.setPaint(new Color(23, 23, 23));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path111);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path111);

      // Element 112: Path
      GeneralPath path112 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path112.moveTo(252.5d, 114.5d);
      path112.curveTo(252.4d, 114.6d, 252.6d, 115.1d, 252.4d, 115.4d);
      path112.curveTo(252.1d, 115.6d, 251.6d, 115.4d, 251.5d, 115.5d);
      path112.curveTo(251.6d, 115.3d, 251.4d, 114.9d, 251.6d, 114.6d);
      path112.curveTo(251.8d, 114.3d, 252.4d, 114.6d, 252.5d, 114.5d);
      path112.closePath();
      g2.setPaint(new Color(23, 23, 23));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path112);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path112);

      // Element 113: Path
      GeneralPath path113 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path113.moveTo(244.0d, 113.5d);
      path113.curveTo(243.6d, 114.8d, 242.4d, 114.4d, 241.5d, 114.5d);
      path113.curveTo(241.3d, 114.5d, 241.2d, 114.5d, 241.0d, 114.5d);
      path113.lineTo(241.0d, 113.5d);
      path113.curveTo(242.0d, 113.5d, 243.0d, 113.5d, 244.0d, 113.5d);
      path113.closePath();
      g2.setPaint(new Color(23, 23, 23));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path113);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path113);

      // Element 114: Path
      GeneralPath path114 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path114.moveTo(184.0d, 119.0d);
      path114.curveTo(183.7d, 120.2d, 184.8d, 119.9d, 185.5d, 120.5d);
      path114.curveTo(183.3d, 120.4d, 182.0d, 120.3d, 180.0d, 120.0d);
      path114.curveTo(181.1d, 118.9d, 182.6d, 119.1d, 184.0d, 119.0d);
      path114.closePath();
      g2.setPaint(new Color(53, 53, 52));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path114);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path114);

      // Element 115: Path
      GeneralPath path115 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path115.moveTo(188.5d, 119.0d);
      path115.lineTo(188.5d, 120.0d);
      path115.curveTo(188.5d, 120.0d, 193.0d, 120.0d, 193.0d, 120.0d);
      path115.curveTo(190.6d, 120.3d, 189.1d, 120.5d, 186.5d, 120.5d);
      path115.lineTo(186.5d, 119.0d);
      path115.curveTo(187.2d, 119.0d, 187.8d, 119.0d, 188.5d, 119.0d);
      path115.closePath();
      g2.setPaint(new Color(53, 53, 52));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path115);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path115);

      // Element 116: Path
      GeneralPath path116 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path116.moveTo(186.5d, 119.0d);
      path116.lineTo(186.5d, 120.5d);
      path116.curveTo(186.2d, 120.5d, 185.8d, 120.5d, 185.5d, 120.5d);
      path116.curveTo(184.8d, 119.9d, 183.7d, 120.2d, 184.0d, 119.0d);
      path116.curveTo(184.8d, 119.0d, 185.7d, 119.0d, 186.5d, 119.0d);
      path116.closePath();
      g2.setPaint(new Color(35, 33, 30));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path116);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path116);

      // Element 117: Path
      GeneralPath path117 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path117.moveTo(178.0d, 141.0d);
      path117.curveTo(179.0d, 137.5d, 183.6d, 132.9d, 187.5d, 133.7d);
      path117.curveTo(189.9d, 134.2d, 192.1d, 136.9d, 192.4d, 137.0d);
      path117.curveTo(194.4d, 137.4d, 195.6d, 136.1d, 197.5d, 136.0d);
      path117.lineTo(197.5d, 136.5d);
      path117.curveTo(190.7d, 139.1d, 182.0d, 144.2d, 177.2d, 149.5d);
      path117.curveTo(176.2d, 150.6d, 176.8d, 151.7d, 175.0d, 152.0d);
      path117.curveTo(175.1d, 151.8d, 175.1d, 151.2d, 175.5d, 150.8d);
      path117.curveTo(176.5d, 149.2d, 178.5d, 148.9d, 178.5d, 146.5d);
      path117.curveTo(178.7d, 146.4d, 178.9d, 146.0d, 179.0d, 146.0d);
      path117.curveTo(179.2d, 146.0d, 179.5d, 146.4d, 180.0d, 146.2d);
      path117.curveTo(180.9d, 145.8d, 183.7d, 142.9d, 185.1d, 141.9d);
      path117.curveTo(187.4d, 140.3d, 190.0d, 139.0d, 192.5d, 137.8d);
      path117.curveTo(189.7d, 134.0d, 184.4d, 133.7d, 181.0d, 136.8d);
      path117.curveTo(180.8d, 137.0d, 178.6d, 140.8d, 178.5d, 141.0d);
      path117.lineTo(178.0d, 141.0d);
      path117.closePath();
      g2.setPaint(new Color(66, 62, 53));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path117);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path117);

      // Element 118: Path
      GeneralPath path118 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path118.moveTo(208.0d, 127.5d);
      path118.curveTo(209.1d, 128.8d, 209.6d, 131.6d, 211.0d, 133.0d);
      path118.curveTo(208.9d, 132.9d, 206.9d, 133.3d, 205.0d, 134.0d);
      path118.curveTo(204.8d, 134.1d, 204.7d, 134.4d, 204.5d, 134.5d);
      path118.lineTo(200.5d, 134.5d);
      path118.curveTo(201.8d, 132.6d, 200.8d, 126.5d, 203.2d, 126.0d);
      path118.curveTo(205.0d, 125.6d, 206.8d, 126.1d, 208.0d, 127.5d);
      path118.closePath();
      g2.setPaint(new Color(216, 177, 150));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path118);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path118);

      // Element 119: Path
      GeneralPath path119 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path119.moveTo(194.0d, 120.5d);
      path119.curveTo(196.7d, 120.7d, 200.2d, 122.5d, 202.8d, 122.5d);
      path119.curveTo(205.5d, 122.6d, 207.5d, 120.8d, 210.0d, 120.5d);
      path119.curveTo(210.2d, 121.9d, 209.0d, 122.7d, 209.1d, 124.1d);
      path119.curveTo(209.3d, 127.2d, 212.3d, 129.9d, 212.5d, 133.0d);
      path119.curveTo(210.6d, 132.7d, 211.1d, 131.3d, 210.5d, 130.4d);
      path119.curveTo(209.9d, 129.5d, 208.8d, 128.1d, 208.0d, 127.0d);
      path119.curveTo(207.9d, 126.8d, 208.0d, 126.3d, 207.7d, 126.0d);
      path119.curveTo(206.5d, 124.7d, 204.5d, 125.0d, 202.8d, 125.0d);
      path119.lineTo(200.0d, 128.0d);
      path119.curveTo(200.3d, 123.5d, 197.7d, 121.4d, 193.5d, 121.0d);
      path119.lineTo(194.0d, 120.5d);
      path119.closePath();
      g2.setPaint(new Color(53, 53, 52));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path119);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path119);

      // Element 120: Rect
      Shape shape120 = new Rectangle2D.Double(176.5d, 120.5d, 2.0d, 0.5d);
      g2.setPaint(new Color(53, 53, 52));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(shape120);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(shape120);

      // Element 121: Rect
      Shape shape121 = new Rectangle2D.Double(200.0d, 129.5d, 0.5d, 1.5d);
      g2.setPaint(new Color(53, 53, 52));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(shape121);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(shape121);

      // Element 122: Path
      GeneralPath path122 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path122.moveTo(186.5d, 271.5d);
      path122.curveTo(186.2d, 272.9d, 186.6d, 275.8d, 186.5d, 277.5d);
      path122.curveTo(186.5d, 278.4d, 186.8d, 279.7d, 186.2d, 280.5d);
      path122.curveTo(185.9d, 281.0d, 185.1d, 281.1d, 184.7d, 281.5d);
      path122.curveTo(183.8d, 282.5d, 179.9d, 286.4d, 179.0d, 287.0d);
      path122.curveTo(176.8d, 288.5d, 169.6d, 289.4d, 166.8d, 289.5d);
      path122.curveTo(165.3d, 289.6d, 160.6d, 289.8d, 159.5d, 289.5d);
      path122.curveTo(162.2d, 284.1d, 169.3d, 280.9d, 172.8d, 276.5d);
      path122.curveTo(173.3d, 275.9d, 173.3d, 275.3d, 173.5d, 275.0d);
      path122.curveTo(175.4d, 274.5d, 175.1d, 273.0d, 176.5d, 272.5d);
      path122.curveTo(179.4d, 271.5d, 183.5d, 271.6d, 186.5d, 271.5d);
      path122.closePath();
      g2.setPaint(new Color(152, 174, 130));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path122);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path122);

      // Element 123: Path
      GeneralPath path123 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path123.moveTo(154.5d, 275.0d);
      path123.curveTo(148.5d, 279.8d, 142.1d, 287.7d, 135.5d, 291.0d);
      path123.curveTo(135.2d, 291.1d, 134.2d, 290.9d, 133.5d, 291.2d);
      path123.curveTo(130.3d, 292.8d, 126.4d, 295.9d, 122.6d, 294.6d);
      path123.curveTo(121.3d, 294.2d, 120.5d, 292.9d, 119.5d, 292.5d);
      path123.curveTo(124.5d, 290.2d, 129.6d, 288.3d, 134.3d, 285.6d);
      path123.curveTo(140.6d, 282.1d, 145.4d, 277.6d, 152.3d, 275.1d);
      path123.curveTo(153.0d, 274.8d, 154.0d, 274.0d, 154.5d, 275.0d);
      path123.closePath();
      g2.setPaint(new Color(155, 173, 131));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path123);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path123);

      // Element 124: Path
      GeneralPath path124 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path124.moveTo(209.5d, 270.5d);
      path124.curveTo(209.7d, 272.0d, 211.2d, 274.8d, 211.5d, 276.5d);
      path124.curveTo(211.4d, 276.6d, 211.8d, 277.2d, 211.1d, 277.5d);
      path124.curveTo(210.2d, 277.9d, 205.9d, 278.4d, 204.5d, 278.5d);
      path124.curveTo(202.4d, 278.7d, 200.2d, 279.3d, 198.1d, 278.9d);
      path124.curveTo(197.4d, 275.4d, 195.9d, 272.2d, 193.5d, 269.5d);
      path124.curveTo(198.8d, 269.9d, 204.2d, 269.4d, 209.5d, 270.5d);
      path124.closePath();
      g2.setPaint(new Color(152, 174, 130));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path124);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path124);

      // Element 125: Path
      GeneralPath path125 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path125.moveTo(119.0d, 292.5d);
      path125.curveTo(115.0d, 290.6d, 110.7d, 290.4d, 112.6d, 284.4d);
      path125.curveTo(114.4d, 279.0d, 118.1d, 279.7d, 123.0d, 279.5d);
      path125.curveTo(123.1d, 279.6d, 122.6d, 281.0d, 122.5d, 281.5d);
      path125.curveTo(120.5d, 284.4d, 118.5d, 286.9d, 119.0d, 290.5d);
      path125.curveTo(119.1d, 291.1d, 118.9d, 291.9d, 119.0d, 292.5d);
      path125.closePath();
      g2.setPaint(new Color(231, 214, 128));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path125);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path125);

      // Element 126: Path
      GeneralPath path126 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path126.moveTo(206.0d, 183.5d);
      path126.curveTo(206.1d, 183.4d, 206.9d, 181.5d, 207.0d, 181.2d);
      path126.curveTo(208.5d, 172.3d, 195.8d, 174.5d, 198.2d, 182.0d);
      path126.curveTo(199.2d, 185.0d, 204.2d, 185.1d, 206.0d, 183.5d);
      path126.curveTo(205.0d, 184.5d, 205.8d, 185.5d, 202.8d, 185.5d);
      path126.curveTo(194.2d, 185.8d, 195.2d, 173.1d, 204.2d, 174.5d);
      path126.curveTo(207.7d, 175.1d, 209.5d, 179.9d, 207.4d, 182.7d);
      path126.curveTo(206.9d, 183.5d, 206.1d, 183.4d, 206.0d, 183.5d);
      path126.closePath();
      g2.setPaint(new Color(66, 62, 53));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path126);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path126);

      // Element 127: Path
      GeneralPath path127 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path127.moveTo(200.4d, 166.1d);
      path127.curveTo(203.4d, 165.2d, 206.2d, 167.8d, 204.4d, 170.7d);
      path127.curveTo(201.3d, 176.1d, 195.3d, 167.6d, 200.4d, 166.1d);
      path127.closePath();
      g2.setPaint(new Color(17, 17, 16));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path127);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path127);

      // Element 128: Path
      GeneralPath path128 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path128.moveTo(217.0d, 273.0d);
      path128.curveTo(216.9d, 273.1d, 217.1d, 273.6d, 216.9d, 273.9d);
      path128.curveTo(216.6d, 274.1d, 216.1d, 273.9d, 216.0d, 274.0d);
      path128.curveTo(216.0d, 274.0d, 215.9d, 273.4d, 216.2d, 273.2d);
      path128.curveTo(216.5d, 272.9d, 217.0d, 273.0d, 217.0d, 273.0d);
      path128.closePath();
      g2.setPaint(new Color(66, 62, 53));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path128);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path128);

      // Element 129: Path
      GeneralPath path129 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path129.moveTo(209.5d, 270.5d);
      path129.curveTo(208.5d, 264.2d, 213.3d, 261.8d, 217.5d, 266.2d);
      path129.curveTo(218.4d, 267.2d, 220.2d, 269.8d, 221.0d, 271.0d);
      path129.curveTo(222.0d, 272.5d, 223.4d, 274.2d, 223.5d, 276.0d);
      path129.curveTo(221.6d, 275.1d, 222.1d, 274.1d, 220.9d, 272.4d);
      path129.curveTo(220.4d, 271.6d, 218.2d, 268.2d, 217.7d, 268.0d);
      path129.curveTo(216.1d, 267.2d, 218.0d, 271.7d, 217.9d, 272.2d);
      path129.curveTo(217.8d, 272.8d, 217.1d, 272.8d, 217.0d, 273.0d);
      path129.curveTo(217.5d, 272.2d, 216.0d, 268.9d, 216.0d, 267.8d);
      path129.lineTo(217.0d, 267.0d);
      path129.lineTo(215.5d, 267.2d);
      path129.curveTo(215.4d, 265.5d, 211.6d, 265.2d, 211.0d, 266.2d);
      path129.curveTo(209.8d, 267.8d, 210.9d, 274.4d, 212.7d, 274.5d);
      path129.curveTo(213.0d, 274.5d, 215.9d, 274.1d, 216.0d, 274.0d);
      path129.curveTo(214.4d, 275.1d, 212.4d, 275.4d, 212.1d, 275.6d);
      path129.curveTo(211.8d, 275.8d, 212.1d, 276.4d, 212.0d, 276.5d);
      path129.curveTo(211.9d, 276.6d, 211.6d, 276.4d, 211.5d, 276.5d);
      path129.curveTo(211.2d, 274.8d, 209.7d, 272.0d, 209.5d, 270.5d);
      path129.closePath();
      g2.setPaint(new Color(66, 62, 53));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path129);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path129);

      // Element 130: Path
      GeneralPath path130 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path130.moveTo(181.0d, 212.5d);
      path130.curveTo(179.5d, 214.3d, 178.9d, 214.3d, 177.5d, 215.5d);
      path130.curveTo(178.8d, 212.9d, 178.6d, 213.4d, 181.0d, 212.5d);
      path130.closePath();
      g2.setPaint(new Color(66, 62, 53));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path130);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path130);

      // Element 131: Path
      GeneralPath path131 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path131.moveTo(181.0d, 212.5d);
      path131.curveTo(181.3d, 212.1d, 180.6d, 211.7d, 182.6d, 210.9d);
      path131.curveTo(186.0d, 209.7d, 191.2d, 209.8d, 194.1d, 212.0d);
      path131.curveTo(198.1d, 215.0d, 199.1d, 218.7d, 201.7d, 222.3d);
      path131.curveTo(203.4d, 224.7d, 207.4d, 228.7d, 208.6d, 231.1d);
      path131.curveTo(211.0d, 235.8d, 210.1d, 237.8d, 206.8d, 241.5d);
      path131.curveTo(201.3d, 247.6d, 193.9d, 250.2d, 188.0d, 243.0d);
      path131.curveTo(190.0d, 243.9d, 192.2d, 245.4d, 194.0d, 246.0d);
      path131.curveTo(197.0d, 247.0d, 198.8d, 246.3d, 201.5d, 244.7d);
      path131.curveTo(203.7d, 243.4d, 208.3d, 239.2d, 208.9d, 236.7d);
      path131.curveTo(209.9d, 232.1d, 205.0d, 228.1d, 202.8d, 225.0d);
      path131.curveTo(199.3d, 219.9d, 196.6d, 212.9d, 190.0d, 211.5d);
      path131.curveTo(186.4d, 210.8d, 184.4d, 211.3d, 181.0d, 212.5d);
      path131.closePath();
      g2.setPaint(new Color(66, 62, 53));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path131);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path131);

      // Element 132: Path
      GeneralPath path132 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path132.moveTo(186.0d, 241.5d);
      path132.curveTo(176.8d, 237.6d, 168.2d, 223.3d, 177.5d, 215.5d);
      path132.curveTo(174.9d, 220.8d, 173.3d, 227.8d, 177.2d, 233.0d);
      path132.curveTo(179.7d, 236.2d, 183.4d, 238.2d, 186.0d, 241.5d);
      path132.closePath();
      g2.setPaint(new Color(66, 62, 53));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path132);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path132);

      // Element 133: Path
      GeneralPath path133 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path133.moveTo(188.0d, 243.0d);
      path133.curveTo(187.8d, 242.9d, 187.4d, 242.9d, 187.0d, 242.7d);
      path133.curveTo(186.5d, 242.5d, 186.3d, 241.9d, 186.0d, 241.5d);
      path133.curveTo(186.3d, 241.6d, 187.0d, 241.4d, 187.5d, 241.8d);
      path133.curveTo(188.1d, 242.2d, 187.9d, 242.9d, 188.0d, 243.0d);
      path133.closePath();
      g2.setPaint(new Color(66, 62, 53));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path133);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path133);

      // Element 134: Path
      GeneralPath path134 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path134.moveTo(246.5d, 251.5d);
      path134.curveTo(247.3d, 252.0d, 247.0d, 253.4d, 247.0d, 254.2d);
      path134.curveTo(247.1d, 260.2d, 247.0d, 265.7d, 249.0d, 271.5d);
      path134.curveTo(247.6d, 271.9d, 247.6d, 269.8d, 247.3d, 268.9d);
      path134.curveTo(246.5d, 265.7d, 246.1d, 262.3d, 246.0d, 259.0d);
      path134.curveTo(245.9d, 256.2d, 246.2d, 254.2d, 246.5d, 251.5d);
      path134.closePath();
      g2.setPaint(new Color(66, 62, 53));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path134);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path134);

      // Element 135: Path
      GeneralPath path135 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path135.moveTo(158.0d, 211.0d);
      path135.curveTo(156.6d, 216.3d, 154.2d, 221.3d, 152.5d, 226.5d);
      path135.curveTo(152.3d, 226.4d, 152.2d, 226.1d, 152.0d, 226.0d);
      path135.lineTo(157.0d, 211.0d);
      path135.curveTo(157.3d, 211.0d, 157.7d, 211.0d, 158.0d, 211.0d);
      path135.closePath();
      g2.setPaint(new Color(73, 75, 59));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path135);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path135);

      // Element 136: Path
      GeneralPath path136 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path136.moveTo(150.0d, 240.0d);
      path136.curveTo(148.8d, 245.8d, 148.3d, 250.5d, 145.0d, 255.5d);
      path136.curveTo(145.1d, 255.2d, 144.9d, 254.7d, 145.0d, 254.5d);
      path136.curveTo(146.8d, 249.8d, 148.4d, 244.9d, 149.5d, 240.0d);
      path136.lineTo(150.0d, 240.0d);
      path136.closePath();
      g2.setPaint(new Color(73, 75, 59));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path136);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path136);

      // Element 137: Path
      GeneralPath path137 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path137.moveTo(252.0d, 238.0d);
      path137.curveTo(249.5d, 241.5d, 248.7d, 245.4d, 247.6d, 249.4d);
      path137.curveTo(247.4d, 250.3d, 247.9d, 251.3d, 246.5d, 251.0d);
      path137.curveTo(247.1d, 246.7d, 248.2d, 241.0d, 251.5d, 238.0d);
      path137.curveTo(251.7d, 238.0d, 251.8d, 238.0d, 252.0d, 238.0d);
      path137.closePath();
      g2.setPaint(new Color(73, 75, 59));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path137);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path137);

      // Element 138: Path
      GeneralPath path138 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path138.moveTo(221.5d, 240.5d);
      path138.lineTo(221.5d, 241.0d);
      path138.curveTo(221.3d, 241.0d, 220.4d, 241.6d, 219.3d, 241.5d);
      path138.curveTo(217.1d, 241.5d, 213.7d, 240.7d, 213.0d, 238.5d);
      path138.curveTo(215.9d, 239.9d, 218.3d, 240.4d, 221.5d, 240.5d);
      path138.closePath();
      g2.setPaint(new Color(73, 75, 59));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path138);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path138);

      // Element 139: Path
      GeneralPath path139 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path139.moveTo(119.0d, 290.5d);
      path139.curveTo(118.5d, 286.9d, 120.5d, 284.4d, 122.5d, 281.5d);
      path139.curveTo(121.8d, 284.7d, 120.0d, 287.4d, 119.0d, 290.5d);
      path139.closePath();
      g2.setPaint(new Color(73, 75, 59));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path139);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path139);

      // Element 140: Path
      GeneralPath path140 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path140.moveTo(228.9d, 169.9d);
      path140.curveTo(228.0d, 171.4d, 224.8d, 169.4d, 226.2d, 167.5d);
      path140.curveTo(227.5d, 165.7d, 230.1d, 168.1d, 228.9d, 169.9d);
      path140.closePath();
      g2.setPaint(new Color(17, 17, 16));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path140);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path140);

      // Element 141: Path
      GeneralPath path141 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path141.moveTo(238.9d, 167.9d);
      path141.curveTo(237.5d, 170.3d, 235.0d, 165.7d, 237.3d, 165.0d);
      path141.curveTo(238.9d, 164.5d, 239.5d, 167.0d, 238.9d, 167.9d);
      path141.closePath();
      g2.setPaint(new Color(23, 23, 23));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path141);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path141);

      // Element 142: Path
      GeneralPath path142 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path142.moveTo(211.0d, 133.0d);
      path142.lineTo(211.0d, 133.5d);
      path142.curveTo(209.3d, 133.6d, 206.8d, 135.3d, 205.0d, 134.0d);
      path142.curveTo(206.9d, 133.3d, 208.9d, 132.9d, 211.0d, 133.0d);
      path142.closePath();
      g2.setPaint(new Color(73, 75, 59));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path142);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path142);

      // Element 143: Path
      GeneralPath path143 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path143.moveTo(188.0d, 271.0d);
      path143.curveTo(187.2d, 273.5d, 186.6d, 274.6d, 187.0d, 277.5d);
      path143.lineTo(186.5d, 277.5d);
      path143.curveTo(186.6d, 275.8d, 186.2d, 272.9d, 186.5d, 271.5d);
      path143.curveTo(186.7d, 270.7d, 187.5d, 270.4d, 188.0d, 270.0d);
      path143.curveTo(187.9d, 270.3d, 188.1d, 270.8d, 188.0d, 271.0d);
      path143.closePath();
      g2.setPaint(new Color(39, 36, 32));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path143);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path143);

      // Element 144: Path
      GeneralPath path144 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path144.moveTo(139.0d, 264.0d);
      path144.curveTo(138.7d, 264.4d, 138.1d, 265.9d, 137.8d, 266.3d);
      path144.curveTo(135.3d, 269.5d, 131.5d, 272.3d, 128.7d, 275.0d);
      path144.lineTo(128.0d, 275.0d);
      path144.curveTo(129.7d, 272.1d, 133.0d, 271.0d, 135.3d, 268.5d);
      path144.curveTo(136.3d, 267.4d, 137.5d, 265.5d, 138.0d, 264.0d);
      path144.curveTo(138.3d, 264.1d, 138.7d, 263.9d, 139.0d, 264.0d);
      path144.closePath();
      g2.setPaint(new Color(73, 75, 59));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path144);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path144);

      // Element 145: Path
      GeneralPath path145 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path145.moveTo(178.5d, 141.0d);
      path145.curveTo(178.6d, 140.8d, 180.8d, 137.0d, 181.0d, 136.8d);
      path145.curveTo(184.4d, 133.7d, 189.7d, 134.0d, 192.5d, 137.8d);
      path145.curveTo(190.0d, 139.0d, 187.5d, 140.3d, 185.1d, 141.9d);
      path145.curveTo(183.7d, 142.9d, 180.9d, 145.8d, 180.0d, 146.2d);
      path145.curveTo(179.5d, 146.4d, 179.2d, 146.0d, 179.0d, 146.0d);
      path145.curveTo(179.1d, 144.4d, 178.1d, 142.5d, 178.5d, 141.0d);
      path145.closePath();
      g2.setPaint(new Color(231, 214, 128));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path145);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path145);

      // Element 146: Path
      GeneralPath path146 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path146.moveTo(171.8d, 142.0d);
      path146.curveTo(172.1d, 142.2d, 172.1d, 142.3d, 171.8d, 142.5d);
      path146.lineTo(171.8d, 142.0d);
      path146.closePath();
      g2.setPaint(new Color(52, 52, 52));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path146);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path146);

      // Element 147: Path
      GeneralPath path147 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path147.moveTo(212.0d, 276.5d);
      path147.curveTo(213.1d, 279.6d, 214.8d, 282.8d, 215.8d, 286.0d);
      path147.curveTo(216.9d, 290.1d, 216.9d, 294.0d, 217.7d, 298.0d);
      path147.curveTo(218.2d, 300.5d, 224.3d, 316.9d, 223.5d, 317.5d);
      path147.curveTo(222.5d, 318.3d, 222.5d, 316.5d, 222.2d, 316.5d);
      path147.curveTo(221.2d, 316.5d, 220.3d, 317.3d, 218.4d, 316.9d);
      path147.curveTo(215.1d, 316.3d, 211.7d, 309.8d, 209.7d, 307.1d);
      path147.curveTo(204.9d, 300.3d, 203.5d, 297.7d, 201.0d, 289.5d);
      path147.curveTo(200.9d, 289.4d, 201.0d, 289.2d, 201.0d, 289.0d);
      path147.lineTo(201.6d, 290.0d);
      path147.curveTo(201.8d, 290.1d, 202.2d, 289.6d, 202.5d, 289.5d);
      path147.curveTo(202.4d, 290.4d, 202.5d, 291.2d, 202.7d, 292.0d);
      path147.curveTo(203.1d, 293.5d, 206.2d, 301.1d, 207.0d, 302.3d);
      path147.curveTo(208.3d, 304.2d, 215.6d, 313.7d, 217.0d, 314.8d);
      path147.curveTo(218.4d, 315.9d, 219.6d, 316.5d, 221.5d, 315.8d);
      path147.curveTo(222.1d, 315.4d, 222.0d, 315.0d, 221.9d, 314.4d);
      path147.curveTo(221.5d, 310.8d, 218.2d, 304.3d, 217.1d, 300.1d);
      path147.curveTo(216.2d, 296.2d, 216.1d, 292.3d, 215.3d, 288.5d);
      path147.curveTo(214.5d, 285.0d, 212.8d, 282.0d, 212.0d, 278.5d);
      path147.curveTo(207.1d, 278.4d, 208.0d, 281.1d, 206.4d, 284.2d);
      path147.curveTo(205.9d, 285.1d, 204.0d, 288.7d, 203.5d, 289.2d);
      path147.curveTo(203.0d, 289.7d, 202.8d, 289.4d, 202.5d, 289.5d);
      path147.curveTo(202.7d, 287.8d, 208.1d, 280.1d, 207.0d, 279.0d);
      path147.curveTo(206.3d, 279.1d, 205.6d, 278.9d, 205.0d, 279.0d);
      path147.lineTo(204.5d, 278.5d);
      path147.curveTo(205.9d, 278.4d, 210.2d, 277.9d, 211.1d, 277.5d);
      path147.curveTo(211.8d, 277.2d, 211.4d, 276.6d, 211.5d, 276.5d);
      path147.curveTo(211.5d, 276.4d, 211.9d, 276.6d, 212.0d, 276.5d);
      path147.closePath();
      g2.setPaint(new Color(39, 36, 32));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path147);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path147);

      // Element 148: Path
      GeneralPath path148 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path148.moveTo(234.5d, 327.5d);
      path148.lineTo(237.0d, 335.0d);
      path148.curveTo(235.7d, 335.3d, 236.1d, 334.7d, 235.8d, 334.0d);
      path148.curveTo(235.1d, 332.8d, 233.7d, 329.4d, 233.5d, 328.2d);
      path148.curveTo(233.4d, 327.2d, 233.7d, 327.5d, 234.5d, 327.5d);
      path148.closePath();
      g2.setPaint(new Color(66, 62, 53));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path148);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path148);

      // Element 149: Path
      GeneralPath path149 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path149.moveTo(256.5d, 276.0d);
      path149.curveTo(255.8d, 278.9d, 255.8d, 283.4d, 258.0d, 285.5d);
      path149.lineTo(258.0d, 286.0d);
      path149.curveTo(257.7d, 285.7d, 256.9d, 285.4d, 256.5d, 284.8d);
      path149.curveTo(254.8d, 282.3d, 255.0d, 278.2d, 256.0d, 275.5d);
      path149.curveTo(256.1d, 275.5d, 256.3d, 275.9d, 256.5d, 276.0d);
      path149.closePath();
      g2.setPaint(new Color(66, 62, 53));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path149);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path149);

      // Element 150: Path
      GeneralPath path150 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path150.moveTo(220.0d, 334.5d);
      path150.lineTo(222.0d, 338.5d);
      path150.curveTo(221.3d, 339.2d, 217.4d, 334.0d, 220.0d, 334.5d);
      path150.closePath();
      g2.setPaint(new Color(73, 75, 59));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path150);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path150);

      // Element 151: Path
      GeneralPath path151 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path151.moveTo(227.0d, 298.0d);
      path151.curveTo(228.2d, 299.0d, 223.0d, 300.4d, 222.5d, 300.0d);
      path151.curveTo(222.2d, 298.5d, 222.9d, 299.2d, 223.7d, 298.9d);
      path151.curveTo(224.2d, 298.8d, 226.9d, 297.9d, 227.0d, 298.0d);
      path151.closePath();
      g2.setPaint(new Color(73, 75, 59));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path151);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path151);

      // Element 152: Path
      GeneralPath path152 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path152.moveTo(243.5d, 318.5d);
      path152.lineTo(247.0d, 323.0d);
      path152.curveTo(246.1d, 322.8d, 244.4d, 321.6d, 243.7d, 321.0d);
      path152.curveTo(243.1d, 320.4d, 243.1d, 319.6d, 243.0d, 319.5d);
      path152.curveTo(242.9d, 318.9d, 243.0d, 318.8d, 243.5d, 318.5d);
      path152.closePath();
      g2.setPaint(new Color(66, 62, 53));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path152);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path152);

      // Element 153: Polygon
      GeneralPath path153 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path153.moveTo(224.0d, 341.0d);
      path153.lineTo(223.3d, 341.0d);
      path153.lineTo(222.0d, 339.0d);
      path153.lineTo(222.7d, 339.0d);
      path153.lineTo(224.0d, 341.0d);
      path153.closePath();
      g2.setPaint(new Color(73, 75, 59));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path153);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path153);

      // Element 154: Path
      GeneralPath path154 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path154.moveTo(207.5d, 327.5d);
      path154.curveTo(207.0d, 328.0d, 205.5d, 326.5d, 206.0d, 326.0d);
      path154.curveTo(206.5d, 325.5d, 208.0d, 327.0d, 207.5d, 327.5d);
      path154.closePath();
      g2.setPaint(new Color(66, 62, 53));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path154);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path154);

      // Element 155: Path
      GeneralPath path155 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path155.moveTo(256.5d, 276.0d);
      path155.curveTo(256.6d, 276.1d, 257.1d, 275.9d, 257.4d, 276.1d);
      path155.curveTo(257.9d, 276.6d, 259.1d, 284.4d, 258.0d, 285.5d);
      path155.curveTo(255.8d, 283.4d, 255.8d, 278.9d, 256.5d, 276.0d);
      path155.closePath();
      g2.setPaint(new Color(209, 170, 143));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path155);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path155);

      // Element 156: Path
      GeneralPath path156 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path156.moveTo(138.0d, 264.0d);
      path156.curveTo(137.5d, 265.5d, 136.3d, 267.4d, 135.3d, 268.5d);
      path156.curveTo(133.0d, 271.0d, 129.7d, 272.1d, 128.0d, 275.0d);
      path156.curveTo(127.4d, 274.8d, 126.6d, 274.7d, 126.0d, 274.5d);
      path156.curveTo(125.5d, 270.5d, 122.6d, 269.7d, 126.0d, 265.3d);
      path156.curveTo(127.4d, 263.4d, 129.8d, 261.4d, 132.2d, 261.5d);
      path156.curveTo(134.8d, 261.5d, 136.3d, 263.5d, 138.0d, 264.0d);
      path156.closePath();
      g2.setPaint(new Color(231, 214, 128));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path156);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path156);

      // Element 157: Path
      GeneralPath path157 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path157.moveTo(144.0d, 240.0d);
      path157.curveTo(144.7d, 239.7d, 148.6d, 238.4d, 149.2d, 238.5d);
      path157.curveTo(149.7d, 238.6d, 149.6d, 239.7d, 149.5d, 240.0d);
      path157.curveTo(148.4d, 244.9d, 146.8d, 249.8d, 145.0d, 254.5d);
      path157.curveTo(138.3d, 253.4d, 141.3d, 243.9d, 144.0d, 240.0d);
      path157.closePath();
      g2.setPaint(new Color(231, 214, 128));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path157);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path157);

      // Element 158: Path
      GeneralPath path158 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path158.moveTo(170.5d, 275.5d);
      path158.curveTo(168.4d, 275.3d, 166.8d, 276.0d, 166.1d, 273.1d);
      path158.curveTo(165.4d, 270.1d, 166.5d, 270.9d, 167.5d, 269.8d);
      path158.curveTo(168.0d, 269.3d, 168.4d, 268.2d, 168.8d, 268.0d);
      path158.curveTo(171.4d, 266.4d, 174.8d, 268.1d, 174.5d, 271.2d);
      path158.curveTo(174.3d, 272.9d, 171.9d, 273.3d, 171.0d, 274.3d);
      path158.curveTo(170.5d, 274.8d, 170.6d, 275.3d, 170.5d, 275.5d);
      path158.closePath();
      g2.setPaint(new Color(216, 177, 150));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path158);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path158);

      // Element 159: Path
      GeneralPath path159 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path159.moveTo(175.0d, 271.5d);
      path159.curveTo(175.5d, 268.4d, 173.2d, 267.4d, 170.7d, 266.5d);
      path159.curveTo(169.4d, 266.1d, 169.2d, 265.9d, 170.8d, 266.0d);
      path159.curveTo(174.4d, 266.1d, 176.7d, 267.8d, 175.5d, 271.5d);
      path159.lineTo(175.0d, 271.5d);
      path159.closePath();
      g2.setPaint(new Color(217, 178, 150));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path159);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path159);

      // Element 160: Path
      GeneralPath path160 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path160.moveTo(173.5d, 275.0d);
      path160.curveTo(173.3d, 275.0d, 173.2d, 275.0d, 173.0d, 275.0d);
      path160.lineTo(172.5d, 274.5d);
      path160.lineTo(175.0d, 271.5d);
      path160.lineTo(175.5d, 271.5d);
      path160.curveTo(175.0d, 272.9d, 174.3d, 273.8d, 173.5d, 275.0d);
      path160.closePath();
      g2.setPaint(new Color(144, 144, 144));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path160);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path160);

      // Element 161: Path
      GeneralPath path161 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path161.moveTo(173.0d, 275.0d);
      path161.lineTo(172.5d, 274.5d);
      path161.lineTo(173.0d, 275.0d);
      path161.closePath();
      g2.setPaint(new Color(217, 178, 150));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path161);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path161);

      // Element 162: Path
      GeneralPath path162 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path162.moveTo(241.0d, 114.5d);
      path162.curveTo(241.2d, 114.5d, 241.3d, 114.5d, 241.5d, 114.5d);
      path162.lineTo(241.5d, 116.0d);
      path162.curveTo(240.5d, 115.8d, 239.6d, 115.5d, 238.5d, 115.5d);
      path162.curveTo(234.9d, 115.4d, 230.5d, 115.2d, 227.0d, 116.0d);
      path162.curveTo(226.9d, 116.0d, 226.2d, 116.4d, 225.5d, 116.5d);
      path162.lineTo(224.0d, 116.0d);
      path162.lineTo(225.0d, 115.0d);
      path162.lineTo(228.5d, 115.0d);
      path162.curveTo(232.8d, 115.0d, 236.7d, 115.1d, 241.0d, 114.5d);
      path162.closePath();
      g2.setPaint(new Color(53, 53, 52));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path162);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path162);

      // Element 163: Path
      GeneralPath path163 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path163.moveTo(238.5d, 115.5d);
      path163.lineTo(238.5d, 116.5d);
      path163.curveTo(235.1d, 115.9d, 230.5d, 115.7d, 227.0d, 116.0d);
      path163.curveTo(230.5d, 115.2d, 234.9d, 115.4d, 238.5d, 115.5d);
      path163.closePath();
      g2.setPaint(new Color(23, 23, 23));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path163);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path163);

      // Element 164: Rect
      Shape shape164 = new Rectangle2D.Double(244.5d, 129.0d, 0.5d, 1.5d);
      g2.setPaint(new Color(53, 53, 52));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(shape164);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(shape164);

      // Element 165: Path
      GeneralPath path165 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path165.moveTo(250.5d, 229.5d);
      path165.curveTo(250.6d, 229.6d, 250.5d, 229.8d, 250.5d, 230.0d);
      path165.curveTo(250.6d, 230.2d, 250.8d, 230.4d, 251.0d, 230.5d);
      path165.curveTo(251.4d, 231.1d, 251.5d, 231.1d, 252.0d, 231.5d);
      path165.curveTo(252.4d, 233.3d, 253.6d, 235.2d, 254.0d, 237.0d);
      path165.curveTo(250.6d, 236.7d, 247.0d, 236.5d, 243.7d, 238.0d);
      path165.lineTo(244.5d, 228.0d);
      path165.curveTo(247.2d, 228.0d, 248.2d, 228.2d, 250.5d, 229.5d);
      path165.closePath();
      g2.setPaint(new Color(152, 174, 130));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path165);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path165);

      // Element 166: Path
      GeneralPath path166 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path166.moveTo(222.5d, 108.0d);
      path166.curveTo(222.7d, 108.4d, 217.7d, 110.2d, 217.0d, 110.8d);
      path166.curveTo(216.1d, 111.5d, 216.3d, 112.1d, 216.0d, 112.5d);
      path166.curveTo(215.7d, 112.9d, 214.8d, 113.1d, 215.0d, 114.0d);
      path166.curveTo(209.7d, 109.2d, 211.1d, 100.4d, 214.8d, 95.0d);
      path166.curveTo(216.1d, 99.9d, 218.2d, 105.0d, 222.5d, 108.0d);
      path166.closePath();
      g2.setPaint(new Color(209, 170, 143));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path166);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path166);

      // Element 167: Path
      GeneralPath path167 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path167.moveTo(225.0d, 114.5d);
      path167.curveTo(222.2d, 115.0d, 219.5d, 115.6d, 216.8d, 116.0d);
      path167.lineTo(216.5d, 114.5d);
      path167.curveTo(219.3d, 114.7d, 228.4d, 110.0d, 230.0d, 113.3d);
      path167.curveTo(230.2d, 114.5d, 229.2d, 113.9d, 228.5d, 114.0d);
      path167.curveTo(227.2d, 114.3d, 226.1d, 114.3d, 225.0d, 114.5d);
      path167.closePath();
      g2.setPaint(new Color(209, 170, 143));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path167);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path167);

      // Element 168: Path
      GeneralPath path168 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path168.moveTo(216.0d, 112.5d);
      path168.curveTo(219.1d, 111.1d, 222.9d, 107.4d, 225.5d, 111.2d);
      path168.curveTo(222.8d, 111.9d, 219.4d, 113.6d, 216.7d, 114.0d);
      path168.curveTo(215.6d, 114.1d, 216.1d, 113.4d, 216.0d, 112.5d);
      path168.closePath();
      g2.setPaint(new Color(53, 53, 52));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path168);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path168);

      // Element 169: Path
      GeneralPath path169 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path169.moveTo(228.5d, 114.0d);
      path169.lineTo(228.5d, 115.0d);
      path169.lineTo(225.0d, 115.0d);
      path169.lineTo(225.0d, 114.5d);
      path169.curveTo(226.1d, 114.3d, 227.2d, 114.3d, 228.5d, 114.0d);
      path169.closePath();
      g2.setPaint(new Color(23, 23, 23));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path169);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path169);

      // Element 170: Polygon
      GeneralPath path170 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path170.moveTo(233.5d, 113.5d);
      path170.lineTo(233.5d, 114.5d);
      path170.lineTo(232.8d, 114.5d);
      path170.lineTo(232.0d, 113.8d);
      path170.lineTo(233.5d, 113.5d);
      path170.closePath();
      g2.setPaint(new Color(66, 62, 53));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path170);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path170);

      // Element 171: Path
      GeneralPath path171 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path171.moveTo(218.8d, 116.5d);
      path171.curveTo(219.1d, 116.7d, 219.1d, 116.8d, 218.8d, 117.0d);
      path171.lineTo(218.8d, 116.5d);
      path171.closePath();
      g2.setPaint(new Color(53, 53, 52));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path171);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path171);

      // Element 172: Path
      GeneralPath path172 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path172.moveTo(206.0d, 183.5d);
      path172.curveTo(204.2d, 185.1d, 199.2d, 185.0d, 198.2d, 182.0d);
      path172.curveTo(195.8d, 174.5d, 208.5d, 172.3d, 207.0d, 181.2d);
      path172.curveTo(206.9d, 181.5d, 206.1d, 183.4d, 206.0d, 183.5d);
      path172.closePath();
      g2.setPaint(new Color(227, 161, 173));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path172);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path172);

      // Element 173: Path
      GeneralPath path173 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path173.moveTo(190.0d, 211.5d);
      path173.curveTo(196.6d, 212.9d, 199.3d, 219.9d, 202.8d, 225.0d);
      path173.curveTo(205.0d, 228.1d, 209.9d, 232.1d, 208.9d, 236.6d);
      path173.curveTo(208.3d, 239.2d, 203.7d, 243.4d, 201.5d, 244.7d);
      path173.curveTo(198.8d, 246.3d, 197.0d, 247.0d, 194.0d, 246.0d);
      path173.curveTo(195.4d, 243.4d, 198.8d, 241.7d, 200.4d, 239.2d);
      path173.curveTo(204.0d, 233.4d, 196.6d, 227.2d, 193.8d, 222.5d);
      path173.curveTo(191.8d, 219.2d, 190.0d, 215.4d, 190.0d, 211.5d);
      path173.closePath();
      g2.setPaint(new Color(153, 174, 132));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path173);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path173);

      // Element 174: Path
      GeneralPath path174 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path174.moveTo(202.5d, 289.5d);
      path174.curveTo(202.8d, 289.4d, 203.0d, 289.7d, 203.5d, 289.2d);
      path174.curveTo(204.0d, 288.7d, 205.9d, 285.1d, 206.4d, 284.2d);
      path174.curveTo(208.0d, 281.1d, 207.2d, 278.4d, 212.0d, 278.5d);
      path174.curveTo(212.8d, 281.9d, 214.5d, 285.0d, 215.3d, 288.5d);
      path174.curveTo(216.1d, 292.2d, 216.2d, 296.2d, 217.2d, 300.1d);
      path174.curveTo(218.2d, 304.2d, 221.5d, 310.7d, 221.9d, 314.4d);
      path174.curveTo(222.0d, 314.9d, 222.1d, 315.4d, 221.5d, 315.8d);
      path174.curveTo(219.6d, 316.5d, 218.5d, 315.8d, 217.0d, 314.8d);
      path174.curveTo(215.6d, 313.7d, 208.3d, 304.1d, 207.0d, 302.2d);
      path174.curveTo(206.2d, 301.1d, 203.2d, 293.5d, 202.7d, 292.0d);
      path174.curveTo(202.5d, 291.2d, 202.4d, 290.4d, 202.5d, 289.5d);
      path174.closePath();
      g2.setPaint(new Color(52, 52, 52));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path174);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path174);

      // Element 175: Path
      GeneralPath path175 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
      path175.moveTo(205.0d, 279.0d);
      path175.curveTo(205.6d, 278.9d, 206.4d, 279.1d, 207.0d, 279.0d);
      path175.curveTo(208.1d, 280.0d, 202.7d, 287.8d, 202.5d, 289.5d);
      path175.curveTo(202.2d, 289.6d, 201.8d, 290.1d, 201.6d, 289.9d);
      path175.lineTo(201.0d, 289.0d);
      path175.curveTo(200.1d, 286.0d, 199.3d, 283.0d, 198.5d, 280.0d);
      path175.curveTo(200.8d, 280.6d, 203.0d, 279.3d, 205.0d, 279.0d);
      path175.closePath();
      g2.setPaint(new Color(208, 165, 139));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.fill(path175);
      g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
      g2.setPaint(new Color(0, 0, 0));
      g2.setComposite(AlphaComposite.SrcOver);
      g2.draw(path175);

      g2.dispose();
    }
  }

  /** Small UI for toggling outlines and shading to demonstrate flexibility. */


  /** Entry point with resizable frame; composition scales automatically. */
  public static void main(String[] args){
    SwingUtilities.invokeLater(() -> {
      AlvearMj_ProjectPrelims panel = new AlvearMj_ProjectPrelims();
      JFrame f = new JFrame("Alvear - Portrait");
      f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      f.setLayout(new BorderLayout());
      f.add(panel, BorderLayout.CENTER);
      f.setSize(422, 447);
      f.setLocationRelativeTo(null);
      f.setVisible(true);
    });
  }
}