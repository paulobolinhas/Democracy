package pt.ul.fc.css.example.demo.catalogs;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pt.ul.fc.css.example.demo.entities.Cidadao;
import pt.ul.fc.css.example.demo.entities.Delegado;
import pt.ul.fc.css.example.demo.entities.DelegadoParaTema;
import pt.ul.fc.css.example.demo.entities.ProjetoLei;
import pt.ul.fc.css.example.demo.entities.Tema;
import pt.ul.fc.css.example.demo.repositories.CidadaoRepository;
import pt.ul.fc.css.example.demo.repositories.DelegadoRepository;

@Component
public class CatalogoDelegados {

	@Autowired private DelegadoRepository delegadosRepository;
	@Autowired private CidadaoRepository cidadaoRepository;

	public String setDelegadosParaTema(Cidadao cidadao, Delegado delegado, Tema tema) {

		StringBuilder sb = new StringBuilder();

		List<Delegado> delegadosExistentes = delegadosRepository.findAll();

		for (Delegado d : delegadosExistentes) {
			sb.append("\nID: " + d.getId() + " | Nome: " + d.getNome() + "\n	Projetos Lei envolvidos: \n");

			List<ProjetoLei> l = d.getListaProjetosLeiEnvolvidos();

			for (ProjetoLei p : l) {
				sb.append("		Titulo: " + p.getTitulo() + " | Tema: " + p.getSubTema().getTitulo() + "\n");
			}
		}

		sb.append("\nDelegado escolhido: " + delegado.getId() + "\nTema escolhido: " + tema.getTitulo() + "\n");

		DelegadoParaTema delegadoParaTema = new DelegadoParaTema(delegado, tema);

		cidadao.setDelegadoTema(delegadoParaTema);
		cidadaoRepository.save(cidadao);

		sb.append("\nCidadao " + cidadao.getNome() + " escolheu delegado " + delegado.getNome() + " para o tema escolhido: " + tema.getTitulo() + "\n");

		return sb.toString();
	}

	public void saveDelegado(Delegado delegado) {
		delegadosRepository.save(delegado);
	}
	
	
	public Delegado getDelegadoByName(String name) {
	      return delegadosRepository.findByName(name);
	 }
	
	public Optional<Delegado> getDelegadoByID(Long id) {
	      return delegadosRepository.findById(id);
	 }
	
	public List<Delegado> getDelegados() {
		return delegadosRepository.findAll();
	}
}
