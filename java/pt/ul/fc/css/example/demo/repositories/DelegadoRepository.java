package pt.ul.fc.css.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pt.ul.fc.css.example.demo.entities.Delegado;

public interface DelegadoRepository extends JpaRepository<Delegado, Long> {
	
//	  @Query("SELECT d FROM Delegado d WHERE d.id = :id")
//	  Delegado findByID(@Param("id") long id);
	  
	  @Query("SELECT d FROM Delegado d WHERE d.nome = :nome")
	  Delegado findByName(@Param("nome") String nome);
	  
}
