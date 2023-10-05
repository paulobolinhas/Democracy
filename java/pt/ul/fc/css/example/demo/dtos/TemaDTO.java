package pt.ul.fc.css.example.demo.dtos;

public class TemaDTO {

  private long id;
  private String titulo;

  public TemaDTO() {}

  public TemaDTO(String titulo) {
    this.titulo = titulo;
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
}
