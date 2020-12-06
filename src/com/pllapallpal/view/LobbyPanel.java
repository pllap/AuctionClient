package com.pllapallpal.view;

import com.pllapallpal.Auction;
import com.pllapallpal.SelectorThread;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LobbyPanel {

    private JPanel panel;
    private MainFrame mainFrame;
    private JPanel auctionListPanel;
    private JPanel itemPanel;
    private JList<String> userJList;

    public LobbyPanel(MainFrame mainFrame) {

        this.mainFrame = mainFrame;

        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.setBackground(Color.PINK.brighter());

        auctionListPanel = new JPanel();
        auctionListPanel.setLayout(new BoxLayout(auctionListPanel, BoxLayout.Y_AXIS));
        auctionListPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
        auctionListPanel.setBackground(Color.CYAN);
        auctionListPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        Dimension auctionListPanelPreferredSize = auctionListPanel.getPreferredSize();
        auctionListPanelPreferredSize.width = 600;
        auctionListPanel.setPreferredSize(auctionListPanelPreferredSize);
        panel.add(auctionListPanel, BorderLayout.CENTER);

        userJList = new JList<>();
        Dimension userJListPreferredSize = userJList.getPreferredSize();
        userJListPreferredSize.width = 200;
        userJList.setPreferredSize(userJListPreferredSize);
        userJList.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        panel.add(userJList, BorderLayout.EAST);

        SelectorThread.addOnReceiveAuctionList(this::updateAuctionList);
        SelectorThread.addOnReceiveUserList(this::updateUserList);
    }

    public JPanel getPanel() {
        return panel;
    }

    private void updateAuctionList(List<Auction> auctionList) {
        auctionListPanel.removeAll();
        for (Auction item : auctionList) {
            JPanel auctionItemPanel = makeAuctionItemPanel(item);
            auctionListPanel.add(auctionItemPanel);
            auctionListPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        }
        JButton makeNewAuctionButton = new JButton("Make New Auction");
        makeNewAuctionButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        makeNewAuctionButton.setFont(new Font("Segoe", Font.PLAIN, 20));
        makeNewAuctionButton.addActionListener(e -> {
            NewAuctionFrame newAuctionFrame = new NewAuctionFrame(this);
            newAuctionFrame.setVisible(true);
        });
        auctionListPanel.add(makeNewAuctionButton);
        auctionListPanel.revalidate();
        auctionListPanel.repaint();
    }

    private JPanel makeAuctionItemPanel(Auction item) {
        JPanel auctionItemPanel = new JPanel();
        auctionItemPanel.setLayout(new BorderLayout());

        JLabel itemImage = new JLabel();
        itemImage.setIcon(new ImageIcon(item.getItemImage()));
        auctionItemPanel.add(itemImage, BorderLayout.WEST);

        JPanel linearPanel = new JPanel();
        linearPanel.add(new JLabel(item.getItemName()));
        linearPanel.add(new JButton("참가"));
        auctionItemPanel.add(linearPanel, BorderLayout.CENTER);
        return auctionItemPanel;
    }

    private void updateUserList(List<String> userList) {
        userJList.setListData(userList.toArray(new String[0]));
        panel.revalidate();
        panel.repaint();
    }
}
