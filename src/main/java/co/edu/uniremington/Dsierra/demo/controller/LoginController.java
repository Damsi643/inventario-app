package co.edu.uniremington.Dsierra.demo.controller;

import co.edu.uniremington.Dsierra.demo.modelo.Usuario;
import co.edu.uniremington.Dsierra.demo.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    private final UsuarioService usuarioService;

    public LoginController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(
            @RequestParam("correo") String correo,
            @RequestParam("password") String password,
            HttpSession session) {
        
        // Buscar usuario por correo
        Usuario usuario = usuarioService.buscarPorCorreo(correo);
        
        // Verificar si existe y la contraseña es correcta
        if (usuario != null && usuario.getPassword().equals(password)) {
            // Guardar usuario en sesión
            session.setAttribute("usuario", usuario);
            
            // Verificar si es admin por el rol
            if ("ADMIN".equals(usuario.getRol())) {
                return "redirect:/dashboard";
            } else {
                // Usuario normal → redirigir al catálogo
                return "redirect:/catalogo";
            }
        } else {
            // Credenciales incorrectas
            return "redirect:/login?error=true";
        }
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}