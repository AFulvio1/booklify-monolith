package com.afulvio.booklify.controller;

import com.afulvio.booklify.dto.BookDTO;
import com.afulvio.booklify.model.Book;
import com.afulvio.booklify.model.Category;
import com.afulvio.booklify.service.BookService;
import com.afulvio.booklify.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Controller
public class AdminController {

    public static String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/bookImages";

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BookService bookService;

    public AdminController() {
    }

    @GetMapping("/admin")
    public String adminHome() {
        return "adminHome";
    }

    @GetMapping("/admin/categories")
    public String getCategories(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        return "categories";
    }

    @GetMapping("/admin/categories/add")
    public String getCategoriesAdd(Model model) {
        model.addAttribute("category", new Category());
        return "categoriesAdd";
    }

    @PostMapping("/admin/categories/add")
    public String postCategoriesAdd(@ModelAttribute("category") Category category) {
        categoryService.addCategory(category);
        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/categories/delete/{id}")
    public String getCategoriesDeleteById(@PathVariable Integer id) {
        categoryService.deleteCategoryById(id);
        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/categories/update/{id}")
    public String getCategoriesUpdateById(@PathVariable Integer id, Model model) {
        Optional<Category> category = categoryService.updateCategoryById(id);
        if (category.isPresent()) {
            model.addAttribute("category", category.get());
            return "categoriesAdd";
        }
        else {
            return "404";
        }
    }

    @GetMapping("/admin/books")
    public String getBooks(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "books";
    }

    @GetMapping("/admin/books/add")
    public String getBooksAdd(Model model) {
        model.addAttribute("bookDTO", new BookDTO());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "booksAdd";
    }

    @PostMapping("/admin/books/add")
    public String postBooksAdd(@ModelAttribute(value = "bookDTO") BookDTO bookDTO,
                               @RequestParam(value = "bookImage") MultipartFile file) throws IOException {

        Book book = new Book();
        categoryService.getCategoryById(bookDTO.getCategory().getId()).ifPresent(book::setCategory);
        book.setPrice(bookDTO.getPrice());
        book.setAuthor(bookDTO.getAuthor());
        book.setTitle(bookDTO.getTitle());
        book.setSubtitle(bookDTO.getSubtitle());
        book.setVolume(bookDTO.getVolume());
        book.setYearOfPublication(bookDTO.getYearOfPublication());
        book.setPublishingHouse(bookDTO.getPublishingHouse());
        book.setPlaceOfPublication(bookDTO.getPlaceOfPublication());
        book.setIsbn(bookDTO.getIsbn());
        book.setNote(bookDTO.getNote());

        String imageUUID = "";
        if (!file.isEmpty()) {
            imageUUID = file.getOriginalFilename();
            Path fileNameAndPath = Paths.get(uploadDir, imageUUID);
            try {
                Files.write(fileNameAndPath, file.getBytes());
            } catch (AccessDeniedException ade) {
                ade.printStackTrace();
            }
        }
        book.setImageName(imageUUID);

        bookService.addBook(book);

        return "redirect:/admin/books";
    }

    @GetMapping("/admin/books/delete/{id}")
    public String getBooksDeleteById(@PathVariable Long id) {
        bookService.removeBookById(id);
        return "redirect:/admin/books";
    }

    @GetMapping("/admin/books/update/{id}")
    public String getBooksUpdateById(
            @PathVariable Long id,
            Model model
    ){

        Optional<Book> opt = bookService.getBookById(id);
        Book book = opt.orElseGet(Book::new);

        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(book.getId());
        bookDTO.setCategory(book.getCategory());
        bookDTO.setPrice(book.getPrice());
        bookDTO.setAuthor(book.getAuthor());
        bookDTO.setTitle(book.getTitle());
        bookDTO.setSubtitle(book.getSubtitle());
        bookDTO.setVolume(book.getVolume());
        bookDTO.setYearOfPublication(book.getYearOfPublication());
        bookDTO.setPublishingHouse(book.getPublishingHouse());
        bookDTO.setPlaceOfPublication(book.getPlaceOfPublication());
        bookDTO.setIsbn(book.getIsbn());
        bookDTO.setNote(book.getNote());
        bookDTO.setImageName(book.getImageName());

        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("bookDTO", bookDTO);

        return "booksAdd";
    }
}
