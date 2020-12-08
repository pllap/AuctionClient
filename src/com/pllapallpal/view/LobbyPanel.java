package com.pllapallpal.view;

import com.pllapallpal.*;

import javax.swing.*;
import java.awt.*;
import java.nio.ByteBuffer;
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
        JScrollPane scrollPane = new JScrollPane(auctionListPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));
        scrollPane.getVerticalScrollBar().setUnitIncrement(8);
        auctionListPanel.setLayout(new BoxLayout(auctionListPanel, BoxLayout.Y_AXIS));
        auctionListPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
        auctionListPanel.setBackground(Color.PINK.brighter());
        auctionListPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.add(scrollPane, BorderLayout.CENTER);

        userJList = new JList<>();
        userJList.setFont(new Font("Segoe", Font.PLAIN, 17));
        userJList.setEnabled(false);
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
        auctionItemPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JLabel itemImageLabel = new JLabel();
        itemImageLabel.setIcon(new ImageIcon(item.getItemImage().getScaledInstance(128, 128, Image.SCALE_DEFAULT)));
        auctionItemPanel.add(itemImageLabel, BorderLayout.WEST);

        JPanel itemInfoPanel = new JPanel();
        itemInfoPanel.setLayout(new BoxLayout(itemInfoPanel, BoxLayout.PAGE_AXIS));
        itemInfoPanel.setBackground(Color.WHITE);
        itemInfoPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JLabel itemName = new JLabel("Item name: " + item.getItemName());
        itemName.setFont(new Font("Segoe", Font.PLAIN, 17));
        itemInfoPanel.add(itemName);
        JLabel itemPrice = new JLabel("Starting price: " + Integer.toString(item.getStartingPrice()));
        itemPrice.setFont(new Font("Segoe", Font.PLAIN, 17));
        itemInfoPanel.add(itemPrice);
        itemInfoPanel.add(Box.createVerticalGlue());
        JButton button = new JButton("Enter");
        button.setFont(new Font("Segoe", Font.PLAIN, 17));
        button.addActionListener(e -> {
            int capacity = Integer.BYTES + // protocol
                    Integer.BYTES + item.getKey().getBytes().length; // auction key length, auction key
            ByteBuffer capacityBuffer = ByteBuffer.allocate(Integer.BYTES);
            capacityBuffer.putInt(capacity);
            capacityBuffer.flip();

            ByteBuffer byteBuffer = ByteBuffer.allocate(capacity);
            byteBuffer.putInt(Protocol.AUCTION_ENTER); // protocol
            byteBuffer.putInt(item.getKey().getBytes().length); // auction key length
            byteBuffer.put(item.getKey().getBytes()); // auction key
            byteBuffer.flip();
            Client.getInstance().send(capacityBuffer);
            Client.getInstance().send(byteBuffer);

            Data.getInstance().setCurrentAuction(item);
        });
        itemInfoPanel.add(button);
        auctionItemPanel.add(itemInfoPanel, BorderLayout.CENTER);

        auctionItemPanel.setMaximumSize(new Dimension(600, 200));
        auctionItemPanel.validate();
        return auctionItemPanel;
    }

    private void updateUserList(List<String> userList) {
        userJList.setListData(userList.toArray(new String[0]));
        panel.revalidate();
        panel.repaint();
    }
}
