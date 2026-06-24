package com.jaguar_shop.controller;

import com.jaguar_shop.modelo.Rol;
import com.jaguar_shop.modelo.Usuario;
import com.jaguar_shop.repository.RolRepository;
import com.jaguar_shop.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashSet;

/**
 * Vistas propias de inicio de sesion y registro de Jaguar Shop.
 * El registro crea un usuario real en la tabla 'usuario' con rol USER.
 */
@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;
    private final RolRepository rolRepository;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/registro")
    public String registro() {
        return "registro";
    }

    @PostMapping("/registro")
    public String registrar(@RequestParam String nombre,
                            @RequestParam String correo,
                            @RequestParam String password) {
        try {
            Rol rolUser = rolRepository.findByNombre("USER")
                    .orElseGet(() -> {
                        Rol r = new Rol();
                        r.setNombre("USER");
                        r.setDescripcion("Cliente registrado");
                        return rolRepository.save(r);
                    });

            Usuario usuario = new Usuario();
            usuario.setNombre(nombre);
            usuario.setCorreo(correo);
            usuario.setPassword(password);          // se encripta en usuarioService.guardar
            usuario.setActivo(true);
            usuario.setRoles(new HashSet<>(java.util.List.of(rolUser)));

            usuarioService.guardar(usuario);
            return "redirect:/login?registered";
        } catch (IllegalArgumentException ex) {
            // correo ya registrado u otro problema de validacion
            return "redirect:/registro?error";
        }
    }
}
