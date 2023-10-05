package pt.ul.fc.css.example.demo;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import pt.ul.fc.css.example.demo.dtos.ProjetoLeiDTO;
import pt.ul.fc.css.example.demo.dtos.VotacaoDTO;
import pt.ul.fc.css.example.demo.entities.Cidadao;
import pt.ul.fc.css.example.demo.entities.Delegado;

import pt.ul.fc.css.example.demo.entities.ProjetoLei;
import pt.ul.fc.css.example.demo.entities.SubTema;
import pt.ul.fc.css.example.demo.entities.Tema;
import pt.ul.fc.css.example.demo.entities.Votacao;
import pt.ul.fc.css.example.demo.entities.Voto;
import pt.ul.fc.css.example.demo.enums.ESTADO_PROJETO_LEI;
import pt.ul.fc.css.example.demo.enums.ESTADO_VOTACAO;
import pt.ul.fc.css.example.demo.enums.VOTO_DELEGADO;

import pt.ul.fc.css.example.demo.handlers.ApresentarProjetoLeiHandler;
import pt.ul.fc.css.example.demo.handlers.FecharProjetosLeiExpiradosHandler;


import pt.ul.fc.css.example.demo.handlers.EscolherDelegadoHandler;
import pt.ul.fc.css.example.demo.handlers.FecharVotacaoHandler;

import pt.ul.fc.css.example.demo.repositories.CidadaoRepository;
import pt.ul.fc.css.example.demo.repositories.DelegadoRepository;
import pt.ul.fc.css.example.demo.repositories.ProjetoLeiRepository;
import pt.ul.fc.css.example.demo.repositories.TemaRepository;
import pt.ul.fc.css.example.demo.repositories.VotacoesRepository;


