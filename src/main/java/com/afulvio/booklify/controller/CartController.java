package com.afulvio.booklify.controller;

import com.afulvio.booklify.data.GlobalData;
import com.afulvio.booklify.dto.OrderDTO;
import com.afulvio.booklify.model.Book;
import com.afulvio.booklify.model.Order;
import com.afulvio.booklify.model.User;
import com.afulvio.booklify.repository.OrderRepository;
import com.afulvio.booklify.repository.UserRepository;
import com.afulvio.booklify.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;
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
        BigDecimal total = BigDecimal.ZERO;
        for (Book book : GlobalData.cart) {
            total = total.add(book.getPrice());
        }
        model.addAttribute("cartCount", GlobalData.cart.size());
        model.addAttribute("total", total);
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
        BigDecimal total = BigDecimal.ZERO;
        for (Book book : GlobalData.cart) {
            total = total.add(book.getPrice());
        }
        model.addAttribute("total", total);
        model.addAttribute("orderDTO", new OrderDTO());
        model.addAttribute("cart", GlobalData.cart);
        return "checkout";
    }


    @PostMapping("/payNow")
    public String postPayNow(
            @ModelAttribute(value = "orderDTO") OrderDTO orderDTO
    ){

        Order order = new Order();

        StringBuilder listOfBookIds = new StringBuilder();
        for (Book book : GlobalData.cart){
            listOfBookIds.append(book.getId());
            listOfBookIds.append(",");
        }
        order.setListOfBookIds(listOfBookIds.toString());

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = "";
        if (principal instanceof User) {
            username = ((User) principal).getUsername();
        }
        userRepository.findByEmail(username).ifPresent(order::setUser);
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
        if (orderDTO.getTotal() == null)
            order.setTotal(BigDecimal.ZERO);
        else {
            order.setTotal(orderDTO.getTotal());
        }

        orderRepository.save(order);

        GlobalData.cart.clear();
        return "index";
    }
}
