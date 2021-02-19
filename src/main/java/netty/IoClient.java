package netty;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class IoClient {
    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        byte[] buffer = new byte[1024];

        Socket socket = new Socket("localhost", 8189);
        DataOutputStream os = new DataOutputStream(socket.getOutputStream());
        DataInputStream is = new DataInputStream(socket.getInputStream());


        new Thread(() -> {
            try {
                while (true) {
                    int read = is.read(buffer);
                    System.out.println(new String(buffer, 0, read));
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }).start();

        while (in.hasNext()) {
            String msg = in.nextLine();
            os.write(msg.getBytes(StandardCharsets.UTF_8));
            os.flush();
        }
    }
}
