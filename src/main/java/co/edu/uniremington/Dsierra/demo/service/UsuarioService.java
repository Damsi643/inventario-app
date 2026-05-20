package co.edu.uniremington.Dsierra.demo.service;

import co.edu.uniremington.Dsierra.demo.modelo.Usuario;
import co.edu.uniremington.Dsierra.demo.repository.UsuarioRepository;

import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario guardar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }
}