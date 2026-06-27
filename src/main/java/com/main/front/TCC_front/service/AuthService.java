/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.main.front.TCC_front.service;

import com.main.front.TCC_front.model.UsuarioDTO;
import com.main.front.TCC_front.model.UsuarioRequestDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

/**
 *
 * @author Win
 */
@Service
public class AuthService {
    
    private final RestClient restClient;

    
    public AuthService() {
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:8082/api")
                .build();
    }

    public String logar(UsuarioRequestDTO user) {
        return restClient.post()
                .uri("/auth/login")
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