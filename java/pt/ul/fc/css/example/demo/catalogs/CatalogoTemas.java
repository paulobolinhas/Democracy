package pt.ul.fc.css.example.demo.catalogs;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pt.ul.fc.css.example.demo.entities.Delegado;
import pt.ul.fc.css.example.demo.entities.ProjetoLei;
import pt.ul.fc.css.example.demo.entities.SubTema;
import pt.ul.fc.css.example.demo.entities.Tema;
import pt.ul.fc.css.example.demo.repositories.ProjetoLeiRepository;
import pt.ul.fc.css.example.demo.repositories.TemaRepository;

@Component
public class CatalogoTemas {
	@Autowired private TemaRepository temaRepository;
	
	public Optional<SubTema> getSubTemaByName(String subtema) {
	      return Optional.ofNullable(temaRepository.findByName(subtema));
	 }
	

	public List<Tema> getSubtemas() {
	      return temaRepository.findAll();
	 }
}
