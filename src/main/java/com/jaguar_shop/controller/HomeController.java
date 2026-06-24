package com.jaguar_shop.controller;

import com.jaguar_shop.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ProductoService productoService;

    @GetMapping("/")
    public String inicio(Model model) {

        model.addAttribute(
                "productosDestacados",
                productoService.listarTodos()
                        .stream()
                        .limit(4)
                        .toList()
        );

        return "home";
    }
}
