package br.ufc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufc.model.ColetaEntity;
import br.ufc.repository.ColetaRepository;

@Service
public class ColetaService {

	@Autowired
	ColetaRepository coletaRepository;
	
	public void salvar(ColetaEntity coleta){
		this.coletaRepository.save(coleta);
	}
	
}
