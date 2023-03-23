package com.afulvio.booklify.controller;

import com.afulvio.booklify.data.GlobalData;
import com.afulvio.booklify.model.Book;
import com.afulvio.booklify.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class CartController {

    @Autowired
    private BookService bookService;

    @GetMapping("/cart")
    public String getCart(Model model) {
        model.addAttribute("cartCount", GlobalData.cart.size());
        model.addAttribute("total", GlobalData.cart.stream().filter(b -> b.getPrice() > 0).mapToDouble(b -> b.getPrice()).sum());
        model.addAttribute("cart", GlobalData.cart);
        return "cart";
    }

    @GetMapping("/addToCart/{id}")
    public String addToCart(@PathVariable Long id) {
        Book book;
        Optional<Book> opt = bookService.getBookById(id);
        if (opt.isPresent()) {
            book = opt.get();
        }
        else {
            book = new Book();
        }
        GlobalData.cart.add(book);
        return "redirect:/shop";
    }

    @GetMapping("/cart/removeItem/{id}")
    public String cartItemRemove(@PathVariable Integer id) {
        GlobalData.cart.remove(id);
        return "redirect:/cart";
    }

    @GetMapping("/checkout")
    public String checkout(Model model) {
        model.addAttribute("total", GlobalData.cart.stream().filter(b -> b.getPrice() > 0).mapToDouble(b -> b.getPrice()).sum());
        return "checkout";
    }

    @PostMapping("/payNow")
    public String payNow() {
        GlobalData.cart.clear();
        return "redirect:/index";
    }
}
