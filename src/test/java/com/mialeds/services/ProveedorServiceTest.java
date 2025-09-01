package com.mialeds.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.*;

import com.mialeds.dataProviders.ProveedorDataProvider;
import com.mialeds.models.Proveedor;
import com.mialeds.repositories.ProveedorRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

class ProveedorServiceTest {

    @Mock
    private ProveedorRepository proveedorRepository;

    @Mock
    private Logger logger;

    @InjectMocks
    private ProveedorService proveedorService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListar() {
        // Configura el repositorio para devolver una lista de proveedores
        List<Proveedor> proveedores = ProveedorDataProvider.getProveedores();
        when(proveedorRepository.findAll()).thenReturn(proveedores);

        // Llama al método a probar
        List<Proveedor> resultado = proveedorService.listar();

        // Verifica que el resultado no sea nulo y contenga los datos esperados
        assertNotNull(resultado);
        assertEquals(3, resultado.size());
        assertEquals("Proveedor 1", resultado.get(0).getNombre());

        // Verifica que el método findAll del repositorio haya sido llamado
        verify(proveedorRepository).findAll();
    }

    @Test
    void testListarException() {
        // Configura el repositorio para lanzar una excepción
        when(proveedorRepository.findAll()).thenThrow(new RuntimeException("Error simulado"));

        // Llama al método a probar
        List<Proveedor> resultado = proveedorService.listar();

        // Verifica que el resultado sea null debido a la excepción
        assertNull(resultado);

        // Verifica que el logger registre el mensaje de error
        verify(logger).error("Error al listar los proveedores: Error simulado");
    }

    @Test
    void testBuscarPorId() {
        // Configura el repositorio para devolver un proveedor específico
        Proveedor proveedor = ProveedorDataProvider.getProveedor();
        when(proveedorRepository.findById(1)).thenReturn(Optional.of(proveedor));

        // Llama al método a probar
        Proveedor resultado = proveedorService.buscarPorId(1);

        // Verifica que el resultado no sea nulo y contenga los datos esperados
        assertNotNull(resultado);
        assertEquals("Proveedor 1", resultado.getNombre());

        // Verifica que el método findById del repositorio haya sido llamado con el ID correcto
        verify(proveedorRepository).findById(1);
    }

    @Test
    void testBuscarPorIdException() {
        // Configura el repositorio para lanzar una excepción
        when(proveedorRepository.findById(1)).thenThrow(new RuntimeException("Error simulado"));

        // Llama al método a probar
        Proveedor resultado = proveedorService.buscarPorId(1);

        // Verifica que el resultado sea null debido a la excepción
        assertNull(resultado);

        // Verifica que el logger registre el mensaje de error
        verify(logger).error("Error al buscar el proveedor por id: Error simulado");
    }

    @Test
    void testGuardar() {
        // Configura el repositorio para devolver el proveedor guardado
        Proveedor proveedor = ProveedorDataProvider.getProveedor();
        when(proveedorRepository.save(proveedor)).thenReturn(proveedor);

        // Llama al método a probar
        Proveedor resultado = proveedorService.guardar(proveedor);

        // Verifica que el resultado no sea nulo y contenga los datos esperados
        assertNotNull(resultado);
        assertEquals("Proveedor 1", resultado.getNombre());

        // Verifica que el método save del repositorio haya sido llamado
        verify(proveedorRepository).save(proveedor);
    }

    @Test
    void testGuardarException() {
        // Configura el repositorio para lanzar una excepción al guardar
        Proveedor proveedor = new Proveedor("Proveedor1", "1234", "correo@example.com", "123456789");
        when(proveedorRepository.save(proveedor)).thenThrow(new RuntimeException("Error simulado"));

        // Llama al método a probar
        Proveedor resultado = proveedorService.guardar(proveedor);

        // Verifica que el resultado sea null debido a la excepción
        assertNull(resultado);

        // Verifica que el logger registre el mensaje de error
        verify(logger).error("Error al guardar el proveedor: Error simulado");
    }

