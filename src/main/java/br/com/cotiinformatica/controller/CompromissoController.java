package br.com.cotiinformatica.controller;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import br.com.cotiinformatica.dto.CadastroCompromissoDTO;
import br.com.cotiinformatica.dto.ConsultaCompromissoDTO;
import br.com.cotiinformatica.dto.EdicaoCompromissoDTO;
import br.com.cotiinformatica.dto.RelatorioCompromissoDTO;
import br.com.cotiinformatica.entities.Compromisso;
import br.com.cotiinformatica.entities.Usuario;
import br.com.cotiinformatica.enums.FormatoRelatorio;
import br.com.cotiinformatica.enums.PrioridadeCompromisso;
import br.com.cotiinformatica.enums.TipoCompromisso;
import br.com.cotiinformatica.reports.CompromissoReportPDF;
import br.com.cotiinformatica.services.CompromissoService;
import br.com.cotiinformatica.services.UsuarioService;
import br.com.cotiinformatica.utils.DateUtil;

@Controller
public class CompromissoController {

	/*
	 * Inje��o de depend�ncia Inicializa��o automatica feita pelo spring
	 */

	@Autowired
	private CompromissoService compromissoService;

	@Autowired
	private UsuarioService usuarioService;

	@RequestMapping("/cadastro-compromisso")
	public ModelAndView cadastroCompromisso() {
		ModelAndView modelAndView = new ModelAndView("agenda/cadastro-compromisso");

		// criando um objeto da classe funcionario..
		modelAndView.addObject("compromisso", new CadastroCompromissoDTO());

		// enviando os dados dos ENUMs
		modelAndView.addObject("tipos", TipoCompromisso.values());
		modelAndView.addObject("prioridades", PrioridadeCompromisso.values());

		return modelAndView;
	}

	@RequestMapping(value = "cadastrarCompromisso", method = RequestMethod.POST)
	public ModelAndView cadastrarCompromisso(CadastroCompromissoDTO dto, HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("agenda/cadastro-compromisso");

		try {

			// obter o usuario autenticado na sess�o..
			Usuario auth = (Usuario) request.getSession().getAttribute("usuario_autenticado");
			Usuario usuario = usuarioService.findById(auth.getIdUsuario());

			Compromisso compromisso = new Compromisso();

			compromisso.setNome(dto.getNome());
			compromisso.setDataCompromisso(DateUtil.convertDate(dto.getDataCompromisso()));
			compromisso.setHoraCompromisso(DateUtil.convertTime(dto.getHoraCompromisso()));
			compromisso.setDescricao(dto.getDescricao());
			compromisso.setPrioridade(dto.getPrioridade());
			compromisso.setTipo(dto.getTipo());
			compromisso.setUsuario(usuario); // relacionando o usuario do compromisso..

			// gravar no banco de dados
			compromissoService.createOrUpdate(compromisso);

			modelAndView.addObject("mensagem_sucesso", "Compromisso cadastrado com sucesso");
		} catch (Exception e) {
			modelAndView.addObject("mensagem_erro", e.getMessage());
		}

		// criando um objeto da classe funcionario..
		modelAndView.addObject("compromisso", new CadastroCompromissoDTO());

		// enviando os dados dos ENUMs
		modelAndView.addObject("tipos", TipoCompromisso.values());
		modelAndView.addObject("prioridades", PrioridadeCompromisso.values());

		return modelAndView;
	}

	@RequestMapping("/consulta-compromisso")
	public ModelAndView consultaCompromisso() {
		ModelAndView modelAndView = new ModelAndView("agenda/consulta-compromisso");

		// enviando um objeto da classe ConsultaCompromissoDTO
		modelAndView.addObject("consulta", new ConsultaCompromissoDTO());

		return modelAndView;
	}

	@RequestMapping(value = "consultarCompromissos", method = RequestMethod.POST)
	public ModelAndView consultarCompromissos(ConsultaCompromissoDTO dto, HttpServletRequest request) {

		ModelAndView modelAndView = new ModelAndView("agenda/consulta-compromisso");

		try {

			Date dataInicio = DateUtil.convertDate(dto.getDataInicio());
			Date dataFim = DateUtil.convertDate(dto.getDataFim());
			Usuario usuario = (Usuario) request.getSession().getAttribute("usuario_autenticado");

			// realizando a consulta e armazenar o seu resultado no modelAndView
			modelAndView.addObject("listagem_compromissos",
					compromissoService.find(usuario.getIdUsuario(), dataInicio, dataFim));
		} catch (Exception e) {
			modelAndView.addObject("mensagem_erro", e.getMessage());
		}

		modelAndView.addObject("consulta", dto);
		return modelAndView;
	}

	@RequestMapping("/exclusao-compromisso")
	public ModelAndView exclusaoCompromisso(Integer id, HttpServletRequest request) {

		ModelAndView modelAndView = new ModelAndView("agenda/consulta-compromisso");

		try {

			// obter o usuario autenticado na aplica��o
			Usuario usuario = (Usuario) request.getSession().getAttribute("usuario_autenticado");

			// buscar o compromisso no banco de dados (atraves do id)..
			Compromisso compromisso = compromissoService.findById(id);

			// verificando se o compromisso foi encontrado e se � um compromisso do usu�rio
			// autenticado
			if (compromisso != null && compromisso.getUsuario().equals(usuario)) {

				// excluindo o compromisso
				compromissoService.delete(compromisso);

				modelAndView.addObject("mensagem_sucesso", "Compromisso exclu�do com sucesso.");
			} else {
				throw new Exception("Compromisso inv�lido para exclus�o.");
			}
		} catch (Exception e) {
			modelAndView.addObject("mensagem_erro", e.getMessage());
		}

		modelAndView.addObject("consulta", new ConsultaCompromissoDTO());
		return modelAndView;
	}

