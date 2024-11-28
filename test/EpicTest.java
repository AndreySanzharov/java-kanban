import javakanban.elements.Epic;
import javakanban.elements.Status;
import javakanban.elements.Subtask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

public class EpicTest {

    private Epic epic;
    private Subtask newSubtask;
    private Subtask doneSubtask;
    private Subtask inProgressSubtask;

    @BeforeEach
    void setUp() {
        epic = new Epic("Epic", "Epic description");
        newSubtask = new Subtask("New Subtask", "Description", Status.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        doneSubtask = new Subtask("Done Subtask", "Description", Status.DONE, Duration.ofMinutes(30), LocalDateTime.now().plusDays(1));
        inProgressSubtask = new Subtask("In Progress Subtask", "Description", Status.IN_PROGRESS, Duration.ofMinutes(30), LocalDateTime.now().plusDays(2));
    }

    @Test
    void testAllSubtasksNew() {
        epic.addSubtask(newSubtask);
        Assertions.assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    void testAllSubtasksDone() {
        epic.addSubtask(doneSubtask);
        Assertions.assertEquals(Status.DONE, epic.getStatus());
    }

    @Test
    void testMixedSubtasks() {
        epic.addSubtask(newSubtask);
        epic.addSubtask(doneSubtask);
        Assertions.assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    void testAllSubtasksInProgress() {
        epic.addSubtask(inProgressSubtask);
        Assertions.assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    void testCalculateTime() {
        epic.addSubtask(newSubtask);
        epic.addSubtask(doneSubtask);
        Assertions.assertNotNull(epic.getEpicStartTime());
        Assertions.assertNotNull(epic.getEpicEndTime());
        Assertions.assertEquals(Duration.ofMinutes(60), epic.getEpicDuration());
    }
}
