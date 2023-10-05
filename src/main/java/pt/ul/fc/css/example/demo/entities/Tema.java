package pt.ul.fc.css.example.demo.entities;

import static jakarta.persistence.InheritanceType.JOINED;

import io.micrometer.common.lang.NonNull;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.OneToMany;
import pt.ul.fc.css.example.demo.dtos.TemaDTO;

import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = JOINED)
public class Tema {

  @Id @GeneratedValue private long id;

  @NonNull private String titulo;

  @OneToMany(fetch = FetchType.EAGER)
  private List<SubTema> listaSubTemas;

  public Tema(String titulo) {
    this.titulo = titulo;
    this.listaSubTemas = new ArrayList<SubTema>();
  }

  public Tema() {}

  public TemaDTO toTemaDTO() {
    TemaDTO temaDTO = new TemaDTO();
    temaDTO.setId(id);
    temaDTO.setTitulo(titulo);
    return temaDTO;
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

  public List<SubTema> getListaSubTemas() {
    return listaSubTemas;
  }

  public void setListaSubTemas(List<SubTema> listaSubTemas) {
    this.listaSubTemas = listaSubTemas;
  }

  @Override
  public String toString() {
    return "Tema [id=" + id + ", titulo=" + titulo + "]";
  }
}
