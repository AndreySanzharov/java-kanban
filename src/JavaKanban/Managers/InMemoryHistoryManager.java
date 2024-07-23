package JavaKanban.Managers;

import JavaKanban.Elements.Node;
import JavaKanban.Elements.Task;
import JavaKanban.Interfaces.HistoryManager;

import java.util.*;


public class InMemoryHistoryManager implements HistoryManager {

    private Node<Task> head;
    private Node<Task> tail;

    public static Map<Integer, Node<Task>> history = new HashMap();

    private void linklast(Task task) {
        Node<Task> newNode = new Node<>(task);
        if (tail == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        history.put(task.getId(), newNode);
    }

    private List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        Node<Task> current = head;
        while (current != null) {
            tasks.add(current.data);
            current = current.next;
        }
        return tasks;
    }

    private void removeNode(Node<Task> node) {
        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next;
        }

        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev;
        }

        history.remove(node.data.getId());
    }

    @Override
    public void add(Task task) {
        Node<Task> existingNode = history.get(task.getId());
        if (existingNode != null) {
            removeNode(existingNode);
        }
        linklast(task);
    }

    @Override
    public void remove(int id) {
        Node<Task> node = history.get(id);
        removeNode(node);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

}

