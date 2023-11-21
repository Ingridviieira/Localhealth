package br.com.fiap.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.models.Diagnostico;

public interface DiagnosticoRepository extends JpaRepository<Diagnostico, Long> {

}
