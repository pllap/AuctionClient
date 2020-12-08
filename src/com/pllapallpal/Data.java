package com.pllapallpal;

import java.util.ArrayList;
import java.util.List;

public class Data {

    private String nickname = null;
    private List<Auction> auctionList;
    private List<String> userList;
    private Auction currentAuction;

    private Data() {
        userList = new ArrayList<>();
        auctionList = new ArrayList<>();
    }

    private static class DataHolder {
        private static final Data instance = new Data();
    }

    public static Data getInstance() {
        return DataHolder.instance;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public List<String> getUserList() {
        return userList;
    }

    public List<Auction> getAuctionList() {
        return auctionList;
    }

    public void setCurrentAuction(Auction currentAuction) {
        this.currentAuction = currentAuction;
    }

    public Auction getCurrentAuction() {
        return currentAuction;
    }
}
