package madstodolist.dto;

import madstodolist.model.Categoria;
import madstodolist.model.Escritorio;
import madstodolist.model.Usuario;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.LinkedHashSet;
import java.util.Set;

public class NotaData {

    @NotNull
    private Long idNota;
    @Size(max = 30)
    @NotNull
    private String titulo;
    @Size(max = 16777215)
    private String descripcion;

    @Size(max = 6)
    @NotNull
    private String color;

    private Usuario idCreador;
    private Escritorio idEscritorio;
    private Categoria idCategoria;

    public Long getIdNota() { return idNota; }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Usuario getIdCreador() {
        return idCreador;
    }

    public void setIdCreador(Usuario idCreador) {
        this.idCreador = idCreador;
    }

    public Escritorio getIdEscritorio() {
        return idEscritorio;
    }

    public void setIdEscritorio(Escritorio idEscritorio) {
        this.idEscritorio = idEscritorio;
    }

    public Categoria getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Categoria idCategoria) {
        this.idCategoria = idCategoria;
    }
}
