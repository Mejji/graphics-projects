# AlvearMj_ProjectPrelims — Java2D Portrait

A modular, rubric-ready Java2D rendering of a stylized portrait. The code demonstrates advanced 2D APIs (GeneralPath, AffineTransform, gradients, strokes, composites, optional textures/clipping) while keeping a clean structure for readability and reuse.

> **Note:** Your Java **public class name must match the filename** and cannot contain spaces. If your file is currently `AlvearMj_Project Prelims.java`, rename it and the class to **`AlvearMj_ProjectPrelims`** (no space).

---

## What this project shows

- **Java2D techniques**
  - `GeneralPath` with lines, quadratic and cubic Béziers, and close operations
  - Shape classes: `Ellipse2D`, `RoundRectangle2D`, `Rectangle2D`
  - **Per-node `AffineTransform`** (save → transform → draw → restore) in the main sequencing method
  - `LinearGradientPaint` / `RadialGradientPaint` where gradients are present
  - `AlphaComposite` for opacity and soft overlays
  - `BasicStroke` with cap / join / miter and optional dash arrays
  - Anti-aliasing and a **virtual-canvas scale** for Hi-DPI / window resizing
  - Optional hooks for **TexturePaint** and **clipping** (commented spots in code)

- **Code quality & structure**
  - A **main method** `paintPortrait(Graphics2D)` that applies transforms and calls node methods
  - **Node methods** `paintShapeNode_N(Graphics2D)` that draw at local origin (no transforms inside)
  - Clear comments above non-trivial math and transforms
  - Caching into an off-screen `BufferedImage` for smooth resizing

---

## File layout

- `AlvearMj_ProjectPrelims.java` — the entire rendering and window setup in one file  
  *(If you keep a different name, ensure the public class and filename match exactly.)*

- Sample assets (for your submission folder):
  - `original_image.jpg` — reference image
  - `output_image.png` — rendered output capture
  - `screen_shot.png` — window screenshot

---

## Build & run

From the folder that contains `AlvearMj_ProjectPrelims.java`:

```bash
javac AlvearMj_ProjectPrelims.java
java AlvearMj_ProjectPrelims
```

A resizable Swing window opens. The portrait scales to fit while preserving aspect ratio.

---

## Architecture overview

### Virtual canvas & caching
Rendering is done to a `BufferedImage`. A virtual canvas of `BASE_W × BASE_H` is scaled and centered with a single transform so the artwork remains proportional on any window size.

### Main sequencing with AffineTransform
`paintPortrait(Graphics2D graphics)` iterates all nodes using:

```java
AffineTransform t0 = graphics.getTransform();
graphics.transform(new AffineTransform(a, b, c, d, e, f));
paintShapeNode_N(graphics);   // draws at local (0,0)
graphics.setTransform(t0);
```

### Node painters
Each `paintShapeNode_N` constructs shapes/paths and performs fill/stroke using the element’s style (solid color or gradient). There are **no transforms** inside node methods.

---

## Customization tips

- **Class/window name:** change the public class name (and filename) together.
- **Canvas size:** adjust `BASE_W` / `BASE_H` (logical design resolution).
- **Background:** edit the panel’s background paint in `paintComponent`.
- **Performance:** caching is automatic on resize; set `dirty = true;` if you add runtime toggles.
- **Textures & clipping:** search for “TexturePaint” / “clip” comments to enable those demos.

---

## Screenshots to include

- `output_image.png` 
- `screen_shot.png` 
- `original_image.jpg`

---

## Troubleshooting

- **Blank window** → Ensure class name matches filename (no spaces) and you’re in the correct directory.  
- **Jagged edges** → Anti-aliasing is enabled via `RenderingHints`; verify it’s not removed.  
- **Slow on resize** → Keep the cached rendering; rely on Swing’s repaint cycle.

---

## Notes

- Uses only standard Java SE (`java.awt`, `java.awt.geom`, `javax.swing`); no external libraries.  
- All shapes are vector-based; scaling retains crisp edges.
