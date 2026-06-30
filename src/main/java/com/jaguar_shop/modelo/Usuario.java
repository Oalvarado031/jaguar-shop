package com.jaguar_shop.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 150)
    @NotNull
    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Size(max = 150)
    @NotNull
    @Column(name = "correo", nullable = false, length = 150)
    private String correo;

    @Size(max = 255)
    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    @NotNull
    @ColumnDefault("true")
    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "fecha_creacion", nullable = false)
    private Instant fechaCreacion;   // la asigna @PrePersist; no se valida en el form

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "usuario_rol",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    private Set<Rol> roles = new HashSet<>();

    @PrePersist
    void prePersist() {
        if (fechaCreacion == null) fechaCreacion = Instant.now();
        if (activo == null) activo = true;
    }

}
