package com.example.test.controller;

import com.example.test.data.dto.BookDTO;
import com.example.test.exception.CustomException;
import com.example.test.service.BookService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/books")
public class BookController {

  private final BookService bookService;

  /**
   * 전체 목록, 카테고리 별 목록 출력
   * @param categoryId
   * @return 전체 목록, 카테고리 별 목록
   */
  @GetMapping
  @PreAuthorize("permitAll()")
  public ResponseEntity<List<BookDTO>> getAllBooks(@RequestParam(required = false) Long categoryId) {
    try {
      return ResponseEntity.ok(bookService.getAllBooks(categoryId));
    } catch (Exception e) {
      e.printStackTrace();
      throw new CustomException("Books not found");
    }
  }

  @PreAuthorize("permitAll()")
  @GetMapping("/{id}")
  public ResponseEntity<BookDTO> detail(@PathVariable long id) {
    try {
      return ResponseEntity.ok(bookService.getBook(id));
    } catch (Exception e) {
      e.printStackTrace();
      throw new CustomException("Book not found");
    }
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @PostMapping
  public ResponseEntity<BookDTO> createBook(@Valid @RequestBody BookDTO bookDTO) {
    try {
      BookDTO createdBook = bookService.createBook(bookDTO);
      return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
    } catch (Exception e) {
      e.printStackTrace();
      throw new CustomException("Book creation failed");
    }
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @PutMapping("/{id}")
  public ResponseEntity<BookDTO> updateBook(@Valid @PathVariable long id, @RequestBody BookDTO bookDTO) {
    try {
      BookDTO updatedBook = bookService.updateBook(id, bookDTO);
      return ResponseEntity.status(HttpStatus.OK).body(updatedBook);
    } catch (Exception e) {
      e.printStackTrace();
      throw new CustomException("Book update failed");
    }
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteBook(@PathVariable long id) {
    try {
      bookService.deleteBook(id);
      return ResponseEntity.status(HttpStatus.OK).build();
    } catch (Exception e) {
      e.printStackTrace();
      throw new CustomException("Book delete failed");
    }
  }

}
