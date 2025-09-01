package com.mialeds.repositories;


import java.util.List;
import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mialeds.models.Producto;
import com.mialeds.models.Venta;

public interface VentaRepository extends JpaRepository<Venta, Integer> {

        //metodo para buscar ventas por producto Y ordenarlas por fecha
        List<Venta> findAllByOrderByFechaDesc();

        /*metodo utilizado para que en el servicio verifique si la venta existe con los parametros que indicamos, ya que si es asi no queremos que nos guarde una nueva venta
        sino que actualice la existente */
        Venta findByProductoAndFecha(Producto Producto, LocalDate fecha);

        //metodo utilizado cuando brindamos informacion en la vista del nombre del producto pero no la fecha
        List<Venta> findByProducto_NombreContaining(String nombre);

        //metodo utilizado cuando brindamos informacion en la vista de la fecha pero no el nombre del producto
        List<Venta> findByFecha(LocalDate fecha);

        //metodo utilizado cuando brindamos informacion en la vista del producto y la fecha
        List<Venta> findByProducto_NombreContainingAndFecha(String nombre, LocalDate fecha);
        

        //consulta para obtener los 5 productos mas vendidos
        @Query("SELECT p.idProducto, p.nombre, p.presentacion, SUM(v.cantidad) AS totalVendida " +
        "FROM Venta v " +
        "JOIN v.producto p " +
        "WHERE v.fecha >= :fechaInicio " +
        "GROUP BY p.idProducto, p.nombre, p.presentacion " +
        "ORDER BY totalVendida DESC")
        List<Object[]> findTop5ProductosMasVendidos(LocalDate fechaInicio);


        @Query("SELECT p.idProducto, p.nombre, p.presentacion, SUM(v.cantidad) AS totalVendida " +
        "FROM Venta v " +
        "JOIN v.producto p " +
        "WHERE v.fecha >= :fechaInicio " +
        "GROUP BY p.idProducto, p.nombre, p.presentacion " +
        "ORDER BY totalVendida ASC")
        List<Object[]> findTop5ProductosMenosVendidos(LocalDate fechaInicio);

        @Query("SELECT v.fecha, SUM(v.totalVenta) AS totalDiario " +
        "FROM Venta v " +
        "WHERE v.fecha >= :fechaInicio " +
        "GROUP BY v.fecha " +
        "ORDER BY v.fecha ASC")
        List<Object[]> findVentasPorDiaUltimos30Dias(LocalDate fechaInicio);

        @Query("SELECT v.fecha, SUM(v.totalVenta - (v.cantidad * v.producto.precioCompra)) AS gananciaDiaria " +
        "FROM Venta v " +
        "WHERE v.fecha >= :fechaInicio " +
        "GROUP BY v.fecha " +
        "ORDER BY v.fecha ASC")
        List<Object[]> findGananciasPorDiaUltimos30Dias(LocalDate fechaInicio);

}
