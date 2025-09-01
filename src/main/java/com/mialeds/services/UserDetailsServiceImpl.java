package com.mialeds.services; // Paquete que contiene los servicios de la aplicación

import java.util.ArrayList; // Importamos ArrayList para manejar listas de objetos
import java.util.List; // Importamos List para trabajar con colecciones de objetos

import org.springframework.beans.factory.annotation.Autowired; // Importamos la anotación para inyectar dependencias
import org.springframework.security.core.authority.SimpleGrantedAuthority; // Importamos la clase para manejar las autoridades de seguridad
import org.springframework.security.core.userdetails.User; // Importamos la clase User para representar al usuario autenticado
import org.springframework.security.core.userdetails.UserDetails; // Importamos la interfaz UserDetails para manejar detalles del usuario
import org.springframework.security.core.userdetails.UserDetailsService; // Importamos la interfaz que define el servicio para cargar detalles del usuario
import org.springframework.security.core.userdetails.UsernameNotFoundException; // Importamos la excepción para manejar usuarios no encontrados
import org.springframework.stereotype.Service; // Importamos la anotación que define esta clase como un servicio

import com.mialeds.models.Usuario; // Importamos el modelo Usuario
import com.mialeds.repositories.UsuarioRepository; // Importamos el repositorio de usuarios

// Esta clase es responsable de cargar los detalles del usuario para la autenticación
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    // Inyectamos el repositorio de usuarios para poder acceder a los datos del usuario
    @Autowired
    private UsuarioRepository usuarioRepository;

    // Método que carga un usuario a partir del nombre de usuario (en este caso, la cédula)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Buscamos el usuario en el repositorio usando la cédula
        Usuario usuario = usuarioRepository.findByCedulaQuery(username);
        if (usuario == null) {
            // Si el usuario no se encuentra, lanzamos una excepción
            throw new UsernameNotFoundException("Usuario no encontrado");
        }

        // Creamos una lista para almacenar las autoridades del usuario (roles y permisos)
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

        // Añadimos los roles del usuario a la lista de autoridades
        if (usuario.getRole() != null){
            authorityList.add(new SimpleGrantedAuthority("ROLE_" + usuario.getRole().getRoleEnum().name()));
        }

        // Retornamos un objeto User con la información del usuario y sus autoridades
        return new User(usuario.getCedula(), // Nombre de usuario
            usuario.getContrasena(), // Contraseña del usuario
            usuario.isEnabled(), // Si la cuenta está habilitada
            usuario.isAccountNoExpired(), // Si la cuenta no ha expirado
            usuario.isAccountNoLocked(), // Si la cuenta no está bloqueada
            usuario.isCredentialNoExpired(), // Si las credenciales no han expirado
            authorityList); // Lista de autoridades del usuario
    }
}
