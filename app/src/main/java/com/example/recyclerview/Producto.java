package com.example.recyclerview;

/** Representa un registro de la tabla productos. */
public class Producto {

    public final String clave;
    public final String nombre;
    public final String descripcion;
    public final double precio;
    public final int existencias;
    public final String categoria;

    public Producto(String clave, String nombre, String descripcion,
                    double precio, int existencias, String categoria) {
        this.clave = clave;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.existencias = existencias;
        this.categoria = categoria;
    }
}