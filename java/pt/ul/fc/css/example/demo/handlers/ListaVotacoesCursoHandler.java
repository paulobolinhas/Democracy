package pt.ul.fc.css.example.demo.handlers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import pt.ul.fc.css.example.demo.catalogs.CatalogoVotacoes;
import pt.ul.fc.css.example.demo.dtos.VotacaoDTO;
import pt.ul.fc.css.example.demo.entities.Votacao;
import pt.ul.fc.css.example.demo.enums.ESTADO_VOTACAO;
import pt.ul.fc.css.example.demo.repositories.VotacoesRepository;

@Component
public class ListaVotacoesCursoHandler {

  @Autowired private CatalogoVotacoes catVotacoes;

  public List<VotacaoDTO> getListagemVotacoesEmCurso() {
    return catVotacoes.getListagemVotacoesEmCursoDTO();
  }
}
