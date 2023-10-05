package pt.ul.fc.css.example.demo.handlers;


import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;
import pt.ul.fc.css.example.demo.catalogs.CatalogoCidadaos;
import pt.ul.fc.css.example.demo.catalogs.CatalogoDelegados;
import pt.ul.fc.css.example.demo.catalogs.CatalogoTemas;

import pt.ul.fc.css.example.demo.entities.Cidadao;
import pt.ul.fc.css.example.demo.entities.Delegado;
import pt.ul.fc.css.example.demo.entities.SubTema;
import pt.ul.fc.css.example.demo.entities.Tema;

@Component
public class EscolherDelegadoHandler {

	@Autowired private CatalogoCidadaos catCC;
	@Autowired private CatalogoDelegados catDelegados;
	@Autowired private CatalogoTemas catTemas;

	//  public EscolherDelegadoHandler(CatalogoCidadaos catCC, CatalogoDelegados catDelegados) {
	//    this.catCC = catCC;
	//    this.catDelegados = catDelegados;
	//  }
	//
	//  public static EscolherDelegadoHandler getEscolherDelegadoHandler(
	//      CatalogoCidadaos catCC, CatalogoDelegados catDelegados) {
	//    if (instance == null) instance = new EscolherDelegadoHandler(catCC, catDelegados);
	//    return instance;
	//  }

	// handler func
	public String escolherDelegados(Cidadao c, Delegado d, Tema t) {
		return catDelegados.setDelegadosParaTema(c, d, t);
	}

	public CatalogoCidadaos getCatCC() {
		return catCC;
	}

	public void setCatCC(CatalogoCidadaos catCC) {
		this.catCC = catCC;
	}

	public CatalogoDelegados getCatDelegados() {
		return catDelegados;
	}

	public void setCatDelegados(CatalogoDelegados catDelegados) {
		this.catDelegados = catDelegados;
	}

	public List<Delegado> getDelegados() {
		return catDelegados.getDelegados();
	}
	
	public Optional<Cidadao> getCidadaoByID(Long id) {
		return catCC.getCidadaoByID(id);
	}
	
	public Optional<Delegado> getDelegadoByID(Long id) {
		return catDelegados.getDelegadoByID(id);
	}
	
	public Optional<SubTema> getTemaByName(String tema) {
		return catTemas.getSubTemaByName(tema);
	}
	
	

}
