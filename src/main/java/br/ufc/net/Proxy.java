package br.ufc.net;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.google.protobuf.InvalidProtocolBufferException;

import br.ufc.factory.MensagemFactory;
import br.ufc.model.Lixeiras.ListaLixeira;
import br.ufc.model.Lixeiras.Lixeira;
import br.ufc.model.Message.Mensagem;
import br.ufc.model.Rotas.ListaRota;
import br.ufc.model.Rotas.Rota;
import br.ufc.util.Constantes;

@Service
public class Proxy {
	private GarbageCollectorCliente cliente;
	private Mensagem mensagem;
	private MensagemFactory mensagemFactory;

	// Metodo Responsavel por enviar mensagem de buscar todas a lixeiras
	// cadastradas para servidor
	public List<Lixeira> buscarLixeira() {
		mensagem = mensagemFactory.empacotar("Lixeira", "getLixeiras", new ArrayList<String>());
		
		try {
			Mensagem resposta = Mensagem.parseFrom(doOperations(mensagem));
			List<Lixeira> listaLixeira = ListaLixeira.parseFrom(resposta.getArgumentos(0).toByteArray()).getLixeiraList();
			return listaLixeira;
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}

		return null;
	}

	// Metodo Responsavel por solicitar calculo da rota de coleta de lixo
	// A rota é criada apartir de lixeiras que estão cheias e que possuem
	// StatusColetas como LIVRE
	public List<Rota> calcularRota(List<Lixeira> lixeiras) {
		List<String> pontosLixeira = new ArrayList<>();
		List<String> pontosAlterarStatusColeta = new ArrayList<>();

		pontosLixeira.add(Constantes.EMPRESA);
		pontosLixeira.add(Constantes.LIXAO);

		for (Lixeira lixeira : lixeiras) {
			if (lixeira.getStatusCapacidade().equals(Lixeira.StatusCapacidade.CHEIA)
					&& lixeira.getStatusColeta().equals(Lixeira.StatusColeta.LIVRE)) {
				pontosLixeira.add(lixeira.getLocalizacao());
			}
		}

		mensagem = mensagemFactory.empacotar("Rota", "calcularRota", pontosLixeira);

		try {
			Mensagem resposta = Mensagem.parseFrom(doOperations(mensagem));
			List<Rota> listaRota = ListaRota.parseFrom(resposta.getArgumentos(0)).getRotaList();
			alterarStatusColeta("0", pontosAlterarStatusColeta);
			return listaRota;
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}

		return null;
	}

	// Metodo Responsavvel por solicitar calculo da distancia entre duas
	// localizacoes
	public Double calcularDistancia(String origem, String destino) throws InvalidProtocolBufferException {
		List<String> pontos = new ArrayList<>();

		pontos.add(origem);
		pontos.add(destino);
		
		mensagem = mensagemFactory.empacotar("Distancia", "calcularDistancia", pontos);

		Mensagem resposta = Mensagem.parseFrom(doOperations(mensagem));

		String distancia = new String(resposta.getArgumentos(0).toString(Charset.forName("utf-8")));
		return Double.valueOf(distancia);
	}

	// Metodo Responsavel por informar para o servidor quais lixeiras devem
	// mudar o estado de coleta
	public void alterarStatusColeta(String status, List<String> arguments) throws InvalidProtocolBufferException {
		mensagemFactory = new MensagemFactory();

		List<String> pontosAlterarStatus = new ArrayList<>();

		pontosAlterarStatus.add(status);

		for (String l : arguments) {
			pontosAlterarStatus.add(l);
		}

		mensagem = mensagemFactory.empacotar("Lixeira", "atualizarStatusColeta", pontosAlterarStatus);
		Mensagem resposta = Mensagem.parseFrom(doOperations(mensagem));
	}

	// Metodo Responsavel por enviar mensagem para o servidor e receber resposta
	private byte[] doOperations(Mensagem mensagem) {
		try {

			this.cliente = new TCPClienteBuilder().serverHost("127.0.0.1").serverPort(2068).build();
			cliente.sendRequest(mensagem);
			return cliente.getResponse();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
}