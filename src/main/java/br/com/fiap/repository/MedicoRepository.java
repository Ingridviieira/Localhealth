package br.com.fiap.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.models.Medico;


public interface MedicoRepository extends JpaRepository<Medico, Long> {
     Optional<Medico> findByCrm(String Crm);
}