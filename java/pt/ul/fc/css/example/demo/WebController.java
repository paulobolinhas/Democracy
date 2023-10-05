package pt.ul.fc.css.example.demo;

import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import pt.ul.fc.css.example.demo.dtos.CidadaoDTO;
import pt.ul.fc.css.example.demo.dtos.DelegadoDTO;
import pt.ul.fc.css.example.demo.dtos.ProjetoLeiDTO;
import pt.ul.fc.css.example.demo.dtos.SubTemaDTO;
import pt.ul.fc.css.example.demo.dtos.VotacaoDTO;
import pt.ul.fc.css.example.demo.entities.ProjetoLei;
import pt.ul.fc.css.example.demo.enums.VOTO_DELEGADO;
import pt.ul.fc.css.example.demo.services.ApoiarProjetoLeiService;
import pt.ul.fc.css.example.demo.services.ApresentarProjetoLeiService;
import pt.ul.fc.css.example.demo.services.ConsultarProjetosLeiService;
import pt.ul.fc.css.example.demo.services.EscolherDelegadoService;
import pt.ul.fc.css.example.demo.services.VotacoesCursoService;
import pt.ul.fc.css.example.demo.services.VotarPropostaService;

@Controller
public class WebController {

	Logger logger = LoggerFactory.getLogger(WebController.class);

	@Autowired
	private VotacoesCursoService votacoesCursoService;

	@Autowired private ApresentarProjetoLeiService apresentarProjetoLeiService;

	@Autowired private ConsultarProjetosLeiService consultarProjetosLeiService;

	@Autowired private ApoiarProjetoLeiService apoiarProjetoLeiService;

	@Autowired private EscolherDelegadoService escolherDelegadoService;

	@Autowired private VotarPropostaService votarPropostaService;


	public WebController() {
		super();
	}
	
	// Function to check if the user is logged in
	private boolean isLoggedIn(HttpSession session) {
	    return session.getAttribute("userId") != null;
	}

	@RequestMapping("/")
	public String getIndex(Model model) {
		return "login";
	}

	@GetMapping("/login")
	public String login(final Model model) {
		return "login";
	}

	@PostMapping("/login")
	public String loginAction(@RequestParam("userId") String userId, HttpSession session) {
		
		//Verificar user
		long userIdLong = Long.parseLong(userId);
		Optional<CidadaoDTO> cid = apoiarProjetoLeiService.getCidadaoByID(userIdLong);
		if(cid.isPresent()) {
			session.setAttribute("userId", userId);
			return "redirect:/votacoesCurso";
		} else {
			return "errorLogin";
		}
	}


	@GetMapping({ "/votacoesCurso" })
	public String index(final Model model, HttpSession session) {
		if (isLoggedIn(session)) {
			model.addAttribute("votacoesCurso", votacoesCursoService.getListagemVotacoesEmCursoHandler());
			return "votacoesCurso_list";
		} else {
			return "login";
		}
		
	}

	@GetMapping("/votacoesCurso/{id}")
	public String detalhesVotacao(@PathVariable Long id, final Model model, HttpSession session) {
		if (isLoggedIn(session)) {
			String userId = (String) session.getAttribute("userId");
			long userIdLong = Long.parseLong(userId);
			CidadaoDTO cid = apoiarProjetoLeiService.getCidadaoByID(userIdLong).get();

			Optional<VotacaoDTO> v = votarPropostaService.getVotacaoByID(id);
			
			if (v.isPresent()) {
				model.addAttribute("votacao", v.get());

				String votoOmissao = votarPropostaService.getVotoOmissao(cid, v.get());
				System.out.println("\n-------------------------\n" + votoOmissao + "\n ----------------\n");
				model.addAttribute("votoOmissao", votoOmissao);
				
				return "votacao_detail";
			} else {
				return "error404";
			}
		} else {
			return "login";
		}
		
	}

	@PostMapping("/votacoesCurso/{id}/votar")
	public String votarProposta(@PathVariable Long id, @RequestParam("propostaId") Long propostaId, @RequestParam("tipoVoto") String tipoVoto, HttpSession session) {

		VotacaoDTO votacao = votarPropostaService.getVotacaoByID(id).get();

		String userId = (String) session.getAttribute("userId");
		long userIdLong = Long.parseLong(userId);
		CidadaoDTO cid = apoiarProjetoLeiService.getCidadaoByID(userIdLong).get();

		VOTO_DELEGADO votoCidadao = tipoVoto.equals("favoravel") ? VOTO_DELEGADO.FAVORAVEL : VOTO_DELEGADO.DESFAVORAVEL;

		votarPropostaService.votarProposta(votacao, cid, votoCidadao);

		return "redirect:/votacoesCurso/{id}";
	}

