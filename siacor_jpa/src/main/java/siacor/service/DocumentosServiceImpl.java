package siacor.service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import siacor.model.pojo.DocumentosEditExternoPojo;

import org.springframework.beans.BeanUtils;

import siacor.model.Dto.DatosHojaRutaDto;
import siacor.model.Dto.DocumentosDto;
import siacor.model.Dto.ListaDocumentosDto;
import siacor.model.Dto.ReporteBuscadorDto;
import siacor.model.Dto.SalidaDataDto;
import siacor.model.Dto.buscaDatosDto;
import siacor.model.entity.Correlativos;
import siacor.model.entity.CorrelativosAsignados;
import siacor.model.entity.Documentos;
import siacor.model.entity.Parametrica;
import siacor.model.entity.Usuarios;
import siacor.model.entity.Vias;
import siacor.model.entity.HojasRuta;
import siacor.model.mapper.CorrelativosAsignadosMapper;
import siacor.model.mapper.CorrelativosMapper;
import siacor.model.mapper.DocumentosMapper;
import siacor.model.mapper.ViasMapper;
import siacor.model.pojo.BuscadorDataPojo;
import siacor.model.pojo.CorrelativosPojo;
import siacor.model.pojo.DataDocumentosPojo;
import siacor.model.pojo.DatosDocumentoPojo;
//import siacor.model.pojo.DocumentosEditPojo;
import siacor.model.pojo.DocumentosPojo;
import siacor.model.pojo.ViasPojo;
import siacor.model.repository.CorrelativosAsignadosRepository;
import siacor.model.repository.CorrelativosRepository;
import siacor.model.repository.DocumentosRepository;
import siacor.model.repository.HojasRutaRepository;
import siacor.model.repository.ParametricaRepository;
import siacor.model.repository.UsuariosRepository;
import siacor.model.repository.ViasRepository;

@Service
@Transactional
public class DocumentosServiceImpl implements DocumentosService{
    @Autowired
    private final ParametricaRepository parametricaRepository;

    @Autowired
    private final UsuariosRepository usuariosRepository;

    @Autowired
    private final CorrelativosRepository correlativosRepository;

    @Autowired
    private final DocumentosRepository documentosRepository;

    @Autowired
    private final CorrelativosAsignadosRepository correlativosAsignadosRepository;

    @Autowired
    private final ViasRepository viasRepository;

    @Autowired
    private final HojasRutaRepository hojasRutaRepository;

