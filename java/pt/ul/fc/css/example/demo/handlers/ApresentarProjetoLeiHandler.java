package pt.ul.fc.css.example.demo.handlers;

import java.io.File;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pt.ul.fc.css.example.demo.catalogs.CatalogoDelegados;
import pt.ul.fc.css.example.demo.catalogs.CatalogoProjetosLei;
import pt.ul.fc.css.example.demo.catalogs.CatalogoTemas;
import pt.ul.fc.css.example.demo.entities.Tema;
import pt.ul.fc.css.example.demo.entities.Delegado;
import pt.ul.fc.css.example.demo.entities.ProjetoLei;
import pt.ul.fc.css.example.demo.entities.SubTema;

@Component
public class ApresentarProjetoLeiHandler {

	@Autowired private CatalogoProjetosLei catProjetoLei;
	@Autowired private CatalogoDelegados catDelegados;
	@Autowired private CatalogoTemas catalogoTemas;

	// handler func
	public ProjetoLei apresentarProjeto(
			String titulo,
			String desc,
			File anexoPDF,
			Timestamp datahoraValidade,
			SubTema subtema,
			Delegado delegadoProponente) {
		ProjetoLei projLei =
				new ProjetoLei(titulo, desc, anexoPDF, datahoraValidade, subtema, delegadoProponente);

		catProjetoLei.createProjetoLei(projLei);

		delegadoProponente.addListaProjetosLeiEnvolvidos(projLei);
		this.catDelegados.saveDelegado(delegadoProponente);

		return projLei;
	}

	public Optional<SubTema> getSubTemaByName(String subtema) {
		return catalogoTemas.getSubTemaByName(subtema);
	}
	
	public List<Tema> getSubTemas() {
		return catalogoTemas.getSubtemas();
	}

	public Delegado getDelegadoByName(String name) {
		return catDelegados.getDelegadoByName(name);
	}
	
	public Optional<Delegado> getDelegadoByID(Long id) {
		return catDelegados.getDelegadoByID(id);
	}
}
