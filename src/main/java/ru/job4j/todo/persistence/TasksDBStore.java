package ru.job4j.todo.persistence;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.models.Task;

import java.util.List;

@Repository
public class TasksDBStore implements StoreTransaction {
    private final SessionFactory sf;

    public TasksDBStore(SessionFactory sf) {
        this.sf = sf;
    }

    public Task add(Task task) {
        return (Task) transaction(session -> session.merge(task), sf);
    }

    public void delete(int id) {
        transaction(session -> session
                        .createQuery("DELETE Task t WHERE t.id = :id")
                        .setParameter("id", id)
                        .executeUpdate(),
                sf);
    }

    public void update(Task task) {
        transaction(session -> session
                        .createQuery("UPDATE Task t SET t.name = :newName, "
                                + "t.description = :newDesc, t.created = :newCreated "
                                + "WHERE t.id = :id")
                        .setParameter("newName", task.getName())
                        .setParameter("newDesc", task.getDescription())
                        .setParameter("newCreated", task.getCreated())
                        .setParameter("id", task.getId())
                        .executeUpdate(),
                sf);
    }

    public List<Task> findAll() {
        return transaction(session -> session
                        .createQuery("SELECT DISTINCT t FROM Task t JOIN FETCH t.categories ORDER BY t.id")
                        .getResultList(),
                sf);
    }

    public Task findById(int id) {
        return (Task) transaction(session -> session
                .createQuery("FROM Task WHERE id = :id")
                .setParameter("id", id)
                .uniqueResult(), sf);
    }

    public void setDoneById(int id) {
        transaction(session -> session
                        .createQuery("UPDATE Task t SET t.done = :flag "
                                + "WHERE t.id = :id")
                        .setParameter("flag", true)
                        .setParameter("id", id)
                        .executeUpdate(),
                sf);
    }

    public void setActiveById(int id) {
        transaction(session -> session
                        .createQuery("UPDATE Task t SET t.done = :flag "
                                + "WHERE t.id = :id")
                        .setParameter("flag", false)
                        .setParameter("id", id)
                        .executeUpdate(),
                sf);
    }

    public List<Task> findActive() {
        return transaction(session -> session
                        .createQuery("SELECT DISTINCT t FROM Task t JOIN FETCH t.categories WHERE t.done = :flag "
                                + "ORDER BY t.id")
                        .setParameter("flag", false)
                        .getResultList(),
                sf);
    }

    public List<Task> findDone() {
        return transaction(session -> session
                        .createQuery("SELECT DISTINCT t FROM Task t JOIN FETCH t.categories WHERE t.done = :flag "
                                + "ORDER BY t.id")
                        .setParameter("flag", true)
                        .getResultList(),
                sf);
    }
}
