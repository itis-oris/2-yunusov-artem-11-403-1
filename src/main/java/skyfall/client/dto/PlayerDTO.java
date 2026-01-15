package skyfall.client.dto;

public class PlayerDTO {
    private int id;
    private String nickname;
    private float x;
    private float y;
    private boolean alive;

    private float prevX;
    private boolean facingRight = true;

    public PlayerDTO(int id, String nickname, float x, float y, boolean alive) {
        this.id = id;
        this.nickname = nickname;
        this.x = x;
        this.y = y;
        this.alive = alive;
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

    public float getPrevX() {
        return prevX;
    }
    public void setPrevX(float prevX) {
        this.prevX = prevX;
    }

    public boolean isFacingRight() {
        return facingRight;
    }
    public void setFacingRight(boolean facingRight) {
        this.facingRight = facingRight;
    }

    public void updatePosition(float newX, float newY) {
        prevX = x;
        x = newX;
        y = newY;

        if (x > prevX) facingRight = true;
        else if (x < prevX) facingRight = false;
    }
}
