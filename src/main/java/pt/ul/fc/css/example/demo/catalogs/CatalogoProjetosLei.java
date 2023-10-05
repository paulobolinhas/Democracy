package pt.ul.fc.css.example.demo.catalogs;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import pt.ul.fc.css.example.demo.entities.ProjetoLei;

import pt.ul.fc.css.example.demo.enums.ESTADO_PROJETO_LEI;

import pt.ul.fc.css.example.demo.repositories.ProjetoLeiRepository;

@Component
public class CatalogoProjetosLei {

  @Autowired private ProjetoLeiRepository projetoLeiRepository;
  
  public void createProjetoLei(ProjetoLei projLei) {
    projetoLeiRepository.save(projLei);
  }

  public List<ProjetoLei> getProjetoLeiExpirado() {

    return projetoLeiRepository.findByEstado(ESTADO_PROJETO_LEI.EXPIRADO);
  }
  
  public ProjetoLei getProjetoById(int proj_id) {

	    return (ProjetoLei) projetoLeiRepository.findById((long) proj_id).get();
	  }

  public String consultarProjetos(ProjetoLei projetoLei) {
    StringBuilder sb = new StringBuilder().append("Projetos Lei existentes: ");

    List<ProjetoLei> projetos = projetoLeiRepository.findAllNotExpired(ESTADO_PROJETO_LEI.EXPIRADO);
    projetos.forEach(
        p -> {
          sb.append(p.getTitulo() + " , ID: " + p.getId() + " | ");
        });

    sb.append(
        " ESCOLHIDO: "
            + projetoLei.getTitulo()
            + " - Details: "
            + projetoLei.getDesc());

    return sb.toString();
  }
  
  public List<ProjetoLei> getListaProjetosLeiNaoExpirados() {
	  return projetoLeiRepository.findAllNotExpired(ESTADO_PROJETO_LEI.EXPIRADO);
  }
  
  public Optional<ProjetoLei> getProjetoLeiByID(Long id) {
      return projetoLeiRepository.findById(id);
  }

  @Scheduled(fixedRate = 3000)
  public void fecharProjetoLeiExpirados() {
    List<ProjetoLei> projetosLei = projetoLeiRepository.findAll();
    projetosLei.forEach(projetoLei -> projetoLei.checkExpiration());
    projetoLeiRepository.saveAll(projetosLei);
  }

  public void saveProjetoLei(ProjetoLei proj) {
    projetoLeiRepository.save(proj);
  }
  
  public Optional<ProjetoLei> getProjLeiByID(Long id) {
		return projetoLeiRepository.findById(id);
	}
}
