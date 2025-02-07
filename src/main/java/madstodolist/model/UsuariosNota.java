package madstodolist.model;

import javax.persistence.*;

@Entity
@Table(name = "usuarios_notas", indexes = {
        @Index(name = "idNota", columnList = "idNota")
})
public class UsuariosNota {
    @EmbeddedId
    private UsuariosNotaId id;

    @MapsId("idUsuario")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario idUsuario;

    @MapsId("idNota")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idNota", nullable = false)
    private Nota idNota;

    public UsuariosNotaId getId() {
        return id;
    }

    public void setId(UsuariosNotaId id) {
        this.id = id;
    }

    public Usuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuario idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Nota getIdNota() {
        return idNota;
    }

    public void setIdNota(Nota idNota) {
        this.idNota = idNota;
    }

}