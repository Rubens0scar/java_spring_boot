package siacor.model.pojo;

import lombok.Data;
import siacor.model.Dto.RolesDto;

@Data
public class AutenticacionPojo {
    private String codigo;
	private String nombre_completo;
	private Long pagina_inicio;
	private String email;
	private Long oficina;
	private Long unidad;
	private Long permisos;
	private String cargo;
	private String mosca;
	private Integer nivel;
	private String accion;
    private Long genero;
    private RolesDto rol;
    private Long idSesion;
    private Long idUsrModRol;
    private Integer validaPassword;
    private String regional;
    private boolean asr;

    private String token;

    public AutenticacionPojo(String codigo, String nombre_completo, Long pagina_inicio, String email, Long oficina, Long unidad, Long permisos, String cargo, String mosca, Integer nivel, String accion, Long genero, RolesDto rol, Long idSesion, Integer validaPassword, String token, Long idUsrModRol, String regional, boolean asr){
        this.codigo = codigo;
        this.nombre_completo = nombre_completo;
        this.pagina_inicio = pagina_inicio;
        this.email = email;
        this.oficina = oficina;
        this.unidad = unidad;
        this.permisos = permisos;
        this.cargo = cargo;
        this.mosca = mosca;
        this.nivel = nivel;
        this.accion = accion;
        this.genero = genero;
        this.rol = rol;
        this.idSesion = idSesion;
        this.token = token;
        this.validaPassword = validaPassword;
        this.idUsrModRol =idUsrModRol;
        this.regional = regional;
        this.asr = asr;
    }
}