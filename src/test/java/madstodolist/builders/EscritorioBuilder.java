package madstodolist.builders;

import madstodolist.model.Escritorio;
import madstodolist.model.Nota;
import madstodolist.model.Usuario;

import java.util.HashSet;

public class EscritorioBuilder {
    private Escritorio escritorio;

    public EscritorioBuilder() {
        this.escritorio = new Escritorio();
        this.escritorio.setNombre("Escritorio Test");
        this.escritorio.setNotas(new HashSet<>());
    }

    public Escritorio build() {
        return this.escritorio;
    }

    public EscritorioBuilder withNombre(String nombre) {
        this.escritorio.setNombre(nombre);
        return this;
    }

    public EscritorioBuilder withUsuario(Usuario usuario) {
        this.escritorio.setIdUsuario(usuario);
        return this;
    }

    public EscritorioBuilder withNota(Nota nota) {
        this.escritorio.getNotas().add(nota);
        return this;
    }

    public EscritorioBuilder withId(Long id) {
        this.escritorio.setId(id);
        return this;
    }
}