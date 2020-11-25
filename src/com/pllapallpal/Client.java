package com.pllapallpal;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Scanner;

public class Client {

    private final String ADDRESS;
    private final int PORT;
    private SocketChannel socketChannel = null;
    private Selector selector = null;
    private CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder();

    public Client(String address, int port) {
        this.ADDRESS = address;
        this.PORT = port;

        initClient();

        // reader
        new Thread(() -> {
            try {
                while (true) {
                    selector.select();
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey selectionKey = iterator.next();
                        if (selectionKey.isReadable()) {
                            read(selectionKey);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        // writer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        try {
            while (true) {
                Scanner sc = new Scanner(System.in);
                String message = sc.nextLine();
                byteBuffer.clear();
                byteBuffer.put(message.getBytes());
                byteBuffer.flip();
                socketChannel.write(byteBuffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private void read(SelectionKey selectionKey) {
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        try {
            byteBuffer.flip();
            String data = decoder.decode(byteBuffer).toString();
            System.out.println("Received: " + data);
            byteBuffer.clear();
        } catch (IOException e) {
            try {
                socketChannel.close();
            } catch (IOException ex) {
                e.printStackTrace();
            }
        }
    }
}
