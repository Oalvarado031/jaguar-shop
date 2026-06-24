package com.jaguar_shop.service;

import com.jaguar_shop.modelo.Rol;

import java.util.List;

public interface RolService {
    List<Rol> listarTodos();

    Rol buscarPorId(Long id);

    Rol guardar(Rol rol);

    Rol actualizar(Long id, Rol rol);

    void eliminar(Long id);
}
