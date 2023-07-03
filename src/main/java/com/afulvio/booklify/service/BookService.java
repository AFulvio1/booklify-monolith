package com.afulvio.booklify.service;

import com.afulvio.booklify.model.Book;
import com.afulvio.booklify.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private BookRepository bookRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public void addBook(Book book) {
        bookRepository.save(book);
    }

    public void removeBookById(Long id) {
        bookRepository.deleteById(id);
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public List<Book> getAllBooksByCategory(int id) {
        return bookRepository.findAllByCategory_id(id);
    }

    public List<Book> getAllBooksByTitle(String keyword) {
        return bookRepository.findByTitleContaining(keyword);
    }

    public List<Book> getAllBooksByAuthor(String keyword) {
        return bookRepository.findByAuthorContaining(keyword);
    }

    @Autowired
    public void setBookRepository(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
}
