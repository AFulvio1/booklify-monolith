package com.afulvio.booklify.controller;

import com.afulvio.booklify.data.GlobalData;
import com.afulvio.booklify.dto.OrderDTO;
import com.afulvio.booklify.model.Book;
import com.afulvio.booklify.model.Order;
import com.afulvio.booklify.repository.OrderRepository;
import com.afulvio.booklify.repository.UserRepository;
import com.afulvio.booklify.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class CartController {

    @Autowired
    private BookService bookService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/cart")
    public String getCart(Model model) {
        model.addAttribute("cartCount", GlobalData.cart.size());
        model.addAttribute("total", GlobalData.cart.stream().filter(b -> b.getPrice() > 0).mapToDouble(Book::getPrice).sum());
        model.addAttribute("cart", GlobalData.cart);
        return "cart";
    }

    @GetMapping("/addToCart/{id}")
    public String addToCart(@PathVariable Long id) {
        Book book;
        Optional<Book> opt = bookService.getBookById(id);
        book = opt.orElseGet(Book::new);
        GlobalData.cart.add(book);
        return "redirect:/shop";
    }

    @GetMapping("/cart/removeItem/{id}")
    public String cartItemRemove(@PathVariable int id) {
        GlobalData.cart.remove(id);
        return "redirect:/cart";
    }

    @GetMapping("/checkout")
    public String checkout(Model model) {
        model.addAttribute("total", GlobalData.cart.stream().filter(b -> b.getPrice() > 0).mapToDouble(Book::getPrice).sum());
        return "checkout";
    }


    @PostMapping("/payNow")
    public String postPayNow(@ModelAttribute(value = "orderDTO") OrderDTO orderDTO) {

        Order order = new Order();
        userRepository.findById(orderDTO.getUser().getId()).ifPresent(order::setUser);
        order.setNote(orderDTO.getNote());
        order.setCity(orderDTO.getCity());
        order.setEmail(orderDTO.getEmail());
        order.setFirstAddress(orderDTO.getFirstAddress());
        order.setSecondAddress(orderDTO.getSecondAddress());
        order.setFirstname(orderDTO.getFirstname());
        order.setLastname(orderDTO.getLastname());
        order.setNation(orderDTO.getNation());
        order.setPostCode(orderDTO.getPostCode());
        order.setTelephone(orderDTO.getTelephone());
        order.setTotal(orderDTO.getTotal());

        orderRepository.save(order);

        GlobalData.cart.clear();
        return "redirect:/index";
    }
}
