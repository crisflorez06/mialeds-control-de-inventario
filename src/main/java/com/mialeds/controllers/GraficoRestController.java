package com.mialeds.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import com.mialeds.services.VentaService;

@RestController
@RequestMapping("/api/grafico")
public class GraficoRestController {

    @Autowired
    private VentaService ventaService;

    //metodo para obtener los 5 productos mas vendidos
    @GetMapping("/ProductosMasVendidos")
    public List<Object[]> top5ProductosMasVendidos() {
        return ventaService.obtenerTop5ProductosMasVendidos();
    }

    //metodo para obtener los 5 productos menos vendidos
    @GetMapping("/ProductosMenosVendidos")
    public List<Object[]> top5ProductosMenosVendidos() {
        return ventaService.obtenerTop5ProductosMenosVendidos();
    }

    //metodo para obtener las ventas por dia de los ultimos 30 dias
    @GetMapping("/VentasUltimos30Dias")
    public List<Object[]> ventasPorDiaUltimos30Dias() {
        return ventaService.obtenerVentasUltimoMes();
    }

    //metodo para obtener las ganancias por dia de los ultimos 30 dias
    @GetMapping("/GananciasUltimos30Dias")
    public List<Object[]> gananciasPorDiaUltimos30Dias() {
        return ventaService.obtenerGananciasUltimoMes();
    }
}
