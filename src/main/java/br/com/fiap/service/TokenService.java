package br.com.fiap.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;

import br.com.fiap.models.Credencial;
import br.com.fiap.models.Medico;
import br.com.fiap.models.Token;
import br.com.fiap.repository.MedicoRepository;

@Service
public class TokenService {

    @Autowired
    MedicoRepository medicoRepository;

    @Value("${jwt.secret}")
    String secret;

    public Token generateToken(Credencial credencial){
        Algorithm alg = Algorithm.HMAC256(secret);
        var token = JWT.create()
                    .withSubject(credencial.crm())
                    .withIssuer("Localhealth")
                    .withExpiresAt(Instant.now().plus(20, ChronoUnit.MINUTES))
                    .sign(alg);
        
        return new Token(token, "JWT", "Bearer");
    }

    public Medico getUserByToken(String token) {
        Algorithm alg = Algorithm.HMAC256(secret);
        var crm = JWT.require(alg)
                    .withIssuer("Localhealth")
                    .build()
                    .verify(token)
                    .getSubject();
                ;
        return medicoRepository.findByCrm(crm)
                    .orElseThrow(() -> new JWTVerificationException("Médico invalido"));
    }
}