    public DocumentosServiceImpl(ParametricaRepository parametricaRepository,UsuariosRepository usuariosRepository,CorrelativosRepository correlativosRepository,DocumentosRepository documentosRepository, CorrelativosAsignadosRepository correlativosAsignadosRepository, ViasRepository viasRepository, HojasRutaRepository hojasRutaRepository){
        this.parametricaRepository = parametricaRepository;
        this.usuariosRepository = usuariosRepository;
        this.correlativosRepository = correlativosRepository;
        this.documentosRepository = documentosRepository;
        this.correlativosAsignadosRepository = correlativosAsignadosRepository;
        this.viasRepository = viasRepository;
        this.hojasRutaRepository = hojasRutaRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class) // Habilita rollback para excepciones de tipo Exception
    public Documentos registrarDocumentos(DocumentosPojo inpuDocumentos) throws Exception {
        try{
            String oficina = "";
            String unidad = "";
            String superior = "";
            //buscamos oficina del usuario 
            Usuarios usr = usuariosRepository.findByCodigo(inpuDocumentos.getCreadoPor());
            Parametrica par = parametricaRepository.datosParametrica(usr.getOficina());

            if (par != null) oficina = par.getCodigo();           //oficina del usuario

            //busacamos la unidad si existiera
            if(usr.getUnidad() != 0){
                par = parametricaRepository.datosParametrica(usr.getUnidad());
                unidad = par.getCodigo() + "/";
            }
            //buscamos superior
            Parametrica sup = parametricaRepository.buscaSuperior(usr.getOficina());
            if(sup != null) superior = sup.getCodigo();

            //buscamos tipo documento
            par = parametricaRepository.datosParametrica(inpuDocumentos.getTipoDocumento());
            if (par == null) throw new Exception("Documento no encontrado.");
            String doc = par.getCodigo();           //tipo documento

            String codCite = "";
            String citeOriginal = "";

            superior = superior.equals("")?"":superior + "/";
            oficina = oficina.equals("")?"":oficina + "/";

            if(doc.equals("X")) codCite =  doc + "/";
            else codCite =  doc + "/" + superior + oficina + unidad;

            System.out.println("CODIGO CITE: " + codCite);

            Correlativos correlativo = correlativosRepository.findByCodigo(codCite);
            if (correlativo == null) throw new Exception("Correlativo no encontrado para la oficina del usuario.");

            //generamos el CITE
            Integer nroCorrelativo = correlativo.getCorrelativo()+1;
            String cite = correlativo.getCodigo() + correlativo.getGestion() + "-" +  String.format("%04d",nroCorrelativo);

            if(doc.equals("X")) citeOriginal =  inpuDocumentos.getCiteOriginal();
            else citeOriginal = cite;

            inpuDocumentos.setCodigo(cite);
            inpuDocumentos.setCiteOriginal(citeOriginal);
            inpuDocumentos.setAccion("insert");
            System.out.println("Cite: " + cite);
    
            ZonedDateTime zonedDateTime = inpuDocumentos.getFecha().atOffset(ZoneOffset.UTC).atZoneSameInstant(ZoneId.of("America/La_Paz"));
            LocalDateTime fecha = zonedDateTime.toLocalDateTime();

            inpuDocumentos.setFecha(fecha);

            Documentos documento = DocumentosMapper.INSTANCE.InputDocumentosToDocumentos(inpuDocumentos);

            documentosRepository.save(documento);

            Documentos docVerificar = documentosRepository.findByCodigo(cite);
            if (docVerificar == null) throw new Exception("Hubo algun problema al crear el Documento, por favor verificar e intentar de nuevo.");

            CorrelativosPojo inputCorrelativos = new CorrelativosPojo();
            inputCorrelativos.setCodigo(correlativo.getCodigo());
            inputCorrelativos.setCorrelativo(nroCorrelativo);
            inputCorrelativos.setGestion(correlativo.getGestion());
            inputCorrelativos.setOficina(correlativo.getOficina());
            inputCorrelativos.setHabilitado(correlativo.getHabilitado());
            inputCorrelativos.setModificadoPor(inpuDocumentos.getCreadoPor());
            inputCorrelativos.setAccion(correlativo.getAccion());

            CorrelativosMapper.INSTANCE_EDIT.InputCorrelativosToCorrelativos(inputCorrelativos, correlativo);
            correlativosRepository.save(correlativo);

            CorrelativosAsignados inputAsignado = new CorrelativosAsignados();
            inputAsignado.setCodigo(cite);
            inputAsignado.setPersona(inpuDocumentos.getCreadoPor());
            CorrelativosAsignados correlativosAsignados = CorrelativosAsignadosMapper.INSTANCE.InputCorrelativosToCorrelativos(inputAsignado);
            correlativosAsignadosRepository.save(correlativosAsignados);

            int a = 0;
            if(inpuDocumentos.getVias().size()==0) {
                a = documentosRepository.registraVias(cite,inpuDocumentos.getCreadoPor(),inpuDocumentos.getDireccionIp());
                if(a != 1) throw new Exception("Error al registrar los datos enviados VIAS");
            }
            else {
                for (String via : inpuDocumentos.getVias()) {
                    ViasPojo inputVia = new ViasPojo();
                    inputVia.setCodigoUsuario(via);
                    inputVia.setCodigo(cite);
                    inputVia.setCreadoPor(inpuDocumentos.getCreadoPor());
                    inputVia.setDireccionIp(inpuDocumentos.getDireccionIp());

                    Vias v = ViasMapper.INSTANCE.InputViasToVias(inputVia);
                    viasRepository.save(v);
                }
            }
            

            return documento;

        }catch(Exception e){
            throw new Exception("Error al registrar los datos enviados, no se genero ninguna operacion. Error: "+ e.getMessage());
        }
    }

