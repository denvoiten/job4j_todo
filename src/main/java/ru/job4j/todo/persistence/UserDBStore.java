package ru.job4j.todo.persistence;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.models.User;

import java.util.Optional;

@Repository
public class UserDBStore implements StoreTransaction {
    private final SessionFactory sf;

    public UserDBStore(SessionFactory sf) {
        this.sf = sf;
    }

    public Optional<User> add(User user) {
        return Optional.of((User) transaction(session -> session.merge(user), sf));
    }

    public Optional<User> findUserByEmailAndPwd(String email, String password) {
        return transaction(session -> session
                        .createQuery("from User u where u.email = :email and u.password = :password")
                        .setParameter("email", email)
                        .setParameter("password", password)
                        .uniqueResultOptional(),
                sf);
    }
}
