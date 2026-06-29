import java.util.ArrayList;

import java.util.List;

import java.util.Random;



public class Board {

    private Bird bird;

    private int score = 0;

    private boolean gameOver = false;

    private List<Pipe> pipes = new ArrayList<>();

    private long lastSpawnTime = 0;

    private static final long SPAWN_INTERVAL_MS = 1500;

    private final double gapHeight = 0.28;



    public Board(Bird bird) {

        this.bird = bird;

        this.lastSpawnTime = System.currentTimeMillis();

    }




    public void update(double aspectRatio) {
        if (bird.getY() > 0.9) gameOver = true;
        if (gameOver) return;

        bird.update();
        for (Pipe p : pipes) p.update();


        detectCollision(aspectRatio);

        pipes.removeIf(Pipe::isOffScreen);
        for (Pipe p : pipes) {
            if (p.getX() + p.getWidth() < bird.getX() && !p.isScored()) {
                score++;
                p.setScored(true);
            }
        }
        spawnPipe();
    }


    private void detectCollision(double aspectRatio) {
        for (Pipe p : pipes) {
            if (bird.getBounds(aspectRatio).intersects(p.getTopBounds()) ||
                    bird.getBounds(aspectRatio).intersects(p.getBottomBounds())) {
                gameOver = true;
            }
        }
        if (bird.getY() > 1.0 || bird.getY() < 0.0003799999999999626
        ) gameOver = true;
    }



    private void spawnPipe() {

        long now = System.currentTimeMillis();

        if (now - lastSpawnTime >= SPAWN_INTERVAL_MS) {

            lastSpawnTime = now;

            double margin = 0.1;

            double gapY = margin + Math.random() * (1 - gapHeight - margin * 2);

            pipes.add(new Pipe(gapY, gapHeight));

        }

    }



    public boolean isGameOver() { return gameOver; }

    public int getScore() { return score; }

    public List<Pipe> getPipes() { return pipes; }

    public Bird getBird() { return bird; }

}