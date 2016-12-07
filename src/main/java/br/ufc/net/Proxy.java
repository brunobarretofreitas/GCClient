package br.ufc.net;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.google.protobuf.InvalidProtocolBufferException;

import br.ufc.model.Lixeiras.ListaLixeira;
import br.ufc.model.Lixeiras.Lixeira;
import br.ufc.model.Mensagem;
import br.ufc.model.Rotas.ListaRota;
import br.ufc.model.Rotas.Rota;

@Service
public class Proxy {
	GarbageCollectorCliente cliente;
	private Mensagem mensagem;
	
	public List<Lixeira> buscarLixeira() {
		mensagem = empacotar("Lixeira", "getLixeiras",new ArrayList<String>());
		List<Lixeira> listaLixeira;
		System.out.println(mensagem.toString());
		try {
			listaLixeira = ListaLixeira.parseFrom(doOperations(mensagem)).getLixeiraList();
			return listaLixeira;
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	public List<Rota> calcularRota(){
		List<Lixeira> lixeiras = buscarLixeira();
		List<String> pontosLixeira = new ArrayList<>();
		List<String> pontosAlterarStatusColeta = new ArrayList<>();
		pontosLixeira.add("-3.765529,-38.637767");
		pontosLixeira.add("-3.720905,-38.510949");

		for(Lixeira lixeira : lixeiras){
			if(lixeira.getStatusCapacidade().equals(Lixeira.StatusCapacidade.CHEIA)){
				pontosLixeira.add(lixeira.getLocalizacao());
				pontosAlterarStatusColeta.add(String.valueOf(lixeira.getId()));
			}
		}
		
		
		mensagem = empacotar("Rota","calcularRota", pontosLixeira);
		System.out.println(mensagem.toString());
		List<Rota> listaRota;
		try {
			listaRota = ListaRota.parseFrom(doOperations(mensagem)).getRotaList();
			alterarStatusColeta("0", pontosAlterarStatusColeta);
			return listaRota;
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		 
		return null;
	}
	
	public Double calcularDistancia(String origem, String destino){
		List<String> pontos = new ArrayList<>();
		pontos.add(origem);
		pontos.add(destino);
		mensagem = empacotar("Distancia", "calcularDistancia", pontos);
		String distancia = new String(doOperations(mensagem));
		return Double.valueOf(distancia);
	}
	
	public void alterarStatusColeta(String status,List<String> arguments){
		List<String> pontosAlterarStatus = new ArrayList<>();
		pontosAlterarStatus.add(status);
		for(String l : arguments){
			pontosAlterarStatus.add(l);
		}
		
		Mensagem mensagem = empacotar("Lixeira", "atualizarStatusColeta",pontosAlterarStatus);
		doOperations(mensagem);
	}
	
	public byte[] doOperations(Mensagem mensagem){
		try {
			this.cliente = new TCPClienteBuilder().serverHost("127.0.0.1").serverPort(2055).build();
			cliente.sendRequest(mensagem.toString());
			System.out.println(mensagem.toString());
			return cliente.getResponse();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public Mensagem empacotar(String objectReference, String methodId,List<String> arguments){
		mensagem =new Mensagem();
		mensagem.setArguments(arguments);
		mensagem.setMethodId(methodId);
		mensagem.setObjectReference(objectReference);
		
		return mensagem;
	}
}
