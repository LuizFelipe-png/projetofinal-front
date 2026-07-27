/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.main.front.TCC_front.service;

import com.main.front.TCC_front.model.IncidentesDTO;
import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

/**
 *
 * @author Win
 */
@Service
public class IncidenteService {

    private final RestClient restClient;

    public IncidenteService() {
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:9000")
                .build();
    }

    public List<IncidentesDTO> listarIncidentes(String token) {
        return restClient.get()
                .uri("/industria/incidentes")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(new ParameterizedTypeReference<List<IncidentesDTO>>() {});
    }

    public void cadastrarIncidente(String token, IncidentesDTO incidente) {
        restClient.post()
                .uri("/industria/incidentes")
                .header("Authorization", "Bearer " + token)
                .body(incidente)
                .retrieve()
                .toBodilessEntity();
    }
}
