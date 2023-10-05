package pt.ul.fc.css.example.demo.services;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pt.ul.fc.css.example.demo.dtos.ProjetoLeiDTO;
import pt.ul.fc.css.example.demo.entities.ProjetoLei;
import pt.ul.fc.css.example.demo.handlers.ConsultarProjetosLeiHandler;


@Service
public class ConsultarProjetosLeiService {
	
	@Autowired private ConsultarProjetosLeiHandler consultarProjetosLeiHandler;

	public String getProjetosLeiNaoExpiradosHandler(int proj_id) {
			Optional<ProjetoLei> pl = consultarProjetosLeiHandler.getProjetoLeiByID((long) proj_id);
			if(pl.isPresent()) {
				return consultarProjetosLeiHandler.consultarProjetosLei(consultarProjetosLeiHandler.getCatProj().getProjetoById(proj_id));
			} else {
				return "Não existem projetos com o ID especificado";
			}
	}

	  public List<ProjetoLeiDTO> getListagemProjetosLeiNaoExpirados() {
	    return consultarProjetosLeiHandler.consultarProjetosLeiDTO();
	  }
	  
	  public Optional<ProjetoLeiDTO> getProjetoLeiByID (long id) {
	        return consultarProjetosLeiHandler.getProjetoLeiByID(id).map(ProjetoLei::toProjetoLeiDTO);
	   }
}
