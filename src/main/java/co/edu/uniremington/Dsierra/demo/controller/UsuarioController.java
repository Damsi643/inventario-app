package co.edu.uniremington.Dsierra.demo.controller;

import co.edu.uniremington.Dsierra.demo.modelo.Usuario;
import co.edu.uniremington.Dsierra.demo.repository.UsuarioRepository;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")

@CrossOrigin("*")

public class UsuarioController {

    private final UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // LISTAR USUARIOS
    @GetMapping
    public List<Usuario> listarUsuarios() {

        return usuarioRepository.findAll();

    }

}