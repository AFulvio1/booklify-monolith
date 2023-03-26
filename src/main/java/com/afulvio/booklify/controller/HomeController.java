package com.afulvio.booklify.controller;

import com.afulvio.booklify.data.GlobalData;
import com.afulvio.booklify.dto.SearchDTO;
import com.afulvio.booklify.model.Book;
import com.afulvio.booklify.model.Order;
import com.afulvio.booklify.model.User;
import com.afulvio.booklify.service.BookService;
import com.afulvio.booklify.service.CategoryService;
import com.afulvio.booklify.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;

@Slf4j
@Controller
public class HomeController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BookService bookService;
    @Autowired
    private OrderService orderService;


    @GetMapping({"/","/home"})
    public String home(Model model, HttpSession session) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = "";
        if (principal instanceof User) {
            username = ((User) principal).getUsername();
        }

        Set<Book> recommendedBooks = getRecommendedBoooks(session);
        Set<Book> lastPurchaseBooks = getLastPurchaseBooks(username);

        model.addAttribute("cartCount", GlobalData.cart.size());
        model.addAttribute("recommendedBooks", recommendedBooks);
        model.addAttribute("lastPurchaseBooks", lastPurchaseBooks);
        model.addAttribute("sessionId", session.getId());
        return "index";
    }

    @GetMapping("/shop")
    public String shop(
            Model model,
            @ModelAttribute(value = "searchDTO") SearchDTO searchDTO
    ){
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("books", bookService.getAllBooks());
        model.addAttribute("cartCount", GlobalData.cart.size());
        model.addAttribute("searchDTO", new SearchDTO());
        return "shop";
    }

    @GetMapping("/shop/category/{id}")
    public String shopByCategory(
            Model model,
            @PathVariable Integer id,
            @ModelAttribute(value = "searchDTO") SearchDTO searchDTO
    ){

        log.info("Search books by category...");

        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("books", bookService.getAllBooksByCategory(id));
        model.addAttribute("cartCount", GlobalData.cart.size());
        return "shop";
    }

    @GetMapping("/shop/viewbook/{id}")
    public String viewBook(
            Model model,
            @PathVariable Long id,
            @ModelAttribute(value = "searchDTO") SearchDTO searchDTO,
            HttpServletRequest request
    ){

        Book book;
        Optional<Book> opt = bookService.getBookById(id);
        book = opt.orElseGet(Book::new);

        Integer categoryId = book.getCategory().getId();

        if (categoryId != null) {
            List<Integer> favoriteCategory = getRecommendedCategories(request.getSession());
            favoriteCategory.add(categoryId);
            request.getSession().setAttribute("favoriteCategory", favoriteCategory);
        }

        model.addAttribute("book", book);
        model.addAttribute("cartCount", GlobalData.cart.size());
        return "viewBook";
    }

    @GetMapping("/shop/title")
    public String shopByTitle(Model model, @ModelAttribute(value = "searchDTO") SearchDTO searchDTO, Errors errors){

        log.info("Search books by title...");

        if (errors.hasErrors()) {
            log.info("There is an error, ending search");
        }
        else {
            List<Book> books = bookService.getAllBooksByTitle(searchDTO.getKeyword());
            if (books.isEmpty()) {
                books = bookService.getAllBooks();
            }
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("books", books);
            model.addAttribute("cartCount", GlobalData.cart.size());
            model.addAttribute("searchDTO", searchDTO);
        }
        return "shop";
    }

    @GetMapping("/shop/author")
    public String shopByAuthor(Model model, @ModelAttribute(value = "searchDTO") SearchDTO searchDTO, Errors errors){

        log.info("Search books by author...");

        if (errors.hasErrors()) {
            log.info("There is an error, ending search");
        }
        else {
            List<Book> books = bookService.getAllBooksByAuthor(searchDTO.getKeyword());
            if (books.isEmpty()) {
                books = bookService.getAllBooks();
            }
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("books", books);
            model.addAttribute("cartCount", GlobalData.cart.size());
            model.addAttribute("searchDTO", searchDTO);
        }
        return "shop";
    }

    private List<Integer> getRecommendedCategories(HttpSession session) {
        List<Integer> favoriteCategory = (List<Integer>) session.getAttribute("favoriteCategory");

        if (favoriteCategory == null) {
            favoriteCategory = new ArrayList<>();
        }
        return favoriteCategory;
    }

    private Set<Book> getRecommendedBoooks(HttpSession session) {
        List<Integer> favoriteCategory = getRecommendedCategories(session);
        Set<Book> recommendedBooks = new HashSet<>();
        if (!CollectionUtils.isEmpty(favoriteCategory)) {
            for (Integer id : favoriteCategory) {
                recommendedBooks.addAll(bookService.getAllBooksByCategory(id));
            }
        }
        return recommendedBooks;
    }

    private Set<Book> getLastPurchaseBooks(String username) {
        Set<Long> ids = new HashSet<>();
        List<Order> orders = orderService.getAllOrderByEmail(username);
        for (Order order : orders) {
            String listOfBookIds = order.getListOfBookIds();
            if (listOfBookIds != null && !listOfBookIds.isEmpty() && !listOfBookIds.isBlank()) {
                List<Long> list = Arrays.stream(listOfBookIds.split(",")).map(s -> Long.parseLong(s.trim())).toList();
                ids.addAll(list);
            }
        }
        Set<Book> books = new HashSet<>();
        for (Long id: ids) {
            bookService.getBookById(id).ifPresent(books::add);
        }
        return books;
    }
}
