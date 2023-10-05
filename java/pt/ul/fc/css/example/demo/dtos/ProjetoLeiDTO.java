package pt.ul.fc.css.example.demo.dtos;

import java.io.File;
import java.sql.Timestamp;
import pt.ul.fc.css.example.demo.entities.Delegado;
import pt.ul.fc.css.example.demo.entities.SubTema;

public class ProjetoLeiDTO {

	private long id;
	private String titulo;
	private String descricao;
	private File anexoPDF;
	private Timestamp datahoraValidade;
	private SubTemaDTO subtema;
	private DelegadoDTO delegadoProponente;
	private int numeroApoiantes;


	public ProjetoLeiDTO() {}

	public ProjetoLeiDTO(
			String titulo,
			String descricao,
			File anexoPDF,
			Timestamp datahoraValidade,
			SubTemaDTO subtema,
			DelegadoDTO delegadoProponente) {
		this.titulo = titulo;
		this.descricao = descricao;
		this.setAnexoPDF(anexoPDF);
		this.datahoraValidade = datahoraValidade;
		this.subtema = subtema;
		this.delegadoProponente = delegadoProponente;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Timestamp getDatahoraValidade() {
		return datahoraValidade;
	}

	public void setDatahoraValidade(Timestamp datahoraValidade) {
		this.datahoraValidade = datahoraValidade;
	}

	public SubTemaDTO getSubtema() {
		return subtema;
	}

	public void setSubtema(SubTemaDTO subTema) {
		this.subtema = subTema;
	}

	public DelegadoDTO getDelegadoProponente() {
		return delegadoProponente;
	}

	public void setDelegadoProponente(DelegadoDTO delegadoProponente) {
		this.delegadoProponente = delegadoProponente;
	}

	public File getAnexoPDF() {
		return anexoPDF;
	}

	public void setAnexoPDF(File anexoPDF) {
		this.anexoPDF = anexoPDF;
	}

	public int getNumeroApoiantes() {
		return numeroApoiantes;
	}

	public void setNumeroApoiantes(int numeroApoiantes) {
		this.numeroApoiantes = numeroApoiantes;
	}

}
