package pt.ul.fc.css.example.demo;

import jakarta.persistence.EntityManagerFactory;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import pt.ul.fc.css.example.demo.entities.Author;
import pt.ul.fc.css.example.demo.entities.Cidadao;
import pt.ul.fc.css.example.demo.entities.Delegado;
import pt.ul.fc.css.example.demo.entities.DelegadoParaTema;
import pt.ul.fc.css.example.demo.entities.ProjetoLei;
import pt.ul.fc.css.example.demo.entities.Voto;
import pt.ul.fc.css.example.demo.entities.SubTema;
import pt.ul.fc.css.example.demo.entities.Tema;
import pt.ul.fc.css.example.demo.entities.Votacao;
import pt.ul.fc.css.example.demo.enums.VOTO_DELEGADO;
import pt.ul.fc.css.example.demo.repositories.AuthorRepository;
import pt.ul.fc.css.example.demo.repositories.CidadaoRepository;
import pt.ul.fc.css.example.demo.repositories.DelegadoRepository;
import pt.ul.fc.css.example.demo.repositories.ProjetoLeiRepository;
import pt.ul.fc.css.example.demo.repositories.TemaRepository;
import pt.ul.fc.css.example.demo.repositories.VotacoesRepository;

@SpringBootApplication
@EnableScheduling
public class DemoApplication {

	private static final Logger log = LoggerFactory.getLogger(DemoApplication.class);

	@Autowired EntityManagerFactory factory;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	
//	@Bean
//    public CommandLineRunner demo(AuthorRepository repository) {
//        return (args) -> {
//            // save a few customers
//            repository.save(new Author("Jack", "Bauer"));
//            repository.save(new Author("Chloe", "O'Brian"));
//            repository.save(new Author("Kim", "Bauer"));
//            repository.save(new Author("David", "Palmer"));
//            repository.save(new Author("Michelle", "Dessler"));
//
//            // fetch all customers
//            log.info("Customers found with findAll():");
//            log.info("-------------------------------");
//            for (Author author : repository.findAll()) {
//                log.info(author.toString());
//            }
//            log.info("");
//
//            // fetch an individual customer by ID
//            repository.findById(1L).ifPresent((Author author) -> {
//                log.info("Customer found with findById(1L):");
//                log.info("--------------------------------");
//                log.info(author.toString());
//                log.info("");
//            });
//
//            // fetch customers by last name
//            log.info("Author found with findByName('Bauer'):");
//            log.info("--------------------------------------------");
//            repository.findByName("Bauer").forEach(bauer -> {
//                log.info(bauer.toString());
//            });
//            // for (Customer bauer : repository.findByLastName("Bauer")) {
//            // log.info(bauer.toString());
//            // }
//            log.info("");
//        };
//    }

