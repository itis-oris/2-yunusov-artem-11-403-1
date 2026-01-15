package skyfall.server.game;

import skyfall.common.constants.Constants;
import skyfall.server.model.Platform;
import skyfall.server.model.Player;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.min;

public class GameState {
    private final List<Player> players =  new ArrayList<>();
    private final List<Platform> platforms = new ArrayList<>();

    private float speed;
    private boolean running = false;
    private long gameStartTime = -1;
    private int nextPlayerId = 1;

    public synchronized List<Player> getPlayers() {
        return new ArrayList<>(players);
    }
    public List<Platform> getPlatforms() {
        return platforms;
    }

    public float getSpeed() {
        return speed;
    }
    public void increaseSpeed() {
        speed = min(speed + Constants.SPEED_INCREASE, Constants.SPEED_MAX);
    }

    public boolean isRunning() {
        return running;
    }
    public void startGame() {
        running = true;
        gameStartTime = System.currentTimeMillis();
        speed = Constants.SPEED_START;
    }
    public void stopGame() {
        running =  false;
        speed = Constants.SPEED_STILL;
    }

    public long getGameStartTime() {
        return gameStartTime;
    }
    public void setGameStartTime(long gameStartTime) {
        this.gameStartTime = gameStartTime;
    }

    public void initWorld() {
        platforms.clear();
        players.clear();

        speed = Constants.SPEED_STILL;
        running = false;

        platforms.add(new Platform(Constants.START_PLATFORM_X, Constants.START_PLATFORM_Y, Constants.START_PLATFORM_WIDTH, Constants.START_PLATFORM_HEIGHT));
    }

    public synchronized Player addPlayer(String nickname) {
        int playerId = nextPlayerId++;

        Player player = new Player(playerId, nickname, Constants.PLAYER_START_X, Constants.PLAYER_START_Y);
        players.add(player);
        return player;
    }

    public synchronized void removePlayer(Player player) {
        players.remove(player);
    }

    public boolean allPlayersDead() {
        return !players.isEmpty() && players.stream().noneMatch(Player::isAlive);
    }
    public synchronized List<Player> getPlayersSortedBySurvivalTime() {
        List<Player> result = new ArrayList<>(players);

        result.sort((a, b) ->
                Float.compare(b.getSurvivalTime(), a.getSurvivalTime())
        );

        return result;
    }
}
