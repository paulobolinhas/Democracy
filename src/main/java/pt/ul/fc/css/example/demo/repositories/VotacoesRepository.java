package pt.ul.fc.css.example.demo.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pt.ul.fc.css.example.demo.entities.Votacao;
import pt.ul.fc.css.example.demo.enums.ESTADO_VOTACAO;

public interface VotacoesRepository extends JpaRepository<Votacao, Long> {

  @Query("SELECT v FROM Votacao v WHERE v.estado = :estado")
  List<Votacao> findByEstado(@Param("estado") ESTADO_VOTACAO estado);
}
