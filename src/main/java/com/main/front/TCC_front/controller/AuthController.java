package com.main.front.TCC_front.controller;

import com.main.front.TCC_front.model.IncidentesDTO;
import com.main.front.TCC_front.model.OperadorDTO;
import com.main.front.TCC_front.model.UsuarioDTO;
import com.main.front.TCC_front.model.UsuarioRequestDTO;
import com.main.front.TCC_front.model.UsuarioResponseDTO;
import com.main.front.TCC_front.service.AuthService;
import com.main.front.TCC_front.service.IncidenteService;
import com.main.front.TCC_front.service.OperadorService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tools.jackson.databind.ObjectMapper;

@Controller
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private OperadorService operadorService;

    @Autowired
    private IncidenteService incidenteService;

    @GetMapping("/login")
    public String login(Model model) {
        UsuarioRequestDTO credenciais = new UsuarioRequestDTO();
        model.addAttribute("credenciais", credenciais);
        return "login";
    }

    @PostMapping("/logar")
    public String logar(@ModelAttribute UsuarioRequestDTO credenciais, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            UsuarioResponseDTO user = authService.logar(credenciais);
            session.setAttribute("token", user.getToken());

            if (user.getRole().equals("Operador Logistico")) {
                return "redirect:/industria";
            } else if (user.getRole().equals("Entregador")) {
                return "redirect:/entregador";
            } else {
                redirectAttributes.addFlashAttribute("erro", "Cargo inválido, tente novamente!");
                return "redirect:/login";
            }

        } catch (Exception e) {
            String mensagemErro = "Não foi possível conectar ao servidor de autenticação.";

            if (e instanceof HttpStatusCodeException httpEx) {
                try {
                    mensagemErro = new ObjectMapper()
                            .readTree(httpEx.getResponseBodyAsString())
                            .get("message").asText();
                } catch (Exception ex) {
                    mensagemErro = "Email ou senha inválidos.";
                }
            }

            redirectAttributes.addFlashAttribute("erroServidor", mensagemErro);
            return "redirect:/login";
        }
    }

    @GetMapping("/cadastrar")
    public String registrar(Model model) {
        UsuarioDTO newUser = new UsuarioDTO();
        model.addAttribute("user", newUser);
        return "cadastrar";
    }

    @PostMapping("/cadastrar")
    public String mandarRegistro(@ModelAttribute UsuarioDTO user, RedirectAttributes redirectAttributes) {
        try {
            authService.registrar(user);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Cadastro realizado com sucesso! Faça o login");
            return "redirect:/login";

        } catch (HttpStatusCodeException ex) {
            String mensagemErroDoBackend = ex.getResponseBodyAsString();

            if (mensagemErroDoBackend == null || mensagemErroDoBackend.trim().isEmpty()) {
                mensagemErroDoBackend = "Erro desconhecido no servidor.";
            } else if (mensagemErroDoBackend.contains("message")) {
                try {
                    mensagemErroDoBackend = new ObjectMapper()
                            .readTree(mensagemErroDoBackend)
                            .get("message").asText();
                } catch (Exception e) {
                    // Mantém o texto bruto caso falhe o parse do JSON
                }
            }

            redirectAttributes.addFlashAttribute("erroServidor", mensagemErroDoBackend);
            return "redirect:/cadastrar";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erroServidor", e.getMessage());
            return "redirect:/cadastrar";
        }
    }

    @GetMapping("/entregador")
    public String paginaEntregador(HttpSession session) {
        String token = (String) session.getAttribute("token");
        if (token == null) {
            return "redirect:/login";
        }
        return "entregador";
    }

    @GetMapping("/industria")
    public String paginaOperador(Model model, HttpSession session) {
        String token = (String) session.getAttribute("token");
        if (token == null) {
            return "redirect:/login";
        }
        model.addAttribute("lotes", operadorService.listarPedidos(token));
        return "industria";
    }

    @GetMapping("/novo-lote")
    public String novoLote(Model model, HttpSession session) {
        String token = (String) session.getAttribute("token");
        if (token == null) {
            return "redirect:/login";
        }
        model.addAttribute("operador", new OperadorDTO());
        return "novo_lote";
    }

    @PostMapping("/novo-lote")
    public String cadastrarLote(@ModelAttribute OperadorDTO operador, HttpSession session, RedirectAttributes redirectAttributes) {
        String token = (String) session.getAttribute("token");
        if (token == null) {
            return "redirect:/login";
        }
        operadorService.cadastrarLote(token, operador);
        redirectAttributes.addFlashAttribute("mensagemSucesso", "Lote cadastrado com sucesso!");
        return "redirect:/industria";
    }

    @GetMapping("/")
    public String telaCliente() {
        return "cliente";
    }

    @GetMapping("/rastreamento")
    public String telaRastreamento() {
        return "rastreamento";
    }

    @GetMapping("/notificacoes")
    public String telaNotificacoes() {
        return "notificacoes";
    }

    @GetMapping("/industria/incidentes")
    public String telaIncidentes(Model model, HttpSession session) {
        String token = (String) session.getAttribute("token");
        model.addAttribute("incidentes", incidenteService.listarIncidentes(token));
        model.addAttribute("lotes", operadorService.listarPedidos(token));
        model.addAttribute("incidente", new IncidentesDTO());
        return "incidentes";
    }

    @PostMapping("/industria/incidentes")
    public String cadastrarIncidente(@ModelAttribute IncidentesDTO incidente, HttpSession session, RedirectAttributes redirectAttributes) {
        String token = (String) session.getAttribute("token");
        incidenteService.cadastrarIncidente(token, incidente);
        redirectAttributes.addFlashAttribute("mensagemSucesso", "Incidente registrado com sucesso!");
        return "redirect:/industria/incidentes";
    }

    @GetMapping("/sair")
    public String sair(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
