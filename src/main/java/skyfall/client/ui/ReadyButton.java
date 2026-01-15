package skyfall.client.ui;

import skyfall.client.context.ClientContext;
import skyfall.client.network.ServerHandler;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ReadyButton extends JButton {

    public ReadyButton() {
        super("READY");

        setFont(new Font("Arial", Font.BOLD, 16));
        setForeground(Color.WHITE);
        setBackground(new Color(70, 160, 90));
        setOpaque(true);
        setFocusPainted(false);

        addActionListener(e -> {
            try {
                ClientContext.getServerHandler().sendPlayerReady();
                setEnabled(false);
            }  catch (IOException ignored) {}
        });
    }
}