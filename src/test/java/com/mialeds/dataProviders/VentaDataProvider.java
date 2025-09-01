package com.mialeds.dataProviders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import com.mialeds.models.Producto;
import com.mialeds.models.Usuario;
import com.mialeds.models.Venta;

public class VentaDataProvider {
    
    public static Usuario usuario = UsuarioDataProvider.usuarioPorId();
    public static Producto producto = ProductoDataProvider.getNuevoProducto();

    public static List<Venta> getVentas() {
        return Arrays.asList(
            new Venta(1, usuario, producto, LocalDate.now(), 1, 100),
            new Venta(2, usuario, producto, LocalDate.now(), 2, 200)
        );
    }

    public static List<Venta> ventasVacias() {
        return Arrays.asList();
    }

    public static Venta getVenta() {
        return new Venta(1, usuario, producto, LocalDate.now(), 1, 100);
    }
}
