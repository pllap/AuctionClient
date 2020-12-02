package com.pllapallpal.view;

import com.pllapallpal.Data;

import javax.swing.*;
import java.awt.*;

public class MainFrame {

    private final JFrame frame;
    private JPanel mainPanel;
    private AuctionPanel auctionPanel;
    private LobbyPanel lobbyPanel;

    public MainFrame() {
        frame = new JFrame();
        frame.setSize(800, 600);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        LoginFrame loginFrame = new LoginFrame(this);
        loginFrame.setVisible(true);
        initializeComponents();

        auctionPanel = new AuctionPanel();
        lobbyPanel = new LobbyPanel();
    }

    private void initializeComponents() {
        mainPanel = (JPanel) frame.getContentPane();
        mainPanel.add(new JLabel(Data.getInstance().getNickname()), BorderLayout.CENTER);
        mainPanel.setBackground(Color.PINK.brighter());
    }

    public JFrame getFrame() {
        return frame;
    }

    public void updateUsername() {
        JLabel temp = new JLabel(Data.getInstance().getNickname());
        temp.setFont(new Font("Segoe", Font.PLAIN, 100));
        mainPanel.add(temp, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public void setVisible(boolean b) {
        frame.setVisible(b);
    }
}
