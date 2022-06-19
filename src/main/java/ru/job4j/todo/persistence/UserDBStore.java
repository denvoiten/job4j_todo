package ru.job4j.todo.persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.models.User;

import java.util.Optional;
import java.util.function.Function;

@Repository
public class UserDBStore {
    private final SessionFactory sf;

    public UserDBStore(SessionFactory sf) {
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

    public Optional<User> add(User user) {
        User rsl = (User) this.transaction(session -> session.merge(user));
        return rsl == null ? Optional.empty() : Optional.of(rsl);
    }

    public Optional<User> findUserByEmailAndPwd(String email, String password) {
        return this.transaction(session -> session
                .createQuery("from User u where u.email = :email and u.password = :password")
                .setParameter("email", email)
                .setParameter("password", password)
                .uniqueResultOptional());
    }
}
