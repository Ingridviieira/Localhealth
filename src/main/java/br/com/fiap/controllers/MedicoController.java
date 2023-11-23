package br.com.fiap.controllers;



import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
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
import br.com.fiap.repository.DiagnosticoRepository;
import br.com.fiap.repository.MedicoRepository;
import br.com.fiap.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    DiagnosticoRepository diagnosticoRepository;


    @Autowired
    AuthenticationManager manager;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    TokenService tokenService;

    @Autowired
    PagedResourcesAssembler<Object> assembler;

    @PostMapping("/registrar")
    public ResponseEntity<Medico> registrar(@RequestBody @Valid Medico medico) {
        medico.setSenha(encoder.encode(medico.getSenha()));
        repository.save(medico);
        return ResponseEntity.ok(medico);
}
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody Credencial credencial){
        manager.authenticate(credencial.toAuthentication());
        var token = tokenService.generateToken(credencial);
        return ResponseEntity.ok(token);
    }

    @GetMapping 
    public PagedModel<EntityModel<Object>> index(@RequestParam(required = false) String busca, @ParameterObject @PageableDefault(size = 5) Pageable pageable) {
        var medico = (busca == null)?
            repository.findAll(pageable):
            repository.findByCrmContaining(busca, pageable);

        return assembler.toModel(medico.map(Medico::toEntityModel));
    }

    @PostMapping
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "o medico foi cadastrado com sucesso"),
        @ApiResponse(responseCode = "400", description = "os dados enviados são inválidos")
    })

    public ResponseEntity<EntityModel<Medico>> create(
            @RequestBody @Valid Medico medico,
            BindingResult result) {
        log.info("cadastrando despesa: " + medico);
        repository.save(medico);
        medico.setDiagnostico(diagnosticoRepository.findById(medico.getDiagnostico().getId()).get());
        return ResponseEntity
            .created(medico.toEntityModel().getRequiredLink("self").toUri())
            .body(medico.toEntityModel());
    }

    @GetMapping("{id}")
    @Operation(
        summary = "Detalhes do medico",
        description = "Retornar os dados do medico de acordo com o id informado no path"
    )
    public EntityModel<Medico> show(@PathVariable Long id) {
        log.info("buscando medicos: " + id);
        return getMedico(id).toEntityModel();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Medico> destroy(@PathVariable Long id) {
        log.info("apagando medico: " + id);
        repository.delete(getMedico(id));
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    public ResponseEntity<EntityModel<Medico>> update(
            @PathVariable Long id,
            @RequestBody @Valid Medico medico) {
        log.info("atualizando medico: " + id);
        getMedico(id);
        medico.setId(id);
        repository.save(medico);
        return ResponseEntity.ok(medico.toEntityModel());
    }

    private Medico getMedico(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new RestNotFoundException("médico não encontrado"));
    }
}

