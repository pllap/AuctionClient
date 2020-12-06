package com.pllapallpal.view;

import javax.swing.*;
import java.awt.*;

public class AuctionPanel {

    private JPanel panel;
    private MainFrame mainFrame;

    public AuctionPanel(MainFrame mainFrame) {

        this.mainFrame = mainFrame;

        this.panel = new JPanel(new BorderLayout());
        this.panel.setBackground(Color.RED);

        initializeComponents();
    }

    private void initializeComponents() {

    }

    public JPanel getPanel() {
        return panel;
    }
}
