package com.mialeds.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.mialeds.dataProviders.ProveedorProductoDataProvider;
import com.mialeds.models.ProveedorProducto;
import com.mialeds.repositories.ProveedorProductoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import java.util.List;
import java.util.Map;

class ProveedorProductoServiceTest {

    @Mock
    private ProveedorProductoRepository proveedorProductoRepository;

    @Mock
    private Logger logger;

    @InjectMocks
    private ProveedorProductoService proveedorProductoService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testObtenerProductosAgrupados() {
        // Configura el repositorio para devolver una lista de productos de proveedor
        List<ProveedorProducto> proveedorProductos = ProveedorProductoDataProvider.getProveedorProductos();
        when(proveedorProductoRepository.findAll()).thenReturn(proveedorProductos);

        // Llama al método a probar
        Map<String, List<ProveedorProducto>> resultado = proveedorProductoService.obtenerProductosAgrupados();

        // Verifica que el resultado no sea nulo
        assertNotNull(resultado);

        // Verifica que el resultado esté ordenado correctamente y tenga la clave esperada
        assertTrue(resultado.containsKey("Producto 1 (Descripcion 1)"));
        assertEquals(2, resultado.size());

        // Verifica que se llamó al método findAll del repositorio
        verify(proveedorProductoRepository).findAll();
    }

    @Test
    void testObtenerProductosAgrupadosException() {
        // Configura el repositorio para lanzar una excepción al obtener productos
        when(proveedorProductoRepository.findAll()).thenThrow(new RuntimeException("Error simulado"));

        // Llama al método a probar
        Map<String, List<ProveedorProducto>> resultado = proveedorProductoService.obtenerProductosAgrupados();

        // Verifica que el resultado sea null debido a la excepción
        assertNull(resultado);

        // Verifica que el logger registre el mensaje de erro
    }

    @Test
    void testListarPorNombre() {
        // Configura el repositorio para devolver una lista de productos de proveedor con un nombre específico
        String nombre = "Producto1";
        List<ProveedorProducto> proveedorProductos = ProveedorProductoDataProvider.getProveedorProductosPorNombre();    
        when(proveedorProductoRepository.buscarPorNombreProducto(nombre)).thenReturn(proveedorProductos);

        // Llama al método a probar
        Map<String, List<ProveedorProducto>> resultado = proveedorProductoService.listarPornombre(nombre);

        // Verifica que el resultado no sea nulo
        assertNotNull(resultado);

        // Verifica que el resultado contenga la clave esperada
        assertTrue(resultado.containsKey("Producto 1 (Descripcion 1)"));
        assertEquals(1, resultado.size());

        // Verifica que se llamó al método buscarPorNombreProducto del repositorio
        verify(proveedorProductoRepository).buscarPorNombreProducto(nombre);
    }

    @Test
    void testListarPorNombreException() {
        // Configura el repositorio para lanzar una excepción al buscar por nombre
        when(proveedorProductoRepository.buscarPorNombreProducto("Producto1")).thenThrow(new RuntimeException("Error simulado"));

        // Llama al método a probar
        Map<String, List<ProveedorProducto>> resultado = proveedorProductoService.listarPornombre("Producto1");

        // Verifica que el resultado sea null debido a la excepción
        assertNull(resultado);

        // Verifica que el logger registre el mensaje de error
        verify(logger).error("Error al listar productos por nombre: Error simulado");
    }

    @Test
    void testAsignarPrecio() {
        // Configura el repositorio para encontrar un producto-proveedor específico y asignarle un precio
        ProveedorProducto proveedorProducto = ProveedorProductoDataProvider.getProveedorProducto();
        when(proveedorProductoRepository.encontrarPorProductoYProveedor(1, 2)).thenReturn(proveedorProducto);

        // Llama al método a probar
        boolean resultado = proveedorProductoService.asignarPrecio(1, 2, 1000);

        // Verifica que el resultado sea true indicando éxito
        assertTrue(resultado);

        // Verifica que el precio de compra se haya actualizado correctamente
        assertEquals(1000, proveedorProducto.getPrecioCompraProveedor());

        // Verifica que el método save haya sido llamado con el proveedorProducto actualizado
        verify(proveedorProductoRepository).save(proveedorProducto);
    }

    @Test
    void testAsignarPrecioException() {
        // Configura el repositorio para lanzar una excepción al intentar encontrar o guardar el producto
        when(proveedorProductoRepository.encontrarPorProductoYProveedor(1, 2)).thenThrow(new RuntimeException("Error simulado"));

        // Llama al método a probar
        boolean resultado = proveedorProductoService.asignarPrecio(1, 2, 1000);

        // Verifica que el resultado sea false debido a la excepción
        assertFalse(resultado);

        // Verifica que el logger registre el mensaje de error
        verify(logger).error("Error al crear el registro: Error simulado");
    }
}
