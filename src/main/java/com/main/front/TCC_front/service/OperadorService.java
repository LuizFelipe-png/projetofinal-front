/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.main.front.TCC_front.service;

import com.main.front.TCC_front.model.AtribuirEntregadorRequestDTO;
import com.main.front.TCC_front.model.OperadorDTO;
import java.util.Arrays;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 *
 * @author Win
 */
@Service
public class OperadorService {

    private final RestClient restClient;

    public OperadorService() {
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:9000")
                .build();
    }

    public List<OperadorDTO> listarPedidos(String token, @RequestHeader("Authorization") String auth) {
    return restClient.get()
            .uri("/industria/listar")
            .header("Authorization", "Bearer " + token)
            .retrieve()
            .body(new ParameterizedTypeReference<List<OperadorDTO>>() {});
}

    public void cadastrarLote(String token, OperadorDTO operador) {
        restClient.post()
                .uri("/industria/pedidos")
                .header("Authorization", "Bearer " + token)
                .body(operador)
                .retrieve()
                .toBodilessEntity();
    }
    
    public void atribuirEntregador(String token, AtribuirEntregadorRequestDTO entregador) {
    restClient.put()
            .uri("/industria/despachar")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .body(entregador)
            .retrieve()
            .toBodilessEntity();
}
    
    public List<OperadorDTO> listarPedidosPendentes(String token) {
    OperadorDTO[] array = restClient.get()
            .uri("/industria/pendentes")
            .header("Authorization", "Bearer " + token)
            .retrieve()
            .body(OperadorDTO[].class);

    return Arrays.asList(array);
}
}
