import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/*
 * Quick test harness for Bird / Pipe / Board.
 * This is NOT meant to replace Hassan's frontend - it's just a throwaway
 * window so you can sanity-check your backend logic (gravity feel, gap size,
 * collision, scoring, spawn timing) before plugging it into the real UI.
 *
 * Controls: SPACE or mouse click = jump. R = restart after game over.
 *
 * Drop this file in the same package/folder as Bird.java, Pipe.java, Board.java.
 */
public class FlappyBirdApp extends Application {

    private static final int SCREEN_WIDTH = 400;
    private static final int SCREEN_HEIGHT = 600;
    private static final int SPAWN_X = SCREEN_WIDTH + 20; // 420, matches what we told Hassan
    private static final int GAP_HEIGHT = 150;
    private static final int PIPE_WIDTH = 50;

    private Bird bird;
    private Board board;

    @Override
    public void start(Stage stage) {
        Canvas canvas = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Pane root = new Pane(canvas);
        Scene scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);

        newGame();

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.SPACE) {
                bird.jump();
            }
            if (e.getCode() == KeyCode.R && board.isGameOver()) {
                newGame();
            }
        });
        canvas.setOnMouseClicked(e -> {
            if (!board.isGameOver()) {
                bird.jump();
            } else {
                newGame();
            }
        });
        canvas.setFocusTraversable(true);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                board.update();
                draw(gc);
            }
        };
        timer.start();

        stage.setTitle("Flappy Bird - backend test harness");
        stage.setScene(scene);
        stage.show();
        canvas.requestFocus();
    }

    private void newGame() {
        // velocity=0, y=center of screen, birdWidth=30, birdHeight=30, birdX=50
        bird = new Bird(0, SCREEN_HEIGHT / 2.0, 30, 30, 50);
        board = new Board(GAP_HEIGHT, PIPE_WIDTH, SCREEN_HEIGHT, SPAWN_X, bird);
    }

    private void draw(GraphicsContext gc) {
        gc.setFill(Color.web("#70c5ce"));
        gc.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        gc.setFill(Color.web("#2e8b57"));
        for (Pipe pipe : board.getPipes()) {
            java.awt.geom.Rectangle2D top = pipe.getTopBounds();
            gc.fillRect(top.getX(), top.getY(), top.getWidth(), top.getHeight());
            java.awt.geom.Rectangle2D bottom = pipe.getBottomBounds();
            gc.fillRect(bottom.getX(), bottom.getY(), bottom.getWidth(), bottom.getHeight());
        }

        gc.setFill(Color.web("#f4c542"));
        gc.fillOval(bird.getBirdX(), bird.getY(), 30, 30);

        gc.setFill(Color.BLACK);
        gc.setFont(new Font(24));
        gc.fillText("Score: " + board.getScore(), 10, 30);

        if (board.isGameOver()) {
            gc.setFill(Color.rgb(0, 0, 0, 0.5));
            gc.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
            gc.setFill(Color.WHITE);
            gc.setFont(new Font(32));
            gc.fillText("Game Over", SCREEN_WIDTH / 2.0 - 90, SCREEN_HEIGHT / 2.0 - 10);
            gc.setFont(new Font(16));
            gc.fillText("Click or press R to restart", SCREEN_WIDTH / 2.0 - 100, SCREEN_HEIGHT / 2.0 + 20);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}