package com.afulvio.booklify.controller;

import com.afulvio.booklify.service.BookService;
import com.afulvio.booklify.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HomeController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BookService bookService;

    @GetMapping({"/","/home"})
    public String home(Model model) {
        return "index";
    }

    @GetMapping("/shop")
    public String shop(Model model){
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("books", bookService.getAllBooks());
        return "shop";
    }

    @GetMapping("/shop/category/{id}")
    public String shopByCategory(Model model, @PathVariable int id){
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("books", bookService.getAllBooksByCategory(id));
        return "shop";
    }

    @GetMapping("/shop/viewbook/{id}")
    public String viewBook(Model model, @PathVariable Long id){
        model.addAttribute("book", bookService.getBookById(id).get());
        return "viewBook";
    }
}
