package model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Agendamento implements Comparable<Agendamento>{
	
	private Long id;
	private String titulo;
	private String descricao;
	private Long inicio;
	private Long fim;
	private Integer peso;
	private Long id_evento;
	
	public Agendamento(){
		this.id = -1L;
		this.id_evento = -1L;
		this.titulo = "";
		this.descricao = "";
		this.inicio = -1L;
		this.fim = -1L;
		this.peso = -1;
	}
		
	public Agendamento(String titulo, String descricao,
			Long inicio, Long fim, Long id_evento) {
		this.titulo = titulo;
		this.descricao = descricao;
		this.inicio = inicio;
		this.fim = fim;
		this.peso = 1;
		this.id_evento = id_evento;
	}

	public Agendamento(Long id, String titulo, String descricao,
			Long inicio, Long fim, Integer peso, Long id_evento) {
		this.id = id;
		this.titulo = titulo;
		this.descricao = descricao;
		this.inicio = inicio;
		this.fim = fim;
		this.peso = peso;
		this.id_evento = id_evento;
	}
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Long getInicio() {
		return inicio;
	}

	public void setInicio(Long inicio) {
		this.inicio = inicio;
	}

	public Long getFim() {
		return fim;
	}

	public void setFim(Long fim) {
		this.fim = fim;
	}

	public Integer getPeso() {
		return peso;
	}

	public void setPeso(Integer peso) {
		this.peso = peso;
	}
	
	public Long getId_evento() {
		return id_evento;
	}

	public void setId_evento(Long id_evento) {
		this.id_evento = id_evento;
	}

	@Override
	public String toString() {
		return "Titulo: " + titulo + ", " + "Inicia em: " + getData(inicio) +
				", Termina em: " + getData(fim);
	}
	
	public String getData(Long data){
		Date date = new Date(data);
		DateFormat df = java.text.DateFormat.getDateInstance(DateFormat.SHORT, new Locale("pt,br"));
		return df.format(date);
	}
	
	public String getDataLongaInicio(){
		Date date = new Date(inicio);
		SimpleDateFormat format = new SimpleDateFormat("dd (EE), MMMM. HH:mm", new Locale("pt-br"));
		return format.format(date);
	}
	
	public String getDataLongaFim(){
		Date date = new Date(fim);
		SimpleDateFormat format = new SimpleDateFormat("dd (EE), MMMM. HH:mm", new Locale("pt-br"));
		return format.format(date);
	}

	@Override
	public int compareTo(Agendamento another) {
		int comparacao = this.inicio.compareTo(another.getInicio());
		if (comparacao != 0) return comparacao;
		return this.fim.compareTo(another.getFim());
	}
}