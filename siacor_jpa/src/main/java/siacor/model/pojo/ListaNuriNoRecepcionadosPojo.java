package siacor.model.pojo;

import java.util.List;

import lombok.Data;
import siacor.model.Dto.DocumentosDto;

@Data
public class ListaNuriNoRecepcionadosPojo {
    private String codigo;

    private List<DocumentosDto> listarDocumentosNur;
}
