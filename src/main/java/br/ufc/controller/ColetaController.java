package br.ufc.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.ufc.model.ColetaEntity;
import br.ufc.model.LixeiraEntity;
import br.ufc.net.Proxy;
import br.ufc.service.ColetaService;

@Controller
@RequestMapping(value="/coleta")
public class ColetaController {
	
	@Autowired
	ColetaService coletaService;
	
	@RequestMapping(value="/concluir/{coleta}",method = RequestMethod.GET)
	public String concluirColeta(@PathVariable("coleta") Long id){
		Proxy proxy = new Proxy();
		ColetaEntity coleta = coletaService.buscarColeta(id);
		List<String> pontosAlterarStatusColeta = new ArrayList<>();
		for(LixeiraEntity l : coleta.getLixeiras()){
			pontosAlterarStatusColeta.add(String.valueOf(l.getId()));
		}
		proxy.alterarStatusColeta("1", pontosAlterarStatusColeta);
		coletaService.deletarColeta(coleta);
		return"";
	}
}
