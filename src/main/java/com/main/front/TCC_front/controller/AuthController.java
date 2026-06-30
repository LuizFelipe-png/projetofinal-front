package com.main.front.TCC_front.controller;

import com.main.front.TCC_front.model.UsuarioDTO;
import com.main.front.TCC_front.model.UsuarioRequestDTO;
import com.main.front.TCC_front.service.AuthService;
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
    
    @GetMapping("/home")
    public String home() {
        return "home"; 
    }
    
    @GetMapping("/login") 
    public String login(Model model){
        UsuarioRequestDTO credenciais = new UsuarioRequestDTO();
        model.addAttribute("credenciais", credenciais); 
        return "login";
    }
    
    @PostMapping("/logar")
    public String logar(@ModelAttribute UsuarioRequestDTO credenciais, HttpSession session){
        String token = authService.logar(credenciais);
        System.out.println("token: " + token);
        session.setAttribute("token", token);
        
        return "redirect:/home";
    }
    
    @GetMapping("/cadastrar")
    public String registrar(Model model){
        UsuarioDTO newUser = new UsuarioDTO();
        model.addAttribute("user", newUser); 
        return "cadastrar";
    }
    
    @PostMapping("/cadastrar")
    public String mandarRegistro(@ModelAttribute UsuarioDTO user, RedirectAttributes redirectAttributes){
        try {
            authService.registrar(user);
            
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Cadastro realizado com sucesso! Faça o login.");
            return "redirect:/login";
            
        } catch (HttpStatusCodeException ex) {
            try {
                String mensagemErroDoBackend = new ObjectMapper()
                        .readTree(ex.getResponseBodyAsString())
                        .get("message").asText(); 
                
                redirectAttributes.addFlashAttribute("erroServidor", mensagemErroDoBackend);
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("erroServidor", "Erro ao processar resposta do servidor.");
            }
           
            return "redirect:/cadastrar"; 
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erroServidor", e.getMessage());
            return "redirect:/cadastrar";
        }
    }
}