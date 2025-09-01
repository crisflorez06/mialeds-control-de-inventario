package com.mialeds;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.mialeds.models.Usuario;
import com.mialeds.services.UsuarioService;

import java.util.Objects;


@SpringBootApplication
public class MialedsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MialedsApplication.class, args);
	}
 	@Bean
    CommandLineRunner init(UsuarioService usuarioService) {
        return args -> {
            /* Create PERMISSIONS */

            /* CREATE USERS */
            Usuario userAdmin = Usuario.builder()
                    .nombre("ADMIN")
                    .cedula("123456789")
					.contrasena("admin123")
					.correoElectronico("mialeds06@gmail.com")
					.telefono("123")
                    .isEnabled(true)
                    .accountNoExpired(true)
                    .accountNoLocked(true)
                    .credentialNoExpired(true)
                    .role(usuarioService.role("ADMIN"))
                    .build();

					Usuario user = Usuario.builder()
                    .nombre("USER")
                    .cedula("987654321")
					.contrasena("user123")
					.correoElectronico("mialeds06@gmail.com")
					.telefono("123")
                    .isEnabled(true)
                    .accountNoExpired(true)
                    .accountNoLocked(true)
                    .credentialNoExpired(true)
                    .role(usuarioService.role("USER"))
                    .build();

					Usuario admin1 = usuarioService.buscarUsuarioAutenticacion(userAdmin.getCedula());
					if (Objects.isNull(admin1)) {
						usuarioService.crearUsuario(userAdmin);
					}
					
					Usuario user1 = usuarioService.buscarUsuarioAutenticacion(user.getCedula());
					if (Objects.isNull(user1)) {
						usuarioService.crearUsuario(user);
					}
					

        };
    }
	
}