    @Test
    void testActualizar() {
        // Configura el repositorio para devolver el proveedor encontrado y actualizado
        Proveedor proveedor = ProveedorDataProvider.getProveedor();
        when(proveedorRepository.findById(1)).thenReturn(Optional.of(proveedor));
        when(proveedorRepository.save(proveedor)).thenReturn(proveedor);

        // Llama al método a probar
        Proveedor resultado = proveedorService.actualizar(1, "ProveedorActualizado", "5678", "nuevo@example.com", "987654321");

        // Verifica que el resultado no sea nulo y contenga los datos actualizados
        assertNotNull(resultado);
        assertEquals("ProveedorActualizado", resultado.getNombre());
        assertEquals("5678", resultado.getNit());

        // Verifica que los métodos findById y save del repositorio hayan sido llamados
        verify(proveedorRepository).findById(1);
        verify(proveedorRepository).save(proveedor);
    }

    @Test
    void testActualizarException() {
        // Configura el repositorio para lanzar una excepción al buscar el proveedor
        when(proveedorRepository.findById(1)).thenThrow(new RuntimeException("Error simulado"));

        // Llama al método a probar
        Proveedor resultado = proveedorService.actualizar(1, "ProveedorActualizado", "5678", "nuevo@example.com", "987654321");

        // Verifica que el resultado sea null debido a la excepción
        assertNull(resultado);

        // Verifica que el logger registre el mensaje de error
        verify(logger).error(contains("Error al actualizar el proveedor"));
    }

    @Test
    void testCrear() {
        // Configura el repositorio para devolver el proveedor creado
        Proveedor proveedor = ProveedorDataProvider.getProveedor();
        when(proveedorRepository.save(any(Proveedor.class))).thenReturn(proveedor);

        // Llama al método a probar
        Proveedor resultado = proveedorService.crear("Proveedor 1", "123456789", "correo1@gmail.com", "1234567");

        // Verifica que el resultado no sea nulo y contenga los datos esperados
        assertNotNull(resultado);
        assertEquals("Proveedor 1", resultado.getNombre());

        // Verifica que el método save haya sido llamado con el proveedor
        verify(proveedorRepository).save(any(Proveedor.class));
    }

    @Test
    void testCrearException() {

        // Configura el repositorio para lanzar una excepción al guardar
        
        when(proveedorRepository.save(any(Proveedor.class))).thenThrow(new RuntimeException("Error simulado"));

        // Llama al método a probar
        Proveedor resultado = proveedorService.crear("Proveedor1", "1234", "correo@example.com", "123456789");

        // Verifica que el resultado sea null debido a la excepción
        assertNull(resultado);

        // Verifica que el logger registre el mensaje de error
        verify(logger).error("Error al crear el producto: Error simulado");
    }

    @Test
    void testEliminar() {
        // Configura el repositorio para simular la eliminación del proveedor
        doNothing().when(proveedorRepository).deleteById(1);

        // Llama al método a probar
        proveedorService.eliminar(1);

        // Verifica que el método deleteById del repositorio haya sido llamado con el ID correcto
        verify(proveedorRepository).deleteById(1);
    }

    @Test
    void testEliminarException() {
        // Configura el repositorio para lanzar una excepción al eliminar
        doThrow(new RuntimeException("Error simulado")).when(proveedorRepository).deleteById(1);

        // Llama al método a probar
        proveedorService.eliminar(1);

        // Verifica que el logger registre el mensaje de error
        verify(logger).error("Error al eliminar el proveedor: Error simulado");
    }

    @Test
    void testListarIdNombre() {
        // Configura el repositorio para devolver una lista de objetos con IDs y nombres
        List<Object[]> proveedores = Collections.singletonList(new Object[]{1, "Proveedor1"});
        when(proveedorRepository.findIdAndNombre()).thenReturn(proveedores);

        // Llama al método a probar
        List<Object[]> resultado = proveedorService.listarIdNombre();

        // Verifica que el resultado no sea nulo y contenga los datos esperados
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Proveedor1", resultado.get(0)[1]);

        // Verifica que el método findIdAndNombre del repositorio haya sido llamado
        verify(proveedorRepository).findIdAndNombre();
    }

    @Test
    void testListarIdNombreException() {
        // Configura el repositorio para lanzar una excepción al buscar
        when(proveedorRepository.findIdAndNombre()).thenThrow(new RuntimeException("Error simulado"));

        // Llama al método a probar
        List<Object[]> resultado = proveedorService.listarIdNombre();

        // Verifica que el resultado sea null debido a la excepción
        assertNull(resultado);

        // Verifica que el logger registre el mensaje de error
        verify(logger).error("Error al buscar nombre y presentacion: Error simulado");
    }
}
