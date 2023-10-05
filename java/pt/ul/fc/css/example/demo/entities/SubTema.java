package pt.ul.fc.css.example.demo.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import pt.ul.fc.css.example.demo.dtos.SubTemaDTO;

@Entity
public class SubTema extends Tema {

  private String tituloSubTema;

  @ManyToOne private SubTema temaPai;

  public SubTema() {}

  public SubTema(String tituloSubTema) {
    super(tituloSubTema);
    this.tituloSubTema = tituloSubTema;
    this.temaPai = null;
  }

  public SubTemaDTO toSubTemaDTO() {
    SubTemaDTO subTemaDTO = new SubTemaDTO();
    subTemaDTO.setId(getId());
    subTemaDTO.setTitulo(getTitulo());
    subTemaDTO.setTituloSubTema(tituloSubTema);
    subTemaDTO.setTemaPai(temaPai);
    return subTemaDTO;
  }

  public void setTemaPai(SubTema temaPai) {
    this.temaPai = temaPai;
  }

  public String getTituloSubTema() {
    return tituloSubTema;
  }

  public void setTituloSubTema(String tituloSubTema) {
    this.tituloSubTema = tituloSubTema;
  }

  public Tema getTemaPai() {
    return this.temaPai;
  }

  public void addListaSubTemas(SubTema urgencias) {
    super.getListaSubTemas().add(urgencias);
  }
}
