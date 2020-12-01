package com.pllapallpal.view;

import com.pllapallpal.Client;
import com.pllapallpal.Data;

import javax.swing.*;
import java.awt.*;

public class LoginFrame {

    private final JFrame frame;
    private final MainFrame mainFrame;

    public LoginFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        frame = new JFrame("Auction Login");
        frame.setSize(600, 800);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initializeComponents();
    }

    private void initializeComponents() {

        JPanel mainPanel = (JPanel) frame.getContentPane();
        JPanel titlePanel = new JPanel(new BorderLayout());
        JPanel inputPanel = new JPanel(new BorderLayout());

        titlePanel.setBackground(Color.PINK.brighter());
        inputPanel.setBackground(Color.PINK.brighter());
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 80, 20, 80));

        JLabel title = new JLabel("<html>Auction<br>Simulator</html>");
        title.setFont(new Font("Segoe", Font.PLAIN, 100));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        titlePanel.add(title, BorderLayout.CENTER);

        JLabel label = new JLabel("Enter username: ");
        JTextField textField = new JTextField();
        JButton button = new JButton("Login");
        button.addActionListener(e -> {
            String username = textField.getText();
            Data.getInstance().setNickname(username);
            Client.getInstance().send("LOGIN " + username);
            mainFrame.updateUsername();
            mainFrame.setVisible(true);
            this.setVisible(false);
        });
        inputPanel.add(label, BorderLayout.WEST);
        inputPanel.add(textField, BorderLayout.CENTER);
        inputPanel.add(button, BorderLayout.EAST);

        mainPanel.add(titlePanel, BorderLayout.CENTER);
        mainPanel.add(inputPanel, BorderLayout.SOUTH);
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setVisible(boolean b) {
        frame.setVisible(b);
    }
}
