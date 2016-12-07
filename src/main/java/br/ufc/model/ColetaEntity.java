package br.ufc.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="coleta")
public class ColetaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="coleta_id")
	private Long id;
	
	@OneToMany(mappedBy="coleta", cascade=CascadeType.REMOVE)
	private List<LixeiraEntity> lixeiras;

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<LixeiraEntity> getLixeiras() {
		return lixeiras;
	}

	public void setLixeiras(List<LixeiraEntity> lixeiras) {
		this.lixeiras = lixeiras;
	}
	
}