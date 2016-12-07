package br.ufc.net;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;

import org.apache.commons.io.IOUtils;

public class GarbageCollectorCliente {
	private String serverHost;
	private int serverPort;
	private Socket socket;
	private BufferedWriter out;

	public String getServerHost() {
		return serverHost;
	}

	public void setServerHost(String serverHost) {
		this.serverHost = serverHost;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public void setOut(BufferedWriter out) {
		this.out = out;
	}

	public static TCPClienteBuilder Builder() {
		return new TCPClienteBuilder();
	}

	public void sendRequest(String request) {
		try {
			this.out.write(request, 0, request.length());
			this.out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public byte[] getResponse() {
		try {
			byte[] resposta = IOUtils.toByteArray(socket.getInputStream());
			return resposta;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void close() {
		try {
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
