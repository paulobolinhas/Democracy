package pt.ul.fc.css.example.demo.entities;

import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Embeddable
public class DelegadoParaTema {
    
    @OneToOne
    private Delegado delegado;
    
    @OneToOne
    private Tema tema;
    
 public DelegadoParaTema() {}
    
    public DelegadoParaTema(Delegado delegado, Tema subtema) {
        this.delegado = delegado;
        this.tema = subtema;
    }
    
    // getters e setters

    public Delegado getDelegado() {
        return delegado;
    }

    public void setDelegado(Delegado delegado) {
        this.delegado = delegado;
    }

    public Tema getSubTema() {
        return tema;
    }

    public void setSubTema(SubTema subtema) {
        this.tema = subtema;
    }
}
