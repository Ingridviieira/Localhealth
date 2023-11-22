package br.com.fiap.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import br.com.fiap.repository.MedicoRepository;

public class AuthenticationService implements UserDetailsService {
    
    @Autowired
    MedicoRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByCrm(username)
                    .orElseThrow( () -> new UsernameNotFoundException("Médico não encontrado"));
    }
}
