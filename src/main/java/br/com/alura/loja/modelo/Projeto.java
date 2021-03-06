package br.com.alura.loja.modelo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.gson.Gson;
import com.thoughtworks.xstream.XStream;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Projeto {

	private String nome;
	
	private long id;
	
	private int anoDeInicio;

	public Projeto() {
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public Projeto(long id, String nome, int anoDeInicio) {
		this.nome = nome;
		this.id = id;
		this.anoDeInicio = anoDeInicio;
	}
	
	public String toXML(){
		return new XStream().toXML(this);
	}

	public String getNome() {
		return this.nome;
	}
	
	public int getAnoDeInicio() {
		return anoDeInicio;
	}

	public long getId() {
		return id;
	}

	public String toJSon() {
		return new Gson().toJson(this);
	}
	
}
