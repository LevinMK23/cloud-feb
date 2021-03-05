package netty;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestNettyConnection {

    @Before
    public void initServer() throws InterruptedException {
        new Thread(EchoServer::new).start();
        TimeUnit.SECONDS.sleep(2);
    }

    @Test
    public void test() throws IOException {
        System.out.println("1");
        Socket socket = new Socket("localhost", 8189);
        System.out.println("2");
        socket.getOutputStream().write(new byte[] {65});
        socket.getOutputStream().flush();
        System.out.println((char) 49);
        Assert.assertEquals(65, socket.getInputStream().read());
    }
}
