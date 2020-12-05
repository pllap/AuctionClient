package com.pllapallpal.view;

import com.pllapallpal.SelectorThread;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AuctionPanel {

    private JPanel panel;
    private JPanel itemPanel;

    private JList<String> list;

    public AuctionPanel() {
        panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.GREEN);

        itemPanel = new JPanel(new BorderLayout());
        JLabel itemImage = new JLabel();
        ImageIcon imageIcon = new ImageIcon("itemImage.png");
        itemImage.setIcon(imageIcon);
        itemPanel.add(itemImage, BorderLayout.NORTH);
        JTextArea itemInfo = new JTextArea();
        JScrollPane itemInfoScrollPane = new JScrollPane(itemInfo, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        itemInfoScrollPane.setSize(new Dimension(5, 40));
        itemPanel.add(itemInfoScrollPane, BorderLayout.CENTER);
        panel.add(itemPanel, BorderLayout.WEST);

        list = new JList<>();
        SelectorThread.addOnReceiveList(this::updateUserList);
        panel.add(list, BorderLayout.EAST);
    }

    public JPanel getPanel() {
        return panel;
    }

    private void updateUserList(List<String> userList) {
        list.setListData(userList.toArray(new String[0]));
        panel.revalidate();
        panel.repaint();
    }
}
