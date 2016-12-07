package br.ufc.model;

import java.util.List;

public class Mensagem {
	private int messageType;
	private int requestId;
	private String objectReference;
	private String methodId;
	private List<String> arguments;
	
	public int getMessageType() {
		return messageType;
	}

	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}

	public int getRequestId() {
		return requestId;
	}

	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}

	public String getObjectReference() {
		return objectReference;
	}

	public void setObjectReference(String objectReference) {
		this.objectReference = objectReference;
	}

	public String getMethodId() {
		return methodId;
	}

	public void setMethodId(String methodId) {
		this.methodId = methodId;
	}


	public void setArguments(List<String> arguments) {
		this.arguments = arguments;
	}

	@Override
	public String toString() {
		String args = "";
		if(!arguments.isEmpty()){
			for(String a : arguments){
				args += a + "**";
			}
			
			args = args.substring(0, args.length() - 2);
		}
		
		return objectReference + ";" + methodId + ";" + args;
	}

}
