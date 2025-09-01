package com.mialeds.dataProviders;

import java.util.List;
import java.util.ArrayList;

import com.mialeds.models.Proveedor;

public class ProveedorDataProvider {

    public static List<Proveedor> getProveedores() {
        return new ArrayList<Proveedor>() {
            {
                add(new Proveedor(1, "Proveedor 1", "123456789", "correo1@gmail.com", "1234567"));
                add(new Proveedor(2, "Proveedor 2", "987654321", "correo2@gmail.com", "7654321"));
                add(new Proveedor(3, "Proveedor 3", "123456789", "correo3@gmail.com", "1234567"));
            }
        };
    }

    public static Proveedor getProveedor() {
        return new Proveedor(1, "Proveedor 1", "123456789", "correo1@gmail.com", "1234567");
    }
}
