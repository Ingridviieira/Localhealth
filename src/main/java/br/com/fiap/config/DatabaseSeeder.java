package br.com.fiap.config;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import br.com.fiap.models.Diagnostico;
import br.com.fiap.models.Doenca;
import br.com.fiap.models.Localizacao;
import br.com.fiap.models.Medico;
import br.com.fiap.repository.DiagnosticoRepository;
import br.com.fiap.repository.DoencaRepository;
import br.com.fiap.repository.LocalizacaoRepository;
import br.com.fiap.repository.MedicoRepository;

@Configuration

public class DatabaseSeeder implements CommandLineRunner{
    
    @Autowired
    DiagnosticoRepository diagnosticoRepository;

    @Autowired
    DoencaRepository doencaRepository;

    @Autowired
    LocalizacaoRepository localizacaoRepository;

    @Autowired
    MedicoRepository medicoRepository;

    @Override
    public void run(String... args) throws Exception {

        // Crie um médico
        Medico medico1 = Medico.builder().crm("").nmMedico("Dr. João").email("medico@gmail.com").senha("senha123").build();
        medicoRepository.save(medico1);

        // Crie uma doença
        Doenca doenca1 = Doenca.builder().nmDoenca("Gripe").dsSintomas("Febre, tosse, dor de cabeça").build();
        doencaRepository.save(doenca1);

        // Crie um diagnóstico associado ao médico e à doença
        Diagnostico diagnostico1 = Diagnostico.builder().nrCep("05773-110").dtDiagnostico(LocalDate.now()).medico(medico1).doenca(doenca1).build();
        diagnosticoRepository.save(diagnostico1);

        // Crie uma localização associada ao diagnóstico
        Localizacao localizacao1 = Localizacao.builder()
                .nmCidade("São Paulo")
                .nmEstado("SP")
                .nmRua("Rua Abc, 347")
                .nmBairro("Parque Regina")
                .diagnostico(diagnostico1)
                .build();
        localizacaoRepository.save(localizacao1);
    }
}

