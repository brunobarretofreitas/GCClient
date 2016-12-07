package br.ufc.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.ufc.model.LixeiraEntity;

@Repository
public interface LixeiraRepository extends CrudRepository<LixeiraEntity, Long> {

}
