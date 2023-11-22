package br.com.fiap.controllers;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.exceptions.RestNotFoundException;
import br.com.fiap.models.Credencial;
import br.com.fiap.models.Medico;

import br.com.fiap.repository.MedicoRepository;
import br.com.fiap.service.TokenService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/medico")
@SecurityRequirement(name = "bearer-key")
public class MedicoController {

    @Autowired
    MedicoRepository repository;

    @Autowired
    AuthenticationManager manager;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    TokenService tokenService;

    @PostMapping("/registrar")
    public ResponseEntity<Medico> registrar(@RequestBody @Valid Medico medico) {
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

    @GetMapping
    public Page<Medico> index(@RequestParam(required = false) String busca, @PageableDefault(size = 5) Pageable pageable) {
        if (busca == null)
            return repository.findAll(pageable);
        return repository.findByCrmContaining(busca, pageable);
    }

    @GetMapping("{id}")
    public ResponseEntity<Medico> show(@PathVariable Long id) {
        log.info("buscando medico: " + id);
        return ResponseEntity.ok(getMedico(id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Medico> destroy(@PathVariable Long id) {
        log.info("apagando medico: " + id);
        repository.delete(getMedico(id));
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    public ResponseEntity<Medico> update(
            @PathVariable Long id,
            @RequestBody @Valid Medico medico) {
        log.info("atualizando medico: " + id);
        getMedico(id);
        medico.setId(id);
        repository.save(medico);
        return ResponseEntity.ok(medico);
    }

    private Medico getMedico(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new RestNotFoundException("médico não encontrado"));
    }
}

