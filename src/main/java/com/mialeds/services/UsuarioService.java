package com.mialeds.services;

import java.util.List;
import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mialeds.models.Role;
import com.mialeds.models.RoleEnum;
import com.mialeds.models.Usuario;
import com.mialeds.repositories.RoleRepository;
import com.mialeds.repositories.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(int id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public Role role(String roleName) {
        try {
            RoleEnum roleEnum = RoleEnum.valueOf(roleName.toUpperCase());
            // Busca el rol, y si no existe, lo crea y guarda en la base de datos.
            return roleRepository.findByRoleEnum(roleEnum)
                    .orElseGet(() -> {
                        Role nuevoRol = Role.builder().roleEnum(roleEnum).build();
                        return roleRepository.save(nuevoRol);
                    });
        } catch (IllegalArgumentException e) {
            logger.error("Rol '{}' no es un valor válido.", roleName, e);
            throw new RuntimeException("Rol no válido: " + roleName);
        } catch (Exception e) {
            logger.error("Error al obtener o crear el rol: {}", roleName, e);
            return null;
        }
    }

    public Usuario crearUsuario(Usuario usuario) {
        try {
            usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
            return usuarioRepository.save(usuario);
        } catch (Exception e) {
            logger.error("Error al crear el usuario con cédula: {}", usuario.getCedula(), e);
            return null;
        }
    }

    public Usuario buscarUsuarioAutenticacion(String cedula) {
        return usuarioRepository.findByCedulaQuery(cedula);
    }

    public Integer obtenerIdPorCedula(String cedula) {
        return usuarioRepository.findIdByCedula(cedula)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con cédula: " + cedula));
    }

    public Integer obtenerIdUsuarioSesion() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String cedula = authentication.getName();
        return obtenerIdPorCedula(cedula);
    }

    public String obtenerCedulaUsuarioSesion() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public Usuario obtenerInformacionUsuario() {
        try {
            Integer id = obtenerIdUsuarioSesion();
            return buscarPorId(id);
        } catch (Exception e) {
            logger.error("Error al obtener la información del usuario en sesión.", e);
            return null;
        }
    }

    public RoleEnum obtenerRolDelUsuario() {
        try {
            Usuario usuario = obtenerInformacionUsuario();
            if (usuario != null && usuario.getRole() != null) {
                return usuario.getRole().getRoleEnum();
            }
        } catch (Exception e) {
            logger.error("Error al obtener el rol del usuario en sesión.", e);
        }
        return null;
    }

    public boolean esAdmin() {
        RoleEnum rol = obtenerRolDelUsuario();
        return rol != null && rol == RoleEnum.ADMIN;
    }

    public Usuario actualizarUsuario(int id, String nombre, String cedula, String correo, String telefono) {
        try {
            Usuario usuario = buscarPorId(id);
            if (usuario == null) {
                throw new RuntimeException("Usuario no encontrado para actualizar con id: " + id);
            }
            usuario.setNombre(nombre);
            usuario.setCedula(cedula);
            usuario.setCorreoElectronico(correo);
            usuario.setTelefono(telefono);
            return usuarioRepository.save(usuario);
        } catch (Exception e) {
            logger.error("Error al actualizar el usuario con id: {}", id, e);
            return null;
        }
    }

    public void cambiarContrasena(int id, String claveVieja, String claveNueva) {
        Usuario usuario = buscarPorId(id);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario no encontrado.");
        }
        if (!passwordEncoder.matches(claveVieja, usuario.getContrasena())) {
            throw new IllegalArgumentException("La contraseña antigua no coincide.");
        }
        usuario.setContrasena(passwordEncoder.encode(claveNueva));
        usuarioRepository.save(usuario);
    }

    public void cambiarContrasena(String cedula, String claveNueva) {
        try {
            Usuario usuario = buscarUsuarioAutenticacion(cedula);
            if (usuario == null) {
                throw new RuntimeException("Usuario no encontrado con cédula: " + cedula);
            }
            usuario.setContrasena(passwordEncoder.encode(claveNueva));
            usuarioRepository.save(usuario);
        } catch (Exception e) {
            logger.error("Error al cambiar la contraseña para cédula: {}", cedula, e);
        }
    }

    public String generarContraseña() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            sb.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }
        return sb.toString();
    }
}
