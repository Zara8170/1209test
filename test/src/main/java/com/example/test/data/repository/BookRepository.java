package com.example.test.data.repository;

import com.example.test.data.entity.Book;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book,Long> {

  @Query("select b from Book b where b.category.id = :categoryId")
  List<Book> findByCategory(@Param("categoryId") Long categoryId);
}
