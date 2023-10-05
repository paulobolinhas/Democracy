package pt.ul.fc.css.example.demo.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import pt.ul.fc.css.example.demo.dtos.DelegadoDTO;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Delegado extends Cidadao {

  @OneToMany(mappedBy = "delegadoProponente", fetch = FetchType.EAGER)
  private List<ProjetoLei> listaProjetosLeiEnvolvidos;

  public Delegado() {
    super();
    super.setIsDelegado(true);
    this.listaProjetosLeiEnvolvidos = new ArrayList<ProjetoLei>();
  }

  public DelegadoDTO toDelegadoDTO() {
    DelegadoDTO delegadoDTO = new DelegadoDTO();
    delegadoDTO.setId(getId());
    delegadoDTO.setNome(getNome());
    delegadoDTO.setNif(getNif());
    delegadoDTO.setSenha(getSenha());
    return delegadoDTO;
  }

  public List<ProjetoLei> getListaProjetosLeiEnvolvidos() {
    return listaProjetosLeiEnvolvidos;
  }

  public void addListaProjetosLeiEnvolvidos(ProjetoLei projLei) {
    this.listaProjetosLeiEnvolvidos.add(projLei);
  }

  public void setListaProjetosLeiEnvolvidos(List<ProjetoLei> listaProjetosLeiEnvolvidos) {
    this.listaProjetosLeiEnvolvidos = listaProjetosLeiEnvolvidos;
  }

  public String toString() {
    return " nome: " + super.getNome();
  }
}
