package skyfall.server.storage;

import java.util.List;

public class GameResult {
    private String gameDate;
    private List<SavedPlayerResult> players;

    public GameResult() {}

    public GameResult(String gameDate, List<SavedPlayerResult> players) {
        this.gameDate = gameDate;
        this.players = players;
    }

    public String getGameDate() {
        return gameDate;
    }
    public void setGameDate(String gameDate) {
        this.gameDate = gameDate;
    }

    public List<SavedPlayerResult> getPlayers() {
        return players;
    }
    public void setPlayers(List<SavedPlayerResult> players) {
        this.players = players;
    }
}
