package com.mialeds.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PrincipalController extends UsuarioDatosController {

    @GetMapping("/principal")
    public String principal() {
        return "principal";
    }
}
