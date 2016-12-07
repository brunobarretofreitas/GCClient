package br.ufc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import br.ufc.model.ColetaEntity;
import br.ufc.model.LixeiraEntity;
import br.ufc.model.Lixeiras.Lixeira;
import br.ufc.model.Rotas.Rota;
import br.ufc.net.Proxy;
import br.ufc.service.ColetaService;
import br.ufc.service.LixeiraService;

@Controller
public class LixeiraController {
	
	@Autowired
	ColetaService coletaService;
	
	@Autowired
	LixeiraService lixeiraService;
	
	@RequestMapping("/listarLixeiras")
	public String listarLixeiras(Model model){
		Proxy proxy = new Proxy();
		List<Lixeira> lixeiras = proxy.buscarLixeira();
		for(Lixeira l : lixeiras){
			System.out.println(l.getLocalizacao());
			System.out.println(l.getPeso());
			System.out.println(l.getStatusCapacidade());
			System.out.println(l.getStatusColeta());
		}
		model.addAttribute("lixeiras",lixeiras);
		return "lixeira/listarLixeiras";
	}
	
	@RequestMapping("/calcularRota")
	public String calcularRota(Model model){
		Proxy proxy = new Proxy();
		List<Rota> listaRota = proxy.calcularRota();
		List<Lixeira> lixeiras = proxy.buscarLixeira();
		System.out.println("Numero de Lixeiras = " + listaRota.size());
		ColetaEntity coleta = new ColetaEntity();
		this.coletaService.salvar(coleta);
		
		for(Lixeira l : lixeiras){
			LixeiraEntity lixeira = new LixeiraEntity();
			lixeira.setId(Long.valueOf(l.getId()));
			lixeira.setLocalizacao(l.getLocalizacao());
			lixeira.setPeso(l.getPeso());
			lixeira.setStatusCapacidade(l.getStatusCapacidade());
			lixeira.setStatusColeta(l.getStatusColeta());
			lixeira.setColeta(coleta);
			this.lixeiraService.salvar(lixeira);
		}
		
		model.addAttribute("rotas",listaRota);
		return "rota/ver_rota";
	}
	
	@RequestMapping(value="/calcularDistancia", method=RequestMethod.GET)
	public String calcularDistancia(){
		return "calcular_distancia";
	}
	
	@RequestMapping(value="/calcularDistancia", method=RequestMethod.POST)
	public String calcularDistancia(Model model, @RequestParam("origem") String origem, @RequestParam("destino") String destino ){
		Proxy proxy = new Proxy();
		Double distancia = proxy.calcularDistancia(origem, destino);
		model.addAttribute("distancia", String.valueOf(distancia));
		return "calcular_distancia";
	}

}