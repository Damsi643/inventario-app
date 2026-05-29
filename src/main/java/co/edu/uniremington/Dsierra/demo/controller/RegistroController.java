package co.edu.uniremington.Dsierra.demo.controller;

import co.edu.uniremington.Dsierra.demo.modelo.Usuario;
import co.edu.uniremington.Dsierra.demo.service.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistroController {

    private static final Logger log = LoggerFactory.getLogger(RegistroController.class);
    private final UsuarioService usuarioService;

    public RegistroController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
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


}