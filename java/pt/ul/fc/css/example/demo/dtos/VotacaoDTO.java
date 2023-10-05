package pt.ul.fc.css.example.demo.dtos;

import java.sql.Timestamp;
import pt.ul.fc.css.example.demo.entities.ProjetoLei;

public class VotacaoDTO {

  private long id;
  private Timestamp dataInicio;
  private Timestamp dataEncerramento;
  private ProjetoLeiDTO projetoLeiVotacao;
  private int votosFavoraveis;
  private int votosDesfavoraveis;

  public VotacaoDTO() {}

  public VotacaoDTO(
      Timestamp dataInicio, Timestamp dataEncerramento, ProjetoLeiDTO projetoLeiVotacao) {
    this.dataInicio = dataInicio;
    this.dataEncerramento = dataEncerramento;
    this.projetoLeiVotacao = projetoLeiVotacao;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Timestamp getDataInicio() {
    return dataInicio;
  }

  public void setDataInicio(Timestamp dataInicio) {
    this.dataInicio = dataInicio;
  }

  public Timestamp getDataEncerramento() {
    return dataEncerramento;
  }

  public void setDataEncerramento(Timestamp dataEncerramento) {
    this.dataEncerramento = dataEncerramento;
  }

  public ProjetoLeiDTO getProjetoLeiVotacao() {
    return projetoLeiVotacao;
  }

  public void setProjetoLeiVotacao(ProjetoLeiDTO projetoLeiVotacao) {
    this.projetoLeiVotacao = projetoLeiVotacao;
  }

public int getVotosFavoraveis() {
	return votosFavoraveis;
}

public void setVotosFavoraveis(int votosFavoraveis) {
	this.votosFavoraveis = votosFavoraveis;
}

public int getVotosDesfavoraveis() {
	return votosDesfavoraveis;
}

public void setVotosDesfavoraveis(int votosDesfavoraveis) {
	this.votosDesfavoraveis = votosDesfavoraveis;
}
}
