package com.jaguar_shop.config;

import com.jaguar_shop.modelo.Rol;
import com.jaguar_shop.modelo.Usuario;
import com.jaguar_shop.repository.RolRepository;
import com.jaguar_shop.repository.UsuarioRepository;
import com.jaguar_shop.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;

/**
 * Siembra los roles base (ADMIN / USER) y un usuario administrador
 * la primera vez que arranca la aplicacion.
 *
 *   Login admin:  admin@uam.edu.ni  /  admin123
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;

    @Override
    public void run(String... args) {
        Rol rolAdmin = rolRepository.findByNombre("ADMIN").orElseGet(() -> {
            Rol r = new Rol();
            r.setNombre("ADMIN");
            r.setDescripcion("Administrador de la tienda");
            return rolRepository.save(r);
        });

        rolRepository.findByNombre("USER").orElseGet(() -> {
            Rol r = new Rol();
            r.setNombre("USER");
            r.setDescripcion("Cliente registrado");
            return rolRepository.save(r);
        });

        if (!usuarioRepository.existsByCorreo("admin@uam.edu.ni")) {
            Usuario admin = new Usuario();
            admin.setNombre("Administrador Jaguar");
            admin.setCorreo("admin@uam.edu.ni");
            admin.setPassword("admin123");          // se encripta dentro de usuarioService.guardar
            admin.setActivo(true);
            admin.setRoles(new HashSet<>(java.util.List.of(rolAdmin)));
            usuarioService.guardar(admin);
        }
    }
}
