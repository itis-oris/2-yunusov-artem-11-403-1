package skyfall.client;

import skyfall.client.context.ClientContext;
import skyfall.client.game.ClientGameState;
import skyfall.client.network.ServerHandler;
import skyfall.client.ui.GameFrame;

import javax.swing.*;
import java.io.IOException;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 50000);

        ClientGameState clientGameState = new ClientGameState();
        ServerHandler serverHandler = new ServerHandler(socket);
        ClientContext.init(clientGameState, serverHandler);

        serverHandler.start();

        SwingUtilities.invokeLater(() ->
                new GameFrame()
        );
    }

}
