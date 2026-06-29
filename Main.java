import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class  Main extends Application {
    private MediaPlayer mediaPlayer;
    @Override
    public void start(Stage primaryStage) throws Exception {
        Media media = new Media(getClass().getResource("/assets/From The Start (Instrumental).mp3").toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
        primaryStage.setTitle("Flappy Bird");
        primaryStage.setMinWidth(400);
        primaryStage.setMinHeight(400);
        primaryStage.setWidth(600);
        primaryStage.setHeight(600);
        primaryStage.setScene(buildStartScene(primaryStage));
        primaryStage.show();
    }

    private Scene buildStartScene(Stage stage) {
        // --- canvas + graphics ---
        Canvas canvas = new Canvas();
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Font.loadFont(getClass().getResourceAsStream("/assets/PressStart2P.ttf"), 10);

        // --- images ---
        Image cloudImg  = new Image(getClass().getResourceAsStream("/assets/Cloud.png"));
        Image groundImg = new Image(getClass().getResourceAsStream("/assets/Ground.png"));

        // --- cloud state ---
        List<Cloud> clouds = new ArrayList<>();
        double[] cloudSpawnTimer = {0};

        // --- ground state ---
        double[] groundX = {0};
        double groundSpeed = 1.5;

        // --- buttons ---
        Button startBtn    = buildMenuButton("START",    225);
        Button settingsBtn = buildMenuButton("SETTINGS", 225);
        Button quitBtn     = buildMenuButton("QUIT",     225);

        startBtn.setOnAction(e -> {
            stage.setScene(buildGameScene(stage));
        });
        settingsBtn.setOnAction(e -> stage.setScene(buildSettingsScene(stage)));
        quitBtn.setOnAction(e -> stage.close());

        // --- title ---
        Label titleLabel = buildLabel("Flappy Bird", 50);
        VBox titleBox = new VBox(titleLabel);
        titleBox.setAlignment(Pos.TOP_CENTER);
        titleBox.setPadding(new Insets(40, 0, 0, 0));

        // --- menu layout ---
        VBox menuBox = new VBox(15, startBtn, settingsBtn, quitBtn);
        menuBox.setAlignment(Pos.CENTER);

        // --- root: canvas behind, UI on top ---
        StackPane root = new StackPane(canvas, titleBox, menuBox);

        double w = stage.getWidth();
        double h = stage.getHeight();
        Scene scene = new Scene(root, w, h);

        canvas.widthProperty().bind(scene.widthProperty());
        canvas.heightProperty().bind(scene.heightProperty());

        // --- animation loop ---
        AnimationTimer[] timer = {null};
        timer[0] = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double W = canvas.getWidth();
                double H = canvas.getHeight();
                double groundH = H * 0.15;
                double groundY = H - groundH;
                double tileW = groundH * (groundImg.getWidth() / groundImg.getHeight());

                // background
                gc.setFill(Color.web("#ADD8E6"));
                gc.fillRect(0, 0, W, H);

                // clouds
                cloudSpawnTimer[0] += 0.016;
                if (cloudSpawnTimer[0] > 5) {
                    clouds.add(new Cloud(W, H));
                    cloudSpawnTimer[0] = 0;
                }
                for (Cloud c : clouds) {
                    c.x -= c.speed;
                    gc.drawImage(cloudImg, c.x, c.y, c.width, c.width * 0.6);
                }
                clouds.removeIf(c -> c.x + c.width < 0);

                // ground
                groundX[0] -= groundSpeed;
                if (groundX[0] <= -tileW) groundX[0] += tileW;
                for (double x = groundX[0]; x < W; x += tileW) {
                    gc.drawImage(groundImg, x, groundY, tileW, groundH);
                }
            }
        };
        timer[0].start();

        // stop animation when leaving this scene
        stage.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != scene) timer[0].stop();
        });

        return scene;
    }

    private Scene buildSettingsScene(Stage stage) {
        // title at top
        Label titleLabel = buildLabel("SETTINGS", 40);
        VBox titleBox = new VBox(titleLabel);
        titleBox.setAlignment(Pos.TOP_CENTER);
        titleBox.setPadding(new Insets(40, 0, 0, 0));

        // volume slider in center
        Label volumeLabel = buildLabel("VOLUME", 16);
        Slider volumeSlider = new Slider(0, 1, mediaPlayer.getVolume());
        volumeSlider.setMaxWidth(200);
        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            mediaPlayer.setVolume(newVal.doubleValue());
        });

        VBox centerBox = new VBox(15, volumeLabel, volumeSlider);
        centerBox.setAlignment(Pos.CENTER);

        // back button bottom left
        Button backBtn = buildMenuButton("BACK", 155);
        backBtn.setOnAction(e -> stage.setScene(buildStartScene(stage)));

        BorderPane root = new BorderPane();
        root.setTop(titleBox);
        root.setCenter(centerBox);
        root.setBottom(backBtn);
        BorderPane.setMargin(backBtn, new Insets(0, 0, 30, 30));
        root.setStyle("-fx-background-color: black;");

        double w = stage.getWidth();
        double h = stage.getHeight();
        return new Scene(root, w, h);
    }

    private Scene buildGameScene(Stage stage) {
        Canvas canvas = new Canvas();
        canvas.setMouseTransparent(true);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Image cloudImg    = new Image(getClass().getResourceAsStream("/assets/Cloud.png"));
        Image groundImg   = new Image(getClass().getResourceAsStream("/assets/Ground.png"));
        Image birdUpImg   = new Image(getClass().getResourceAsStream("/assets/BirdRisingUp.png"));
        Image birdMidImg  = new Image(getClass().getResourceAsStream("/assets/BirdHorizontal.png"));
        Image birdDownImg = new Image(getClass().getResourceAsStream("/assets/BirdFallingDown.png"));
        Image pipeImg     = new Image(getClass().getResourceAsStream("/assets/Pipe.png"));

        StackPane[] rootRef = {new StackPane(canvas)};
        StackPane root = rootRef[0];
        root.setStyle("-fx-background-color: #ADD8E6;");
        double w = stage.getWidth();
        double h = stage.getHeight();
        Scene scene = new Scene(root, w, h); // only one scene

        canvas.widthProperty().bind(scene.widthProperty());
        canvas.heightProperty().bind(scene.heightProperty());

        canvas.widthProperty().bind(scene.widthProperty());
        canvas.heightProperty().bind(scene.heightProperty());

        Bird bird = new Bird();
        Board board = new Board(bird);

        List<Cloud> clouds = new ArrayList<>();
        double[] cloudSpawnTimer = {0};
        double[] groundX = {0};
        double groundSpeed = 1.5;

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.SPACE && !board.isGameOver()) {
                bird.jump();
            }
        });

        boolean[] gameOverHandled = {false};

        AnimationTimer[] timer = {null};
        timer[0] = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double W = canvas.getWidth();
                double H = canvas.getHeight();
                double groundH = H * 0.15;
                double groundY = H - groundH;
                double tileW = groundH * (groundImg.getWidth() / groundImg.getHeight());

                boolean isGameOver = board.isGameOver();

                // --- 1. UPDATE LOGIC (Only run if the game is NOT over) ---
                if (!isGameOver) {
                    board.update();

                    // Update clouds
                    cloudSpawnTimer[0] += 0.016;
                    if (cloudSpawnTimer[0] > 5) {
                        clouds.add(new Cloud(W, H));
                        cloudSpawnTimer[0] = 0;
                    }
                    for (Cloud c : clouds) {
                        c.x -= c.speed;
                    }
                    clouds.removeIf(c -> c.x + c.width < 0);

                    // Update ground
                    groundX[0] -= groundSpeed;
                    if (groundX[0] <= -tileW) groundX[0] += tileW;
                }


                // --- 2. DRAWING LOGIC (Run every frame, even when paused/dead) ---

                // Background
                gc.setFill(Color.web("#ADD8E6"));
                gc.fillRect(0, 0, W, H);

                // Clouds
                for (Cloud c : clouds) {
                    gc.drawImage(cloudImg, c.x, c.y, c.width, c.width * 0.6);
                }

                // Pipes
                for (Pipe p : board.getPipes()) {
                    java.awt.geom.Rectangle2D top    = p.getTopBounds();
                    java.awt.geom.Rectangle2D bottom = p.getBottomBounds();
                    double pipeX = top.getX() * W;
                    double pipeW = top.getWidth() * W;

                    // top pipe (flipped so cap faces down)
                    gc.save();
                    gc.translate(pipeX, top.getHeight() * H);
                    gc.scale(1, -1);
                    gc.drawImage(pipeImg, 0, 0, pipeW, top.getHeight() * H);
                    gc.restore();

                    // bottom pipe (normal, cap faces up)
                    gc.drawImage(pipeImg, pipeX, bottom.getY() * H, pipeW, bottom.getHeight() * H);
                }

                // Bird
                double birdW    = H * bird.getSize();
                double birdH    = birdW;
                double birdDrawX = bird.getX() * W;
                double birdDrawY = bird.getY() * H;

                double velocity = bird.getVelocity();
                Image currentBird;
                if (velocity < 0)           currentBird = birdUpImg;
                else if (velocity < 0.005)  currentBird = birdMidImg;
                else                        currentBird = birdDownImg;

                gc.drawImage(currentBird, birdDrawX, birdDrawY, birdW, birdH);

                // Ground (We still loop and draw it across the screen, even if it's not moving)
                for (double x = groundX[0]; x < W; x += tileW) {
                    gc.drawImage(groundImg, x, groundY, tileW, groundH);
                }

                // Score
                gc.setFill(Color.WHITE);
                gc.fillText("Score: " + board.getScore(), W * 0.05, H * 0.07);


                // --- 3. GAME OVER OVERLAY (Draw the dim effect constantly) ---
                if (isGameOver) {
                    // Because the timer is still running, we must draw the dark rectangle every frame
                    gc.setFill(Color.rgb(0, 0, 0, 0.5));
                    gc.fillRect(0, 0, W, H);

                    // Add the UI elements only once
                    if (!gameOverHandled[0]) {
                        gameOverHandled[0] = true;

                        // NOTE: I removed timer[0].stop() from here!

                        Platform.runLater(() -> {
                            Label title = buildLabel("GAME OVER", 40);
                            Button restartBtn = buildMenuButton("RESTART", 210);
                            Button quitBtn = buildMenuButton("QUIT", 210);
                            Button menuBtn = buildMenuButton("MAIN MENU", 210);

                            restartBtn.setOnAction(e -> stage.setScene(buildGameScene(stage)));
                            quitBtn.setOnAction(e -> stage.close());
                            menuBtn.setOnAction(e -> stage.setScene(buildStartScene(stage)));

                            VBox titleBox = new VBox(title);
                            titleBox.setAlignment(Pos.TOP_CENTER);
                            titleBox.setPadding(new Insets(40, 0, 0, 0));

                            VBox buttonsBox = new VBox(15, restartBtn, quitBtn);
                            buttonsBox.setAlignment(Pos.CENTER);

                            VBox bottomLeft = new VBox(menuBtn);
                            bottomLeft.setAlignment(Pos.BOTTOM_LEFT);
                            bottomLeft.setPadding(new Insets(0, 0, 30, 30));

                            buttonsBox.setPickOnBounds(false);
                            titleBox.setPickOnBounds(false);
                            bottomLeft.setPickOnBounds(false);

                            root.getChildren().addAll(titleBox, buttonsBox, bottomLeft);
                        });
                    }
                }
            }
        };
        timer[0].start();

        stage.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != scene) timer[0].stop();
        });
        return scene;
    }

    private Button buildMenuButton(String text, int width) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: #00FFFF; -fx-text-fill: black; -fx-font-size: 18px; -fx-font-family: 'Press Start 2P'; -fx-padding: 12 40 12 40; -fx-cursor: hand;");
        btn.setPrefWidth(width);
        return btn;
    }

    private Label buildLabel(String text, int size) {
        Label label = new Label(text);
        Font customFont = Font.loadFont(getClass().getResourceAsStream("/assets/PressStart2P.ttf"), size);
        label.setStyle("-fx-text-fill: #00FFFF; -fx-effect: dropshadow(three-pass-box, black, 0, 1, 1, 1);");
        label.setFont(customFont);
        return label;
    }

    private HBox buildSettingsRow(String labelText, String btnText, Consumer<KeyCode> saveKey) {
        Label label = buildLabel(labelText, 18);
        label.setPrefWidth(100);
        Button btn = buildMenuButton(btnText, 135);
        btn.setOnAction(e -> {
            btn.setText("...");
            btn.getScene().setOnKeyPressed(k -> {
                saveKey.accept(k.getCode());
                btn.setText(k.getCode().toString());
                btn.getScene().setOnKeyPressed(null);
            });
        });
        HBox row = new HBox(20, label, btn);
        row.setAlignment(Pos.CENTER);
        return row;
    }

    public static void main(String[] args) {
        launch(args);
    }

    static class Cloud {
        double x, y, speed, width;
        Cloud(double sceneW, double sceneH) {
            this.x     = sceneW;
            this.y     = Math.random() * sceneH * 0.5;
            this.speed = 0.5 + Math.random() * 0.5;
            this.width = sceneW * 0.1 + Math.random() * sceneW * 0.08;
        }
    }
}