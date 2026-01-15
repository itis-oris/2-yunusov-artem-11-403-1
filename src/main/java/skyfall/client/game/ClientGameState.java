package skyfall.client.game;

import skyfall.client.dto.PlatformDTO;
import skyfall.client.dto.PlayerDTO;
import skyfall.client.dto.PlayerLeaderboardDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientGameState {
    private final Map<Integer, PlayerDTO> players = new HashMap<>();
    private volatile List<PlatformDTO> platforms = new ArrayList<>();
    private volatile List<PlayerLeaderboardDTO>  leaderboardPlayers = new ArrayList<>();

    private boolean running = false;
    private boolean localPlayerDead = false;

    public Map<Integer, PlayerDTO> getPlayers() {
        return players;
    }
    public PlayerDTO getPlayer(int id) {
        return players.get(id);
    }
    public void removePlayer(int id) {
        players.remove(id);
    }

    public List<PlatformDTO> getPlatforms() {
        return platforms;
    }
    public void setPlatforms(List<PlatformDTO> platforms) {this.platforms = platforms;}

    public List<PlayerLeaderboardDTO> getLeaderboardPlayers() {
        return leaderboardPlayers;
    }
    public void setLeaderboardPlayers(List<PlayerLeaderboardDTO> leaderboardPlayers) {
        this.leaderboardPlayers = leaderboardPlayers;
    }

    public boolean isRunning() {
        return running;
    }
    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isLocalPlayerDead() {
        return localPlayerDead;
    }
    public void setLocalPlayerDead(boolean localPlayerDead) {
        this.localPlayerDead = localPlayerDead;
    }
}
