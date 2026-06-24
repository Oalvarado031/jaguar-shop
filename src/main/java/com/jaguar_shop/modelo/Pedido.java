package com.jaguar_shop.modelo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "pedido")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "nombre_cliente", nullable = false, length = 150)
    private String nombreCliente;

    @Column(name = "correo", nullable = false, length = 150)
    private String correo;

    @Column(name = "comentario", length = Integer.MAX_VALUE)
    private String comentario;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "fecha_pedido", nullable = false)
    private Instant fechaPedido;

    @ColumnDefault("0")
    @Column(name = "total", nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetallePedido> detalles = new ArrayList<>();

    /** Mantiene la relacion bidireccional consistente. */
    public void agregarDetalle(DetallePedido detalle) {
        detalles.add(detalle);
        detalle.setPedido(this);
    }

    @PrePersist
    void prePersist() {
        if (fechaPedido == null) fechaPedido = Instant.now();
        if (total == null) total = BigDecimal.ZERO;
    }

    /** Fecha lista para mostrar en las vistas (evita formatear un Instant directo). */
    @Transient
    public String getFechaTexto() {
        if (fechaPedido == null) return "";
        return java.time.LocalDateTime
                .ofInstant(fechaPedido, java.time.ZoneId.systemDefault())
                .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

}
