package siacor.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import siacor.model.Dto.ModuloDto;
import siacor.model.entity.Parametrica;

@Repository
public interface ParametricaRepository extends JpaRepository<Parametrica, Long>{
    @Query(value = "select id_parametrica as idParametrica,codigo,nombre from public.parametrica where activo = true and tipo=?1 order by 1", nativeQuery = true)
    List<ModuloDto> listarParametrica(String tipo);

    @Query(value = "select * from public.parametrica where activo = true and id_parametrica=?1 order by 1", nativeQuery = true)
    Parametrica datosParametrica(Long idParametrica);

    Parametrica findByIdParametrica(Long idParametrica);

    Parametrica findByCodigo(String codigo);

    @Query(value = "select public.reset_base(:gestion)", nativeQuery = true)
    int resetearBase(Integer gestion);

    @Query(value = "select * from public.parametrica where id_parametrica = (select valor_numerico  from public.parametrica where id_parametrica = :id)", nativeQuery = true)
    Parametrica buscaSuperior(Long id);
    

}
