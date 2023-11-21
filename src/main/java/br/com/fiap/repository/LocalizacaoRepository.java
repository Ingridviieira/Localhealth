package br.com.fiap.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.models.Localizacao;
import br.com.fiap.models.Medico;

public interface LocalizacaoRepository extends JpaRepository<Localizacao, Long> {
    Page<Medico> findByNmCidadeContaining(String busca, Pageable pageable);
}

