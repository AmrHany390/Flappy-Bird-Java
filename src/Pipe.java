import java.awt.geom.Rectangle2D;

public class Pipe {
    private double x = 1.1;
    private final double width = 0.1;
    private final double gapY;
    private final double gapHeight;
    private boolean scored = false;

    
    private Rectangle2D.Double topBox = new Rectangle2D.Double();
    private Rectangle2D.Double bottomBox = new Rectangle2D.Double();

    public Pipe(double gapY, double gapHeight) {
        this.gapY = gapY;
        this.gapHeight = gapHeight;
    }

    public void update() {
        x -= 0.003;
    }

    public boolean isOffScreen() { return x + width < 0; }
    public double getX() { return x; }
    public double getWidth() { return width; }
    public boolean isScored() { return scored; }
    public void setScored(boolean scored) { this.scored = scored; }

    public Rectangle2D getTopBounds() {
        // -> FIX: Update existing rectangle
        topBox.setRect(x, 0, width, gapY);
        return topBox;
    }

    public Rectangle2D getBottomBounds() {
        // -> FIX: Update existing rectangle
        bottomBox.setRect(x, gapY + gapHeight, width, 1 - (gapY + gapHeight));
        return bottomBox;
    }
}