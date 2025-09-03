package siacor.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import siacor.model.Dto.AgrupacionDto;
import siacor.model.Dto.AgrupacionSalidaDto;
import siacor.model.entity.Agrupacion;
import siacor.model.entity.Seguimientos;
import siacor.model.mapper.AgrupacionMapper;
import siacor.model.pojo.AgrupacionEditPojo;
import siacor.model.pojo.AgrupacionPojo;
import siacor.model.repository.AgrupacionRepository;
import siacor.model.repository.HojasRutaRepository;
import siacor.model.repository.SeguimientoRepository;

@Service
@Transactional
public class AgrupacionServiceImpl implements AgrupacionService{
    @Autowired
    private final SeguimientoRepository seguimientoRepository;

    @Autowired
    private final AgrupacionRepository agrupacionRepository;

    @Autowired
    private final HojasRutaRepository hojasRutaRepository;

    public AgrupacionServiceImpl(AgrupacionRepository agrupacionRepository, SeguimientoRepository seguimientoRepository,HojasRutaRepository hojasRutaRepository){
        this.agrupacionRepository = agrupacionRepository;
        this.seguimientoRepository = seguimientoRepository;
        this.hojasRutaRepository = hojasRutaRepository;
    }

    @Override
    public List<Seguimientos> listaAgrupacion(String nuri, String usuario) throws Exception {
        try{
            return seguimientoRepository.listaAgrupacion(nuri, usuario);
        }catch(Exception e){
            throw new Exception("Error al listar nuris para agrupar, por favor intente nuevamente." + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class) // Habilita rollback para excepciones
    public Agrupacion registraAgrupacion(AgrupacionPojo inputAgrupacionPojo) throws Exception {
        try{
            Agrupacion data = agrupacionRepository.findByNurPAndNurSAndIdSeguimiento(inputAgrupacionPojo.getNurP(), inputAgrupacionPojo.getNurS(), inputAgrupacionPojo.getIdSeguimiento());
            if(data != null) throw new Exception("Ya se registro esa agrupacion.");

            Agrupacion agrupa = AgrupacionMapper.INSTANCE.InputAgregadosToAgregados(inputAgrupacionPojo);

            agrupacionRepository.save(agrupa);

            int seguimiento = seguimientoRepository.actualizaSeguimientoAgrupado(inputAgrupacionPojo.getIdSeguimiento(),inputAgrupacionPojo.getNurP(),63L);

            if(seguimiento == 1) return agrupa;
            throw new Exception("Ocurrio un error al registrar la agrupacion, por favor intente nuevamente.");
        }catch(Exception e){
            throw new Exception("Error al registrar la agrupacion, por favor intente nuevamente." + e.getMessage());
        }
    }

    @Override
    public List<AgrupacionPojo> listarAgrupados(String nuri) throws Exception {
        try{
            List<Agrupacion> agrupa = agrupacionRepository.findByNurPOrderByIdAgruAsc(nuri);

            List<AgrupacionPojo> agrupaPojo = agrupa.stream().
                map(dto -> {
                    AgrupacionPojo pojo = new AgrupacionPojo();
                    BeanUtils.copyProperties(dto, pojo);
                    pojo.setUsuarioAutor(hojasRutaRepository.buscaUsuarioAutor(nuri));
                    return pojo;
                }).collect(Collectors.toList());

            return agrupaPojo;
        }catch(Exception e){
            throw new Exception("Error al listar las agrupaciones del nuri, por favor intente nuevamente." + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class) // Habilita rollback para excepciones
    public int actualizarEstados(AgrupacionEditPojo pojo) throws Exception{
        try{
            int res = agrupacionRepository.actualizarEstados(pojo.getNuri(),pojo.getModificadoPor());
            if(res >= 1) return res;
            throw new Exception("Hubo algun problema al actualizar los estados de la agrupacion, por favor intente nuevamente");
        }catch(Exception e){
            throw new Exception("Error al actualizar para agrupar, por favor intente nuevamente." + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class) // Habilita rollback para excepciones
    public String eliminarAgrupacion(Long id) throws Exception {
        try{
            AgrupacionSalidaDto agrupa = agrupacionRepository.buscarData(id);
            if(agrupa == null) throw new Exception("No se encontro el registro del agrupado, por favor verificar.");

            Long estado = 41L;
            if(agrupa.getOficial() == 0) estado = 65L;

            int resSeg = seguimientoRepository.actualizaEstadoAgrupacion(agrupa.getId(), estado);
            int resAgru = agrupacionRepository.eliminarAgrupacion(id);
            if(resSeg == 1 && resAgru == 1) return "Se elimino con exito el registro de la Agrupacion";
            throw new Exception("Hubo algun problema al elimino con exito el registro de la agrupacion, por favor intente nuevamente");
        }catch(Exception e){
            throw new Exception("Error al eliminar el registro para agrupar, por favor intente nuevamente." + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class) // Habilita rollback para excepciones
    public List<AgrupacionDto> reporteAgrupacion(String nuri) throws Exception {
        try{
            return agrupacionRepository.reporteAgrupacion(nuri);
        }catch(Exception e){
            throw new Exception("Error al listar nuris agrupados, por favor intente nuevamente." + e.getMessage());
        }
    }
    
}
