package ru.job4j.todo.services;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.todo.models.Task;
import ru.job4j.todo.persistence.TasksDBStore;

import java.util.List;

@ThreadSafe
@Service
public class TasksService {
    private final TasksDBStore tasksDBStore;

    public TasksService(TasksDBStore tasksDBStore) {
        this.tasksDBStore = tasksDBStore;
    }

    public Task add(Task task) {
        return tasksDBStore.add(task);
    }

    public void delete(int id) {
        tasksDBStore.delete(id);
    }

    public List<Task> findAll() {
        return tasksDBStore.findAll();
    }

    public Task findById(int id) {
        return tasksDBStore.findById(id);
    }

    public List<Task> findActive() {
        return tasksDBStore.findActive();
    }

    public List<Task> findDone() {
        return tasksDBStore.findDone();
    }

    public void update(Task task) {
        tasksDBStore.update(task);
    }

    public void setDoneById(int id) {
        tasksDBStore.setDoneById(id);
    }

    public void setActiveById(int id) {
        tasksDBStore.setActiveById(id);
    }
}
