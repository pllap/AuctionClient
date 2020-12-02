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
import java.util.Set;

public class Client {

    private final String ADDRESS;
    private final int PORT;
    private SocketChannel socketChannel = null;
    private Selector selector = null;
    private final CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder();

    private Client(String address, int port) {
        this.ADDRESS = address;
        this.PORT = port;

        initClient();

        // reader
        new Thread(() -> {
            try {
                while (true) {
                    selector.select();
                    Set<SelectionKey> selectedKeys = selector.selectedKeys();
                    Iterator<SelectionKey> keys = selectedKeys.iterator();
                    while (keys.hasNext()) {
                        SelectionKey selectionKey = keys.next();
                        if (selectionKey.isReadable()) {
                            ByteBuffer byteBuffer = readFrom((SocketChannel) selectionKey.channel());
                            int protocol = byteBuffer.getInt();
                            switch (protocol) {
                                // LOGIN
                                case 100: {
                                    break;
                                }
                                // LOGOUT
                                case 101: {
                                    break;
                                }
                                // LIST
                                case 102: {
                                    int numData = byteBuffer.getInt();
                                    String receivedList = decoder.decode(byteBuffer).toString();
                                    System.out.println("Received: " + receivedList);
                                    byteBuffer.clear();
                                    break;
                                }

                            }
                        }
                        keys.remove();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
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

    private ByteBuffer readFrom(SocketChannel socketChannel) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        try {
            socketChannel.read(byteBuffer);
        } catch (IOException e) {
            try {
                socketChannel.close();
            } catch (IOException ex) {
                e.printStackTrace();
            }
        }

        byteBuffer.flip();
        return byteBuffer;
    }

    public void send(ByteBuffer byteBuffer) {
        try {
            socketChannel.write(byteBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
