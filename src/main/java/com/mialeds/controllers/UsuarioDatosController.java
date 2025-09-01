package com.mialeds.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.mialeds.services.UsuarioService;

//este controlador lo utilizamos para mostrar la informacion del usuario en cualquier vista, permitiendo cambiar la informacion sin importar el modulo que estemos ubicados
public class UsuarioDatosController {

    @Autowired
    private UsuarioService usuarioService;

    @ModelAttribute
    public void agregarAtributoUsuario(Model model) {
        try {
            model.addAttribute("usuario", usuarioService.obtenerInformacionUsuario()); 
            model.addAttribute("rol", usuarioService.obtenerRolDelUsuario());
            model.addAttribute("admin", usuarioService.esAdmin());
        } catch (Exception e) {
            model.addAttribute("error", "Error al obtener la información del usuario: " + e.getMessage());
        }
    }
}
