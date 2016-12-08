package br.ufc.net;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import br.ufc.model.Lixeiras.ListaLixeira;
import br.ufc.model.Lixeiras.Lixeira;
import br.ufc.model.MensagemAntiga;
import br.ufc.model.Message.Mensagem;
import br.ufc.model.Rotas.ListaRota;
import br.ufc.model.Rotas.Rota;

@Service
public class Proxy {
	GarbageCollectorCliente cliente;
	private Mensagem mensagem;
	
	public List<Lixeira> buscarLixeira() {
		mensagem = empacotar("Lixeira", "getLixeiras",new ArrayList<String>());
		List<Lixeira> listaLixeira;
		try {
			Mensagem resposta = Mensagem.parseFrom(doOperations(mensagem));
			listaLixeira = ListaLixeira
					.parseFrom(resposta.getArgumentos(0).toByteArray())
					.getLixeiraList();
			
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
			if(lixeira.getStatusCapacidade().equals(Lixeira.StatusCapacidade.CHEIA)
					&& lixeira.getStatusColeta().equals(Lixeira.StatusColeta.LIVRE)){
				pontosLixeira.add(lixeira.getLocalizacao());
			}
		}
		
		mensagem = empacotar("Rota","calcularRota", pontosLixeira);
		List<Rota> listaRota;
		try {
			Mensagem resposta = Mensagem.parseFrom(doOperations(mensagem));
			listaRota = ListaRota.parseFrom(resposta.getArgumentos(0)).getRotaList();
			alterarStatusColeta("0", pontosAlterarStatusColeta);
			return listaRota;
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		 
		return null;
	}
	
	public Double calcularDistancia(String origem, String destino) throws InvalidProtocolBufferException{
		List<String> pontos = new ArrayList<>();
		pontos.add(origem);
		pontos.add(destino);
		mensagem = empacotar("Distancia", "calcularDistancia", pontos);
		Mensagem resposta = Mensagem.parseFrom(doOperations(mensagem));
		String distancia = new String(resposta.getArgumentos(0).toString(Charset.forName("utf-8")));
		return Double.valueOf(distancia);
	}
	
	public void alterarStatusColeta(String status,List<String> arguments) throws InvalidProtocolBufferException{
		List<String> pontosAlterarStatus = new ArrayList<>();
		pontosAlterarStatus.add(status);
		for(String l : arguments){
			pontosAlterarStatus.add(l);
		}
		
		mensagem = empacotar("Lixeira", "atualizarStatusColeta", pontosAlterarStatus);
		Mensagem resposta = Mensagem.parseFrom(doOperations(mensagem));
	}
		
	public byte[] doOperations(Mensagem mensagem){
		try {
			this.cliente = new TCPClienteBuilder().serverHost("127.0.0.1").serverPort(2068).build();
			cliente.sendRequest(mensagem);
			return cliente.getResponse();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public Mensagem empacotar(String objectReference, String methodId,List<String> arguments){
		Mensagem.Builder builder = Mensagem.newBuilder();
		builder.setTipo(0);
		builder.setId(1);
		builder.setObjeto(objectReference);
		builder.setMetodo(methodId);
		for(String arg : arguments){
			builder.addArgumentos(ByteString.copyFrom(arg, Charset.forName("utf-8")));
		}
		
		return builder.build();
	}
}