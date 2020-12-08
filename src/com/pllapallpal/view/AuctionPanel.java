package com.pllapallpal.view;

import com.pllapallpal.Auction;

import javax.swing.*;
import java.awt.*;

public class AuctionPanel {

    private JPanel panel;
    private MainFrame mainFrame;

    private Auction auction;

    public AuctionPanel(MainFrame mainFrame) {

        this.mainFrame = mainFrame;
    }

    public void setAuction(Auction auction) {
        this.auction = auction;

        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel itemInfoPanel = new JPanel();
        itemInfoPanel.setLayout(new BorderLayout());
        itemInfoPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JLabel itemImageLabel = new JLabel();
        itemImageLabel.setIcon(new ImageIcon(auction.getItemImage().getScaledInstance(128, 128, Image.SCALE_DEFAULT)));
        itemInfoPanel.add(itemImageLabel, BorderLayout.WEST);
        panel.add(itemInfoPanel, BorderLayout.CENTER);
    }

    public JPanel getPanel() {
        return panel;
    }
}
