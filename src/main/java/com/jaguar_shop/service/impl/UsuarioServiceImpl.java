package com.jaguar_shop.service.impl;

import com.jaguar_shop.modelo.Usuario;
import com.jaguar_shop.repository.UsuarioRepository;
import com.jaguar_shop.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            throw new IllegalArgumentException("Ya existe un usuario con ese correo");
        }

        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setActivo(true);

        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario actualizar(Long id, Usuario usuario) {
        Usuario usuarioExistente = buscarPorId(id);

        usuarioExistente.setNombre(usuario.getNombre());
        usuarioExistente.setCorreo(usuario.getCorreo());
        usuarioExistente.setActivo(usuario.getActivo());
        usuarioExistente.setRoles(usuario.getRoles());

        if (usuario.getPassword() != null && !usuario.getPassword().isBlank()) {
            usuarioExistente.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }

        return usuarioRepository.save(usuarioExistente);
    }

    @Override
    public void eliminar(Long id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    public void cambiarEstado(Long id) {
        Usuario usuario = buscarPorId(id);
        usuario.setActivo(!usuario.getActivo());
        usuarioRepository.save(usuario);
    }
}
