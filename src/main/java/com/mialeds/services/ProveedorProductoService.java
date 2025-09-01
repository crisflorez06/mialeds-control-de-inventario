package com.mialeds.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mialeds.models.ProveedorProducto;
import com.mialeds.repositories.ProveedorProductoRepository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class ProveedorProductoService {

    // Inyectar el repositorio
    @Autowired
    private ProveedorProductoRepository proveedorProductoRepository;

    // Inyectar los servicios
    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProveedorService proveedorService;


    private Logger logger = LoggerFactory.getLogger(ProveedorService.class);


    //metodo que me agrupa los productos por nombre y presentacion, retornando un mapa con la clave que es el nombre y presentacion del producto y el valor es una lista de proveedorProducto
    public Map<String, List<ProveedorProducto>> obtenerProductosAgrupados() {
        try {
        // Obtener todos los registros de la tabla proveedor_producto
        List<ProveedorProducto> proveedorProductos = proveedorProductoRepository.findAll();

        // recorremos la lista de proveedorProductos, cada registro que corresponda con el mismo producto lo agrupamos en una lista, generamos un mapa con la clave que es el nombre y presentacion del producto y el valor es una lista de proveedorProducto
        Map<String, List<ProveedorProducto>> productosAgrupados = proveedorProductos.stream()
            .collect(Collectors.groupingBy(pp -> 
                // Combinar el nombre y la presentación del producto para formar la clave del mapa
                pp.getProducto().getNombre() + " (" + pp.getProducto().getPresentacion() + ")"
            ));

        // Ordenar el mapa por la clave (nombre y presentación del producto) en orden alfabético
        return productosAgrupados.entrySet().stream()
            .sorted(Map.Entry.comparingByKey()) // Ordenar por clave (alfabético)
            .collect(Collectors.toMap(
                Map.Entry::getKey, 
                Map.Entry::getValue, 
                (oldValue, newValue) -> oldValue, 
                LinkedHashMap::new // Mantener el orden de inserción
            ));
        } catch (Exception e) {
            logger.error("Error al obtener los productos agrupados: " + e.getMessage());
            return null;
        }
    }

    //metodo que me retorna tambien un mapa pero con los productos que contienen el nombre que se le pasa como parametro
    public Map<String, List<ProveedorProducto>> listarPornombre(String nombre) {
        try {
        List<ProveedorProducto> proveedorProductos = proveedorProductoRepository.buscarPorNombreProducto(nombre);
    
        // Agrupar por nombre y presentación del producto
        Map<String, List<ProveedorProducto>> productosAgrupados = proveedorProductos.stream()
            .collect(Collectors.groupingBy(pp -> 
                pp.getProducto().getNombre() + " (" + pp.getProducto().getPresentacion() + ")"
            ));

        // Ordenar el mapa por la clave (nombre y presentación del producto) en orden alfabético
        return productosAgrupados.entrySet().stream()
            .sorted(Map.Entry.comparingByKey()) // Ordenar por clave (alfabético)
            .collect(Collectors.toMap(
                Map.Entry::getKey, 
                Map.Entry::getValue, 
                (oldValue, newValue) -> oldValue, 
                LinkedHashMap::new // Mantener el orden de inserción
            ));
        } catch (Exception e) {
            logger.error("Error al listar productos por nombre: " + e.getMessage());
            return null;
        }
    }


    //metodo que me permite asignar un precio a un producto de un proveedor
    public boolean asignarPrecio(int idProducto, int idProveedor, int precio) {
        try {
        // Buscar el registro por producto y proveedor, si no existe, crearlo
        ProveedorProducto pp = proveedorProductoRepository.encontrarPorProductoYProveedor(idProducto, idProveedor);
        if(pp == null) {
            pp = new ProveedorProducto();
            pp.setProducto(productoService.buscarPorId(idProducto));
            pp.setProveedor(proveedorService.buscarPorId(idProveedor));
            pp.setPrecioCompraProveedor(precio);
            proveedorProductoRepository.save(pp);
            return true;
        }
        //actualizamos y guardamos el registro
        pp.setPrecioCompraProveedor(precio);
        proveedorProductoRepository.save(pp);
        return true;
        } catch (Exception e) {
            logger.error("Error al crear el registro: " + e.getMessage());
            return false;
        }
    }
}
