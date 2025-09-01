package com.mialeds.services;

import java.util.List;
import java.util.Locale;
import java.text.NumberFormat;
import java.time.LocalDate;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mialeds.models.Producto;
import com.mialeds.models.Usuario;
import com.mialeds.models.Venta;
import com.mialeds.repositories.VentaRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ProductoService productoService;

    protected Logger logger = LoggerFactory.getLogger(VentaService.class);

    //metodo para listar todas las ventas en orden descendente de fecha
    public List<Venta> listar() {
        try {
            return ventaRepository.findAllByOrderByFechaDesc();
        } catch (Exception e) {
            logger.error("Error al listar las ventas: " + e.getMessage());
            return null;
        }
    }

    //metodo que lista las ventas por las que contentan parte de la busqueda en el nombre del producto
    public List<Venta> listarPorProducto(String nombre) {
        try {
            return ventaRepository.findByProducto_NombreContaining(nombre);
        } catch (Exception e) {
            logger.error("Error al listar las ventas: " + e.getMessage());
            return null;
        }
    }

    //metodo que lista las ventas por la fecha indicada
    public List<Venta> listarPorFecha(LocalDate fecha) {
        try {
            return ventaRepository.findByFecha(fecha);
        } catch (Exception e) {
            logger.error("Error al listar las ventas: " + e.getMessage());
            return null;
        }
    }

    //metodo que lista las ventas por el nombre del producto y la fecha indicada
    public List<Venta> listarPorProductoYFecha(String nombre, LocalDate fecha) {
        try {
            return ventaRepository.findByProducto_NombreContainingAndFecha(nombre, fecha);
        } catch (Exception e) {
            logger.error("Error al listar las ventas: " + e.getMessage());
            return null;
        }
    }

    //metodo que lista las ventas segun los parametros que se le pasen ya sea el nombre del producto o la fecha, ambos o ninguno
    public List<Venta> obtenerVentas(String nombre, LocalDate fecha) {
        if (nombre != null && !nombre.isEmpty() && fecha != null) {
            return listarPorProductoYFecha(nombre, fecha);
        } else if (nombre != null && !nombre.isEmpty()) {
            return listarPorProducto(nombre);
        } else if (fecha != null) {
            return listarPorFecha(fecha);
        } else {
            return listarPorFecha(LocalDate.now());
        }
    }

    //metodo que suma el total de las ventas que esten en la tabla de ventas y poner el total en formato de moneda, (con puntos y comas)
    public String formatearTotalVentas(List<Venta> ventas) {
        //sumamos el total de las ventas
    double totalVentas = ventas.stream()
            .mapToDouble(Venta::getTotalVenta)
            .sum();
            //formateamos el total de las ventas a moneda
    NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.forLanguageTag("es-CO"));
    return numberFormat.format(totalVentas);
    }


    public Venta guardar(int idProducto, int cantidad, LocalDate fecha) {
        try {
            //verificamos que el producto exista
            Producto producto = productoService.buscarPorId(idProducto);
            if (producto == null) {
                logger.error("Error al guardar la venta: producto no encontrado");
                return null;
            }

            //obtenemos el usuario que esta realizando la venta
            int idUsuario = usuarioService.obtenerIdUsuarioSesion();
            Usuario usuario = usuarioService.buscarPorId(idUsuario);

            //verificamos que la cantidad de productos existentes sea suficiente
            if (producto.getCantidadExistente() < cantidad) {
                logger.error("Error al guardar la venta: cantidad insuficiente".toString());
                return null;
            }

            //calculamos el total de la venta
            int totalVenta = producto.getPrecioVenta() * cantidad;            

            //verificamos si ya existe una venta con los mismos parametros, ya que si exite no queremos que se cree una nueva venta si no editar la existente aumentando la cantidad
            Venta ventaExistente = ventaRepository.findByProductoAndFecha(producto, fecha);            

            //si ya existe una venta con los mismos parametros, se actualiza la venta existente
            if (ventaExistente != null) {
                //sumamos la cantidad a la venta
                ventaExistente.setCantidad(ventaExistente.getCantidad() + cantidad);
                //sumamos el total de la venta con lo de la nueva venta
                ventaExistente.setTotalVenta(ventaExistente.getTotalVenta() + totalVenta);
                //descontamos la cantidad de productos en el inventario
                producto.setCantidadExistente(producto.getCantidadExistente() - cantidad);

                //guardamos el cambio que hicimos de la cantidad del producto en el inventario
                productoService.guardar(producto);
                //retornamos la venta actualizada
                return ventaRepository.save(ventaExistente);
            }else{
                //si no existe una venta con los mismos parametros, se crea una nueva venta
                producto.setCantidadExistente(producto.getCantidadExistente() - cantidad);

                Venta venta = new Venta();
                venta.setProducto(producto);
                venta.setUsuario(usuario);
                venta.setCantidad(cantidad);
                venta.setTotalVenta(totalVenta);
                venta.setFecha(fecha);

                //guardamos el cambio que hicimos de la cantidad del producto en el inventario                
                productoService.guardar(producto);
                return ventaRepository.save(venta);
            }

        } catch (Exception e) {
            logger.error("Error al guardar la venta: " + e.getMessage());
            return null;
        }
    }

    //metodo para actualizar la venta
    public Venta actualizar(int id, int idProducto, int cantidad, int total, LocalDate fecha) {
        try {

            //verificamos si la venta existe
            Venta venta = ventaRepository.findById(id).orElse(null);
            if (venta == null) {
                logger.error("Error al actualizar la venta: venta no encontrada");
                return null;
            }

            //verificamos si el producto existe
            Producto producto = productoService.buscarPorId(idProducto);
            if (producto == null) {
                logger.error("Error al actualizar la venta: producto no encontrado");
                return null;
            }

            //obtenemos el usuario que esta realizando la venta
            int idUsuario = usuarioService.obtenerIdUsuarioSesion();
            Usuario usuario = usuarioService.buscarPorId(idUsuario);

            //si el total de la venta no cambio, se calcula el total de la venta multiplicando el precio de venta del producto por la cantidad, si cambio el total de la venta, se toma el total que se le paso
            int totalVenta = 0;
            if(venta.getTotalVenta() == total){
                totalVenta = producto.getPrecioVenta() * cantidad;
            } else {
                totalVenta = total;
            }

            
            //verificamos que la cantidad de productos existentes sea suficiente
            if (producto.getCantidadExistente() < cantidad - venta.getCantidad()) {
                logger.error("Error al actualizar la venta: cantidad insuficiente");
                return null;
            }

            //logica para evitar errores al momento de descontar el producto en el inventario, verificamos si la cantidad de la venta cambio, si cambio, se calcula la diferencia de la cantidad de la venta anterior con la nueva cantidad y se suma a la cantidad existente del producto
            int diferenciaCantidad = 0;
            if (venta.getCantidad() != cantidad) {
                diferenciaCantidad = venta.getCantidad() - cantidad;
                producto.setCantidadExistente(producto.getCantidadExistente() + diferenciaCantidad);
            }

            //actualizamos los datos de la venta
            venta.setProducto(producto);
            venta.setUsuario(usuario);
            venta.setCantidad(cantidad);
            venta.setTotalVenta(totalVenta);
            venta.setFecha(fecha);

            //guardamos el cambio que hicimos de la cantidad del producto en el inventario
            productoService.guardar(producto);
            return ventaRepository.save(venta);
        } catch (Exception e) {
            logger.error("Error al actualizar la venta: " + e.getMessage());
            return null;
        }
    }

    public void eliminar(int id) {
        //verificamos si la venta existe
        try {
            Venta venta = ventaRepository.findById(id).orElse(null);
            if (venta == null) {
                logger.error("Error al eliminar la venta: venta no encontrada");
                return;
            }

            //devolvemos la cantidad de productos que se vendieron
            Producto producto = venta.getProducto();
            producto.setCantidadExistente(producto.getCantidadExistente() + venta.getCantidad());

            //guardamos el cambio que hicimos de la cantidad del producto en el inventario
            productoService.guardar(producto);
            ventaRepository.deleteById(id);
        } catch (Exception e) {
            logger.error("Error al eliminar la venta: " + e.getMessage());
        }
    }

    //metodo que obtiene los 5 productos mas vendidos
    public List<Object[]> obtenerTop5ProductosMasVendidos() {
        try {
            LocalDate fechaInicio = LocalDate.now().minusMonths(1);
            return ventaRepository.findTop5ProductosMasVendidos(fechaInicio);
        } catch (Exception e) {
            logger.error("Error al obtener los 5 productos mas vendidos: " + e.getMessage());
            return null;
        }
    }

    //metodo que obtiene los 5 productos menos vendidos
    public List<Object[]> obtenerTop5ProductosMenosVendidos() {
        try {
            LocalDate fechaInicio = LocalDate.now().minusMonths(1);
            return ventaRepository.findTop5ProductosMenosVendidos(fechaInicio);
        } catch (Exception e) {
            logger.error("Error al obtener los 5 productos menos vendidos: " + e.getMessage());
            return null;
        }
    }

    //metodo que obtiene las ventas por dia de los ultimos 30 dias
    public List<Object[]> obtenerVentasUltimoMes() {
        try {
            LocalDate fechaInicio = LocalDate.now().minusMonths(1);
            return ventaRepository.findVentasPorDiaUltimos30Dias(fechaInicio);
        } catch (Exception e) {
            logger.error("Error al obtener las ventas de los ultimos 30 dias: " + e.getMessage());
            return null;
        }
    }

    //metodo que obtiene las ganancias por dia de los ultimos 30 dias
    public List<Object[]> obtenerGananciasUltimoMes() {
        try {
            LocalDate fechaInicio = LocalDate.now().minusMonths(1);
            return ventaRepository.findGananciasPorDiaUltimos30Dias(fechaInicio);
        } catch (Exception e) {
            logger.error("Error al obtener las ganancias de los ultimos 30 dias: " + e.getMessage());
            return null;
        }
    }
}
