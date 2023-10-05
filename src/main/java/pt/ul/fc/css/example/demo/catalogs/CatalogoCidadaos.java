package pt.ul.fc.css.example.demo.catalogs;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pt.ul.fc.css.example.demo.entities.Cidadao;
import pt.ul.fc.css.example.demo.repositories.CidadaoRepository;

@Component
public class CatalogoCidadaos {

	@Autowired private CidadaoRepository cidRepository;

	public Cidadao getCidadaoById(int cid_id) {

		return (Cidadao) cidRepository.findById((long) cid_id).get();
	}
    
    public Optional<Cidadao> getCidadaoByID(Long id) {
		return cidRepository.findById(id);
	}
}
