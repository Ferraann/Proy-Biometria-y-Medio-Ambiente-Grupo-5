package com.example.grupo5.myapplication;

// ------------------------------------------------------------------
// Fichero: PojoUsuario.java
// Autor: Pablo Chasi
// Fecha: 28/10/2025
// ------------------------------------------------------------------
// Descripción:
// Esta clase lo que pretende es ser un clase donde se guarde
// información basica del usuario: Nombre, Apellidos, Correo, Id
//-------------------------------------------------------------------
public class PojoUsuario {
    private String Id;
    private String Nombre;
    private String Apellidos;
    private String gmail;
    private String password;
    private String Activo;
    private String accion;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getApellidos() {
        return Apellidos;
    }

    public void setApellidos(String apellidos) {
        Apellidos = apellidos;
    }

    public String getCorreo() {
        return gmail;
    }

    public void setCorreo(String correo) {
        gmail = correo;
    }

    public String getContrasenya() {
        return password;
    }

    public void setContrasenya(String contrasenya) { password = contrasenya; }

    public String getActivo() {
        return Activo;
    }

    public void setActivo(String activo) {
        Activo = activo;
    }

    public String getAction() {
        return accion;
    }

    public void setAction(String action) {
        accion = action;
    }
}
