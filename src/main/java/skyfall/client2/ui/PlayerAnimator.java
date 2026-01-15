package skyfall.client2.ui;

import skyfall.client2.dto.PlayerDTO;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class PlayerAnimator {
    private static final long FRAME_TIME_MS = 125;
    private static final int DIFFERRENT_SKINS = 2;

    private final Map<Integer, PlayerSprites> spritesByPlayer = new HashMap<>();

    private long lastFrameTime = System.currentTimeMillis();
    private int frameIndex = 0;

    public void update() {
        long now = System.currentTimeMillis();
        if (now - lastFrameTime > FRAME_TIME_MS) {
            frameIndex++;
            lastFrameTime = now;
        }
    }

    public BufferedImage getFrame(PlayerDTO player) {
        PlayerSprites sprites = spritesByPlayer.get(player.getId());

        if (sprites == null) {
            int skinId = player.getId() % DIFFERRENT_SKINS;
            if (skinId == 0) {skinId = DIFFERRENT_SKINS;}
            String path = "/assets/player" + skinId + ".png";
            sprites = new PlayerSprites(path, 32, 32);
            spritesByPlayer.put(player.getId(), sprites);
        }

        BufferedImage[] anim;

        if (player.getX() != player.getPrevX()) {
            anim = player.isFacingRight()
                    ? sprites.getRunRight()
                    : sprites.getRunLeft();
        } else {
            anim = player.isFacingRight()
                    ? sprites.getIdleRight()
                    : sprites.getIdleLeft();
        }

        return anim[frameIndex % anim.length];
    }


}
