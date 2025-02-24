package madstodolist.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "escritorios", indexes = {
        @Index(name = "Escritorios", columnList = "id_Usuario")
})
public class Escritorio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 20)
    @NotNull
    @Column(name = "nombre", nullable = false, length = 20)
    private String nombre;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_Usuario", nullable = false)
    private Usuario idUsuario;

    @OneToMany(mappedBy = "idEscritorio")
    private Set<Nota> notas = new LinkedHashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Usuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuario idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Set<Nota> getNotas() {
        return notas;
    }

    public void setNotas(Set<Nota> notas) {
        this.notas = notas;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Escritorio)) return false;
        Escritorio escritorio = (Escritorio) o;
        if (escritorio.id == null || id == null) {
            return false;
        }
        if (Objects.equals(id, escritorio.id)) return true;
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}