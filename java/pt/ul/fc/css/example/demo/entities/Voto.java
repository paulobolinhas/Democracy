package pt.ul.fc.css.example.demo.entities;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import pt.ul.fc.css.example.demo.enums.VOTO_DELEGADO;

@Embeddable
public class Voto {

	@OneToOne
	private Delegado associado;
	@Enumerated(EnumType.STRING)
	private VOTO_DELEGADO voto;
	
	public Voto() {
	}

	public Voto(Delegado associado, VOTO_DELEGADO voto) {
		this.associado = associado;
		this.voto = voto;
	}

	public VOTO_DELEGADO getVoto() {
		return voto;
	}
	public void setVoto(VOTO_DELEGADO voto) {
		this.voto = voto;
	}

	public Delegado getAssociado() {
		return associado;
	}

	public void setAssociado(Delegado associado) {
		this.associado = associado;
	}

}
