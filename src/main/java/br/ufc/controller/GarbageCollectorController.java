package br.ufc.controller;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import br.ufc.model.ColetaEntity;
import br.ufc.model.LixeiraEntity;
import br.ufc.model.Lixeiras.Lixeira;
import br.ufc.model.Message.Mensagem;
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
		Double distancia = null;
		try {
			distancia = proxy.calcularDistancia(origem, destino);
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		model.addAttribute("distancia", String.valueOf(distancia));
		return "calcular_distancia";
	}
	
	@RequestMapping(value="/teste")
	public String teste() throws InvalidProtocolBufferException{
		
		List<String> argumentos = new ArrayList<String>();
		argumentos.add("-31232,-312321");
		argumentos.add("-31232,-312321");
		
		Mensagem.Builder builder = Mensagem.newBuilder();
		builder.setId(1);
		builder.setTipo(0);
		builder.setObjeto("Lixeira");
		builder.setMetodo("teste");
		for(String arg : argumentos){
			builder.addArgumentos(ByteString.copyFrom(arg, Charset.forName("utf-8")));
		}
		
		Mensagem m = builder.build();
		Mensagem mm = Mensagem.parseFrom(m.toByteArray());
		for(ByteString b : mm.getArgumentosList()){
			System.out.println(b.toString(Charset.forName("utf-8")));
		}
		return "";
	}

}