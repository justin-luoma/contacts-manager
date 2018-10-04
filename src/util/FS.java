package util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FS {
    private Path file;
    private List<String> fileLines;

    public FS(String path) throws Exception {
        Path file = Paths.get(path);
        if (Files.exists(file) && Files.isRegularFile(file) && Files.isReadable(file)) {
            this.file = file;
            return;
        }
        throw new Exception("not a usable file");
    }

    public FS(String path, List<String> lines) throws Exception {
        Path file = Paths.get(path);
        if (Files.exists(file) && Files.isRegularFile(file) && Files.isWritable(file)) {
            this.file = file;
            this.fileLines = lines;
        } else {
            if (file.getParent() != null) {
                Files.createDirectories(file.getParent());
            }
            Files.createFile(file);
            this.file = file;
            this.fileLines = lines;
        }
    }

    public List<String> read() throws Exception {
        this.fileLines = Files.readAllLines(this.file);
        return this.fileLines;
    }

    public void write() throws Exception {
        Files.write(this.file, this.fileLines);
    }
}
