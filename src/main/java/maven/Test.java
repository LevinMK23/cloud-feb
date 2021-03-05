package maven;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;

public class Test {
    public static void main(String[] args) throws URISyntaxException, IOException {
        User object = IoUtils.readJsonAsObject(
                Path.of(Test.class.getResource("user.json").toURI()),
                User.class);
        System.out.println(object);
        User u2 = new User("Petr", 55);
        IoUtils.writeAsJson(u2, Path.of("output_user.json"));
    }
}
