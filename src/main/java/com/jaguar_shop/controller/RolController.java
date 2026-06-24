package com.jaguar_shop.controller;

import com.jaguar_shop.modelo.Rol;
import com.jaguar_shop.service.RolService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RolController {
    private final RolService rolService;

    @GetMapping
    public String listar(Model model) {

        model.addAttribute("roles", rolService.listarTodos());

        return "roles/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {

        model.addAttribute("rol", new Rol());

        return "roles/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(
            @Valid @ModelAttribute("rol") Rol rol,
            BindingResult result
    ) {

        if (result.hasErrors()) {
            return "roles/formulario";
        }

        rolService.guardar(rol);

        return "redirect:/roles";
    }

    @GetMapping("/editar/{id}")
    public String editar(
            @PathVariable Long id,
            Model model
    ) {

        model.addAttribute("rol",
                rolService.buscarPorId(id));

        return "roles/formulario";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(
            @PathVariable Long id,
            @Valid @ModelAttribute("rol") Rol rol,
            BindingResult result
    ) {

        if (result.hasErrors()) {
            return "roles/formulario";
        }

        rolService.actualizar(id, rol);

        return "redirect:/roles";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {

        rolService.eliminar(id);

        return "redirect:/roles";
    }
}
