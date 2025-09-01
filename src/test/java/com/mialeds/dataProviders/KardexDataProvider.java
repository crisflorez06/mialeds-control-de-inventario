package com.mialeds.dataProviders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import com.mialeds.models.Kardex;
import com.mialeds.models.Producto;
import com.mialeds.models.Usuario;

public class KardexDataProvider {

    public static Usuario usuario1 = new Usuario(7, "cristian", "1007474345", "123456", "correoPrueba@gmail.com", "1234567890", true, true, true, true, null, null);

    public static Usuario usuario2 = new Usuario(8, "cristian", "1007474345", "123456", "correo@gmail.com", "1234567890", true, true, true, true, null, null);

    public static Producto producto1 = new Producto(1, "Producto 1", "Descripcion 1", 10, 20000, 18000);

    public static Producto producto2 = new Producto(2, "Producto 2", "Descripcion 2", 20, 30000, 28000);

    public static List<Kardex> getKardexList() {
        return Arrays.asList(
            new Kardex(1, producto1 , usuario2, "Entrada", LocalDate.now(), 10),
            new Kardex(2, producto2 , usuario1, "Salida", LocalDate.now(), 5),
            new Kardex(3, producto1 , usuario2, "Entrada", LocalDate.now(), 10)
        );
    }

    public static Kardex getKardex() {
        return new Kardex(1, producto1 , usuario2, "Entrada", LocalDate.now(), 10);
    }

    public static List<Kardex> getKardexListEntrada() {
        return Arrays.asList(
            new Kardex(1, producto1 , usuario2, "Entrada", LocalDate.now(), 10),
            new Kardex(3, producto1 , usuario2, "Entrada", LocalDate.now(), 10)
        );
    }

}
