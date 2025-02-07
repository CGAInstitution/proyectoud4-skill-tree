package madstodolist.model;

import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Embeddable
public class UsuariosNotaId implements java.io.Serializable {
    private static final long serialVersionUID = -1587667124465091289L;
    @NotNull
    @Column(name = "idUsuario", nullable = false)
    private Integer idUsuario;

    @NotNull
    @Column(name = "idNota", nullable = false)
    private Integer idNota;

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getIdNota() {
        return idNota;
    }

    public void setIdNota(Integer idNota) {
        this.idNota = idNota;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UsuariosNotaId entity = (UsuariosNotaId) o;
        return Objects.equals(this.idUsuario, entity.idUsuario) &&
                Objects.equals(this.idNota, entity.idNota);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUsuario, idNota);
    }

}