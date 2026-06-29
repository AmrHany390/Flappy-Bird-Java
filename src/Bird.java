import java.awt.geom.Rectangle2D;

public class Bird {
    private double x = 0.2;
    private double y = 0.5;
    private double velocity = 0;
    private final double GRAVITY = 0.0002;
    private final double birdSize = 0.08;

    // -> FIX: Create the rectangle ONCE in memory
    private Rectangle2D.Double hitbox = new Rectangle2D.Double();

    public void jump() {
        velocity = -0.0067;
    }

    public void update() {
        velocity += GRAVITY;
        y += velocity;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getVelocity() { return velocity; }
    public double getSize() { return birdSize; }

    public Rectangle2D getBounds(double aspectRatio) {
        double hitWidth = (birdSize / aspectRatio);
        double hitHeight = birdSize;
        double shrinkX = hitWidth * 0.85;
        double shrinkY = hitHeight * 0.85;

        // -> FIX: Update the existing rectangle instead of creating a new one
        hitbox.setRect(
                x + (shrinkX / 2),
                y + (shrinkY / 2),
                hitWidth - shrinkX,
                hitHeight - shrinkY
        );
        return hitbox;
    }
}