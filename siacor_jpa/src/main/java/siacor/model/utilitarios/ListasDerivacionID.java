package siacor.model.utilitarios;

import java.io.Serializable;

import lombok.Data;

@Data
public class ListasDerivacionID implements Serializable {
    private String usrOrigen;

	private String usrDestino;
}
