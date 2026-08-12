package com.main.front.TCC_front.service;

import com.main.front.TCC_front.model.HistoricoDTO;
import com.main.front.TCC_front.model.OperadorDTO;
import com.main.front.TCC_front.model.UsuarioDTO;
import java.util.List;
import java.util.Map;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class EntregadorService {

    private final RestClient restClient;

    public EntregadorService() {
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:9000")
                .build();
    }

    public List<OperadorDTO> listarPedidosPorEntregador(String token) {
        return restClient.get()
                .uri("/api/auth/pedidos")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(new ParameterizedTypeReference<List<OperadorDTO>>() {
                });
    }

    public List<UsuarioDTO> listarEntregadores(String token) {
        return restClient.get()
                .uri("/api/auth/listar-entregadores")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(new ParameterizedTypeReference<List<UsuarioDTO>>() {
                });
    }

    public void confirmarEntrega(String token, int idPedido, String tokenDigitado) {
        restClient.post()
                .uri("/api/auth/confirmar")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of(
                        "id_pedido", idPedido,
                        "token", tokenDigitado
                ))
                .retrieve()
                .toBodilessEntity();
    }

    public void registrarCheckpoint(String token, int idPedido, String descricao) {
        restClient.post()
                .uri("/api/auth/checkpoint")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("id_pedido", idPedido, "descricao", descricao))
                .retrieve()
                .toBodilessEntity();
    }

    public List<HistoricoDTO> listarHistoricoPublico(int idPedido) {
        return restClient.get()
                .uri("/api/auth/historico-publico/" + idPedido)
                .retrieve()
                .body(new ParameterizedTypeReference<List<HistoricoDTO>>() {
                });
    }

    public List<HistoricoDTO> listarHistoricoGeral() {
        return restClient.get()
                .uri("/api/auth/historico-geral")
                .retrieve()
                .body(new ParameterizedTypeReference<List<HistoricoDTO>>() {
                });
    }
}
