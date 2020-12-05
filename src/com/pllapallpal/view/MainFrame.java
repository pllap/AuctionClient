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
    }

    private void initializeComponents() {
        mainPanel = (JPanel) frame.getContentPane();
        mainPanel.add(new JLabel(Data.getInstance().getNickname()), BorderLayout.CENTER);
        mainPanel.setBackground(Color.PINK.brighter());

        auctionPanel = new AuctionPanel();
        lobbyPanel = new LobbyPanel();

        mainPanel.add(auctionPanel.getPanel(), BorderLayout.CENTER);
    }

    public JFrame getFrame() {
        return frame;
    }

    public void updateUsername() {

    }

    public void setVisible(boolean b) {
        frame.setVisible(b);
    }
}
