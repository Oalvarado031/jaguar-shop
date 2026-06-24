package com.jaguar_shop.service.impl;

import com.jaguar_shop.modelo.Producto;
import com.jaguar_shop.repository.ProductoRepository;
import com.jaguar_shop.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {
    private final ProductoRepository productoRepository;

    @Override
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    @Override
    public Producto buscarPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(
                        () -> new IllegalArgumentException("No existe el " +
                                "producto con el id: " + id)
                );
    }

    @Override
    public Producto guardar(Producto producto) {
        return productoRepository.save(producto);
    }

    @Override
    public Producto actualizar(Long id, Producto producto) {
        Producto productoExistente = buscarPorId(id);
        productoExistente.setNombre(producto.getNombre());
        productoExistente.setDescripcion(producto.getDescripcion());
        productoExistente.setCategoria(producto.getCategoria());
        productoExistente.setPrecio(producto.getPrecio());
        productoExistente.setImagen(producto.getImagen());

        return productoRepository.save(productoExistente);
    }

    @Override
    public void eliminar(Long id) {
        Producto producto = buscarPorId(id);
        productoRepository.delete(producto);
    }
}
