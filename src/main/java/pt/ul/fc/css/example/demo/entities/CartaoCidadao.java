package pt.ul.fc.css.example.demo.entities;

public class CartaoCidadao {
	
	private Cidadao cid;
	private String dataValidade;
	
	public CartaoCidadao(Cidadao cid, String dataValidade) {
		this.cid = cid;
		this.dataValidade = dataValidade;
	}
	public Cidadao getCid() {
		return cid;
	}
	public void setCid(Cidadao cid) {
		this.cid = cid;
	}
	public String getDataValidade() {
		return dataValidade;
	}
	public void setDataValidade(String dataValidade) {
		this.dataValidade = dataValidade;
	}

}
