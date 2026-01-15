package skyfall.client2.ui;

import skyfall.client2.context.ClientContext;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class NicknamePanel extends JPanel {

    private boolean nicknameSubmitted = false;

    private final JTextField textField;
    private final JButton connectButton;

    public NicknamePanel() {
        setLayout(new BorderLayout(12, 12));
        setBackground(new Color(0, 0, 0, 90));
        setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        JLabel label = new JLabel("Введите имя:", SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 18));

        textField  = new JTextField();
        textField.setFont(new Font("Arial", Font.PLAIN, 16));
        textField.setHorizontalAlignment(JTextField.CENTER);

        connectButton = new JButton("ПОДКЛЮЧИТЬСЯ");
        connectButton.setFont(new Font("Arial", Font.BOLD, 14));
        connectButton.setBackground(new Color(70, 160, 90));
        connectButton.setForeground(Color.WHITE);
        connectButton.setOpaque(true);
        connectButton.setFocusPainted(false);

        connectButton.addActionListener(e -> submitNickname());

        add(label, BorderLayout.NORTH);
        add(textField, BorderLayout.CENTER);
        add(connectButton, BorderLayout.SOUTH);
    }

    private void submitNickname() {
        String nickname = textField.getText().trim();

        if (nickname.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Имя не может быть пустым", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        nicknameSubmitted = true;

        connectButton.setEnabled(false);
        textField.setEnabled(false);

        try {
            ClientContext.getServerHandler().sendPlayerConnect(nickname);
        }  catch (IOException ignored) {}
    }

    public boolean isNicknameSubmitted() {
        return nicknameSubmitted;
    }
}