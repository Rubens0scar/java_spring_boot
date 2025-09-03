package siacor.service;

import java.util.List;

import siacor.model.Dto.RecuperaCiteNuriDto;
import siacor.model.Dto.ReporteCreadosDto;
import siacor.model.entity.HojasRuta;
import siacor.model.pojo.HojasRutaEditPojo;
import siacor.model.pojo.HojasRutaPojo;

public interface HojasRutaService {
    HojasRuta generarHojaRuta(HojasRutaPojo inputHojasRutaPojo) throws Exception;

    HojasRuta actualizaHojasRuta(HojasRutaEditPojo inEditPojo) throws Exception;

    HojasRuta guardarHojaRuta(HojasRutaPojo inputHojasRutaPojo) throws Exception;

    HojasRuta eliminaHojaRuta(HojasRutaPojo inputRutaPojo) throws Exception;

    List<HojasRuta> listarCites(String nur) throws Exception;

    HojasRuta asignaNuriPendiente(HojasRutaPojo inHojasRutaPojo) throws Exception;

    HojasRuta buscaRegistroUltimo(String nuri) throws Exception;

    List<RecuperaCiteNuriDto> recuperaNuriCite(String cite) throws Exception;

    List<ReporteCreadosDto> reporteCreadosNuri(String usuario, Integer opcionNuri, String fechaIni, String fechaFin) throws Exception;

    List<HojasRuta> listadoHojasSeguimientoInternasCreadas(String usuario) throws Exception;

    HojasRuta anularHojaRuta(HojasRutaPojo inHojasRutaPojo) throws Exception;

    List<HojasRuta> listadoHojasSeguimientoExternasCreadas(String usuario) throws Exception;

    HojasRuta anularHojaRutaExterno(HojasRutaPojo inHojasRutaPojo) throws Exception;

}
