package com.afulvio.booklify.controller;

import com.afulvio.booklify.data.GlobalData;
import com.afulvio.booklify.model.Book;
import com.afulvio.booklify.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class CartController {

    @Autowired
    private BookService bookService;

    @GetMapping("/addToCart/{id}")
    public String addToCart(@PathVariable Long id) {
        GlobalData.cart.add(bookService.getBookById(id).get());
        return "redirect:/shop";
    }

    @GetMapping("/cart")
    public String getCart(Model model) {
        model.addAttribute("cartCount", GlobalData.cart.size());
        model.addAttribute("total", GlobalData.cart.stream().mapToDouble(Book::getPrice));
        model.addAttribute("cart", GlobalData.cart);
        return "cart";
    }

    @GetMapping("/cart/removeItem/{id}")
    public String cartItemRemove(@PathVariable int id) {
        GlobalData.cart.remove(id);
        return "redirect:/cart";
    }

    @GetMapping("/checkout")
    public String checkout(Model model) {
        model.addAttribute("total", GlobalData.cart.stream().mapToDouble(Book::getPrice));
        return "checkout";
    }
}
