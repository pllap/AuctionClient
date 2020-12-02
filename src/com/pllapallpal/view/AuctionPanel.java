package com.pllapallpal.view;

import javax.swing.*;
import java.awt.*;

public class AuctionPanel {

    private JPanel panel;

    public AuctionPanel() {
        this.panel = new JPanel(new BorderLayout());

        JPanel itemInfoPanel = new JPanel(new BorderLayout());
        JLabel itemImage = new JLabel();
        ImageIcon imageIcon = new ImageIcon();
        itemImage.setIcon(imageIcon);
        JTextArea textArea = new JTextArea();
        JScrollPane itemInfoScrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        itemInfoPanel.add(itemImage, BorderLayout.NORTH);
        itemInfoPanel.add(itemInfoScrollPane, BorderLayout.CENTER);
    }

    public JPanel getPanel() {
        return panel;
    }
}
