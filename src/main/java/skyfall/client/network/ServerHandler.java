package skyfall.client.network;

import skyfall.client.context.ClientContext;
import skyfall.client.dto.PlatformDTO;
import skyfall.client.dto.PlayerDTO;
import skyfall.client.dto.PlayerLeaderboardDTO;
import skyfall.client.game.ClientGameState;
import skyfall.common.protocol.MessageType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ServerHandler extends Thread {

    private final Socket socket;
    private final DataInputStream dis;
    private final DataOutputStream dos;

    public ServerHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.dis = new DataInputStream(socket.getInputStream());
        this.dos = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        try {
            while (true) {
                MessageType type = MessageType.fromByte(dis.readByte());

                switch (type) {
                    case PLAYER_ASSIGNED -> handlePlayerAssigned();
                    case GAME_START -> handleGameStart();
                    case GAME_STATE -> handleGameState();
                    case PLAYER_DIED -> handlePlayerDied();
                    case GAME_OVER -> handleGameOver();
                    default -> System.out.println("Unknown server message: " + type);
                }
            }
        } catch (IOException e) {
            System.out.println("Disconnected from server");
        }
    }

    //handlers

    private void handlePlayerAssigned() throws IOException {
        int playerId = dis.readInt();
        ClientContext.setLocalPlayerId(playerId);
    }

    private void handleGameStart() throws IOException {
        ClientContext.getGameState().setRunning(true);
    }

    private void handleGameState() throws IOException {
        ClientGameState gameState = ClientContext.getGameState();
        Set<Integer> aliveIds = new HashSet<>();

        int playersCount = dis.readInt();
        for (int i = 0; i < playersCount; i++) {
            int id = dis.readInt();
            aliveIds.add(id);

            int nicknameLength = dis.readInt();
            byte[] nicknameBytes = dis.readNBytes(nicknameLength);
            String nickname = new String(nicknameBytes, StandardCharsets.UTF_8);

            float x = dis.readFloat();
            float y = dis.readFloat();

            boolean alive = dis.readBoolean();

            PlayerDTO player = gameState.getPlayer(id);
            if (player == null) {
                player = new PlayerDTO(id, nickname, x, y, alive);
                gameState.getPlayers().put(id, player);
            } else {
                player.updatePosition(x, y);
                player.setAlive(alive);
            }
        }
        gameState.getPlayers().keySet().removeIf(id -> !aliveIds.contains(id));

        List<PlatformDTO> platforms = new ArrayList<>();

        int platformsCount = dis.readInt();
        for (int i = 0; i < platformsCount; i++) {
            float x = dis.readFloat();
            float y = dis.readFloat();
            float width = dis.readFloat();
            float height = dis.readFloat();
            platforms.add(new PlatformDTO(x, y, width, height));
        }

        gameState.setPlatforms(platforms);
    }

    private void handlePlayerDied() throws IOException {
        ClientContext.getGameState().setLocalPlayerDead(true);
    }

    private void handleGameOver() throws IOException {
        List<PlayerLeaderboardDTO> players = new ArrayList<>();

        int count = dis.readInt();

        for (int i = 0; i < count; i++) {
            int id = dis.readInt();

            int nicknameLength = dis.readInt();
            byte[] nicknameBytes = dis.readNBytes(nicknameLength);
            String nickname = new String(nicknameBytes, StandardCharsets.UTF_8);

            float survivalTime = dis.readFloat();

            players.add(new PlayerLeaderboardDTO(id, nickname, survivalTime));
        }
        ClientContext.getGameState().setLeaderboardPlayers(players);
        ClientContext.getGameState().setRunning(false);
    }

    //senders

    public void sendPlayerConnect(String nickname) throws IOException {
        byte[] nicknameBytes = nickname.getBytes(StandardCharsets.UTF_8);

        dos.writeByte(MessageType.PLAYER_CONNECT.code);
        dos.writeInt(nicknameBytes.length);
        dos.write(nicknameBytes);

        dos.flush();
    }

    public void sendPlayerReady() throws IOException {
        dos.writeByte(MessageType.PLAYER_READY.code);
        dos.flush();
    }

    public void sendPlayerInput(boolean left, boolean right, boolean jump) throws IOException {
        dos.writeByte(MessageType.PLAYER_INPUT.code);

        dos.writeBoolean(left);
        dos.writeBoolean(right);
        dos.writeBoolean(jump);

        dos.flush();
    }
}