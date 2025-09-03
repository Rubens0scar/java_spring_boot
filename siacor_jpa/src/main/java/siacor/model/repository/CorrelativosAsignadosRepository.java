package siacor.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import siacor.model.entity.CorrelativosAsignados;

@Repository
public interface CorrelativosAsignadosRepository extends JpaRepository<CorrelativosAsignados, String> {
    
}
