package nio;

import java.nio.channels.SocketChannel;

public class ClientSoket {
    private String name;
    public SocketChannel channel;

    public ClientSoket(String name, SocketChannel channel) {
        this.name = name;
        this.channel = channel;
    }

    public String getName() {
        return name;
    }

    public SocketChannel getChannel() {
        return channel;
    }


}
