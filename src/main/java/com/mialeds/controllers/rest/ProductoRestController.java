package com.mialeds.controllers.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mialeds.services.ProductoService;

//se utiliza para listar productos en buscadores
@RestController
@RequestMapping("/api/producto")
public class ProductoRestController {
    @Autowired
    private ProductoService productoService;

    @GetMapping("/listar")
    public List<Object[]> listarProductos() {
        return productoService.listarIdNombrePresentacion();
    }
}
