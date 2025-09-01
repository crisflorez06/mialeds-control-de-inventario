package com.mialeds.repositories;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mialeds.models.ProveedorProducto;

@Repository
public interface ProveedorProductoRepository extends JpaRepository<ProveedorProducto, Integer> {

    //metodo que me recupera una lista de los registros que coincidan con el nombre de producto
    @Query("SELECT pp FROM ProveedorProducto pp WHERE pp.producto.nombre LIKE %:nombreProducto%")
    List<ProveedorProducto> buscarPorNombreProducto(@Param("nombreProducto") String nombreProducto);

    //metodo que me busque el registro de un precio de proveedor por el id del producto y el id del proveedor
    @Query("SELECT pp FROM ProveedorProducto pp WHERE pp.producto.id = :productoId AND pp.proveedor.id = :proveedorId")
    ProveedorProducto encontrarPorProductoYProveedor(@Param("productoId") int productoId, @Param("proveedorId") int proveedorId);
}

