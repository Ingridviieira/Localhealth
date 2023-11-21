package br.com.fiap.models;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;

import br.com.fiap.controllers.MedicoController;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_medico")
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank @Size(min = 5, max = 200, message = "Digite o CRM")
    private int sgCrm;

    private String nmMedico;

    private String vlSenha;

    @ManyToOne
    private Diagnostico diagnostico;

    public EntityModel<Medico> toEntityModel() {
        return EntityModel.of(
            this,
            linkTo(methodOn(MedicoController.class).show(id)).withSelfRel(),
            linkTo(methodOn(MedicoController.class).destroy(id)).withRel("delete"),
            linkTo(methodOn(MedicoController.class).index(null, Pageable.unpaged())).withRel("all"),
            linkTo(methodOn(MedicoController.class).show(this.getDiagnostico().getId())).withRel("Diagnostico do Medico")
        );
    }


}