	@RequestMapping("/edicao-compromisso")
	public ModelAndView edicaoCompromisso(Integer id, HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("agenda/edicao-compromisso");

		try {

			// resgatar o usuario autenticado..
			Usuario usuario = (Usuario) request.getSession().getAttribute("usuario_autenticado");

			// consultar o compromisso atraves do id..
			Compromisso compromisso = compromissoService.findById(id);

			// verificar se o compromisso foi encontrado e se pertence ao usuario que esta
			// autenticado
			if (compromisso != null && compromisso.getUsuario().equals(usuario)) {

				// exibir na p�gina de edi��o os enums..
				modelAndView.addObject("tipos", TipoCompromisso.values());
				modelAndView.addObject("prioridades", PrioridadeCompromisso.values());

				// carregando todos os dados do compromisso no DTO..
				EdicaoCompromissoDTO dto = new EdicaoCompromissoDTO();

				dto.setIdCompromisso(compromisso.getIdCompromisso());
				dto.setNome(compromisso.getNome());
				dto.setDataCompromisso(new SimpleDateFormat("yyyy-MM-dd").format(compromisso.getDataCompromisso()));
				dto.setHoraCompromisso(new SimpleDateFormat("HH:mm").format(compromisso.getHoraCompromisso()));
				dto.setDescricao(compromisso.getDescricao());
				dto.setTipo(compromisso.getTipo());
				dto.setPrioridade(compromisso.getPrioridade());

				modelAndView.addObject("compromisso", dto);
			} else {
				throw new Exception("Compromisso inv�lido para edi��o.");
			}
		} catch (Exception e) {
			modelAndView.addObject("mensagem_erro", e.getMessage());
		}

		return modelAndView;
	}

	@RequestMapping(value = "atualizarCompromisso", method = RequestMethod.POST)
	public ModelAndView atualizarCompromisso(EdicaoCompromissoDTO dto, HttpServletRequest request) {

		ModelAndView modelAndView = new ModelAndView("agenda/edicao-compromisso");

		try {

			// transferir os dados do dto para um objeto compromisso..
			Compromisso compromisso = compromissoService.findById(dto.getIdCompromisso());

			compromisso.setNome(dto.getNome());
			compromisso.setDataCompromisso(DateUtil.convertDate(dto.getDataCompromisso()));
			compromisso.setHoraCompromisso(DateUtil.convertTime(dto.getHoraCompromisso()));
			compromisso.setDescricao(dto.getDescricao());
			compromisso.setPrioridade(dto.getPrioridade());
			compromisso.setTipo(dto.getTipo());

			// atualizando o compromisso..
			compromissoService.createOrUpdate(compromisso);

			modelAndView.addObject("mensagem_sucesso", "Compromisso atualizado com sucesso.");
		} catch (Exception e) {
			modelAndView.addObject("mensagem_erro", e.getMessage());
		}

		modelAndView.addObject("compromisso", dto);
		return modelAndView;
	}

	@RequestMapping("/relatorio-compromisso")
	public ModelAndView relatorioCompromisso() {
		ModelAndView modelAndView = new ModelAndView("agenda/relatorio-compromisso");
		// enviando os objetos que ser�o utilizados
		// pelo formul�rio da p�gina
		modelAndView.addObject("compromisso", new RelatorioCompromissoDTO());
		modelAndView.addObject("formatos", FormatoRelatorio.values());
		return modelAndView;
	}

	@RequestMapping(value = "gerarRelatorio",
			method = RequestMethod.POST)
			public ModelAndView gerarRelatorio(RelatorioCompromissoDTO dto,
			HttpServletRequest request, HttpServletResponse response) {
			ModelAndView modelAndView = new ModelAndView
			("agenda/relatorio-compromisso");
			try {
			//convertendo as datas recebidas..
			Date dataInicio = DateUtil
			.convertDate(dto.getDataInicio());
			Date dataFim = DateUtil
			.convertDate(dto.getDataFim());
			//obtendo os dados do usuario autenticado..
			Usuario usuario = (Usuario) request.getSession()
			.getAttribute("usuario_autenticado");
			//verificando se o tipo do relatorio
			//selecionado � PDF..
			if(dto.getFormato().equals(FormatoRelatorio.PDF)) {
			//consultando os compromissos no banco de dados atraves das datas informadas..
			List<Compromisso> compromissos = compromissoService.find(usuario.getIdUsuario(), dataInicio, dataFim);
			//gerar o arquivo do relatorio..
			CompromissoReportPDF report
			= new CompromissoReportPDF();
			ByteArrayInputStream stream = report.gerarRelatorio(compromissos, dataInicio, dataFim, usuario);
			//download do relat�rio
			response.setContentType
			("application/pdf"); //tipo do arquivo..
			response.addHeader("Content-Disposition",
			"attachment; filename=relatorio_compromissos.pdf");
			byte[] dados = stream.readAllBytes();
			OutputStream out = response.getOutputStream();
			out.write(dados, 0, dados.length);
			out.flush();
			out.close();
			response.getOutputStream()
			.flush(); //encerrando..
			return null;
			}
			}
			catch(Exception e) {
			modelAndView.addObject
			("mensagem_erro", e.getMessage());
			}
			modelAndView.addObject("compromisso",
			new RelatorioCompromissoDTO());
			modelAndView.addObject("formatos",
			FormatoRelatorio.values());
			return modelAndView;
			}

}
