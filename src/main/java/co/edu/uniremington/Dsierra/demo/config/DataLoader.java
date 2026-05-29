package co.edu.uniremington.Dsierra.demo.config;

import co.edu.uniremington.Dsierra.demo.modelo.Usuario;
import co.edu.uniremington.Dsierra.demo.repository.UsuarioRepository;
import co.edu.uniremington.Dsierra.demo.service.BackupService;
import co.edu.uniremington.Dsierra.demo.service.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataLoader.class);
    private final UsuarioService usuarioService;
    private final BackupService backupService;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    public DataLoader(UsuarioService usuarioService,
                      BackupService backupService,
                      PasswordEncoder passwordEncoder,
                      UsuarioRepository usuarioRepository) {
        this.usuarioService = usuarioService;
        this.backupService = backupService;
        this.passwordEncoder = passwordEncoder;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void run(String... args) {
        List<Usuario> existentes = usuarioService.obtenerTodos();

        migrarPasswords(existentes);

        if (!existentes.isEmpty()) {
            log.info("BD ya tiene {} usuarios — se omite carga inicial", existentes.size());
            asegurarAdmin(existentes);
            return;
        }

        List<Usuario> usuarios = backupService.leerTodosLosUsuarios();
        int cargados = 0;

        for (Usuario u : usuarios) {
            if (u.getRol() == null || u.getRol().isBlank()) {
                u.setRol("USER");
            }
            usuarioService.guardar(u);
            cargados++;
        }

        log.info("Cargados {} usuarios desde JSON a la BD", cargados);
        asegurarAdmin(usuarioService.obtenerTodos());
    }

    private void migrarPasswords(List<Usuario> usuarios) {
        for (Usuario u : usuarios) {
            if (!usuarioService.isHashed(u.getPassword())) {
                log.info("Migrando password de {} a BCrypt", u.getCorreo());
                u.setPassword(passwordEncoder.encode(u.getPassword()));
                usuarioRepository.save(u);
            }
        }
    }

    private void asegurarAdmin(List<Usuario> existentes) {
        existentes.stream()
                .filter(u -> adminEmail.equals(u.getCorreo()))
                .findFirst()
                .ifPresentOrElse(admin -> {
                    boolean cambio = false;
                    if (!"ADMIN".equals(admin.getRol())) {
                        admin.setRol("ADMIN");
                        cambio = true;
                    }
                    if (!passwordEncoder.matches(adminPassword, admin.getPassword())) {
                        admin.setPassword(adminPassword);
                        cambio = true;
                    }
                    if (cambio) {
                        usuarioService.guardar(admin);
                        log.info("Admin actualizado: {}", adminEmail);
                    }
                }, () -> {
                    Usuario admin = new Usuario();
                    admin.setNombre("Admin");
                    admin.setCorreo(adminEmail);
                    admin.setPassword(adminPassword);
                    admin.setRol("ADMIN");
                    usuarioService.guardar(admin);
                    log.info("Admin creado: {}", adminEmail);
                });
    }
}
