package com.pllapallpal.view;

import com.pllapallpal.Auction;
import com.pllapallpal.Data;
import com.pllapallpal.SelectorThread;

import javax.swing.*;
import java.awt.*;

public class MainFrame {

    private final JFrame frame;
    private JPanel mainPanel;
    private AuctionPanel auctionPanel;

    public MainFrame() {
        frame = new JFrame("Auction");
        frame.setSize(800, 600);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        LoginFrame loginFrame = new LoginFrame(this);
        loginFrame.setVisible(true);
        initializeComponents();

        SelectorThread.addOnEnterAuction(this::enterAuction);
    }

    private void initializeComponents() {
        mainPanel = (JPanel) frame.getContentPane();
        mainPanel.add(new JLabel(Data.getInstance().getNickname()), BorderLayout.CENTER);
        mainPanel.setBackground(Color.PINK.brighter());

        auctionPanel = new AuctionPanel(this);

        mainPanel.add(new LobbyPanel(this).getPanel(), BorderLayout.CENTER);
    }

    public JFrame getFrame() {
        return frame;
    }

    public void updateUsername() {

    }

    public void enterAuction(Auction auction) {
        auctionPanel.setAuction(auction);
        mainPanel.removeAll();
        mainPanel.add(auctionPanel.getPanel(), BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public void setVisible(boolean b) {
        frame.setVisible(b);
    }
}
