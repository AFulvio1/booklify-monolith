package com.afulvio.booklify.controller;

import com.afulvio.booklify.data.Role;
import com.afulvio.booklify.dto.UserDTO;
import com.afulvio.booklify.model.User;
import com.afulvio.booklify.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthenticationController {

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserRepository userRepository;

    @GetMapping("/login")
    public String getLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String postLogin() {
        return "login";
    }

    @GetMapping("/register")
    public String getRegister(Model model) {
        model.addAttribute("userDTO", UserDTO.builder().build());
        return "register";
    }

    @PostMapping("/register")
    public String postRegister(@ModelAttribute("userDTO") UserDTO userDTO, HttpServletRequest request) throws ServletException {

        User user = User.builder()
                .firstname(userDTO.getFirstname())
                .lastname((userDTO.getLastname()))
                .email(userDTO.getEmail())
                .password(bCryptPasswordEncoder.encode(userDTO.getPassword()))
                .role(Role.USER)
                .build();
        userRepository.save(user);

        return "redirect:/login";
    }

    @Autowired
    public void setBCryptPasswordEncoder(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Autowired
    public  void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
