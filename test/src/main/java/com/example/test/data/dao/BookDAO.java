package com.example.test.data.dao;

import com.example.test.data.entity.Book;
import com.example.test.data.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookDAO {

  private final BookRepository bookRepository;

  public List<Book> getAllBooks() {
    return bookRepository.findAll();
  }

  public Book getBook(long id) {
    return bookRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Book not found"));
  }

  public Book addBook(Book book) {
    return bookRepository.save(book);
  }

  public Book updateBook(Long id, Book book) {
    Book updateBook = this.getBook(id);

    updateBook.setTitle(book.getTitle());

    return updateBook;
  }

  public void deleteBook(Long id) {
    bookRepository.deleteById(id);
  }

  public List<Book> getAllBooksByCategory(Long categoryId) {
    return bookRepository.findByCategory(categoryId);
  }

}
