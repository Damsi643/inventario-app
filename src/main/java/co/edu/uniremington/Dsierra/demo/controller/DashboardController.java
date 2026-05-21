package co.edu.uniremington.Dsierra.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";  // Ahora existe admin.html
    }

    @GetMapping("/productos")
    public String productos() {
        return "dashboard";
    }

    @GetMapping("/categorias")
    public String categorias() {
        return "dashboard";
    }

    @GetMapping("/ventas")
    public String ventas() {
        return "dashboard";
    }

    @GetMapping("/stock")
    public String stock() {
        return "dashboard";
    }
}