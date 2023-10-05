package pt.ul.fc.css.example.demo.entities;

import static jakarta.persistence.InheritanceType.JOINED;

import io.micrometer.common.lang.NonNull;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import pt.ul.fc.css.example.demo.dtos.CidadaoDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Inheritance(strategy = JOINED)
public class Cidadao {

  @Id @GeneratedValue private long id;

  @NonNull private String nome;

  @NonNull private int nif;

  @NonNull private String senha;

  private boolean isDelegado;

  @ElementCollection(fetch = FetchType.EAGER)
  private List<DelegadoParaTema> delegadosTemas;

  public Cidadao(String nome, int nif, String senha) {
    this.nome = nome;
    this.nif = nif;
    this.senha = senha;
    this.delegadosTemas = new ArrayList<DelegadoParaTema>();
    this.isDelegado = false;
  }

  public Cidadao() {}

  public CidadaoDTO toCidadaoDTO() {
    CidadaoDTO cidadaoDTO = new CidadaoDTO();
    cidadaoDTO.setId(id);
    cidadaoDTO.setNome(nome);
    cidadaoDTO.setNif(nif);
    cidadaoDTO.setSenha(senha);
    return cidadaoDTO;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public List<DelegadoParaTema> getListaDelegados() {
    return delegadosTemas;
  }

  public String getNome() {
    return nome;
  }

  public int getNif() {
    return nif;
  }

  public boolean isDelegado() {
    return isDelegado;
  }

  public void setIsDelegado(boolean isDelegado) {
    this.isDelegado = isDelegado;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public void setListaDelegados(List<DelegadoParaTema> listaDelegados) {
    this.delegadosTemas = listaDelegados;
  }

  public void setDelegadoTema(DelegadoParaTema delegadoParaTema) {

    Boolean isChanged = false;

    int i = 0;
    while (i < this.delegadosTemas.size() && isChanged == false) {
      if (this.delegadosTemas.get(i).getSubTema().getId()
          == delegadoParaTema.getSubTema().getId()) {
        this.delegadosTemas.set(i, delegadoParaTema);
        isChanged = true;
      }
      i++;
    }

    if (!isChanged) this.delegadosTemas.add(delegadoParaTema);
  }

  public void setNif(int nif) {
    this.nif = nif;
  }

  public String getSenha() {
    return senha;
  }

  public void setSenha(String senha) {
    this.senha = senha;
  }

  @Override
  public String toString() {
    return "Cidadao [nome=" + nome + ", nif=" + nif + ", senha=" + senha + "]";
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    Cidadao cidadao = (Cidadao) obj;
    return Objects.equals(id, cidadao.id);
  }
}
