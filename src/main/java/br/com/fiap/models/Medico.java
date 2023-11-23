package br.com.fiap.models;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.fiap.controllers.MedicoController;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
public class Medico implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String crm;
    private String nmMedico;
    private String email;
    private String senha;

   
    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return crm;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @ManyToOne
    Diagnostico diagnostico;

    public EntityModel<Medico> toEntityModel() {
        return EntityModel.of(this,
        linkTo(methodOn(MedicoController.class).show(id)).withSelfRel(),
        linkTo(methodOn(MedicoController.class).destroy(id)).withRel("delete"),
        linkTo(methodOn(MedicoController.class).index(null, Pageable.unpaged())).withRel("all"),
        linkTo(methodOn(MedicoController.class).show(this.getDiagnostico().getId())).withRel("diagnostico")
        );
    }

}

