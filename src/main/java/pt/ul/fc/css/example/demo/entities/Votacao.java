package pt.ul.fc.css.example.demo.entities;

import io.micrometer.common.lang.NonNull;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import pt.ul.fc.css.example.demo.dtos.VotacaoDTO;
import pt.ul.fc.css.example.demo.enums.ESTADO_VOTACAO;
import pt.ul.fc.css.example.demo.enums.VOTO_DELEGADO;

@Entity
public class Votacao {

  @Id @GeneratedValue private long id;

  @NonNull private Timestamp dataInicio;

  @NonNull private Timestamp dataEncerramento;

  @Enumerated(EnumType.STRING)
  private ESTADO_VOTACAO estado;

  @OneToOne
  @JoinColumn(name = "projetoLeiVotaçao_id")
  private ProjetoLei projetoLeiVotaçao;

  @ManyToMany(fetch = FetchType.EAGER)
  private List<Cidadao> listaPessoasQueVotaram;

  @NonNull private int votosFavoraveis;

  @NonNull private int votosDesfavoraveis;

  @ElementCollection(fetch = FetchType.EAGER)
  private List<Voto> votos;

  public Votacao(Timestamp dataInicio, Timestamp dataEncerramento, ProjetoLei projetoLeiVotaçao) {
    super();
    this.dataInicio = dataInicio;
    this.dataEncerramento = dataEncerramento;
    this.projetoLeiVotaçao = projetoLeiVotaçao;
    this.listaPessoasQueVotaram = new ArrayList<Cidadao>();
    this.votos = new ArrayList<Voto>();
    this.listaPessoasQueVotaram.add((Cidadao) projetoLeiVotaçao.getDelegadoProponente());
    this.votos.add(new Voto(projetoLeiVotaçao.getDelegadoProponente(), VOTO_DELEGADO.FAVORAVEL));
    this.votosFavoraveis = 1;
    this.votosDesfavoraveis = 0;
    this.estado = ESTADO_VOTACAO.EM_CURSO;
  }

  public Votacao() {}

  public VotacaoDTO toVotacaoDTO() {
    VotacaoDTO votacaoDTO = new VotacaoDTO();
    votacaoDTO.setId(id);
    votacaoDTO.setDataInicio(dataInicio);
    votacaoDTO.setDataEncerramento(dataEncerramento);
    votacaoDTO.setProjetoLeiVotacao(projetoLeiVotaçao.toProjetoLeiDTO());
    votacaoDTO.setVotosFavoraveis(votosFavoraveis);
    votacaoDTO.setVotosDesfavoraveis(votosDesfavoraveis);
    votacaoDTO.setEstado(estado);
    return votacaoDTO;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public ESTADO_VOTACAO getEstado() {
    return estado;
  }

  public void setEstado(ESTADO_VOTACAO estado) {
    this.estado = estado;
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

  public List<Voto> getVotos() {
    return votos;
  }

  public void setVotos(List<Voto> votos) {
    this.votos = votos;
  }

  public ProjetoLei getProjetoLeiVotaçao() {
    return projetoLeiVotaçao;
  }

  public void setProjetoLeiVotaçao(ProjetoLei projetoLeiVotaçao) {
    this.projetoLeiVotaçao = projetoLeiVotaçao;
  }

  public List<Cidadao> getListaPessoasQueVotaram() {
    return listaPessoasQueVotaram;
  }

  public void setListaPessoasQueVotaram(List<Cidadao> listaPessoasQueVotaram) {
    this.listaPessoasQueVotaram = listaPessoasQueVotaram;
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

  public void adicionarVoto(Voto v) {
    Boolean isChanged = false;

    int i = 0;
    while (i < this.votos.size() && isChanged == false) {
      if (this.votos.get(i).getAssociado().getId() == v.getAssociado().getId()) {
        this.votos.set(i, v);
        isChanged = true;
      }
      i++;
    }

    if (!isChanged) this.votos.add(v);
  }

  public boolean adicionarListaPessoasQueVotaram(Cidadao c, Votacao v, VOTO_DELEGADO votoAssociado) {
    Boolean isVotou = false;

    int i = 0;
    while (i < this.listaPessoasQueVotaram.size() && isVotou == false) {
      if (this.listaPessoasQueVotaram.get(i).getId() == c.getId()) {
        isVotou = true;
      }
      i++;
    }

    if (!isVotou) {
    	this.listaPessoasQueVotaram.add(c);
    	
    	if (votoAssociado == VOTO_DELEGADO.FAVORAVEL) {
			v.setVotosFavoraveis(v.getVotosFavoraveis() + 1);
		} else {
			v.setVotosDesfavoraveis(v.getVotosDesfavoraveis() + 1);
		}
    }

    return isVotou;
  }
  
  public void checkExpiration() {
	    long currentTime = System.currentTimeMillis();
	    long expirationTime = this.dataEncerramento.getTime();
	    if (expirationTime < currentTime) {
	      this.estado = ESTADO_VOTACAO.EXPIRADA;
	    }
	  }

  @Override
  public String toString() {
    return "Votacao [id="
        + id
        + ", estado="
        + estado
        + ", votosFavoraveis="
        + votosFavoraveis
        + ", votosDesfavoraveis="
        + votosDesfavoraveis
        + "]";
  }
}
