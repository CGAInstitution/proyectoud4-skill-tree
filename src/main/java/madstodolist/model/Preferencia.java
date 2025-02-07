package madstodolist.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "preferencias")
public class Preferencia {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id", nullable = false)
    private Usuario usuarios;

    @NotNull
    @Column(name = "modoOscuro", nullable = false)
    private Boolean modoOscuro = false;

    @NotNull
    @Column(name = "`tamañoFuente`", nullable = false)
    private Byte tamañoFuente;

    @Size(max = 2)
    @NotNull
    @Column(name = "idioma", nullable = false, length = 2)
    private String idioma;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(Usuario usuarios) {
        this.usuarios = usuarios;
    }

    public Boolean getModoOscuro() {
        return modoOscuro;
    }

    public void setModoOscuro(Boolean modoOscuro) {
        this.modoOscuro = modoOscuro;
    }

    public Byte getTamañoFuente() {
        return tamañoFuente;
    }

    public void setTamañoFuente(Byte tamañoFuente) {
        this.tamañoFuente = tamañoFuente;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

}