package com.mialeds.models;

import java.util.List;

import jakarta.persistence.*;
import lombok.*;
//tener en cuenta que el kardex le puede generar conflictos asi que tener el cuenta JsonIgnoreProperties
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private int idUsuario;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "cedula", nullable = false, length = 10, unique = true)
    private String cedula;

    @Column(name = "contrasena", nullable = false, length = 255)
    private String contrasena;

    @Column(name = "correo_electronico", nullable = false, length = 100)
    private String correoElectronico;

    @Column(name = "telefono", nullable = false, length = 10)
    private String telefono;

    @Column(name = "is_enabled")
    private boolean isEnabled;

    @Column(name = "account_No_Expired")
    private boolean accountNoExpired;

    @Column(name = "account_No_Locked")
    private boolean accountNoLocked;

    @Column(name = "credential_No_Expired")
    private boolean credentialNoExpired;

    @OneToMany(mappedBy = "usuario")
    private List<Kardex> kardexes;

    @ManyToOne
    @JoinColumn(name = "id_role")
    private Role role;
    

    
}
