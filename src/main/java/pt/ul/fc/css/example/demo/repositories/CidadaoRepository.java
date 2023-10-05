package pt.ul.fc.css.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import pt.ul.fc.css.example.demo.entities.Cidadao;

public interface CidadaoRepository extends JpaRepository<Cidadao, Long> {
	
//	@Query("SELECT c FROM Cidadao c WHERE c.id = :id")
//	  Cidadao findByID(@Param("id") long id);
	
}
