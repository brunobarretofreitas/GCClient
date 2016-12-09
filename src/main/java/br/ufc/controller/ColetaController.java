package br.ufc.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.protobuf.InvalidProtocolBufferException;

import br.ufc.model.ColetaEntity;
import br.ufc.model.LixeiraEntity;
import br.ufc.net.Proxy;
import br.ufc.service.ColetaService;

@Controller
@RequestMapping(value="/coleta")
public class ColetaController {
	
	@Autowired
	ColetaService coletaService;
	
	//Metodo Responsavel por Marcar como Finalizada a coleta alterando StatusColeta das lixeiras para LIVRE e o StatusCapacida para VAZIO
	@RequestMapping(value="/concluir/{coleta}",method = RequestMethod.GET)
	public String concluirColeta(@PathVariable("coleta") Long id){
		Proxy proxy = new Proxy();
		ColetaEntity coleta = coletaService.buscarColeta(id);
		List<String> pontosAlterarStatus = new ArrayList<>();
		for(LixeiraEntity l : coleta.getLixeiras()){
			pontosAlterarStatus.add(String.valueOf(l.getId()));
		}
		
		try {
			proxy.alterarStatusColeta("1", pontosAlterarStatus);
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		
		coletaService.deletarColeta(coleta);
		return "coletas";
	}
	
	//Metodo Responsavel por Listar Todas As Coletas
	@RequestMapping(method=RequestMethod.GET)
	public String listarColetas(Model model){
		List<ColetaEntity> coletas = coletaService.listar();
		model.addAttribute("coletas", coletas);
		return "coletas";
	}
}
