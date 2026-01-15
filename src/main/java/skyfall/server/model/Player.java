package skyfall.server.model;

import skyfall.server.game.PlayerInput;

public class Player {
    private int id;
    String nickname;
    private float x;
    private float y;
    private boolean alive = true;
    private float survivalTime = -1f;
    private float velocityY = 0f;
    private boolean onGround = false;
    private PlayerInput playerInput = new PlayerInput();

    public Player(int id, String nickname, float x, float y) {
        this.id = id;
        this.nickname = nickname;
        this.x = x;
        this.y = y;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public float getX() {
        return x;
    }
    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }
    public void setY(float y) {
        this.y = y;
    }

    public boolean isAlive() {
        return alive;
    }
    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public float getSurvivalTime() {
        return survivalTime;
    }
    public void setSurvivalTime(float survivalTime) {
        this.survivalTime = survivalTime;
    }

    public float getVelocityY() {
        return velocityY;
    }
    public void setVelocityY(float velocityY) {
        this.velocityY = velocityY;
    }

    public boolean isOnGround() {
        return onGround;
    }
    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public PlayerInput getPlayerInput() {return playerInput;}
}
