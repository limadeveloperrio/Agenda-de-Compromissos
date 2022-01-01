package br.com.cotiinformatica.services;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.cotiinformatica.entities.Compromisso;
import br.com.cotiinformatica.interfaces.ICompromissoRepository;

@Service //classe de servi�o do spring
@Transactional //capaz de executar transa��es em BD
public class CompromissoService {

	/*
	 * Inje��o de depend�ncia do repositorio que ser� utilizado.
	 * O Spring inicializa o repositorio
	 */
	
	@Autowired
	private ICompromissoRepository compromissoRepository;
	
	//m�todo para gravar ou atualizar um compromisso
	public void createOrUpdate(Compromisso compromisso) throws Exception {
		compromissoRepository.save(compromisso);
	}
	
	//m�todo para excluir um compromisso
	public void delete(Compromisso compromisso) throws Exception {
		compromissoRepository.delete(compromisso);
	}
	
	//m�todo para consultar todos os compromissos
	public List<Compromisso> findAll() throws Exception {
		return (List<Compromisso>) compromissoRepository.findAll();
	}
	
	//m�todo para retornar 1 unico compromisso atraves do id
	public Compromisso findById(Integer id) throws Exception {
		return compromissoRepository.findById(id).get();
	}
	
	//m�todo para executar a consulta de compromissos por periodo de data e usuario
	public List<Compromisso> find(Integer idUsuario, Date dataMin, Date dataMax) throws Exception {
		return compromissoRepository.find(idUsuario, dataMin, dataMax);
	}
	
	//m�todo para executar a consulta de compromissos de um usuario a partir de uma data
	public List<Compromisso> find(Integer idUsuario, Date data) throws Exception{
		return compromissoRepository.find(idUsuario, data);
	}
}









