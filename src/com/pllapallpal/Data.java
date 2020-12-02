package com.pllapallpal;

public class Data {

    private String nickname = null;

    private Data() {

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
}
