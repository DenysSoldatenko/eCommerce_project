package com.example.library.services.implementations;

import com.example.library.models.Category;
import com.example.library.repositories.CategoryRepository;
import com.example.library.services.CategoryService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of the CategoryService interface.
 */
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
  public Optional<Category> findByName(String name) {
    return repo.findByName(name);
  }

  @Override
  public void update(Category category) {
    Optional<Category> categoryUpdate = repo.findById(category.getId());
    if (categoryUpdate.isPresent()) {
      categoryUpdate.get().setName(category.getName());
      categoryUpdate.get().setActivated(category.isActivated());
      categoryUpdate.get().setDeleted(category.isDeleted());
      repo.save(categoryUpdate.get());
    }
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

  @Override
  public List<Category> findAllByActivated() {
    return repo.findAllByActivated();
  }
}