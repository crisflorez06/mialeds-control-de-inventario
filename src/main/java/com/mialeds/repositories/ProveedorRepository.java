package com.mialeds.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mialeds.models.Proveedor;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Integer> {

        /*metodo para buscar proveedores por nombre utilizado para el AJAX de listaProducto.js*/ 
    @Query("SELECT p.idProveedor, p.nombre FROM Proveedor p")
    List<Object[]> findIdAndNombre();

}
