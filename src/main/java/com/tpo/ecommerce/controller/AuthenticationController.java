package com.tpo.ecommerce.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tpo.ecommerce.dto.AuthenticationRequest;
import com.tpo.ecommerce.dto.AuthenticationResponse;
import com.tpo.ecommerce.dto.RegisterRequest;
import com.tpo.ecommerce.dto.UsuarioResponseDTO;
import com.tpo.ecommerce.entity.Usuario;
import com.tpo.ecommerce.service.AuthenticationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @GetMapping("/me")
    public ResponseEntity<UsuarioResponseDTO> me(
            @AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(service.me(usuario));
    }
}
