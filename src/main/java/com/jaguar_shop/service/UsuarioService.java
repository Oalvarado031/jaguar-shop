package com.jaguar_shop.service;

import com.jaguar_shop.modelo.Usuario;

import java.util.List;

public interface UsuarioService {
    List<Usuario> listarTodos();

    Usuario buscarPorId(Long id);

    Usuario guardar(Usuario usuario);

    Usuario actualizar(Long id, Usuario usuario);

    void eliminar(Long id);

    void cambiarEstado(Long id);
}
