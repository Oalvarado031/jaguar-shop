package com.jaguar_shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Páginas informativas de la tienda (footer "Ayuda").
 */
@Controller
public class InfoController {

    @GetMapping("/horarios")
    public String horarios() {
        return "paginas/horarios";
    }

    @GetMapping("/contacto")
    public String contacto() {
        return "paginas/contacto";
    }

    @GetMapping("/cambios")
    public String cambios() {
        return "paginas/cambios";
    }
}
