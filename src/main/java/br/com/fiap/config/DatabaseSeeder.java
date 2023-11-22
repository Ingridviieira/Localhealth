package br.com.fiap.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import br.com.fiap.models.Diagnostico;
import br.com.fiap.repository.DiagnosticoRepository;
import br.com.fiap.repository.DoencaRepository;
import br.com.fiap.repository.LocalizacaoRepository;

@Configuration
public class DatabaseSeeder implements CommandLineRunner{
    
    @Autowired
    DiagnosticoRepository diagnosticoRepository;

    @Autowired
    DoencaRepository doencaRepository;

    @Autowired
    LocalizacaoRepository localizacaoRepository;

    @Override
    public void run(String... args) throws Exception {
        
        Diagnostico dg1 = new Diagnostico(1L,"05773110",11/11/2023, )
    
    }
}
