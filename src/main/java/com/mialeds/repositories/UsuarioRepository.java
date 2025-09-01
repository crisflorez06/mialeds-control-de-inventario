package com.mialeds.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mialeds.models.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository <Usuario, Integer> {

    //metodo utilizado para manejar logica de SpringSecurity
    Optional<Usuario> findByCedula(String cedula);
    
    //se creo este archivo para un archivo de codigo diferente que necesita que retorne algo diferente
    @Query("SELECT u FROM Usuario u WHERE u.cedula = :cedula")
    Usuario findByCedulaQuery(@Param("cedula") String cedula);

    // método utilizado para encontrar el id del usuario por su cédula
    @Query("SELECT u.idUsuario FROM Usuario u WHERE u.cedula = :cedula")
    Optional<Integer> findIdByCedula(@Param("cedula") String cedula);



    
}
