package pt.ul.fc.css.example.demo.catalogs;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import pt.ul.fc.css.example.demo.entities.CartaoCidadao;

public class CatalogoCC {

	@Autowired private List<CartaoCidadao> catCC;

	public List<CartaoCidadao> getCatCC() {
		return catCC;
	}

	public void setCatCC(List<CartaoCidadao> catCC) {
		this.catCC = catCC;
	}
}
