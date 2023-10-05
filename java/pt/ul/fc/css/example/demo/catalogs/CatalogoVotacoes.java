package pt.ul.fc.css.example.demo.catalogs;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import pt.ul.fc.css.example.demo.dtos.VotacaoDTO;
import pt.ul.fc.css.example.demo.entities.Cidadao;
import pt.ul.fc.css.example.demo.entities.Delegado;
import pt.ul.fc.css.example.demo.entities.DelegadoParaTema;
import pt.ul.fc.css.example.demo.entities.ProjetoLei;
import pt.ul.fc.css.example.demo.entities.SubTema;
import pt.ul.fc.css.example.demo.entities.Tema;
import pt.ul.fc.css.example.demo.entities.Votacao;
import pt.ul.fc.css.example.demo.entities.Voto;
import pt.ul.fc.css.example.demo.enums.ESTADO_VOTACAO;
import pt.ul.fc.css.example.demo.enums.VOTO_DELEGADO;
import pt.ul.fc.css.example.demo.repositories.CidadaoRepository;
import pt.ul.fc.css.example.demo.repositories.VotacoesRepository;

@Component
public class CatalogoVotacoes {

	@Autowired private VotacoesRepository votacoesRepository;
	@Autowired private CidadaoRepository cidadaoRepository;

	public Votacao getVotacaoById(int votacao_id) {
		return (Votacao) votacoesRepository.findById((long) votacao_id).get();
	}

	public List<Votacao> getListagemVotacoesEmCurso() {
		return votacoesRepository.findByEstado(ESTADO_VOTACAO.EM_CURSO);
	}

	public List<VotacaoDTO> getListagemVotacoesEmCursoDTO() {
		List<VotacaoDTO> votacoes = new ArrayList<VotacaoDTO>();
		for (Votacao v : votacoesRepository.findByEstado(ESTADO_VOTACAO.EM_CURSO)) {
			VotacaoDTO v2 = v.toVotacaoDTO();
			votacoes.add(v2);
		}
		return votacoes;
	}

	
	public boolean fecharVotacaoV(Votacao v) {
		
		fecharVotacoesExpiradas();
		
		if (v.getEstado() == ESTADO_VOTACAO.EXPIRADA) {

			List<Cidadao> listaPessoasVotaram = v.getListaPessoasQueVotaram();
			List<Cidadao> listaPessoasNaoVotaram = new ArrayList<Cidadao>();

			for (Cidadao c : cidadaoRepository.findAll()) {
				if (!listaPessoasVotaram.contains(c)) {
					listaPessoasNaoVotaram.add(c);
				}
			}

			Delegado associado = null;
			SubTema st = v.getProjetoLeiVotaçao().getSubTema();
			VOTO_DELEGADO votoAssociado = null;

			for (Cidadao cnv : listaPessoasNaoVotaram) {

				associado = getAssociadoMaisEspecifico(cnv, st);

				if (associado != null) {

					votoAssociado = getVotoAssociado(v, associado);

					if (votoAssociado != null) {

						votarComoNaoDelegado(v, cnv, votoAssociado);
					}
				}
			}
			Boolean isAprovada = v.getVotosFavoraveis() > (0.5 * v.getListaPessoasQueVotaram().size());

			if (isAprovada) {
				v.setEstado(ESTADO_VOTACAO.APROVADA);
			} else {
				v.setEstado(ESTADO_VOTACAO.REJEITADA);
			}

			votacoesRepository.save(v);
			return isAprovada;
		} else {
			new Exception("Votação não Expirada.");
			return false;
		}
	}

	@Scheduled(fixedRate = 3000)
	private void fecharVotacoesExpiradas() {
		List<Votacao> votacoes = votacoesRepository.findAll();
		votacoes.forEach(votacao -> votacao.checkExpiration());
		votacoesRepository.saveAll(votacoes);
	}

	public VOTO_DELEGADO getVotoAssociado(Votacao v, Delegado associado) {

		for (Voto voto : v.getVotos()) {
			if (voto.getAssociado().getId() == associado.getId()) {
				return voto.getVoto();
			}
		}
		return null;
	}

	public Delegado getAssociadoMaisEspecifico(Cidadao cnv, SubTema st) {
		Delegado associado = catchAssociado(cnv, st);

		if (associado == null) {

			while (st.getTemaPai() != null) {
				associado = catchAssociado(cnv, st.getTemaPai());
				st = (SubTema) st.getTemaPai();

				if (associado != null) {
					break;
				}
			}
		}

		return associado;
	}

	public void votarComoNaoDelegado(Votacao v, Cidadao c, VOTO_DELEGADO votoAssociado) {
		v.adicionarListaPessoasQueVotaram(c, v, votoAssociado);
	}

	public static Delegado catchAssociado(Cidadao c, Tema t) {
		Delegado associado = null;
		for (DelegadoParaTema dt : c.getListaDelegados()) {
			if (dt.getSubTema().getId() == t.getId()) {
				associado = dt.getDelegado();
				return associado;
			}
		}
		return associado;
	}

	public Votacao createVotacao(ProjetoLei proj) {
		Timestamp dataFimValida = createDataFimVotacaoValida(proj.getDatahoraValidade());
		Votacao v = new Votacao(new Timestamp(System.currentTimeMillis()), dataFimValida, proj);
		votacoesRepository.save(v);
		return v;
	}

	private Timestamp createDataFimVotacaoValida(java.sql.Timestamp datahoraValidadeProjeto) {

		Timestamp datahoraFimVotacao;

		// Set the minimum and maximum duration for the voting period
		int minDays = 15;
		int maxDays = 60;

		// Calculate the duration between the current date and the expiration date
		long duration = datahoraValidadeProjeto.getTime() - System.currentTimeMillis();

		// Calculate the duration in days
		int durationInDays = (int) (duration / (1000 * 60 * 60 * 24));

		// If the duration is less than the minimum duration, set the end date to the minimum
		if (durationInDays < minDays) {
			datahoraFimVotacao =
					new Timestamp(System.currentTimeMillis() + minDays * (1000 * 60 * 60 * 24));
		}
		// If the duration is more than the maximum duration, set the end date to the maximum
		else if (durationInDays > maxDays) {
			datahoraFimVotacao =
					new Timestamp(System.currentTimeMillis() + maxDays * (1000 * 60 * 60 * 24));
		}
		// Otherwise, set the end date to the expiration date
		else {
			datahoraFimVotacao = datahoraValidadeProjeto;
		}

		return datahoraFimVotacao;
	}

	public void votarComoDelegado(Votacao votacao, Cidadao cidadao, VOTO_DELEGADO votoCidadao) {
		Voto newVoto = new Voto((Delegado) cidadao, votoCidadao);

		votarComoNaoDelegado(votacao, cidadao, votoCidadao);

		votacao.adicionarVoto(newVoto);

		votacoesRepository.save(votacao);
	}

	public void save(Votacao votacao) {
		votacoesRepository.save(votacao);
	}

	public Optional<Votacao> getVotacaoByID(Long id) {
		return votacoesRepository.findById(id);
	}

}
