package utils;

import ia.hugo.silencetime.R;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import persistencia.AgendamentoSilenciosoDao;

import model.Agendamento;

import android.app.NotificationManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AgendamentosAdapter extends BaseAdapter {

	private Context mContext;
	private List<Agendamento> mAgendamentos;
	AgendamentoSilenciosoDao dao;

	
	public AgendamentosAdapter(Context context, List<Agendamento> agendamentos){
		this.mContext = context;
		this.mAgendamentos = agendamentos;
		dao = new AgendamentoSilenciosoDao(context);
	}
	
	@Override
	public int getCount() {
		return mAgendamentos.size();
	}

	@Override
	public Agendamento getItem(int position) {
		return mAgendamentos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return mAgendamentos.get(position).getId();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		View v = convertView;
		if (v == null){
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.item_lista_agendamento, null);
		}
		
		final Agendamento agendamento = mAgendamentos.get(position);
		
		Button btApagar = (Button) v.findViewById(R.id.btApagarItemAgendamentos);
				
		String titulo = agendamento.getTitulo();
		if (!titulo.isEmpty()){
			TextView tvTitulo = (TextView) v.findViewById(R.id.tvTituloItemAgendamentos);
			tvTitulo.setText(titulo);
		}

		String descricao = agendamento.getDescricao();
		if (descricao != null){
			if (!descricao.isEmpty()){
				TextView tvDescricao = (TextView) v.findViewById(R.id.tvDescricaoItemAgendamentos);
				tvDescricao.setText(descricao);

			}
		}
		
		String dataInicial = agendamento.getDataLongaInicio();
		if (!dataInicial.isEmpty()){
			TextView tvDataInicial = (TextView) v.findViewById(R.id.tvDataInicialItemAgendamentos);
			tvDataInicial.setText(dataInicial);
		}
		
		String dataFinal = agendamento.getDataLongaFim();
		if (!dataFinal.isEmpty()){
			TextView tvDataFinal = (TextView) v.findViewById(R.id.tvDataFinalItemAgendamentos);
			tvDataFinal.setText(dataFinal);
		}
		
		btApagar.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				NotificationManager notificationManager = (NotificationManager)
						mContext.getSystemService(Context.NOTIFICATION_SERVICE);
				
				if (dao.delete(agendamento)){
					notificationManager.cancel(agendamento.getId_evento().intValue()
							+ agendamento.getInicio().intValue());
					
					notificationManager.cancel(agendamento.getId_evento().intValue()
							+ agendamento.getFim().intValue());
					
					mAgendamentos.remove(position);
					notifyDataSetChanged();
					Toast.makeText(mContext, "Apagado(s)", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(mContext, "N�o apagado(s)", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
		
		return v;
	}
	
	public boolean deleteAll(){
		if (dao.deleteAll()){
			NotificationManager notificationManager = (NotificationManager)
					mContext.getSystemService(Context.NOTIFICATION_SERVICE);
			
			for (Agendamento agendamento : mAgendamentos) {
				notificationManager.cancel(agendamento.getId_evento().intValue()
						+ agendamento.getInicio().intValue());
				
				notificationManager.cancel(agendamento.getId_evento().intValue()
						+ agendamento.getFim().intValue());
			}
			
			mAgendamentos.clear();
			notifyDataSetChanged();
			return true;
		}
		return false;
	}

}
