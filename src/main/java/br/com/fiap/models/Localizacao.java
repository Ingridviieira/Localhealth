package br.com.fiap.models;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;

import br.com.fiap.controllers.LocalizacaoController;
import br.com.fiap.controllers.MedicoController;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_localizacao")
public class Localizacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nmCidade;

    private String nmEstado;

    private String nmRua;

    private String nmBairro;

    @ManyToOne
    @JoinColumn(name = "diagnostico_id")
    private Diagnostico diagnostico;

    public EntityModel<Localizacao> toEntityModel() {
        return EntityModel.of(this,
        linkTo(methodOn(LocalizacaoController.class).show(id)).withSelfRel(),
        linkTo(methodOn(LocalizacaoController.class).destroy(id)).withRel("delete"),
        linkTo(methodOn(LocalizacaoController.class).index(null, Pageable.unpaged())).withRel("all"),
        linkTo(methodOn(LocalizacaoController.class).show(this.getDiagnostico().getId())).withRel("diagnostico")
        );
    }

}