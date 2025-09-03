package siacor.model.pojo;

import lombok.Data;

@Data
public class UsuarioCambioPasswordPojo {
    private String usuario;
    private String anteriorPassword;
    private String nuevoPassword;
}
