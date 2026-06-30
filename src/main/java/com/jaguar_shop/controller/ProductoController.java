package com.jaguar_shop.controller;

import com.jaguar_shop.modelo.Producto;
import com.jaguar_shop.repository.CategoriaRepository;
import com.jaguar_shop.service.ProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Controller
@RequestMapping("/productos")
@RequiredArgsConstructor
public class ProductoController {
    private final ProductoService productoService;
    private final CategoriaRepository categoriaRepository;

    @GetMapping
    public String listar(Model model){
        model.addAttribute("productos", productoService.listarTodos());
        return "productos/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model){
        model.addAttribute("producto", new Producto());
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "productos/formulario";
    }

    @PostMapping("/guardar")
    public String guardarProducto(@Valid @ModelAttribute Producto producto,
                                  BindingResult result, Model model,
                                  @RequestParam("archivoImagen")MultipartFile imagen) throws IOException {
        if(result.hasErrors()){
            model.addAttribute("categorias", categoriaRepository.findAll());
            return "productos/formulario";
        }

        if(!imagen.isEmpty()){
            String nombreArchivo = UUID.randomUUID() + "_" + imagen.getOriginalFilename();

            Path rutaCarpeta = Paths.get("uploads/productos");
            Files.createDirectories(rutaCarpeta);

            Path rutaArchivo = rutaCarpeta.resolve(nombreArchivo);
            Files.copy(imagen.getInputStream(), rutaArchivo, StandardCopyOption.REPLACE_EXISTING);

            producto.setImagen(nombreArchivo);
        }

        productoService.guardar(producto);
        return "redirect:/productos";
    }

    @GetMapping("/{id}")
    public String verDetalle(@PathVariable Long id, Model model){
        model.addAttribute("producto", productoService.buscarPorId(id));
        return "productos/detalle";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model)
    {
        model.addAttribute("producto", productoService.buscarPorId(id));
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "productos/formulario";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarProducto(@PathVariable Long id, @Valid @ModelAttribute Producto producto,
                                     BindingResult result, Model model){
        if(result.hasErrors()){
            model.addAttribute("categorias", categoriaRepository.findAll());
            return "productos/formulario";
        }

        productoService.actualizar(id, producto);
        return "redirect:/productos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id){
        productoService.eliminar(id);
        return "redirect:/productos";
    }


}
