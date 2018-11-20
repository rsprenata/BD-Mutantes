package com.example.rsprenata.bd_mutantes;

import java.util.List;

public class Mutante {
	private Integer id;
	private String nome;
	private List<String> habilidades;

	public Mutante() {}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<String> getHabilidades() {
		return this.habilidades;
	}

	public void setHabilidades(List<String> habilidades) {
		this.habilidades = habilidades;
	}
}
