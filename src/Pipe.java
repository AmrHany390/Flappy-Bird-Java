import java.awt.geom.Rectangle2D;

public class Pipe {
    private final int screenHeight;
    private int pipeX;
    private final int pipeWidth;
    private final int gapY;
    private final int gapHeight;
    private int topPipeY;
    private int topPipeHeight;
    private int topPipeWidth;
    private int bottomPipeY;
    private int bottomPipeHeight;
    private int bottomPipeWidth;
    private boolean scored;

    public void setScored(boolean scored) {
        this.scored = scored;
    }

    public boolean isScored() {
        return scored;
    }

    public int getPipeX() {
        return pipeX;
    }

    public Pipe(int screenHeight, int pipeX, int pipeWidth, int gapY, int gapHeight) {
        this.screenHeight = screenHeight;
        this.pipeX = pipeX;
        this.pipeWidth = pipeWidth;
        this.gapY = gapY;
        this.gapHeight = gapHeight;
        setTopPipe();
        setBottomPipe();
    }
    public void setTopPipe(){
         topPipeY=0;
        topPipeHeight = gapY;
         topPipeWidth=pipeWidth;

    }
    public void setBottomPipe(){
         bottomPipeY=gapY+gapHeight;
         bottomPipeHeight = screenHeight-(gapY+gapHeight);
        bottomPipeWidth=pipeWidth;

    }
    public Rectangle2D getTopBounds() {
        return new Rectangle2D.Double(pipeX, topPipeY, topPipeWidth, topPipeHeight);
    }
    public Rectangle2D getBottomBounds() {
        return new Rectangle2D.Double(pipeX, bottomPipeY, bottomPipeWidth, bottomPipeHeight);
    }
    public void update(){
        pipeX-=2;
    }
    public boolean isOffScreen() {
        return pipeX + pipeWidth < 0;
    }

}
