package siacor.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import siacor.model.entity.Correlativos;


@Repository
public interface CorrelativosRepository extends JpaRepository<Correlativos, Long>{
    Correlativos findByCodigo(String codigo);
}
