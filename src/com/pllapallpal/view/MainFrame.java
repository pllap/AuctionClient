package com.pllapallpal.view;

import com.pllapallpal.*;

import javax.swing.*;
import java.awt.*;

public class MainFrame {

    private final JFrame frame;
    private JPanel mainPanel;
    private LobbyPanel lobbyPanel;
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
        SelectorThread.addOnQuitAuction(this::quitAuction);
    }

    private void initializeComponents() {
        mainPanel = (JPanel) frame.getContentPane();
        mainPanel.add(new JLabel(Data.getInstance().getNickname()), BorderLayout.CENTER);
        mainPanel.setBackground(Color.PINK.brighter());

        lobbyPanel = new LobbyPanel(this);
        auctionPanel = new AuctionPanel(this);

        mainPanel.add(lobbyPanel.getPanel(), BorderLayout.CENTER);
    }

    public JFrame getFrame() {
        return frame;
    }

    public void backToAuctionList() {
        mainPanel.removeAll();
        mainPanel.add(lobbyPanel.getPanel(), BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public void enterAuction(Auction auction) {
        auctionPanel.setAuction(auction);
        auction.enter();
        mainPanel.removeAll();
        mainPanel.add(auctionPanel.getPanel(), BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public void quitAuction(Auction auction) {
        auction.exit();
        Data.getInstance().getAuctionList().remove(auction);
        backToAuctionList();
    }

    public void setVisible(boolean b) {
        frame.setVisible(b);
    }
}
