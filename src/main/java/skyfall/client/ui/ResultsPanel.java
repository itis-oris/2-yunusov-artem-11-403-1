package skyfall.client.ui;

import skyfall.client.context.ClientContext;
import skyfall.client.dto.PlayerLeaderboardDTO;

import java.util.List;

import javax.swing.*;
import java.awt.*;

public class ResultsPanel extends JPanel {

    private final JPanel listPanel;

    public ResultsPanel() {
        setLayout(new BorderLayout(16, 16));
        setBackground(new Color(20, 24, 36, 190));
        setBorder(BorderFactory.createEmptyBorder(28, 36, 28, 36));

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);

        JLabel title = new JLabel("ИГРА ОКОНЧЕНА");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 24));

        JLabel subtitle = new JLabel("Результаты");
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setForeground(new Color(220, 220, 220));
        subtitle.setFont(new Font("Arial", Font.PLAIN, 16));

        headerPanel.add(title);
        headerPanel.add(Box.createVerticalStrut(6));
        headerPanel.add(subtitle);

        add(headerPanel, BorderLayout.NORTH);

        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(false);

        add(listPanel, BorderLayout.CENTER);
    }

    public void refresh() {
        listPanel.removeAll();

        List<PlayerLeaderboardDTO> players = ClientContext.getGameState().getLeaderboardPlayers();

        for (int i = 0; i < players.size(); i++) {
            PlayerLeaderboardDTO player = players.get(i);

            JLabel row = new JLabel((i + 1) + ". " + player.getNickname() +"(" + player.getId() + ")" + " — " + String.format("%.2f", player.getSurvivalTime()) + " сек");

            row.setForeground(Color.WHITE);
            row.setFont(new Font("Arial", Font.PLAIN, 15));
            row.setBorder(BorderFactory.createEmptyBorder(6, 0, 6, 0));

            listPanel.add(row);
        }

        revalidate();
        repaint();
    }
}

