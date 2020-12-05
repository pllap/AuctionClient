package com.pllapallpal;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class SelectorThread implements Runnable {

    private final Selector selector;
    private final CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder();

    private static Consumer<List<String>> onReceiveList;

    public SelectorThread(Selector selector) {
        this.selector = selector;
    }

    @Override
    public void run() {
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
                            case Protocol.LOGIN: {
                                break;
                            }
                            // LOGOUT
                            case Protocol.LOGOUT: {
                                return;
                            }
                            // LIST_AUCTION
                            case Protocol.LIST_AUCTION: {
                                break;
                            }
                            // LIST_User
                            case Protocol.LIST_USER: {
                                int numData = byteBuffer.getInt();
                                List<String> receivedList = Arrays.asList(decoder.decode(byteBuffer).toString().split(">>>"));
                                byteBuffer.clear();
                                onReceiveList.accept(receivedList);
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
    }

    public static void addOnReceiveList(Consumer<List<String>> onReceiveList) {
        SelectorThread.onReceiveList = onReceiveList;
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
}
