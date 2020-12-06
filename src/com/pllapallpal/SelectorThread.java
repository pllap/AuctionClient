package com.pllapallpal;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
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
                        ByteBuffer byteBuffer = read(selectionKey);
                        // DEBUG
                        System.out.println(byteBuffer.toString());
                        System.out.println(Arrays.toString(byteBuffer.array()));
                        int protocol = byteBuffer.getInt();
                        switch (protocol) {
                            case Protocol.LOGIN: {
                                break;
                            }
                            case Protocol.LOGOUT: {
                                return;
                            }
                            case Protocol.LIST_AUCTION: {
                                System.out.println("제발");
                                break;
                            }
                            case Protocol.LIST_USER: {
                                List<String> userList = new ArrayList<>();
                                int numData = byteBuffer.getInt();
                                for (int i = 0; i < numData; ++i) {
                                    int usernameBytesLength = byteBuffer.getInt();
                                    byte[] usernameBytes = new byte[usernameBytesLength];
                                    byteBuffer.get(usernameBytes, byteBuffer.arrayOffset(), usernameBytesLength);
                                    userList.add(decoder.decode(ByteBuffer.wrap(usernameBytes)).toString());
                                }
                                byteBuffer.clear();
                                onReceiveList.accept(userList);
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

    private ByteBuffer read(SelectionKey selectionKey) throws IOException {

        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

        ByteBuffer capacityBuffer = ByteBuffer.allocate(Integer.BYTES);
        socketChannel.read(capacityBuffer);
        capacityBuffer.flip();
        int capacity = capacityBuffer.getInt();

        ByteBuffer byteBuffer = ByteBuffer.allocate(capacity);
        while (byteBuffer.hasRemaining()) {
            synchronized (socketChannel) {
                if (socketChannel.isConnected()) {
                    socketChannel.read(byteBuffer);
                }
            }
        }
        byteBuffer.flip();

        return byteBuffer;
    }
}
