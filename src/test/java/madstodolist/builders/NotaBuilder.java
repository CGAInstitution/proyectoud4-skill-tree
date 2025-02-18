package madstodolist.builders;

import madstodolist.model.Categoria;
import madstodolist.model.Escritorio;
import madstodolist.model.Nota;
import madstodolist.model.Usuario;

public class NotaBuilder {
    private Nota nota;

    public NotaBuilder() {
        this.nota = new Nota();
        this.nota.setTitulo("Nota Test");
        this.nota.setDescripcion("Esto es una nota de prueba");
        this.nota.setColor("ffadad"); // #ffadad, #ffd6a5, #fae890, #bcfdae, #9bf6ff, #a0c4ff, #bdb2ff, #ffc6ff
        this.nota.setPosicionX(1);
        this.nota.setPosicionY(1);
    }

    public Nota build() {
        return this.nota;
    }

    public NotaBuilder withTitulo(String titulo) {
        this.nota.setTitulo(titulo);
        return this;
    }

    public NotaBuilder withDescripcion(String descripcion) {
        this.nota.setDescripcion(descripcion);
        return this;
    }

    public NotaBuilder withColor(String color) {
        this.nota.setColor(color);
        return this;
    }

    public NotaBuilder withId(Long id) {
        this.nota.setId(id);
        return this;
    }

    public NotaBuilder withPosicionX(int posicionX) {
        this.nota.setPosicionX(posicionX);
        return this;
    }

    public NotaBuilder withPosicionY(int posicionY) {
        this.nota.setPosicionY(posicionY);
        return this;
    }

    public NotaBuilder withUsuario(Usuario usuario) {
        this.nota.setIdCreador(usuario);
        return this;
    }

    public NotaBuilder withEscritorio(Escritorio escritorio) {
        this.nota.setIdEscritorio(escritorio);
        return this;
    }

    public NotaBuilder withCategoria(Categoria categoria) {
        this.nota.setIdCategoria(categoria);
        return this;
    }
}