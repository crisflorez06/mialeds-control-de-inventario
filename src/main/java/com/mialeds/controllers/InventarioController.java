package com.mialeds.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import com.mialeds.services.KardexService;
import com.mialeds.services.ProductoService;
import com.mialeds.services.UsuarioService;

import java.time.LocalDate;

@Controller
@RequestMapping("/inventario")
public class InventarioController extends UsuarioDatosController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private KardexService kardexService;

    @Autowired
    private UsuarioService usuarioService;

    private void addProductosEscasosToModel(Model model) {
        model.addAttribute("productosEsc", productoService.productosEscasos());
    }

    private void addKardexToModel(Model model) {
        model.addAttribute("kardexE", kardexService.listarPorMovimiento("entrada"));
        model.addAttribute("kardexS", kardexService.listarPorMovimiento("salida"));
    }

    @GetMapping("/listar")
    public String listar(Model model) {
        try {
            addProductosEscasosToModel(model);
            addKardexToModel(model);
            model.addAttribute("productos", productoService.listar());
        } catch (Exception e) {
            model.addAttribute("error", "Error al listar los productos: " + e.getMessage());
        }
        return "inventario";
    }

    @GetMapping("/buscar")
    public String buscarProducto(@RequestParam("producto") String nombre, Model model) {
        try {
            if (nombre == null || nombre.isEmpty()) {
                return "redirect:/inventario/listar";
            }
            model.addAttribute("productos", productoService.listarPorNombre(nombre));
            addProductosEscasosToModel(model);
        } catch (Exception e) {
            model.addAttribute("error", "Error al buscar el producto: " + e.getMessage());
        }
        return "inventario";
    }

    @PutMapping("/editar")
    public String editarProducto(@RequestParam("id_editar") int id, 
                                @RequestParam("nombre_editar") String nombre,
                                @RequestParam("presentacion_editar") String presentacion,
                                @RequestParam("precio_compra_editar") int precioCompra,
                                @RequestParam("precio_venta_editar") int precioVenta,
                                @RequestParam("cantidad_editar") int cantidad,
                                Model model) {
        try {
            productoService.actualizar(id, nombre, presentacion, precioCompra, precioVenta, cantidad);
        } catch (Exception e) {
            model.addAttribute("error", "Error al editar el producto: " + e.getMessage());
        }
        return "redirect:/inventario/listar";
    }

    @PostMapping("/nuevo")
    public String crearProducto(@RequestParam("nombre_producto_nuevo") String nombre, 
                              @RequestParam("presentacion_nuevo") String presentacion, 
                              @RequestParam("precio_compra_nuevo") int precioCompra, 
                              @RequestParam("precio_venta_nuevo") int precioVenta,
                              @RequestParam("cantidad_nuevo") int cantidad,
                              Model model) {
        try {
            productoService.crear(nombre, presentacion, precioCompra, precioVenta, cantidad);
        } catch (Exception e) {
            model.addAttribute("error", "Error al crear el producto: " + e.getMessage());
        }
        return "redirect:/inventario/listar";
    }

    @DeleteMapping("/eliminar")
    public String eliminarProducto(@RequestParam("id_eliminar") int id, Model model) {
        try {
            productoService.eliminar(id);
        } catch (Exception e) {
            model.addAttribute("error", "Error al eliminar el producto: " + e.getMessage());
        }
        return "redirect:/inventario/listar";
    }

    @PostMapping("/movimiento")
    public String movimientoProducto(@RequestParam("id_movimiento") int id, 
                                   @RequestParam("cantidad_movimiento") int cantidad, 
                                   @RequestParam("movimiento") String movimiento,
                                   @RequestParam("fecha_movimiento") String fecha,
                                   Model model) {
        try {
            productoService.movimiento(id, cantidad, movimiento);
            int idUsuario = usuarioService.obtenerIdUsuarioSesion();
            kardexService.crear(id, idUsuario, movimiento, LocalDate.parse(fecha), cantidad);
        } catch (Exception e) {
            model.addAttribute("error", "Error al registrar el movimiento del producto: " + e.getMessage());
        }
        return "redirect:/inventario/listar";
    }
}
