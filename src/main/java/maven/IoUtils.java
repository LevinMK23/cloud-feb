package maven;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.gson.Gson;

public final class IoUtils {

    private static final Gson gson = new Gson();

    private IoUtils() {
    }

    public static String readAsString(Path path) throws IOException {
        return String.join("", Files.readAllLines(path));
    }

    public static <T> T readJsonAsObject(Path jsonPath, Class<T> clazz) throws IOException { ;
        return gson.fromJson(readAsString(jsonPath), clazz);
    }

    public static void writeAsJson(Object object, Path path) throws IOException {
        String json = gson.toJson(object);
        Files.write(path, json.getBytes());
    }
}
