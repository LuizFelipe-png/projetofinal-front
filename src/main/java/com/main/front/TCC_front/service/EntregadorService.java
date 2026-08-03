/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.main.front.TCC_front.service;

import com.main.front.TCC_front.model.EntregadorDTO;
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
                .baseUrl("http://localhost:9000")
                .build();
    }
    
    public List<EntregadorDTO> listarPedidosPorEntregador(String token) {
        return restClient.get()
                .uri("/entregador/pedidos")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(new ParameterizedTypeReference<List<EntregadorDTO>>() {});
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
