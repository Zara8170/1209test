package com.example.test.data.dao;

import com.example.test.data.entity.Category;
import com.example.test.data.repository.CategoryRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CategoryDAO {

  private final CategoryRepository repository;
  private final CategoryRepository categoryRepository;

  public List<Category> getAllCategories(String name) {
    if(name != null && !name.isEmpty()) {
      List<Category> categoryList = new ArrayList<>();
      categoryList.add(categoryRepository.findByNameWithBooks(name));
      return categoryList;
    }
    return categoryRepository.findAll();
  }
}
