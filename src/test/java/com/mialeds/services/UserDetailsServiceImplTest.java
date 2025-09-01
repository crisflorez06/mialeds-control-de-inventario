package com.mialeds.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.mialeds.models.Usuario;
import com.mialeds.models.Role;
import com.mialeds.models.RoleEnum;
import com.mialeds.repositories.UsuarioRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;



class UserDetailsServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadUserByUsernameUsuarioEncontrado() {
        // Configuración de datos de prueba
        Usuario usuario = new Usuario();
        usuario.setCedula("123456789");
        usuario.setContrasena("password");
        usuario.setEnabled(true);
        usuario.setAccountNoExpired(true);
        usuario.setAccountNoLocked(true);
        usuario.setCredentialNoExpired(true);

        // Configura los roles y permisos del usuario
        Role role = new Role();
        role.setRoleEnum(RoleEnum.USER);

        when(usuarioRepository.findByCedulaQuery("123456789")).thenReturn(usuario);

        // Llama al método a probar
        UserDetails userDetails = userDetailsService.loadUserByUsername("123456789");

        // Verifica que el usuario cargado tenga los detalles esperados
        assertNotNull(userDetails);
        assertEquals("123456789", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        assertTrue(userDetails.isEnabled());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());

        // Verifica que los roles y permisos se hayan añadido correctamente a las autoridades
        assertTrue(userDetails.getAuthorities().stream().anyMatch(
                authority -> authority.getAuthority().equals("ROLE_USER")));
        assertTrue(userDetails.getAuthorities().stream().anyMatch(
                authority -> authority.getAuthority().equals("READ")));

        // Verifica que se llamó al repositorio con el nombre de usuario correcto
        verify(usuarioRepository).findByCedulaQuery("123456789");
    }

    @Test
    void testLoadUserByUsernameUsuarioNoEncontrado() {
        // Configura el repositorio para devolver null cuando el usuario no se encuentra
        when(usuarioRepository.findByCedulaQuery("123456789")).thenReturn(null);

        // Llama al método y verifica que se lanza una excepción
        Exception exception = assertThrows(UsernameNotFoundException.class, () ->
                userDetailsService.loadUserByUsername("123456789"));

        // Verifica el mensaje de la excepción
        assertEquals("Usuario no encontrado", exception.getMessage());

        // Verifica que se llamó al repositorio con el nombre de usuario correcto
        verify(usuarioRepository).findByCedulaQuery("123456789");
    }
}
