package br.com.fiap.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.models.Doenca;
import br.com.fiap.models.Medico;

public interface DoencaRepository extends JpaRepository<Doenca, Long> {
    Page<Doenca> findByNmDoencaContaining(String busca, Pageable pageable);


}
