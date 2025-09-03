package siacor.model.pojo;

//import java.util.List;

import lombok.Data;
//import siacor.model.Dto.SalidaDataDto;

@Data
public class ListaSeguimientosPojo {
    private Long idSeguimiento;
    //private List<SalidaDataDto> cites;
    private String derivadoPor;
    private String derivadoA;
    private String fechaDespacho;
    private String fechaRecepcion;
    private String accion;
    private String estado;
    private String proveido;
	private Integer oficial;

    private String cite1;	
	private String cite2;	
	private String cite3;	
	private String cite4;	
	private String cite5;	
	private String cite6;	
	private String cite7;	
	private String cite8;	
	private String cite9;	
	private String cite10;	
	private String cite11;	
	private String cite12;
}
