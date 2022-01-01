package br.com.cotiinformatica.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Integer idUsuario;
	
	@Column
	private String nome;
	
	@Column(unique = true)
	private String email;
	
	@Column
	private String senha;

	// construtor default (vazio)
	public Usuario() {
		// TODO Auto-generated constructor stub
	}

	// sobrecarga de construtores (overloading)
	public Usuario(Integer idUsuario, String nome, String email, String senha) {
		super();
		this.idUsuario = idUsuario;
		this.nome = nome;
		this.email = email;
		this.senha = senha;
	}

	public Integer getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	// Sobrescrita (OVERRIDE) do m�todo toString
	@Override
	public String toString() {
		return "Usuario [idUsuario=" + idUsuario + ", nome=" + nome + ", email=" + email + ", senha=" + senha + "]";
	}

	// Sobrescrita (OVERRIDE) do m�todo equals
	@Override
	public boolean equals(Object obj) {

		if (obj instanceof Usuario) {
			Usuario usuario = (Usuario) obj;
			return this.idUsuario.equals(usuario.getIdUsuario());
		}

		return false;
	}

	// Sobrescrita do m�todo hashCode
	@Override
	public int hashCode() {
		return this.idUsuario.hashCode();
	}
}
