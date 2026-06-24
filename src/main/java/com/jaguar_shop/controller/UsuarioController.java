package com.jaguar_shop.controller;

import com.jaguar_shop.modelo.Usuario;
import com.jaguar_shop.repository.RolRepository;
import com.jaguar_shop.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;
    private final RolRepository rolRepository;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "usuarios/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("roles", rolRepository.findAll());
        return "usuarios/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(
            @Valid @ModelAttribute Usuario usuario,
            BindingResult result,
            Model model
    ) {
        if (result.hasErrors()) {
            model.addAttribute("roles", rolRepository.findAll());
            return "usuarios/formulario";
        }

        usuarioService.guardar(usuario);
        return "redirect:/usuarios";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("usuario", usuarioService.buscarPorId(id));
        model.addAttribute("roles", rolRepository.findAll());
        return "usuarios/formulario";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(
            @PathVariable Long id,
            @Valid @ModelAttribute Usuario usuario,
            BindingResult result,
            Model model
    ) {
        if (result.hasErrors()) {
            model.addAttribute("roles", rolRepository.findAll());
            return "usuarios/formulario";
        }

        usuarioService.actualizar(id, usuario);
        return "redirect:/usuarios";
    }

    @GetMapping("/estado/{id}")
    public String cambiarEstado(@PathVariable Long id) {
        usuarioService.cambiarEstado(id);
        return "redirect:/usuarios";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return "redirect:/usuarios";
    }
}
