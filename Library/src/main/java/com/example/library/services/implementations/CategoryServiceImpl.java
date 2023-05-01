package com.example.library.services.implementations;

import com.example.library.models.Category;
import com.example.library.repositories.CategoryRepository;
import com.example.library.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repo;
    @Autowired
    public CategoryServiceImpl(CategoryRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<Category> findAll() {
        return repo.findAll();
    }

    @Override
    public Category save(Category category) {
        Category categorySave = new Category(category.getName());
        return repo.save(categorySave);
    }

    @Override
    public Optional<Category> findById(Long id) {
        return repo.findById(id);
    }

    @Override
    public Category update(Category category) {
        Category categoryUpdate = new Category();
        categoryUpdate.setName(category.getName());
        categoryUpdate.setActivated(category.isActivated());
        categoryUpdate.setDeleted(category.isDeleted());
        return repo.save(categoryUpdate);
    }

    @Override
    public void deleteById(Long id) {
        Optional<Category> category = repo.findById(id);
        if (category.isPresent()) {
            category.get().setDeleted(true);
            category.get().setActivated(false);
            repo.save(category.get());
        }
    }

    @Override
    public void enabledById(Long id) {
        Optional<Category> category = repo.findById(id);
        if (category.isPresent()) {
            category.get().setDeleted(false);
            category.get().setActivated(true);
            repo.save(category.get());
        }
    }
}
