package br.com.cotiinformatica.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import br.com.cotiinformatica.dto.LoginDTO;
import br.com.cotiinformatica.dto.RegisterDTO;
import br.com.cotiinformatica.entities.Usuario;
import br.com.cotiinformatica.services.UsuarioService;

@Controller
public class HomeController {

	//inje��o de depend�ncia da classe UsuarioService
	@Autowired
	private UsuarioService usuarioService;
	
	@RequestMapping("/") // raiz do projeto
	public ModelAndView login(HttpServletRequest request) {

		// definindo a p�gina que ser� acessada na URL
		ModelAndView modelAndView = new ModelAndView();
		
		//verificando se o usuario ja esta autenticado
		if(request.getSession().getAttribute("usuario_autenticado") != null) {
			modelAndView.setViewName("redirect:home"); //redirecionamento
		}
		else {
			modelAndView.setViewName("login");
			modelAndView.addObject("login", new LoginDTO());
		}	

		return modelAndView;
	}

	@RequestMapping(value = "loginUser", method = RequestMethod.POST)
	public ModelAndView loginUser(LoginDTO dto, ModelMap map, HttpServletRequest request) {

		ModelAndView modelAndView = new ModelAndView();
		
		try {
			
			//buscando o usu�rio no banco de dados atraves do email e da senha
			Usuario usuario = usuarioService.findByEmailAndSenha(dto.getEmail(), dto.getSenha());
			
			if(usuario != null) { //verificando se o usu�rio foi encontrado
				
				//gravando os dados do usu�rio em sess�o..
				request.getSession().setAttribute("usuario_autenticado", usuario);
				
				//redirecionando para a ROTA: /home
				modelAndView.setViewName("redirect:home");
			}
			else {
				throw new Exception("Acesso Negado. Usu�rio inv�lido.");
			}
		}
		catch(Exception e) {
			modelAndView.setViewName("login"); //nome da p�gina..
			
			modelAndView.addObject("login", new LoginDTO());
			map.addAttribute("mensagem_erro", e.getMessage());
		}

		return modelAndView;
	}

	@RequestMapping("/register") // p�gina de cadastro de usu�rio
	public ModelAndView register() {

		// definindo a p�gina que ser� acessada na URL
		ModelAndView modelAndView = new ModelAndView("register");
		modelAndView.addObject("register", new RegisterDTO());

		return modelAndView;
	}

	@RequestMapping(value = "registerUser", method = RequestMethod.POST)
	public ModelAndView registerUser(RegisterDTO dto, ModelMap map) {

		ModelAndView modelAndView = new ModelAndView("register");
		
		try {
			
			//verificar se as senhas n�o est�o iguais..
			if(!dto.getSenha().equals(dto.getSenhaConfirmacao())) {
				throw new Exception("Senhas n�o conferem.");
			}
			
			//verificar se o email j� esta cadastrado no banco de dados
			if(usuarioService.findByEmail(dto.getEmail()) != null) {
				throw new Exception("O email informado j� encontra-se cadastrado. Tente outro.");
			}
			
			Usuario usuario = new Usuario();
			
			usuario.setNome(dto.getNome());
			usuario.setEmail(dto.getEmail());
			usuario.setSenha(dto.getSenha());
			
			usuarioService.createOrUpdate(usuario); //gravando no banco de dados..
			
			map.addAttribute("mensagem_sucesso", "Usu�rio " + usuario.getNome() + ", cadastrado com sucesso.");
		}
		catch(Exception e) {
			map.addAttribute("mensagem_erro", e.getMessage());
		}		
		
		modelAndView.addObject("register", new RegisterDTO());

		return modelAndView;
	}
	
	@RequestMapping("/logout")
	public ModelAndView logout(HttpServletRequest request) {
		
		//remover os dados da sess�o
		request.getSession().removeAttribute("usuario_autenticado");
		request.getSession().invalidate();
		
		ModelAndView modelAndView = new ModelAndView("redirect:/");
		return modelAndView;
	}
}






