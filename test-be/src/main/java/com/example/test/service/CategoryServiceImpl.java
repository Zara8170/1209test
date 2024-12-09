package com.example.test.service;

import com.example.test.data.dao.CategoryDAO;
import com.example.test.data.dto.CategoryDTO;
import com.example.test.data.entity.Category;
import com.example.test.data.repository.CategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService{

  private final CategoryDAO categoryDAO;

  @Transactional(readOnly = true)
  @Override
  public List<CategoryDTO> getCategoryBooks(String name) {
    List<Category> categoryList = categoryDAO.getAllCategories(name);
    return categoryList.stream()
        .map(this::toDTO)
        .toList();
  }
}
