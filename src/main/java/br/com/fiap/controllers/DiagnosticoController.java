package br.com.fiap.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.exceptions.RestNotFoundException;
import br.com.fiap.models.Diagnostico;
import br.com.fiap.repository.DiagnosticoRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/diagnostico")
@Slf4j
public class DiagnosticoController {
    
    @Autowired
    DiagnosticoRepository repository;

    @GetMapping
    public List<Diagnostico> index(){
        log.info("buscando todas os diagnosticos");
        return repository.findAll();
    }

    @PostMapping
    public ResponseEntity<Diagnostico> create(
            @RequestBody @Valid Diagnostico diagnostico,
            BindingResult result
        ){
        log.info("cadastrando o diagnostico: " + diagnostico);
        repository.save(diagnostico);
        return ResponseEntity.status(HttpStatus.CREATED).body(diagnostico);
    }

    @GetMapping("{id}")
    public ResponseEntity<Diagnostico> show(@PathVariable Long id){
        log.info("buscando diagnostico: " + id);
        return ResponseEntity.ok(getDiagnostico(id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Diagnostico> destroy(@PathVariable Long id){
        log.info("apagando diagnostico: " + id);
        repository.delete(getDiagnostico(id));
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    public ResponseEntity<Diagnostico> update(
        @PathVariable Long id,
        @RequestBody @Valid Diagnostico diagnostico
    ){
        log.info("atualizando diagnostico: " + id);
        getDiagnostico(id);
        diagnostico.setId(id);
        repository.save(diagnostico);
        return ResponseEntity.ok(diagnostico);
    }

    private Diagnostico getDiagnostico(Long id) {
        return repository.findById(id).orElseThrow(
            () -> new RestNotFoundException("diagnostico não encontrado"));
    }

}

