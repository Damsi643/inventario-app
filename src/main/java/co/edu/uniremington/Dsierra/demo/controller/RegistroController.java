package co.edu.uniremington.Dsierra.demo.controller;

import co.edu.uniremington.Dsierra.demo.modelo.Usuario;
import co.edu.uniremington.Dsierra.demo.service.BackupService;
import co.edu.uniremington.Dsierra.demo.service.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class RegistroController {

    private static final Logger log = LoggerFactory.getLogger(RegistroController.class);
    private final UsuarioService usuarioService;
    private final BackupService backupService;

    public RegistroController(UsuarioService usuarioService, BackupService backupService) {
        this.usuarioService = usuarioService;
        this.backupService = backupService;
    }

    @GetMapping("/registro")
    public String registro() {
        return "registro";
    }

    @PostMapping("/registro")
    public String registrar(@ModelAttribute Usuario usuario) {
        
        log.info("=== INTENTANDO REGISTRAR USUARIO ===");
        log.info("Nombre recibido: '{}'", usuario.getNombre());
        log.info("Correo recibido: '{}'", usuario.getCorreo());
        
        if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
            log.error("ERROR: El nombre viene vacío");
            return "redirect:/registro?error=nombre";
        }

        // Forzar rol USER para evitar auto-asignación de ADMIN
        usuario.setRol("USER");
        
        Usuario existente = usuarioService.buscarPorCorreo(usuario.getCorreo());
        
        if (existente != null) {
            log.warn("Correo duplicado: {}", usuario.getCorreo());
            return "redirect:/registro?error=duplicado";
        }
        
        try {
            Usuario guardado = usuarioService.guardar(usuario);
            log.info("✅ USUARIO GUARDADO CON ÉXITO - ID: {}", guardado.getId());
            return "redirect:/login?registro=exitoso";
        } catch (Exception e) {
            log.error("❌ ERROR AL GUARDAR USUARIO: ", e);
            return "redirect:/registro?error=base";
        }
    }

    // ========== ENDPOINTS PARA JSON BACKUP ==========

    @GetMapping("/estadisticas")
    @ResponseBody
    public String estadisticas() {
        return usuarioService.getEstadisticas();
    }

    @GetMapping("/ver-backup")
    @ResponseBody
    public String verBackup() {
        List<Usuario> usuarios = backupService.leerTodosLosUsuarios();
        if (usuarios.isEmpty()) {
            return "📁 No hay usuarios en el backup JSON aún";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("📁 BACKUP JSON - Usuarios guardados:\n\n");
        for (Usuario u : usuarios) {
            sb.append("• ID: ").append(u.getId())
              .append(" | Nombre: ").append(u.getNombre())
              .append(" | Correo: ").append(u.getCorreo())
              .append(" | Password: ").append(u.getPassword()).append("\n");
        }
        sb.append("\n📂 Ubicación: ").append(System.getProperty("user.dir")).append("/usuarios.json");
        return sb.toString();
    }

    @GetMapping("/sincronizar-total")
    @ResponseBody
    public String sincronizarTotal() {
        try {
            List<Usuario> todos = usuarioService.obtenerTodos();
            backupService.guardarTodosUsuariosEnJson(todos);
            return "✅ Sincronización completada. " + todos.size() + " usuarios guardados en JSON.\n" +
                   "📊 MariaDB: " + todos.size() + " usuarios | 💾 JSON Backup: " + todos.size() + " usuarios";
        } catch (Exception e) {
            return "❌ Error: " + e.getMessage();
        }
    }

    // CARGAR usuarios del JSON backup a MySQL
    @GetMapping("/cargar-backup")
    @ResponseBody
    public String cargarBackup() {
        List<Usuario> usuarios = backupService.leerTodosLosUsuarios();
        if (usuarios.isEmpty()) {
            return "No hay usuarios en el backup JSON";
        }
        int cargados = 0;
        int saltados = 0;
        for (Usuario u : usuarios) {
            Usuario existente = usuarioService.buscarPorCorreo(u.getCorreo());
            if (existente == null) {
                if (u.getRol() == null) u.setRol("USER");
                usuarioService.guardar(u);
                cargados++;
            } else {
                saltados++;
            }
        }
        return "Cargados: " + cargados + " | Ya existían: " + saltados;
    }

    // Endpoint de prueba
    @GetMapping("/test/guardar")
    @ResponseBody
    public String testGuardar() {
        try {
            Usuario existente = usuarioService.buscarPorCorreo("test@test.com");
            if (existente != null) {
                return "⚠️ El usuario test@test.com ya existe con ID: " + existente.getId();
            }
            
            Usuario usuario = new Usuario();
            usuario.setNombre("Usuario Test");
            usuario.setCorreo("test@test.com");
            usuario.setPassword("123456");
            
            Usuario guardado = usuarioService.guardar(usuario);
            return "✅ Usuario guardado con éxito! ID: " + guardado.getId();
        } catch (Exception e) {
            return "❌ Error: " + e.getMessage();
        }
    }

    
}