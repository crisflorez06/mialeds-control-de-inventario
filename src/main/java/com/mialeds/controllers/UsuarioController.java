package com.mialeds.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;

import com.mialeds.models.Usuario;
import com.mialeds.services.EmailService;
import com.mialeds.services.UsuarioService;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EmailService emailService;

    @GetMapping("/crearUsuario")
    public String mostrarFormularioUsuario(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "formularioUsuario";
    }

    @PostMapping("/nuevoUsuario")
    public String crearUsuario(@RequestParam("usuario_nuevo_nombre") String nombre,
                             @RequestParam("usuario_nuevo_cedula") String cedula,
                             @RequestParam("usuario_nuevo_clave") String clave,
                             @RequestParam("usuario_nuevo_correo") String correo,
                             @RequestParam("usuario_nuevo_telefono") String telefono,
                             @RequestParam("usuario_nuevo_role") String role,
                             Model model) {
        try {
            Usuario user = Usuario.builder()
                    .nombre(nombre)
                    .cedula(cedula)
                    .contrasena(clave)
                    .correoElectronico(correo)
                    .telefono(telefono)
                    .isEnabled(true)
                    .accountNoExpired(true)
                    .accountNoLocked(true)
                    .credentialNoExpired(true)
                    .role(usuarioService.role(role))
                    .build();

            usuarioService.crearUsuario(user);
            model.addAttribute("mensaje", "Usuario creado correctamente");
        } catch (Exception e) {
            model.addAttribute("error", "Error al crear el usuario: " + e.getMessage());
        }
        return "redirect:/principal";
    }

    @PutMapping("/editarUsuario/{id}")
    public String editarUsuario(@PathVariable("id") int id,
                                @RequestParam("cambio_nombre") String nombre,
                                @RequestParam("cambio_cedula") String cedula,
                                @RequestParam("cambio_correo") String correo,
                                @RequestParam("cambio_telefono") String telefono,
                                Model model) {
        Usuario usuario = usuarioService.buscarPorId(id);
        try {
            usuarioService.actualizarUsuario(id, nombre, cedula, correo, telefono);
            model.addAttribute("mensaje", "Usuario editado correctamente");
        } catch (Exception e) {
            model.addAttribute("error", "Error al editar el usuario: " + e.getMessage());
        }

        // Si la cédula (username) ha cambiado, es necesario forzar un nuevo login para refrescar el objeto de seguridad.
        if (!usuario.getCedula().equals(cedula)) {
            return "redirect:/logout";
        }
        return "redirect:/principal";
    }

    @PutMapping("/cambiarClave/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> cambiarClave(@PathVariable("id") int id,
                                                          @RequestParam("clave_antigua") String claveAntigua,
                                                          @RequestParam("clave_nueva1") String claveNueva,
                                                          @RequestParam("clave_nueva2") String confirmacionClave) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (!claveNueva.equals(confirmacionClave)) {
                response.put("success", false);
                response.put("message", "Las nuevas contraseñas no coinciden");
                return ResponseEntity.ok(response);
            }

            usuarioService.cambiarContrasena(id, claveAntigua, claveNueva);
            emailService.enviarCorreoAdministrador(confirmacionClave);

            response.put("success", true);
            response.put("message", "Contraseña cambiada con éxito");

        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error inesperado al cambiar la contraseña.");
        }
        return ResponseEntity.ok(response);
    }
}
    


