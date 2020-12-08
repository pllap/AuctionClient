package com.pllapallpal.view;

import com.pllapallpal.Client;
import com.pllapallpal.Data;
import com.pllapallpal.Protocol;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class NewAuctionFrame {

    private final JFrame frame;
    private JPanel mainPanel;
    private final LobbyPanel lobbyPanel;
    private BufferedImage itemImage;
    private boolean imageUploaded = false;

    public NewAuctionFrame(LobbyPanel lobbyPanel) {

        this.lobbyPanel = lobbyPanel;

        frame = new JFrame("New Auction");
        frame.setSize(600, 500);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        try {
            itemImage = ImageIO.read(new File("newAuctionItem.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        initializeComponents();
    }

    private void initializeComponents() {

        mainPanel = (JPanel) frame.getContentPane();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 5, 20));
        mainPanel.setBackground(new Color(255, 245, 235));

        JLabel itemImageLabel = new JLabel();
        itemImageLabel.setIcon(new ImageIcon(itemImage));
        itemImageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(itemImageLabel);

        mainPanel.add(Box.createRigidArea(new Dimension(1, 5)));

        JButton uploadPictureButton = new JButton("Upload Picture");
        uploadPictureButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        uploadPictureButton.addActionListener(e -> {
            FileDialog fileDialog = new FileDialog(new JFrame(), "Select image file", 0);
            String filePath = loadFilePath(fileDialog);
            String fileName = loadFileName(fileDialog);
            try {
                File image = new File(filePath + fileName);
                System.out.println("Select file: " + filePath + fileName);
                itemImage = ImageIO.read(image);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            ImageIcon newIcon = new ImageIcon(itemImage.getScaledInstance(256, 256, Image.SCALE_DEFAULT));
            itemImageLabel.setIcon(newIcon);
            imageUploaded = true;
            mainPanel.revalidate();
            mainPanel.repaint();
        });
        mainPanel.add(uploadPictureButton);

        mainPanel.add(Box.createRigidArea(new Dimension(1, 20)));

        JLabel productNameLabel = new JLabel("Item Name");
        productNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(productNameLabel);
        JTextField productNameField = new JTextField();
        productNameField.setHorizontalAlignment(SwingConstants.CENTER);
        productNameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, productNameField.getPreferredSize().height));
        mainPanel.add(productNameField);

        JLabel startingPriceLabel = new JLabel("Starting Price (Won)");
        startingPriceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(startingPriceLabel);
        JTextField startingPriceField = new JTextField();
        startingPriceField.setHorizontalAlignment(SwingConstants.CENTER);
        startingPriceField.setMaximumSize(new Dimension(Integer.MAX_VALUE, startingPriceField.getPreferredSize().height));
        mainPanel.add(startingPriceField);

        mainPanel.add(Box.createRigidArea(new Dimension(1, 10)));

        JButton submitButton = new JButton("Submit New Auction");
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitButton.addActionListener(e -> {
            if (imageUploaded) {
                String username = Data.getInstance().getNickname();
                String itemName = productNameField.getText();
                int startingPrice = 0;
                try {
                    startingPrice = Integer.parseInt(startingPriceField.getText());
                } catch (NumberFormatException ex) {
                    startingPriceField.setText(startingPriceField.getText() + " - Only numbers can be submitted");
                    return;
                }

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                try {
                    ImageIO.write(itemImage, "png", byteArrayOutputStream);
                    byteArrayOutputStream.flush();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                byte[] itemImageBytes = byteArrayOutputStream.toByteArray();
                int capacity = Integer.BYTES + // protocol (4bytes)
                        Integer.BYTES + username.getBytes().length + // username length (4bytes) + username data
                        Integer.BYTES + itemImageBytes.length + // image length (4bytes) + image data
                        Integer.BYTES + itemName.getBytes().length + // item name length (4bytes) + item name data
                        Integer.BYTES; // start price
                ByteBuffer capacityBuffer = ByteBuffer.allocate(Integer.BYTES);
                capacityBuffer.putInt(capacity);
                capacityBuffer.flip();
                Client.getInstance().send(capacityBuffer);

                ByteBuffer byteBuffer = ByteBuffer.allocate(capacity);
                byteBuffer.putInt(Protocol.NEW_AUCTION);
                byteBuffer.putInt(username.getBytes().length);
                byteBuffer.put(username.getBytes());
                byteBuffer.putInt(itemImageBytes.length);
                byteBuffer.put(itemImageBytes);
                byteBuffer.putInt(itemName.getBytes().length);
                byteBuffer.put(itemName.getBytes());
                byteBuffer.putInt(startingPrice);
                byteBuffer.flip();
                Client.getInstance().send(byteBuffer);

                frame.dispose();
            }
        });
        mainPanel.add(submitButton);
    }

    public String loadFilePath(FileDialog fileDialog) {
        fileDialog.setDirectory("C:");
        fileDialog.setVisible(true);
        return fileDialog.getDirectory();
    }

    public String loadFileName(FileDialog fileDialog) {
        return fileDialog.getFile();
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setVisible(boolean b) {
        frame.setVisible(b);
    }
}
