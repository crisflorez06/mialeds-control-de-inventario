package com.mialeds.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mialeds.models.Producto;
import com.mialeds.repositories.ProductoRepository;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    protected Logger logger = LoggerFactory.getLogger(ProductoService.class);

    public List<Producto> productosEscasos() {
        try {
            return productoRepository.findByCantidadExistenteMenorQue(4);
        } catch (Exception e) {
            logger.error("Error al buscar productos escasos.", e);
            return List.of(); // Devuelve una lista vacía en caso de error
        }
    }

    public List<Producto> listar() {
        try {
            return productoRepository.findAllByOrderByNombreAsc();
        } catch (Exception e) {
            logger.error("Error al listar los productos.", e);
            return List.of();
        }
    }

    public List<Producto> listarPorNombre(String nombre) {
        try {
            // "Containing" es una palabra clave de Spring Data JPA para búsquedas tipo "LIKE %nombre%"
            return productoRepository.findByNombreContaining(nombre);
        } catch (Exception e) {
            logger.error("Error al buscar productos por nombre: {}", nombre, e);
            return List.of();
        }
    }

    protected Producto buscarPorId(int id) {
        return productoRepository.findById(id).orElse(null);
    }

    protected Producto guardar(Producto producto) {
        try {
            return productoRepository.save(producto);
        } catch (Exception e) {
            logger.error("Error al guardar el producto: {}", producto.getNombre(), e);
            return null;
        }
    }

    public Producto actualizar(int id, String nombre, String presentacion, int precioCompra, int precioVenta, int cantidad) {
        try {
            Producto p = buscarPorId(id);
            if (p == null) {
                throw new RuntimeException("Producto no encontrado con id: " + id);
            }
            p.setNombre(nombre);
            p.setPresentacion(presentacion);
            p.setPrecioCompra(precioCompra);
            p.setPrecioVenta(precioVenta);
            p.setCantidadExistente(cantidad);
            return guardar(p);
        } catch (Exception e) {
            logger.error("Error al actualizar el producto con id: {}", id, e);
            return null;
        }
    }

    public Producto crear(String nombre, String presentacion, int precioCompra, int precioVenta, int cantidad) {
        try {
            Producto p = new Producto(nombre, presentacion, cantidad, precioCompra, precioVenta);
            return guardar(p);
        } catch (Exception e) {
            logger.error("Error al crear el producto: {}", nombre, e);
            return null;
        }
    }

    public void eliminar(int id) {
        try {
            if (!productoRepository.existsById(id)) {
                throw new RuntimeException("No se puede eliminar. Producto no encontrado con id: " + id);
            }
            productoRepository.deleteById(id);
        } catch (Exception e) {
            logger.error("Error al eliminar el producto con id: {}", id, e);
        }
    }

    public Producto movimiento(int id, int cantidad, String movimiento) {
        try {
            Producto p = this.buscarPorId(id);
            if (p == null) {
                throw new RuntimeException("Producto no encontrado con id: " + id);
            }

            if ("salida".equals(movimiento)) {
                if (p.getCantidadExistente() < cantidad) {
                    throw new RuntimeException("Cantidad insuficiente para la salida.");
                }
                p.setCantidadExistente(p.getCantidadExistente() - cantidad);
            } else if ("entrada".equals(movimiento)) {
                p.setCantidadExistente(p.getCantidadExistente() + cantidad);
            } else {
                throw new IllegalArgumentException("Tipo de movimiento no válido: " + movimiento);
            }
            return guardar(p);
        } catch (Exception e) {
            logger.error("Error al registrar movimiento para el producto con id: {}", id, e);
            return null;
        }
    }

    public List<Object[]> listarIdNombrePresentacion() {
        try {
            return productoRepository.findIdNombreAndPresentacion();
        } catch (Exception e) {
            logger.error("Error al listar ID, nombre y presentación.", e);
            return List.of();
        }
    }
}
