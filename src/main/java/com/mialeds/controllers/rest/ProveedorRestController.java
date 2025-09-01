package com.mialeds.controllers.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mialeds.services.ProveedorService;

//se utiliza para listar productos en buscadores
@RestController
@RequestMapping("/api/proveedor")
public class ProveedorRestController {
    @Autowired
    private ProveedorService proveedorService;

    @GetMapping("/listar")
    public List<Object[]> listarProveedores() {
        return proveedorService.listarIdNombre();
    }
}