    @Override
    public int actulizarHabilitados(String codigo) {
        return viasRepository.bajaVias(codigo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class) // Habilita rollback para excepciones de tipo Exception
    public Documentos actualizarDocumentos(DocumentosPojo inpuDocumentosEditPojo,String cite) throws Exception {
        try {
            Documentos documento = documentosRepository.findByCodigo(cite);
            if (documento == null) throw new Exception("Documento con el CITE: " + cite + ", no encontrado.");

            List<String> vias = inpuDocumentosEditPojo.getVias();

            if (inpuDocumentosEditPojo.getDestinatarioTitulo() == null || inpuDocumentosEditPojo.getDestinatarioTitulo().trim().isEmpty()) {
                inpuDocumentosEditPojo.setDestinatarioTitulo(documento.getDestinatarioTitulo());
            }
            if (inpuDocumentosEditPojo.getDestinatarioNombres() == null || inpuDocumentosEditPojo.getDestinatarioNombres().trim().isEmpty()) {
                inpuDocumentosEditPojo.setDestinatarioNombres(documento.getDestinatarioNombres());
            }
            if (inpuDocumentosEditPojo.getDestinatarioCargo() == null || inpuDocumentosEditPojo.getDestinatarioCargo().trim().isEmpty()) {
                inpuDocumentosEditPojo.setDestinatarioCargo(documento.getDestinatarioCargo());
            }
            if (inpuDocumentosEditPojo.getDestinatarioInstitucion() == null || inpuDocumentosEditPojo.getDestinatarioInstitucion().trim().isEmpty()) {
                inpuDocumentosEditPojo.setDestinatarioInstitucion(documento.getDestinatarioInstitucion());
            }
            
            if (inpuDocumentosEditPojo.getReferencia() == null || inpuDocumentosEditPojo.getReferencia().trim().isEmpty()) {
                inpuDocumentosEditPojo.setReferencia(documento.getReferencia());
            }
            if (inpuDocumentosEditPojo.getContenido() == null || inpuDocumentosEditPojo.getContenido().trim().isEmpty()) {
                inpuDocumentosEditPojo.setContenido(documento.getContenido());
            }

            if (inpuDocumentosEditPojo.getAdjuntos() == null || inpuDocumentosEditPojo.getAdjuntos().trim().isEmpty()) {
                inpuDocumentosEditPojo.setAdjuntos(documento.getAdjuntos());
            }
            if (inpuDocumentosEditPojo.getCopias() == null || inpuDocumentosEditPojo.getCopias().trim().isEmpty()) {
                inpuDocumentosEditPojo.setCopias(documento.getCopias());
            }
            if (inpuDocumentosEditPojo.getNroHojas() == null || inpuDocumentosEditPojo.getNroHojas()== 0) {
                inpuDocumentosEditPojo.setNroHojas(documento.getNroHojas());
            } 
            
            //si esta vacio el viasNuevo hay que borrar los existentes
            if(vias == null || vias.isEmpty()) {
                int r = viasRepository.bajaVias(cite);
                if(r != 1) throw new Exception("Error al actualizar los datos enviados en VIAS enviados.");
            } //cuando vias no se registro al inicio ni tampoco cuando se modifico
            else {  //si es true, vias no modificadas, son las mismas
                List<Vias> v = viasRepository.buscarVias(cite);
                if(v.get(0).getCodigoUsuario() == vias.get(0).toString()){
                    int actVias = viasRepository.actualizarRegistro(v.get(0).getIdVias(), vias.get(0).toString());
                    if(actVias != 1) throw new Exception("Error al actualizar los datos enviados en VIAS enviados.");
                } else if(v.get(0).getCodigoUsuario() != vias.get(0).toString()){
                    int actVias = viasRepository.actualizarRegistro(v.get(0).getIdVias(), vias.get(0).toString());
                    if(actVias != 1) throw new Exception("Error al actualizar los datos enviados en VIAS enviados.");

                }
            }
            
            int resultado = documentosRepository.modificaDocumento(cite, inpuDocumentosEditPojo.getDestinatarioTitulo(), inpuDocumentosEditPojo.getDestinatarioNombres(), inpuDocumentosEditPojo.getDestinatarioCargo(), inpuDocumentosEditPojo.getDestinatarioInstitucion(), inpuDocumentosEditPojo.getReferencia(), inpuDocumentosEditPojo.getContenido(), inpuDocumentosEditPojo.getAdjuntos(), inpuDocumentosEditPojo.getCopias(), inpuDocumentosEditPojo.getNroHojas(), inpuDocumentosEditPojo.getCreadoPor(), inpuDocumentosEditPojo.getDireccionIp());

            if(resultado == 1) return documentosRepository.findByCodigo(cite);

            throw new Exception("Error al actualizar los datos enviados.");

        }catch(Exception e){
            throw new Exception("Error al actualizar los datos enviados, no se genero ninguna operacion. Error: "+ e.getMessage());
        }
    }

    @Override
    public List<ListaDocumentosDto> listaDocumentos(String usuario) throws Exception {
        try{
            return documentosRepository.listaDocumentos(usuario);
        }catch(Exception e){
            throw new Exception("Error al listar la derevivacion que puede ralizar el usuario " + usuario + ", por favor intente nuevamente." + e.getMessage());
        }
    }

    @Override
    public DatosDocumentoPojo datosDocumentos(String nuri, String cite) throws Exception {
        try{
            Documentos doc =  documentosRepository.findByCodigo(cite);

            DatosDocumentoPojo docPojo = new DatosDocumentoPojo();
            BeanUtils.copyProperties(doc, docPojo);

            Parametrica par = parametricaRepository.datosParametrica(docPojo.getTipoDocumento());

            docPojo.setTipoNombreDocumento(par.getNombre());

            List<buscaDatosDto> datosDto = documentosRepository.listaDocumentosVias(cite);

            docPojo.setVias(datosDto);

            docPojo.setNuri(hojasRutaRepository.registroNurCite(nuri,cite).getNur());
            
            return docPojo;
        }catch(Exception e){
            throw new Exception("Error al buscar los datos del CITE " + cite + ", por favor intente nuevamente." + e.getMessage());
        }
    }

    @Override
    public DatosDocumentoPojo datosDocumentosNuri(String cite) throws Exception {
        try{
            Documentos doc =  documentosRepository.findByCodigo(cite);

            DatosDocumentoPojo docPojo = new DatosDocumentoPojo();
            BeanUtils.copyProperties(doc, docPojo);

            Parametrica par = parametricaRepository.datosParametrica(docPojo.getTipoDocumento());

            docPojo.setTipoNombreDocumento(par.getNombre());

            List<buscaDatosDto> datosDto = documentosRepository.listaDocumentosVias(cite);

            docPojo.setVias(datosDto);
            docPojo.setNuri(hojasRutaRepository.registroNur(cite).getNur());
            
            return docPojo;
        }catch(Exception e){
            throw new Exception("Error al buscar los datos del CITE " + cite + ", por favor intente nuevamente." + e.getMessage());
        }
    }

    @Override
    public DatosHojaRutaDto datosHojaRuta(String nur) throws Exception {
        try{
            return documentosRepository.datosHojaRuta(nur);
        }catch(Exception e){
            throw new Exception("Error al recuperar datos para la generacion de la Hoja de Ruta, por favor intente nuevamente." + e.getMessage());
        }
    }

    @Override
    public DatosHojaRutaDto datosHojaRutaReimprsion(String nur) throws Exception {
        try{
            return documentosRepository.datosHojaRutaReimprsion(nur);
        }catch(Exception e){
            throw new Exception("Error al recuperar datos para la generacion de la Hoja de Ruta, por favor intente nuevamente." + e.getMessage());
        }
    }

    @Override
    public List<DataDocumentosPojo> listaDatosDocumentos(String usuario, String codigo) throws Exception {
        try{
            List<Object[]> resultList = documentosRepository.listaDatosDocumentos(usuario,codigo);
            List<DataDocumentosPojo> documentos = new ArrayList<>();

            for (Object[] row : resultList) {
                DataDocumentosPojo documento = new DataDocumentosPojo(
                        (String) row[0],   // codigo
                        (String) row[1],   // citeOriginal
                        (String) row[2],   // destinatarioTitulo
                        (String) row[3],   // destinatarioNombres
                        (String) row[4],   // destinatarioCargo
                        (String) row[5],   // destinatarioInstitucion
                        (String) row[6],   // remitenteNombres
                        (String) row[7],   // remitenteCargo
                        (String) row[8],   // remitenteInstitucion
                        (String) row[9],   // mosca
                        (String) row[10],  // referencia
                        (String) row[11],  // contenido
                        row[12] != null ? ((Timestamp) row[12]).toLocalDateTime() : null,  // fecha
                        (String) row[13],  // autor
                        (String) row[14],  // adjuntos
                        (String) row[15],  // copias
                        (Integer) row[16], // nroHojas
                        row[17] != null ? ((Number) row[17]).longValue() : null,           // tipoDocumento
                        (Integer) row[18],  // habilitado
                        row[19] != null ? ((Timestamp) row[19]).toLocalDateTime() : null,  // fechaCreacion
                        (String) row[20],   // creadoPor
                        row[21] != null ? ((Timestamp) row[21]).toLocalDateTime() : null,  // fechaModificacion
                        (String) row[22],   // modificadoPor
                        (String) row[23],   // direccionIp
                        (String) row[24]    // accion
                );
                documentos.add(documento);
            }

            return documentos;
        }catch(Exception e){
            throw new Exception("Error al recuperar datos para la generacion de la Hoja de Ruta, por favor intente nuevamente." + e.getMessage());
        }
    }

    @Override
    public int documentoValidar(String cite) throws Exception {
        try{
            List<SalidaDataDto> res = null;
            res = documentosRepository.documentoCompletoNoConfirmado(cite);
            if(res.size() > 0) return 3;
            res = documentosRepository.documentoCompleto(cite);
            if(res.size() > 0) return 2;
            res = documentosRepository.documentoCiteNuri(cite);
            if(res.size() > 0) return 1;
            res = documentosRepository.documentoSolo(cite);
            if(res.size() > 0) return 0;

            return -1;
        }catch(Exception e){
            throw new Exception("Error al validar el documento, por favor intente nuevamente." + e.getMessage());
        }
    }

    @Override
    public List<DataDocumentosPojo> listaDatosDocumentosTodo(String usuario) throws Exception {
        try{
            List<Object[]> resultList = documentosRepository.listaDatosDocumentosTodo(usuario);
            List<DataDocumentosPojo> documentos = new ArrayList<>();

            for (Object[] row : resultList) {
                DataDocumentosPojo documento = new DataDocumentosPojo(
                        (String) row[0],   // codigo
                        (String) row[1],   // citeOriginal
                        (String) row[2],   // destinatarioTitulo
                        (String) row[3],   // destinatarioNombres
                        (String) row[4],   // destinatarioCargo
                        (String) row[5],   // destinatarioInstitucion
                        (String) row[6],   // remitenteNombres
                        (String) row[7],   // remitenteCargo
                        (String) row[8],   // remitenteInstitucion
                        (String) row[9],   // mosca
                        (String) row[10],  // referencia
                        (String) row[11],  // contenido
                        row[12] != null ? ((Timestamp) row[12]).toLocalDateTime() : null,  // fecha
                        (String) row[13],  // autor
                        (String) row[14],  // adjuntos
                        (String) row[15],  // copias
                        (Integer) row[16], // nroHojas
                        row[17] != null ? ((Number) row[17]).longValue() : null,           // tipoDocumento
                        (Integer) row[18],  // habilitado
                        row[19] != null ? ((Timestamp) row[19]).toLocalDateTime() : null,  // fechaCreacion
                        (String) row[20],   // creadoPor
                        row[21] != null ? ((Timestamp) row[21]).toLocalDateTime() : null,  // fechaModificacion
                        (String) row[22],   // modificadoPor
                        (String) row[23],   // direccionIp
                        (String) row[24]    // accion
                );
                documentos.add(documento);
            }

            return documentos;
        }catch(Exception e){
            throw new Exception("Error al recuperar datos para la generacion de la Hoja de Ruta, por favor intente nuevamente." + e.getMessage());
        }
    }

    @Override
    public DatosDocumentoPojo datosDocumentosCite(String cite) throws Exception {
        try{
            Documentos doc = documentosRepository.findByCodigo(cite);
            
            DatosDocumentoPojo docPojo = new DatosDocumentoPojo();
            BeanUtils.copyProperties(doc, docPojo);

            Parametrica par = parametricaRepository.datosParametrica(docPojo.getTipoDocumento());

            docPojo.setTipoNombreDocumento(par.getNombre());

            List<buscaDatosDto> datosDto = documentosRepository.listaDocumentosVias(cite);
            
            docPojo.setVias(datosDto);
            docPojo.setNuri("");       
            
            return docPojo;
        }catch(Exception e){
            throw new Exception("Error al recuperar los datos del documento, por favor intente nuevamente." + e.getMessage());
        }
    }

    @Override
    public DatosDocumentoPojo datosDocumentosCiteConNuri(String cite) throws Exception {
        try{
            Documentos doc = documentosRepository.findByCodigo(cite);
            
            DatosDocumentoPojo docPojo = new DatosDocumentoPojo();
            BeanUtils.copyProperties(doc, docPojo);

            Parametrica par = parametricaRepository.datosParametrica(docPojo.getTipoDocumento());

            docPojo.setTipoNombreDocumento(par.getNombre());

            List<buscaDatosDto> datosDto = documentosRepository.listaDocumentosVias(cite);
            
            HojasRuta nuri = hojasRutaRepository.registroNur(cite);

            docPojo.setVias(datosDto);
            docPojo.setNuri(nuri.getNur());       
            
            return docPojo;
        }catch(Exception e){
            throw new Exception("Error al recuperar los datos del documento, por favor intente nuevamente." + e.getMessage());
        }
    }   

    @Override
    public List<ReporteBuscadorDto> reporteBuscador(BuscadorDataPojo pojo) throws Exception {
        try {
            String[] partes = null;

            partes = pojo.getFechaInicio().split("/");
            LocalDate fechaIniIngreso = LocalDate.of(Integer.parseInt(partes[2]), Integer.parseInt(partes[1]), Integer.parseInt(partes[0]));

            partes = pojo.getFechaFin().split("/");
            LocalDate fechaFinIngreso = LocalDate.of(Integer.parseInt(partes[2]), Integer.parseInt(partes[1]), Integer.parseInt(partes[0]));
            
            if(fechaIniIngreso.isAfter(fechaFinIngreso)) throw new Exception("Fecha Inicial del Ingreso no puede ser mayor a la fecha Final.");

            // ObjectMapper mapper = new ObjectMapper();
            // System.out.println(mapper.writeValueAsString(pojo));
            return documentosRepository.reporteBuscador(
                pojo.getUsuario(),
                pojo.getCite(),
                pojo.getNur(),
                pojo.getAdjuntos(),
                pojo.getReferencia(),
                pojo.getRemitente(),
                pojo.getRemCargo(),
                pojo.getRemInstitucion(),
                pojo.getDestinatario(),
                pojo.getDesCargo(),
                pojo.getDesInstitucion(),
                fechaIniIngreso,
                fechaFinIngreso
            );

        } catch (Exception e) {
            throw new Exception("Error al listar el reporte del buscador, por favor intente nuevamente." + e.getMessage());
        }
    }

    @Override
    public String fechaPrimerDocumento() throws Exception {
        try{
            return documentosRepository.fechaPrimerDocumento();
        }catch(Exception e){
            throw new Exception("Error al recuperar la fecha del primer documento creado." + e.getMessage());
        }
    }

    @Override
    public List<DocumentosDto> listarDocumentos(Long tipo, Long oficina) throws Exception {
        try{
            return documentosRepository.listarDocumentos(tipo, oficina);
        }catch(Exception e){
            throw new Exception("Error al recuperar el listado." + e.getMessage());
        }
    }

    @Override
    public DocumentosDto ultimoDocumento(Long tipo, Long oficina) throws Exception {
        try{
            return documentosRepository.ultimoDocumento(tipo, oficina);
        }catch(Exception e){
            throw new Exception("Error al recuperar el ultimo documento." + e.getMessage());
        }
    }

    @Override
    public Documentos actualizarDocumentosExterno(DocumentosEditExternoPojo inpuDocumentosEditExternoPojo, String cite) throws Exception {
        try{
            Documentos documento = documentosRepository.findByCodigo(cite);
            if (documento == null) throw new Exception("Documento con el CITE: " + cite + ", no encontrado.");

            if (inpuDocumentosEditExternoPojo.getCiteOriginal() == null || inpuDocumentosEditExternoPojo.getCiteOriginal().trim().isEmpty()) {
                inpuDocumentosEditExternoPojo.setCiteOriginal(documento.getCiteOriginal());
            }
            if (inpuDocumentosEditExternoPojo.getRemitenteNombres() == null || inpuDocumentosEditExternoPojo.getRemitenteNombres().trim().isEmpty()) {
                inpuDocumentosEditExternoPojo.setRemitenteNombres(documento.getRemitenteNombres());
            }
            if (inpuDocumentosEditExternoPojo.getRemitenteCargo() == null || inpuDocumentosEditExternoPojo.getRemitenteCargo().trim().isEmpty()) {
                inpuDocumentosEditExternoPojo.setRemitenteCargo(documento.getRemitenteCargo());
            }
            if (inpuDocumentosEditExternoPojo.getRemitenteInstitucion() == null || inpuDocumentosEditExternoPojo.getRemitenteInstitucion().trim().isEmpty()) {
                inpuDocumentosEditExternoPojo.setRemitenteInstitucion(documento.getRemitenteInstitucion());
            }
            
            if (inpuDocumentosEditExternoPojo.getReferencia() == null || inpuDocumentosEditExternoPojo.getReferencia().trim().isEmpty()) {
                inpuDocumentosEditExternoPojo.setReferencia(documento.getReferencia());
            }

            if (inpuDocumentosEditExternoPojo.getFecha() == null) {
                LocalDateTime fechaDocumento = documento.getFecha();
                if (fechaDocumento != null) {
                    inpuDocumentosEditExternoPojo.setFecha(fechaDocumento.atOffset(ZoneOffset.of("-04:00")));
                }
            }
            
            DocumentosMapper.INSTANCE_EDIT_EXTERNO.InputDocumentosEditExternoToDocumentos(inpuDocumentosEditExternoPojo, documento);
            return documentosRepository.save(documento);
        }catch(Exception e){
            throw new Exception("Error al modificar el documento Externo." + e.getMessage());
        }
    }

    @Override
    public DatosDocumentoPojo datosDocumentosExternos(String nur, String cite) throws Exception{
        try{
            Documentos doc =  documentosRepository.findByCodigo(cite);

            DatosDocumentoPojo docPojo = new DatosDocumentoPojo();
            BeanUtils.copyProperties(doc, docPojo);

            Parametrica par = parametricaRepository.datosParametrica(docPojo.getTipoDocumento());

            docPojo.setTipoNombreDocumento(par.getNombre());

            List<buscaDatosDto> datosDto = documentosRepository.listaDocumentosVias(cite);

            docPojo.setVias(datosDto);

            docPojo.setNuri(hojasRutaRepository.registroNurCite(nur,cite).getNur());
            
            return docPojo;
        }catch(Exception e){
            throw new Exception("Error al buscar los datos del CITE " + cite + ", por favor intente nuevamente." + e.getMessage());
        }
    }
    
}
