package br.ufc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufc.model.LixeiraEntity;
import br.ufc.repository.LixeiraRepository;

@Service
public class LixeiraService {

	@Autowired
	LixeiraRepository lixeiraRepository;
	
	public void salvar(LixeiraEntity lixeira){
		this.lixeiraRepository.save(lixeira);
	}
	
}