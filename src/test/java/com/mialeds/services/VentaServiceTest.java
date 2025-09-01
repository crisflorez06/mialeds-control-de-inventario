package com.mialeds.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.mialeds.dataProviders.ProductoDataProvider;
import com.mialeds.dataProviders.UsuarioDataProvider;
import com.mialeds.dataProviders.VentaDataProvider;
import com.mialeds.models.Producto;
import com.mialeds.models.Usuario;
import com.mialeds.models.Venta;
import com.mialeds.repositories.VentaRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

class VentaServiceTest {

    @Mock
    private VentaRepository ventaRepository;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private ProductoService productoService;

    @Mock
    private Logger logger;

    @InjectMocks
    private VentaService ventaService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        
        
    }

    @Test
    void testListar() {
        List<Venta> ventas = VentaDataProvider.getVentas();
        when(ventaRepository.findAllByOrderByFechaDesc()).thenReturn(ventas);

        List<Venta> resultado = ventaService.listar();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(ventaRepository).findAllByOrderByFechaDesc();
    }

    @Test
    void testListarException() {
        when(ventaRepository.findAllByOrderByFechaDesc()).thenThrow(new RuntimeException("Error simulado"));

        List<Venta> resultado = ventaService.listar();

        assertNull(resultado);
        verify(logger).error("Error al listar las ventas: Error simulado");
    }

    @Test
    void testListarPorProducto() {
        List<Venta> ventas = VentaDataProvider.getVentas();
        when(ventaRepository.findByProducto_NombreContaining("Producto")).thenReturn(ventas);

        List<Venta> resultado = ventaService.listarPorProducto("Producto");

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(ventaRepository).findByProducto_NombreContaining("Producto");
    }

    @Test
    void testListarPorProductoException() {
        when(ventaRepository.findByProducto_NombreContaining("Producto")).thenThrow(new RuntimeException("Error simulado"));

        List<Venta> resultado = ventaService.listarPorProducto("Producto");

        assertNull(resultado);
        verify(logger).error("Error al listar las ventas: Error simulado");
    }

    @Test
    void testListarPorFecha() {
        List<Venta> ventas = VentaDataProvider.getVentas();
        when(ventaRepository.findByFecha(LocalDate.now())).thenReturn(ventas);

        List<Venta> resultado = ventaService.listarPorFecha(LocalDate.now());

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(ventaRepository).findByFecha(LocalDate.now());
    }

    @Test
    void testListarPorFechaException() {
        when(ventaRepository.findByFecha(LocalDate.now())).thenThrow(new RuntimeException("Error simulado"));

        List<Venta> resultado = ventaService.listarPorFecha(LocalDate.now());

        assertNull(resultado);
        verify(logger).error("Error al listar las ventas: Error simulado");
    }

    @Test
    void testObtenerVentasPorProductoYFecha() {
        String nombre = "Producto A";
        LocalDate fecha = LocalDate.of(2023, 11, 1);
        List<Venta> ventasEsperadas = VentaDataProvider.getVentas();

        when(ventaRepository.findByProducto_NombreContainingAndFecha(nombre, fecha)).thenReturn(ventasEsperadas);

        List<Venta> resultado = ventaService.obtenerVentas(nombre, fecha);

        assertEquals(ventasEsperadas, resultado);
        verify(ventaRepository).findByProducto_NombreContainingAndFecha(nombre, fecha);
    }

    @Test
    void testObtenerVentasPorProducto() {
        String nombre = "Producto B";
        List<Venta> ventasEsperadas = VentaDataProvider.getVentas();

        when(ventaRepository.findByProducto_NombreContaining(nombre)).thenReturn(ventasEsperadas);

        List<Venta> resultado = ventaService.obtenerVentas(nombre, null);

        assertEquals(ventasEsperadas, resultado);
        verify(ventaRepository).findByProducto_NombreContaining(nombre);
    }

    @Test
    void testObtenerVentasPorFecha() {
        LocalDate fecha = LocalDate.of(2023, 10, 15);
        List<Venta> ventasEsperadas = VentaDataProvider.getVentas();

        when(ventaRepository.findByFecha(fecha)).thenReturn(ventasEsperadas);

        List<Venta> resultado = ventaService.obtenerVentas(null, fecha);

        assertEquals(ventasEsperadas, resultado);
        verify(ventaRepository).findByFecha(fecha);
    }


    @Test
    void testListarPorProductoYFecha() {
    // Obtén una lista de ventas de prueba desde el DataProvider
    List<Venta> ventas = VentaDataProvider.getVentas();
    when(ventaRepository.findByProducto_NombreContainingAndFecha("Producto", LocalDate.now())).thenReturn(ventas);

    // Llama al método a probar
    List<Venta> resultado = ventaService.listarPorProductoYFecha("Producto", LocalDate.now());

    // Verifica que el resultado no sea nulo y contenga los datos esperados
    assertNotNull(resultado);
    assertEquals(ventas.size(), resultado.size());
    verify(ventaRepository).findByProducto_NombreContainingAndFecha("Producto", LocalDate.now());
}

    @Test
    void testListarPorProductoYFechaException() {
        // Configura el repositorio para lanzar una excepción
        when(ventaRepository.findByProducto_NombreContainingAndFecha("Producto", LocalDate.now()))
                .thenThrow(new RuntimeException("Error simulado"));

        // Llama al método y verifica que el resultado sea null debido a la excepción
        List<Venta> resultado = ventaService.listarPorProductoYFecha("Producto", LocalDate.now());

        assertNull(resultado);

        // Verifica que el logger registre el mensaje de error
        verify(logger).error("Error al listar las ventas: Error simulado");
    }

    @Test
    void testFormatearTotalVentasConVentas() {
        // Configura una lista de ventas con totales específicos a través del data provider
        List<Venta> ventas = VentaDataProvider.getVentas();

        // Calcula el total esperado en formato de moneda
        String totalEsperado = NumberFormat.getNumberInstance(Locale.forLanguageTag("es-CO")).format(300);

        // Llama al método a probar
        String totalFormateado = ventaService.formatearTotalVentas(ventas);

        // Verifica que el resultado sea el esperado
        assertEquals(totalEsperado, totalFormateado);
    }

    @Test
    void testFormatearTotalVentasSinVentas() {
        // Configura una lista vacía a través del data provider
        List<Venta> ventas = VentaDataProvider.ventasVacias();

        // Calcula el total esperado en formato de moneda (cero)
        String totalEsperado = NumberFormat.getNumberInstance(Locale.forLanguageTag("es-CO")).format(0.0);

        // Llama al método a probar
        String totalFormateado = ventaService.formatearTotalVentas(ventas);

        // Verifica que el resultado sea el esperado
        assertEquals(totalEsperado, totalFormateado);
    }

    @Test
    void testFormatearTotalVentasValoresDecimales() {
        // Configura una lista de ventas con valores decimales a través del data provider
        List<Venta> ventas = VentaDataProvider.getVentas();

        // Calcula el total esperado en formato de moneda
        String totalEsperado = NumberFormat.getNumberInstance(Locale.forLanguageTag("es-CO")).format(300);

        // Llama al método a probar
        String totalFormateado = ventaService.formatearTotalVentas(ventas);

        // Verifica que el resultado sea el esperado
        assertEquals(totalEsperado, totalFormateado);
    }    

    @Test
    void testGuardarVentaExistente() {
        // Configura los datos de la venta existente
        int idProducto = 1;
        int cantidad = 3;
        LocalDate fecha = LocalDate.now();
        Producto producto = ProductoDataProvider.getNuevoProducto();
        Usuario usuario = UsuarioDataProvider.usuarioPorId();
        Venta ventaExistente = VentaDataProvider.getVenta();

        when(productoService.buscarPorId(idProducto)).thenReturn(producto);
        when(usuarioService.obtenerIdUsuarioSesion()).thenReturn(usuario.getIdUsuario());
        when(usuarioService.buscarPorId(usuario.getIdUsuario())).thenReturn(usuario);
        when(ventaRepository.findByProductoAndFecha(producto, fecha)).thenReturn(ventaExistente);
        when(ventaRepository.save(ventaExistente)).thenReturn(ventaExistente);

        // Llama al método
        Venta resultado = ventaService.guardar(idProducto, cantidad, fecha);

        // Verificaciones
        assertNotNull(resultado);
        assertEquals(ventaExistente, resultado);
        assertEquals(ventaExistente.getCantidad(), resultado.getCantidad());
        assertEquals(ventaExistente.getTotalVenta(), resultado.getTotalVenta());
        verify(ventaRepository).save(ventaExistente);
        verify(productoService).guardar(producto);
    }

    @Test
    void testGuardarVentaProductoNoExistente() {
        // Configura el caso donde el producto no existe
        int idProducto = 1;
        int cantidad = 5;
        LocalDate fecha = LocalDate.now();

        when(productoService.buscarPorId(idProducto)).thenReturn(null);

        // Llama al método
        Venta resultado = ventaService.guardar(idProducto, cantidad, fecha);

        // Verifica que el resultado sea null y se registre el error
        assertNull(resultado);
        verify(logger).error("Error al guardar la venta: producto no encontrado");
    }


    @Test
    void testGuardarVentaConExcepcion() {
        // Configura un caso que lanza una excepción
        int idProducto = 1;
        int cantidad = 5;
        LocalDate fecha = LocalDate.now();
        Producto producto = ProductoDataProvider.getNuevoProducto();

        when(productoService.buscarPorId(idProducto)).thenReturn(producto);
        when(ventaRepository.save(any(Venta.class))).thenThrow(new RuntimeException("Error simulado"));

        // Llama al método
        Venta resultado = ventaService.guardar(idProducto, cantidad, fecha);

        // Verifica que el resultado sea null y se registre el error
        assertNull(resultado);
        verify(logger).error("Error al guardar la venta: Error simulado");
    }



    @Test
    void testObtenerTop5ProductosMasVendidos() {
        // Configura los datos simulados para el top 5 de productos más vendidos
        List<Object[]> top5ProductosEsperados = Collections.singletonList(new Object[]{"Producto1", 5});

        // Configura el mock para devolver los datos simulados cuando se llama al método
        LocalDate fechaInicio = LocalDate.now().minusMonths(1);
        when(ventaRepository.findTop5ProductosMasVendidos(fechaInicio)).thenReturn(top5ProductosEsperados);

        // Llama al método a probar
        List<Object[]> resultado = ventaService.obtenerTop5ProductosMasVendidos();

        // Verifica que el resultado sea el esperado
        assertEquals(top5ProductosEsperados, resultado);
        verify(ventaRepository).findTop5ProductosMasVendidos(fechaInicio);
    }

    @Test
    void testObtenerTop5ProductosMasVendidosConExcepcion() {
        // Configura el mock para lanzar una excepción al llamar al método
        LocalDate fechaInicio = LocalDate.now().minusMonths(1);
        when(ventaRepository.findTop5ProductosMasVendidos(fechaInicio)).thenThrow(new RuntimeException("Error simulado"));

        // Llama al método a probar
        List<Object[]> resultado = ventaService.obtenerTop5ProductosMasVendidos();

        // Verifica que el resultado sea null debido a la excepción
        assertNull(resultado);

        // Verifica que el logger registre el mensaje de error
        verify(logger).error("Error al obtener los 5 productos mas vendidos: Error simulado");
    }

    @Test
    void testObtenerTop5ProductosMenosVendidos() {
        // Configura el repositorio para devolver una lista de productos menos vendidos
        List<Object[]> productosMenosVendidos = Collections.singletonList(new Object[]{"Producto1", 5});
        LocalDate fechaInicio = LocalDate.now().minusMonths(1);
        when(ventaRepository.findTop5ProductosMenosVendidos(fechaInicio)).thenReturn(productosMenosVendidos);

        // Llama al método a probar
        List<Object[]> resultado = ventaService.obtenerTop5ProductosMenosVendidos();

        // Verifica que el resultado no sea nulo y contenga los datos esperados
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Producto1", resultado.get(0)[0]);
        assertEquals(5, resultado.get(0)[1]);

        // Verifica que el método en el repositorio haya sido llamado con la fecha correcta
        verify(ventaRepository).findTop5ProductosMenosVendidos(fechaInicio);
    }

    @Test
    void testObtenerTop5ProductosMenosVendidosException() {
        LocalDate fechaInicio = LocalDate.now().minusMonths(1);
        when(ventaRepository.findTop5ProductosMenosVendidos(fechaInicio)).thenThrow(new RuntimeException("Error simulado"));

        // Llama al método y verifica que el resultado sea null debido a la excepción
        List<Object[]> resultado = ventaService.obtenerTop5ProductosMenosVendidos();

        assertNull(resultado);

        // Verifica que el logger registre el mensaje de error
        verify(logger).error("Error al obtener los 5 productos menos vendidos: Error simulado");
    }

    @Test
    void testObtenerVentasUltimoMes() {
        List<Object[]> ventasUltimoMes = Collections.singletonList(new Object[]{LocalDate.now(), 10});
        LocalDate fechaInicio = LocalDate.now().minusMonths(1);
        when(ventaRepository.findVentasPorDiaUltimos30Dias(fechaInicio)).thenReturn(ventasUltimoMes);

        List<Object[]> resultado = ventaService.obtenerVentasUltimoMes();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(LocalDate.now(), resultado.get(0)[0]);
        assertEquals(10, resultado.get(0)[1]);

        verify(ventaRepository).findVentasPorDiaUltimos30Dias(fechaInicio);
    }

    @Test
    void testObtenerVentasUltimoMesException() {
        LocalDate fechaInicio = LocalDate.now().minusMonths(1);
        when(ventaRepository.findVentasPorDiaUltimos30Dias(fechaInicio)).thenThrow(new RuntimeException("Error simulado"));

        List<Object[]> resultado = ventaService.obtenerVentasUltimoMes();

        assertNull(resultado);

        verify(logger).error("Error al obtener las ventas de los ultimos 30 dias: Error simulado");
    }

    @Test
    void testObtenerGananciasUltimoMes() {
        List<Object[]> gananciasUltimoMes = Collections.singletonList(new Object[]{LocalDate.now(), 500.0});
        LocalDate fechaInicio = LocalDate.now().minusMonths(1);
        when(ventaRepository.findGananciasPorDiaUltimos30Dias(fechaInicio)).thenReturn(gananciasUltimoMes);

        List<Object[]> resultado = ventaService.obtenerGananciasUltimoMes();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(LocalDate.now(), resultado.get(0)[0]);
        assertEquals(500.0, resultado.get(0)[1]);

        verify(ventaRepository).findGananciasPorDiaUltimos30Dias(fechaInicio);
    }

    @Test
    void testObtenerGananciasUltimoMesException() {
        LocalDate fechaInicio = LocalDate.now().minusMonths(1);
        when(ventaRepository.findGananciasPorDiaUltimos30Dias(fechaInicio)).thenThrow(new RuntimeException("Error simulado"));

        List<Object[]> resultado = ventaService.obtenerGananciasUltimoMes();

        assertNull(resultado);

        verify(logger).error("Error al obtener las ganancias de los ultimos 30 dias: Error simulado");
    }
}
