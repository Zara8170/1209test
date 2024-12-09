package com.example.test.service;

import com.example.test.data.dto.BookDTO;
import com.example.test.data.entity.Book;
import java.util.List;

public interface BookService {

  List<BookDTO> getAllBooks(Long categoryId);

  BookDTO getBook(long id);

  BookDTO createBook(BookDTO bookDTO);

  BookDTO updateBook(long id, BookDTO bookDTO);

  void deleteBook(long id);

  // entity -> dto
  default BookDTO toDTO(Book book) {
    return BookDTO.builder()
        .id(book.getId())
        .title(book.getTitle())
        .imgsrc(book.getImgsrc())
        .categoryId(book.getCategory() != null ? book.getCategory().getId() : null)
        .categoryName(book.getCategory() != null ? book.getCategory().getName() : null)
        .build();
  }

  // dto -> entity
  default Book toEntity(BookDTO bookDTO) {
    String imageUrl = switch (bookDTO.getCategoryId().intValue()) {
      case 1 -> "https://via.placeholder.com/150x150/FFFF00";
      case 2 -> "https://via.placeholder.com/150x150/00FF00";
      case 3 -> "https://via.placeholder.com/150x150/0000FF";
      default -> "https://via.placeholder.com/150x150/CCCCCC";
    };

    return Book.builder()
        .title(bookDTO.getTitle())
        .imgsrc(imageUrl)
        .build();
  }
}