	@Bean
	public CommandLineRunner demo(
			VotacoesRepository votacoesRepository,
			ProjetoLeiRepository projetoLeiRepository,
			DelegadoRepository delegadoRepository,
			TemaRepository temaRepository,
			CidadaoRepository cidadaoRepository) {
		return (args) -> {
			
			//----CIDADAOS-------------------------------------
			//1Delegado(id:1) + 2Cidadao(id:2 / id:3)
			Cidadao cidadao = new Delegado();
			cidadao.setNome("João");
			cidadao.setNif(123456789);
			cidadao.setSenha("senha123");

			Cidadao cidadao2 = new Cidadao("Paulo", 123456789, "password");
			Cidadao cidadao3 = new Cidadao("Manuel", 123456789, "password");

			Delegado delegado = (Delegado) cidadao;

			delegadoRepository.save(delegado);
			cidadaoRepository.save(cidadao2);
			cidadaoRepository.save(cidadao3);
			//----------------------------------------------
			
			//----TEMAS-------------------------------------
			//Hierarquia de temas: SAÚDE(Hospitais(Urgencias), Centros de Saúde), Educação, Economia
			//IDs -> Urgencias:1, Hospitais:2, Centro de Saúde:3, Saúde:4, Educação:5, Economia:6
			
			SubTema urgencias = new SubTema("Urgências");
			SubTema hospitais = new SubTema("Hospitais");
			
			SubTema centroSaude = new SubTema("Centros Saúde");
			
			Tema temaSaude = new SubTema("Saúde");
			SubTema subTemaSaude = (SubTema) temaSaude;

			
			Tema temaEducacao = new SubTema("Educação");
			SubTema subTemaEducacao = (SubTema) temaEducacao;

			
			Tema temaEconomia = new SubTema("Economia");
			SubTema subTemaEconomia = (SubTema) temaEconomia;
			
			temaRepository.save(urgencias);
			temaRepository.save(hospitais);
			temaRepository.save(centroSaude);
			temaRepository.save(subTemaSaude);
			temaRepository.save(subTemaEducacao);
			temaRepository.save(subTemaEconomia);
			
			urgencias.setTemaPai(hospitais);
			hospitais.setTemaPai(subTemaSaude);
			centroSaude.setTemaPai(subTemaSaude);
			
			hospitais.addListaSubTemas(urgencias);
			subTemaSaude.addListaSubTemas(hospitais);
			subTemaSaude.addListaSubTemas(centroSaude);
			
			temaRepository.save(urgencias);
			temaRepository.save(hospitais);
			temaRepository.save(centroSaude);
			temaRepository.save(subTemaSaude);
			//----------------------------------------------

			//----ATRIBUIÇÃO DE DELEGADOS PARA TEMA-------------------------------------
			//Cidadao 2 associado ao temaSaúde pelo Delegado 1
			//Cidadao 3 associado ao temaUrgencias pelo Delegado 1
			
			DelegadoParaTema dpt = new DelegadoParaTema(delegado, temaSaude);
			DelegadoParaTema dpt2 = new DelegadoParaTema(delegado, urgencias);
			cidadao2.setDelegadoTema(dpt);
			cidadao3.setDelegadoTema(dpt2);

			cidadaoRepository.save(cidadao2);
			cidadaoRepository.save(cidadao3);
			//----------------------------------------------

			//----CRIAÇÃO PROJETOS LEI-------------------------------------
			//ProjLei1: ID1 -> Urgencias -> DelegadoProponente:1
			//ProjLei2: ID2 -> Saúde -> DelegadoProponente:1
			//ProjLei2: ID3 -> Saúde -> DelegadoProponente:1

			
			File anexoPDF = null;
			
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			Timestamp timestamp2 = new Timestamp(System.currentTimeMillis() + 600000); //10segundos
			Timestamp timestamp3 = new Timestamp(System.currentTimeMillis() + 120000); // 120segundos

			ProjetoLei projLei =
					new ProjetoLei("Medidas para urgencias dos hospitais", "Descrição relativa a urgencias dos hospitais", anexoPDF, timestamp2, urgencias, delegado);

			ProjetoLei projLei2 =
					new ProjetoLei("Medidas para Saúde", "Descrição relativa a medidas para Saúde", anexoPDF, timestamp3, subTemaSaude, delegado);

			ProjetoLei projLei3 =
					new ProjetoLei("Medidas para Saúde2.0", "Descrição relativa a medidas para Saúde 2.0", anexoPDF, timestamp3, subTemaSaude, delegado);

			projetoLeiRepository.save(projLei);

			projetoLeiRepository.save(projLei2);

			projetoLeiRepository.save(projLei3);

			List<ProjetoLei> listaProjetosLeiEnvolvidos = new ArrayList<ProjetoLei>();
			listaProjetosLeiEnvolvidos.add(projLei2);
			delegado.setListaProjetosLeiEnvolvidos(listaProjetosLeiEnvolvidos);
			delegado.getListaProjetosLeiEnvolvidos().add(projLei);

			delegadoRepository.save(delegado);
			//----------------------------------------------
			
			//----CRIAÇÃO VOTAÇÃO-------------------------------------
			//Votacao v: ID1 -> associada a ProjetoLei1(Urgencias)
			//Votacao v2: ID2 -> associada a ProjetoLei2(Saude)
			//AMBAS com 10 segundos de validade -> apenas para efeito de teste é permitida a criação de votações com
			//menos de 15 dias de duração.
			
			//Delegado1 só tem voto definido para a votação2 (Tema_Saúde) -> FAVORÁVEL
			
			Votacao v = new Votacao(timestamp, timestamp2, projLei);
			Votacao v2 = new Votacao(timestamp, timestamp2, projLei2);

			votacoesRepository.save(v);
			votacoesRepository.save(v2);

			Voto voto = new Voto(delegado, VOTO_DELEGADO.FAVORAVEL);

			v2.adicionarVoto(voto);

			votacoesRepository.save(v);
			votacoesRepository.save(v2);


		};
	}
}
