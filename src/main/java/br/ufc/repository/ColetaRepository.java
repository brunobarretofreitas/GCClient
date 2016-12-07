package br.ufc.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.ufc.model.ColetaEntity;

@Repository
public interface ColetaRepository extends CrudRepository<ColetaEntity, Long>{

}