	@GetMapping("/projetoLei/new")
	public String newProjetoLei(final Model model, HttpSession session) {
		if (isLoggedIn(session)) {
			model.addAttribute("projLei", new ProjetoLei());
			model.addAttribute("temas", apresentarProjetoLeiService.getTemas());
			return "projetoLei_add";
		} else {
			return "login";
		}
		
	}

	@PostMapping("/projetoLei/new")
	public String newProjetoLeiAction(final Model model, HttpSession session, @ModelAttribute ProjetoLeiDTO pl, @RequestParam("date") String date, @RequestParam("subtema") String subtema) {
		ProjetoLeiDTO pl2;
		String userId = (String) session.getAttribute("userId");
		long userIdLong = Long.parseLong(userId);
	
			Timestamp datahoraValidade = convertStringToDate(date);
			SubTemaDTO subtemaProj = apresentarProjetoLeiService.getSubTemaByName(subtema).get();
			
			Optional<DelegadoDTO> delegadoProponente = apresentarProjetoLeiService.getDelegadoByID(userIdLong);
			if(delegadoProponente.isPresent()) {
				pl2 = apresentarProjetoLeiService.getProjetoLeiApresentado(pl.getTitulo(), pl.getDescricao(), null, datahoraValidade, subtemaProj, delegadoProponente.get()).toProjetoLeiDTO();
				logger.debug("ProjetoLei added to the database.");
				return "redirect:/projetosLei/" + pl2.getId();
			} else {
				 model.addAttribute("projLei", new ProjetoLeiDTO());
				 model.addAttribute("temas", apresentarProjetoLeiService.getTemas());
				 return "errorNaoDelegado";
			}
	}

	@GetMapping({ "/projetosLeiNaoExpirados" })
	public String consultarProjetosLeiNaoExpirados(final Model model, HttpSession session) {
		if (isLoggedIn(session)) {
			model.addAttribute("projetosLeiNaoExpirados", consultarProjetosLeiService.getListagemProjetosLeiNaoExpirados());
			return "projetosLeiNaoExpirados_list";
		} else {
			return "login";
		}
		
	}

	@GetMapping("/projetosLei/{id}")
	public String checkProjetoLei(final Model model, @PathVariable Long id, HttpSession session) {
		if (isLoggedIn(session)) { 
			Optional<ProjetoLeiDTO> pl = consultarProjetosLeiService.getProjetoLeiByID(id);
			if (pl.isPresent()) {
				model.addAttribute("projLei", pl.get());
				return "projetoLei_detail";
			} else {
				return "error404";
			}
		} else {
			return "login";
		}
		
	}

	@PostMapping("/projetosLei/{id}/apoio")
	public String apoiarProjetoLei(@PathVariable Long id, HttpSession session) {
		String userId = (String) session.getAttribute("userId");
		long userIdLong = Long.parseLong(userId);

		CidadaoDTO cid = apoiarProjetoLeiService.getCidadaoByID(userIdLong).get();
		ProjetoLeiDTO projLei = apoiarProjetoLeiService.getProjLeiByID(id).get();
		apoiarProjetoLeiService.apoiarProjetos(cid, projLei);
		System.out.println("\n-------------------------\n" + "redirect:/projetosLei/" + id + "\n ----------------\n");

		return "redirect:/projetosLei/" + id;
	}

	@GetMapping("/escolherDelegados")
	public String escolherDelegados(final Model model, HttpSession session) {
		if (isLoggedIn(session)) { 
			model.addAttribute("delegados", escolherDelegadoService.getDelegados());
			model.addAttribute("temas", apresentarProjetoLeiService.getTemas());
			return "escolher_delegados";
		} else {
			return "login";
		}
		
	}

	@PostMapping("/escolherDelegados")
	public String escolherDelegadosAction(@RequestParam("tema") String tema, @RequestParam("delegado") Long delegadoId, HttpSession session) {
		String userId = (String) session.getAttribute("userId");
		long userIdLong = Long.parseLong(userId);
		CidadaoDTO cid = apoiarProjetoLeiService.getCidadaoByID(userIdLong).get();
		SubTemaDTO subtema = apresentarProjetoLeiService.getSubTemaByName(tema).get();
		DelegadoDTO delegado = apresentarProjetoLeiService.getDelegadoByID(delegadoId).get();

		escolherDelegadoService.escolherDelegados(cid, delegado, subtema);

		return "redirect:/escolherDelegados";
	}

	private Timestamp convertStringToDate(String date) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
		try {
			Date parsedDate = dateFormat.parse(date);
			Timestamp timestamp = new Timestamp(parsedDate.getTime());
			return timestamp;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
}
