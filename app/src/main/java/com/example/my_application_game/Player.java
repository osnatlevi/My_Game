package com.example.my_application_game;

public class Player {

    private int x;
    private int y;
    private int direction;

    public Player() { }

    public Player(int x, int y, int direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;

    }
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Player setX(int x) {
        this.x = x;
        return this;
    }

    public Player setY(int y) {
        this.y = y;
        return this;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;

    }






}
