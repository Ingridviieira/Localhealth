package br.com.fiap.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


import br.com.fiap.models.Credencial;
import br.com.fiap.models.Medico;

import br.com.fiap.repository.MedicoRepository;
import br.com.fiap.service.TokenService;

import jakarta.validation.Valid;


public class MedicoController {

    @Autowired
    MedicoRepository repository;

    @Autowired
    AuthenticationManager manager;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    TokenService tokenService;

    @PostMapping("/api/registrar")
    public ResponseEntity<Medico> registrar(@RequestBody @Valid Medico medico){
        medico.setSenha(encoder.encode(medico.getSenha()));
        repository.save(medico);
        return ResponseEntity.ok(medico);
    }

    @PostMapping("/api/login")
    public ResponseEntity<Object> login(@RequestBody Credencial credencial){
        manager.authenticate(credencial.toAuthentication());
        var token = tokenService.generateToken(credencial);
        return ResponseEntity.ok(token);
    }
}