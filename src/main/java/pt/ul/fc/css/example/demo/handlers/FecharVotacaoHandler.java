package pt.ul.fc.css.example.demo.handlers;

import pt.ul.fc.css.example.demo.entities.Votacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pt.ul.fc.css.example.demo.catalogs.CatalogoCidadaos;
import pt.ul.fc.css.example.demo.catalogs.CatalogoVotacoes;

@Component
public class FecharVotacaoHandler {

	@Autowired private CatalogoVotacoes catVotacoes;
	@Autowired private CatalogoCidadaos catCidadaos;

	// handler func
	public boolean fecharVotacao(Votacao v) {
		return catVotacoes.fecharVotacaoV(v);
	}

	public CatalogoCidadaos getCatCidadaos() {
		return catCidadaos;
	}

	public void setCatCidadaos(CatalogoCidadaos catCidadaos) {
		this.catCidadaos = catCidadaos;

	}
}
