package ru.job4j.todo.persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.models.Task;

import java.util.List;
import java.util.function.Function;

@Repository
public class TasksDBStore {
    private final SessionFactory sf;

    public TasksDBStore(SessionFactory sf) {
        this.sf = sf;
    }

    private <T> T transaction(final Function<Session, T> command) {
        final Session session = sf.openSession();
        final Transaction transaction = session.beginTransaction();
        try {
            T rsl = command.apply(session);
            transaction.commit();
            return rsl;
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public Task add(Task task) {
        return (Task) this.transaction(session -> session.merge(task));
    }

    public void delete(int id) {
        this.transaction(session -> session
                .createQuery("delete Task t where t.id = :id")
                .setParameter("id", id)
                .executeUpdate());
    }

    public void update(Task task) {
        this.transaction(session -> session
                .createQuery("update Task t set t.name = :newName, "
                        + "t.description = :newDesc, t.created = :newCreated "
                        + "where t.id = :id")
                .setParameter("newName", task.getName())
                .setParameter("newDesc", task.getDescription())
                .setParameter("newCreated", task.getCreated())
                .setParameter("id", task.getId())
                .executeUpdate());
    }

    public List<Task> findAll() {
        return this.transaction(session -> session
                .createQuery("from Task t order by t.id")
                .getResultList());
    }

    public Task findById(int id) {
        return (Task) this.transaction(session -> session
                .createQuery("from Task where id = :id")
                .setParameter("id", id)
                .uniqueResult());
    }

    public void setDoneById(int id) {
        this.transaction(session -> session
                .createQuery("update Task t set t.done = :flag "
                        + "where t.id = :id")
                .setParameter("flag", true)
                .setParameter("id", id)
                .executeUpdate());
    }

    public void setActiveById(int id) {
        this.transaction(session -> session
                .createQuery("update Task t set t.done = :flag "
                        + "where t.id = :id")
                .setParameter("flag", false)
                .setParameter("id", id)
                .executeUpdate());
    }

    public List<Task> findActive() {
        return this.transaction(session -> session
                .createQuery("from Task t where t.done = :flag")
                .setParameter("flag", false)
                .getResultList());
    }

    public List<Task> findDone() {
        return this.transaction(session -> session
                .createQuery("from Task t where t.done = :flag")
                .setParameter("flag", true)
                .getResultList());
    }
}
