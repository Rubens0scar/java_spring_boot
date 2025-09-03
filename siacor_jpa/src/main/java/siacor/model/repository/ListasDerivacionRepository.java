package siacor.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import siacor.model.Dto.ListarDerivacionDto;
import siacor.model.Dto.buscaDatosDto;
import siacor.model.entity.ListasDerivacion;
import siacor.model.utilitarios.ListasDerivacionID;

public interface ListasDerivacionRepository extends JpaRepository<ListasDerivacion,ListasDerivacionID> {
    ListasDerivacion findByUsrOrigenAndUsrDestino(String origen, String destino);

    @Query(value = "select u.codigo as codigo,u.nombre_completo as nombreCompleto,u.cargo as cargo,u.mosca as mosca " +
                   "from public.usuario u " +
                   "inner join ( " +
                   "	select l.usr_destino, u.oficina " +
                   "	from siacor.listas_derivacion l" +
                   "    inner join public.usuario u on u.codigo = l.usr_origen " +
                   "	where l.habilitado=1 and l.usr_origen = ?1 " +
                   ") a on a.usr_destino = u.codigo " +
                   "where u.oficina = a.oficina and u.nivel = 1", nativeQuery = true)
    buscaDatosDto buscarJefeUsuario(String usuario);

    @Query(value = "select u.codigo,u.nombre_completo nombreCompleto,u.cargo,u.mosca " +
                   "from public.usuario u " +
                   "inner join ( " +
                   "	select l.usr_destino " +
                   "	from siacor.listas_derivacion l " +
                   "	inner join public.usuario u on u.codigo = l.usr_origen " +
                   "	where l.opcion = 1 and l.habilitado=1 and l.usr_origen = ?1 " +
                   ") a on a.usr_destino = u.codigo  " +
                   "order by u.nivel desc ", nativeQuery = true)
    List<buscaDatosDto> listaDerivacion(String usuario);
    
    @Query(value = "select l.usr_destino as usuario,u.nombre_completo as nombre,p.nombre as oficina,l.opcion " +
                   "from siacor.listas_derivacion l " +
                   "inner join public.usuario u on u.codigo = l.usr_destino " +
                   "inner join public.parametrica p on p.id_parametrica = u.oficina " +
                   "where l.usr_origen = :usuario", 
           nativeQuery = true)
    List<ListarDerivacionDto> listaDerivacionUsuario(String usuario);

    @Modifying
    @Transactional
    @Query(
        value = "update siacor.listas_derivacion set opcion = :opcion where usr_origen = :origen and usr_destino = :destino",
        nativeQuery = true
    )
    int actualizaOpcionLista(Integer opcion, String origen, String destino);


}
