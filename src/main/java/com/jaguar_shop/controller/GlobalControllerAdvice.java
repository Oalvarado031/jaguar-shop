package com.jaguar_shop.controller;

import com.jaguar_shop.modelo.Usuario;
import com.jaguar_shop.repository.UsuarioRepository;
import com.jaguar_shop.security.UsuarioPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Expone el nombre del usuario logueado a TODAS las vistas.
 * - Sesión nueva: lo toma del UsuarioPrincipal (sin consultar BD).
 * - Sesión vieja u otro principal: lo busca en la BD por correo.
 */
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    private final UsuarioRepository usuarioRepository;

    @ModelAttribute("usuarioNombre")
    public String usuarioNombre(Authentication authentication) {
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof UsuarioPrincipal up) {
            return up.getNombre();
        }
        // Fallback (p. ej. sesión previa al cambio): resolver el nombre por correo
        return usuarioRepository.findByCorreo(authentication.getName())
                .map(Usuario::getNombre)
                .orElse(authentication.getName());
    }
}
