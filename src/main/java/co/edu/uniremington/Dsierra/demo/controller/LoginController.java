package co.edu.uniremington.Dsierra.demo.controller;

import co.edu.uniremington.Dsierra.demo.modelo.Usuario;
import co.edu.uniremington.Dsierra.demo.service.UsuarioService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;

    public LoginController(UsuarioService usuarioService, PasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
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

        Usuario usuario = usuarioService.buscarPorCorreo(correo);

        if (usuario != null && passwordEncoder.matches(password, usuario.getPassword())) {
            session.setAttribute("usuario", usuario);

            if ("ADMIN".equals(usuario.getRol())) {
                return "redirect:/dashboard";
            } else {
                return "redirect:/catalogo";
            }
        } else if (usuario != null && usuario.getPassword().equals(password)) {
            usuario.setPassword(passwordEncoder.encode(password));
            usuarioService.guardar(usuario);
            session.setAttribute("usuario", usuario);

            if ("ADMIN".equals(usuario.getRol())) {
                return "redirect:/dashboard";
            } else {
                return "redirect:/catalogo";
            }
        } else {
            return "redirect:/login?error=true";
        }
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}