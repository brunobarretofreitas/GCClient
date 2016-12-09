package br.ufc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufc.model.ColetaEntity;
import br.ufc.repository.ColetaRepository;

@Service
public class ColetaService {

	@Autowired
	ColetaRepository coletaRepository;
	
	public ColetaEntity salvar(ColetaEntity coleta) {
		this.coletaRepository.save(coleta);
		System.out.println(coleta.getId());
		return coleta;
	}
	
	public ColetaEntity buscarColeta(Long id) {
		return this.coletaRepository.findOne(id);
	}
	
	public void deletarColeta(ColetaEntity coleta){
		this.coletaRepository.delete(coleta);
	}
	
	public List<ColetaEntity> listar() {
		return (List<ColetaEntity>) this.coletaRepository.findAll();
	}
	
}