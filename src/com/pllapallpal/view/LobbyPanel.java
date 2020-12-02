package com.pllapallpal.view;

import javax.swing.*;
import java.awt.*;

public class LobbyPanel {

    private JPanel panel;

    public LobbyPanel() {
        this.panel = new JPanel(new BorderLayout());

        initializeComponents();
    }

    private void initializeComponents() {

    }

    public JPanel getPanel() {
        return panel;
    }
}
