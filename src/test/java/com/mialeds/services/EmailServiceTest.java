package com.mialeds.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.mialeds.dataProviders.UsuarioDataProvider;
import com.mialeds.models.Usuario;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

import org.slf4j.Logger;

public class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private Logger logger;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);

        emailService.logger = logger;
    }
    
    @Test
    void testEnviarUsuarioNoExiste() {
        // Configuramos el servicio de usuario para devolver null
        when(usuarioService.buscarUsuarioAutenticacion("1007474345")).thenReturn(null);

        // Ejecutamos el método
        boolean resultado = emailService.enviarCorreo("12345", "correoPrueba@gmail.com");

        // Validamos que el método retorne false si el usuario no existe
        assertFalse(resultado);
    }

    @Test
    void testEnviarCorreoCorreoNoCoincide() {
        // Creamos un usuario con un correo diferente al destinatario
        Usuario usuarioPrueba = UsuarioDataProvider.usuarioPorCedula();

        // Configuramos el servicio de usuario para devolver este usuario
        when(usuarioService.buscarUsuarioAutenticacion("1007474345")).thenReturn(usuarioPrueba);

        // Ejecutamos el método
        boolean resultado = emailService.enviarCorreo("1007474345", "correoErroneo@gmail.com");

        // Validamos que el método retorne false si el correo no coincide
        assertFalse(resultado);
    }

    @SuppressWarnings("null")
    @Test
    void testEnviarCorreoExitoso() {
        // Creamos un usuario con el correo que coincide con el destinatario
        Usuario usuarioPrueba = UsuarioDataProvider.usuarioPorCedula();

        // Configuramos el servicio de usuario para devolver este usuario y simular una contraseña generada
        when(usuarioService.buscarUsuarioAutenticacion("1007474345")).thenReturn(usuarioPrueba);
        when(usuarioService.generarContraseña()).thenReturn("nuevaContraseña");

        // Configuramos el servicio de cambio de contraseña para que no haga nada (void)
        doNothing().when(usuarioService).cambiarContrasena("1007474345", "nuevaContraseña");

        // Simulamos el comportamiento del mailSender para el envío del mensaje
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        // Ejecutamos el método
        boolean resultado = emailService.enviarCorreo("1007474345", "correoPrueba@gmail.com");

        // Validamos que el método retorne true
        assertTrue(resultado);

        // Verificamos que se generó y cambió la contraseña
        verify(usuarioService).generarContraseña();
        verify(usuarioService).cambiarContrasena("1007474345", "nuevaContraseña");

        // Verificamos que el correo se envió con los parámetros correctos
        verify(mailSender).send(argThat((SimpleMailMessage mensaje) ->
                mensaje.getTo() != null && mensaje.getTo()[0].equals("correoPrueba@gmail.com") &&
                mensaje.getSubject() != null && mensaje.getSubject().equals("Cambiar contraseña") &&
                mensaje.getText() != null && mensaje.getText().contains("Contraseña temporal: nuevaContraseña")
        ));
    }

    @Test
    void testEnviarCorreoConError() {
        // Configuramos el servicio de usuario para devolver un usuario válido
        Usuario usuarioPrueba = UsuarioDataProvider.usuarioPorCedula();

        when(usuarioService.buscarUsuarioAutenticacion("1007474345")).thenReturn(usuarioPrueba);
        when(usuarioService.generarContraseña()).thenReturn("nuevaContraseña");

        // Configuramos el mailSender para lanzar una excepción
        doThrow(new RuntimeException("Error de correo")).when(mailSender).send(any(SimpleMailMessage.class));

        // Ejecutamos el método
        boolean resultado = emailService.enviarCorreo("1007474345", "correoPrueba@gmail.com");

        // Validamos que el método retorne true a pesar del error en el envío
        assertTrue(resultado);

        // Verificamos que el logger haya registrado el error
        verify(logger).error("Error al enviar el correo: Error de correo");
    }

    @SuppressWarnings("null")
    @Test
    void testEnviarCorreoAdministradorExitoso() {
        // Configuración de datos de prueba
        String clave = "nuevaClave";
        String cedula = "12345";
        Usuario usuario = UsuarioDataProvider.usuarioPorCedula();

        // Configuramos los mocks para devolver la cédula y el usuario correcto
        when(usuarioService.obtenerCedulaUsuarioSesion()).thenReturn(cedula);
        when(usuarioService.buscarUsuarioAutenticacion(cedula)).thenReturn(usuario);

        // Simulamos el comportamiento del mailSender para el envío del mensaje
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        // Ejecutamos el método
        boolean resultado = emailService.enviarCorreoAdministrador(clave);

        // Validamos que el método retorne true
        assertTrue(resultado);

        // Verificamos que se obtuvo la cédula del usuario en sesión
        verify(usuarioService).obtenerCedulaUsuarioSesion();

        // Verificamos que se buscó el usuario con la cédula obtenida
        verify(usuarioService).buscarUsuarioAutenticacion(cedula);

        // Verificamos que el correo se envió con los parámetros correctos
        verify(mailSender).send(argThat((SimpleMailMessage mensaje) ->
                mensaje.getTo() != null && mensaje.getTo()[0].equals("mialeds06@gmail.com") &&
                mensaje.getSubject() != null && mensaje.getSubject().equals("Deteccion cambio de contraseña") &&
                mensaje.getText() != null && mensaje.getText().contains("Nombre: cristian") &&
                mensaje.getText().contains("Cédula: " + cedula) &&
                mensaje.getText().contains("La nueva contraseña es:\n" + clave)
        ));
    }

    @Test
    void testEnviarCorreoAdministradorConError() {
        // Configuración de datos de prueba
        String clave = "nuevaClave";
        String cedula = "12345";
        Usuario usuario = UsuarioDataProvider.usuarioPorCedula();

        // Configuramos los mocks para devolver la cédula y el usuario correcto
        when(usuarioService.obtenerCedulaUsuarioSesion()).thenReturn(cedula);
        when(usuarioService.buscarUsuarioAutenticacion(cedula)).thenReturn(usuario);

        // Configuramos el mailSender para lanzar una excepción
        doThrow(new RuntimeException("Error de correo")).when(mailSender).send(any(SimpleMailMessage.class));

        // Ejecutamos el método
        boolean resultado = emailService.enviarCorreoAdministrador(clave);

        // Verificamos que el método retorne true, incluso si falla el envío de correo
        assertTrue(resultado);

        // Verificamos que el logger haya registrado el error
        verify(logger).error("Error al enviar el correo: Error de correo");
    }
}
