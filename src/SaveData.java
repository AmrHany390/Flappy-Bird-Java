import java.io.*;
import java.nio.file.*;

public class SaveData {
    private static final Path SAVE_FILE = Paths.get(
            System.getProperty("user.home"), "AppData", "Roaming", "FlappyBird", "save.txt"
    );

    public int highScore = 0;
    public double volume = 1.0;

    public void load() {
        try {
            if (!Files.exists(SAVE_FILE)) return;
            for (String line : Files.readAllLines(SAVE_FILE)) {
                String[] parts = line.split("=");
                if (parts.length != 2) continue;
                switch (parts[0].trim()) {
                    case "highscore" -> highScore = Integer.parseInt(parts[1].trim());
                    case "volume"    -> volume    = Double.parseDouble(parts[1].trim());
                }
            }
        } catch (Exception e) { /* corrupt file, just use defaults */ }
    }

    public void save() {
        try {
            Files.createDirectories(SAVE_FILE.getParent());
            Files.writeString(SAVE_FILE,
                    "highscore=" + highScore + "\n" +
                            "volume="    + volume
            );
        } catch (Exception e) { e.printStackTrace(); }
    }
}