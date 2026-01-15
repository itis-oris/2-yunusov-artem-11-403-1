package skyfall.server;

import skyfall.server.context.ServerContext;
import skyfall.server.game.GameLoop;
import skyfall.server.game.GameState;
import skyfall.server.network.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(50000);

        GameState gameState = new GameState();
        gameState.initWorld();

        ServerContext.init(gameState);

        new GameLoop(gameState).start();

        while (true) {
            Socket socket = serverSocket.accept();
            new ClientHandler(socket).start();
        }
    }
}