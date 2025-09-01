package com.mialeds.controllers; // Paquete que contiene los controladores de la aplicación


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller; // Importamos la anotación que define esta clase como un controlador
import org.springframework.web.bind.annotation.GetMapping; // Importamos la anotación para manejar solicitudes GET
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mialeds.services.EmailService;


// Esta clase es responsable de manejar las solicitudes relacionadas con el inicio de sesión 
@Controller
public class LoginController {

    @Autowired
    private EmailService emailService;

    // Método que maneja la solicitud GET para la página de inicio de sesión
    @GetMapping("/login")
    public String index() {
        return "index"; // Retorna el nombre de la vista que se mostrará (index.html)
    }

    //metodo para enviar un correo electronico
    @PostMapping("/restaurarClave")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> restaurarClave(
        @RequestParam("cedula_olvide_clave") String cedula,
        @RequestParam("correo_olvide_clave") String correo) {

        // Crear un mapa para almacenar la respuesta (success/failure y mensaje)
        Map<String, Object> response = new HashMap<>();

        try {
            // Intentar enviar el correo
            boolean respuesta = emailService.enviarCorreo(cedula, correo);

            // Si el correo se envía correctamente
            if (respuesta) {
                response.put("success", true);
                response.put("message", "Correo de recuperación enviado con éxito. Por favor revise su correo electrónico.");
            } else {
                response.put("success", false);
                response.put("message", "Error en las credenciales.");
            }
        } catch (Exception e) {
            // Capturar cualquier excepción y devolver un mensaje de error
            response.put("success", false);
            response.put("message", "Error al enviar el correo: " + e.getMessage());
        }

        // Retornar la respuesta como JSON
        return ResponseEntity.ok(response);
    }

}
