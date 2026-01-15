package skyfall.common.constants;

public class Constants {
    public static final float WORLD_WIDTH = 1200f;
    public static final float WORLD_HEIGHT = 800f;

    public static final float PLAYER_WIDTH = 25f;
    public static final float PLAYER_HEIGHT = 50f;
    public static final float PLAYER_START_X = WORLD_WIDTH / 2f;
    public static final float PLAYER_START_Y = 0f;
    public static final float PLAYER_SPEED = 8f;

    public static final float PLATFORM_WIDTH = 100f;
    public static final float PLATFORM_HEIGHT = 30f;

    public static final float START_PLATFORM_X = 0f;
    public static final float START_PLATFORM_Y = 600f;
    public static final float START_PLATFORM_WIDTH = WORLD_WIDTH;
    public static final float START_PLATFORM_HEIGHT = WORLD_HEIGHT - START_PLATFORM_Y;

    public static final float PLATFORM_MIN_GAP_Y = 75f;
    public static final float PLATFORM_MAX_GAP_Y = 125f;
    public static final float PLATFORM_MAX_DELTA_X = 250f;

    public static final float SPEED_START = 0.5f;
    public static final float SPEED_STILL = 0f;
    public static final float SPEED_INCREASE = 0.0005f;
    public static final float SPEED_MAX = 6f;

    public static final float JUMP_FORCE = -12f;
    public static final float GRAVITY = 0.5f;
    public static final float MAX_FALL_SPEED = 12f;
}
