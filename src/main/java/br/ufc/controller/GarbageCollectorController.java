package br.ufc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.protobuf.InvalidProtocolBufferException;

import br.ufc.factory.LixeiraFactory;
import br.ufc.model.ColetaEntity;
import br.ufc.model.LixeiraEntity;
import br.ufc.model.Lixeiras.Lixeira;
import br.ufc.model.Rotas.Rota;
import br.ufc.net.Proxy;
import br.ufc.service.ColetaService;
import br.ufc.service.LixeiraService;

@Controller
public class GarbageCollectorController {

	@Autowired
	ColetaService coletaService;

	@Autowired
	LixeiraService lixeiraService;
	
	//Metodo Responsavel por Listar Todas As Lixeiras Cadastradas
	@RequestMapping("/listarLixeiras")
	public String listarLixeiras(Model model) {
		Proxy proxy = new Proxy();
		List<Lixeira> lixeiras = proxy.buscarLixeira();
		model.addAttribute("lixeiras", lixeiras);
		return "lixeira/listarLixeiras";
	}
	
	//Metodo Responsavel por Calcular Rota
	@RequestMapping("/calcularRota")
	public String calcularRota(Model model) {
		LixeiraFactory lixeiraFactory = new LixeiraFactory();
		Proxy proxy = new Proxy();
		List<Lixeira> ListaLixeirasProtocol = proxy.buscarLixeira();
		
		if (ListaLixeirasProtocol != null) {
			ColetaEntity coleta = new ColetaEntity();
			this.coletaService.salvar(coleta);

			for (Lixeira lixeiraProtocol : ListaLixeirasProtocol) {
				LixeiraEntity lixeira = lixeiraFactory.factoryLixeira(lixeiraProtocol, coleta);
				this.lixeiraService.salvar(lixeira);
			}

			List<Rota> listaRota = proxy.calcularRota(ListaLixeirasProtocol);
			model.addAttribute("rotas", listaRota);
			return "rota/ver_rota";
		}

		model.addAttribute("rotas", null);
		return "rota/ver_rota";
	}

	//Metodo Responsavel por Direcionar para pagina de calcular distancia 
	@RequestMapping(value = "/calcularDistanciaForm", method = RequestMethod.GET)
	public String calcularDistancia() {
		return "calcular_distancia";
	}

	//Método Responsavel por Calcular Distancia 
	@RequestMapping(value = "/calcularDistancia", method = RequestMethod.POST)
	public String calcularDistancia(Model model, @RequestParam("origem") String origem,
			@RequestParam("destino") String destino) {
		Proxy proxy = new Proxy();
		Double distancia = null;
		try {
			distancia = proxy.calcularDistancia(origem, destino);
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		model.addAttribute("distancia", String.valueOf(distancia));
		return "calcular_distancia";
	}
}