package skyfall.server.game;

import skyfall.common.constants.Constants;
import skyfall.server.model.Platform;
import skyfall.server.model.Player;

import java.util.List;
import java.util.Random;

public class GameEngine {

    private final Random random = new Random();

    public void update(GameState gameState) {

        updatePlayers(gameState);

        if (gameState.isRunning()) {
            updatePlatforms(gameState);
        }
    }

    private void updatePlayers(GameState gameState) {
        for (Player player : gameState.getPlayers()) {
            if (!player.isAlive()) continue;

            PlayerInput playerInput = player.getPlayerInput();

            float dx = 0;
            if (playerInput.isLeft() && !playerInput.isRight()) dx = -Constants.PLAYER_SPEED;
            if (playerInput.isRight() && !playerInput.isLeft()) dx = Constants.PLAYER_SPEED;
            float newX = player.getX() + dx;
            newX = Math.max(0, Math.min(newX, Constants.WORLD_WIDTH - Constants.PLAYER_WIDTH));
            player.setX(newX);


            float velocityY = player.getVelocityY();
            if (playerInput.isJump() && player.isOnGround()) {
                velocityY = Constants.JUMP_FORCE;
                player.setOnGround(false);
            }

            velocityY += Constants.GRAVITY;
            if (velocityY > Constants.MAX_FALL_SPEED) {
                velocityY = Constants.MAX_FALL_SPEED;
            }

            float prevY = player.getY();
            float newY = prevY + velocityY;

            boolean landed = false;

            if (velocityY > 0) {
                for (Platform platform : gameState.getPlatforms()) {
                    float playerLeft = newX;
                    float playerRight = newX + Constants.PLAYER_WIDTH;
                    float playerBottomPrev = prevY + Constants.PLAYER_HEIGHT;
                    float playerBottomNow = newY + Constants.PLAYER_HEIGHT;

                    float platformLeft = platform.getX();
                    float platformRight = platform.getX() + platform.getWidth();
                    float platformTop = platform.getY();

                    boolean overlapX = playerRight > platformLeft && playerLeft < platformRight;
                    boolean crossedPlatform = playerBottomPrev <= platformTop && playerBottomNow >= platformTop;

                    if (overlapX && crossedPlatform) {
                        newY = platformTop - Constants.PLAYER_HEIGHT;
                        velocityY = 0;
                        landed = true;
                        break;
                    }
                }
            }

            player.setY(newY);
            player.setVelocityY(velocityY);
            player.setOnGround(landed);

            if (player.isOnGround() && gameState.isRunning()) {
                player.setY(player.getY() + gameState.getSpeed());
            }

            if (player.isAlive() && player.getY() > Constants.WORLD_HEIGHT) {
                player.setAlive(false);
            }

            playerInput.reset();
        }
    }

    private void updatePlatforms(GameState gameState) {
        List<Platform> platforms = gameState.getPlatforms();

        for(Platform platform : platforms) {
            platform.setY(platform.getY() + gameState.getSpeed());
        }
        platforms.removeIf(p -> p.getY() > Constants.WORLD_HEIGHT+Constants.PLAYER_HEIGHT);

        spawnPlatform(gameState);

        gameState.increaseSpeed();
    }

    private void spawnPlatform(GameState gameState) {
        Platform topPlatform = getTopPlatform(gameState);
        if (topPlatform == null) return;

        if (topPlatform.getY() >= Constants.PLATFORM_MIN_GAP_Y) {
            float gapY = randomRange(Constants.PLATFORM_MIN_GAP_Y, Constants.PLATFORM_MAX_GAP_Y);
            float newY = topPlatform.getY() - gapY;

            float prevCenterX = topPlatform.getX() + topPlatform.getWidth() / 2f;
            float deltaX = randomRange(-Constants.PLATFORM_MAX_DELTA_X, Constants.PLATFORM_MAX_DELTA_X);
            float newCenterX = prevCenterX + deltaX;
            float newX = newCenterX - Constants.PLATFORM_WIDTH / 2f;
            newX = clamp(newX, 0, Constants.WORLD_WIDTH - Constants.PLATFORM_WIDTH);

            gameState.getPlatforms().add(new Platform(newX, newY, Constants.PLATFORM_WIDTH, Constants.PLATFORM_HEIGHT));
        }
    }

    private Platform getTopPlatform(GameState gameState) {
        Platform topPlatform = null;

        for (Platform platform : gameState.getPlatforms()) {
            if (topPlatform == null || platform.getY() < topPlatform.getY()) {
                topPlatform = platform;
            }
        }

        return topPlatform;
    }


    private float randomRange(float min, float max) {
        return min + random.nextFloat() * (max - min);
    }

    private float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(value, max));
    }
}
