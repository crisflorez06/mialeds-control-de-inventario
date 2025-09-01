package com.mialeds.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "proveedor_producto")
public class ProveedorProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; 

    @ManyToOne
    @JoinColumn(name = "id_proveedor", nullable = false)
    private Proveedor proveedor;

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @Column(name = "precio_compra_proveedor", nullable = false, columnDefinition = "int default 0")
    private int precioCompraProveedor;

    public ProveedorProducto() {
    }

    public ProveedorProducto(int id, Proveedor proveedor, Producto producto, int precioCompraProveedor) {
        this.id = id;
        this.proveedor = proveedor;
        this.producto = producto;
        this.precioCompraProveedor = precioCompraProveedor;
    }

    public ProveedorProducto(Proveedor proveedor, Producto producto, int precioCompraProveedor) {
        this.proveedor = proveedor;
        this.producto = producto;
        this.precioCompraProveedor = precioCompraProveedor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getPrecioCompraProveedor() {
        return precioCompraProveedor;
    }

    public void setPrecioCompraProveedor(int precioCompraProveedor) {
        this.precioCompraProveedor = precioCompraProveedor;
    }

}

