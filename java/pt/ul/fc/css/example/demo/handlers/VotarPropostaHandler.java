package pt.ul.fc.css.example.demo.handlers;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.example.demo.catalogs.CatalogoCidadaos;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.example.demo.catalogs.CatalogoVotacoes;
import pt.ul.fc.css.example.demo.entities.Cidadao;
import pt.ul.fc.css.example.demo.entities.Delegado;
import pt.ul.fc.css.example.demo.entities.ProjetoLei;
import pt.ul.fc.css.example.demo.entities.Votacao;
import pt.ul.fc.css.example.demo.enums.VOTO_DELEGADO;

@Component
public class VotarPropostaHandler {

	@Autowired private CatalogoVotacoes catVotacoes;
	@Autowired private CatalogoCidadaos catCidadao;
	@Autowired private CatalogoCidadaos catCidadaos;

	// handler func
	public String votarProposta(Votacao votacao, Cidadao cidadao, VOTO_DELEGADO votoCidadao) {
		StringBuilder sb = new StringBuilder();

		List<Votacao> votacoes = catVotacoes.getListagemVotacoesEmCurso();
		sb.append("Votações: ");
		votacoes.forEach(
				v -> {
					sb.append(" Título: " + v.getProjetoLeiVotaçao().getTítulo() + " ID: " + v.getId() + " | ");
				});

		sb.append(
				"ESCOLHIDA: Título: "
						+ votacao.getProjetoLeiVotaçao().getTitulo()
						+ " ID: "
						+ votacao.getId());

		Delegado delegadoAssociado =
				catVotacoes.getAssociadoMaisEspecifico(
						cidadao, votacao.getProjetoLeiVotaçao().getSubTema());

		VOTO_DELEGADO votoOmissao = null;

		if (delegadoAssociado != null)
			votoOmissao = catVotacoes.getVotoAssociado(votacao, delegadoAssociado);

		if (votoOmissao != null) {
			sb.append("\nVoto por omissão: " + votoOmissao);
		} else {
			sb.append("\nSem voto por omissão.");
		}

		if (votoCidadao != null) {
			if (cidadao.isDelegado()) {
				catVotacoes.votarComoDelegado(votacao, cidadao, votoCidadao);
			} else {
				catVotacoes.votarComoNaoDelegado(votacao, cidadao, votoCidadao);
			}
		} else {
			if (votoOmissao != null) catVotacoes.votarComoNaoDelegado(votacao, cidadao, votoOmissao);
		}

		catVotacoes.save(votacao);
		return sb.toString();
	}

	public CatalogoVotacoes getCatVotacoes() {
		return catVotacoes;
	}

	public void setCatVotacoes(CatalogoVotacoes catVotacoes) {
		this.catVotacoes = catVotacoes;
	}

	public CatalogoCidadaos getCatCidadao() {
		return catCidadao;
	}

	public void setCatCidadao(CatalogoCidadaos catCidadao) {
		this.catCidadao = catCidadao;
	}

	public Optional<Votacao> getVotacaoByID(Long id) {
		return catVotacoes.getVotacaoByID(id);
	}

	public Optional<Cidadao> getCidadaoByID(Long id) {
		return catCidadaos.getCidadaoByID(id);
	}

	public String getVotoOmissao(Cidadao cid, Votacao votacao) {
		Delegado delegadoAssociado =
				catVotacoes.getAssociadoMaisEspecifico(
						cid, votacao.getProjetoLeiVotaçao().getSubTema());

		VOTO_DELEGADO votoOmissao = null;

		if (delegadoAssociado != null)
			votoOmissao = catVotacoes.getVotoAssociado(votacao, delegadoAssociado);

		if (votoOmissao != null) {
			return votoOmissao.toString();
		} else {
			return "Sem voto por omissão.";
		}
	}
}
