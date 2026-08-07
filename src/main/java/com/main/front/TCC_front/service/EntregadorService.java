/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.main.front.TCC_front.service;

import com.main.front.TCC_front.model.OperadorDTO;
import com.main.front.TCC_front.model.UsuarioDTO;
import java.util.List;
import java.util.Map;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

/**
 *
 * @author Aluno
 */
@Service
public class EntregadorService {

    private final RestClient restClient;

    public EntregadorService() {
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:8080")
                .build();
    }

    public List<OperadorDTO> listarPedidosPorEntregador(String token) {
        return restClient.get()
                .uri("/auth/pedidos") 
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(new ParameterizedTypeReference<List<OperadorDTO>>() {});
    }

    public List<UsuarioDTO> listarEntregadores(String token) {
        return restClient.get()
                .uri("/auth/listar-entregadores") 
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(new ParameterizedTypeReference<List<UsuarioDTO>>() {});
    }

    public void confirmarEntrega(String token, int idPedido, String tokenDigitado) {
        restClient.post()
                .uri("/entregador/confirmar") 
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("id_pedido", idPedido, "token", tokenDigitado))
                .retrieve()
                .toBodilessEntity();
    }
}