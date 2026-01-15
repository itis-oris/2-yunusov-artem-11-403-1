package skyfall.server.context;

import skyfall.server.network.ClientHandler;
import skyfall.server.game.GameState;

import java.util.ArrayList;
import java.util.List;

public class ServerContext {
    private static final List<ClientHandler> clients = new ArrayList<>();
    private static volatile GameState gameState;

    public static void init(GameState state) {
        gameState = state;
    }

    public static synchronized List<ClientHandler> getClients() {
        return new ArrayList<>(clients);
    }

    public static synchronized void addClient(ClientHandler client) {
        clients.add(client);
    }

    public static synchronized void removeClient(ClientHandler client) {
        clients.remove(client);
    }

    public static synchronized boolean allReady() {
        return !clients.isEmpty() && clients.stream().allMatch(ClientHandler::isReady);
    }

    public static synchronized void resetReadyForAll() {
        for (ClientHandler client : clients) {
            client.setReady(false);
        }
    }

    public static GameState getGameState() {
        return gameState;
    }
}
