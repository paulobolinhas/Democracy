package pt.ul.fc.css.example.demo.entities;

import io.micrometer.common.lang.NonNull;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import java.io.File;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import pt.ul.fc.css.example.demo.dtos.ProjetoLeiDTO;
import pt.ul.fc.css.example.demo.enums.ESTADO_PROJETO_LEI;

@Entity
public class ProjetoLei {

  @Id @GeneratedValue private long id;

  @NonNull private String titulo;

  @NonNull private String descricao;

  @NonNull @Lob private File anexoPDF;

  @NonNull private Timestamp datahoraValidade;

  @ManyToOne @NonNull private SubTema subtema;

  @NonNull private int numeroApoiantes;

  @ManyToOne
  @JoinColumn(name = "delegadoProponente_id", nullable = false)
  private Delegado delegadoProponente;

  @Enumerated(EnumType.STRING)
  private ESTADO_PROJETO_LEI estado;

  @ManyToMany(fetch = FetchType.EAGER)
  private List<Cidadao> listaApoiantes;

  public ProjetoLei(
      String titulo,
      String descricao,
      File anexoPDF,
      Timestamp datahoraValidade,
      SubTema subtema,
      Delegado delegadoProponente) {
    this.titulo = titulo;
    this.descricao = descricao;
    this.anexoPDF = anexoPDF;
    this.datahoraValidade = isValidData(datahoraValidade);
    this.subtema = subtema;
    this.delegadoProponente = delegadoProponente;
    this.estado = ESTADO_PROJETO_LEI.EM_APOIO;
    this.listaApoiantes = new ArrayList<Cidadao>();
    this.listaApoiantes.add((Cidadao) delegadoProponente);
    this.numeroApoiantes = 1;
  }

  public ProjetoLei() {}

  public ProjetoLeiDTO toProjetoLeiDTO() {
    ProjetoLeiDTO projetoLeiDTO = new ProjetoLeiDTO();
    projetoLeiDTO.setId(id);
    projetoLeiDTO.setTitulo(titulo);
    projetoLeiDTO.setDescricao(descricao);
    projetoLeiDTO.setAnexoPDF(anexoPDF);
    projetoLeiDTO.setDatahoraValidade(datahoraValidade);
    projetoLeiDTO.setSubtema(subtema.toSubTemaDTO());
    projetoLeiDTO.setDelegadoProponente(delegadoProponente.toDelegadoDTO());
    projetoLeiDTO.setNumeroApoiantes(numeroApoiantes);
    return projetoLeiDTO;
  }

  public String getTítulo() {
    return titulo;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getTitulo() {
    return titulo;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public String getDescricao() {
    return descricao;
  }

  public SubTema getSubTema() {
    return subtema;
  }

  public void setSubTema(SubTema subtema) {
    this.subtema = subtema;
  }

  public Timestamp getDatahoraValidade() {
    return datahoraValidade;
  }

  public void setTítulo(String título) {
    this.titulo = título;
  }

  public String getDesc() {
    return descricao;
  }

  public void setDesc(String desc) {
    this.descricao = desc;
  }

  public File getAnexoPDF() {
    return anexoPDF;
  }

  public void setAnexoPDF(File anexoPDF) {
    this.anexoPDF = anexoPDF;
  }

  public void setDatahoraValidade(Timestamp datahoraValidade) {
    this.datahoraValidade = datahoraValidade;
  }

  public void setDelegadoProponente(Delegado delegadoProponente) {
    this.delegadoProponente = delegadoProponente;
  }

  private Timestamp isValidData(Timestamp datahoraValidade) {
    LocalDateTime datahoraValidadeAux = datahoraValidade.toLocalDateTime();
    LocalDateTime agora = LocalDateTime.now();
    LocalDateTime umAnoDepois = agora.plusYears(1);

    if (datahoraValidadeAux.isAfter(umAnoDepois)) {
      return Timestamp.valueOf(agora.plusYears(1));
    } else {
      return datahoraValidade;
    }
  }

  public ESTADO_PROJETO_LEI getEstado() {
    return estado;
  }

  public void setEstado(ESTADO_PROJETO_LEI estado) {
    this.estado = estado;
  }

  public List<Cidadao> getListaApoiantes() {
    return listaApoiantes;
  }

  public void setListaApoiantes(List<Cidadao> listaApoiantes) {
    this.listaApoiantes = listaApoiantes;
  }

  public void adicionaApoiante(Cidadao cidadao) {
    if (!listaApoiantes.contains(cidadao)) {
      listaApoiantes.add(cidadao);
      this.numeroApoiantes++;
    }
  }

  public int getNumeroApoiantes() {
    //		this.numeroApoiantes = listaApoiantes.size();
    return this.numeroApoiantes;
  }

  public void setNumeroApoiantes(int n) {
    this.numeroApoiantes = n;
  }

  public void checkExpiration() {
    long currentTime = System.currentTimeMillis();
    long expirationTime = this.datahoraValidade.getTime();
    if (expirationTime < currentTime) {
      this.estado = ESTADO_PROJETO_LEI.EXPIRADO;
    }
  }

  public Delegado getDelegadoProponente() {
    return delegadoProponente;
  }

  @Override
  public String toString() {
    return "ProjetoLei [id="
        + id
        + ", titulo="
        + titulo
        + ", descricao="
        + descricao
        + ", tema="
        + subtema.toString()
        + ", delegadoProponente="
        + delegadoProponente.toString()
        + ", estado="
        + estado
        + ", numeroApoiantes="
        + listaApoiantes.size()
        + "]";
  }
}
