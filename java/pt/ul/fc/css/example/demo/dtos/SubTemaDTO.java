package pt.ul.fc.css.example.demo.dtos;

import pt.ul.fc.css.example.demo.entities.SubTema;

public class SubTemaDTO extends TemaDTO {

  private SubTema temaPai;
  private String tituloSubTema;

  public SubTemaDTO() {}

  public SubTemaDTO(String tituloSubTema) {
    super(tituloSubTema);
  }

  public String getTituloSubTema() {
    return tituloSubTema;
  }

  public void setTituloSubTema(String tituloSubTema) {
    this.tituloSubTema = tituloSubTema;
  }

  public void setTemaPai(SubTema temaPai) {
    this.temaPai = temaPai;
  }
}
