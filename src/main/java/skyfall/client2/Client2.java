package skyfall.client2;

import skyfall.client2.context.ClientContext;
import skyfall.client2.game.ClientGameState;
import skyfall.client2.network.ServerHandler;
import skyfall.client2.ui.GameFrame;

import javax.swing.*;
import java.io.IOException;
import java.net.Socket;

public class Client2 {
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
