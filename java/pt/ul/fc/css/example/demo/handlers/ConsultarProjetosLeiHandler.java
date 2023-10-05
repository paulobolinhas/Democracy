package pt.ul.fc.css.example.demo.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pt.ul.fc.css.example.demo.entities.ProjetoLei;
import pt.ul.fc.css.example.demo.entities.Votacao;
import pt.ul.fc.css.example.demo.enums.ESTADO_VOTACAO;
import pt.ul.fc.css.example.demo.catalogs.CatalogoProjetosLei;
import pt.ul.fc.css.example.demo.dtos.ProjetoLeiDTO;
import pt.ul.fc.css.example.demo.dtos.VotacaoDTO;
import pt.ul.fc.css.example.demo.repositories.ProjetoLeiRepository;

@Component
public class ConsultarProjetosLeiHandler {
  
  @Autowired private CatalogoProjetosLei catProj;
	// handler func
	public String consultarProjetosLei(ProjetoLei p) {
		return catProj.consultarProjetos(p);
	}

	//handler func
	public List<ProjetoLeiDTO> consultarProjetosLeiDTO() {
		List<ProjetoLeiDTO> projetosLeiDTO = new ArrayList<ProjetoLeiDTO>();
		List<ProjetoLei>  projetosLeiNaoexpirados = catProj.getListaProjetosLeiNaoExpirados();
		for (ProjetoLei pl : projetosLeiNaoexpirados) {
			ProjetoLeiDTO pl2 = pl.toProjetoLeiDTO();
			projetosLeiDTO.add(pl2);
		}
		return projetosLeiDTO;
	}

	public Optional<ProjetoLei> getProjetoLeiByID(Long id) {
		return catProj.getProjetoLeiByID(id);
	}

	public CatalogoProjetosLei getCatProj() {
		return catProj;
	}

	public void setCatProj(CatalogoProjetosLei catProj) {
		this.catProj = catProj;
	}
}
