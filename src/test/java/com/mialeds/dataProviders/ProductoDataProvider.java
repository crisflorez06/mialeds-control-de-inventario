package com.mialeds.dataProviders;

import com.mialeds.models.Producto;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ProductoDataProvider {

    // Proveer lista de productos escasos
    public static List<Producto> getProductosEscasos() {
        return Arrays.asList(
            new Producto(1, "mobil 4t", "litro", 2, 20000, 25000),
            new Producto(1,"terpel 2t", "sachet", 1, 4000, 6000)
        );
    }

    // Proveer lista de productos ordenados
    public static List<Producto> getProductosOrdenados() {
        return Arrays.asList(
            new Producto(1,"mobil 4t", "litro", 1, 7000, 10000),
            new Producto(1,"terpel 2t", "sachet", 2, 15000, 20000),
            new Producto(1,"lubristone", "litro", 3, 20000, 25000),
            new Producto(1,"mobil plus", "pinta", 4, 25000, 30000)
        );
    }
    // Proveer lista de productos por nombre
    public static List<Producto> getProductosPorNombre() {
        return Arrays.asList(
            new Producto(1,"mobil 4t", "litro", 1, 7000, 10000),
            new Producto(1,"mobil plus", "pinta", 2, 15000, 20000),
            new Producto(1,"mobil 4t", "pinta", 3, 20000, 25000)
        );
    }

    // Proveer lista de productos por nombre tipo Object
    public static List<Object[]> getProductosPorNombreObject() {
        return List.of(
            new Object[]{1, "lubristone", "litro"},
            new Object[]{2, "liquido frenos", "unidad"},
            new Object[]{3, "grasa roja", "unidad"}
        );
    }

    // Proveer producto individual para buscarPorId, actualizar y eliminar
    public static Optional<Producto> getProductoPorId() {
        Producto producto = new Producto(1,"mobil 4t", "litro", 10, 3000, 4000);
        return Optional.of(producto);
    }

    // Proveer nuevo producto para guardar
    public static Producto getNuevoProducto() {
        return new Producto("liquido frenos", "unidad", 10, 300, 400);
    }
    
    // Proveer producto para prueba de movimiento
    public static Optional<Producto> getProductoParaMovimiento() {
        Producto producto = new Producto("lubristone", "litro", 10, 3000, 4000);
        return Optional.of(producto);
    }
}
