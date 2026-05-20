package co.edu.uniremington.Dsierra.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import co.edu.uniremington.Dsierra.demo.modelo.Usuario;
import co.edu.uniremington.Dsierra.demo.repository.UsuarioRepository;

@Controller
public class RegistroController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/registro")
    public String registro() {
        return "registro";
    }

    @PostMapping("/registro")
    public String registrar(Usuario usuario) {

        usuarioRepository.save(usuario);

        return "redirect:/login";
    }
}