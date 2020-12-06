package com.pllapallpal.view;

import com.pllapallpal.Auction;
import com.pllapallpal.SelectorThread;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LobbyPanel {

    private JPanel panel;
    private JPanel auctionListPanel;
    private JPanel itemPanel;
    private JList<String> userJList;

    public LobbyPanel() {
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.PINK.brighter());

        itemPanel = new JPanel();
        itemPanel.setLayout(new BorderLayout());
        JLabel itemImage = new JLabel();
        ImageIcon imageIcon = new ImageIcon("itemImage.png");
        itemImage.setIcon(imageIcon);
        itemPanel.add(itemImage, BorderLayout.NORTH);
        JTextArea itemInfo = new JTextArea();
        JScrollPane itemInfoScrollPane = new JScrollPane(itemInfo, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        itemInfoScrollPane.setSize(new Dimension(5, 40));
        itemPanel.add(itemInfoScrollPane, BorderLayout.CENTER);
        panel.add(itemPanel, BorderLayout.WEST);

        auctionListPanel = new JPanel();
        auctionListPanel.setLayout(new BoxLayout(auctionListPanel, BoxLayout.Y_AXIS));
        Dimension auctionListPanelPreferredSize = auctionListPanel.getPreferredSize();
        auctionListPanelPreferredSize.width = 600;
        auctionListPanel.setPreferredSize(auctionListPanelPreferredSize);
        panel.add(auctionListPanel, BorderLayout.CENTER);

        userJList = new JList<>();
        Dimension userJListPreferredSize = userJList.getPreferredSize();
        userJListPreferredSize.width = 200;
        userJList.setPreferredSize(userJListPreferredSize);
        userJList.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        panel.add(userJList, BorderLayout.EAST);

        SelectorThread.addOnReceiveAuctionList(this::updateAuctionList);
        SelectorThread.addOnReceiveUserList(this::updateUserList);
    }

    public JPanel getPanel() {
        return panel;
    }

    private void updateAuctionList(List<Auction> auctionList) {
        auctionListPanel.removeAll();
        for (Auction auction : auctionList) {
            JPanel auctionPanel = new JPanel();
            auctionPanel.setLayout(new BorderLayout());

            JLabel itemImage = new JLabel();
            itemImage.setIcon(new ImageIcon(auction.getItemImage()));
            auctionPanel.add(itemImage, BorderLayout.WEST);

            JPanel linearPanel = new JPanel();
            linearPanel.add(new JLabel(auction.getItemName()));
            linearPanel.add(new JButton("참가"));
            auctionPanel.add(linearPanel, BorderLayout.CENTER);

            auctionListPanel.add(auctionPanel);
        }
        auctionListPanel.add(new JButton("추가"));
        auctionListPanel.revalidate();
        auctionListPanel.repaint();
    }

    private void updateUserList(List<String> userList) {
        userJList.setListData(userList.toArray(new String[0]));
        panel.revalidate();
        panel.repaint();
    }
}
