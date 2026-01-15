package skyfall.client2.ui;

import skyfall.client2.context.ClientContext;
import skyfall.client2.dto.PlatformDTO;
import skyfall.client2.dto.PlayerDTO;
import skyfall.client2.game.ClientGameState;
import skyfall.client2.network.ServerHandler;
import skyfall.common.constants.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class GameComponent extends JComponent {

    private final ClientGameState gameState = ClientContext.getGameState();
    private final ServerHandler serverHandler = ClientContext.getServerHandler();
    private final PlayerAnimator animator = new PlayerAnimator();

    private NicknamePanel nicknamePanel;
    private ReadyButton readyButton;
    private ResultsPanel resultsPanel;

    private Image platformImage;
    private Image backgroundImage;

    private boolean leftPressed;
    private boolean rightPressed;
    private boolean jumpPressed;

    public GameComponent() {
        setLayout(null);
        setFocusable(true);
        setDoubleBuffered(true);
        setPreferredSize(new Dimension((int) Constants.WORLD_WIDTH, (int) Constants.WORLD_HEIGHT));

        loadImages();

        initNicknameComponent();
        initReadyButton();
        initResultsPanel();
        initKeyBindings();
        startLoop();
    }

    private void startLoop() {
        Timer timer = new Timer(16, e -> {
            animator.update();
            nicknamePanel.setVisible(!nicknamePanel.isNicknameSubmitted());
            readyButton.setVisible(nicknamePanel.isNicknameSubmitted() && !gameState.isRunning() && !resultsPanel.isVisible());
            if (!gameState.isRunning() && !gameState.getLeaderboardPlayers().isEmpty() && !resultsPanel.isVisible()) {
                resultsPanel.refresh();
                resultsPanel.setVisible(true);
            }
            sendInput();
            repaint();
        });
        timer.start();
    }

    private void sendInput() {
        try {
            serverHandler.sendPlayerInput(leftPressed, rightPressed, jumpPressed);
        } catch (IOException ignored) {}
    }

    private void initNicknameComponent() {
        nicknamePanel = new NicknamePanel();

        int width = 300;
        int height = 160;

        nicknamePanel.setBounds((int) ((Constants.WORLD_WIDTH - width) / 2), (int) ((Constants.WORLD_HEIGHT - height) / 4), width, height);

        add(nicknamePanel);
    }

    private void initReadyButton() {
        readyButton = new ReadyButton();

        int width = 120;
        int height = 40;

        readyButton.setBounds((int) (Constants.WORLD_WIDTH - width - 24), (int) (Constants.WORLD_HEIGHT - height - 24), width, height);

        readyButton.setVisible(false);
        add(readyButton);
    }

    private void loadImages() {
        platformImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/assets/platform.png"))).getImage();
        backgroundImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/assets/bg.png"))).getImage();
    }

    private void initKeyBindings() {
        InputMap im = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getActionMap();

        im.put(KeyStroke.getKeyStroke("pressed LEFT"), "leftPressed");
        im.put(KeyStroke.getKeyStroke("released LEFT"), "leftReleased");
        im.put(KeyStroke.getKeyStroke("pressed RIGHT"), "rightPressed");
        im.put(KeyStroke.getKeyStroke("released RIGHT"), "rightReleased");
        im.put(KeyStroke.getKeyStroke("pressed UP"), "jumpPressed");
        im.put(KeyStroke.getKeyStroke("released UP"), "jumpReleased");

        am.put("leftPressed", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                leftPressed = true;
            }
        });
        am.put("leftReleased", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                leftPressed = false;
            }
        });
        am.put("rightPressed", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                rightPressed = true;
            }
        });
        am.put("rightReleased", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                rightPressed = false;
            }
        });
        am.put("jumpPressed", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                jumpPressed = true;
            }
        });
        am.put("jumpReleased", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                jumpPressed = false;
            }
        });
    }

    private void initResultsPanel() {
        resultsPanel = new ResultsPanel();

        int width = 400;
        int height = 300;

        resultsPanel.setBounds((int) ((Constants.WORLD_WIDTH - width) / 2), (int) ((Constants.WORLD_HEIGHT - height) / 2), width, height);

        resultsPanel.setVisible(false);
        add(resultsPanel);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawBackground(g2d);
        drawPlatforms(g2d);
        drawPlayers(g2d);
        if (gameState.isLocalPlayerDead() && gameState.isRunning()) {
            drawDeath(g2d);
        }
    }

    private void drawBackground(Graphics2D g2d) {
        if (backgroundImage == null) return;
        g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
    }

    private void drawPlatforms(Graphics2D g2d) {
        for (PlatformDTO platform : gameState.getPlatforms()) {
            if (platformImage == null || platform.getWidth() == Constants.WORLD_WIDTH) {
                int x = (int) platform.getX();
                int y = (int) platform.getY();
                int w = (int) platform.getWidth();
                int h = (int) platform.getHeight();

                g2d.setColor(new Color(69, 57, 53));
                g2d.fillRect(x, y, w, h);

                int grassHeight = Math.min(12, h);
                g2d.setColor(new Color(133, 149, 70));
                g2d.fillRect(x, y, w, grassHeight);
            } else {
                g2d.drawImage(platformImage, (int) platform.getX(), (int) platform.getY(), (int) platform.getWidth(), (int) platform.getHeight(),null);
            }
        }
    }

    private void drawPlayers(Graphics2D g2d) {
        for (PlayerDTO player : gameState.getPlayers().values()) {
            if (!player.isAlive()) continue;

            BufferedImage frame = animator.getFrame(player);

            int drawX = (int) player.getX() - 20;
            int drawY = (int) player.getY() - 10;

            g2d.drawImage(frame, drawX, drawY, 64, 64, null);

            drawNickname(g2d, player);
        }
    }

    private void drawDeath(Graphics2D g2d) {

        g2d.setColor(new Color(200, 80, 80));
        g2d.setFont(new Font("Arial", Font.BOLD, 40));

        String text = "ВЫ ПОГИБЛИ";

        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(text);

        int x = (int) (Constants.WORLD_WIDTH / 2 - textWidth / 2);
        int y = (int) (Constants.WORLD_HEIGHT / 2);

        g2d.drawString(text, x, y);

    }

    private void drawNickname(Graphics2D g2d, PlayerDTO player) {
        String nickname = player.getNickname();

        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(nickname);

        int centerX = (int) (player.getX() + Constants.PLAYER_WIDTH / 2);
        int hitboxTopY = (int) player.getY();

        int textX = centerX - textWidth / 2;
        int textY = hitboxTopY - 8;

        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.drawString(nickname, textX + 1, textY + 1);

        g2d.setColor(Color.WHITE);
        g2d.drawString(nickname, textX, textY);
    }
}
