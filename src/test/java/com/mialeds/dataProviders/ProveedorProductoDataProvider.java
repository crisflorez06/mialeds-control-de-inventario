package com.mialeds.dataProviders;

import java.util.ArrayList;
import java.util.List;

import com.mialeds.models.Producto;
import com.mialeds.models.Proveedor;
import com.mialeds.models.ProveedorProducto;

public class ProveedorProductoDataProvider {

    public static Producto producto1 = new Producto(1, "Producto 1", "Descripcion 1", 10, 20000, 18000);

    public static Producto producto2 = new Producto(2, "Producto 2", "Descripcion 2", 20, 30000, 28000);

    public static Proveedor proveedor1 = new Proveedor(1, "Proveedor 1", "123456789", "correo1@gmail.com", "1234567");

    public static Proveedor proveedor2 = new Proveedor(2, "Proveedor 2", "987654321", "correo2@gmail.com", "7654321");

    public static List<ProveedorProducto> getProveedorProductos() {
        // Crear una lista de productos de proveedor
        return new ArrayList<ProveedorProducto>() {
            {
                add(new ProveedorProducto(1, proveedor1, producto1, 18000));
                add(new ProveedorProducto(2, proveedor1, producto2, 28000));
                add(new ProveedorProducto(3, proveedor2, producto1, 19000));
                add(new ProveedorProducto(4, proveedor2, producto2, 29000));
            }
        };
    }

    public static List<ProveedorProducto> getProveedorProductosPorNombre(){
        // Crear una lista de productos de proveedor
        return new ArrayList<ProveedorProducto>() {
            {
                add(new ProveedorProducto(1, proveedor1, producto1, 18000));
                add(new ProveedorProducto(2, proveedor2, producto1, 28000));
            }
        };
    }

    public static ProveedorProducto getProveedorProducto() {
        return new ProveedorProducto(1, proveedor2, producto1, 18000);
    }
}
