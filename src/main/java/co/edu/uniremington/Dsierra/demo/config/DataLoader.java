package co.edu.uniremington.Dsierra.demo.config;

import co.edu.uniremington.Dsierra.demo.modelo.Usuario;
import co.edu.uniremington.Dsierra.demo.service.BackupService;
import co.edu.uniremington.Dsierra.demo.service.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataLoader.class);
    private final UsuarioService usuarioService;
    private final BackupService backupService;

    public DataLoader(UsuarioService usuarioService, BackupService backupService) {
        this.usuarioService = usuarioService;
        this.backupService = backupService;
    }

    @Override
    public void run(String... args) {
        List<Usuario> existentes = usuarioService.obtenerTodos();

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

    private void asegurarAdmin(List<Usuario> existentes) {
        existentes.stream()
                .filter(u -> "admin@gmail.com".equals(u.getCorreo()))
                .findFirst()
                .ifPresentOrElse(admin -> {
                    boolean cambio = false;
                    if (!"ADMIN".equals(admin.getRol())) {
                        admin.setRol("ADMIN");
                        cambio = true;
                    }
                    if (!"admin123".equals(admin.getPassword())) {
                        admin.setPassword("admin123");
                        cambio = true;
                    }
                    if (cambio) {
                        usuarioService.guardar(admin);
                        log.info("Admin actualizado: admin@gmail.com / admin123");
                    }
                }, () -> {
                    Usuario admin = new Usuario();
                    admin.setNombre("Admin");
                    admin.setCorreo("admin@gmail.com");
                    admin.setPassword("admin123");
                    admin.setRol("ADMIN");
                    usuarioService.guardar(admin);
                    log.info("Admin creado: admin@gmail.com / admin123");
                });
    }
}
