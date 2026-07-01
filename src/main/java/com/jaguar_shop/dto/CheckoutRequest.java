package com.jaguar_shop.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Lo que el carrito del navegador (jaguar.js) envia al finalizar la compra:
 * datos de envio + items. El precio se recalcula en el servidor desde la BD.
 */
@Getter
@Setter
public class CheckoutRequest {

    private String nombre;
    private String telefono;
    private String tipoEntrega;   // RETIRO | ENVIO
    private String direccion;
    private String comentario;
    private List<Item> items;

    @Getter
    @Setter
    public static class Item {
        private Long id;
        private Integer qty;
    }
}
