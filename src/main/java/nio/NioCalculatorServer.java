package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static java.nio.channels.SelectionKey.OP_ACCEPT;
import static java.nio.channels.SelectionKey.OP_READ;

public class NioCalculatorServer {

    private ServerSocketChannel serverChannel;
    private Selector selector;
    private ByteBuffer buffer;
    List<ClientSoket> list;
    private static int clientCount = 0;


    public NioCalculatorServer() throws IOException {
        list = new ArrayList<>();
        buffer = ByteBuffer.allocate(256);
        serverChannel = ServerSocketChannel.open();
        selector = Selector.open();
        serverChannel.bind(new InetSocketAddress(8189));
        serverChannel.configureBlocking(false);
        serverChannel.register(selector, OP_ACCEPT);
        while (serverChannel.isOpen()) {
            selector.select(); // block
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = keys.iterator();
            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                if (key.isAcceptable()) {
                    handleAccept(key);
                }
                if (key.isReadable()) {
                    handleRead(key);

                }

                keyIterator.remove();
            }
        }
    }

    private void handleRead(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        StringBuilder msg = new StringBuilder();
        String name = "";
        for (ClientSoket clientSoket : list) {
            if (clientSoket.getChannel().equals(channel)) {
                name = clientSoket.getName();
                break;
            }
        }
        int read = channel.read(buffer);
        if (read == -1) {
                    return;
                }

            buffer.flip();
            while (buffer.hasRemaining()) {
                msg.append((char) buffer.get());
            }

           String message = new String(msg);
            if(message.startsWith("lm")) {
                String[] token = message.split(" ", 3);
                for(ClientSoket client : list) {
                    if(client.getName().equals(token[1])) {
                        client.getChannel().write(ByteBuffer.wrap((name + ": " + token[2]).
                                getBytes(StandardCharsets.UTF_8)));
                        break;
                    }
                }
            }
            else {
                for (ClientSoket clientSoket : list) {
                    clientSoket.getChannel().write(ByteBuffer.wrap((name + ": " + msg.toString()
                            + System.lineSeparator()).getBytes(StandardCharsets.UTF_8)));
                }
            }
            System.out.print(msg.toString());
            buffer.clear();
    }


    private void handleAccept(SelectionKey key) throws IOException {
        ServerSocketChannel channel = (ServerSocketChannel) key.channel();
        SocketChannel socketChannel = channel.accept();
        clientCount++;
        list.add(new ClientSoket("Client#" + clientCount, socketChannel));
        socketChannel.write(ByteBuffer.wrap(
                "Hello to chat server!".getBytes(StandardCharsets.UTF_8)));
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, OP_READ);
    }

    public static void main(String[] args) throws IOException {
        new NioCalculatorServer();
    }
}
