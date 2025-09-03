package siacor.model.Dto;

import lombok.Data;

@Data
public class UsuarioDto {
    private String codigo;
    private String password;

    public UsuarioDto(String password, String codigo){
        this.codigo = codigo;
        this.password = password;
    }
}