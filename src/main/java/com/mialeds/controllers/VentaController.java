package com.mialeds.controllers;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;

import com.mialeds.models.Venta;
import com.mialeds.services.VentaService;

@Controller
@RequestMapping("/venta")
public class VentaController extends UsuarioDatosController {

    @Autowired
    private VentaService ventaService;

    @GetMapping("/listar")
    public String listar(Model model) {
        try {
            List<Venta> ventas = ventaService.listarPorFecha(LocalDate.now());
            String totalVentasFormateado = ventaService.formatearTotalVentas(ventas);

            model.addAttribute("ventas", ventas);
            model.addAttribute("totalVentas", totalVentasFormateado);
        } catch (Exception e) {
            model.addAttribute("error", "Error al listar las ventas: " + e.getMessage());
        }
        return "venta";
    }

    @GetMapping("/buscar")
    public String buscarVenta(@RequestParam(value = "producto_nombre", required = false) String nombre,
                            @RequestParam(value = "fecha_busqueda", required = false) LocalDate fecha,
                            Model model) {
        try {
            List<Venta> ventas = ventaService.obtenerVentas(nombre, fecha);
            String totalVentasFormateado = ventaService.formatearTotalVentas(ventas);
    
            model.addAttribute("ventas", ventas);
            model.addAttribute("totalVentas", totalVentasFormateado);
        } catch (Exception e) {
            model.addAttribute("error", "Error al buscar las ventas: " + e.getMessage());
        }
        return "venta";
    }
    
    @PostMapping("/nuevo")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> crearVenta(@RequestParam("id_producto_venta") int idProducto, 
                                                      @RequestParam("cantidad_entrada_venta") int cantidad, 
                                                      @RequestParam("fecha_venta") LocalDate fecha) {
        Map<String, Object> response = new HashMap<>();
        try {
            Venta nuevaVenta = ventaService.guardar(idProducto, cantidad, fecha);
            
            if (nuevaVenta == null) {
                response.put("success", false);
                response.put("message", "Error al realizar la venta: cantidad insuficiente o producto no encontrado.");
            } else {
                response.put("success", true);
                response.put("message", "Venta realizada con éxito.");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al realizar la venta: " + e.getMessage());
        }
        return ResponseEntity.ok(response);
    }
    
   @PutMapping("/editar")
   public String editarVenta(@RequestParam("id_venta_editar") int id, 
                               @RequestParam("id_producto_venta") int idProducto,
                               @RequestParam("cantidad_editar_venta") int cantidad,
                               @RequestParam("cantidad_editar_total_venta") int totalVenta,
                               @RequestParam("fecha_editar_venta") LocalDate fecha,
                               Model model) {
        try {
            ventaService.actualizar(id, idProducto, cantidad, totalVenta, fecha);
        } catch (Exception e) {
            model.addAttribute("error", "Error al editar la venta: " + e.getMessage());
        }
        return "redirect:/venta/listar";
    }

    @DeleteMapping("/eliminar")
    public String eliminarVenta(@RequestParam("id_eliminar_venta") int id, Model model) {
        try {
            ventaService.eliminar(id);
        } catch (Exception e) {
            model.addAttribute("error", "Error al eliminar la venta: " + e.getMessage());
        }
        return "redirect:/venta/listar";
    }
}
    


