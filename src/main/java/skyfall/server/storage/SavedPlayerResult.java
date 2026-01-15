package skyfall.server.storage;

public class SavedPlayerResult {
    private int id;
    private String nickname;
    private float survivalTime;

    public SavedPlayerResult() {}

    public SavedPlayerResult(int id, String nickname, float survivalTime) {
        this.id = id;
        this.nickname = nickname;
        this.survivalTime = survivalTime;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public float getSurvivalTime() {
        return survivalTime;
    }
    public void setSurvivalTime(float survivalTime) {
        this.survivalTime = survivalTime;
    }
}
