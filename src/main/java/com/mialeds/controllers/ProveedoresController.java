package com.mialeds.controllers;

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

import com.mialeds.services.ProveedorProductoService;
import com.mialeds.services.ProveedorService;

import java.util.HashMap;
import java.util.Map;


import org.springframework.ui.Model;

@Controller
@RequestMapping("/proveedor")
public class ProveedoresController extends UsuarioDatosController {

    // Inyección de dependencias
    @Autowired
    private ProveedorService proveedorService;

    @Autowired
    private ProveedorProductoService proveedorProductoService;


    @GetMapping("/listar")
    public String listarProveedores(Model model) {
        try {

            //este model lo utilizamos en la parte de vista de la primera tabla donde comparamos los precios de venta de los proveedores por cada producto
            model.addAttribute("productosAgrupados", proveedorProductoService.obtenerProductosAgrupados());
            //este model lo utilizamos en la parte de vista en el modal proveedores donde se listan los proveedores
            model.addAttribute("proveedores", proveedorService.listar());

        } catch(Exception e) {
            model.addAttribute("error", "Error al listar los proveedores: " + e.getMessage());
        }
        return "proveedor";
    }

    //metodo que me mostrara todos los precios que me ofrecen los proveedores por el producto seleccionado
    @GetMapping("/buscar")
    public String buscarProducto(@RequestParam("producto") String nombre, Model model) {
        try {
            //si el nombre del producto es nulo o vacio, entonces redirigimos a la lista de proveedores
            if (nombre == null || nombre.isEmpty()) {
                return "redirect:/proveedor/listar";
            } else {
                //si el nombre del producto no es nulo o vacio, entonces mostramos los precios de los proveedores por los productos que coincidan con el nombre
                model.addAttribute("productosAgrupados", proveedorProductoService.listarPornombre(nombre));
                model.addAttribute("proveedores", proveedorService.listar());
                
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error al buscar el producto: " + e.getMessage());
        }
        return "proveedor";
    }

    // Método para editar un producto
    @PutMapping("/editar")
    public String editarProveedor(
        @RequestParam("editar_id_proveedor") int id,
        @RequestParam("editar_nombre_proveedor") String nombre,
        @RequestParam("editar_nit_proveedor") String nit,
        @RequestParam("editar_correo_proveedor") String correo,
        @RequestParam("editar_telefono_proveedor") String telefono,
        Model model) {
        try {
            // Actualizar el producto por ID
            proveedorService.actualizar(id, nombre, nit, correo, telefono);
            model.addAttribute("mensaje", "Proveedor editado correctamente");
        } catch (Exception e) {
            model.addAttribute("error", "Error al editar el proveedor: " + e.getMessage());
        }
        return "redirect:/proveedor/listar";
    }

        // Método para crear un nuevo proveedor
    @PostMapping("/nuevo")
    public String crearProveedor(
        @RequestParam("nuevo_nombre_proveedor") String nombre,
        @RequestParam("nuevo_nit_proveedor") String nit,
        @RequestParam("nuevo_correo_proveedor") String correo,
        @RequestParam("nuevo_telefono_proveedor") String telefono,
        Model model) {
        try {
            proveedorService.crear(nombre, nit, correo, telefono); // Crear un nuevo proveedor
        } catch (Exception e) {
            model.addAttribute("error", "Error al crear el proveedor: " + e.getMessage());
        }
        return "redirect:/proveedor/listar"; 
    }

    @DeleteMapping("/eliminar")
    public String eliminarProveedor(@RequestParam("id_proveedor_eliminar") int id, Model model) {
        try {
            proveedorService.eliminar(id); // Eliminar el producto por ID
        } catch (Exception e) {
            model.addAttribute("error", "Error al eliminar el proveedor: " + e.getMessage());
        }
        return "redirect:/proveedor/listar"; // Redirigir a la lista de productos
    }

    // Método para asignar precio de venta de proveedores a cierto producto
    @PutMapping("/asignarPrecio")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> asignarPrecio(
        @RequestParam("id_proveedor_precio") int idProveedor,
        @RequestParam("id_producto_precio") int idProducto,
        @RequestParam("precio_proveedor") int precio) {

        Map<String, Object> response = new HashMap<>();
        try{
            //validamos que el id del proveedor y del producto si existan
            if(idProveedor == 0 || idProducto == 0){
                response.put("success", false);
                response.put("message", "Error: producto o proveedor no encontrado");
                return ResponseEntity.ok(response);
            }
            //buscamos el producto por id y el proveedor por id y asignamos el precio
            boolean respuesta = proveedorProductoService.asignarPrecio(idProducto, idProveedor, precio);
            if (respuesta) {
                response.put("success", true);
                response.put("message", "Precio asignado correctamente");
            } else {
                response.put("success", false);
                response.put("message", "Error al asignar el precio: producto no encontrado");
            }
        }catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al asignar el precio: " + e.getMessage());
        }
        return ResponseEntity.ok(response);
    }


}
