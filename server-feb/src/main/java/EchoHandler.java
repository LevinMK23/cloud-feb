import java.util.Enumeration;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class EchoHandler extends SimpleChannelInboundHandler<String> {

    private EchoServer server;
    private String userName;

    public EchoHandler(EchoServer server, String userName) {
        this.server = server;
        this.userName = userName;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // ctx.writeAndFlush(userName + ", приветствую тебя в лучшем чате на земле!\n");
        server.getClients().put(ctx, userName);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
        Enumeration<ChannelHandlerContext> clients = server.getClients().keys();
        while (clients.hasMoreElements()) {
            clients.nextElement().writeAndFlush(s);
        }
        System.out.println("received: " + s);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client disconnected");
        server.getClients().remove(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
