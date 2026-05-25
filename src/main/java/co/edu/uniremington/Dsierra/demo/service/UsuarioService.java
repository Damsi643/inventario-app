package co.edu.uniremington.Dsierra.demo.service;

import co.edu.uniremington.Dsierra.demo.modelo.Usuario;
import co.edu.uniremington.Dsierra.demo.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private static final Logger log = LoggerFactory.getLogger(UsuarioService.class);
    private final UsuarioRepository usuarioRepository;
    private final BackupService backupService;

    public UsuarioService(UsuarioRepository usuarioRepository, BackupService backupService) {
        this.usuarioRepository = usuarioRepository;
        this.backupService = backupService;
    }

    public Usuario guardar(Usuario usuario) {
        Usuario guardado = usuarioRepository.save(usuario);
        log.info("✅ Usuario guardado en MariaDB - ID: {}", guardado.getId());
        backupService.guardarUsuarioEnJson(guardado);
        return guardado;
    }

    public Usuario buscarPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }
    
    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }
    
    public boolean eliminar(Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            log.info("✅ Usuario eliminado - ID: {}", id);
            return true;
        }
        return false;
    }

    public String getEstadisticas() {
        long bdCount = usuarioRepository.count();
        long jsonCount = backupService.contarUsuariosEnJson();
        return String.format("📊 MariaDB: %d usuarios | 💾 JSON Backup: %d usuarios", bdCount, jsonCount);
    }
}