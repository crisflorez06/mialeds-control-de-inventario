package com.mialeds.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.mialeds.models.Usuario;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmailService {

    // Inyectamos el JavaMailSender para enviar correos electrónicos
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UsuarioService usuarioService;

    protected Logger logger = LoggerFactory.getLogger(EmailService.class);

    public boolean enviarCorreo(String cedula, String destinatario) {

        try {
            // Creamos un mensaje de correo
            SimpleMailMessage mensaje = new SimpleMailMessage();
            // Buscamos el usuario por su cédula
            Usuario usuario = usuarioService.buscarUsuarioAutenticacion(cedula);
    
            // Si el usuario no existe o el correo no coincide con el destinatario, retornamos false, asi validamos los datos enviados desde el frontend
            if(usuario == null || !usuario.getCorreoElectronico().equals(destinatario)) {
                return false;
            }

            // Generamos una nueva contraseña para el usuario
            String claveNueva = usuarioService.generarContraseña();
            // Cambiamos la contraseña del usuario
            usuarioService.cambiarContrasena(cedula, claveNueva);
    
            // Configuramos el mensaje de correo
            mensaje.setFrom("mialeds06@gmail.com");
            mensaje.setTo(destinatario);
            mensaje.setSubject("Cambiar contraseña");
            mensaje.setText("Estimado usuario,\n\n" +
                        "Hemos recibido una solicitud para cambiar su contraseña.\n" +
                        "Por favor, recuerde cambiar su contraseña después de iniciar sesión.\n\n" +
                        "Para acceder a su cuenta, use la siguiente contraseña temporal:\n" +
                        "Contraseña temporal: " + claveNueva + "\n\n" +
                        "Una vez que haya iniciado sesión, le recomendamos que cambie su contraseña a una más segura.\n\n" +
                        "El equipo de soporte de MIALEDS");
    
            mailSender.send(mensaje);
        } catch (Exception e) {
            logger.error("Error al enviar el correo: " + e.getMessage());
        }

        return true;
    }

    public boolean enviarCorreoAdministrador(String clave) {

        try {
            // Creamos un mensaje de correo
            SimpleMailMessage mensaje = new SimpleMailMessage();
            // Buscamos el usuario por su cédula
            String cedula = usuarioService.obtenerCedulaUsuarioSesion();
            Usuario usuario = usuarioService.buscarUsuarioAutenticacion(cedula);
        
            // Configuramos el mensaje de correo
            mensaje.setFrom("mialeds06@gmail.com");
            mensaje.setTo("quirogaeds2023@gmail.com");
            mensaje.setSubject("Deteccion cambio de contraseña");
            mensaje.setText("Estimado Administrador,\n\n" +
                            "Se ha realizado un cambio de contraseña para el usuario:\n" +
                            "Nombre: " + usuario.getNombre() + "\n" +
                            "Cédula: " + cedula + "\n\n" +
                            "La nueva contraseña es:\n" 
                             + clave + "\n\n" +
                            "Saludos cordiales,\n" +
                            "El equipo de soporte de MIALEDS");

            mailSender.send(mensaje);
        } catch (Exception e) {
            logger.error("Error al enviar el correo: " + e.getMessage());
        }

        return true;
    }
    
}