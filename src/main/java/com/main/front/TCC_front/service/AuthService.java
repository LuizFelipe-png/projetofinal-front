package com.main.front.TCC_front.service;

import com.main.front.TCC_front.model.UsuarioDTO;
import com.main.front.TCC_front.model.UsuarioRequestDTO;
import com.main.front.TCC_front.model.UsuarioResponseDTO;
import java.util.Arrays;
import java.util.List;
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

    public UsuarioResponseDTO logar(UsuarioRequestDTO user) {
        return restClient.post()
                .uri("/auth/logar")
                .body(user)
                .retrieve()
                .body(UsuarioResponseDTO.class);
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
    
    public List<UsuarioDTO> listarEntregadores(){
        UsuarioDTO[] array = restClient.get()
                .uri("/auth/entregador")
                .retrieve()
                .body(UsuarioDTO[].class);
        return Arrays.asList(array);
    }
    
    
}