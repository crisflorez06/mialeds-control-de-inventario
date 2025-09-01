package com.mialeds.services;

import com.mialeds.dataProviders.ProductoDataProvider;
import com.mialeds.models.Producto;
import com.mialeds.repositories.ProductoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository; // Mock del repositorio de productos para simular operaciones con la base de datos

    @Mock
    private Logger logger; // Mock del logger para capturar errores en las pruebas

    @InjectMocks
    private ProductoService productoService; // Inyecta los mocks en la instancia de ProductoService

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this); // Inicializa los mocks y las anotaciones de Mockito

        productoService.logger = logger; // Inyecta manualmente el mock del logger en productoService
    }

    // Test para verificar que se obtienen los productos escasos
    @Test
    void testProductosEscasos() {
        // Simula la lista de productos escasos que se espera recibir
        List<Producto> ProductosEsperados = ProductoDataProvider.getProductosEscasos();

        // Configura el mock para devolver la lista de productos escasos cuando se llame al método correspondiente
        when(productoRepository.findByCantidadExistenteMenorQue(4)).thenReturn(ProductosEsperados);

        // Llama al método productosEscasos() en ProductoService
        List<Producto> resultado = productoService.productosEscasos();

        // Verifica que el resultado no sea nulo y que tenga el tamaño esperado
        assertNotNull(resultado);
        assertEquals(ProductosEsperados, resultado);
        assertEquals(2, ProductosEsperados.size());

        // Verifica que el método findByCantidadExistenteMenorQue() haya sido llamado con el valor esperado
        verify(productoRepository).findByCantidadExistenteMenorQue(4);
    }

    // Test para verificar el manejo de excepciones en productos escasos
    @Test
    void testProductosEscasosException() {
        // Configura el mock para lanzar una excepción cuando se llama al método findByCantidadExistenteMenorQue
        when(productoRepository.findByCantidadExistenteMenorQue(4)).thenThrow(new RuntimeException("Error simulado"));

        // Llama al método productosEscasos() y espera un resultado nulo debido a la excepción
        List<Producto> resultado = productoService.productosEscasos();

        // Verifica que el resultado sea nulo
        assertNull(resultado);

        // Verifica que el logger haya registrado el error con el mensaje esperado
        verify(logger).error("Error al buscar productos escasos: Error simulado");
    }

    // Test para verificar que se obtienen los productos ordenados
    @Test
    void testListar() {
        // Simula la lista de productos ordenados que se espera recibir
        List<Producto> productosEsperados = ProductoDataProvider.getProductosOrdenados();
        
        // Configura el mock para devolver la lista ordenada cuando se llama al método correspondiente
        when(productoRepository.findAllByOrderByNombreAsc()).thenReturn(productosEsperados);

        // Llama al método listar() en ProductoService
        List<Producto> resultado = productoService.listar();

        // Verifica que el resultado no sea nulo y tenga el tamaño esperado
        assertNotNull(resultado);
        assertEquals(productosEsperados, resultado);
        assertEquals(4, resultado.size());

        // Verifica que el método findAllByOrderByNombreAsc() haya sido llamado
        verify(productoRepository).findAllByOrderByNombreAsc();
    }

    // Test para verificar el manejo de excepciones en listar
    @Test
    void testListarException() {
        // Configura el mock para lanzar una excepción cuando se llama al método findAllByOrderByNombreAsc
        when(productoRepository.findAllByOrderByNombreAsc()).thenThrow(new RuntimeException("Error simulado"));

        // Llama al método listar() y espera un resultado nulo debido a la excepción
        List<Producto> resultado = productoService.listar();

        // Verifica que el resultado sea nulo
        assertNull(resultado);

        // Verifica que el logger haya registrado el error con el mensaje esperado
        verify(logger).error("Error al listar los productos: Error simulado");
    }

    // Test para verificar que se obtienen los productos por nombre
    @Test
    void testListarPorNombre() {
        // Simula la lista de productos filtrados por nombre que se espera recibir
        List<Producto> productosEsperados = ProductoDataProvider.getProductosPorNombre();
        
        // Configura el mock para devolver la lista filtrada cuando se llama al método correspondiente
        when(productoRepository.findByNombreContaining("mobil")).thenReturn(productosEsperados);

        // Llama al método listarPorNombre() en ProductoService
        List<Producto> resultado = productoService.listarPorNombre("mobil");

        // Verifica que el resultado no sea nulo y tenga el tamaño esperado
        assertNotNull(resultado);
        assertEquals(productosEsperados, resultado);
        assertEquals(3, resultado.size());

        // Verifica que el método findByNombreContaining() haya sido llamado con el valor esperado
        verify(productoRepository).findByNombreContaining("mobil");
    }

    // Test para verificar el manejo de excepciones en listar por nombre
    @Test
    void testListarPorNombreException() {
        // Configura el mock para lanzar una excepción cuando se llama al método findByNombreContaining
        when(productoRepository.findByNombreContaining("terpel")).thenThrow(new RuntimeException("Error simulado"));

        // Llama al método listarPorNombre() y espera un resultado nulo debido a la excepción
        List<Producto> resultado = productoService.listarPorNombre("terpel");

        // Verifica que el resultado sea nulo
        assertNull(resultado);

        // Verifica que el logger haya registrado el error con el mensaje esperado
        verify(logger).error("Error al buscar productos por nombre: Error simulado");
    }

    // Test para verificar que se obtiene un producto por su id
    @Test
    void testBuscarPorId() {
        // Simula un producto específico que se espera recibir
        Optional<Producto> productoEsperado = ProductoDataProvider.getProductoPorId();
        
        // Configura el mock para devolver el producto esperado cuando se llama al método correspondiente
        when(productoRepository.findById(1)).thenReturn(productoEsperado);

        // Llama al método buscarPorId() en ProductoService
        Producto respuesta = productoService.buscarPorId(1);

        // Verifica que la respuesta no sea nula y tenga los datos esperados
        assertNotNull(respuesta);
        assertEquals(1, respuesta.getIdProducto());

        // Verifica que el método findById() haya sido llamado con el valor esperado
        verify(productoRepository).findById(1);
    }

    // Test para verificar que buscarPorId retorna null cuando no se encuentra el producto
    @Test
    void testBuscarPorIdNoEncontrado() {
        // Configura el mock para devolver un Optional vacío cuando se llama al método findById
        when(productoRepository.findById(1)).thenReturn(Optional.empty());

        // Llama al método buscarPorId() y espera un resultado nulo
        Producto resultado = productoService.buscarPorId(1);

        // Verifica que el resultado sea nulo
        assertNull(resultado);

        // Verifica que el método findById del repositorio haya sido llamado con el ID correcto
        verify(productoRepository).findById(1);
    }

    // Test para verificar el manejo de excepciones en buscar por id
    @Test
    void testBuscarPorIdException() {
        // Configura el mock para lanzar una excepción cuando se llama al método findById
        when(productoRepository.findById(1)).thenThrow(new RuntimeException("Error simulado"));

        // Llama al método buscarPorId() y espera un resultado nulo debido a la excepción
        Producto resultado = productoService.buscarPorId(1);

        // Verifica que el resultado sea nulo
        assertNull(resultado);

        // Verifica que el logger haya registrado el error con el mensaje esperado
        verify(logger).error("Error al buscar producto por id: Error simulado");
    }

    // Test para verificar que se guarda un producto
    @Test
    void testGuardar() {

        // Se obtiene un nuevo producto a través del data provider
        Producto productoNuevo = ProductoDataProvider.getNuevoProducto();

        // Se simula la operación de guardado en el repositorio, retornando el producto esperado
        when(productoRepository.save(productoNuevo)).thenReturn(productoNuevo);

        // Se llama al método guardar del servicio para guardar el producto
        Producto resultado = productoService.guardar(productoNuevo);

        // Se verifica que el resultado no sea nulo y que el nombre del producto sea el esperado
        assertNotNull(resultado);
        assertEquals("liquido frenos", resultado.getNombre());

        // Se verifica que el método save del repositorio haya sido llamado con el producto nuevo
        verify(productoRepository).save(productoNuevo);
    }

    // Test para verificar el manejo de excepciones al guardar un producto
    @Test
    void testGuardarException() {

        // Se obtiene un nuevo producto a través del data provider
        Producto productoNuevo = ProductoDataProvider.getNuevoProducto();

        // Se simula que el método save del repositorio lanza una excepción
        when(productoRepository.save(productoNuevo)).thenThrow(new RuntimeException("Error simulado"));

        // Se llama al método guardar del servicio, esperando que devuelva null en caso de excepción 
        Producto resultado = productoService.guardar(productoNuevo);

        // Se verifica que el resultado sea null debido a la excepción
        assertNull(resultado);

        // Se verifica que el logger registre el mensaje de error
        verify(logger).error("Error al guardar el producto: Error simulado");
    }

    // Test para verificar que se actualiza un producto
    @Test
    void testActualizar() {

        // Se obtiene un producto existente a través del data provider
        Optional<Producto> productoActualizar = ProductoDataProvider.getProductoPorId();

        // Se simula la existencia del producto en el repositorio
        when(productoRepository.findById(1)).thenReturn(productoActualizar);

        // Se simula la operación de guardado en el repositorio, retornando el producto actualizado
        when(productoRepository.save(productoActualizar.get())).thenReturn(productoActualizar.get());

        // Se llama al método actualizar del servicio para actualizar el producto
        Producto resultado = productoService.actualizar(1, "ProductoActualizado", "PresentacionActualizada", 5000, 6000, 20);

        // Se verifica que el resultado no sea nulo y que el nombre del producto sea el actualizado
        assertNotNull(resultado);
        assertEquals("ProductoActualizado", resultado.getNombre());

        // Se verifica que el método findById del repositorio haya sido llamado con el ID correcto
        verify(productoRepository).findById(1);
        verify(productoRepository).save(productoActualizar.get());
    }

    // Test para verificar el manejo de excepciones al actualizar un producto
    @Test
    void ttestActualizarException(){
        // Se obtiene un producto existente a través del data provider
        when(productoRepository.findById(1)).thenReturn(Optional.empty());

        // Se llama al método actualizar del servicio para actualizar el producto
        Producto resultado = productoService.actualizar(1, "ProductoActualizado", "PresentacionActualizada", 5000, 6000, 20);

        // Se verifica que el resultado sea null y que el mensaje de error sea el esperado
        assertNull(resultado);
        verify(logger).error("Error al actualizar el producto");
    }

    // Test para verificar que se crea un producto
    @Test
    void testCrear() {

        // Se obtiene un nuevo producto a través del data provider
        Producto nuevoProducto = ProductoDataProvider.getNuevoProducto();

        // Se utiliza ArgumentCaptor para capturar el producto que se pasa al método save
        ArgumentCaptor<Producto> productoCaptor = ArgumentCaptor.forClass(Producto.class);

        // Se simula la operación de guardado en el repositorio, retornando el producto esperado
        when(productoRepository.save(any(Producto.class))).thenReturn(nuevoProducto);

        // Se llama al método crear del servicio para crear el producto con los datos del data provider
        Producto resultado = productoService.crear(
            ProductoDataProvider.getNuevoProducto().getNombre(), 
            ProductoDataProvider.getNuevoProducto().getPresentacion(), 
            ProductoDataProvider.getNuevoProducto().getPrecioCompra(), 
            ProductoDataProvider.getNuevoProducto().getPrecioVenta(), 
            ProductoDataProvider.getNuevoProducto().getCantidadExistente());

        // Se verifica que el resultado no sea nulo y que el nombre del producto sea el esperado
        assertNotNull(resultado);
        assertEquals("liquido frenos", resultado.getNombre());

        // Se verifica que el método save haya sido llamado con un producto capturado
        verify(productoRepository).save(productoCaptor.capture());

        // Se comprueba que el nombre del producto capturado sea el esperado
        assertEquals("liquido frenos", productoCaptor.getValue().getNombre());
    }

    // Test para verificar el manejo de excepciones al crear un producto
    @Test
    void testCrearException() {

        // Se obtiene un nuevo producto a través del data provider
        Producto nuevoProducto = ProductoDataProvider.getNuevoProducto();

        // Se configura el spy para lanzar una excepción al llamar al método guardar
        when(productoRepository.save(any(Producto.class))).thenThrow(new RuntimeException("Error simulado"));

        // Se llama al método crear, esperando que devuelva null en caso de excepción
        Producto resultado = productoService.crear(
            nuevoProducto.getNombre(), 
            nuevoProducto.getPresentacion(), 
            nuevoProducto.getPrecioCompra(), 
            nuevoProducto.getPrecioVenta(), 
            nuevoProducto.getCantidadExistente());

        // Se verifica que el resultado sea null debido a la excepción
        assertNull(resultado);

        // Se verifica que el logger registre el mensaje de error
        verify(logger).error("Error al crear el producto: Error simulado");
    }

    
    // Test para verificar que se elimina un producto
    @Test
    void testEliminar() {

        // Simula que el producto a eliminar existe en la base de datos
        Optional<Producto> productoEliminar = ProductoDataProvider.getProductoPorId();
        when(productoRepository.findById(1)).thenReturn(productoEliminar);

        // Simula el comportamiento de eliminar sin realizar ninguna acción (void)
        doNothing().when(productoRepository).deleteById(1);

        // Ejecuta el método eliminar del servicio
        productoService.eliminar(1);

        // Verifica que se llamó al método findById del repositorio exactamente una vez
        verify(productoRepository).findById(1);

        // Verifica que se llamó al método deleteById del repositorio exactamente una vez
        verify(productoRepository).deleteById(1);
    }

    // Test para verificar el manejo de excepciones al eliminar un producto
    @Test
    void testEliminarException() {

        // Simula que el producto a eliminar existe en la base de datos
        Optional<Producto> productoEliminar = ProductoDataProvider.getProductoPorId();
        when(productoRepository.findById(1)).thenReturn(productoEliminar);

        // Configura el repositorio para lanzar una excepción cuando se llama a deleteById
        doThrow(new RuntimeException("Error simulado")).when(productoRepository).deleteById(1);

        // Ejecuta el método eliminar del servicio
        productoService.eliminar(1);

        // Verifica que el logger registró un mensaje de error
        verify(logger).error("Error al eliminar el producto: Error simulado");
    }

    // Test para verificar que se realiza un movimiento de entrada (suma de cantidad)
    @Test
    void testMovimientoEntrada() {

        // Simula que el producto existe en la base de datos
        Optional<Producto> productoEsperado = ProductoDataProvider.getProductoParaMovimiento();
        when(productoRepository.findById(1)).thenReturn(productoEsperado);

        // Configura el repositorio para devolver el producto después de guardar
        when(productoRepository.save(productoEsperado.get())).thenReturn(productoEsperado.get());

        // Ejecuta el método movimiento con el tipo "entrada"
        Producto resultado = productoService.movimiento(1, 3, "entrada");

        // Verifica que el resultado no es null
        assertNotNull(resultado);

        // Verifica que la cantidad existente del producto haya aumentado en 3
        assertEquals(13, resultado.getCantidadExistente());

        // Verifica que se llamó al método findById del repositorio
        verify(productoRepository).findById(1);
    }

    // Test para verificar que se realiza un movimiento de salida (resta de cantidad)
    @Test
    void testMovimientoSalida() {

        // Simula que el producto existe en la base de datos
        Optional<Producto> productoEsperado = ProductoDataProvider.getProductoParaMovimiento();
        when(productoRepository.findById(1)).thenReturn(productoEsperado);

        // Configura el repositorio para devolver el producto después de guardar
        when(productoRepository.save(productoEsperado.get())).thenReturn(productoEsperado.get());

        // Ejecuta el método movimiento con el tipo "salida"
        Producto resultado = productoService.movimiento(1, 3, "salida");

        // Verifica que el resultado no es null
        assertNotNull(resultado);

        // Verifica que la cantidad existente del producto haya disminuido en 3
        assertEquals(7, resultado.getCantidadExistente());

        // Verifica que se llamó al método findById del repositorio
        verify(productoRepository).findById(1);
    }

    // Test para verificar que se realiza un movimiento sin registrar (debe retornar null)
    @Test
    void testMovimientoNull() {

        // Simula que el producto existe en la base de datos
        Optional<Producto> productoEsperado = ProductoDataProvider.getProductoParaMovimiento();
        when(productoRepository.findById(1)).thenReturn(productoEsperado);

        // Configura el repositorio para devolver el mismo producto al guardar
        when(productoRepository.save(productoEsperado.get())).thenReturn(productoEsperado.get());

        // Ejecuta el método movimiento con un tipo de movimiento no registrado
        Producto resultado = productoService.movimiento(1, 3, "otroTexto");

        // Verifica que el resultado sea null, ya que "otroTexto" no es un movimiento válido
        assertNull(resultado);

        // Verifica que se llamó al método findById del repositorio
        verify(productoRepository).findById(1);
    }    

    // Test para verificar el manejo de excepciones en movimiento
    @Test
    void testMovimientoException() {

        // Configura los datos del producto para simular la existencia en la base de datos
        Optional<Producto> movimientoProducto = ProductoDataProvider.getProductoParaMovimiento();

        // Simula que el repositorio encuentra el producto por su ID
        when(productoRepository.findById(1)).thenReturn(movimientoProducto);

        // Configura el spy para lanzar una excepción al intentar guardar
        when(productoRepository.save(movimientoProducto.get())).thenThrow(new RuntimeException("Error simulado"));

        // Ejecuta el método movimiento, que internamente llamará a guardar() y lanzará la excepción
        Producto resultado = productoService.movimiento(1, 3, "salida");

        // Verifica que el resultado sea null debido a la excepción
        assertNull(resultado);

        // Verifica que el logger registró el mensaje de error correspondiente
        verify(logger).error("Error al realizar el movimiento: Error simulado");
    }

    // Test para verificar que se obtienen los productos por nombre y presentación
    @Test
    void testListarPorNombreYPresentacion() {

        // Configura el repositorio para devolver una lista de productos esperada
        List<Object[]> productosEsperados = ProductoDataProvider.getProductosPorNombreObject();
        when(productoRepository.findIdNombreAndPresentacion()).thenReturn(productosEsperados);

        // Ejecuta el método listarIdNombrePresentacion del servicio
        List<Object[]> resultado = productoService.listarIdNombrePresentacion();

        // Verifica que el resultado no sea null
        assertNotNull(resultado);

        // Verifica que la cantidad de elementos en el resultado coincida con lo esperado
        assertEquals(3, resultado.size());

        // Verifica que el método findIdNombreAndPresentacion fue llamado en el repositorio
        verify(productoRepository).findIdNombreAndPresentacion();
    }

    // Test para verificar el manejo de excepciones al listar productos por nombre y presentación
    @Test
    void testListarPorNombreYPresentacionException() {

        // Configura el repositorio para lanzar una excepción al buscar productos
        when(productoRepository.findIdNombreAndPresentacion()).thenThrow(new RuntimeException("Error simulado"));

        // Ejecuta el método listarIdNombrePresentacion del servicio
        List<Object[]> resultado = productoService.listarIdNombrePresentacion();

        // Verifica que el resultado sea null debido a la excepción
        assertNull(resultado);

        // Verifica que el logger registró un mensaje de error indicando el problema
        verify(logger).error("Error al buscar nombre y presentacion: Error simulado");
    }
}