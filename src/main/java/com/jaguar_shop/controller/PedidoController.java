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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;
    private final UsuarioRepository usuarioRepository;

    /** Página de checkout: datos de envío + resumen del carrito (que carga el JS). */
    @GetMapping("/checkout")
    public String checkout(Model model, Authentication auth) {
        usuarioRepository.findByCorreo(auth.getName()).ifPresent(u -> {
            model.addAttribute("nombreUsuario", u.getNombre());
            model.addAttribute("correoUsuario", u.getCorreo());
        });
        return "pedidos/checkout";
    }

    /** Recibe el carrito + datos de envío (JSON) y guarda el pedido. */
    @PostMapping("/checkout")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> procesarCheckout(@RequestBody CheckoutRequest request,
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

    /** Mis pedidos */
    @GetMapping
    public String listar(Model model, Authentication auth) {
        model.addAttribute("pedidos", pedidoService.listarPorCorreo(auth.getName()));
        return "pedidos/lista";
    }

    /** Panel de administración: todos los pedidos (solo ADMIN). */
    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("pedidos", pedidoService.listarTodos());
        model.addAttribute("estados", new String[]{"PENDIENTE", "PAGADO", "ENTREGADO", "CANCELADO"});
        return "pedidos/admin";
    }

    /** Cambiar el estado de un pedido (solo ADMIN). */
    @PostMapping("/admin/{id}/estado")
    public String cambiarEstado(@PathVariable Long id,
                                @RequestParam String estado,
                                RedirectAttributes redirect) {
        try {
            pedidoService.cambiarEstado(id, estado);
            redirect.addFlashAttribute("mensaje", "Pedido #" + id + " actualizado a " + estado);
        } catch (IllegalArgumentException ex) {
            redirect.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/pedidos/admin";
    }

    /** Confirmación / detalle de un pedido */
    @GetMapping("/{id}")
    public String confirmacion(@PathVariable Long id, Model model) {
        model.addAttribute("pedido", pedidoService.buscarPorId(id));
        return "pedidos/confirmacion";
    }
}
