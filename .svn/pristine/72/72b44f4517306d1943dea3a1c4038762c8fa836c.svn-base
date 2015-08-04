package utils;

import ia.hugo.silencetime.R;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import model.Agendamento;
import model.TempoAgendamentoSugestao;
import persistencia.AgendamentoSilenciosoDao;
import persistencia.TASDao;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TASAdapter extends BaseAdapter {

	private Context mContext;
	private List<TempoAgendamentoSugestao> mTas;
	
	public TASAdapter(Context context, List<TempoAgendamentoSugestao> Tas){
		this.mContext = context;
		this.mTas = Tas;
	}
	
	@Override
	public int getCount() {
		return mTas.size();
	}

	@Override
	public TempoAgendamentoSugestao getItem(int position) {
		return mTas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return mTas.get(position).getId();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		View v = convertView;
		if (v == null){
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.item_lista_sugestoes_agendamento, null);
		}
		
		final TempoAgendamentoSugestao tas = mTas.get(position);
		
		String titulo = tas.getTitulo();
		if (titulo != null){
			if (!titulo.isEmpty()){
				TextView tvTitulo = (TextView) v.findViewById(R.id.tvTituloTAS);
				tvTitulo.setText(titulo);
			}
		}
		
		String descricao = tas.getDescricao();
		if (descricao != null){
			if (!descricao.isEmpty()){
				TextView tvDescricao = (TextView)
						v.findViewById(R.id.tvDescricaoTAS);
				tvDescricao.setText(descricao);
			}
		}
		
		Integer diaDaSemana = tas.getDiaDaSemana();
		if (diaDaSemana != null){
			if (diaDaSemana != -1){
				TextView tvDiaDaSemana = (TextView)
						v.findViewById(R.id.tvDiaDaSemanaTAS);
				
				tvDiaDaSemana.setText(getDiaDaSemana(diaDaSemana));
			}
		}
		
		Integer horas = tas.getHora();
		Integer minutos = tas.getMinuto();
		
		if (horas != null && minutos != null){
			if (horas != -1 && minutos != -1){
				TextView tvTempo = (TextView)
						v.findViewById(R.id.tvTempoTAS);
				tvTempo.setText(horas+":"+minutos);
			}
		}
		
		return v;
	}

	/*
	 * Retorna o nome do dia da semana dado um número que o
	 * represente.
	 */
	private CharSequence getDiaDaSemana(Integer diaDaSemana) {
		String dia;
		
		switch (diaDaSemana) {
		case Calendar.SUNDAY:
			dia = "Domingo";
			break;
		case Calendar.MONDAY:
			dia = "Segunda";
			break;
		case Calendar.TUESDAY:
			dia = "Terça";
			break;
		case Calendar.WEDNESDAY:
			dia = "Quarta";
			break;
		case Calendar.THURSDAY:
			dia = "Quinta";
			break;
		case Calendar.FRIDAY:
			dia = "Sexta";
			break;
		case Calendar.SATURDAY:
			dia = "Sábado";
			break;

		default:
			dia = "DIA";
			break;
		}
		
		return dia;
	}
	
	public void clear(){
		mTas.clear();
		notifyDataSetChanged();
	}

}
