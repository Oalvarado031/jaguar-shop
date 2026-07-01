package com.jaguar_shop.security;

import com.jaguar_shop.modelo.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * UserDetails con datos extra del usuario (nombre, correo) para mostrarlos
 * en las vistas mediante sec:authentication="principal.nombre".
 */
public class UsuarioPrincipal extends User {

    private final String nombre;
    private final String correo;

    public UsuarioPrincipal(Usuario usuario, Collection<? extends GrantedAuthority> authorities) {
        super(
                usuario.getCorreo(),
                usuario.getPassword(),
                !(usuario.getActivo() != null && !usuario.getActivo()), // enabled
                true, true, true,
                authorities
        );
        this.nombre = usuario.getNombre();
        this.correo = usuario.getCorreo();
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }
}
