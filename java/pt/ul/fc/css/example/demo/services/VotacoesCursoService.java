package pt.ul.fc.css.example.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pt.ul.fc.css.example.demo.handlers.ListaVotacoesCursoHandler;
import pt.ul.fc.css.example.demo.dtos.VotacaoDTO;

@Service
public class VotacoesCursoService {

  @Autowired private ListaVotacoesCursoHandler listaVotacoesCursoHandler;

  public List<VotacaoDTO> getListagemVotacoesEmCursoHandler() {
    return listaVotacoesCursoHandler.getListagemVotacoesEmCurso();
  }
  
  
  
}
