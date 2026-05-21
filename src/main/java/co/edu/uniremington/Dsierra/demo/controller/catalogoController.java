package co.edu.uniremington.Dsierra.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class catalogoController {

  
    @GetMapping("/catalogo")
    public String catalogo() {
        return "catalogo"; 
    }
}