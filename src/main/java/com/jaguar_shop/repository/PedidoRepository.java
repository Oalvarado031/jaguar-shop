package com.jaguar_shop.repository;

import com.jaguar_shop.modelo.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByCorreoOrderByFechaPedidoDesc(String correo);
}
