package siacor.model.pojo;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class AutenticacionUsuarioPojo {
    @NotNull(message = "Error ingreso de datos al Usuario: El Usuario no puede ser nulo.")
    @Size(min = 3, max = 60, message = "Error el Usuario debe tener de 3-60 caracteres")
    private String usuario;

    @NotNull(message = "Error ingreso de datos al Password del Usuario: El Password del Usuario no puede ser nulo.")
    @Size(min = 3, max = 150, message = "Error el password del usuario debe tener de 3-150 caracteres")
    private String password;

    @NotNull(message = "Error ingreso de datos al IP: El IP no puede ser nulo.")
    @Size(min = 7, max = 15, message = "Error el IP debe tener de 7-15 caracteres")
    private String ip;

    @NotNull(message = "Error ingreso de datos del Modulo: El Modulo no puede ser nulo.")
    @Min(value = 1, message = "El valor mínimo permitido es 1")
    private Long modulo;

    private String agente;
}
