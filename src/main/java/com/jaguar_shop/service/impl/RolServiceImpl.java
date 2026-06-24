package com.jaguar_shop.service.impl;

import com.jaguar_shop.modelo.Rol;
import com.jaguar_shop.repository.RolRepository;
import com.jaguar_shop.service.RolService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RolServiceImpl implements RolService {

    private final RolRepository rolRepository;

    @Override
    public List<Rol> listarTodos() {
        return rolRepository.findAll();
    }

    @Override
    public Rol buscarPorId(Long id) {
        return rolRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Rol no encontrado"));
    }

    @Override
    public Rol guardar(Rol rol) {
        if (rolRepository.existsByNombre(rol.getNombre())) {
            throw new IllegalArgumentException("El rol ya existe");
        }

        return rolRepository.save(rol);
    }

    @Override
    public Rol actualizar(Long id, Rol rol) {
        Rol rolExistente = buscarPorId(id);

        rolExistente.setNombre(rol.getNombre());
        rolExistente.setDescripcion(rol.getDescripcion());

        return rolRepository.save(rolExistente);
    }

    @Override
    public void eliminar(Long id) {
        rolRepository.deleteById(id);
    }
}
