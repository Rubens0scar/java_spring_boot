package siacor.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import siacor.model.Dto.RolesDto;
import siacor.model.entity.UsrModRol;

@Repository
public interface UsrModRolRepository extends JpaRepository<UsrModRol, Long>{
    @Query(value = "SELECT umr.id_usr_mod_rol AS idUsrModRol, umr.cod_rol AS codRol, pr.codigo AS codigo, pr.nombre AS nombre, umr.cod_modulo AS codModulo " +
                   "FROM public.usr_mod_rol umr " +
                   "INNER JOIN public.parametrica pr ON pr.id_parametrica = umr.cod_rol " +
                   "WHERE umr.habilitado = 1 AND umr.codigo_usuario = ?1 AND umr.cod_modulo = ?2", nativeQuery = true)
    RolesDto buscarRol(String codigo_usuario, Long cod_modulo);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(
        value = "UPDATE public.usr_mod_rol " +
                "SET cod_rol=:rol, cod_modulo=:modulo, fecha_modificacion=now(), modificado_por=:modificadoPor, accion='update' " +
                "WHERE id_usr_mod_rol=:idUsrModRol",
        nativeQuery = true
    )
    int actualizarUsrMod(Long idUsrModRol, Long rol, Long modulo, String modificadoPor);

    UsrModRol findByCodigoUsuarioAndCodModulo(String usuario, Long modulo);
}
