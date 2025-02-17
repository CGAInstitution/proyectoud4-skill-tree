package madstodolist.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class SettingsForm {
    @NotNull
    private String nombre;

    @NotNull
    private String apellidos;

    @NotNull
    @Email
    private String email;

    private String contrasenia;
    private String confirmarContrasenia;

    @NotNull
    private Boolean modo;//oscuro 0 claro 1

    @NotNull
    private int textSize;

    @NotNull
    private String idioma;

    public SettingsForm() {
    }

    public SettingsForm(String nombre, String apellidos, String email, String contrasenia, String confirmarContrasenia, Boolean modo, int textSize, String idioma) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.contrasenia = contrasenia;
        this.confirmarContrasenia = confirmarContrasenia;
        this.modo = modo;
        this.textSize = textSize;
        this.idioma = idioma;
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
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getContrasenia() {
        return contrasenia;
    }
    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }
    public String getConfirmarContrasenia() {
        return confirmarContrasenia;
    }
    public void setConfirmarContrasenia(String confirmarContrasenia) {
        this.confirmarContrasenia = confirmarContrasenia;
    }
    public Boolean getModo() {
        return modo;
    }
    public void setModo(Boolean modo) {
        this.modo = modo;
    }
    public int getTextSize() {
        return textSize;
    }
    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }
    public String getIdioma() {
        return idioma;
    }
    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

}
