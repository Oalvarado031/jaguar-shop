package com.jaguar_shop.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Lo que el carrito del navegador (jaguar.js) envia al finalizar la compra.
 * Solo manda id y cantidad; el precio se recalcula en el servidor desde la BD.
 */
@Getter
@Setter
public class CheckoutRequest {

    private String comentario;
    private List<Item> items;

    @Getter
    @Setter
    public static class Item {
        private Long id;
        private Integer qty;
    }
}
