package pt.ul.fc.css.example.demo.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pt.ul.fc.css.example.demo.catalogs.CatalogoProjetosLei;

@Component
public class FecharProjetosLeiExpiradosHandler {

  @Autowired private CatalogoProjetosLei catProjetoLei;

  public void fecharProjetosLeiExpirados() {
    // @EnableScheduling
    catProjetoLei.fecharProjetoLeiExpirados();
  }
}
