package com.main.front.TCC_front.service;

import com.main.front.TCC_front.model.UsuarioDTO;
import com.main.front.TCC_front.model.UsuarioRequestDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class AuthService {
    
    private final RestClient restClient;

    public AuthService() {
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:9000/api")
                .build();
    }

    public String logar(UsuarioRequestDTO user) {
        return restClient.post()
                .uri("/auth/logar")
                .body(user)
                .retrieve()
                .body(String.class);
    }
    
    public void registrar(UsuarioDTO user) {
        String retorno = 
            restClient
                .post()
                .uri("/auth/cadastrar")
                .body(user)
                .retrieve()
                .body(String.class);
    }
}