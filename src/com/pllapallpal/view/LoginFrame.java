package com.pllapallpal.view;

import com.pllapallpal.Client;
import com.pllapallpal.Data;
import com.pllapallpal.Protocol;

import javax.swing.*;
import java.awt.*;
import java.nio.ByteBuffer;

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
        textField.addActionListener(e -> {
            String username = textField.getText();
            login(username);
            setUsername(username);
            mainFrame.setVisible(true);
            this.setVisible(false);
        });
        button.addActionListener(e -> {
            String username = textField.getText();
            login(username);
            setUsername(username);
            mainFrame.setVisible(true);
            this.setVisible(false);
        });
        inputPanel.add(label, BorderLayout.WEST);
        inputPanel.add(textField, BorderLayout.CENTER);
        inputPanel.add(button, BorderLayout.EAST);

        mainPanel.add(titlePanel, BorderLayout.CENTER);
        mainPanel.add(inputPanel, BorderLayout.SOUTH);
    }

    private void login(String username) {
        ByteBuffer capacityBuffer = ByteBuffer.allocate(Integer.BYTES);
        int bufferSize = Integer.BYTES + username.getBytes().length;
        capacityBuffer.putInt(bufferSize);
        capacityBuffer.flip();
        Client.getInstance().send(capacityBuffer);

        ByteBuffer byteBuffer = ByteBuffer.allocate(bufferSize);
        byteBuffer.putInt(Protocol.LOGIN);
        byteBuffer.put(username.getBytes());
        byteBuffer.flip();
        Client.getInstance().send(byteBuffer);
    }

    private void setUsername(String username) {
        Data.getInstance().setNickname(username);
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setVisible(boolean b) {
        frame.setVisible(b);
    }
}
