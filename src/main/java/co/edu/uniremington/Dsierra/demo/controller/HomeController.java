package co.edu.uniremington.Dsierra.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String inicio() {
        return "index";
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }
}