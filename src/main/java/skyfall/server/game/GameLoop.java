package skyfall.server.game;

import skyfall.server.network.ClientHandler;
import skyfall.server.context.ServerContext;
import skyfall.server.model.Player;
import skyfall.server.storage.GameResult;
import skyfall.server.storage.GameResultStorage;
import skyfall.server.storage.SavedPlayerResult;

import java.util.List;

public class GameLoop extends Thread {

    private final GameState gameState;
    private final GameEngine gameEngine;
    private final GameResultStorage storage;

    private static final int TICK_RATE = 60;
    private static final long TICK_TIME = 1000 / TICK_RATE;

    public GameLoop(GameState gameState) {
        this.gameState = gameState;
        this.gameEngine = new GameEngine();
        this.storage = new GameResultStorage();
    }

    @Override
    public void run() {
        while (true) {
            long tickStartTime = System.currentTimeMillis();

            updateGameState();

            long tickDuration = System.currentTimeMillis() - tickStartTime;
            long sleepTime = TICK_TIME - tickDuration;

            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {}
            }
        }
    }

    private void updateGameState() {
        if (!gameState.isRunning() && ServerContext.allReady()) {
            handleGameStart();
        }

        gameEngine.update(gameState);

        handlePlayerDeaths();

        if (gameState.isRunning() && gameState.allPlayersDead()) {
            handleGameOver();
        }

        sendGameState();
    }

    private void handleGameStart() {
        gameState.startGame();

        ServerContext.resetReadyForAll();

        for (ClientHandler client : ServerContext.getClients()) {
            try {
                client.sendGameStart();
            } catch (Exception ignored) {}
        }
    }

    private void handlePlayerDeaths() {
        long now = System.currentTimeMillis();

        for (Player player : gameState.getPlayers()) {
            if (!player.isAlive() && player.getSurvivalTime() < 0) {

                float survivalTime = (now - gameState.getGameStartTime()) / 1000f;
                player.setSurvivalTime(survivalTime);

                for (ClientHandler client : ServerContext.getClients()) {
                    if (client.getPlayer() == player) {
                        try {
                            client.sendPlayerDied();
                        } catch (Exception ignored) {}
                        break;
                    }
                }
            }
        }
    }

    private void handleGameOver() {
        gameState.stopGame();
        List<Player> leaderboard = gameState.getPlayersSortedBySurvivalTime();

        storage.saveGameResult(new GameResult(java.time.LocalDateTime.now().toString(),
                        leaderboard.stream()
                        .map(p -> new SavedPlayerResult(p.getId(), p.getNickname(), p.getSurvivalTime()))
                        .toList()));

        for (ClientHandler client : ServerContext.getClients()) {
            try {
                client.sendGameOver(leaderboard);
            } catch (Exception ignored) {}
        }
    }

    private void sendGameState() {
        for (ClientHandler client : ServerContext.getClients()) {
            try {
                client.sendGameState(gameState);
            } catch (Exception ignored) {}
        }
    }
}
