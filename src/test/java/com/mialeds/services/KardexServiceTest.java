package com.mialeds.services;

import com.mialeds.dataProviders.KardexDataProvider;
import com.mialeds.models.Kardex;
import com.mialeds.models.Producto;
import com.mialeds.models.Usuario;
import com.mialeds.repositories.KardexRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.slf4j.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public class KardexServiceTest {

    @Mock
    private KardexRepository kardexRepository;

    @Mock
    private ProductoService productoService;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private Logger logger;

    @InjectMocks
    private KardexService kardexService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);

        kardexService.logger = logger;
    }

        @Test
    void testListar() {

        List<Kardex> kardexsEsperados = KardexDataProvider.getKardexList();

        when(kardexRepository.findAll()).thenReturn(kardexsEsperados);
        // Configura el comportamiento del repositorio para devolver una lista vacía

        // Llama al método a probar
        List<Kardex> resultado = kardexService.listar();

        assertNotNull(resultado);
        assertEquals(kardexsEsperados, resultado);
        verify(kardexRepository).findAll();
    }

    @Test
    void testListarException() {
        // Simula una excepción en el repositorio
        when(kardexRepository.findAll()).thenThrow(new RuntimeException("Error simulado"));

        // Llama al método a probar
        List<Kardex> resultado = kardexService.listar();

        // Verifica que el resultado sea null debido a la excepción
        assertNull(resultado);

        // Verifica que el logger registre el mensaje de error
        verify(logger).error("Error al listar los kardex: Error simulado");
    }

    @Test
    void testBuscarPorId() {
        // Configura el comportamiento del repositorio para devolver un objeto Kardex
        Kardex kardex = KardexDataProvider.getKardex();
        when(kardexRepository.findById(1)).thenReturn(Optional.of(kardex));

        // Llama al método a probar
        Kardex resultado = kardexService.buscarPorId(1);

        // Verifica que el resultado sea el esperado
        assertNotNull(resultado);
        assertEquals(kardex, resultado);

        // Verifica que el método `findById` del repositorio haya sido llamado con el ID correcto
        verify(kardexRepository).findById(1);
    }

    @Test
    void testBuscarPorIdException() {
        // Simula una excepción en el repositorio
        when(kardexRepository.findById(1)).thenThrow(new RuntimeException("Error simulado"));

        // Llama al método a probar
        Kardex resultado = kardexService.buscarPorId(1);

        // Verifica que el resultado sea null debido a la excepción
        assertNull(resultado);

        // Verifica que el logger registre el mensaje de error
        verify(logger).error("Error al buscar el kardex: Error simulado");
    }

    @Test
    void testGuardar() {
        // Configura el comportamiento del repositorio para devolver el objeto guardado
        Kardex kardex = KardexDataProvider.getKardex();
        when(kardexRepository.save(kardex)).thenReturn(kardex);

        // Llama al método privado guardar a través de la reflexión
        Kardex resultado = kardexService.guardar(kardex);

        // Verifica que el resultado sea el esperado
        assertNotNull(resultado);
        assertEquals(kardex, resultado);

        // Verifica que el método `save` del repositorio haya sido llamado
        verify(kardexRepository).save(kardex);
    }

    @Test
    void testGuardarException() {
        // Configura el repositorio para lanzar una excepción
        Kardex kardex = new Kardex();
        when(kardexRepository.save(kardex)).thenThrow(new RuntimeException("Error simulado"));

        // Llama al método a probar
        Kardex resultado = kardexService.guardar(kardex);

        // Verifica que el resultado sea null debido a la excepción
        assertNull(resultado);

        // Verifica que el logger registre el mensaje de error
        verify(logger).error("Error al guardar el kardex: Error simulado");
    }

    @Test
    void testCrear() {
        // Configura el comportamiento de los servicios y repositorios para el caso exitoso
        Producto producto = new Producto();
        Usuario usuario = new Usuario();
        Kardex kardex = new Kardex( producto, usuario, "Entrada", LocalDate.now(), 10);

        ArgumentCaptor<Kardex> kardexCaptor = ArgumentCaptor.forClass(Kardex.class);

        when(productoService.buscarPorId(1)).thenReturn(producto);
        when(usuarioService.buscarPorId(1)).thenReturn(usuario);
        when(kardexRepository.save(any(Kardex.class))).thenReturn(kardex);

        // Llama al método a probar
        Kardex resultado = kardexService.crear(1, 1, "Entrada", LocalDate.now(), 10);

        // Verifica que los métodos dependientes hayan sido llamados
        verify(productoService).buscarPorId(1);
        verify(usuarioService).buscarPorId(1);
        verify(kardexRepository).save(kardexCaptor.capture());

        // Verifica que el resultado sea el esperado
        assertNotNull(resultado);
        assertEquals(kardex.getIdKardex(), kardexCaptor.getValue().getIdKardex());
    }

    @Test
    void testCrearException() {
        // Configura el servicio para lanzar una excepción
        when(productoService.buscarPorId(1)).thenThrow(new RuntimeException("Error simulado"));

        // Llama al método a probar
        Kardex resultado = kardexService.crear(1, 1, "Entrada", LocalDate.now(), 10);

        // Verifica que el resultado sea null debido a la excepción
        assertNull(resultado);

        // Verifica que el logger registre el mensaje de error
        verify(logger).error("Error al crear el kardex: Error simulado");
    }

    @Test
    void testListarPorMovimiento() {
        List <Kardex> kardexsEsperados = KardexDataProvider.getKardexListEntrada();
        // Configura el comportamiento del repositorio para devolver una lista vacía
        when(kardexRepository.findByMovimientoOrderByFechaDesc("Entrada")).thenReturn(kardexsEsperados);

        // Llama al método a probar
        List<Kardex> resultado = kardexService.listarPorMovimiento("Entrada");

        // Verifica que el resultado sea una lista vacía
        assertNotNull(resultado);
        assertEquals(kardexsEsperados, resultado);
        // Verifica que el método `findByMovimientoOrderByFechaDesc` del repositorio haya sido llamado
        verify(kardexRepository).findByMovimientoOrderByFechaDesc("Entrada");
    }

    @Test
    void testListarPorMovimientoException() {
        // Simula una excepción en el repositorio
        when(kardexRepository.findByMovimientoOrderByFechaDesc("Entrada")).thenThrow(new RuntimeException("Error simulado"));

        // Llama al método a probar
        List<Kardex> resultado = kardexService.listarPorMovimiento("Entrada");

        // Verifica que el resultado sea null debido a la excepción
        assertNull(resultado);

        // Verifica que el logger registre el mensaje de error
        verify(logger).error("Error al listar los kardex por movimiento: Error simulado");
    }
}
