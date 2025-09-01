package com.mialeds.services;

import java.util.List;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mialeds.models.Kardex;
import com.mialeds.models.Producto;
import com.mialeds.models.Usuario;
import com.mialeds.repositories.KardexRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@Service
public class KardexService {

    @Autowired
    private KardexRepository kardexRepository;
    @Autowired
    private ProductoService productoService;
    @Autowired
    private UsuarioService usuarioService;

    protected Logger logger = LoggerFactory.getLogger(KardexService.class);


    public List<Kardex> listar() {
        try {
            return kardexRepository.findAll();
        } catch (Exception e) {
            logger.error("Error al listar los kardex: " + e.getMessage());
            return null;
        }
    }

    protected Kardex buscarPorId(int id) {
        try {
            return kardexRepository.findById(id).orElse(null);
        } catch (Exception e) {
            logger.error("Error al buscar el kardex: " + e.getMessage());
            return null;
        }
    }

    protected Kardex guardar(Kardex kardex) {
        try {
            return kardexRepository.save(kardex);
        } catch (Exception e) {
            logger.error("Error al guardar el kardex: " + e.getMessage());
            return null;
        }
    }

    public Kardex crear(int idProducto,int idUsuario, String movimiento, LocalDate fecha, int cantidad) {
        try {
            Producto producto = productoService.buscarPorId(idProducto);
            Usuario usuario = usuarioService.buscarPorId(idUsuario);
            Kardex kardex = new Kardex(producto, usuario, movimiento, fecha, cantidad);
            this.guardar(kardex);
            return kardex;
        } catch (Exception e) {
            logger.error("Error al crear el kardex: " + e.getMessage());
            return null;
        }
    }

    public List<Kardex> listarPorMovimiento(String movimiento) {
        try {
            return kardexRepository.findByMovimientoOrderByFechaDesc(movimiento);
        } catch (Exception e) {
            logger.error("Error al listar los kardex por movimiento: " + e.getMessage());
            return null;
        }
    }

    
}
