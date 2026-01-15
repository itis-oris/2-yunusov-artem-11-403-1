package skyfall.common.protocol;

public enum MessageType {
    PLAYER_CONNECT((byte) 0),
    PLAYER_ASSIGNED((byte) 1),
    PLAYER_READY((byte) 2),
    GAME_START((byte) 3),
    PLAYER_INPUT((byte) 4),
    GAME_STATE((byte) 5),
    PLAYER_DIED((byte) 6),
    GAME_OVER((byte) 7);

    public final byte code;

    MessageType(byte code) {
        this.code = code;
    }

    public static MessageType fromByte(byte b) {
        for (MessageType t : values()) {
            if (t.code == b) return t;
        }
        throw new IllegalArgumentException("Unknown message type: " + b);
    }
}