@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DemoApplicationTests {

	@Autowired VotacoesRepository votacoesRepository;
	@Autowired TemaRepository temasRepository;
	@Autowired DelegadoRepository delegadosRepository;
	@Autowired ProjetoLeiRepository projetoLeiRepository;
	@Autowired CidadaoRepository cidadaosRepository;
	@Autowired FecharProjetosLeiExpiradosHandler fecharProjetosLeiExpiradosHandler;
	@Autowired EscolherDelegadoHandler escolherDelegadoHandler;
	@Autowired ApresentarProjetoLeiHandler apresentarProjetoLeiHandler;
	@Autowired FecharVotacaoHandler fecharVotacaoHandler;

	@Autowired private RestApplicationController restApplicationController;



	//	@BeforeAll
	//	static void start() {
	//		/*
	//		 * ESTADO DA BD APÓS EXECUÇÃO DA MAIN
	// 				- 3 CIDADÃOS:
	//						- 1 DELEGADO(ID:1)
	//						- 2 CIDADAOS (ID:2 / ID:3)
	//
	// 				- HIERARQUIA DE TEMAS: 
	//						- SAÚDE(  Hospitais(Urgencias)  ,   Centros de Saúde  ),   Educação  , Economia
	//						- IDs -> Urgencias:1
	//		 					 	 Hospitais:2, 
	//		 					 	 Centro de Saúde:3, 
	//		 					 	 Saúde:4, 
	//		 					 	 Educação:5, 
	//		 					 	 Economia:6.
	//
	// 				- ATRIBUIÇÃO DE DELEGADOS PARA TEMA
	//						- CIDADAO 2 associado ao TEMASAÚDE pelo DELEGADO 1
	//						- CIDADAO 3 associado ao TEMAURGENCIAS pelo DELEGADO 1 
	//
	// 				- CRIAÇÃO PROJETOS LEI
	//						- PROJLEI1: ID1 -> TEMA_URGENCIAS -> DelegadoProponente: DELEGADO 1 - 10SEGUNDOS DE DURAÇÃO MAX
	//						- PROJLEI2: ID2 -> TEMA_SAÚDE -> DelegadoProponente: DELEGADO 1		- 10MINUTOS DE DURAÇÃO MAX
	//						- PROJLEI3: ID3 -> TEMA_SAÚDE -> DelegadoProponente: DELEGADO 1		- 10MINUTOS DE DURAÇÃO MAX
	//
	// 				- CRIAÇÃO VOTAÇÃO - AMBAS EM CURSO
	//						- VOTAÇÃO 1: ID1 -> associada a PROJLEI1 (TEMA_URGENCIAS)
	//						- VOTAÇÃO 2: ID2 -> associada a PROJLEI2 (TEMA_SAÚDE)
	//						- AMBAS com 10 segundos de validade -> apenas para efeito de teste é permitida a criação de votações com
	//						  menos de 15 dias de duração.
	//
	// 				- DEFINIÇÃO DE VOTO PRÉ DEFINIDO PARA DELEGADO
	//						- DELEGADO 1 só tem voto definido para A VOTAÇÃO 2 (TEMA_SAÚDE) -> FAVORÁVEL
	//		 */
	//
	//	}



	@Test
	@Order(1)
	void ListaVotacoesCursoGeralRestAPI() {

		//@GetMapping("/votacoesCurso")
		List<VotacaoDTO> l = restApplicationController.votacoesCurso();

		//Existem duas votações EM_CURSO
		assertNotNull(l);
		assertEquals(2, l.size());
		assertEquals(l.get(0).getEstado(), ESTADO_VOTACAO.EM_CURSO);

		//toString de modo a facilitar a visualização dos atributos relevantes.
		String expectedString = "VotacaoDTO{id=1, estado=EM_CURSO, votosFavoraveis=1, votosDesfavoraveis=0}";
		assertEquals(expectedString, l.get(0).toString());

		//Existem duas votações EM_CURSO -> != EXPIRADA
		assertNotEquals(l.get(1).getEstado(), ESTADO_VOTACAO.EXPIRADA);
	}

	@Test
	@Order(2)
	void ApresentarProjetoLeiHandlerGeral() {
		File anexoPDF = null;

		//ProjetoLei com Tema_Urgencias, DelegadoProponente: ID_1
		//Obter data maior que um ano para teste
		LocalDateTime agora = LocalDateTime.now();
		LocalDateTime maisDeUmAno = agora.plusYears(1).plusDays(1); // adiciona um dia extra

		apresentarProjetoLeiHandler.apresentarProjeto(
				"Titulo_Teste",
				"Descrição_Teste",
				anexoPDF,
				Timestamp.valueOf(maisDeUmAno),
				(SubTema) temasRepository.findById((long) 1).get(),
				(Delegado) delegadosRepository.findById((long) 1).get());

		//Sendo que existiam 3 ProjetosLei até ao momento, este será o 4º (ID_4)
		assertNotNull((ProjetoLei) projetoLeiRepository.findById((long) 4).get());

		String expectedString = "ProjetoLei [id=4, titulo=Titulo_Teste, descricao=Descrição_Teste, tema=Tema [id=1, titulo=Urgências], delegadoProponente= nome: João, estado=EM_APOIO, numeroApoiantes=1]";
		assertEquals(expectedString, ((ProjetoLei) projetoLeiRepository.findById((long) 4).get()).toString());

		//Data Limite ser um ano, apesar de ter sido passado um argumento > que 1 ano
		assertEquals(-1, ((ProjetoLei) projetoLeiRepository.findById((long) 4).get()).getDatahoraValidade().compareTo(Timestamp.valueOf(maisDeUmAno)));
	}


	@Test
	@Order(3)
	void FecharProjetosLeiExpiradosHandler() {
		try {

			//Verificar que está EM_APOIO antes dos 10segundos passarem
			assertEquals("EM_APOIO", projetoLeiRepository.findById((long)
					1).get().getEstado().toString());

			//Esperar que PROJLEI expire (10segundos)
			Thread.sleep(10000); 

			//Passados 10segundos
			fecharProjetosLeiExpiradosHandler.fecharProjetosLeiExpirados();
			assertEquals("EXPIRADO", projetoLeiRepository.findById((long)
					1).get().getEstado().toString());

		} catch (InterruptedException e) {
			//Devido a Thread.sleep(10000); 
		}
	}

	@Test
	@Order(4)
	void ConsultarProjetosLeiGeralRestAPI() {
		//Fechar ProjetosLei com data limite expirada
		fecharProjetosLeiExpiradosHandler.fecharProjetosLeiExpirados();

		//PROJLEI2: ID2 -> TEMA_SAÚDE -> DelegadoProponente: DELEGADO 1
		ProjetoLei p = (ProjetoLei) projetoLeiRepository.findById((long) 2).get();

		//ProjetosLeiExistentes não expirados
		List<ProjetoLei> allExpectedProjLei = projetoLeiRepository.findAllNotExpired(ESTADO_PROJETO_LEI.EXPIRADO);
		List<ProjetoLeiDTO> allExpectedProjLeiDTO = allExpectedProjLei.stream().map(ProjetoLei::toProjetoLeiDTO).collect(Collectors.toList());

		//@GetMapping("/consultarProjetos")
		List<ProjetoLeiDTO> actualProjLei = restApplicationController.consultarProjetosAll();

		//Comparar tamanho
		assertEquals(allExpectedProjLeiDTO.size(), actualProjLei.size());

		//Comparar toString de forma a ter visão geral de todos os atributos em causa
		assertEquals(allExpectedProjLeiDTO.toString(), actualProjLei.toString());


		//Listar todos os não expirados + detalhes do: PROJLEI2: ID2 -> TEMA_SAÚDE -> DelegadoProponente: DELEGADO 1
		//@GetMapping("/consultarProjetos/{proj_id}")
		String result = restApplicationController.consultarProjetos((int) p.getId());

		//Existem 4 ProjetosLei na BD, 
		//mas PROJLEI1: ID1 -> TEMA_URGENCIAS -> DelegadoProponente: DELEGADO 1 - 10SEGUNDOS DE DURAÇÃO MAX (EXPIRADO)
		String expectedString = "Projetos Lei existentes: Medidas para Saúde , ID: 2 | "
				+ "Medidas para Saúde2.0 , ID: 3 | "
				+ "Titulo_Teste , ID: 4 |  "
				+ "ESCOLHIDO: Medidas para Saúde - Details: Descrição relativa a medidas para Saúde";

		assertEquals(expectedString, result);
	}

	@Test
	@Order(5)
	void ConsultarProjetosLeiErroRestAPI() {
		//@GetMapping("/consultarProjetos/{proj_id}")
		//Não existe o projeto lei que está a tentar ser verificado > id = 10 (por exemplo);
		String result = restApplicationController.consultarProjetos(10);

		String expectedString = "Não existem projetos com o ID especificado";

		assertEquals(expectedString, result);

	}

	@Test
	@Order(6)
	void EscolherDelegadoHandlerGeral() {

		//CIDADAO_2
		Cidadao c = (Cidadao) cidadaosRepository.findById((long) 2).get();

		//DELEGADO_1
		Delegado d = (Delegado) delegadosRepository.findById((long) 1).get();

		//TEMA_1 -> Urgencias
		Tema t = (Tema) temasRepository.findById((long) 1).get();


		//CIDADAO_2 já tem associado o DELEGADO 1 ao TEMASAÚDE
		//Verificar tamanho da lista de delegados associados do CIDADAO_2
		assertEquals(1, cidadaosRepository.findById((long) 2).get().getListaDelegados().size());

		//DELEGAR o voto do CIDADAO_2 a DELEGADO_1 para o TEMA_URGENCIAS
		String s =	escolherDelegadoHandler.escolherDelegados(c, d, t);



		//Verificar que CIDADAO_2 está também associado ao DELEGADO_1 para o TEMA_URGENCIAS
		assertEquals(cidadaosRepository.findById((long) 2).get().getListaDelegados().get(1).getDelegado().getId(), d.getId());
		assertEquals(cidadaosRepository.findById((long) 2).get().getListaDelegados().get(1).getSubTema().getId(), t.getId());

		//Verificar tamanho da lista de delegados associados do CIDADAO_2
		assertEquals(2, cidadaosRepository.findById((long) 2).get().getListaDelegados().size());



		//Verificar repetição de tema, mas com outro delegado
		Cidadao cidadaoTeste = new Delegado();
		cidadaoTeste.setNome("Cidadao_Teste");
		cidadaoTeste.setNif(123456789);
		cidadaoTeste.setSenha("senha_Teste");

		Delegado delegadoTeste = (Delegado) cidadaoTeste;
		delegadosRepository.save(delegadoTeste);

		//Mesmo Cidadao (CIDADAO_2), mesmo Tema (TEMA_1 -> Urgencias), Delegado diferente
		escolherDelegadoHandler.escolherDelegados(c, delegadoTeste, t);

		//Verificar tamanho da lista de delegados associados do CIDADAO_2 (Deve manter-se o mesmo, 
		//mas com um novo delegado associado ao tema)
		assertEquals(2, cidadaosRepository.findById((long) 2).get().getListaDelegados().size());
		assertEquals(delegadoTeste.getId(), cidadaosRepository.findById((long) 2).get().getListaDelegados().get(1).getDelegado().getId());
		assertEquals(t.getId(), cidadaosRepository.findById((long) 2).get().getListaDelegados().get(1).getSubTema().getId());


		String expectedString = "\n"
				+ "ID: 1 | Nome: João\n"
				+ "	Projetos Lei envolvidos: \n"
				+ "		Titulo: Medidas para urgencias dos hospitais | Tema: Urgências\n"
				+ "		Titulo: Medidas para Saúde | Tema: Saúde\n"
				+ "		Titulo: Medidas para Saúde2.0 | Tema: Saúde\n"
				+ "		Titulo: Titulo_Teste | Tema: Urgências\n"
				+ "\n"
				+ "Delegado escolhido: 1\n"
				+ "Tema escolhido: Urgências\n"
				+ "\n"
				+ "Cidadao Paulo escolheu delegado João para o tema escolhido: Urgências\n";
		assertEquals(expectedString, s);
	}

	@Test
	@Order(7)
	void ApoiarProjetosLeiGeralRestAPI() {
		//PROJLEI2: ID2 -> TEMA_SAÚDE -> DelegadoProponente: DELEGADO 1 - 10 MIN DE DURAÇÃO MAX
		ProjetoLei projLei = projetoLeiRepository.findById((long) 2).get();

		//Numero de apoiantes inicial (Contém o voto do delegadoProponente)
		assertEquals(1, projLei.getNumeroApoiantes());

		// CIDADAO_2 apoia PROJLEI2: ID2 -> TEMA_SAÚDE
		Cidadao c1 = (Cidadao) cidadaosRepository.findById((long) 2).get();

		//@GetMapping("/apoiarProjetos/{cid_id}/{proj_id}")
		restApplicationController.apoiarProjetos((int) c1.getId(), (int) projLei.getId());


		//Verificar numero de apoiantes após apoio
		projLei = projetoLeiRepository.findById((long) 2).get();
		assertEquals(2, projLei.getNumeroApoiantes());

		// Mesmo cidadao (CIDADAO_2) tenta apoiar novamente
		//@GetMapping("/apoiarProjetos/{cid_id}/{proj_id}")
		restApplicationController.apoiarProjetos((int) c1.getId(), (int) projLei.getId());

		//Verificar numero de apoiantes após apoio repetido
		projLei = projetoLeiRepository.findById((long) 2).get();
		assertEquals(2, projLei.getNumeroApoiantes());

		//Construir cenário de forma a testar abertura de votação (10000 apoios)
		projLei.setNumeroApoiantes(9999);
		projetoLeiRepository.save(projLei);

		//Verificar votações até ao momento
		List<Votacao> listaVotacoes = votacoesRepository.findAll();
		assertEquals(2, listaVotacoes.size());


		//verificar que PROJLEI ainda está EM_APOIO
		assertEquals(ESTADO_PROJETO_LEI.EM_APOIO, projetoLeiRepository.findById((long) 2).get().getEstado());

		//Guardar TimeStamp de 15 dias para efeitos de teste (Imediatamente antes de aberta a votação)
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime quinzeDias = now.plusDays(15);

		// PROJLEI obtém 10000 apoios
		//CIDADAO3 APOIA
		//@GetMapping("/apoiarProjetos/{cid_id}/{proj_id}")
		Cidadao c2 = (Cidadao) cidadaosRepository.findById((long) 3).get();
		restApplicationController.apoiarProjetos((int) c2.getId(), (int) projLei.getId());

		//Verificar atributos relevantes
		projLei = projetoLeiRepository.findById((long) 2).get();

		//Estado
		assertEquals(ESTADO_PROJETO_LEI.APROVADO, projLei.getEstado());
		//Numero Apoiantes
		assertEquals(10000, projLei.getNumeroApoiantes());

		//Listar novas Votações - Existiam duas até aqui
		listaVotacoes = votacoesRepository.findAll();
		assertEquals(3, listaVotacoes.size());

		//Votação aberta associada ao PROJLEI2: ID2 -> TEMA_SAÚDE -> DelegadoProponente: DELEGADO 1	- 120 SEGUNDOS DE DURAÇÃO MAX
		//Testar data de expiração (min 15dias, max 2meses)

		//PROJLEI2: ID2 tem EXPIRAÇÃO de 120segundos, a votação abre com uma data de encerramento minima de 15dias
		assertEquals(1, votacoesRepository.findById((long) 3).get().getDataEncerramento().compareTo(Timestamp.valueOf(quinzeDias)));

		//Número de votantes da nova notação
		assertEquals(1, votacoesRepository.findById((long) 3).get().getListaPessoasQueVotaram().size());

		//Número de votos favoraveis
		assertEquals(1, votacoesRepository.findById((long) 3).get().getVotosFavoraveis());

		//Número de votos desfavoraveis
		assertEquals(0, votacoesRepository.findById((long) 3).get().getVotosDesfavoraveis());

		//Voto do Delegado que votou
		Delegado delegadoProponente = votacoesRepository.findById((long) 3).get().getProjetoLeiVotaçao().getDelegadoProponente();
		assertEquals(delegadoProponente.getId(), votacoesRepository.findById((long) 3).get().getVotos().get(0).getAssociado().getId());
		assertEquals(VOTO_DELEGADO.FAVORAVEL, votacoesRepository.findById((long) 3).get().getVotos().get(0).getVoto());

	}


	@Test
	@Order(8)
	void ApoiarProjetosLeiGeralErroRestAPI() {
		//@GetMapping("/apoiarProjetos/{cid_id}/{proj_id}")
		//Cidadao 2 a tentar apoiar um projeto lei que não existe
		String firstResult = restApplicationController.apoiarProjetos(2, 10);

		String expectedString = "Não existem projetos ou cidadaos com o ID especificado";

		assertEquals(expectedString, firstResult);


		//@GetMapping("/apoiarProjetos/{cid_id}/{proj_id}")
		//Apoiar o projeto lei existente 2 a partir de um cidadao inexistente
		String secondResult = restApplicationController.apoiarProjetos(10, 2);

		assertEquals(expectedString, secondResult);
	}


	@Test
	@Order(9)
	void FecharVotacaoHandlerGeral() {

		//Tema: urgencias (nivel mais baixo da hierarquia de temas)
		Votacao v = (Votacao) votacoesRepository.findById((long) 1).get();

		//Forçar a validade, apenas para teste
		v.setEstado(ESTADO_VOTACAO.EXPIRADA);

		//CIDADAO_1 é dono da votação (FAVORAVEL); 
		//CIDADAO_2 tem delegado associado para Urgencia, mas este não votou nesta Votação (relativa às Urgencias) (SEM VOTO)
		//CIDADAO_3 têm delegado o seu voto apenas no tema saude (é mais abrangente) para o Cidadao1 (FAVORAVEL)

		//Retorna true se Votação aprovada.
		assertEquals(true, fecharVotacaoHandler.fecharVotacao(v));
		assertEquals(ESTADO_VOTACAO.APROVADA, votacoesRepository.findById((long) 1).get().getEstado());

		//Quantos votos
		assertEquals(2, votacoesRepository.findById((long) 1).get().getListaPessoasQueVotaram().size());

		//Quem votou (CIDADAO_1 é trivial (delegadoProponente))
		Cidadao cid3 = cidadaosRepository.findById((long) 3).get();
		assertEquals(cid3.getId(), votacoesRepository.findById((long) 1).get().getListaPessoasQueVotaram().get(1).getId());

		//Votos favoraveis
		assertEquals(2, votacoesRepository.findById((long) 1).get().getVotosFavoraveis());

		//CIDADAO2 não votou, porque o seu delegado associado não tinha voto no tema
		//Delegado associado vai agora votar DESFAVORAVELMENTE no tema
		Delegado delegadoCriadoNoTesteAcima = delegadosRepository.findById((long) 4).get();
		Voto novoVoto = new Voto(delegadoCriadoNoTesteAcima, VOTO_DELEGADO.DESFAVORAVEL);

		//Como a votação está expirada nada irá acontecer a esta
		assertEquals(false, fecharVotacaoHandler.fecharVotacao(v));

		//Para efeitos de teste é reativada e colocado o novo voto do delegado_teste
		v.setEstado(ESTADO_VOTACAO.EM_CURSO);
		v.adicionarVoto(novoVoto);
		v.setEstado(ESTADO_VOTACAO.EXPIRADA);
		votacoesRepository.save(v);

		//Aprovada na mesma, mas com um voto DESFAVORAVEL agora
		assertEquals(true, fecharVotacaoHandler.fecharVotacao(v));

		//Quantos votos
		assertEquals(3, votacoesRepository.findById((long) 1).get().getListaPessoasQueVotaram().size());

		//Votos favoraveis
		assertEquals(2, votacoesRepository.findById((long) 1).get().getVotosFavoraveis());

		//Votos desfavoraveis
		assertEquals(1, votacoesRepository.findById((long) 1).get().getVotosDesfavoraveis());

		//Estado
		assertEquals(ESTADO_VOTACAO.APROVADA, votacoesRepository.findById((long) 1).get().getEstado());


	}

	@Test
	@Order(10)
	void VotarPropostaGeralRestAPI() {

		//VOTAÇÃO_2: ID2 -> associada a PROJLEI2 (TEMA_SAÚDE)
		Votacao v2 = (Votacao) votacoesRepository.findById((long) 2).get();

		//CIDADAO_2 -> com DELEGADO ASSOCIADO ao TEMA_SAÚDE
		Cidadao c2 = (Cidadao) cidadaosRepository.findById((long) 2).get();

		//Numero de pessoas que votaram originalmente 
		assertEquals(1, votacoesRepository.findById((long) 2).get().getListaPessoasQueVotaram().size());

		//Voto Favoravel do delegado proponente
		assertEquals(1, votacoesRepository.findById((long) 2).get().getVotosFavoraveis());

		//CIDADAO_2 NÃO VOTA na VOTACAO_2 - delega para o seu respetivo delegado
		//@GetMapping("/votarProposta/{votacao_id}/{cid_id}/{voto_delegado}")
		String result = restApplicationController.votarProposta((int) v2.getId(), (int) c2.getId(), null);

		//Numero de pessoas que passaram a votar 
		assertEquals(2, votacoesRepository.findById((long) 2).get().getListaPessoasQueVotaram().size());

		//Votos publicos mantém-se
		assertEquals(1, votacoesRepository.findById((long) 2).get().getVotos().size());

		//Votos Favoraveis
		assertEquals(2, votacoesRepository.findById((long) 2).get().getVotosFavoraveis());	


		//Verificar atributos relevantes, como VOTO_POR_OMISSÃO
		//Votacao com ID 1 já expirou
		String expectedString = "Votações:  "
				+ "Título: Medidas para Saúde ID: 2 |  "
				+ "Título: Medidas para Saúde ID: 3 | "
				+ "ESCOLHIDA: Título: Medidas para Saúde ID: 2\n"
				+ "Voto por omissão: FAVORAVEL";

		assertEquals(expectedString, result);


		//DELEGADO_TESTE (delegado criado no teste EscolherDelegadoHandlerGeral)
		Cidadao delegadoTeste = cidadaosRepository.findById((long) 4).get();

		//Verificar lista dos votos publicos
		assertEquals(1, votacoesRepository.findById((long) 2).get().getVotos().size());

		//@GetMapping("/votarProposta/{votacao_id}/{cid_id}/{voto_delegado}")
		String delegadoTesteVoto = restApplicationController.votarProposta((int) v2.getId(), (int) delegadoTeste.getId(), VOTO_DELEGADO.DESFAVORAVEL);

		//Verificar lista dos votos publicos
		assertEquals(2, votacoesRepository.findById((long) 2).get().getVotos().size());

		//Verificar o voto do DELEGADO_TESTE
		assertEquals(delegadoTeste.getId(), votacoesRepository.findById((long) 2).get().getVotos().get(1).getAssociado().getId());
		assertEquals(VOTO_DELEGADO.DESFAVORAVEL, votacoesRepository.findById((long) 2).get().getVotos().get(1).getVoto());

		//Mesmo Cidadao a tentar votar novamente
		restApplicationController.votarProposta((int) v2.getId(), (int) delegadoTeste.getId(), VOTO_DELEGADO.DESFAVORAVEL);

		//Não é aumentado o numero de votos
		//Votos publicos
		assertEquals(2, votacoesRepository.findById((long) 2).get().getVotos().size());

		//Votos totais
		assertEquals(3, votacoesRepository.findById((long) 2).get().getListaPessoasQueVotaram().size());

		//Verificar votoOmissão do DELEGADO_TESTE
		String expectedString2 = "Votações:  "
				+ "Título: Medidas para Saúde ID: 2 |  "
				+ "Título: Medidas para Saúde ID: 3 | "
				+ "ESCOLHIDA: Título: Medidas para Saúde ID: 2\n"
				+ "Sem voto por omissão.";

		assertEquals(expectedString2, delegadoTesteVoto);
	}

	@Test
	@Order(11)
	void VotarPropostaGeralErroRestAPI() {
		//@GetMapping("/votarProposta/{votacao_id}/{cid_id}/{voto_delegado}")
		//Cidadao 2 a tentar votar numa votacao inexistente
		String firstResult = restApplicationController.votarProposta(10, 2, null);

		String expectedString = "Não existem cidadaos ou votacoes com o ID especificado";

		assertEquals(firstResult, expectedString);

		//@GetMapping("/votarProposta/{votacao_id}/{cid_id}/{voto_delegado}")
		//Cidadao inexistente a tentar votar numa votacao existente
		String secondResult = restApplicationController.votarProposta(1, 10, null);

		assertEquals(secondResult, expectedString);
	}

}