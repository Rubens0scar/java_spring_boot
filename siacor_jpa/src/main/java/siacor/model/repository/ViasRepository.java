package siacor.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;

import siacor.model.entity.Vias;
import java.util.List;


@Repository
public interface ViasRepository extends JpaRepository<Vias, Long>{
    
    Vias findByCodigoUsuarioAndCodigo(String usuario, String cite);

    Vias findByCodigoUsuarioAndCodigoAndHabilitado(String usuario, String cite,Integer habilitado);

    @Query(value = "SELECT * FROM siacor.vias where habilitado = :habilitado and codigo = :cite and codigo_usuario = :usuario", nativeQuery = true)
    Vias buscarCodigo(String usuario, String cite,Integer habilitado);

    @Modifying
    @Transactional
    @Query(
        value = "UPDATE siacor.vias SET codigo_usuario = '', habilitado=0 WHERE codigo=:codigo",
        nativeQuery = true
    )
    int bajaVias(String codigo);

    List<Vias> findByCodigo(String codigo);

    @Query(value = "SELECT * FROM siacor.vias where codigo = :cite", nativeQuery = true)
    List<Vias> buscarVias(String cite);

    @Modifying
    @Transactional
    @Query(
        value = "UPDATE siacor.vias SET codigo_usuario=:usuario, habilitado=1 WHERE id_vias=:id",
        nativeQuery = true
    )
    int actualizarRegistro(Long id, String usuario);
    
    @Modifying
    @Transactional
    @Query(
        value = "INSERT INTO siacor.vias(codigo_usuario, codigo, habilitado, fecha_creacion, creado_por, direccion_ip, accion) VALUES(:usuario, :cite, 1, now(), :creador, :ip, 'insert')",
        nativeQuery = true
    )
    int insertarVias(String usuario,String cite, String creador, String ip);


}
