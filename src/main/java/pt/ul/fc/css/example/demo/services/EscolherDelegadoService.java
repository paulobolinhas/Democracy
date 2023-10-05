package pt.ul.fc.css.example.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pt.ul.fc.css.example.demo.dtos.CidadaoDTO;
import pt.ul.fc.css.example.demo.dtos.DelegadoDTO;
import pt.ul.fc.css.example.demo.dtos.TemaDTO;
import pt.ul.fc.css.example.demo.entities.Cidadao;
import pt.ul.fc.css.example.demo.entities.Delegado;
import pt.ul.fc.css.example.demo.entities.Tema;
import pt.ul.fc.css.example.demo.handlers.EscolherDelegadoHandler;


@Service
public class EscolherDelegadoService {

	@Autowired private EscolherDelegadoHandler escolherDelegadoHandler;

	public String escolherDelegados(CidadaoDTO c, DelegadoDTO d, TemaDTO t) {
		
		Cidadao cid = escolherDelegadoHandler.getCidadaoByID(c.getId()).get();
		Delegado delegado = escolherDelegadoHandler.getDelegadoByID(d.getId()).get();
		Tema tema = escolherDelegadoHandler.getTemaByName(t.getTitulo()).get();
		
		return escolherDelegadoHandler.escolherDelegados(cid, delegado, tema);
	}

	public List<Delegado> getDelegados() {
		return escolherDelegadoHandler.getDelegados();
	}

}
