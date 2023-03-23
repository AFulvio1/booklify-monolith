package com.afulvio.booklify.controller;

import com.afulvio.booklify.data.GlobalData;
import com.afulvio.booklify.model.User;
import com.afulvio.booklify.repository.UserRepository;
import com.afulvio.booklify.request.RegisterRequest;
import com.afulvio.booklify.response.AuthenticationRequest;
import com.afulvio.booklify.response.AuthenticationResponse;
import com.afulvio.booklify.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@Controller
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public String register(RegisterRequest request) {
        authenticationService.register(request);
        return "redirect:/login";
    }

    @PostMapping("/login")
    public String authenticate(AuthenticationRequest request) {

        GlobalData.cart.clear();
        Optional<User> opt = userRepository.findByEmail(request.getEmail());
        User user = opt.orElseGet(User::new);
        String page = user.getRole().name().equals("ADMIN") ? "admin" : "shop";
        String redirect = "redirect:/";

        authenticationService.authenticate(request);

        return redirect.concat(page);
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/index";
    }
}
