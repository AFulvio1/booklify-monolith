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

    private BookService bookService;
    private UserRepository userRepository;
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
        model.addAttribute("orderDTO", OrderDTO.builder().build());
        model.addAttribute("cart", GlobalData.cart);
        return "checkout";
    }


    @PostMapping("/payNow")
    public String postPayNow(
            @ModelAttribute(value = "orderDTO") OrderDTO orderDTO
    ){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = "";
        if (principal instanceof User) {
            username = ((User) principal).getUsername();
        }

        User user = new User();
        if (userRepository.findByEmail(username).isPresent()) {
            user = userRepository.findByEmail(username).get();
        }

        BigDecimal totale = new BigDecimal(0);
        if (orderDTO.getTotal() != null) {
            totale = orderDTO.getTotal();
        }

        Order order = Order.builder()
                .books(GlobalData.cart)
                .user(user)
                .note(orderDTO.getNote())
                .city(orderDTO.getCity())
                .email(orderDTO.getEmail())
                .firstAddress(orderDTO.getFirstAddress())
                .secondAddress(orderDTO.getSecondAddress())
                .firstname(orderDTO.getFirstname())
                .lastname((orderDTO.getLastname()))
                .nation(orderDTO.getNation())
                .postCode(orderDTO.getPostCode())
                .telephone(orderDTO.getTelephone())
                .total(totale)
                .build();

        orderRepository.save(order);

        GlobalData.cart.clear();
        return "index";
    }

    @Autowired
    public void setBookService(BookService bookService) {
        this.bookService = bookService;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setOrderRepository(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
}
