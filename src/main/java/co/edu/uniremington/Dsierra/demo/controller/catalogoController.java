package co.edu.uniremington.Dsierra.demo.controller;

import co.edu.uniremington.Dsierra.demo.modelo.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class catalogoController {

    @GetMapping("/catalogo")
    public String catalogo(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario != null) {
            model.addAttribute("loggedIn", true);
            model.addAttribute("userName", usuario.getNombre());
            model.addAttribute("userRol", usuario.getRol());
        } else {
            model.addAttribute("loggedIn", false);
        }
        return "catalogo";
    }
}