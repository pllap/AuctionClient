package com.pllapallpal.view;

import com.pllapallpal.Auction;
import com.pllapallpal.Client;
import com.pllapallpal.Protocol;
import com.pllapallpal.SelectorThread;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.nio.ByteBuffer;

public class AuctionPanel {

    private JPanel panel;
    private MainFrame mainFrame;
    JTextArea chatTextArea;

    private Auction auction;

    public AuctionPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public void setAuction(Auction auction) {
        this.auction = auction;
        SelectorThread.addOnMessageReceived(this::onMessageReceived);

        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel itemInfoPanel = new JPanel();
        itemInfoPanel.setLayout(new BorderLayout());
        itemInfoPanel.setBackground(Color.PINK.brighter());
        itemInfoPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        itemInfoPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JLabel itemImageLabel = new JLabel();
        itemImageLabel.setIcon(new ImageIcon(auction.getItemImage().getScaledInstance(128, 128, Image.SCALE_DEFAULT)));
        itemInfoPanel.add(itemImageLabel, BorderLayout.NORTH);
        JTextPane itemImageInfo = new JTextPane();
        itemImageInfo.setFont(new Font("Segoe", Font.PLAIN, 17));
        itemImageInfo.setText("Item name: " + auction.getItemName() + "\n");
        itemImageInfo.setText(itemImageInfo.getText() + "Start price: " + auction.getStartingPrice() + "\n");
        itemImageInfo.setText(itemImageInfo.getText() + "Creator: " + auction.getCreatorName());
        itemInfoPanel.add(itemImageInfo, BorderLayout.CENTER);
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Segoe", Font.PLAIN, 17));
        backButton.setPreferredSize(new Dimension(150, backButton.getPreferredSize().height));
        backButton.addActionListener(e -> {
            String auctionKey = auction.getKey();
            byte[] byteAuctionKey = auctionKey.getBytes();
            int capacity = Integer.BYTES + // protocol
                    Integer.BYTES + byteAuctionKey.length; // key bytes + key
            ByteBuffer capacityBuffer = ByteBuffer.allocate(Integer.BYTES);
            capacityBuffer.putInt(capacity);
            capacityBuffer.flip();

            ByteBuffer byteBuffer = ByteBuffer.allocate(capacity);
            byteBuffer.putInt(Protocol.AUCTION_QUIT);
            byteBuffer.putInt(byteAuctionKey.length);
            byteBuffer.put(byteAuctionKey);
            byteBuffer.flip();

            Client.getInstance().send(capacityBuffer);
            Client.getInstance().send(byteBuffer);

            auction.exit();
            mainFrame.backToAuctionList();
        });
        itemInfoPanel.add(backButton, BorderLayout.SOUTH);
        panel.add(itemInfoPanel, BorderLayout.WEST);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        JTextPane bidLog = new JTextPane();
        bidLog.setFont(new Font("Segoe", Font.PLAIN, 17));
        bidLog.setEditable(false);
        bidLog.setText("Bid Log");
        centerPanel.add(bidLog, BorderLayout.CENTER);
        JPanel bidPanel = new JPanel();
        bidPanel.setLayout(new BorderLayout());
        bidPanel.setBackground(Color.PINK.brighter());
        bidPanel.setBorder(BorderFactory.createEmptyBorder(30, 5, 0, 5));
        JLabel bidLabel = new JLabel("Enter bid (number only) ");
        bidLabel.setFont(new Font("Segoe", Font.PLAIN, 17));
        bidPanel.add(bidLabel, BorderLayout.WEST);
        JTextField inputBid = new JTextField();
        inputBid.setFont(new Font("Segoe", Font.PLAIN, 17));
        bidPanel.add(inputBid, BorderLayout.CENTER);
        JButton submitBid = new JButton("Submit");
        submitBid.setFont(new Font("Segoe", Font.PLAIN, 17));
        bidPanel.add(submitBid, BorderLayout.EAST);
        centerPanel.add(bidPanel, BorderLayout.SOUTH);
        panel.add(centerPanel, BorderLayout.CENTER);

        JPanel eastPanel = new JPanel();
        eastPanel.setLayout(new BorderLayout());
        JTextPane leftTimePane = new JTextPane();
        StyledDocument doc = leftTimePane.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        leftTimePane.setText("Left Time\n00:30");
        leftTimePane.setFont(new Font("Segoe", Font.PLAIN, 20));
        leftTimePane.setForeground(Color.RED);
        leftTimePane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        eastPanel.add(leftTimePane, BorderLayout.NORTH);

        chatTextArea = new JTextArea();
        chatTextArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        chatTextArea.setPreferredSize(new Dimension(150, chatTextArea.getPreferredSize().height));
        eastPanel.add(chatTextArea, BorderLayout.CENTER);
        JPanel chatInputPanel = new JPanel();
        chatInputPanel.setLayout(new BorderLayout());
        JTextField chatInputField = new JTextField();
        chatInputField.addActionListener(event -> {

            String auctionKey = auction.getKey();
            byte[] byteAuctionKey = auctionKey.getBytes();

            String message = chatInputField.getText();
            chatInputField.setText("");
            byte[] byteMessage = message.getBytes();

            int capacity = Integer.BYTES + // protocol
                    Integer.BYTES + byteAuctionKey.length + // auction key length + auction key
                    Integer.BYTES + byteMessage.length; // message length + message
            ByteBuffer capacityBuffer = ByteBuffer.allocate(Integer.BYTES);
            capacityBuffer.putInt(capacity);
            capacityBuffer.flip();

            ByteBuffer byteBuffer = ByteBuffer.allocate(capacity);
            byteBuffer.putInt(Protocol.AUCTION_MESSAGE);
            byteBuffer.putInt(byteAuctionKey.length);
            byteBuffer.put(byteAuctionKey);
            byteBuffer.putInt(byteMessage.length);
            byteBuffer.put(byteMessage);
            byteBuffer.flip();

            Client.getInstance().send(capacityBuffer);
            Client.getInstance().send(byteBuffer);
        });
        chatInputPanel.add(chatInputField, BorderLayout.CENTER);
        JButton chatSendButton = new JButton("send");
        chatInputPanel.add(chatSendButton, BorderLayout.EAST);
        eastPanel.add(chatInputPanel, BorderLayout.SOUTH);
        panel.add(eastPanel, BorderLayout.EAST);
    }

    void onMessageReceived(String message) {
        chatTextArea.setText(chatTextArea.getText() + message + "\n");
    }

    public JPanel getPanel() {
        return panel;
    }
}
