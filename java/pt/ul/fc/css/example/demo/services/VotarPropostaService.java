package pt.ul.fc.css.example.demo.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pt.ul.fc.css.example.demo.dtos.CidadaoDTO;
import pt.ul.fc.css.example.demo.dtos.VotacaoDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import pt.ul.fc.css.example.demo.entities.Cidadao;
import pt.ul.fc.css.example.demo.entities.ProjetoLei;
import pt.ul.fc.css.example.demo.entities.Votacao;
import pt.ul.fc.css.example.demo.enums.VOTO_DELEGADO;
import pt.ul.fc.css.example.demo.handlers.VotarPropostaHandler;

import pt.ul.fc.css.example.demo.repositories.CidadaoRepository;
import pt.ul.fc.css.example.demo.repositories.VotacoesRepository;


@Service
public class VotarPropostaService {
	
	@Autowired private VotarPropostaHandler votarPropostaHandler;
	
	public String votarPropostaHandler(int votacao_id, int cid_id, VOTO_DELEGADO voto_delegado) {
		Optional<Votacao> v = votarPropostaHandler.getVotacaoByID((long) votacao_id);
		Optional<Cidadao> c = votarPropostaHandler.getCidadaoByID((long) cid_id);
		if(v.isPresent() && c.isPresent()) {
			return votarPropostaHandler.votarProposta(votarPropostaHandler.getCatVotacoes().getVotacaoById(votacao_id), votarPropostaHandler.getCatCidadao().getCidadaoById(cid_id), voto_delegado);
		} else {
			return "Não existem cidadaos ou votacoes com o ID especificado";
		}
	}
	
	public String votarProposta(VotacaoDTO votacaoDTO, CidadaoDTO cidadaoDTO, VOTO_DELEGADO votoCidadao) {
		Votacao votacao = votarPropostaHandler.getVotacaoByID(votacaoDTO.getId()).get();
		Cidadao cidadao = votarPropostaHandler.getCidadaoByID(cidadaoDTO.getId()).get();
		
		return votarPropostaHandler.votarProposta(votacao, cidadao, votoCidadao);
	}

	public Optional<VotacaoDTO> getVotacaoByID (long id) {
		return votarPropostaHandler.getVotacaoByID(id).map(Votacao::toVotacaoDTO);
	}

	public String getVotoOmissao(CidadaoDTO c, VotacaoDTO v) {
		Cidadao cid = votarPropostaHandler.getCidadaoByID(c.getId()).get();
		Votacao votacao = votarPropostaHandler.getVotacaoByID(v.getId()).get();
		return votarPropostaHandler.getVotoOmissao(cid, votacao);
	}

}
