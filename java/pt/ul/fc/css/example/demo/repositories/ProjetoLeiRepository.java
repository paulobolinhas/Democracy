package pt.ul.fc.css.example.demo.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pt.ul.fc.css.example.demo.entities.Cidadao;
import pt.ul.fc.css.example.demo.entities.ProjetoLei;
import pt.ul.fc.css.example.demo.enums.ESTADO_PROJETO_LEI;

public interface ProjetoLeiRepository extends JpaRepository<ProjetoLei, Long> {
	
	  @Query("SELECT p FROM ProjetoLei p WHERE p.estado = :estado")
	  List<ProjetoLei> findByEstado(@Param("estado") ESTADO_PROJETO_LEI estado);
	  
	  @Query("SELECT p FROM ProjetoLei p WHERE p.estado != :estado")
	  List<ProjetoLei> findAllNotExpired(@Param("estado") ESTADO_PROJETO_LEI estado);
	  
//	  @Query("SELECT pl FROM ProjetoLei pl WHERE pl.id  = :id")
//	  ProjetoLei findByID(@Param("id") long id);
	  
	  
	
}
