package com.example.test.service;

import com.example.test.data.dao.BookDAO;
import com.example.test.data.dao.CategoryDAO;
import com.example.test.data.dto.BookDTO;
import com.example.test.data.entity.Book;
import com.example.test.data.entity.Category;
import com.example.test.data.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class BookServiceImpl implements BookService {

  private final BookDAO bookDAO;
  private final CategoryDAO categoryDAO;
  private final CategoryRepository categoryRepository;

  @Transactional(readOnly = true)
  @Override
  public List<BookDTO> getAllBooks(Long categoryId) {
    if(categoryId == null) {
      return bookDAO.getAllBooks().stream().map(this::toDTO).toList();
    }

    categoryRepository.findById(categoryId)
        .orElseThrow(() -> new EntityNotFoundException("Category not found categoryId: " + categoryId));

    return bookDAO.getAllBooksByCategory(categoryId).stream()
        .map(this::toDTO)
        .toList();

  }

  @Transactional(readOnly = true)
  @Override
  public BookDTO getBook(long id) {

    Book book = bookDAO.getBook(id);
    return toDTO(book);
  }

  @Transactional
  @Override
  public BookDTO createBook(BookDTO bookDTO) {

    Category category = bookDTO.getCategoryId() != null
        ? categoryDAO.getAllCategories(null).stream()
        .filter(e -> e.getId().equals(bookDTO.getCategoryId()))
        .findFirst()
        .orElseThrow(()-> new EntityNotFoundException("Category not found"))
        : null;

    Book book = toEntity(bookDTO);
    book.setCategory(category);

    Book saveBook = bookDAO.addBook(book);
    return toDTO(saveBook);
  }

  @Transactional
  @Override
  public BookDTO updateBook(long id, BookDTO bookDTO) {

    Category category = bookDTO.getCategoryId() != null
        ? categoryDAO.getAllCategories(null).stream()
        .filter(e -> e.getId().equals(bookDTO.getCategoryId()))
        .findFirst()
        .orElseThrow(()-> new EntityNotFoundException("Category not found"))
        : null;

    Book existingBook = bookDAO.getBook(id);

    String imageUrl = switch (bookDTO.getCategoryId().intValue())
    { case 1 -> "https://via.placeholder.com/150x150/000080";
      case 2 -> "https://via.placeholder.com/150x150/00FF00";
      case 3 -> "https://via.placeholder.com/150x150/0000FF";
      case 4 -> "https://via.placeholder.com/150x150/FFFF00";
      default -> "https://via.placeholder.com/150x150/CCCCCC";
    };

    existingBook.setTitle(bookDTO.getTitle());
    existingBook.setCategory(category);
    existingBook.setImgsrc(imageUrl);

    Book resultBook = bookDAO.updateBook(id, existingBook);
    return toDTO(resultBook);
  }

  @Transactional
  @Override
  public void deleteBook(long id) {
    bookDAO.deleteBook(id);
  }

}
