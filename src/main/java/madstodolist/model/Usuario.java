package madstodolist.model;

import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 50)
    @NotNull
    @Column(name = "email", nullable = false, length = 50, unique = true)
    private String email;

    @Size(max = 20)
    @NotNull
    @Column(name = "nombre", nullable = false, length = 20)
    private String nombre;

    @Size(max = 40)
    @NotNull
    @Column(name = "apellidos", nullable = false, length = 40)
    private String apellidos;

    @Size(max = 255)
    @NotNull
    @Column(name = "`contraseña`", nullable = false)
    private String contraseña;

    @OneToMany(mappedBy = "idUsuario")
    private Set<Categoria> categorias = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idUsuario")
    private Set<Escritorio> escritorios = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idCreador")
    private Set<Nota> notas = new LinkedHashSet<>();

    @OneToOne(mappedBy = "usuarios")
    private Preferencia preferencia;

    @ManyToMany
    @JoinTable(name = "usuarios_notas",
            joinColumns = @JoinColumn(name = "idUsuario"),
            inverseJoinColumns = @JoinColumn(name = "idNota"))
    private Set<Nota> notasCompartidas = new LinkedHashSet<>();

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

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public Set<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(Set<Categoria> categorias) {
        this.categorias = categorias;
    }

    public Set<Escritorio> getEscritorios() {
        return escritorios;
    }

    public void setEscritorios(Set<Escritorio> escritorios) {
        this.escritorios = escritorios;
    }

    public void addEscritorio(Escritorio escritorio) {
        this.escritorios.add(escritorio);
    }

    public Set<Nota> getNotas() {
        return notas;
    }

    public void setNotas(Set<Nota> notas) {
        this.notas = notas;
    }

    public Preferencia getPreferencia() {
        return preferencia;
    }

    public void setPreferencia(Preferencia preferencia) {
        this.preferencia = preferencia;
    }

    public Set<Nota> getNotasCompartidas() {
        return notasCompartidas;
    }

    public void setNotasCompartidas(Set<Nota> notasCompartidas) {
        this.notasCompartidas = notasCompartidas;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Usuario)) return false;
        Usuario usuario = (Usuario) o;
        if (Objects.equals(email, usuario.email)) return true;
        if (usuario.id == null || id == null) {
            return false;
        }
        if (Objects.equals(id, usuario.id)) return true;
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }
}