package com.jaguar_shop.service;

import com.jaguar_shop.dto.CheckoutRequest;
import com.jaguar_shop.modelo.Pedido;

import java.util.List;

public interface PedidoService {

    Pedido crear(CheckoutRequest request, String nombreCliente, String correo);

    List<Pedido> listarPorCorreo(String correo);

    Pedido buscarPorId(Long id);
}
