package co.edu.uniremington.Dsierra.demo.service;

import co.edu.uniremington.Dsierra.demo.modelo.Usuario;
import co.edu.uniremington.Dsierra.demo.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {

    private static final Logger log = LoggerFactory.getLogger(UsuarioService.class);
    private final UsuarioRepository usuarioRepository;
    private final BackupService backupService;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          BackupService backupService,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.backupService = backupService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Usuario guardar(Usuario usuario) {
        if (!isHashed(usuario.getPassword())) {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }
        Usuario guardado = usuarioRepository.save(usuario);
        log.info("✅ Usuario guardado en MariaDB - ID: {}", guardado.getId());
        backupService.guardarUsuarioEnJson(guardado);
        return guardado;
    }

    public boolean isHashed(String password) {
        return password != null && password.startsWith("$2");
    }

    public Usuario buscarPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }
    
    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }
    
    @Transactional
    public Usuario cambiarRol(Long id, String nuevoRol) {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario != null) {
            usuario.setRol(nuevoRol);
            Usuario guardado = usuarioRepository.save(usuario);
            log.info(" Rol cambiado - ID: {}, Nuevo rol: {}", id, nuevoRol);
            return guardado;
        }
        return null;
    }

    @Transactional
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