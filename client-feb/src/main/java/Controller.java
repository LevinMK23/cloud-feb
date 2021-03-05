import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Controller implements Initializable {

    public TextField in;
    public TextArea out;
    private ObjectEncoderOutputStream os;
    private ObjectDecoderInputStream is;

    public void send(ActionEvent actionEvent) throws IOException {
        os.writeObject(new Message("user", in.getText()));
        os.flush();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Socket socket = new Socket("localhost", 8189);
            os = new ObjectEncoderOutputStream(socket.getOutputStream());
            is = new ObjectDecoderInputStream(socket.getInputStream());

            new Thread(() -> {
                while (true) {
                    try {
                        Message message = (Message) is.readObject();
                        Platform.runLater(() -> out.appendText(message.toString() + "\n"));
                    } catch (ClassNotFoundException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
