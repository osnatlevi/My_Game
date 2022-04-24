package com.example.my_application_game;

public class Game_Manager {

    public static int height = 5;
    public static int width  = 3;
    public static int totalGameLives = 3;
    public static int lives = totalGameLives;

    public Game_Manager() {

    }

    public static int getLives() {
        return lives;
    }
    public static void reduceLives() {
        lives--;
    }
    public static int getTotalGameLives() {
        return totalGameLives;
    }

    public static int getHeight() {
        return height;
    }

    public static int getWidth() {
        return width;
    }








}
