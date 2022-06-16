package ru.job4j.todo.persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.models.Task;

import java.util.List;

@Repository
public class TasksDBStore {
    private final SessionFactory sf;

    public TasksDBStore(SessionFactory sf) {
        this.sf = sf;
    }

    public Task add(Task task) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.save(task);
        session.getTransaction().commit();
        session.close();
        return task;
    }

    public void delete(int id) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.createQuery("delete Task t where t.id = :id")
                .setParameter("id", id)
                .executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    public void update(Task task) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.createQuery(
                        "update Task t set t.name = :newName, t.description = :newDesc, t.created = :newCreated "
                                + "where t.id = :id")
                .setParameter("newName", task.getName())
                .setParameter("newDesc", task.getDescription())
                .setParameter("newCreated", task.getCreated())
                .setParameter("id", task.getId())
                .executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    public List<Task> findAll() {
        Session session = sf.openSession();
        session.beginTransaction();
        List<Task> taskList = session.createQuery("from Task t order by t.id").getResultList();
        session.getTransaction().commit();
        session.close();
        return taskList;
    }

    public Task findById(int id) {
        Session session = sf.openSession();
        session.beginTransaction();
        Task task = (Task) session.createQuery("from Task where id = :id").setParameter("id", id).uniqueResult();
        session.getTransaction().commit();
        session.close();
        return task;
    }

    public void setDoneById(int id) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.createQuery(
                        "update Task t set t.done = :flag "
                                + "where t.id = :id")
                .setParameter("flag", true)
                .setParameter("id", id)
                .executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    public void setActiveById(int id) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.createQuery(
                        "update Task t set t.done = :flag "
                                + "where t.id = :id")
                .setParameter("flag", false)
                .setParameter("id", id)
                .executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    public List<Task> findActive() {
        Session session = sf.openSession();
        session.beginTransaction();
        List<Task> taskList = session.createQuery("from Task t where t.done = :flag")
                .setParameter("flag", false)
                .getResultList();
        session.getTransaction().commit();
        session.close();
        return taskList;
    }

    public List<Task> findDone() {
        Session session = sf.openSession();
        session.beginTransaction();
        List<Task> taskList = session.createQuery("from Task t where t.done = :flag")
                .setParameter("flag", true)
                .getResultList();
        session.getTransaction().commit();
        session.close();
        return taskList;
    }
}
