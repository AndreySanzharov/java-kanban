import javakanban.elements.Epic;
import javakanban.elements.Status;
import javakanban.elements.Subtask;
import javakanban.elements.Task;
import javakanban.managers.FileBackedTaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileBackedTaskManagerTest {

    private File tempFile;
    private File restrictedDir;
    private File saveFile;

    @BeforeEach
    void setUp() throws IOException {
        tempFile = Files.createTempFile("test", ".csv").toFile();
        tempFile.deleteOnExit();

        restrictedDir = new File("restrictedDir");
        restrictedDir.mkdir();
        restrictedDir.setReadable(true);
        restrictedDir.setWritable(false);

        saveFile = new File(restrictedDir, "save.csv");
    }

    @AfterEach
    void tearDown() {
        // Удаление временных файлов и директорий
        if (restrictedDir.exists()) {
            restrictedDir.delete();
        }
        if (tempFile.exists()) {
            tempFile.delete();
        }
        if (saveFile.exists()) {
            saveFile.delete();
        }
    }

    @Test
    void testLoadFromFileThrowsIOExceptionWhenFileDoesNotExist() {
        // Файл не существует
        File nonExistentFile = new File("non_existent_file.csv");

        assertThrows(IOException.class, () -> {
            FileBackedTaskManager.loadFromFile(nonExistentFile);
        });
    }

    @Test
    void testSaveAndLoad() throws IOException {
        FileBackedTaskManager manager = new FileBackedTaskManager();
        manager.addTask(new Task("task1", "task1 desc", Status.NEW,
                Duration.ofMinutes(55), LocalDateTime.of(2024, 11, 10, 10, 0)));
        manager.addEpic(new Epic("epic1", "epic1 desc"));
        manager.addSubtask(1, new Subtask("sub1", "sub1 desc", Status.DONE,
                Duration.ofMinutes(55), LocalDateTime.of(2024, 11, 12, 11, 0)));

        File saveFile = new File(tempFile.getAbsolutePath());
        manager.save();

        assertTrue(Files.exists(Paths.get(saveFile.getPath())));

        manager = new FileBackedTaskManager();
        FileBackedTaskManager.loadFromFile(saveFile);

    }
}
