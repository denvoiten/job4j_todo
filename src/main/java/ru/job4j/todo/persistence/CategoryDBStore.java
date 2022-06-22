package ru.job4j.todo.persistence;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.models.Category;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class CategoryDBStore implements StoreTransaction {
    private final SessionFactory sf;

    public CategoryDBStore(SessionFactory sf) {
        this.sf = sf;
    }

    public List<Category> findAll() {
        return transaction(session -> session
                        .createQuery("FROM Category ORDER BY id")
                        .getResultList(),
                sf);
    }

    private Category findById(int id) {
        return (Category) transaction(session -> session
                .createQuery("FROM Category WHERE id = :id")
                .setParameter("id", id)
                .uniqueResult(), sf);
    }

    public Set<Category> getCategories(Set<Integer> categoryID) {
        Set<Category> categories = new HashSet<>();
        for (Integer id : categoryID) {
            categories.add(findById(id));
        }
        return categories;
    }
}
