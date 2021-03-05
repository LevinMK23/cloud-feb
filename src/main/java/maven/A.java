package maven;

import java.io.IOException;
import java.nio.file.Path;

public class A {

    public void foo(Path path) throws IOException {
        IoUtils.readAsString(path);
    }
}