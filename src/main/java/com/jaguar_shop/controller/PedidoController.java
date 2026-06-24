package com.jaguar_shop.controller;

import com.jaguar_shop.dto.CheckoutRequest;
import com.jaguar_shop.modelo.Pedido;
import com.jaguar_shop.modelo.Usuario;
import com.jaguar_shop.repository.UsuarioRepository;
import com.jaguar_shop.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;
    private final UsuarioRepository usuarioRepository;

    /** Mis pedidos */
    @GetMapping
    public String listar(Model model, Authentication auth) {
        model.addAttribute("pedidos", pedidoService.listarPorCorreo(auth.getName()));
        return "pedidos/lista";
    }

    /** Confirmacion / detalle de un pedido */
    @GetMapping("/{id}")
    public String confirmacion(@PathVariable Long id, Model model) {
        model.addAttribute("pedido", pedidoService.buscarPorId(id));
        return "pedidos/confirmacion";
    }

    /** Recibe el carrito del navegador (JSON) y guarda el pedido. */
    @PostMapping("/checkout")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> checkout(@RequestBody CheckoutRequest request,
                                                        Authentication auth) {
        try {
            String correo = auth.getName();
            String nombre = usuarioRepository.findByCorreo(correo)
                    .map(Usuario::getNombre)
                    .orElse(correo);

            Pedido pedido = pedidoService.crear(request, nombre, correo);
            return ResponseEntity.ok(Map.of("ok", true, "pedidoId", pedido.getId()));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("ok", false, "error", ex.getMessage()));
        }
    }
}
