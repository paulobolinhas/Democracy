package pt.ul.fc.css.example.demo.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pt.ul.fc.css.example.demo.dtos.CidadaoDTO;
import pt.ul.fc.css.example.demo.dtos.ProjetoLeiDTO;
import pt.ul.fc.css.example.demo.entities.Cidadao;
import pt.ul.fc.css.example.demo.entities.ProjetoLei;
import pt.ul.fc.css.example.demo.handlers.ApoiarProjetosLeiHandler;



@Service
public class ApoiarProjetoLeiService {
	@Autowired private ApoiarProjetosLeiHandler apoiarProjetosLeiHandler;
	
	public void apoiarProjetos(CidadaoDTO cidDTO, ProjetoLeiDTO projDTO) {
		
		Cidadao cid = apoiarProjetosLeiHandler.getCidadaoByID(cidDTO.getId()).get();
		ProjetoLei proj = apoiarProjetosLeiHandler.getProjLeiByID(projDTO.getId()).get();
		apoiarProjetosLeiHandler.apoiarProjetos(cid, proj);
	}
	
	public String getApoiarProjetosHandler(int cid_id, int proj_id) {
		Optional<ProjetoLei> pl = apoiarProjetosLeiHandler.getProjLeiByID((long) proj_id);
		Optional<Cidadao> c = apoiarProjetosLeiHandler.getCidadaoByID((long) cid_id);
		if(pl.isPresent() && c.isPresent()) {
			apoiarProjetosLeiHandler.apoiarProjetos(apoiarProjetosLeiHandler.getCatCidadao().getCidadaoById(cid_id), apoiarProjetosLeiHandler.getCatProj().getProjetoById(proj_id));
			return "Projeto Apoiado";
		} else {
			return "Não existem projetos ou cidadaos com o ID especificado";
		}
	}
	
	public Optional<CidadaoDTO> getCidadaoByID(Long id) {
		return apoiarProjetosLeiHandler.getCidadaoByID(id).map(Cidadao::toCidadaoDTO);
	}
	
	public Optional<ProjetoLeiDTO> getProjLeiByID(Long id) {
		return apoiarProjetosLeiHandler.getProjLeiByID(id).map(ProjetoLei::toProjetoLeiDTO);
	}
}
