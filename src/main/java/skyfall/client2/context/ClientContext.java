package skyfall.client2.context;

import skyfall.client2.game.ClientGameState;
import skyfall.client2.network.ServerHandler;

public class ClientContext {
    private static volatile ServerHandler serverHandler;
    private static volatile ClientGameState clientGameState;
    private static volatile int localPlayerId = -1;

    public static void init(ClientGameState state, ServerHandler handler) {
        serverHandler = handler;
        clientGameState = state;
    }

    public static ClientGameState getGameState() {
        return clientGameState;
    }
    public static ServerHandler getServerHandler() {
        return serverHandler;
    }

    public static int getLocalPlayerId() {
        return localPlayerId;
    }
    public static void setLocalPlayerId(int localPlayerId) {
        ClientContext.localPlayerId = localPlayerId;
    }
}
