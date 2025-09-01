package com.mialeds.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

import com.mialeds.models.Producto;

/*al extender de JpaRepository se heredan los metodos basicos para interactuar con la base de datos
se debe indicar el tipo de entidad y el tipo de dato del id de la entidad*/
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    /* este metodo es un query personalizado para buscar productos con cantidad 
    existente menor a la cantidad indicada */
    @Query("SELECT p FROM Producto p WHERE p.cantidadExistente < :cantidad")
    List<Producto> findByCantidadExistenteMenorQue(@Param("cantidad") int cantidad);

    //metodo para buscar productos por nombre del producto
    List<Producto> findByNombreContaining(String nombre);

    /*metodo para buscar todos los productos ordenados por nombre, Order By 
    es una palabra clave de SQL para ordenar los datos por un atributo y en que direccion*/
    List<Producto> findAllByOrderByNombreAsc();

    /*metodo para buscar productos por nombre y presentacion utilizado para el AJAX de la vista 
    ventas para buscar productos para registrar las ventas*/
    @Query("SELECT p.idProducto, p.nombre, p.presentacion FROM Producto p")
    List<Object[]> findIdNombreAndPresentacion();
}
