import javakanban.elements.Epic;
import javakanban.elements.Status;
import javakanban.elements.Subtask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

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
        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    void testAllSubtasksDone() {
        epic.addSubtask(doneSubtask);
        assertEquals(Status.DONE, epic.getStatus());
    }

    @Test
    void testMixedSubtasks() {
        epic.addSubtask(newSubtask);
        epic.addSubtask(doneSubtask);
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    void testAllSubtasksInProgress() {
        epic.addSubtask(inProgressSubtask);
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    void testCalculateTime() {
        epic.addSubtask(newSubtask);
        epic.addSubtask(doneSubtask);
        assertNotNull(epic.getStartTime());
        assertNotNull(epic.getEndTime());
        assertEquals(Duration.ofMinutes(60), epic.getDuration());
    }
}
