package br.ufc.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


import br.ufc.model.Lixeiras.Lixeira.StatusCapacidade;
import br.ufc.model.Lixeiras.Lixeira.StatusColeta;


@Entity
@Table(name="lixeira")
public class LixeiraEntity {

	@Id
	private Long id;
	
	private String localizacao;
	private Float peso;
	private StatusCapacidade statusCapacidade;
	private StatusColeta statusColeta;
	
	@ManyToOne
	private ColetaEntity coleta;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getLocalizacao() {
		return localizacao;
	}
	public void setLocalizacao(String localizacao) {
		this.localizacao = localizacao;
	}
	public Float getPeso() {
		return peso;
	}
	public void setPeso(Float peso) {
		this.peso = peso;
	}
	public StatusCapacidade getStatusCapacidade() {
		return statusCapacidade;
	}
	public void setStatusCapacidade(StatusCapacidade statusCapacidade) {
		this.statusCapacidade = statusCapacidade;
	}
	public StatusColeta getStatusColeta() {
		return statusColeta;
	}
	public void setStatusColeta(StatusColeta statusColeta) {
		this.statusColeta = statusColeta;
	}
	public ColetaEntity getColeta() {
		return coleta;
	}
	public void setColeta(ColetaEntity coleta) {
		this.coleta = coleta;
	}
	
	
}