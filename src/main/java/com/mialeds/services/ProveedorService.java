package com.mialeds.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mialeds.repositories.ProveedorRepository;
import com.mialeds.models.Proveedor;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ProveedorService {

    // Inyectar el repositorio
    @Autowired
    private ProveedorRepository proveedorRepository;

    private Logger logger = LoggerFactory.getLogger(ProveedorService.class);

    //metodo que me retorna una lista de proveedores
    public List<Proveedor> listar() {
        try {
            return proveedorRepository.findAll();
        } catch (Exception e) {
            logger.error("Error al listar los proveedores: " + e.getMessage());
            return null;
        } 
    }

    //metodo que me retorna un proveedor por id, utilizado en esta misma clase mas que todo
    protected Proveedor buscarPorId(int id){
        try {
            return proveedorRepository.findById(id).orElse(null);
        } catch (Exception e) {
            logger.error("Error al buscar el proveedor por id: " + e.getMessage());
            return null;
        }
    }

    //separacion de la logica de save que viene por defecto en jpa
    protected Proveedor guardar(Proveedor proveedor){
        try {
            return proveedorRepository.save(proveedor);
        } catch (Exception e) {
            logger.error("Error al guardar el proveedor: " + e.getMessage());
            return null;
        }
    }

    //este metodo actualiza un producto y lo guarda en la base de datos
    public Proveedor actualizar(int id, String nombre, String nit, String correo, String numero){
        try {
            //buscamos el producto por id
            Proveedor proveedor = buscarPorId(id);

            //actualizamos los datos
            proveedor.setNombre(nombre);
            proveedor.setNit(nit);
            proveedor.setCorreoElectronico(correo);
            proveedor.setTelefono(numero);

            //guardamos el producto
            guardar(proveedor);
            
        return proveedor;
        } catch (Exception e) {
            logger.error("Error al actualizar el proveedor: " + e.getMessage());
            return null;
        }
    }

        //este metodo crea un proveedor y lo guarda en la base de datos
    public Proveedor crear(String nombre, String nit, String correo, String numero) {
        //creamos una instancia de proveedor usando el constructor que no necesita id
        try {
        Proveedor proveedor = new Proveedor(nombre, nit, correo, numero);
        proveedorRepository.save(proveedor);
        return proveedor;
        } catch (Exception e) {
            logger.error("Error al crear el producto: " + e.getMessage());
            return null;
        }
    }

    public void eliminar(int id) {
        try {
            proveedorRepository.deleteById(id);
        } catch (Exception e) {
            logger.error("Error al eliminar el proveedor: " + e.getMessage());
        }
    }

        /*este metodo retorna una lista de productos con nombre y presentacion
    utilizado en la apirest para los productos de la venta*/
    public List<Object[]> listarIdNombre() {
        try {
            return proveedorRepository.findIdAndNombre();
        } catch (Exception e) {
            logger.error("Error al buscar nombre y presentacion: " + e.getMessage());
            return null;
        }
    }

    
}
