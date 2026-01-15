package skyfall.server.game;

public class PlayerInput {

    private volatile boolean left;
    private volatile boolean right;
    private volatile boolean jump;

    public PlayerInput() {}

    public boolean isLeft() {
        return left;
    }
    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isRight() {
        return right;
    }
    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isJump() {
        return jump;
    }
    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public void set(boolean left, boolean right, boolean jump) {
        this.left = left;
        this.right = right;
        this.jump = jump;
    }

    public void reset() {
        left = right = jump = false;
    }
}
