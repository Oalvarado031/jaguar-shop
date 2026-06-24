package com.jaguar_shop.service.impl;

import com.jaguar_shop.dto.CheckoutRequest;
import com.jaguar_shop.modelo.DetallePedido;
import com.jaguar_shop.modelo.Pedido;
import com.jaguar_shop.modelo.Producto;
import com.jaguar_shop.repository.PedidoRepository;
import com.jaguar_shop.repository.ProductoRepository;
import com.jaguar_shop.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;

    @Override
    @Transactional
    public Pedido crear(CheckoutRequest request, String nombreCliente, String correo) {
        if (request == null || request.getItems() == null || request.getItems().isEmpty()) {
            throw new IllegalArgumentException("El carrito esta vacio");
        }

        Pedido pedido = new Pedido();
        pedido.setNombreCliente(nombreCliente);
        pedido.setCorreo(correo);
        pedido.setComentario(request.getComentario());
        pedido.setFechaPedido(Instant.now());

        BigDecimal total = BigDecimal.ZERO;

        for (CheckoutRequest.Item item : request.getItems()) {
            Producto producto = productoRepository.findById(item.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Producto no existe: " + item.getId()));

            int cantidad = (item.getQty() == null) ? 1 : Math.max(1, item.getQty());
            BigDecimal precioUnitario = producto.getPrecio();
            BigDecimal subtotal = precioUnitario.multiply(BigDecimal.valueOf(cantidad));

            DetallePedido detalle = new DetallePedido();
            detalle.setProducto(producto);
            detalle.setCantidad(cantidad);
            detalle.setPrecioUnitario(precioUnitario);
            detalle.setSubtotal(subtotal);

            pedido.agregarDetalle(detalle);
            total = total.add(subtotal);
        }

        pedido.setTotal(total);
        return pedidoRepository.save(pedido);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> listarPorCorreo(String correo) {
        return pedidoRepository.findByCorreoOrderByFechaPedidoDesc(correo);
    }

    @Override
    @Transactional(readOnly = true)
    public Pedido buscarPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado: " + id));
    }
}
