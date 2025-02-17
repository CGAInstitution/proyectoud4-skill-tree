package madstodolist.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class EscritorioCreateData {
    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(@NotBlank(message = "El nombre no puede estar vacío") String nombre) {
        this.nombre = nombre;
    }
}
