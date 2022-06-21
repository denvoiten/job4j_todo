package ru.job4j.todo.services;

import org.springframework.stereotype.Service;
import ru.job4j.todo.models.Category;
import ru.job4j.todo.persistence.CategoryDBStore;

import java.util.Collection;
import java.util.Set;

@Service
public class CategoryService {
    private final CategoryDBStore categoryDBStore;

    public CategoryService(CategoryDBStore store) {
        this.categoryDBStore = store;
    }

    public Collection<Category> findAll() {
        return categoryDBStore.findAll();
    }

    public Set<Category> getCategories(Set<Integer> categoryID) {
        return categoryDBStore.getCategories(categoryID);
    }
}
