package madstodolist.dto;

import madstodolist.model.Categoria;
import madstodolist.model.Escritorio;
import madstodolist.model.Nota;

import java.util.Date;
import java.util.Objects;
import java.util.Set;

// Data Transfer Object para la clase Usuario
public class UsuarioData {

    private Long id;
    private String email;
    private String nombre;
    private String apellidos;
    private String contraseña;
    private Set<Nota> notasCompartidas;


    // Getters y setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public void setContraseña(String contraseña) { this.contraseña = contraseña; }

    public String getContraseña() { return contraseña; }

    public Set<Nota> getNotasCompartidas() {
        return notasCompartidas;
    }

    public void setNotasCompartidas(Set<Nota> notasCompartidas) {
        this.notasCompartidas = notasCompartidas;
    }

    // Sobreescribimos equals y hashCode para que dos usuarios sean iguales
    // si tienen el mismo ID (ignoramos el resto de atributos)

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UsuarioData)) return false;
        UsuarioData that = (UsuarioData) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
