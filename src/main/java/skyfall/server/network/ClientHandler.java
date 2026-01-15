package skyfall.server.network;

import skyfall.common.protocol.MessageType;
import skyfall.server.context.ServerContext;
import skyfall.server.game.GameState;
import skyfall.server.model.Platform;
import skyfall.server.model.Player;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ClientHandler extends Thread {

    private final Socket socket;
    private final DataInputStream dis;
    private final DataOutputStream dos;

    private boolean isReady = false;
    private Player player;

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.dis = new DataInputStream(socket.getInputStream());
        this.dos = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        ServerContext.addClient(this);
        try {
            while (true) {
                byte type = dis.readByte();
                MessageType messageType = MessageType.fromByte(type);

                switch (messageType) {
                    case PLAYER_CONNECT -> handlePlayerConnect();
                    case PLAYER_READY -> handlePlayerReady();
                    case PLAYER_INPUT -> handlePlayerInput();
                    default -> System.out.println("Unexpected message from client: " + messageType);
                }
            }
        } catch (IOException e) {
            System.out.println("Client disconnected: " + player.getNickname());
        } finally {
            ServerContext.removeClient(this);
            if (player != null) {
                ServerContext.getGameState().removePlayer(player);
            }
        }
    }

    //handlers

    private void handlePlayerConnect() throws IOException {
        int length = dis.readInt();
        byte[] nicknameBytes = dis.readNBytes(length);
        String nickname = new String(nicknameBytes, StandardCharsets.UTF_8);

        GameState gameState = ServerContext.getGameState();
        this.player = gameState.addPlayer(nickname);

        sendPlayerAssigned();
    }

    private void handlePlayerReady() throws IOException {
        setReady(true);
    }

    private void handlePlayerInput() throws IOException {
        boolean left = dis.readBoolean();
        boolean right = dis.readBoolean();
        boolean jump = dis.readBoolean();

        if (player != null) {
            player.getPlayerInput().set(left, right, jump);
        }
    }

    //Senders

    public synchronized void sendPlayerAssigned() throws IOException {
        dos.writeByte(MessageType.PLAYER_ASSIGNED.code);
        dos.writeInt(player.getId());
        dos.flush();
    }

    public synchronized void sendGameStart() throws IOException {
        dos.writeByte(MessageType.GAME_START.code);
        dos.flush();
    }

    public synchronized void sendGameState(GameState gameState) throws IOException {
        dos.writeByte(MessageType.GAME_STATE.code);

        dos.writeInt(gameState.getPlayers().size());
        for (Player player : gameState.getPlayers()) {
            dos.writeInt(player.getId());
            byte[] nicknameBytes = player.getNickname().getBytes(StandardCharsets.UTF_8);
            dos.writeInt(nicknameBytes.length);
            dos.write(nicknameBytes);
            dos.writeFloat(player.getX());
            dos.writeFloat(player.getY());
            dos.writeBoolean(player.isAlive());
        }

        dos.writeInt(gameState.getPlatforms().size());
        for (Platform platform : gameState.getPlatforms()) {
            dos.writeFloat(platform.getX());
            dos.writeFloat(platform.getY());
            dos.writeFloat(platform.getWidth());
            dos.writeFloat(platform.getHeight());

        }

        dos.flush();
    }

    public synchronized void sendPlayerDied() throws IOException {
        dos.writeByte(MessageType.PLAYER_DIED.code);
        dos.flush();
    }

    public synchronized void sendGameOver(List<Player> players) throws IOException {
        dos.writeByte(MessageType.GAME_OVER.code);

        dos.writeInt(players.size());

        for (Player player : players) {
            dos.writeInt(player.getId());
            byte[] nicknameBytes = player.getNickname().getBytes(StandardCharsets.UTF_8);
            dos.writeInt(nicknameBytes.length);
            dos.write(nicknameBytes);
            dos.writeFloat(player.getSurvivalTime());
        }

        dos.flush();
    }

    public boolean isReady() {
        return isReady;
    }
    public void setReady(boolean ready) {
        isReady = ready;
    }

    public Player getPlayer() {
        return player;
    }
}
