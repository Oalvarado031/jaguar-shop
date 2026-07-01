package com.jaguar_shop.controller;

import com.jaguar_shop.security.UsuarioPrincipal;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Expone el nombre del usuario logueado a TODAS las vistas de forma segura.
 * Si el principal no es un UsuarioPrincipal (p. ej. una sesión vieja),
 * cae al nombre de usuario (correo) en vez de romper la página.
 */
@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute("usuarioNombre")
    public String usuarioNombre(Authentication authentication) {
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof UsuarioPrincipal up) {
            return up.getNombre();
        }
        return authentication.getName();
    }
}
