package pt.ul.fc.css.example.demo.dtos;

public class CidadaoDTO {

  private long id;
  private String nome;
  private int nif;
  private String senha;

  public CidadaoDTO() {}

  public CidadaoDTO(String nome, int nif, String senha) {
    this.nome = nome;
    this.nif = nif;
    this.senha = senha;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public int getNif() {
    return nif;
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
}
