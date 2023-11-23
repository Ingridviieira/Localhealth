package br.com.fiap.controllers;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.exceptions.RestNotFoundException;
import br.com.fiap.models.Localizacao;
import br.com.fiap.repository.DiagnosticoRepository;
import br.com.fiap.repository.LocalizacaoRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/localizacao")
@Slf4j
@SecurityRequirement(name = "bearer-key")
@Tag(name = "T_Localizacao")
public class LocalizacaoController {

    @Autowired
    private LocalizacaoRepository localizacaoRepository;

    @Autowired
    private DiagnosticoRepository diagnosticoRepository;

    @Autowired
    PagedResourcesAssembler<Object> assembler;

    @GetMapping
    public PagedModel<EntityModel<Object>> index(@RequestParam(required = false) String busca, @ParameterObject @PageableDefault(size = 5) Pageable pageable) {
        var localizacao = (busca == null) ? 
            localizacaoRepository.findAll(pageable): 
            localizacaoRepository.findByNmCidadeContaining(busca, pageable);

        return assembler.toModel(localizacao.map(Localizacao::toEntityModel)); //HAL
    }

    @PostMapping
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "a despesa foi cadastrada com sucesso"),
        @ApiResponse(responseCode = "400", description = "os dados enviados são inválidos")
    })
    public ResponseEntity<EntityModel<Localizacao>> create(
            @RequestBody @Valid Localizacao localizacao,
            BindingResult result) {
        log.info("cadastrando localizacao: " + localizacao);
        localizacaoRepository.save(localizacao);
        localizacao.setDiagnostico(diagnosticoRepository.findById(localizacao.getDiagnostico().getId()).get());
        return ResponseEntity
            .created(localizacao.toEntityModel().getRequiredLink("self").toUri())
            .body(localizacao.toEntityModel());
    }

    @GetMapping("{id}")
    @Operation(
        summary = "Detalhes da localizacao",
        description = "Retornar os dados da localizacao de acordo com o id informado no path"
    )
    public EntityModel<Localizacao> show(@PathVariable Long id) {
        log.info("buscando localizacao: " + id);
        return getLocalizacao(id).toEntityModel();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Localizacao> destroy(@PathVariable Long id) {
        log.info("apagando localizacao: " + id);
        localizacaoRepository.delete(getLocalizacao(id));
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    public ResponseEntity<EntityModel<Localizacao>> update(
            @PathVariable Long id,
            @RequestBody @Valid Localizacao localizacao) {
        log.info("atualizando despesa: " + id);
        getLocalizacao(id);
        localizacao.setId(id);
        localizacaoRepository.save(localizacao);
        return ResponseEntity.ok(localizacao.toEntityModel());
    }

    private Localizacao getLocalizacao(Long id) {
        return localizacaoRepository.findById(id).orElseThrow(
                () -> new RestNotFoundException("Localização não encontrada"));
    }

}
