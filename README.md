# 🐦 Flappy Bird - JavaFX Edition

A fully playable, from-scratch recreation of the classic Flappy Bird game built using Java and JavaFX. 

This project features a custom physics engine, smooth sprite animations, and a highly optimized game loop designed to run flawlessly even in fullscreen mode. 

## ✨ Features

* **Custom Physics Engine:** Realistic gravity, jump velocity, and collision detection built entirely from scratch.
* **Dynamic Hitbox Scaling:** The game window is completely resizable. The backend physics calculate the aspect ratio in real-time so the hitboxes perfectly match the visual sprites at any resolution.
* **Zero-Allocation Game Loop:** Optimized to prevent Java Garbage Collection spikes. All hitboxes and collision objects are cached and reused, resulting in buttery-smooth, lag-free gameplay.
* **Full UI Integration:** Features a Main Menu, Game Over overlay, and a Settings screen with a working volume slider.
* **Sprite Animations:** Parallax scrolling clouds and ground, alongside a bird sprite that dynamically tilts and changes based on its current velocity.
* **Soundtrack:** Integrated background music using JavaFX `MediaPlayer`.

## 🛠️ Tech Stack

* **Language:** Java
* **GUI Framework:** JavaFX (`Canvas` API for rendering)
* **Architecture:** Object-Oriented Programming (OOP) with a strict separation between frontend rendering and backend logic.

## 🚀 How to Run

1. Make sure you have the **Java Development Kit (JDK)** and **JavaFX SDK** installed (version 11 or higher recommended).
2. Clone this repository:
   ```bash
   git clone [https://github.com/](https://github.com/)[YourUsername]/FlappyBird-JavaFX.git
   ```
3. Open the project in your preferred IDE (IntelliJ IDEA, Eclipse, etc.).
4. Ensure the `assets` folder (containing the PNG sprites, TTF font, and MP3 music) is properly set in your resources directory.
5. Add the JavaFX VM options to your run configuration (if required by your IDE):
   ```bash
   --module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.media
   ```
6. Run `FlappyBirdApp.java`!

## 🎮 Controls

* **SPACEBAR:** Jump / Flap

## 👥 Contributors:

This project was a collaborative effort, split cleanly between graphics and physics:

* **Backend Development:** Amr [github.com/AmrHany390]
  * *Responsible for the custom physics engine, game loop memory optimization, collision math, and dynamic hitbox scaling.*
* **Frontend Development:** Hassan [github.com/UpLightt]
  * *Responsible for the JavaFX UI, Canvas rendering, sprite animations, and menu layouts.*
* **Testing:** Anas [github.com/AlphaTesterAnas]
  * *Responsible for testing the game and looking for bugs in it's early stages*
* **Music:** Laufey 
  * *The amazing soundtrack*
