package br.com.fiap.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.models.Medico;

public interface MedicoRepository extends JpaRepository<Medico, Long> {
    Page<Medico> findByNmMedicoContaining(String busca, Pageable pageable);
}