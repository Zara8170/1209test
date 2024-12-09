package com.example.test.data.repository;

import com.example.test.data.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoryRepository extends JpaRepository<Category,Long> {

  @Query("select c from Category c left join fetch c.books where c.name = :name")
  Category findByNameWithBooks(@Param("name")String name);
}
