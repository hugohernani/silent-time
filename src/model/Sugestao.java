package model;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.text.format.Formatter;
import android.util.Log;

public class Sugestao implements Comparable<Sugestao>{
	
	private Long id;
	private Integer tempoEspera;
	private Integer repeticao;
		
	public Sugestao(Long id, Integer tempoEspera, Integer repeticao) {
		this.id = id;
		this.tempoEspera = tempoEspera;
		this.repeticao = repeticao;
	}

	public Sugestao(Integer tempoEspera) {
		this.tempoEspera = tempoEspera;
		this.repeticao = 1;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getTempoDeEspera() {
		return tempoEspera;
	}

	public void setTime(Integer tempoDeEspera) {
		this.tempoEspera = tempoDeEspera;
	}

	public Integer getRepeticao() {
		return repeticao;
	}

	public void setRepeticao(Integer repeticao) {
		this.repeticao = repeticao;
	}
	
	public Date getDataByTime(){
		Date data = new Date(Integer.valueOf(tempoEspera));
		return data;
	}
	
	public String getDiasRestantes() {
		
		SimpleDateFormat format = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", new Locale("pt-br"));
		Calendar calendarSugestao = new GregorianCalendar();
		calendarSugestao.setTimeInMillis(Long.valueOf(tempoEspera));
		
		String resposta = "+ " + tempoEspera + " dias";
		
		Calendar calendarAtual = new GregorianCalendar();
		calendarAtual.add(Calendar.DAY_OF_MONTH, tempoEspera);
		
		resposta += (" | Data: " + format.format(calendarAtual.getTime()));

		return resposta;
	}

	@Override
	public int compareTo(Sugestao another) {
		int comparacao = this.repeticao.compareTo(another.repeticao);
		if (comparacao != 0) return comparacao;
		return this.tempoEspera.compareTo(another.tempoEspera);
	}

}
