package pt.ul.fc.css.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pt.ul.fc.css.example.demo.entities.SubTema;
import pt.ul.fc.css.example.demo.entities.Tema;

public interface TemaRepository extends JpaRepository<Tema, Long> {
	
	  @Query("SELECT t FROM Tema t WHERE t.id = :id")
	  Tema findByID(@Param("id") long id);
	  
	  @Query("SELECT t FROM Tema t WHERE t.titulo = :titulo")
	  SubTema findByName(@Param("titulo") String titulo);
}
