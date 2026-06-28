import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Board {
    Bird bird;
    private int score = 0;
    private int screenHeight;
    private int spawnX; // Hassan so this is where the pipe is gonna spawn at. So let's say the screen has a width of 400. when you pass in the value make it 420 for example so it's invisble from the player's screen.
    private boolean gameOver;
    private final int gapHeight;
    private final int pipeWidth;
    private List<Pipe> pipes;
    int frameCount=0;
    private long lastSpawnTime = 0;
    private static final long SPAWN_INTERVAL_MS = 1500;




    public Board(int gapHeight, int pipeWidth, int screenHeight, int spawnX, Bird bird) {
        this.gapHeight = gapHeight;
        this.pipeWidth = pipeWidth;
        this.screenHeight = screenHeight;
        this.spawnX = spawnX;
        this.bird = bird;
        this.pipes   = new ArrayList<>();
        this.lastSpawnTime = System.currentTimeMillis();
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public int getScore() {
        return score;
    }

    public void detectCollision(){
        for (int i=0;i< pipes.size();i++){
        if (bird.getBounds().intersects(pipes.get(i).getTopBounds())||bird.getBounds().intersects(pipes.get(i).getBottomBounds()))
            gameOver=true;}
    }
    public List<Pipe> getPipes() {
        return pipes;
    }


    public void deletePipe(){
        pipes.removeIf(p->p.isOffScreen());
    }


    public void update() {
        if (isGameOver())return;
        bird.update();
        for (Pipe p : pipes) {
            p.update();
        }
        detectCollision();
        deletePipe();
        for (int i=0;i< pipes.size();i++){
            if (pipes.get(i).getPipeX()< bird.getBirdX()&&!pipes.get(i).isScored()){
                ++score;
                pipes.get(i).setScored(true);}
        }
        ++frameCount;
        spawnPipe();
    }


    public void spawnPipe(){
        long now = System.currentTimeMillis();
        if (now - lastSpawnTime >= SPAWN_INTERVAL_MS) {
            lastSpawnTime = now;
            int margin = 50;
            Random random = new Random();
            int minGapY = margin;
            int maxGapY = screenHeight - gapHeight - margin;
            int gapY = random.nextInt(minGapY, maxGapY);
            Pipe newPipe = new Pipe(screenHeight, spawnX, pipeWidth, gapY, gapHeight);
            pipes.add(newPipe);
        }
    }

}
