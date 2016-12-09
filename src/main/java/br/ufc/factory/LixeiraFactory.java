package br.ufc.factory;

import br.ufc.model.ColetaEntity;
import br.ufc.model.LixeiraEntity;
import br.ufc.model.Lixeiras.Lixeira;

public class LixeiraFactory {
	private LixeiraEntity lixeiraEntity;

	public LixeiraEntity factoryLixeira(Lixeira lixeiraProtocol, ColetaEntity coleta) {
		lixeiraEntity = new LixeiraEntity();

		lixeiraEntity.setId(Long.valueOf(lixeiraProtocol.getId()));
		lixeiraEntity.setLocalizacao(lixeiraProtocol.getLocalizacao());
		lixeiraEntity.setPeso(lixeiraProtocol.getPeso());
		lixeiraEntity.setStatusCapacidade(lixeiraProtocol.getStatusCapacidade());
		lixeiraEntity.setStatusColeta(lixeiraProtocol.getStatusColeta());
		lixeiraEntity.setColeta(coleta);

		return lixeiraEntity;
	}
}
