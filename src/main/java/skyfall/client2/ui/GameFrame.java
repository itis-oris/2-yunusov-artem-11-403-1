package skyfall.client2.ui;

import javax.swing.*;

public class GameFrame extends JFrame {

    public GameFrame() {
        super("SkyFall");

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        GameComponent gameComponent = new GameComponent();
        add(gameComponent);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}