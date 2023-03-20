package com.afulvio.booklify.controller;

import com.afulvio.booklify.response.AuthenticationRequest;
import com.afulvio.booklify.request.RegisterRequest;
import com.afulvio.booklify.data.GlobalData;
import com.afulvio.booklify.service.AuthenticationService;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(RegisterRequest request) {
        authenticationService.register(request);
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("/")).build();
    }

    @PostMapping("/login")
    public ResponseEntity<Void> authenticate(AuthenticationRequest request ) {
        GlobalData.cart.clear();
        authenticationService.authenticate(request);
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("/shop")).build();
    }
}
