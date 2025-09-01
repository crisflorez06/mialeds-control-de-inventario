package com.mialeds.dataProviders;

import java.util.Arrays;
import java.util.List;

import com.mialeds.models.Usuario;

public class UsuarioDataProvider {
    
    public static Usuario usuarioPorCedula(){
        return new Usuario(7, "cristian", "1007474345", "123456", "correoPrueba@gmail.com", "1234567890", true, true, true, true, null, null);
    }

    public static List<Usuario> usuarios(){
        return Arrays.asList(
            new Usuario(1, "cristian", "1007474345", "123456", "correo1@gmail.com", "1234567890", true, true, true, true, null, null),
            new Usuario(2, "cristian", "1007474345", "123456", "correo2@gmail.com", "1234567890", true, true, true, true, null, null),
            new Usuario(3, "cristian", "1007474345", "123456", "correo3@gmail.com", "1234567890", true, true, true, true, null, null));
    }

    public static Usuario usuarioPorId(){
        return new Usuario(1, "cristian", "1007474345", "123456", "correoPrueba@gmail.com", "1234567890", true, true, true, true, null, null);
    }
}
