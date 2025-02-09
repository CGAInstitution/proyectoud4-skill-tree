package madstodolist.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "notas", indexes = {
        @Index(name = "idCreador", columnList = "id_Creador"),
        @Index(name = "idEscritorio", columnList = "id_Escritorio"),
        @Index(name = "idCategoria", columnList = "idCategoria")
})
public class Nota {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 30)
    @NotNull
    @Column(name = "titulo", nullable = false, length = 30)
    private String titulo;

    @Lob
    @Column(name = "descripcion")
    private String descripcion;

    @Size(max = 6)
    @NotNull
    @Column(name = "color", nullable = false, length = 6)
    private String color;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_Creador", nullable = false)
    private Usuario idCreador;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_Escritorio", nullable = false)
    private Escritorio idEscritorio;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idCategoria", nullable = false)
    private Categoria idCategoria;

    @ManyToMany
    @JoinTable(name = "usuarios_notas",
            joinColumns = @JoinColumn(name = "idNota"),
            inverseJoinColumns = @JoinColumn(name = "idUsuario"))
    private Set<Usuario> usuarios = new LinkedHashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Set<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(Set<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

}