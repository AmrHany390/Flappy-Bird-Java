import java.awt.geom.Rectangle2D;

public class Bird {
    private double Y;
    private  double velocity;
    private  final double GRAVITY=0.35;
    private  final int birdWidth;
    private  final int birdHeight;
    private final int birdX;

    public int getBirdX() {
        return birdX;
    }

    public Bird(double velocity, double y, int birdWidth, int birdHeight, int birdX) {
        this.velocity = velocity;
        Y = y;
        this.birdWidth = birdWidth;
        this.birdHeight = birdHeight;
        this.birdX = birdX;
    }
    public void jump(){
        velocity = -6.5 ;
    }
    public void update() {
        velocity += GRAVITY;
        Y += velocity;

    }
    public double getY() {
        return Y;
    }
    public Rectangle2D getBounds() {
        return new Rectangle2D.Double(birdX, Y, birdWidth, birdHeight);
    }
}
