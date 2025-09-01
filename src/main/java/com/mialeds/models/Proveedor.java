package com.mialeds.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "proveedor")
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proveedor")
    private int idProveedor;

    @Column(name = "nombre", nullable = false, length = 250)
    private String nombre;

    @Column(name = "NIT", nullable = false, length = 15, unique = true)
    private String nit;

    @Column(name = "correo_electronico", nullable = false, length = 250)
    private String correoElectronico;

    @Column(name = "telefono", nullable = false, length = 10)
    private String telefono;

    // Relación uno a muchos con ProveedorProducto
    @JsonIgnore
    @OneToMany(mappedBy = "proveedor",  cascade = CascadeType.REMOVE)
    private List<ProveedorProducto> proveedorProductos;

    public Proveedor() {
    }

    public Proveedor(int idProveedor, String nombre, String nit, String correoElectronico, String telefono) {
        this.idProveedor = idProveedor;
        this.nombre = nombre;
        this.nit = nit;
        this.correoElectronico = correoElectronico;
        this.telefono = telefono;
    }

    public Proveedor(String nombre, String nit, String correoElectronico, String telefono) {
        this.nombre = nombre;
        this.nit = nit;
        this.correoElectronico = correoElectronico;
        this.telefono = telefono;
    }    

    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public List<ProveedorProducto> getProveedorProductos() {
        return proveedorProductos;
    }

    public void setProveedorProductos(List<ProveedorProducto> proveedorProductos) {
        this.proveedorProductos = proveedorProductos;
    }

}
