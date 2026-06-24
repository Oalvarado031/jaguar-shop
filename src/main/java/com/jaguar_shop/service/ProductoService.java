package com.jaguar_shop.service;

import com.jaguar_shop.modelo.Producto;

import java.util.List;

public interface ProductoService {
    List<Producto> listarTodos();
    Producto buscarPorId(Long id);
    Producto guardar(Producto producto);
    Producto actualizar(Long id, Producto producto);
    void eliminar(Long id);
}
