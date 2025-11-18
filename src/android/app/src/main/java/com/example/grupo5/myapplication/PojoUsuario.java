package com.example.grupo5.myapplication;

// ------------------------------------------------------------------
// Fichero: PojoUsuario.java
// Autor: Pablo Chasi
// Fecha: 28/10/2025
// ------------------------------------------------------------------
// Descripción:
// Esta clase lo que pretende es ser un clase donde se guarde
// información basic del usuario: Nombre, Apellidos, Correo, Id
//-------------------------------------------------------------------
public class PojoUsuario {
    private String Id;
    private String Nombre;
    private String Apellidos;
    private String Email;

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

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}
