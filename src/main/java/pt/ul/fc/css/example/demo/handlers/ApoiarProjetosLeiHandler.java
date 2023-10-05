package pt.ul.fc.css.example.demo.handlers;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import org.springframework.stereotype.Component;
import pt.ul.fc.css.example.demo.catalogs.CatalogoCidadaos;

import pt.ul.fc.css.example.demo.catalogs.CatalogoProjetosLei;
import pt.ul.fc.css.example.demo.catalogs.CatalogoVotacoes;

import pt.ul.fc.css.example.demo.entities.Cidadao;
import pt.ul.fc.css.example.demo.entities.ProjetoLei;
import pt.ul.fc.css.example.demo.enums.ESTADO_PROJETO_LEI;

@Component
public class ApoiarProjetosLeiHandler {

	@Autowired private CatalogoProjetosLei catProj;
	@Autowired private CatalogoVotacoes catVotacoes;
	@Autowired private CatalogoCidadaos catCidadao;

	// handler func
	public void apoiarProjetos(Cidadao cid, ProjetoLei proj) {
		proj.adicionaApoiante(cid);
		if (proj.getNumeroApoiantes() == 10000) {
			proj.setEstado(ESTADO_PROJETO_LEI.APROVADO);
			//			proj.setVotacao(catVotacoes.createVotacao(proj));
			catVotacoes.createVotacao(proj);
		}
		catProj.saveProjetoLei(proj);
	}

	public CatalogoProjetosLei getCatProj() {
		return catProj;
	}

	public void setCatProj(CatalogoProjetosLei catProj) {
		this.catProj = catProj;
	}

	public CatalogoVotacoes getCatVotacoes() {
		return catVotacoes;
	}
	
	public CatalogoCidadaos getCatCidadao() {
		return catCidadao;
	}

	public void setCatCidadao(CatalogoCidadaos catCidadao) {
		this.catCidadao = catCidadao;
	}

	public void setCatVotacoes(CatalogoVotacoes catVotacoes) {
		this.catVotacoes = catVotacoes;
	}
	
	public Optional<Cidadao> getCidadaoByID(Long id) {
		return catCidadao.getCidadaoByID(id);
	}
	
	public Optional<ProjetoLei> getProjLeiByID(Long id) {
		return catProj.getProjLeiByID(id);
	}


}
