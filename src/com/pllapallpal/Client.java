package com.pllapallpal;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class Client {

    private final String ADDRESS;
    private final int PORT;
    private SocketChannel socketChannel = null;
    private Selector selector = null;

    private Client(String address, int port) {
        this.ADDRESS = address;
        this.PORT = port;

        initClient();

        // reader
        new Thread(new SelectorThread(selector)).start();
    }

    private static class ClientHolder {
        private static final Client instance = new Client("localhost", 7777);
    }

    public static Client getInstance() {
        return ClientHolder.instance;
    }

    private void initClient() {
        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open(new InetSocketAddress(ADDRESS, PORT));
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(ByteBuffer byteBuffer) {
        try {
            socketChannel.write(byteBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
