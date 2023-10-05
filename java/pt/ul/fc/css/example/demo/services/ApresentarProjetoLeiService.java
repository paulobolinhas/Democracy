package pt.ul.fc.css.example.demo.services;

import java.io.File;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import pt.ul.fc.css.example.demo.catalogs.CatalogoTemas;
import pt.ul.fc.css.example.demo.dtos.CidadaoDTO;
import pt.ul.fc.css.example.demo.dtos.DelegadoDTO;
import pt.ul.fc.css.example.demo.dtos.ProjetoLeiDTO;
import pt.ul.fc.css.example.demo.dtos.SubTemaDTO;
import pt.ul.fc.css.example.demo.dtos.VotacaoDTO;
import pt.ul.fc.css.example.demo.entities.Cidadao;
import pt.ul.fc.css.example.demo.entities.Delegado;
import pt.ul.fc.css.example.demo.entities.ProjetoLei;
import pt.ul.fc.css.example.demo.entities.SubTema;
import pt.ul.fc.css.example.demo.entities.Tema;
import pt.ul.fc.css.example.demo.handlers.ApresentarProjetoLeiHandler;

@Service
public class ApresentarProjetoLeiService {

	@Autowired private ApresentarProjetoLeiHandler apresentarProjetoLeiHandler;


	public ProjetoLei getProjetoLeiApresentado(		  
			String titulo,
			String desc,
			File anexoPDF,
			Timestamp datahoraValidade,
			SubTemaDTO subtemaDTO,
			DelegadoDTO delegadoProponenteDTO) {
		
		SubTema subtema = apresentarProjetoLeiHandler.getSubTemaByName(subtemaDTO.getTitulo()).get();
		Delegado delegadoProponente = apresentarProjetoLeiHandler.getDelegadoByName(delegadoProponenteDTO.getNome());
		
		return apresentarProjetoLeiHandler.apresentarProjeto(titulo, desc, anexoPDF, datahoraValidade, subtema, delegadoProponente);
	}

	
	public Optional<SubTemaDTO> getSubTemaByName(String subtema) {
		return apresentarProjetoLeiHandler.getSubTemaByName(subtema).map(SubTema::toSubTemaDTO);
	}

	public List<Tema> getTemas() {
		return apresentarProjetoLeiHandler.getSubTemas();
	}

	public Delegado getDelegadoByName(String name) {
		return apresentarProjetoLeiHandler.getDelegadoByName(name);
	}
	
	public Optional<DelegadoDTO> getDelegadoByID(Long id) {
		return apresentarProjetoLeiHandler.getDelegadoByID(id).map(Delegado::toDelegadoDTO);
	}

}
