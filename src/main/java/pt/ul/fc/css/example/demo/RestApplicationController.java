package pt.ul.fc.css.example.demo;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import pt.ul.fc.css.example.demo.enums.VOTO_DELEGADO;
import pt.ul.fc.css.example.demo.services.ApoiarProjetoLeiService;
import pt.ul.fc.css.example.demo.services.ConsultarProjetosLeiService;
import pt.ul.fc.css.example.demo.services.VotacoesCursoService;
import pt.ul.fc.css.example.demo.services.VotarPropostaService;
import pt.ul.fc.css.example.demo.dtos.VotacaoDTO;
import pt.ul.fc.css.example.demo.dtos.CidadaoDTO;
import pt.ul.fc.css.example.demo.dtos.ProjetoLeiDTO;

@RestController
@RequestMapping("/api")
public class RestApplicationController {
    
	@Autowired private VotacoesCursoService votacoesCursoService;
	@Autowired private ConsultarProjetosLeiService consultarProjetosLeiService;
	@Autowired private ApoiarProjetoLeiService apoiarProjetoLeiService;
	@Autowired private VotarPropostaService votarPropostaService;
    
	@GetMapping("/cidadao/{cid_id}")
	Optional<CidadaoDTO> getCidadao(@PathVariable long cid_id) {
		return apoiarProjetoLeiService.getCidadaoByID(cid_id);
	}
	
	@GetMapping("/votacoesCurso")
	List<VotacaoDTO> votacoesCurso() {
		return votacoesCursoService.getListagemVotacoesEmCursoHandler();
	}
	
	@GetMapping("/consultarProjetos")
	List<ProjetoLeiDTO> consultarProjetosAll() {
		return consultarProjetosLeiService.getListagemProjetosLeiNaoExpirados();
	}
	
	@GetMapping("/consultarProjetos/{proj_id}")
	String consultarProjetos(@PathVariable int proj_id) {
		return consultarProjetosLeiService.getProjetosLeiNaoExpiradosHandler(proj_id);
	}
	
	@GetMapping("/apoiarProjetos/{cid_id}/{proj_id}")
	String apoiarProjetos(@PathVariable int cid_id, @PathVariable int proj_id) {
		return apoiarProjetoLeiService.getApoiarProjetosHandler(cid_id, proj_id);
	}
	
	@GetMapping("/votarProposta/{votacao_id}/{cid_id}/{voto_delegado}")
	String votarProposta(@PathVariable int votacao_id, @PathVariable int cid_id, @PathVariable VOTO_DELEGADO voto_delegado) {
		return votarPropostaService.votarPropostaHandler(votacao_id, cid_id, voto_delegado);
	}

    @RequestMapping(value = "/index")
    public ModelAndView home() {
        return new ModelAndView("index");
    }
}
