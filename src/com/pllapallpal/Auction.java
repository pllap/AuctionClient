package com.pllapallpal;

import java.awt.image.BufferedImage;

public class Auction {

    private String key;
    private String creatorName;
    private BufferedImage itemImage;
    private String itemName;
    private int startingPrice;
    private int entered;

    public Auction(String key) {
        this.key = key;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public BufferedImage getItemImage() {
        return itemImage;
    }

    public void setItemImage(BufferedImage itemImage) {
        this.itemImage = itemImage;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getStartingPrice() {
        return startingPrice;
    }

    public void setStartingPrice(int startingPrice) {
        this.startingPrice = startingPrice;
    }

    public String getKey() {
        return key;
    }

    public void enter() {
        entered = 1;
    }

    public void exit() {
        entered = 0;
    }
}
