package com.mialeds.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.mialeds.dataProviders.UsuarioDataProvider;
import com.mialeds.models.Role;
import com.mialeds.models.RoleEnum;
import com.mialeds.models.Usuario;
import com.mialeds.repositories.UsuarioRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.Authentication;


import java.util.List;
import java.util.Optional;

class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Logger logger;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UsuarioService usuarioService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
        
    }

    @Test
    void testListar() {
        // Configura el repositorio para devolver una lista de usuarios
        List<Usuario> usuarios = UsuarioDataProvider.usuarios();
        when(usuarioRepository.findAll()).thenReturn(usuarios);

        // Llama al método a probar
        List<Usuario> resultado = usuarioService.listar();

        // Verifica que el resultado no sea nulo y contenga los datos esperados
        assertNotNull(resultado);
        assertEquals(3, resultado.size());
        assertEquals("1007474345", resultado.get(0).getCedula());

        // Verifica que el método findAll del repositorio haya sido llamado
        verify(usuarioRepository).findAll();
    }

    @Test
    void testListarException() {
        // Configura el repositorio para lanzar una excepción
        when(usuarioRepository.findAll()).thenThrow(new RuntimeException("Error simulado"));

        // Llama al método a probar
        List<Usuario> resultado = usuarioService.listar();

        // Verifica que el resultado sea null debido a la excepción
        assertNull(resultado);

        // Verifica que el logger registre el mensaje de error
        verify(logger).error("Error al listar los usuarios: Error simulado");
    }

    @Test
    void testBuscarPorId() {
        // Configura el repositorio para devolver un usuario específico
        Usuario usuario = UsuarioDataProvider.usuarioPorId();
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));

        // Llama al método a probar
        Usuario resultado = usuarioService.buscarPorId(1);

        // Verifica que el resultado no sea nulo y contenga los datos esperados
        assertNotNull(resultado);
        assertEquals("1007474345", resultado.getCedula());

        // Verifica que el método findById del repositorio haya sido llamado con el ID correcto
        verify(usuarioRepository).findById(1);
    }

    @Test
    void testBuscarPorIdException() {
        // Configura el repositorio para lanzar una excepción
        when(usuarioRepository.findById(1)).thenThrow(new RuntimeException("Error simulado"));

        // Llama al método a probar
        Usuario resultado = usuarioService.buscarPorId(1);

        // Verifica que el resultado sea null debido a la excepción
        assertNull(resultado);

        // Verifica que el logger registre el mensaje de error
        verify(logger).error("Error al buscar el usuario: Error simulado");
    }
    @Test
    void testCrearUsuario() {
        // Configura el encriptador de contraseñas y el repositorio
        Usuario usuario = UsuarioDataProvider.usuarioPorCedula();
        usuario.setContrasena("clave"); // Asegúrate de establecer la contraseña original

        when(passwordEncoder.encode("clave")).thenReturn("claveCodificada");
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        // Llama al método a probar
        Usuario resultado = usuarioService.crearUsuario(usuario);

        // Verifica que el usuario se haya creado con la contraseña encriptada
        assertNotNull(resultado);
        assertEquals("claveCodificada", resultado.getContrasena());

        // Verifica que el método save del repositorio y el encoder se llamaron correctamente
        verify(passwordEncoder).encode("clave");
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void testCrearUsuarioException() {
        // Configura el repositorio para lanzar una excepción al guardar
        Usuario usuario = UsuarioDataProvider.usuarioPorCedula();
        when(passwordEncoder.encode("clave")).thenReturn("claveCodificada");
        when(usuarioRepository.save(usuario)).thenThrow(new RuntimeException("Error simulado"));

        // Llama al método a probar
        Usuario resultado = usuarioService.crearUsuario(usuario);

        // Verifica que el resultado sea null debido a la excepción
        assertNull(resultado);

        // Verifica que el logger registre el mensaje de error
        verify(logger).error("Error al crear el usuario: Error simulado");
    }

    @Test
    void testBuscarUsuarioAutenticacion() {
        // Configura el repositorio para devolver un usuario por su cédula
        Usuario usuario = UsuarioDataProvider.usuarioPorCedula();
        when(usuarioRepository.findByCedulaQuery("1007474345")).thenReturn(usuario);

        // Llama al método a probar
        Usuario resultado = usuarioService.buscarUsuarioAutenticacion("1007474345");

        // Verifica que el usuario se encontró correctamente
        assertNotNull(resultado);
        assertEquals("1007474345", resultado.getCedula());

        // Verifica que el método findByCedulaQuery del repositorio haya sido llamado
        verify(usuarioRepository).findByCedulaQuery("1007474345");
    }

    @Test
    void testBuscarUsuarioAutenticacionException() {
        // Configura el repositorio para lanzar una excepción
        when(usuarioRepository.findByCedulaQuery("1007474345")).thenThrow(new RuntimeException("Error simulado"));

        // Llama al método a probar
        Usuario resultado = usuarioService.buscarUsuarioAutenticacion("1007474345");

        // Verifica que el resultado sea null debido a la excepción
        assertNull(resultado);

        // Verifica que el logger registre el mensaje de error
        verify(logger).error("Error al buscar el usuario: Error simulado");
    }

    @Test
    void testObtenerIdPorCedula() {
        // Configura el repositorio para devolver un ID por cédula
        when(usuarioRepository.findIdByCedula("1007474345")).thenReturn(Optional.of(1));

        // Llama al método a probar
        int id = usuarioService.obtenerIdPorCedula("1007474345");

        // Verifica que el ID sea el esperado
        assertEquals(1, id);

        // Verifica que el método findIdByCedula del repositorio haya sido llamado
        verify(usuarioRepository).findIdByCedula("1007474345");
    }

    @Test
    void testObtenerIdPorCedulaUsuarioNoEncontrado() {
        // Configura el repositorio para devolver un Optional vacío
        when(usuarioRepository.findIdByCedula("1007474345")).thenReturn(Optional.empty());

        // Llama al método a probar y verifica que se lanza una excepción
        Exception exception = assertThrows(RuntimeException.class, () ->
                usuarioService.obtenerIdPorCedula("1007474345"));

        // Verifica el mensaje de la excepción
        assertEquals("Usuario no encontrado con cédula: 1007474345", exception.getMessage());

        // Verifica que el método findIdByCedula del repositorio haya sido llamado
        verify(usuarioRepository).findIdByCedula("1007474345");
    }

    @Test
    void testObtenerIdUsuarioSesion() {
        // Configura el contexto de seguridad para devolver el nombre de usuario y el ID
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("1007474345");
        SecurityContextHolder.setContext(securityContext);
        when(usuarioRepository.findIdByCedula("1007474345")).thenReturn(Optional.of(1));

        // Llama al método a probar
        int id = usuarioService.obtenerIdUsuarioSesion();

        // Verifica que el ID sea el esperado
        assertEquals(1, id);

        // Verifica que se llamaron los métodos necesarios
        verify(authentication).getName();
        verify(usuarioRepository).findIdByCedula("1007474345");
    }   

    @Test
    void testObtenerCedulaUsuarioSesion() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("1007474345");

        String cedula = usuarioService.obtenerCedulaUsuarioSesion();

        assertEquals("1007474345", cedula);
        verify(authentication).getName();
    }

    @Test
    void testObtenerInformacionUsuario() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("1007474345");
        Usuario usuario = UsuarioDataProvider.usuarioPorCedula();
        when(usuarioRepository.findIdByCedula("1007474345")).thenReturn(Optional.of(1));
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));

        Usuario resultado = usuarioService.obtenerInformacionUsuario();

        assertNotNull(resultado);
        assertEquals("1007474345", resultado.getCedula());
    }

    @Test
    void testObtenerRolDelUsuario() {
        // Crea un usuario con un rol ADMIN para la prueba
        Usuario usuario = new Usuario();
        Role role = new Role();
        role.setRoleEnum(RoleEnum.ADMIN);
        usuario.setRole(role);
    
        // Configura el método `obtenerInformacionUsuario` para devolver el usuario con el rol ADMIN
        UsuarioService spyUsuarioService = spy(usuarioService);
        doReturn(usuario).when(spyUsuarioService).obtenerInformacionUsuario();
    
        // Llama al método a probar
        RoleEnum rol = spyUsuarioService.obtenerRolDelUsuario();
    
        // Verifica que el rol obtenido sea ADMIN
        assertEquals(RoleEnum.ADMIN, rol);
    }

    @Test
    void testObtenerRolDelUsuarioException() {
        // Configura el método `obtenerInformacionUsuario` para lanzar una excepción
        UsuarioService spyUsuarioService = spy(usuarioService);
        doThrow(new RuntimeException("Error simulado")).when(spyUsuarioService).obtenerInformacionUsuario();
    
        // Llama al método a probar
        RoleEnum rol = spyUsuarioService.obtenerRolDelUsuario();
    
        // Verifica que el rol obtenido sea null
        assertNull(rol);
    
        // Verifica que el logger registre el mensaje de error
        verify(logger).error("Error al obtener el rol del usuario: Error simulado");
    }
    

    @Test
    void testEsAdmin() {
        // Crea un usuario con un rol ADMIN para la prueba
        Usuario usuario = new Usuario();
        Role role = new Role();
        role.setRoleEnum(RoleEnum.ADMIN);
        usuario.setRole(role);
    
        // Configura un spy en `usuarioService` para simular `obtenerInformacionUsuario`
        UsuarioService spyUsuarioService = spy(usuarioService);
        doReturn(usuario).when(spyUsuarioService).obtenerInformacionUsuario();
    
        // Llama al método a probar y verifica que devuelve true si el rol es ADMIN
        assertTrue(spyUsuarioService.esAdmin());
    }
    

    @Test
    void testActualizarUsuario() {
        Usuario usuario = new Usuario();
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Usuario resultado = usuarioService.actualizarUsuario(1, "Nombre Actualizado", "CedulaActualizada", "correo@ejemplo.com", "123456789");

        assertNotNull(resultado);
        assertEquals("Nombre Actualizado", resultado.getNombre());
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void testActualizarUsuarioException() {
        when(usuarioRepository.findById(1)).thenThrow(new RuntimeException("Error simulado"));

        Usuario resultado = usuarioService.actualizarUsuario(1, "Nombre Actualizado", "CedulaActualizada", "prueba1@gmail.com", "123456789");

        assertNull(resultado);
        verify(logger).error( contains("Error al actualizar el usuario"));
    }

    @Test
    void testCambiarContrasenaUsuarioSesionCorrecta() {
        Usuario usuario = new Usuario();
        usuario.setContrasena("oldEncodedPassword");

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("oldPassword", "oldEncodedPassword")).thenReturn(true);
        when(passwordEncoder.encode("newPassword")).thenReturn("newEncodedPassword");

        usuarioService.cambiarContrasena(1, "oldPassword", "newPassword");

        verify(usuarioRepository).save(usuario);
        assertEquals("newEncodedPassword", usuario.getContrasena());
    }

    @Test
    void testCambiarContrasenaUsuarioSesionIncorrecta() {
        Usuario usuario = new Usuario();
        usuario.setContrasena("oldEncodedPassword");

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("wrongOldPassword", "oldEncodedPassword")).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.cambiarContrasena(1, "wrongOldPassword", "newPassword");
        });

        assertEquals("La contraseña antigua no coincide", exception.getMessage());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void testCambiarContrasenaSinSesion() {
        Usuario usuario = UsuarioDataProvider.usuarioPorId();

        when(usuarioRepository.findByCedulaQuery("123456789")).thenReturn(usuario);
        when(passwordEncoder.encode("newPassword")).thenReturn("newEncodedPassword");

        usuarioService.cambiarContrasena("123456789", "newPassword");

        verify(usuarioRepository).save(usuario);
        assertEquals("newEncodedPassword", usuario.getContrasena());
    }

    @Test
    void testCambiarContrasenaSinSesionException() {
        when(usuarioRepository.findByCedulaQuery("123456789")).thenThrow(new RuntimeException("Error simulado"));

        usuarioService.cambiarContrasena("123456789", "newPassword");

        verify(logger).error(contains("Error al cambiar la contraseña"));
    }

    @Test
    void testGenerarContraseña() {
        String contraseña = usuarioService.generarContraseña();

        assertNotNull(contraseña);
        assertEquals(8, contraseña.length());
        assertTrue(contraseña.matches("[A-Za-z0-9]+"));
    }    
}
