package com.jaguar_shop.security;

import com.jaguar_shop.modelo.Usuario;
import com.jaguar_shop.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Autentica contra la tabla 'usuario'. El login usa el CORREO como usuario.
 * Cada rol se traduce a una autoridad "ROLE_xxx" (compatible con hasRole(...)).
 */
@Service
@RequiredArgsConstructor
public class JaguarUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + correo));

        List<GrantedAuthority> autoridades = usuario.getRoles().stream()
                .map(rol -> {
                    String nombre = rol.getNombre();
                    return new SimpleGrantedAuthority(nombre.startsWith("ROLE_") ? nombre : "ROLE_" + nombre);
                })
                .collect(Collectors.toList());

        boolean deshabilitado = usuario.getActivo() != null && !usuario.getActivo();

        return User.withUsername(usuario.getCorreo())
                .password(usuario.getPassword())
                .authorities(autoridades)
                .disabled(deshabilitado)
                .build();
    }
}
